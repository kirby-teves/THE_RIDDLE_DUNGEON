package panels;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import mains.Application;
import model.GameManager;
public class MainMenuPanel extends JPanel {
    private transient BufferedImage backgroundImg;
    private GameManager game;
    private final Application app;
    @SuppressWarnings("this-escape")
    public MainMenuPanel(Application app, GameManager game) {
        this.app = app;
        this.game = game;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 650));
        loadBackgroundImage();
        setupUI();
    }
    public void updateGameInstance(GameManager game){
        this.game = game;
    }
    private void loadBackgroundImage() {
        String imagePath = "Images/mainmenu.jpg";
        try {
            backgroundImg = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Could not load background image. Please ensure '" + imagePath + "' exists.");
            backgroundImg = null;
        }
    }
    private void setupUI() {
        setOpaque(false);
        removeAll();
        JLabel title = new JLabel("THE RIDDLE DUNGEON");
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(new Color(212, 175, 55));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 300, 50, 300));
        JButton newGame = new JButton("New Game");
        styleButton(newGame);
        newGame.addActionListener(_ ->{
            if(game != null){
                game.resetGame();
                app.setGame(game);
            }
            app.showGame();
        });
        buttonPanel.add(newGame);
        JButton loadGame = new JButton("Load Game");
        styleButton(loadGame);
        loadGame.addActionListener(_ ->{
            GameManager loadedProgress = GameManager.loadProgress();
            if(loadedProgress != null){
                this.game = loadedProgress;
                app.setGame(loadedProgress);
                app.showGame();
            } else {
                JOptionPane.showMessageDialog(this, "No save file Found.");
            }
        });
        buttonPanel.add(loadGame);
        JButton exit = new JButton("Exit");
        styleButton(exit);
        exit.addActionListener(_ -> System.exit(0));
        buttonPanel.add(exit);
        add(title, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }
    private void styleButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setForeground(new Color(212, 175, 55));
        button.setBackground(new Color(0, 0, 0, 100));
        button.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(300, 50));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (backgroundImg != null) {
            g2d.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2d.setColor(new Color(20, 20, 30));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    public void refresh() {
        revalidate();
        repaint();
    }
}
