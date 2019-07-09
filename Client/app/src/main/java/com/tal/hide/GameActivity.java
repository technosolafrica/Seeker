package com.tal.hide;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tal.hide.ui.game.GameFragment;
import com.tal.hide.ui.game.Loading;
import com.tal.hide.utils.Constants;
import com.tal.hide.utils.PairingListener;

public class GameActivity extends AppCompatActivity implements PairingListener {
    Loading loadFrag;
    FragmentTransaction loader;
    WebClient webClient;
    final private String TAG = "Knowingly GameActivity ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        if (savedInstanceState == null) {
            //put loading spinner fragment
            loadFrag = Loading.newInstance();
            loader = getSupportFragmentManager().beginTransaction();
            loader.replace(R.id.container, loadFrag);
            loader.commitNow();
        }
        //now send ready to server
        webClient = WebClient.getInstance();
        webClient.setListener(this);
        Log.d(TAG," Listener set");
        //get username from intent
        String user = getIntent().getStringExtra(Constants.keyUser);
        //send the ready command
        webClient.sendReady(user);
        Log.d(TAG,"on Create");
    }

    @Override
    public void pair(final boolean success) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(success){
                    Bundle args = new Bundle();
                    //put game data on game fragment
                    args.putString(Constants.keyRole, webClient.getCurrentGame().getRole());
                    args.putString(Constants.keyOpponent, webClient.getCurrentGame().getOpponent());
                    args.putInt(Constants.keyGameId, webClient.getCurrentGame().getGameid());
                    //create game fragment and set data
                    GameFragment gameFragment = GameFragment.newInstance();
                    gameFragment.setArguments(args);
                    //load the fragment
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().remove(loadFrag);
                    transaction.replace(R.id.container, gameFragment,"GameFrag");
                    transaction.commit();
                    Log.d(TAG,"pairing true: detach fragment : ");
                }else{
                    //remove the loading progress bar
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().remove(loadFrag);
                    transaction.commit();
                    //alert failure
                    Toast failedPairing = Toast.makeText(getApplicationContext(), " Fail to find pair ", Toast.LENGTH_LONG);
                    failedPairing.show();
                    //end game
                    finish();
                    Log.d(TAG,"pairing false: detach fragment ");
                }
            }
        });
    }

    public void submit(boolean move){
        //when user submits in fragment
        GameFragment fraggy = (GameFragment)getSupportFragmentManager().findFragmentByTag("GameFrag");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().remove(fraggy);
        //put data on new loading fragment
        Loading ld = Loading.newInstance();
        Bundle bd = new Bundle();
        bd.putString("message","Awaiting "+webClient.getCurrentGame().getOpponent()+"'s Move!");
        ld.setArguments(bd);
        //show new loading fragment
        transaction.replace(R.id.container, ld, "LoadFrag");
        transaction.commit();

        if(move) {
            //send the move
            webClient.sendThis(webClient.dispatcher.getPlay(true));
        }else {
            //send the move
            webClient.sendThis(webClient.dispatcher.getPlay(false));
        }
    }

    @Override
    public void onBackPressed() {
        //if user clicks back after attempting to pair
        Log.d(TAG, " Back Pressed ");
        //send eject
        webClient.sendThis(webClient.dispatcher.getEject());
        //call super to exec
        super.onBackPressed();
    }

    @Override
    public void result(final boolean resultSuccess) {
        //if result has come
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //if the result was successful then show result screen win or loose
                if(resultSuccess){
                    //get loading fragment and remove it
                    Loading resultLoad = (Loading)getSupportFragmentManager().findFragmentByTag("LoadFrag");
                    getSupportFragmentManager().beginTransaction().remove(resultLoad);
                    //put winning data
                    Intent theGameResultIntent = getIntent();
                    theGameResultIntent.putExtra(Constants.keyOutcome, webClient.dispatcher.currentGame.isWin());
                    //end activity
                    setResult(Activity.RESULT_OK, theGameResultIntent);
                    finish();
                }else{
                    //if result = false then no result sent so finish
                    setResult(Activity.RESULT_CANCELED);
                    //end activity
                    finish();
                }
            }
        });
    }
}
