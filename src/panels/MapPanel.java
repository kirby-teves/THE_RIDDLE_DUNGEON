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

    private JPanel mapPanel;
    private JLabel title;
    private JButton room1;
    private JButton room2;
    private JButton room3;
    private JButton room4;
    private JButton room5;
    private JButton room6;

    private Application mainApp;
    private GameManager game;
    private JButton[] roomButtons;

    private BufferedImage bgImage;

    private static final Color OVERLAY_COLOR = new Color(0, 0, 0, 160);

    public MapPanel(Application app, GameManager game) {
        this.mainApp = app;
        this.game = game;

        loadBackgroundImage();


        roomButtons = new JButton[]{room1, room2, room3, room4, room5, room6};


        if (title == null) {
            setupFallbackUI();
        } else {
            setupFormUI();
        }

        applyDungeonTheme();
    }


    private void loadBackgroundImage() {
        URL imgUrl = getClass().getResource("/dungeon1.jpg");
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

        java.io.File fallback = new java.io.File("src/main/resources/dungeon1.jpg");
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

            g2d.setColor(new Color(15, 15, 15));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        g2d.setColor(OVERLAY_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.dispose();
    }


    private void setupFallbackUI() {
        setLayout(new BorderLayout(0, 15));

        setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));
        setPreferredSize(new Dimension(300, 500));

        title = new JLabel("DUNGEON MAP", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setForeground(new Color(212, 175, 55));
        title.setOpaque(false);
        add(title, BorderLayout.NORTH);

        JPanel btnContainer = new JPanel(new GridLayout(6, 1, 0, 10));
        btnContainer.setOpaque(false);
        btnContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        roomButtons = new JButton[6];
        for (int i = 0; i < 6; i++) {
            roomButtons[i] = new JButton("ROOM " + (i + 1));
            roomButtons[i].setFont(new Font("Serif", Font.BOLD, 16));
            roomButtons[i].setFocusPainted(false);
            roomButtons[i].setOpaque(false);
            roomButtons[i].setContentAreaFilled(false);
            addHoverEffect(roomButtons[i]);
            btnContainer.add(roomButtons[i]);
        }
        add(btnContainer, BorderLayout.CENTER);


        JButton backBtn = new JButton("← Back to Game");
        styleButton(backBtn);
        backBtn.addActionListener(e -> mainApp.showGame());
        add(backBtn, BorderLayout.SOUTH);
    }

    private void setupFormUI() {
        if (title != null) {
            title.setFont(new Font("Serif", Font.BOLD, 24));
            title.setForeground(new Color(212, 175, 55));
            title.setHorizontalAlignment(SwingConstants.CENTER);
            title.setOpaque(false);
        }

        for (JButton btn : roomButtons) {
            if (btn != null) {
                btn.setFont(new Font("Serif", Font.BOLD, 16));
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false);
                btn.setOpaque(false);
                addHoverEffect(btn);
            }
        }
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(new Color(212, 175, 55));
        btn.setBackground(new Color(0, 0, 0, 80));
        btn.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void refresh() {
        this.game = GameManager.getInstance();

        boolean[] completed = game.getCompletedRooms();
        int currentRoomIndex = game.getPlayer().getCurrentRoomIndex();

        for (int i = 0; i < 6; i++) {
            JButton btn = roomButtons[i];
            if (btn == null) continue;

            boolean isCurrent   = (i == currentRoomIndex);
            boolean isCompleted = completed[i];

            btn.setBorder(null);
            btn.setForeground(Color.WHITE);

            if (isCompleted) {
                btn.setText("✓ ROOM " + (i + 1));
                btn.setForeground(new Color(212, 175, 55));
                btn.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));
            } else if (isCurrent) {
                btn.setText("⬤ ROOM " + (i + 1));
                btn.setForeground(new Color(212, 175, 55));
                btn.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));
            } else {
                btn.setText("X ROOM " + (i + 1));
                btn.setForeground(new Color(100, 100, 100));
                btn.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));
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

        private static final Color HOVER_FILL   = new Color(212, 175, 55, 60);
        private static final Color HOVER_BORDER = new Color(255, 215, 80, 220);
        private static final Color HOVER_GLOW   = new Color(212, 175, 55, 25);

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

        if (mapPanel != null) {
            mapPanel.setOpaque(false);
        }

        if (title != null) {
            title.setForeground(new Color(212, 175, 55));
            title.setFont(new Font("Monospaced", Font.BOLD, 26));
            title.setOpaque(false);
        }

        for (JButton btn : roomButtons) {
            if (btn == null) continue;

            btn.setFont(new Font("Monospaced", Font.BOLD, 16));
            btn.setForeground(new Color(212, 175, 55));
            btn.setBackground(new Color(30, 30, 30));
            btn.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 3));
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);
            addHoverEffect(btn);
        }

        repaint();
    }
}