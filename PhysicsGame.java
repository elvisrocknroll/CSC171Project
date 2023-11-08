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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.lang.Math;
import java.util.ArrayList;
import mypackage.Vector2f;
import mypackage.VecGraphics;
import mypackage.shapes.*;

public class PhysicsGame {
        public static void main(String[] args) {
		System.out.println("Origin at start: " + Vector2f.origin);
		Window myWindow = new Window();
        }
}
class Window extends JFrame {
	Window() {
		System.out.println("Origin at Window: " + Vector2f.origin);
		this.add(new Canvas());
		this.setVisible(true);
		this.setPreferredSize(new Dimension(800, 800));
		this.setTitle("My Game");
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}
}
class Canvas extends JPanel {

	private Point prev;
	private Point current;
	private Vector2f dr;
	private Vector2f prevVec;
	private String drawMode = "line";
	private char keyChar = ' ';
	private int keyCode = 0;
	private Color defaultColor = Color.black;
	private int defaultLineWidth = 2;
	private int radius = 10;
	private VectorOval circ = new VectorOval(new Vector2f(50, 50), new Vector2f(500, 400));
	private int dt = 15;
	private int framerate;
	private int h;
	private int w;
	private Timer timer;
	private ArrayList<VectorShape> shapes = new ArrayList<VectorShape>();
	private Vector2f gravity = new Vector2f(0, (float) 9.81);
	private ImageShape mario = new ImageShape("sprites/mario.png", new Vector2f(40, 60), new Vector2f(500, 400));
	//private VectorRect ground = new VectorRect(new Vector2f(500, 100), new Vector2f(0, 700));
	private float v = 100;
	//private Vector2f vup = new Vector2f(0, -v);
	//private Vector2f vdown = new Vector2f(0, v);
	//private Vector2f vleft = new Vector2f(-v, 0);
	//private Vector2f vright = new Vector2f(v, 0);
	
	Canvas () {	
		System.out.println("Origin in Canvas: " + Vector2f.origin);
		//shapes.add(mario);
		//shapes.add(floor);
		timer = new Timer(dt, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
	//	timer.start();
		//circ.setVelocity(new Vector2f(0, -70));
		this.addMouseListener(new ClickListener());
		this.addMouseMotionListener(new DragListener());
		this.addKeyListener(new KeyTrack());
		setFocusable(true);
		requestFocusInWindow();
	}
	@Override
        public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g);
		h = getHeight();
                w = getWidth();
	        g.setColor(defaultColor);
		g2d.setStroke(new BasicStroke(defaultLineWidth));
	        g.setFont(new Font("Serif", Font.PLAIN, 12));
	        g.drawString("Pressed Key: " + keyChar, 10, 10);
		g.drawString("Current Mode: " + drawMode, 10, 25);
		g.drawString("Line Width: " + defaultLineWidth, 10, 40);
		//if (dr != null) mario.accelerate(dr);
		//circ.accelerate(gravity);
		//if (mario.getTail().getY() >= 800) mario.translate(0, -800);
		//if (mario.getTail().getY() <= 0) mario.translate(0, 800);
		//if (mario.getTail().getX() >= 1000) mario.translate(0, -1000);
		//if (mario.getTail().getX() <= 0) mario.translate(0, 1000);

		//for (VectorShape shape : shapes) {
			//for (VectorShape other : shapes) {
			//	//shape.handleCollision(other);
			//	if (shape.detectCollision(other)) System.out.println("Collision");
			//}
		//	shape.calculateMotion(dt);
		//	shape.draw(g);
		//System.out.println(mario.getAcceleration());
		//}
		System.out.println("Acceleration: " + mario.getAcceleration());
		System.out.println("Origin: " + Vector2f.origin);
	//	mario.calculateMotion(dt);
		mario.draw(g);
	}
	private class ClickListener extends MouseAdapter {
		public void mousePressed(MouseEvent event) { // procedure for when a mouse click is registered
			prev = event.getPoint();
			prevVec = new Vector2f((float) prev.getX(),(float)  prev.getY());
			dr = prevVec.add(circ.getTail().scalarMult(-1));
			repaint();
		}
		public void mouseReleased(MouseEvent event) {
			dr = Vector2f.origin;
			repaint();
		}
	}
	private class DragListener extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent event) { // procedure for when a mouse drag is registered
			current = event.getPoint();
			dr = new Vector2f((float) (current.getX() - circ.getCenter().getX()), (float) (current.getY() - circ.getCenter().getY()));
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
			mario.setVelocity(Vector2f.origin);
		}
		public void keyCodeInputOperation(int keyCode) {
			switch (keyCode) {
			case (KeyEvent.VK_R): 
				drawMode = "reset";
				circ = new VectorOval(new Vector2f(radius, radius), new Vector2f(w - radius, h - radius));
				mario.zeroAll();
				break;
			/*case (KeyEvent.VK_W):
				mario.setVelocity(vup);
				break;
			case (KeyEvent.VK_A):
				mario.setVelocity(vleft);
				break;
			case (KeyEvent.VK_S):
				mario.setVelocity(vdown);
				break;
			case (KeyEvent.VK_D):
				mario.setVelocity(vright);
				break; */
			default: drawMode = "";
			}
			repaint();
		}
	}
}
