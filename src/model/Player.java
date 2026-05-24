package model;

import util.FileManager; // Import the FileManager
import javax.swing.*;
import java.io.Serializable;

public class Player implements Serializable {
    private int hearts;
    private int currentRoomIndex;

    public Player() {
        this.hearts = 3;
        this.currentRoomIndex = 0;
    }

    public void adjustHeartsForRoom(int roomNumber) {
        if (roomNumber == 3 || roomNumber == 5) {
            this.hearts++;
        }
    }

    public void loseHeart() {
        this.hearts--;

        // If player dies, delete the save file immediately
        if (this.hearts <= 0) {
            FileManager.deleteSaveFile();
        }
    }

    public int getHearts() {
        return hearts;
    }

    public boolean isDead() {
        return hearts == 0;
    }

    public int getCurrentRoomIndex() {
        return currentRoomIndex;
    }

    public boolean hasWon() {
        return currentRoomIndex >= 6;
    }

    public void nextRoom() {
        if (this.hearts == 1) {
            JOptionPane.showMessageDialog(null,
                    "⚠️ WARNING: You are down to your last heart! One more mistake and you die.",
                    "Low Health Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
        this.currentRoomIndex++;
    }
}