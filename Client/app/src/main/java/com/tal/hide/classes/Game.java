package com.tal.hide.classes;

public class Game {
    // this class holds an instance of each game
    private String username;
    private String role;
    private int gameid;
    private String opponent;
    private boolean running;
    private boolean win;
    private boolean move;
    private String Uuid;

    public Game(String username, String role, int gameid, String opponent, boolean running) {
        this.username = username;
        this.role = role;
        this.gameid = gameid;
        this.opponent = opponent;
        this.running = running;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public int getGameid() {
        return gameid;
    }

    public String getOpponent() {
        return opponent;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public boolean getMove() {
        return move;
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    public String getUuid() {
        return Uuid;
    }

    public void setUuid(String uuid) {
        Uuid = uuid;
    }
}
