package model;
import util.FileManager;

import java.io.Serializable;
public class GameManager implements Serializable {
    private static GameManager instance;
    private Player player;
    private boolean[] completedRooms;
    private GameManager() {
        resetGame();
    }
    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
    public void resetGame() {
        this.player = new Player();
        this.completedRooms = new boolean[6];
    }
    public Player getPlayer() { return player; }
    public boolean[] getCompletedRooms() { return completedRooms.clone(); }

    public void completeRoom(int index) {
        if (index >= 0 && index < 6) {
            completedRooms[index] = true;
        }
    }
    public void saveProgress() {
        FileManager.saveGame(this);
    }

    public static GameManager loadProgress() {
        GameManager loaded = FileManager.loadGame();
        if (loaded != null) {
            instance = loaded;
        }
        return loaded;
    }
}
