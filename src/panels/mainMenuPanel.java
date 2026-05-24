package panels;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import mains.Application;
import model.GameManager;
public class mainMenuPanel extends JPanel {
    private transient BufferedImage backgroundImg;
    private GameManager game;
    private final Application app;
    private static final Color GOLD = new Color(232, 190, 55);
    private static final Color GOLD_BRIGHT = new Color(255, 225, 105);
    private static final Color BUTTON_BG = new Color(18, 14, 10, 210);
    private static final Color BUTTON_HOVER_BG = new Color(78, 51, 13, 235);
    private static final Color SHADOW = new Color(0, 0, 0, 185);
    @SuppressWarnings("this-escape")
    public mainMenuPanel(Application app, GameManager game) {
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
        JLabel title = getJLabel();

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 90, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(8, 0, 8, 0);

        JButton newGame = new JButton("New Game");
        styleButton(newGame);
        newGame.addActionListener(_ ->{
            if(game != null){
                game.resetGame();
                app.setGame(game);
            }
            app.showGame();
        });
        buttonPanel.add(newGame, gbc);

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
        buttonPanel.add(loadGame, gbc);

        JButton exit = new JButton("Exit");
        styleButton(exit);
        exit.addActionListener(_ -> System.exit(0));
        buttonPanel.add(exit, gbc);
        add(title, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private JLabel getJLabel() {
        JLabel title = new JLabel("THE RIDDLE DUNGEON") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                String text = getText();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.setColor(SHADOW);
                g2.drawString(text, x + 3, y + 3);
                g2.setColor(getForeground());
                g2.drawString(text, x, y);
                g2.dispose();
            }
        };
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(GOLD_BRIGHT);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setPreferredSize(new Dimension(900, 92));
        return title;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Serif", Font.BOLD, 22));
        button.setForeground(GOLD_BRIGHT);
        button.setBackground(BUTTON_BG);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD, 2),
                BorderFactory.createEmptyBorder(12, 28, 12, 28)));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(320, 64));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(BUTTON_HOVER_BG);
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(GOLD_BRIGHT, 3),
                        BorderFactory.createEmptyBorder(11, 27, 11, 27)));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(BUTTON_BG);
                button.setForeground(GOLD_BRIGHT);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(GOLD, 2),
                        BorderFactory.createEmptyBorder(12, 28, 12, 28)));
            }
        });
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
        g2d.setColor(new Color(0, 0, 0, 65));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setPaint(new GradientPaint(
                0, 0, new Color(0, 0, 0, 150),
                0, (float) getHeight() / 2, new Color(0, 0, 0, 20)));
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
    public void refresh() {
        revalidate();
        repaint();
    }
}
