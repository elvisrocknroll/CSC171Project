/*
WHATS GOING ON??

wtf is the goal for this game tho
add respawn method here and reset the map translation vector
maybe add a separate method for handling map translation that also translates respawn point
figure out whats going on with mtv??
build maps finally

DONE
also add dog bella, write pathfinding for bella to follow (teleport if max distance exceeded)
add boundary boxes and respawn point
add dynamic map
add enemies? include pathfinding for them too maybe, pathfinding should go in the vectorshape class
jump and movement methods might have to go into vectorshape class too
finish implementing load and save in Level class

*/

import javax.swing.*; //JFrame, JPanel, JComponent (drawable object)
import javax.swing.JComponent;
import javax.swing.JOptionPane;
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
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import mypackage.*;
import mypackage.Level;
import mypackage.Vector2f;
import mypackage.VecGraphics;
import mypackage.shapes.*;

public class RecoverPhysicsGame {
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
	private Vector2f charToClick;
	private String drawMode = "rectangle";
	private char keyChar = ' ';
	private int keyCode = 0;
	private Color defaultColor = Color.black;
	private int defaultLineWidth = 2;
	private boolean buildMode = false;
	private boolean vectorMode = false;
	private ImageShape drawnShape = null;	
	private ImageShape selectedShape = null;
	private Door selectedDoor;
	private int dt = 3;
	private int framerate = 300;
	private int animationTimer = 0;
	private int h;
	private int w;
	private Scanner console = new Scanner(System.in);
	private Timer timer;
	private VectorRect focusedRight = new VectorRect(600, 0, 800, 800);
	private VectorRect focusedLeft = new VectorRect(0, 000, 200, 800);
	private VectorRect focusedTop = new VectorRect(200, 0, 600, 200);
	private VectorRect focusedBottom = new VectorRect(0, 600, 800, 800);
	private VectorRect spawnpoint = new VectorRect(new Vector2f(0, 0) , new Vector2f(500, 500));
	private ArrayList<ImageShape> environment = new ArrayList<>();
	private ArrayList<ImageShape> enemies = new ArrayList<>();
	private ArrayList<VectorRect> boundaries = new ArrayList<>();
	private ArrayList<Door> doors = new ArrayList<>();
	private ArrayList<ImageShape> doorimg = new ArrayList<>();
	private ImageShape background;
	private int backgroundSelector = 0;
	private ArrayList<ImageShape> focusedShapes = new ArrayList<>();
	private ArrayList<VectorShape> movableShapes = new ArrayList<>();
	private ArrayList<ImageShape> draggableShapes = new ArrayList<>();
	private ImageShape spray;
	private Level level = new Level(spawnpoint, environment, enemies, boundaries, doors);
	private Vector2f gravity = new Vector2f(0, (float) 2000);
	private ImageShape mario;
	private ImageShape bella;
	private ArrayList<String> environmentTextures = new ArrayList<>() {{
		add("sprites/environment/dirt_top.png");
		add("sprites/environment/dirt.png");
		add("sprites/environment/floor1.png");
		add("sprites/environment/floor2.png");
		add("sprites/environment/road.png");
	}};
	private ArrayList<String> sprites = new ArrayList<>() {{
		add("sprites/characters/purtee/spawnpoint.png");
		add("sprites/characters/terminator/terminator_R_still.png");
		
	}};
	private ArrayList<String> doorTextures = new ArrayList<>() {{
		add("sprites/doors/door1.png");
	}};
	private ArrayList<String> backgroundTextures = new ArrayList<>() {{
		add("sprites/backgrounds/bg_mountains.png");
		add("sprites/backgrounds/hoyt.png");
		add("sprites/backgrounds/rushrhees.png");
		add("sprites/backgrounds/wilson.png");
		add("sprites/backgrounds/goinghome.png");
		add("sprites/backgrounds/lab1.png");
		add("sprites/backgrounds/lab2.png");
	}};
	private ArrayList<String> textures = environmentTextures;
	private int textureSelector = 0;
	private ImageShape ground = new ImageShape("sprites/environment/dirt_top.png", new Vector2f(1600, 100), new Vector2f(0, 700));
	private ImageShape bg_mountains = new ImageShape("sprites/backgrounds/bg_mountains.png", new Vector2f(1600, 800), new Vector2f(0, 0));

	Canvas () {
	/*
		movableShapes.add(ground);
	*/
		initLevel(Level.load("flat.txt"));
		timer = new Timer(Math.round(1000/framerate), new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				animationTimer += 1;
				if (animationTimer >= 30) {
					for (VectorShape shape : focusedShapes) {
						shape.updateFrame();
					}
					animationTimer = 0;
				}
				repaint();
			}
		});
		timer.start();
		this.addMouseListener(new ClickListener());
		this.addMouseMotionListener(new DragListener());
		this.addKeyListener(new KeyTrack());
		initKeyBindings();
		setFocusable(true);
		requestFocusInWindow();
	}
	public void initLevel(Level loadedLevel) {
		level = loadedLevel;
		spawnpoint = level.getSpawnpoint();
		mario = Level.getPurtee(spawnpoint.getTail());
		bella = Level.getBella(spawnpoint.getTail());
		level.addMario(mario);
		level.addBella(bella);
		environment = level.getEnvironment();
		enemies = level.getEnemies();
		doors = level.getDoors();
		doorimg = level.getDoorimg();
		boundaries = level.getBoundary();
		spray = level.getLevelSpray();
		focusedShapes = level.getFocusedShapes();
		movableShapes = level.getMovableShapes();
		draggableShapes = level.getDraggableShapes();
		aggro();
		bella.setPathfindTarget(mario, true);
		background = level.getBackground();
	}
	@Override
        public void paintComponent(Graphics g) {
		super.paintComponent(g);
		h = this.getHeight();
                w = this.getWidth();
		//background.resize(new Vector2f(w, -1 * h));
		Vector2f mapdr = new Vector2f(0, 0);
	        if (buildMode) {
			buildPaint(g);
		} else {
			selectedDoor = null;
			background.draw(g);
			for (VectorShape env : environment) {
				for (VectorShape focus : focusedShapes) {
					focus.handleCollision(env, false);
				}
				if (spray != null) spray.handleCollision(env, true);
				env.draw(g);
			}
			for (ImageShape door : doorimg) {
				door.draw(g);
			}
			for (Door door : doors) {
				if (mario.detectCollision(door.getImage())) {
					selectedDoor = door;
					VectorRect highlight = new VectorRect(door.getImage().getVec(), door.getImage().getTail()) {{
						draw(g, 3, Color.yellow);
					}};
				}
			}
			for (VectorShape focus : focusedShapes) {
				focus.calculateMotion(dt);
				focus.draw(g);
			}
			if (spray != null) {
				if (spray.isAlive()) {
					spray.calculateMotion(dt);
					spray.draw(g);
				} else {
					level.removeSpray();
					spray = null;
				}
			}
			for (ImageShape enemy : enemies) {
				if (mario.detectCollision(enemy)) respawn();
				if (spray != null && spray.detectCollision(enemy)) {
					enemy.stun(1000);
					level.removeSpray();
					spray = null;
				}
			}
			if (mario.detectCollision(focusedRight)) translateMap((float) (-1 * mario.getMovementSpeed() * 0.001 * dt), 0);
			if (mario.detectCollision(focusedLeft)) translateMap((float) (mario.getMovementSpeed() * 0.001 * dt), 0);
			if (mario.detectCollision(focusedTop)) translateMap(0, (float) (mario.getMovementSpeed() * 0.001 * dt));
			if (mario.detectCollision(focusedBottom)) translateMap(0, (float) (-1 * mario.getMovementSpeed() * 0.004 * dt));
		}
		if (vectorMode) {
			for (VectorShape shape : movableShapes) {
				shape.drawVectors(g);
			}
			background.drawVectors(g);
			if (spray != null) spray.drawVectors(g);
		}
	}
	public void buildPaint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
		g.setColor(defaultColor);
		g2d.setStroke(new BasicStroke(defaultLineWidth));
		g.setFont(new Font("Serif", Font.PLAIN, 12));
		background.draw(g);
		g.drawString("Pressed Key: " + keyChar, 10, 10);
		g.drawString("Current Mode: " + drawMode, 10, 25);
		ImageShape selectedTexture = new ImageShape(textures.get(textureSelector), new Vector2f(20, 20), new Vector2f(10, 40)) {{
			draw(g);
		}};
		ImageShape spawnghost = new ImageShape("sprites/characters/purtee/spawnpoint.png", new Vector2f(80, 120), spawnpoint.getTail()) {{
			draw(g);
		}};
                if (drawnShape != null) drawnShape.draw(g);
                for (VectorShape shape : movableShapes) {
                        if (shape != null) shape.draw(g);
                }               
                if (selectedShape != null && drawMode.equals("drag")) selectedShape.draw(g, Color.blue);
	}
	public void addDoorDialogue() {
		String destination = (String) JOptionPane.showInputDialog(
			this, "Enter a destination for this door: ",
			"Customized Dialog",
			JOptionPane.PLAIN_MESSAGE,
			null,
			null,
			"destination"
		);
		if (destination != null) level.addDoor(new Door(destination, drawnShape));
	}
	/*public void consoleInput() {
		switch (console.next()) {
		case "load":
			initLevel(Level.load(console.next()));
			repaint();
			break;
		case "save";:
			level.save(console.next());
			break;
		default:
			System.out.println("unknown console command");
		}
	}*/
	// map translation and respawn methods
	private Vector2f mapTranslationVector = new Vector2f(0, 0);
	public void translateMap(Vector2f dr) {
		for (VectorShape shape : movableShapes) {
			shape.translate(dr);
		}
		mapTranslationVector = mapTranslationVector.add(dr);
	}
	public void translateMap(float dx, float dy) {
		for (VectorShape shape : movableShapes) {
			shape.translate(dx, dy);
		}
		mapTranslationVector = mapTranslationVector.add(dx, dy);
	}
	public void recenterMap() {
		for (VectorShape shape : movableShapes) {
			shape.translate(mapTranslationVector.scalarMult(-1));
		}
		mapTranslationVector.set(0, 0);
	}
	public void recenterCamera() {
		Vector2f dr = mapTranslationVector.sub(mario.getTail());
		for (VectorShape shape : movableShapes) {
			shape.translate(dr);
		}
		mapTranslationVector = mapTranslationVector.sub(dr);
	}
	public void respawn() {
		recenterMap();
		mario.teleport(spawnpoint.getTail());
		aggro();
	}
	public void reload() {
		initLevel(Level.load(level.getFilepath()));
	}
	public void aggro() {
		for (ImageShape enemy : enemies) {
			enemy.setPathfindTarget(mario, false);
		}
	}
	// listeners
	private class ClickListener extends MouseAdapter {
		public void mousePressed(MouseEvent event) { // procedure for when a mouse click is registered
			if (buildMode) {
				dr = Vector2f.zero();
				prev = event.getPoint();
				prevVec = new Vector2f((float) prev.getX(),(float)  prev.getY());
				if (drawMode.equals("character")) {
					switch (textureSelector) {
					case 0 : spawnpoint.setTail(prevVec);
						break;
					case 1 :
						level.addEnemy(Level.getTerminator(prevVec)); 
						break;
					}
				}
				for (ImageShape shape : draggableShapes) {
					if (shape != null && shape.contains(prev)) {
						selectedShape = shape;
						if (drawMode.equals("delete")) {
							if (enemies.contains(selectedShape)) level.removeEnemy(selectedShape);
							if (environment.contains(selectedShape)) level.removeEnvironment(selectedShape); 
							selectedShape = null;
						}
					}
				}
				repaint();
			} else {
				prev = event.getPoint();
				prevVec = new Vector2f((float) prev.getX(),(float)  prev.getY());
				charToClick = prevVec.sub(bella.getTail());
				if (spray == null) {
					level.addSpray(bella.getFoot().add(0, -20), charToClick.unitVector().scalarMult(1200).add(bella.getVelocity()));
					spray = level.getLevelSpray();
				}
				repaint();
			}
		}
		public void mouseReleased(MouseEvent event) {
			if (buildMode) {
				switch (drawMode) {
				case "environment":
					level.addEnvironment(drawnShape);
					break;
				case "door":
					addDoorDialogue();
					break;
				}
				drawnShape = null;
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
				case "environment":
				case "door" : drawnShape = new ImageShape(textures.get(textureSelector), dr, prevVec);
					break;
				case "drag": selectedShape.translate(dx, dy);
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
                }
                public void keyPressed(KeyEvent event) {
                        keyCode = event.getKeyCode();
                        keyCodeInputOperation(keyCode);
                }
                public void keyReleased(KeyEvent event) {

                }
                public void keyCodeInputOperation(int keyCode) {
			if (buildMode) {
				switch (keyCode) {
				case (KeyEvent.VK_L): drawMode = "load";
					System.out.println("Enter save filename: ");
					try {
						initLevel(Level.load(console.next()));
						repaint();
					} catch(Exception e) {
						System.out.println("Save file could not be found");
					}
					break;
				case (KeyEvent.VK_S): drawMode = "save";
					System.out.print("Enter save filename: ");
					level.save(console.next());
					break;
				case (KeyEvent.VK_E): drawMode = "environment";
					if (!textures.equals(environmentTextures)) {
						textures = environmentTextures;
						textureSelector = 0;
					}
					break;
				case (KeyEvent.VK_P): drawMode = "door";
					if (!textures.equals(doorTextures)) {
						textures = doorTextures;
						textureSelector = 0;
					}
					break;
				case (KeyEvent.VK_D): drawMode = "drag";
					break;
				case (KeyEvent.VK_C): drawMode = "character";
					if (!textures.equals(sprites)) {
						textures = sprites;
						textureSelector = 0;
					}	
					break;
				case (KeyEvent.VK_ESCAPE): 
					drawMode = "reset";
					level.clear();
					drawnShape = null;
					break;
				case (KeyEvent.VK_BACK_SPACE):
					drawMode= "delete";
					break;
				case (KeyEvent.VK_UP):
					for (VectorShape shape : movableShapes) {
						translateMap(0, 10);
					}
					break;
				case (KeyEvent.VK_DOWN):
					for (VectorShape shape : movableShapes) {
						translateMap(0, -10);
					}
					break;
				case (KeyEvent.VK_LEFT):
					for (VectorShape shape : movableShapes) {
						translateMap(10, 0);
					}
					break;
				case (KeyEvent.VK_RIGHT):
					for (VectorShape shape : movableShapes) {
						translateMap(-10, 0);
					}
					break;
				case (KeyEvent.VK_OPEN_BRACKET):
					textureSelector--;
					if (textureSelector < 0) textureSelector = textures.size() - 1;
					break;
				case (KeyEvent.VK_CLOSE_BRACKET):
					textureSelector++;
					if (textureSelector >= textures.size()) textureSelector = 0;
					break;
				case (KeyEvent.VK_BACK_SLASH):
					System.out.println("changing background to " + backgroundSelector);
					backgroundSelector++;
					if (backgroundSelector >= backgroundTextures.size()) backgroundSelector = 0;
					level.setBackground(backgroundTextures.get(backgroundSelector));
					break;
				default: drawMode = "environment";
				}
				repaint();
			}
		}
	}
	Action keyD = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			mario.moveRight();
		}
	};
	Action rkeyD = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			mario.stopRight();
		}
	};
	Action keyA = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			mario.moveLeft();
		}
	};
	Action rkeyA = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			mario.stopLeft();
		}
	};
	Action keySPACE = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			if (selectedDoor != null) {
				initLevel(Level.load(selectedDoor.getDestination()));
			} else {
				mario.jump();
			}
		}
	};
	Action keyB = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			if (buildMode) {
				buildMode = false;
				timer.start();
				recenterMap();
				aggro();
				repaint();
			} else {
				textures = environmentTextures;
				textureSelector = 0;
				drawMode = "environment";
				recenterMap();
				buildMode = true;
				timer.stop();
				repaint();
			}
		}
	};
	Action keyR = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			reload();
		}
	};
	Action keyV = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			if (vectorMode) {
				vectorMode = false;
			} else {
				vectorMode = true;
			}
		}
	};
	public void initKeyBindings() {
		// keyD
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "key_D");
		getActionMap().put("key_D", this.keyD);
		// rkeyD
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "rkey_D");
		getActionMap().put("rkey_D", this.rkeyD);
		// keyA
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "key_A");
		getActionMap().put("key_A", this.keyA);
		// rkeyA
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "rkey_A");
		getActionMap().put("rkey_A", this.rkeyA);
		// keySPACE
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "key_SPACE");
		getActionMap().put("key_SPACE", this.keySPACE);
		// keyB
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0), "key_B");
		getActionMap().put("key_B", this.keyB);
		// keyR
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "key_R");
		getActionMap().put("key_R", this.keyR);
		// keyV
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0), "key_V");
		getActionMap().put("key_V", this.keyV);
	}
}
