package panels;
import model.GameManager;
import mains.Application;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
public class MapPanel extends JPanel {
    private final Application mainApp;
    private JButton[] roomButtons;
    private JLabel currentRoomLabel;
    private JLabel roomsLeftLabel;
    private transient BufferedImage bgImage;
    private static final Color COLOR_GOLD = new Color(212, 175, 55);
    private static final Color COLOR_DARK_GOLD = new Color(142, 112, 36);
    private static final Color COLOR_PANEL = new Color(8, 8, 12, 155);
    private static final Color COLOR_LOCKED = new Color(145, 145, 145);
    private static final Color OVERLAY_COLOR = new Color(0, 0, 0, 85);
    @SuppressWarnings("this-escape")
    public MapPanel(Application app) {
        this.mainApp = app;
        loadBackgroundImage();
        setupUI();
        applyDungeonTheme();
    }
    private void loadBackgroundImage() {
        URL imgUrl = getClass().getResource("/Images/dungeon1.jpg");
        if (imgUrl == null) {
            imgUrl = getClass().getResource("/images/dungeon1.jpg");
        }
        if (imgUrl != null) {
            try {
                bgImage = ImageIO.read(imgUrl);
                return;
            } catch (IOException e) {
                System.err.println("MapPanel: could not read image from classpath – " + e.getMessage());
            }
        }
        java.io.File fallback = new java.io.File("Images/dungeon1.jpg");
        if (!fallback.exists()) {
            fallback = new java.io.File("src/main/resources/dungeon1.jpg");
        }
        if (!fallback.exists()) {
            fallback = new java.io.File("resources/dungeon1.jpg");
        }
        if (fallback.exists()) {
            try {
                bgImage = ImageIO.read(fallback);
            } catch (IOException e) {
                System.err.println("MapPanel: could not read image from file – " + e.getMessage());
            }
        }

        if (bgImage == null) {
            System.err.println("MapPanel: background image not found; panel will use solid colour.");
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        if (bgImage != null) {
            int panelW = getWidth();
            int panelH = getHeight();
            double scaleX = (double) panelW / bgImage.getWidth();
            double scaleY = (double) panelH / bgImage.getHeight();
            double scale  = Math.max(scaleX, scaleY);
            int drawW = (int) (bgImage.getWidth()  * scale);
            int drawH = (int) (bgImage.getHeight() * scale);
            int drawX = (panelW - drawW) / 2;
            int drawY = (panelH - drawH) / 2;
            g2d.drawImage(bgImage, drawX, drawY, drawW, drawH, null);
        } else {
            g2d.setColor(new Color(20, 18, 24));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        g2d.setColor(OVERLAY_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }
    private void setupUI() {
        setLayout(new BorderLayout(0, 15));

        setBorder(BorderFactory.createLineBorder(COLOR_GOLD, 2));
        setPreferredSize(new Dimension(300, 500));

        JLabel title = new JLabel("DUNGEON MAP", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setForeground(COLOR_GOLD);
        title.setOpaque(false);

        currentRoomLabel = new JLabel("Current Room: 1", SwingConstants.CENTER);
        currentRoomLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        currentRoomLabel.setForeground(Color.WHITE);

        roomsLeftLabel = new JLabel("Rooms Left: 6", SwingConstants.CENTER);
        roomsLeftLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        roomsLeftLabel.setForeground(COLOR_GOLD);

        JPanel header = new JPanel(new GridLayout(3, 1, 0, 4));
        header.setOpaque(true);
        header.setBackground(COLOR_PANEL);
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 10, 20));
        header.add(title);
        header.add(currentRoomLabel);
        header.add(roomsLeftLabel);
        add(header, BorderLayout.NORTH);

        JPanel btnContainer = new JPanel(new GridLayout(6, 1, 0, 10));
        btnContainer.setOpaque(false);
        btnContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        roomButtons = new JButton[6];
        for (int i = 0; i < 6; i++) {
            roomButtons[i] = new JButton("ROOM " + (i + 1));
            roomButtons[i].setFont(new Font("Serif", Font.BOLD, 16));
            roomButtons[i].setFocusPainted(false);
            roomButtons[i].setOpaque(true);
            roomButtons[i].setContentAreaFilled(true);
            addHoverEffect(roomButtons[i]);
            btnContainer.add(roomButtons[i]);
        }
        add(btnContainer, BorderLayout.CENTER);
        JButton backBtn = new JButton("← Back to Game");
        styleButton(backBtn);
        backBtn.addActionListener(_ -> mainApp.showGame());
        add(backBtn, BorderLayout.SOUTH);
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(COLOR_GOLD);
        btn.setBackground(new Color(0, 0, 0, 150));
        btn.setBorder(BorderFactory.createLineBorder(COLOR_GOLD, 2));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    public void refresh() {
        GameManager game = GameManager.getInstance();
        boolean[] completed = game.getCompletedRooms();
        int currentRoomIndex = game.getPlayer().getCurrentRoomIndex();
        int completedCount = 0;
        for (boolean roomCompleted : completed) {
            if (roomCompleted) completedCount++;
        }
        int roomsLeft = Math.max(0, completed.length - completedCount);

        if (currentRoomLabel != null) {
            currentRoomLabel.setText(currentRoomIndex >= completed.length
                    ? "Current Room: Escaped"
                    : "Current Room: " + (currentRoomIndex + 1));
        }
        if (roomsLeftLabel != null) {
            roomsLeftLabel.setText("Rooms Left: " + roomsLeft);
        }

        for (int i = 0; i < 6; i++) {
            JButton btn = roomButtons[i];
            if (btn == null) continue;
            boolean isCurrent   = (i == currentRoomIndex);
            boolean isCompleted = completed[i];
            btn.setBackground(new Color(0, 0, 0, 155));
            btn.setForeground(Color.WHITE);
            if (isCompleted) {
                btn.setText("UNLOCKED  ROOM " + (i + 1));
                btn.setForeground(COLOR_GOLD);
                btn.setBorder(BorderFactory.createLineBorder(COLOR_GOLD, 2));
            } else if (isCurrent) {
                btn.setText("CURRENT   ROOM " + (i + 1));
                btn.setForeground(Color.WHITE);
                btn.setBackground(new Color(92, 70, 18, 210));
                btn.setBorder(BorderFactory.createLineBorder(COLOR_GOLD, 3));
            } else {
                btn.setText("LOCKED    ROOM " + (i + 1));
                btn.setForeground(COLOR_LOCKED);
                btn.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
            }
        }
        revalidate();
        repaint();
    }
    private void addHoverEffect(JButton btn) {
        btn.setUI(new DungeonButtonUI());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.putClientProperty("hovered", Boolean.TRUE);
                btn.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.putClientProperty("hovered", Boolean.FALSE);
                btn.repaint();
            }
        });
    }
    private static class DungeonButtonUI extends BasicButtonUI {
        private static final Color HOVER_FILL   = new Color(212, 175, 55, 55);
        private static final Color HOVER_BORDER = new Color(255, 215, 80);
        private static final Color HOVER_GLOW   = new Color(212, 175, 55, 35);
        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton btn = (AbstractButton) c;
            boolean hovered = Boolean.TRUE.equals(btn.getClientProperty("hovered"));
            if (hovered) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                int w = c.getWidth();
                int h = c.getHeight();
                int arc = 6;
                g2.setColor(HOVER_GLOW);
                g2.fillRoundRect(-3, -3, w + 6, h + 6, arc + 4, arc + 4);
                g2.setColor(HOVER_FILL);
                g2.fillRoundRect(0, 0, w, h, arc, arc);
                g2.setColor(HOVER_BORDER);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, arc, arc);
                g2.dispose();
            }
            super.paint(g, c);
        }
    }
    private void applyDungeonTheme() {
        setOpaque(true);

        for (JButton btn : roomButtons) {
            if (btn == null) continue;

            btn.setFont(new Font("Monospaced", Font.BOLD, 16));
            btn.setForeground(COLOR_GOLD);
            btn.setBackground(new Color(0, 0, 0, 155));
            btn.setBorder(BorderFactory.createLineBorder(COLOR_DARK_GOLD, 2));
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(true);
            btn.setOpaque(true);
            addHoverEffect(btn);
        }

        repaint();
    }
}
