package com.tal.hide.utils;

public interface PairingListener {
    /**
     * This listener listens for pairing from server and executes
     * methods to alter the UI
     */
    void pair(boolean pairingSuccess);
    /**
     * This listens for the result from server
     */
    void result(boolean resultSuccess);
}
