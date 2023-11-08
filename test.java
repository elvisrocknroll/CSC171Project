import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JPanel;


public class KeyCharacterPrinter {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Key Character Printer");
        
        frame.add(new myPanel());
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class myPanel extends JPanel {
	private char keyChar;
	myPanel() {
		
	        this.addKeyListener(new KeyListener() {
	            @Override
	            public void keyTyped(KeyEvent e) {
	                keyChar = e.getKeyChar();
	                System.out.println("Key Typed: " + keyChar);
			repaint();
	            }

	            @Override
	            public void keyPressed(KeyEvent e) {
	                // This method is called when a key is pressed (including non-character keys).
	            }
	
	            @Override
	            public void keyReleased(KeyEvent e) {
	                // This method is called when a key is released.
	            }
	        });
	}
	
	public void paintComponent(Graphics g) {
		g.drawString(String.valueOf(keyChar), 100, 100);
	}
}
