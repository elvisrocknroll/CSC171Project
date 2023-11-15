import javax.swing.*; //JFrame, JPanel, JComponent (drawable object)
import java.awt.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.Math;
import java.util.ArrayList;
import mypackage.Vector2f;
import mypackage.VecGraphics;
import mypackage.shapes.*;

public class Game {
        public static void main(String[] args) {
		Window myWindow = new Window();
        }
}
class Window extends JFrame {
	Window() {
		this.add(new Canvas());
		this.setVisible(true);
		this.setPreferredSize(new Dimension(500, 500));
		this.setLocationRelativeTo(null);
		this.setTitle("My Game");
//		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}
}
class Canvas extends JPanel {

	private Point prev;
	private Point current;
	private Vector2f dr;
	private Vector2f prevVec;
	private VectorShape drawnShape = new VectorLine(Vector2f.zero(), Vector2f.zero());
	private VectorShape selectedShape;
	private String drawMode = "line";
	private char keyChar = ' ';
	private int keyCode = 0;
	private ArrayList<VectorShape> drawnShapes = new ArrayList<VectorShape>();
	private Color defaultColor = Color.black;
	private int defaultLineWidth = 2;
	private ImageShape mario = new ImageShape("sprites/mario.png", new Vector2f(20, 30), Vector2f.zero());

	Canvas () {
		ClickListener clickListener = new ClickListener();
		DragListener dragListener = new DragListener();
		KeyTrack keyListener = new KeyTrack();
		this.addMouseListener(clickListener);
		this.addMouseMotionListener(dragListener);
		this.addKeyListener(keyListener);
		setFocusable(true);
		requestFocusInWindow(); 
	}
	@Override
        public void paintComponent(Graphics g) {
		super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
		int h = getHeight();
                int w = getWidth();
	        g.setColor(defaultColor);
		g2d.setStroke(new BasicStroke(defaultLineWidth));
	        g.setFont(new Font("Serif", Font.PLAIN, 12));
	        g.drawString("Pressed Key: " + keyChar, 10, 10);
		g.drawString("Current Mode: " + drawMode, 10, 25);
		g.drawString("Line Width: " + defaultLineWidth, 10, 40);
		drawnShapes.add(mario);
		if (drawnShape != null) drawnShape.draw(g, defaultLineWidth, defaultColor);
		for (VectorShape shape : drawnShapes) {
			if (shape != null) shape.draw(g);
		}		
		if (selectedShape != null && drawMode.equals("drag")) selectedShape.draw(g, Color.blue);
	}
	private class ClickListener extends MouseAdapter {
		public void mousePressed(MouseEvent event) { // procedure for when a mouse click is registered
			dr = Vector2f.zero();
			prev = event.getPoint();
			prevVec = new Vector2f((float) prev.getX(),(float)  prev.getY());
			for (VectorShape shape : drawnShapes) {
				if (shape != null && shape.contains(prev)) {
					selectedShape = shape;
				}
			}
			repaint();
		}
		public void mouseReleased(MouseEvent event) {
			drawnShapes.add(drawnShape);
			selectedShape = null;
			repaint();
		}
	}
	private class DragListener extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent event) { // procedure for when a mouse drag is registered
			current = event.getPoint();
			int dx = (int) Math.round(current.getX() - prev.getX());
			int dy = (int) Math.round(current.getY() - prev.getY());
			dr = dr.add(dx, dy);
			switch (drawMode) {
			case "line": drawnShape = new VectorLine(dr, prevVec); 
				break;
			case "rectangle": drawnShape = new VectorRect(dr, prevVec);
				break;
			case "circle": drawnShape = new VectorOval(dr, prevVec);
				break;
			case "drag": selectedShape.translate(dx, dy);
				break;
			case "image": drawnShape = new ImageShape("sprites/mario.png", dr, prevVec);
				break;
			default:
			}
			prev = current;
			repaint();
		}
	}
	private class KeyTrack implements KeyListener {
		public void keyTyped(KeyEvent event) {
			keyChar = event.getKeyChar();
//			keyInputOperation(keyChar);
		}
		public void keyPressed(KeyEvent event) {
			System.out.println(event.getKeyCode());
			keyCode = event.getKeyCode();
			keyCodeInputOperation(keyCode);
		}
		public void keyReleased(KeyEvent event) {

		}
		public void keyCodeInputOperation(int keyCode) {
			switch (keyCode) {
			case (KeyEvent.VK_L): drawMode = "line";
				break;
			case (KeyEvent.VK_R): drawMode = "rectangle";
				break;
			case (KeyEvent.VK_C): drawMode = "circle";
				break;
			case (KeyEvent.VK_F):
				drawMode = "falling";
				for (VectorShape shape : drawnShapes) {
					if (shape != null) shape.translate(10, 10);
				}
				break;
			case (KeyEvent.VK_D): drawMode = "drag";
				break;
			case (KeyEvent.VK_ESCAPE): 
				drawMode = "reset";
				drawnShapes.clear();
				drawnShape = null;
				break;
			case (KeyEvent.VK_UP):
				defaultLineWidth++;
				break;
			case (KeyEvent.VK_DOWN):
				defaultLineWidth--;
				break;
			case (KeyEvent.VK_I): drawMode = "image";
				break;
			case (KeyEvent.VK_M):
				drawMode = "mario";
				mario.translate(10, 10);
				break;
			default: drawMode = "line";
			}
			repaint();
		}
	}
}
