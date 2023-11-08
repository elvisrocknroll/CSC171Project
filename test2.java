import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyCharacterPrinterWithPanel extends JPanel {
    private char keyPressed = ' '; // Initialize with a space

    public KeyCharacterPrinterWithPanel() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                keyPressed = e.getKeyChar();
                repaint();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // This method is not used here, but it's part of the KeyListener interface.
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // This method is not used here, but it's part of the KeyListener interface.
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.PLAIN, 24));
        g.drawString("Pressed Key: " + keyPressed, 10, 30);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Key Character Printer with JPanel");
        KeyCharacterPrinterWithPanel panel = new KeyCharacterPrinterWithPanel();
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 100);
        frame.setVisible(true);
    }
}

