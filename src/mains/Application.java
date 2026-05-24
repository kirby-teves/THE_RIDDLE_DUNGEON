package mains;
import panels.mainMenuPanel;
import panels.GamePanel;
import panels.MapPanel;
import model.GameManager;

import javax.swing.*;
import java.awt.*;
public class Application extends JFrame {
    private GameManager game;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private mainMenuPanel mainMenu;
    private GamePanel gamePanel;
    private MapPanel mapPanel;
    public Application() {
        game = GameManager.getInstance();
        initializeGUI();
        showMainMenu();
    }
    private void initializeGUI() {
        setTitle("The Riddle Dungeon");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainMenu = new mainMenuPanel(this,game);
        gamePanel = new GamePanel(this,game);
        mapPanel = new MapPanel(this);
        gamePanel.setOnReturnToMenu(this::showMainMenu);

        mainPanel.add(mainMenu, "MENU");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(mapPanel, "MAP");

        setContentPane(mainPanel);
    }
    public void showMainMenu() {
        if (game == null) {
            game = GameManager.getInstance();
            game.resetGame();
        }
        game.resetGame();
        mainMenu.updateGameInstance(game);
        cardLayout.show(mainPanel, "MENU");
        mainMenu.refresh();
    }
    public void showGame() {
        cardLayout.show(mainPanel, "GAME");
        if(gamePanel != null){
            gamePanel.updateGameInstance(game);
        }
    }
    public void setGame(GameManager game) {
        if (game != null) {
            this.game = game;
            mainMenu.updateGameInstance(game);
            gamePanel.updateGameInstance(game);
        }
    }
    public void showMap() {
        cardLayout.show(mainPanel, "MAP");
        mapPanel.refresh();
    }
}
