package panels;

import mains.Application;
import model.GameManager;
import model.Player;
import model.Room;
import gamemasters.Awit;
import gamemasters.Deanver;
import gamemasters.Kirby;
import gamemasters.Jojan;
import gamemasters.Hayes;
import gamemasters.Patrick;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;



public class GamePanel extends JPanel {


    private static final Dimension PANEL_SIZE    = new Dimension(900, 650);
    private static final Dimension DIALOGUE_SIZE = new Dimension(900, 220);
    private static final Dimension BUTTON_SIZE   = new Dimension(80, 34);

    private static final Color COLOR_DARK_BG     = new Color(12, 10, 22);
    private static final Color COLOR_GOLD        = new Color(212, 175, 55);
    private static final Color COLOR_HINT        = new Color(255, 165, 0);
    private static final Color COLOR_CORRECT     = new Color(80, 220, 100);
    private static final Color COLOR_INPUT_BG    = new Color(25, 20, 45);
    private static final Color COLOR_HOVER_BG    = new Color(60, 50, 20);

    private static final Font FONT_HUD      = new Font("Monospaced", Font.BOLD, 15);
    private static final Font FONT_HEARTS   = new Font("SansSerif",  Font.BOLD, 22);
    private static final Font FONT_RIDDLE   = new Font("Serif",      Font.PLAIN, 17);
    private static final Font FONT_HINT     = new Font("SansSerif",  Font.ITALIC, 14);
    private static final Font FONT_BUTTON   = new Font("Monospaced", Font.BOLD, 13);
    private static final Font FONT_LABEL    = new Font("Monospaced", Font.BOLD, 14);
    private static final Font FONT_INPUT    = new Font("Monospaced", Font.PLAIN, 15);
    private static final Font FONT_ENDTITLE = new Font("Serif",      Font.BOLD, 36);
    private static final Font FONT_ENDSUB   = new Font("SansSerif",  Font.PLAIN, 16);

    private static final String[] ROOM_IMAGE_FILES = {
            "kirbyroom.jpg",
            "deanverroom.jpg",
            "jojanroom.jpg",
            "hayesroom.jpg",
            "awitroom.jpg",
            "patroom.jpg"
    };


    private JPanel    gamePanel;
    private JLabel    title;

    private JLabel    lblHearts;
    private JLabel    lblRoomInfo;
    private JTextArea txtRiddle;
    private JTextArea lblHint;
    private JLabel    answer;
    private JTextField txtInput;
    private JButton   SOLVEButton;
    private JButton   Map;
    private JButton   btnSave;


    private JPanel imagePaneRef;



    private Application application;
    private GameManager game;
    private Player      player;
    private Room[]      rooms;
    private boolean[]   completedRooms;
    private boolean     isProcessingAnswer = false;
    private Runnable    onReturnToMenu;

    private final BufferedImage[] roomImages = new BufferedImage[6];
    private BufferedImage currentBg = null;



    public GamePanel(Application application, GameManager game) {
        this.application = application;
        this.game        = game;
        initializeGame();
    }


    public void updateGameInstance(GameManager game) {
        if (game != null) {
            this.game   = game;
            this.player = game.getPlayer();
        }
        loadLevel();
    }

    public void setOnReturnToMenu(Runnable callback) {
        this.onReturnToMenu = callback;
    }


    public void initializeGame() {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(COLOR_DARK_BG);
        setPreferredSize(PANEL_SIZE);

        loadRoomImages();
        buildUI();

        player         = new Player();
        completedRooms = new boolean[6];
        createRooms();
        setupListeners();
        loadLevel();

        revalidate();
        repaint();
    }

    private void createRooms() {
        rooms    = new Room[6];
        rooms[0] = new Room(1, "Easy",   new Kirby());
        rooms[1] = new Room(2, "Easy",   new Deanver());
        rooms[2] = new Room(3, "Medium", new Jojan());
        rooms[3] = new Room(4, "Medium", new Hayes());
        rooms[4] = new Room(5, "Hard",   new Awit());
        rooms[5] = new Room(6, "Hard",   new Patrick());
    }


    private void loadRoomImages() {
        for (int i = 0; i < ROOM_IMAGE_FILES.length; i++) {
            roomImages[i] = tryLoadImage(ROOM_IMAGE_FILES[i]);
        }
    }


    private BufferedImage tryLoadImage(String name) {

        for (String prefix : new String[]{ "/", "/images/", "/assets/" }) {
            URL url = getClass().getResource(prefix + name);
            if (url != null) {
                try {
                    BufferedImage img = ImageIO.read(url);
                    if (img != null) return img;
                } catch (IOException ignored) {}
            }
        }


        String wd  = System.getProperty("user.dir");
        String sep = File.separator;
        String[] candidates = {
                name,
                "resources"                                + sep + name,
                "src" + sep + "resources"                  + sep + name,
                "src" + sep + "main" + sep + "resources"   + sep + name,
                wd + sep + name,
                wd + sep + "resources"                     + sep + name,
                wd + sep + "src" + sep + "resources"       + sep + name,
                wd + sep + "src" + sep + "main" + sep + "resources" + sep + name,
        };

        for (String path : candidates) {
            File f = new File(path);
            if (f.exists()) {
                try {
                    BufferedImage img = ImageIO.read(f);
                    if (img != null) return img;
                } catch (IOException ignored) {}
            }
        }

        System.err.printf("[GamePanel] Image not found: %s  (user.dir=%s)%n", name, wd);
        return null;
    }


    private void buildUI() {
        add(buildImagePane(),    BorderLayout.CENTER);
        add(buildDialoguePane(), BorderLayout.SOUTH);
    }


    private JPanel buildImagePane() {
        JPanel pane = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                int pw = getWidth(), ph = getHeight();


                g2.setColor(COLOR_DARK_BG);
                g2.fillRect(0, 0, pw, ph);

                if (currentBg != null) {
                    paintBackgroundImage(g2, pw, ph);
                    paintVignettes(g2, pw, ph);
                }

                g2.dispose();
            }

            private void paintBackgroundImage(Graphics2D g2, int pw, int ph) {
                int iw = currentBg.getWidth(), ih = currentBg.getHeight();
                double scale = (double) pw / iw;
                int dw = pw, dh = (int)(ih * scale);


                if (dh < ph) {
                    scale = (double) ph / ih;
                    dw    = (int)(iw * scale);
                    dh    = ph;
                }

                int drawX = (pw - dw) / 2;
                g2.drawImage(currentBg, drawX, 0, dw, dh, null);
            }


            private void paintVignettes(Graphics2D g2, int pw, int ph) {

                g2.setPaint(new GradientPaint(
                        0, ph - 80, new Color(0, 0, 0, 0),
                        0, ph,      COLOR_DARK_BG));
                g2.fillRect(0, ph - 80, pw, 80);


                g2.setPaint(new GradientPaint(
                        0, 0,  new Color(0, 0, 0, 130),
                        0, 70, new Color(0, 0, 0, 0)));
                g2.fillRect(0, 0, pw, 70);
            }
        };

        pane.setOpaque(true);
        pane.setBackground(COLOR_DARK_BG);
        pane.add(buildHudBar(), BorderLayout.NORTH);

        imagePaneRef = pane;
        return pane;
    }

    private JPanel buildHudBar() {
        lblHearts = new JLabel("❤️❤️❤️");
        lblHearts.setFont(FONT_HEARTS);
        lblHearts.setForeground(Color.WHITE);

        lblRoomInfo = new JLabel("Room 1  |  Easy", SwingConstants.RIGHT);
        lblRoomInfo.setFont(FONT_HUD);
        lblRoomInfo.setForeground(COLOR_GOLD);

        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(14, 20, 6, 20));
        bar.add(lblHearts,   BorderLayout.WEST);
        bar.add(lblRoomInfo, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildDialoguePane() {
        JPanel outer = new JPanel(new BorderLayout(0, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(COLOR_DARK_BG);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(COLOR_GOLD);
                g.fillRect(0, 0, getWidth(), 2); // gold top rule
            }
        };
        outer.setOpaque(true);
        outer.setBackground(COLOR_DARK_BG);
        outer.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        outer.setPreferredSize(DIALOGUE_SIZE);

        outer.add(buildTextStack(),  BorderLayout.CENTER);
        outer.add(buildAnswerRow(),  BorderLayout.SOUTH);
        return outer;
    }

    private JPanel buildTextStack() {
        txtRiddle = createDialogueTextArea(FONT_RIDDLE, Color.WHITE);
        lblHint   = createDialogueTextArea(FONT_HINT,   COLOR_HINT);

        JPanel stack = new JPanel(new BorderLayout(0, 4));
        stack.setOpaque(true);
        stack.setBackground(COLOR_DARK_BG);
        stack.add(txtRiddle, BorderLayout.CENTER);
        stack.add(lblHint,   BorderLayout.SOUTH);
        return stack;
    }

    private JPanel buildAnswerRow() {
        answer = new JLabel("Answer: ");
        answer.setFont(FONT_LABEL);
        answer.setForeground(COLOR_GOLD);

        txtInput = new JTextField();
        txtInput.setFont(FONT_INPUT);
        txtInput.setForeground(Color.WHITE);
        txtInput.setBackground(COLOR_INPUT_BG);
        txtInput.setCaretColor(COLOR_GOLD);
        txtInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_GOLD, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));

        SOLVEButton = makeGoldButton("SOLVE");
        Map         = makeGoldButton("MAP");
        btnSave     = makeGoldButton("SAVE");

        JPanel buttons = new JPanel(new GridLayout(1, 3, 6, 0));
        buttons.setOpaque(true);
        buttons.setBackground(COLOR_DARK_BG);
        buttons.add(SOLVEButton);
        buttons.add(Map);
        buttons.add(btnSave);

        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(true);
        row.setBackground(COLOR_DARK_BG);
        row.add(answer,  BorderLayout.WEST);
        row.add(txtInput, BorderLayout.CENTER);
        row.add(buttons,  BorderLayout.EAST);
        return row;
    }


    private JTextArea createDialogueTextArea(Font font, Color foreground) {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(font);
        area.setForeground(foreground);
        area.setOpaque(true);
        area.setBackground(COLOR_DARK_BG);
        area.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        return area;
    }


    private JButton makeGoldButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setForeground(COLOR_GOLD);
        btn.setBackground(COLOR_INPUT_BG);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(COLOR_GOLD, 1));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(BUTTON_SIZE);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_HOVER_BG);
                btn.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COLOR_INPUT_BG);
                btn.setForeground(COLOR_GOLD);
            }
        });
        return btn;
    }



    private void setupListeners() {
        if (SOLVEButton != null) SOLVEButton.addActionListener(e -> checkAnswer());
        if (txtInput    != null) txtInput.addActionListener(e -> checkAnswer());

        if (Map != null) Map.addActionListener(e -> {
            if (application != null) application.showMap();
        });

        if (btnSave != null) btnSave.addActionListener(e -> {
            if (game != null) {
                game.saveProgress();
                JOptionPane.showMessageDialog(this, "Game saved.");
            }
        });
    }


    public void loadLevel() {
        if (player.hasWon())   { showEndScreen(true);  return; }
        if (!player.isAlive()) { showEndScreen(false); return; }

        int  idx     = player.getCurrentRoomIndex();
        Room current = rooms[idx];
        player.adjustHeartsForRoom(current.getRoomNumber());

        currentBg = (idx < roomImages.length) ? roomImages[idx] : null;
        if (imagePaneRef != null) imagePaneRef.repaint();

        if (lblHearts   != null) lblHearts.setText(buildHeartsString());
        if (lblRoomInfo != null) lblRoomInfo.setText(
                "Room " + current.getRoomNumber() + "  |  " + current.getDifficultyLabel());
        if (txtRiddle   != null) txtRiddle.setText(
                current.getGreeting() + "\n\n" + current.getRiddleQuestion());
        if (lblHint     != null) setHintText("Hint: " + current.getRiddleHint(), COLOR_HINT);
        if (txtInput    != null) { txtInput.setText(""); txtInput.requestFocus(); }

        isProcessingAnswer = false;
        revalidate();
        repaint();
    }

    private void checkAnswer() {
        if (isProcessingAnswer || player == null
                || player.hasWon() || !player.isAlive()) return;

        isProcessingAnswer = true;
        setInputsEnabled(false);

        Room   current = rooms[player.getCurrentRoomIndex()];
        String answer  = (txtInput != null && txtInput.getText() != null)
                ? txtInput.getText().trim() : "";

        if (current.attemptAnswer(answer)) {
            onCorrectAnswer(current);
        } else {
            onWrongAnswer(current);
        }
    }

    private void onCorrectAnswer(Room current) {
        int idx = player.getCurrentRoomIndex();
        completedRooms[idx] = true;
        if (game != null) game.completeRoom(idx);

        setHintText("✅ CORRECT! The door unlocks...", COLOR_CORRECT);

        scheduleDelayed(1500, () -> {
            player.nextRoom();
            setInputsEnabled(true);
            loadLevel();
        });
    }

    private void onWrongAnswer(Room current) {
        player.loseHeart();
        setHintText("❌ WRONG! You lose a heart.", Color.RED);
        if (lblHearts != null) lblHearts.setText(buildHeartsString());

        if (!player.isAlive()) {
            scheduleDelayed(1500, () -> {
                isProcessingAnswer = false;
                setInputsEnabled(true);
                loadLevel();
            });
        } else {
            scheduleDelayed(1200, () -> {
                setHintText("Hint: " + current.getRiddleHint(), COLOR_HINT);
                setInputsEnabled(true);
                isProcessingAnswer = false;
            });
        }
    }

    private void showEndScreen(boolean won) {
        JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                "Game Over",
                Dialog.ModalityType.APPLICATION_MODAL);

        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(COLOR_DARK_BG);
        dialog.setUndecorated(true);
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(COLOR_GOLD, 2));

        JLabel msgLabel = new JLabel(won ? "🏆  ESCAPED!" : "💀  YOU DIED");
        msgLabel.setFont(FONT_ENDTITLE);
        msgLabel.setForeground(won ? Color.YELLOW : Color.RED);

        JLabel subLabel = new JLabel(
                won ? "You conquered the dungeon!" : "The dungeon claims another soul.");
        subLabel.setFont(FONT_ENDSUB);
        subLabel.setForeground(Color.WHITE);

        JButton returnBtn = makeGoldButton("Return to Menu");
        returnBtn.setPreferredSize(new Dimension(160, 36));
        returnBtn.addActionListener(e -> {
            dialog.dispose();
            if (onReturnToMenu != null) onReturnToMenu.run();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 40, 15, 40);
        gbc.gridy = 0; dialog.add(msgLabel,   gbc);
        gbc.gridy = 1; dialog.add(subLabel,   gbc);
        gbc.gridy = 2; dialog.add(returnBtn,  gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private String buildHeartsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < player.getHearts(); i++) sb.append("❤️");
        return sb.toString();
    }

    private void setHintText(String text, Color color) {
        if (lblHint == null) return;
        lblHint.setText(text);
        lblHint.setForeground(color);
        lblHint.setBackground(COLOR_DARK_BG);
    }

    private void setInputsEnabled(boolean enabled) {
        if (txtInput    != null) txtInput.setEnabled(enabled);
        if (SOLVEButton != null) SOLVEButton.setEnabled(enabled);
    }

    private void scheduleDelayed(int delayMs, Runnable action) {
        Timer timer = new Timer(delayMs, e -> action.run());
        timer.setRepeats(false);
        timer.start();
    }
}