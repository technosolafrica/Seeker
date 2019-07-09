package com.tal.hide;

import android.util.Log;

import com.tal.hide.classes.Game;
import com.tal.hide.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class Dispatcher {
    final String TAG = "Knowingly Dispatcher";
    Game currentGame;

    /**
     * All senders return a
     * @param identifier
     * @return object
     */

    public String getIdentifier(String identifier){
        //send identifier on connection successful
        JSONObject object = new JSONObject();
        try {
            object.put(Constants.keyCommand, Constants.cmdSetUuid);
            object.put(Constants.keyUUID, identifier);
        }catch (JSONException jse){
            jse.printStackTrace();
            Log.d(TAG," JSON Fail fail");
        }
        Log.d(TAG," send uuid : "+object);
        return object.toString();
    }

    public String getReadyJson(String username){
        //sends ready command once user presses play
        JSONObject object = new JSONObject();
        try {
            object.put(Constants.keyCommand,Constants.cmdReady);
            object.put(Constants.keyUser, username);
        }catch (JSONException jse){
            jse.printStackTrace();
            Log.d(TAG," JSON Fail fail");
        }
        Log.d(TAG," send ready : data : "+object.toString());
        return object.toString();
    }

    public Game onPair(JSONObject pairResult, String username){
        Log.d(TAG, " on pair success :" + pairResult.toString());
        try{
            //once pairs received : start game
            String opponent = pairResult.getString(Constants.keyOpponent);
            String role = pairResult.getString(Constants.keyRole);
            int gameid = pairResult.getInt(Constants.keyGameId);
            Log.d(TAG, " your role is :" + role);
            //create game instance
            currentGame = new Game(username, role, gameid, opponent, true);
            Log.d(TAG, " game ::"+currentGame.toString());
            //return currentGame;
        }catch (JSONException jse) {
            Log.d(TAG, " on pair : game failed ! json :" + jse.getMessage());
        }
        return currentGame;
    }

    public void onPairFail(){
        //do something
        Log.d(TAG, " on pair : pair failed! ");
    }

    public String getPlay(Boolean moveAction){
        //a play is triggered when user submits heads or tails
        JSONObject object = new JSONObject();
        try {
            object.put(Constants.keyCommand, Constants.cmdSubmit);
           if(currentGame != null){
               //set current game move
               currentGame.setMove(moveAction);
               //submit this game
               object.put(Constants.keyGameId, currentGame.getGameid());
               object.put(Constants.keyRole, currentGame.getRole());
               object.put(Constants.keyMove, currentGame.getMove());
               Log.d(TAG," submit play : "+object);
           }else{
               Log.d(TAG," current game null : ");
           }
        }catch (JSONException jse){
            jse.printStackTrace();
        }
        return object.toString();
    }

    public void onResult(JSONObject result){
        try {//if we have a final result, get win status
            boolean win = result.getBoolean(Constants.keyOutcome);
            //set current game win status
            currentGame.setWin(win);
        }catch (JSONException jse){
            jse.printStackTrace();
        }
    }

    public void onPending(){
        //do something
    }

    public void onNoResult(JSONObject reason){
        try{
            String wtf = reason.getString(Constants.keyReason);
            Log.d(TAG, " No result ::: "+ wtf);
        }catch (JSONException jse){
            jse.printStackTrace();
        }
    }

    public String getEject(){
        //a play is triggered when user submits heads or tails
        JSONObject object = new JSONObject();
        try {
            object.put(Constants.keyCommand, Constants.cmdEject);
        }catch (JSONException jse){
            jse.printStackTrace();
        }
        return object.toString();
    }
}