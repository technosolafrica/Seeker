package com.tal.hide;

public class Game {
    // this class holds an instance of each game
    private String username;
    private String role;
    private String gameid;
    private String opponent;
    private boolean running;
    private boolean win;
    private String move;
    private String Uuid;

    public Game(String username, String role, String gameid, String opponent, boolean running) {
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

    public String getGameid() {
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

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public String getUuid() {
        return Uuid;
    }

    public void setUuid(String uuid) {
        Uuid = uuid;
    }
}
