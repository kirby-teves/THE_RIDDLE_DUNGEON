package model;
import util.FileManager;
import javax.swing.*;
import java.io.Serializable;
public class Player implements Serializable {
    private static final int MAX_ROOMS = 6;
    private int hearts;
    private int currentRoomIndex;
    private int lastHeartAwardedRoom = -1;

    public Player() {
        this.hearts = 3;
        this.currentRoomIndex = 0;
    }
    public void adjustHeartsForRoom(int roomNumber) {
        // Only award a heart if this room hasn't given one yet
        if (roomNumber != lastHeartAwardedRoom) {
            if (roomNumber == 3 || roomNumber == 5) {
                this.hearts++;
                this.lastHeartAwardedRoom = roomNumber;
            }
        }
    }
    public void loseHeart() {
        hearts--;

        if (hearts == 1) {
            JOptionPane.showMessageDialog(null,
                    "⚠️ WARNING: You are down to your last heart! One more mistake and you die.",
                    "Low Health Warning",
                    JOptionPane.WARNING_MESSAGE);
        } else if (this.isDead()) {
            JOptionPane.showMessageDialog(null, "💀 You have run out of hearts!", "Game Over", JOptionPane.ERROR_MESSAGE);
        }
        if (this.hearts <= 0) {
            FileManager.deleteSaveFile();
        }
    }
    public int getHearts() {
        return hearts;
    }

    public boolean isDead() {
        return hearts <= 0;
    }
    public int getCurrentRoomIndex() {
        return currentRoomIndex;
    }
    public boolean hasWon() {
        return currentRoomIndex == MAX_ROOMS;
    }
    public void nextRoom() {
        if (isDead()) {
            JOptionPane.showMessageDialog(null, "You cannot move. You are dead.");
            return;
        }
        if (this.currentRoomIndex >= MAX_ROOMS) {
            JOptionPane.showMessageDialog(null, "🏁 You are already at the final room!");
            return;
        }
        this.currentRoomIndex++; // Step 1: Move to the next room

        if (hasWon()) {
            JOptionPane.showMessageDialog(null,
                    "🏆 Congratulations! You reached Room 6 and won the game!",
                    "Victory",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Step 2: Show the index we JUST moved to
            JOptionPane.showMessageDialog(null,
                    "🚶 You have entered Room " + this.currentRoomIndex,
                    "Progress",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }
}