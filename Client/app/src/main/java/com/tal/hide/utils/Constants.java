package com.tal.hide.utils;

public class Constants {
    //keeps all commands and constants\
    public static final String SERVER = "ws://172.16.40.66:5555"; //server url

    public static final String keyCommand = "command"; //key command
    public static final String keyUser = "username"; // key user
    public static final String keyGameId = "gameid";//key game ID
    public static final String keyMove = "move"; //key move
    public static final String keyUUID = "uuid"; //key uuid
    public static final String keyOpponent = "opponent"; //key opponent
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

    public static boolean paired = false;
}
