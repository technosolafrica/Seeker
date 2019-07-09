package com.tal.hide;

import android.content.Context;
import android.util.Log;

import com.tal.hide.classes.Game;
import com.tal.hide.utils.ConnectionListener;
import com.tal.hide.utils.Constants;
import com.tal.hide.utils.PairingListener;

import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.json.JSONException;
import org.json.JSONObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebClient extends WebSocketClient{
    static Dispatcher dispatcher = new Dispatcher();
    final String TAG = "Knowingly WebClient ";

    String identifier;
    private Context context;

    PairingListener listener;
    ConnectionListener conn;

    private Game currentGame;

    //Singleton
    private static WebClient client = null;

    public static WebClient getInstance(){
        return client;
    }

    public WebClient(URI serverUri, Context context) {
        super(serverUri);
        client = this;
        this.context = context;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // on connection : get account id and send
        //if user ID valid then send
        this.send(dispatcher.getIdentifier(identifier));
        Log.d(TAG, "on open send user ID : "+identifier);
        conn.alertConn("Connected!");
    }

    @Override
    public void onMessage(String message) {
        try {
            //get message and check command first
            JSONObject jsonMsg = new JSONObject(message);
            //determine command
            switch (jsonMsg.getString(Constants.keyCommand)) {
                case Constants.cmdPairingTimeout:
                    //if pairing fails
                    dispatcher.onPairFail();
                    listener.pair(false);
                    Log.d(TAG, "pairing timeout ");
                    break;
                case Constants.cmdPairResult:
                    //if pairing success
                    currentGame = dispatcher.onPair(jsonMsg, identifier);
                    Log.d(TAG, " curr game :" + currentGame.toString());
                    //run on ui
                    listener.pair(true);
                    break;
                case Constants.cmdResult:
                    //if result successful
                    dispatcher.onResult(jsonMsg);
                    //listen for result
                    listener.result(true);
                    break;
                case Constants.cmdPending:
                    //if other player played
                    dispatcher.onPending();
                    break;
                case Constants.cmdNoResult:
                    //if no result
                    dispatcher.onNoResult(jsonMsg);
                    //listen for failed result
                    listener.result(false);
                    break;
                default:
                    Log.d(TAG," message: "+jsonMsg.getString(Constants.keyCommand));
                    break;
            }
        }catch (JSONException jse){
            jse.printStackTrace();
            Log.d(TAG, "json parse fail : "+message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.d(TAG, "Connection Closed: "+reason);
        conn.alertConn("Connection Closed: ");
    }

    @Override
    public void onError(Exception ex) {
        conn.alertConn("Connection Error:  "+ex.getMessage());
        Log.d(TAG, "Connection Error: "+ex.getMessage());
        ex.printStackTrace();
        conn.reconnect();
    }

    public void setIdentifier(String identifier) {
        //sets user ID from main activity
        this.identifier = identifier;
    }

    public void sendReady(String username){
        //triggers dispatcher to send ready
        this.sendThis(dispatcher.getReadyJson(username));
    }

    public void sendThis(String data){
        //checks if connection still on
        if(this.isClosed()){
            //conn.reconnect();//will never be reached
            Log.d(TAG," reconnecting ....... ");
        }else{
            try{
                this.send(data);
            }catch (WebsocketNotConnectedException notConnected){
                conn.alertConn(notConnected.getMessage());
            }catch (IllegalStateException els){
                conn.alertConn(els.getMessage());
            }
        }
    }

    public void setListener(PairingListener listener) {
        this.listener = listener;
    }

    public void setConn(ConnectionListener conn) {
        this.conn = conn;
    }

    public Game getCurrentGame() {
        return currentGame;
    }
}
