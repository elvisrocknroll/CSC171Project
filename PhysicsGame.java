/*
WHATS GOING ON??

implemented walking animation, index out of range error sometimes when changing frames. also need to add other textures
also add dog bella
*/


import javax.swing.*; //JFrame, JPanel, JComponent (drawable object)
import javax.swing.JComponent;
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
		Window myWindow = new Window();
        }
}
class Window extends JFrame {
	Window() {
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
	private String drawMode = "rectangle";
	private char keyChar = ' ';
	private int keyCode = 0;
	private Color defaultColor = Color.black;
	private int defaultLineWidth = 2;
	private boolean buildMode = false;
	private VectorShape drawnShape = null;	
	private VectorShape selectedShape = null;
	private int dt = 15;
	private int framerate = 60;
	private int h;
	private int w;
	private Timer timer;
	private ArrayList<VectorShape> hitboxes = new ArrayList<VectorShape>();
	private ArrayList<VectorShape> focusedShapes = new ArrayList<VectorShape>();
	private Vector2f gravity = new Vector2f(0, (float) 2000);
	private ImageShape mario = new ImageShape("sprites/purtee_R_still.png", new Vector2f(80, 120), new Vector2f(500, 500)) {{
		accelerate(gravity);
	}};
	private ImageShape ground = new ImageShape("sprites/dirt_top.png", new Vector2f(800, 100), new Vector2f(0, 700));
	private float v = 400;
	private float vjump = -800;
	private Vector2f vup = new Vector2f(0, -v);
	private Vector2f vdown = new Vector2f(0, v);
	private Vector2f vleft = new Vector2f(-v, 0);
	private Vector2f vright = new Vector2f(v, 0);
	private String[] rightStillFrames = {"sprites/purtee_R_still.png"};
	private String[] rightWalkFrames = {"sprites/purtee_R_walk1.png", "sprites/purtee_R_still.png", "sprites/purtee_R_walk2.png", "sprites/purtee_R_still.png"};
	private String[] leftStillFrames = {"sprites/purtee_L_still.png"};
	private String[] leftWalkFrames = {"sprites/purtee_L_walk1.png", "sprites/purtee_L_still.png", "sprites/purtee_L_walk2.png", "sprites/purtee_L_still.png"};
	private String[] animationFrames = rightStillFrames;
	private int frameNumber = 0;
	private boolean movingLeft = false;
	private boolean movingRight = true;	

	Canvas () {	
		System.out.println("Origin in Canvas: " + Vector2f.zero());
		//shapes.add(mario);
		//shapes.add(floor);
		hitboxes.add(ground);
		focusedShapes.add(mario);
		timer = new Timer(Math.round(1000/framerate), new ActionListener() {
			private int animationTimer = 0;
			public void actionPerformed(ActionEvent e) {
				animationTimer += 1;
				if (animationTimer >= 10) {
					if (frameNumber + 1 == animationFrames.length) {
						frameNumber = 0;
					} else {
						frameNumber++;
					}
					mario.setPath(animationFrames[frameNumber]);
					animationTimer = 0;
				}
				repaint();
			}
		});
		timer.start();
		//circ.setVelocity(new Vector2f(0, -70));
		this.addMouseListener(new ClickListener());
		this.addMouseMotionListener(new DragListener());
		this.addKeyListener(new KeyTrack());
		initKeyBindings();
		setFocusable(true);
		requestFocusInWindow();
	}
	@Override
        public void paintComponent(Graphics g) {
		super.paintComponent(g);
		h = getHeight();
                w = getWidth();
	        if (buildMode) {
			buildPaint(g);
		} else {
			for (VectorShape box : hitboxes) {
				for (VectorShape focus : focusedShapes) {
					focus.handleCollisionInelastic(box);
				}
				box.calculateMotion(dt);
				box.draw(g);
			}
			for (VectorShape focus : focusedShapes) {
				focus.calculateMotion(dt);
				focus.draw(g);
			}
		}
	}
	public void buildPaint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
		g.setColor(defaultColor);
		g2d.setStroke(new BasicStroke(defaultLineWidth));
		g.setFont(new Font("Serif", Font.PLAIN, 12));
		g.drawString("Pressed Key: " + keyChar, 10, 10);
		g.drawString("Current Mode: " + drawMode, 10, 25);
                if (drawnShape != null) drawnShape.draw(g, defaultLineWidth, defaultColor);
                for (VectorShape shape : hitboxes) {
                        if (shape != null) shape.draw(g);
                }               
                if (selectedShape != null && drawMode.equals("drag")) selectedShape.draw(g, Color.blue);
	}
	private class ClickListener extends MouseAdapter {
		public void mousePressed(MouseEvent event) { // procedure for when a mouse click is registered
			if (buildMode) {
				dr = Vector2f.zero();
				prev = event.getPoint();
				prevVec = new Vector2f((float) prev.getX(),(float)  prev.getY());
				for (VectorShape shape : hitboxes) {
					if (shape != null && shape.contains(prev)) {
						selectedShape = shape;
						if (drawMode.equals("delete")) {
							hitboxes.remove(selectedShape);
							selectedShape = null;
						}
					}
				}
				repaint();
			} else {
				prev = event.getPoint();
				prevVec = new Vector2f((float) prev.getX(),(float)  prev.getY());
				repaint();
			}
		}
		public void mouseReleased(MouseEvent event) {
			if (buildMode) {
				hitboxes.add(drawnShape);
				selectedShape = null;
				repaint();
			} else {
				dr = Vector2f.zero();
				repaint();
			}
		}
	}
	private class DragListener extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent event) { // procedure for when a mouse drag is registered
			if (buildMode) {
				current = event.getPoint();
				int dx = (int) Math.round(current.getX() - prev.getX());
				int dy = (int) Math.round(current.getY() - prev.getY());
				dr = dr.add(dx, dy);
				switch (drawMode) {
				case "line": drawnShape = new VectorLine(dr, prevVec); 
					break;
				case "rectangle": drawnShape = new ImageShape("sprites/dirt.png", dr, prevVec);
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
			} else {
				current = event.getPoint();
				prev = current;
				repaint();
			}
		}
	}
        private class KeyTrack implements KeyListener {
                public void keyTyped(KeyEvent event) {
                        keyChar = event.getKeyChar();
//                      keyInputOperation(keyChar);
                }
                public void keyPressed(KeyEvent event) {
                        System.out.println(event.getKeyCode());
                        keyCode = event.getKeyCode();
                        keyCodeInputOperation(keyCode);
                }
                public void keyReleased(KeyEvent event) {

                }
                public void keyCodeInputOperation(int keyCode) {
			if (buildMode) {
				switch (keyCode) {
				case (KeyEvent.VK_L): drawMode = "line";
					break;
				case (KeyEvent.VK_R): drawMode = "rectangle";
					break;
				case (KeyEvent.VK_D): drawMode = "drag";
					break;
				case (KeyEvent.VK_ESCAPE): 
					drawMode = "reset";
					hitboxes.clear();
					drawnShape = null;
					break;
				case (KeyEvent.VK_UP):
					defaultLineWidth++;
					break;
				case (KeyEvent.VK_DOWN):
					defaultLineWidth--;
					break;
				case (KeyEvent.VK_BACK_SPACE):
					drawMode= "delete";
					break;
				default: drawMode = "rectangle";
				}
				repaint();
			}
                }
        }
	// define keybindings
	private boolean pressed_D = false;
	private boolean pressed_A = false;
	
	AbstractAction keyD = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			if (!pressed_D) {
				mario.addVelocity(vright);
				pressed_D = true;
				frameNumber = 0;
			}
			if (!movingRight) {
				movingRight = true;
			}
			animationFrames = rightWalkFrames;
		}
	};
	AbstractAction rkeyD = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			if (mario.getVelocity().getX() != 0) mario.addVelocity(vleft);
			pressed_D = false;
			animationFrames = rightStillFrames;
			frameNumber = 0;
		}
	};
	AbstractAction keyA = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			if (!pressed_A) {
				mario.addVelocity(vleft);
				pressed_A = true;
				frameNumber = 0;
			}
			if (movingRight) {
				movingRight = false;
			}
			animationFrames = leftWalkFrames;
		}
	};
	AbstractAction rkeyA = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			if (mario.getVelocity().getX() != 0) mario.addVelocity(vright);
			pressed_A = false;
			animationFrames = leftStillFrames;
			frameNumber = 0;
		}
	};
	AbstractAction keySPACE = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			mario.translate(0, -5);
			mario.addVelocity(0, vjump);
			// add fix for double jumping
		}
	};
	AbstractAction keyB = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			if (buildMode) {
				buildMode = false;
				mario.setVelocity(0, 0);
				timer.start();
			} else {
				buildMode = true;
				timer.stop();
			}
		}
	};
	private void initKeyBindings() {
		// key_D
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "key_D");
		this.getActionMap().put("key_D", keyD);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "rkey_D");
		this.getActionMap().put("rkey_D", rkeyD);
		// key_A
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "key_A");
		this.getActionMap().put("key_A", keyA);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "rkey_A");
		this.getActionMap().put("rkey_A", rkeyA);
		// key_SPACE
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "key_SPACE");
		this.getActionMap().put("key_SPACE", keySPACE);
		// key_B
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0), "key_B");
		this.getActionMap().put("key_B", keyB);
	}

/*
	private class KeyTrack implements KeyListener {
		public void keyTyped(KeyEvent event) {
			keyChar = event.getKeyChar();
//			keyInputOperation(keyChar);
		}
		public void keyPressed(KeyEvent event) {
			//System.out.println(event.getKeyCode());
			keyCode = event.getKeyCode();
			keyCodeInputOperation(keyCode);
		}
		public void keyReleased(KeyEvent event) {
			keyCodeReleaseOperation(event.getKeyCode());
		}
		public void keyCodeInputOperation(int keyCode) {
			switch (keyCode) {
			case (KeyEvent.VK_R): 
				drawMode = "reset";
				circ = new VectorOval(new Vector2f(radius, radius), new Vector2f(w - radius, h - radius));
				mario.zeroAll();
				break;
			case (KeyEvent.VK_W):
				mario.addVelocity(vup);
				break;
			case (KeyEvent.VK_A):
				mario.addVelocity(vleft);
				break;
			case (KeyEvent.VK_S):
				mario.addVelocity(vdown);
				break;
			case (KeyEvent.VK_D):
				mario.addVelocity(vright);
				break;
			case (KeyEvent.VK_SPACE):
				mario.addVelocity(0, 10);
				break;
			}
		}
		public void keyCodeReleaseOperation(int keyCode) {
			switch (keyCode) {
			case (KeyEvent.VK_R): 
				drawMode = "reset";
				circ = new VectorOval(new Vector2f(radius, radius), new Vector2f(w - radius, h - radius));
				mario.zeroAll();
				mario.accelerate(gravity);
				break;
			case (KeyEvent.VK_W):
				mario.addVelocity(vdown);
				break;
			case (KeyEvent.VK_A):
				mario.addVelocity(vright);
				break;
			case (KeyEvent.VK_S):
				mario.addVelocity(vup);
				break;
			case (KeyEvent.VK_D):
				mario.addVelocity(vleft);
				break;
			}
		} 
	}
*/
}
