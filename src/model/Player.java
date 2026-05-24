package model;
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
        if (roomNumber != lastHeartAwardedRoom) {
            if (roomNumber == 0) {
                this.hearts = 3;
                this.lastHeartAwardedRoom = roomNumber;
            } else if (roomNumber == 2) {
                this.hearts = 4;
                this.lastHeartAwardedRoom = roomNumber;
                JOptionPane.showMessageDialog(null, "❤️ Medium Level: Hearts set to 4!");
            } else if (roomNumber == 4) {
                this.hearts = 5;
                this.lastHeartAwardedRoom = roomNumber;
                JOptionPane.showMessageDialog(null, "❤️ Hard Level: Hearts set to 5!");
            }
        }
    }

    public void loseHeart() {
        this.hearts--;
        if (this.hearts == 1) {
            JOptionPane.showMessageDialog(null,
                    "⚠️ WARNING: Last heart!",
                    "Low Health",
                    JOptionPane.WARNING_MESSAGE);
        } else if (this.isDead()) {
            JOptionPane.showMessageDialog(null, "💀 Game Over!", "Death", JOptionPane.ERROR_MESSAGE);
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
        this.currentRoomIndex++;
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