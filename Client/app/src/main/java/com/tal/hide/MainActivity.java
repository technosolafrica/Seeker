package com.tal.hide;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tal.hide.utils.ConnectionListener;
import com.tal.hide.utils.Constants;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements ConnectionListener {
    private final static String TAG = "Knowingly Main ";
    private WebClient webClient;
    private EditText ed_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set theme to show splashscreen
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init edit text field
        ed_user = (EditText) findViewById(R.id.ed_user);
        //connect to server
        connectWebSocket();
        Log.d(TAG, "on create ");
    }

    public void connectWebSocket() {
        //create new Websocket
        webClient = new WebClient(getURI(), getApplicationContext());
        //set the account name as user ID
        webClient.setIdentifier(getAccountName());
        //set the connection listener
        webClient.setConn(this);
        //connect the socket
        webClient.connect();
    }

    @Override
    public void alertConn(final String reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, reason, Toast.LENGTH_LONG).show();
                //if(!reason.contains("Connected!")) finish(); //if not connected then finish
            }
        });
    }

    public void onReady(View v) {
        //when user clicks play
        String username = ed_user.getText().toString();
        if (webClient != null && !webClient.isClosed()) {
            //open the game activity
            Intent gameIntent = new Intent(this, GameActivity.class);
            //put username on intent
            gameIntent.putExtra(Constants.keyUser, username);
            startActivityForResult(gameIntent, Constants.REQUEST_GAME_CODE);
            //get username and dispatch to server
            //webClient.sendReady(username);
            //calls progress spinner asynctask
            //webClient.loading(getApplicationContext());
        } else {
            //Toast.makeText(this, " Connection Failure ", Toast.LENGTH_LONG).show();
            //try reconnect
            reconnect();
        }
    }

    @Override
    public void reconnect() {
        //reconnect if connection closed
        if (webClient != null) {
            //attr cannot be reused so completely destroy
            webClient = null;
        }
        connectWebSocket();
    }

    public String getAccountName() {
        String email = "default@tal.app";
        //get unique account name to server as user ID
        AccountManager accountManager = AccountManager.get(getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType("com.google");
        //match email pattern
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        //iterate through accounts store on client device
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                email = account.name;//store email from first account
                break;//if email received then break
            } else {
                email = account.name;
                Log.d(TAG, " match fail account name : " + account.name);
            }
        }
        Log.d(TAG, " email : " + email);
        return email;
        /*Intent getAccountIntent = AccountManager.newChooseAccountIntent(null, null,
                new String[]{"com.google","com.google.android.legacymap"}, false, null, null, null, null);
        startActivityForResult(getAccountIntent, REQUEST_ACCOUNT_CODE); */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String identifier;
        //get the account name from the chosen account
        if (requestCode == Constants.REQUEST_ACCOUNT_CODE) {
            if (resultCode == RESULT_OK) {
                identifier = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            } else if (resultCode == RESULT_CANCELED) {
                //if user cancels then use device id
                identifier = Settings.Secure.getString(getContentResolver(), Settings.Secure._ID);
            } else identifier = null;
        } else if (requestCode == Constants.REQUEST_GAME_CODE) {
            //if reply is from game intent
            if (resultCode == RESULT_OK) {
                //then get result win or loose from game
                boolean win = data.getBooleanExtra(Constants.keyOutcome, false);
                //toast user result
                if (win) {
                    Toast.makeText(MainActivity.this, " You Won! ", Toast.LENGTH_LONG).show();
                    //play sound
                    MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI).start();
                }

                else {
                    Toast.makeText(MainActivity.this, " You Lost! ", Toast.LENGTH_LONG).show();
                    //play sound
                    MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI).start();
                }
            } else if (requestCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, " Game Ended ", Toast.LENGTH_LONG).show();
            }
        }
    }

    void showPopUp(){
        //Show the pop up of Winner
        PopupWindow popUp = new PopupWindow();
//        popUp.showAtLocation();
    }

    URI getURI(){
        /**
         * Get URI from URL
         */
        try{
            return new URI(Constants.SERVER);//change URL to URI
        }catch (URISyntaxException urie){
            urie.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //close socket on destroy
        webClient.close();
    }
}
