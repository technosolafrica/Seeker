package com.tal.hide.utils;

public interface ConnectionListener {
    /**
     * Listens for connection drops or errors and sends callback.
     * Callback sent to MainActivity (originator) for reconnection
     */
    void reconnect();
    void alertConn(String conn);
//    void connected();
}
