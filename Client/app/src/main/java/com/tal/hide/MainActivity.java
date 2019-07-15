package com.tal.hide;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private TextView txtUser;
    private ConstraintLayout ltMain;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set theme to show splashscreen
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init text field
        txtUser = findViewById(R.id.txt_user);
        //connect to server
        connectWebSocket();
        //initialize constraint layout main
        ltMain = findViewById(R.id.lt_main);

        //instantiate the tool bar
        //Toolbar topToolbar = findViewById(R.id.toolbar_top);
        //setSupportActionBar(topToolbar);

        //shared prefs
        playerName = sharedPrefs(); //test
        txtUser.setText(playerName);
        //log
        Log.d(TAG, " on create ");
    }

    public String sharedPrefs(){
        //setup shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //get editor
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        //register listener
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                //
                if(key.equals("key_display_name")){
                    playerName = sharedPreferences.getString("key_display_name", "Noobie");
                    editor.putString("key_display_name", playerName);
                    editor.apply();
                    txtUser.setText(playerName);
                }
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        Log.d(TAG, "on shared prefs name ");
        return sharedPreferences.getString("key_display_name", "Noobie");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //put menu item
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings://start settings activity on click settings
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivityForResult(settingsIntent, Constants.REQUEST_SETTING_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        if (webClient != null && !webClient.isClosed()) {
            //open the game activity
            Intent gameIntent = new Intent(this, GameActivity.class);
            //put username on intent
            gameIntent.putExtra(Constants.keyUser, playerName);
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
                    //show popup
                    showPopUp(getResources().getString(R.string.win),R.drawable.ff3);
                    //play sound
                    MediaPlayer.create(this, R.raw.goalpass).start();
                }

                else {
                    Toast.makeText(MainActivity.this, " You Lost! ", Toast.LENGTH_LONG).show();
                    //show popup
                    showPopUp(getResources().getString(R.string.lose),R.drawable.ff7);
                    //play sound
                    MediaPlayer.create(this, R.raw.quaquafail).start();
                }
            } else if (requestCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, " Game Ended ", Toast.LENGTH_LONG).show();
            }
        }
    }

    void showPopUp(String result, int imageId){
        //instantiate popup layout
        LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.activity_result_pop_up, null);
        //instantiate popup window
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //instantiate textview
        TextView txtResult = popupView.findViewById(R.id.txt_rez);
        //instantiate button
        ImageButton clsPopUp = popupView.findViewById(R.id.im_result);
        //set either win or lose image
        clsPopUp.setBackgroundResource(imageId);
        //clsPopUp.setImageResource(getResources().getIdentifier("com.tal.hide:drawable/"+imageName, null, null));
        //set either win or lose text
        txtResult.setText(result);
        //display popup
        popupWindow.showAtLocation(ltMain, Gravity.CENTER, 0,0);
        //set on close listener
        clsPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
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
        //unregister shared prefs listener
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(listener);
    }
}
