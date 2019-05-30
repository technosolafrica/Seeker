package com.tal.hide;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tal.hide.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    final String TAG = "Knowingly Dispatcher";
    private Game currentGame;

    public String sendIdentifier(String identifier){
        //send identifier on connection successful
        String data = "{'command':'what'}";
//        Map<String, String> identMap = new HashMap<>();
//        identMap.put(Constants.keyCommand,Constants.cmdSetUuid);
//        identMap.put(Constants.keyUUID,identifier);
//        data = getJson(identMap);
        try {
            JSONObject object = new JSONObject();
            object.put(Constants.keyCommand, Constants.cmdSetUuid);
            object.put(Constants.keyUUID, identifier);
            data = object.toString();
        }catch (JSONException jse){
            jse.printStackTrace();
            Log.d(TAG," JSON Fail fail");
        }
        Log.d(TAG," send uuid : "+data);
        return data;
    }

    public String getReadyJson(String username){
        //sends ready command once user presses play
        String data;
        Map<String, String> map = new HashMap<>();
        map.put(Constants.keyCommand,Constants.cmdReady);
        map.put(Constants.keyUser, username);
        data = getJson(map);
        Log.d(TAG," send ready : data : "+data);
        return data;
    }

    public Game onPair(JSONObject pairResult, String username){
        Log.d(TAG, " on pair success :" + pairResult.toString());
        try{
            //once pairs received : start game
            String opponent = pairResult.getString(Constants.keyOpponent);
            String role = pairResult.getString(Constants.keyRole);
            String gameid = pairResult.getString(Constants.keyGameId);
            Log.d(TAG, " your role is :" + role);
            //create game instance
            currentGame = new Game(username, role, gameid, opponent, true);
            return currentGame;
        }catch (JSONException jse) {
            Log.d(TAG, " on pair : game failed ! json :" + jse.getMessage());
        }

        return null;
    }

    public void onPairFail(){
        //do something
        Log.d(TAG, " on pair : pair failed! ");
    }

    public String getPlay(String moveAction){
        //a play is triggered when user submits heads or tails
        String data;
        Map<String, String> map = new HashMap<>();
        //put data on map
        map.put(Constants.keyCommand, Constants.cmdSubmit);
        //get game
        if(currentGame!= null){
            if(currentGame.isRunning()){
                //set current game move
                currentGame.setMove(moveAction);
                //submit this game
                map.put(Constants.keyGameId, currentGame.getGameid());
                map.put(Constants.keyUUID, currentGame.getUuid());
                map.put(Constants.keyMove, moveAction);
                //get json
                data = getJson(map);
                Log.d(TAG," submit play : move : "+moveAction);
                return data;
            }
        }
        return null;
    }

    public void onResult(){
        //do something
    }

    public void onPending(){
        //do something
    }

    public void onNoResult(){
        //do something
    }

    public String getJson(Map<String, String> map){
        Gson gson = new GsonBuilder().create();
        String jsonData = gson.toJson(map);
        Log.d(TAG," outgoing Json : "+jsonData);
        return jsonData;
    }
}
