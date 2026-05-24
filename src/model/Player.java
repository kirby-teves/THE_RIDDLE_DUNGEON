package model;
import javax.swing.*;
import java.io.Serializable;
public class Player implements Serializable {
    private int hearts;
    private int currentRoomIndex;
    private final int Max_Rooms = 6;
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
        if (this.hearts == 1) {
            JOptionPane.showMessageDialog(null,
                    "⚠️ WARNING: You are down to your last heart! One more mistake and you die.",
                    "Low Health Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
    public int getHearts() {
        return hearts;
    }
    public boolean isDead() {
        return hearts <= 0;
    }
    public int getCurrentRoomIndex() {
        return currentRoomIndex >= Max_Rooms ? 0 : currentRoomIndex;
    }
    public boolean hasWon() {
        return currentRoomIndex >= 6;
    }
    public void nextRoom() {
        if (isDead()) {
            System.out.println("❌ Cannot advance: Player is dead.");
            return;
        }

        if (hasWon()) {
            System.out.println("🎉 Already at the final room.");
            return;
        }
        this.currentRoomIndex++;
        System.out.println("🚶 Player advanced sequentially to Room " + this.currentRoomIndex);
    }
}

