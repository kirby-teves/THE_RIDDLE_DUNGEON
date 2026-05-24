package mains;
import javax.swing.SwingUtilities;
public class Main {
    static void main() {
        SwingUtilities.invokeLater(() -> {
            Application app = new Application();
            app.setVisible(true);
        });
    }
}