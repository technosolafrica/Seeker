package com.tal.hide.utils;

public class Constants {
    //keeps all commands and constants\
    public static final String SERVER = "ws://172.16.40.105:5000"; //server url

    public final static int REQUEST_ACCOUNT_CODE = 123;
    public final static int REQUEST_GAME_CODE = 129;

    public static final String keyCommand = "command"; //key command
    public static final String keyUser = "username"; // key user
    public static final String keyGameId = "gameid";//key game ID
    public static final String keyOpponent = "opponent"; //key opponent
    public static final String keyMove = "move"; //key move
    public static final String keyUUID = "uuid"; //key uuid
    public static final String keyRole = "role"; //key role
    public static final String keyOutcome = "outcome"; //key outcome
    public static final String keyReason = "reason"; //key outcome

    public static final String cmdSetUuid = "cmd_setuuid";//send UUID on connection
    public static final String cmdReady = "cmd_ready";// send when user clicks play  username
    public static final String cmdPairingTimeout = "cmd_pairingtimeout";//receive if pairing failed
    public static final String cmdPairResult = "cmd_pairresult";//receive if pairing success result
    public static final String cmdSubmit = "cmd_submit";//send if user submits move
    public static final String cmdPending = "cmd_pending";// receive if player has played
    public static final String cmdResult = "cmd_result";//receive result if game success
    public static final String cmdNoResult = "cmd_noresult";//receive if other player timed out or diconnects
    public static final String cmdEject = "cmd_eject";//send when user clicks back button

    public static boolean paired = false;
}
