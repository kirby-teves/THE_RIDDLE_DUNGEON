package model;

import gamemasters.GameMaster;
import javax.swing.*;

public class Room {
    private final int roomNumber;
    private final String difficultyLabel;
    private final GameMaster gameMaster;
    private boolean isSolved;

    public Room(int roomNumber, String difficultyLabel, GameMaster gameMaster) {
        this.roomNumber = roomNumber;
        this.difficultyLabel = difficultyLabel;
        this.gameMaster = gameMaster;
        this.isSolved = false;
    }

    public boolean attemptAnswer(String playerAnswer) {
        if (playerAnswer == null || playerAnswer.trim().isEmpty()) {
            return false;
        }
        String input   = playerAnswer.trim().toLowerCase();
        String correct = gameMaster.getRiddle().getAnswer().trim().toLowerCase();

        if (input.equals(correct)) {
            this.isSolved = true;
            return true;
        }
        return false;
    }

    public void awardKeyReward(JComponent parent) {
        JOptionPane.showMessageDialog(
                parent,
                "🗝️ Congratulations!\n\nYou obtained a Key!",
                "Chest Opened!",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public boolean isSolved()            { return isSolved; }
    public int getRoomNumber()           { return roomNumber; }
    public String getDifficultyLabel()   { return difficultyLabel; }
    public String getGreeting()          { return gameMaster.greet(); }
    public String getRiddleQuestion()    { return gameMaster.getRiddle().getQuestion(); }
    public String getRiddleHint()        { return gameMaster.getRiddle().getHint(); }
}