package com.tal.hide;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tal.hide.ui.game.GameFragment;
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
        //get instance of webClient
        webClient = WebClient.getInstance();
        webClient.setListener(this);
        Log.d(TAG,"on Create : Listener set");
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
                    args.putString(Constants.keyOpponent, webClient.getCurrentGame().getRole());
                    args.putString(Constants.keyGameId, webClient.getCurrentGame().getRole());
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
        //create new dispatcher
        Dispatcher dispatcher = new Dispatcher();
        //put data on new loading fragment
        Loading ld = Loading.newInstance();
        Bundle bd = new Bundle();
        bd.putString("message","Awaiting Result!");
        ld.setArguments(bd);
        //show new loading fragment
        transaction.replace(R.id.container, ld, "LoadFrag");
        transaction.commit();

        if(move) {
            //send the move
            webClient.send(dispatcher.getPlay("true"));
        }else {
            //send the move
            webClient.send(dispatcher.getPlay("false"));
        }
    }

    @Override
    public void result(final boolean resultSuccess) {
        //if result has come
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //get loading fragment and remove it
                Loading resultLoad = (Loading)getSupportFragmentManager().findFragmentByTag("LoadFrag");
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().remove(resultLoad);
                transaction.replace(R.id.container, GameFragment.newInstance(), "ResultFrag");
                transaction.commit();
            }
        });
    }
}
