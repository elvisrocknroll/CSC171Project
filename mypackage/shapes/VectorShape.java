package mypackage.shapes;

import mypackage.Vector2f;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JComponent;
import java.util.ArrayList;
import java.lang.Short;

public abstract class VectorShape extends JComponent{

	protected Vector2f tail = Vector2f.zero();
	protected Vector2f vec;
	protected Color color = Color.black;
	protected int stroke = 2;
	protected Vector2f velocity = Vector2f.zero();
	protected Vector2f acceleration = Vector2f.zero();
	protected float mass = 10;
	protected float x1;
	protected float x2;
	protected float y1;
	protected float y2;
        protected Vector2f head;
        protected Vector2f drawnTail;
        protected Vector2f drawnVec;
        protected float width;
        protected float height;
	protected Vector2f ULU = Vector2f.zero();
	protected Vector2f URU = Vector2f.zero();
	protected Vector2f ULD = Vector2f.zero();
	protected Vector2f URD = Vector2f.zero();
	protected Vector2f DLU = Vector2f.zero();
	protected Vector2f DRU = Vector2f.zero();
	protected Vector2f DLD = Vector2f.zero();
	protected Vector2f DRD = Vector2f.zero();
	protected Vector2f[] cmesh = {ULU, URU, ULD, URD, DLU, DRU, DLD, DRD};
	protected byte meshSpacing = 5;
	
	private ArrayList<Short> UCollisions = new ArrayList<>() {{
		this.add(Short.parseShort("10000000", 2)); // 10000000
		this.add(Short.parseShort("11000000", 2)); // 11000000
		this.add(Short.parseShort("01000000", 2)); // 01000000
		this.add(Short.parseShort("11110000", 2));
		this.add(Short.parseShort("11111100", 2));
	}};
	private ArrayList<Short> DCollisions = new ArrayList<>() {{
		this.add(Short.parseShort("00000010", 2)); // 00000010
		this.add(Short.parseShort("00000011", 2)); // 00000011
		this.add(Short.parseShort("00000001", 2)); // 00000001
		this.add(Short.parseShort("00001111", 2));
		this.add(Short.parseShort("00111111", 2));
	}};
	private ArrayList<Short> LCollisions = new ArrayList<>() {{
		this.add(Short.parseShort("10100000", 2)); // 10100000
		this.add(Short.parseShort("10101000", 2)); // 10101000
		this.add(Short.parseShort("10101010", 2)); // 10101010
		this.add(Short.parseShort("00101010", 2)); // 00101010
		this.add(Short.parseShort("00001010", 2)); // 00001010
	}};
	private ArrayList<Short> RCollisions = new ArrayList<>() {{
		this.add(Short.parseShort("01010000", 2)); // 01010000
		this.add(Short.parseShort("01010100", 2)); // 01010100
		this.add(Short.parseShort("01010101", 2)); // 01010101
		this.add(Short.parseShort("00010101", 2)); // 00010101
		this.add(Short.parseShort("00000101", 2)); // 00000101
	}};

	public abstract float getArea();
	public abstract void draw(Graphics g);
	public abstract void draw(Graphics g, int stroke);
	public abstract void draw(Graphics g, Color outline);
	public abstract void draw(Graphics g, int stroke, Color outline);
	public abstract void fill(Graphics g);
	public abstract void fill(Graphics g, Color fill);
	public abstract void fill(Graphics g, Color fill, Color outline);
	public abstract void fill(Graphics g, int stroke, Color fill, Color outline);
        
	public void calculatePosition() {
                this.head = vec.add(tail);
                this.drawnTail = tail.add((float) (0.5 * (vec.getX() - Math.abs(vec.getX()))), (float) (0.5 * (vec.getY() - Math.abs(vec.getY()))));
                this.drawnVec = new Vector2f(Math.abs(vec.getX()), Math.abs(vec.getY()));

                x1 = drawnTail.getX();
                x2 = drawnTail.add(drawnVec).getX();
                y1 = drawnTail.getY();
                y2 = drawnTail.add(drawnVec).getY();
                height = drawnVec.getY();
                width = drawnVec.getX();
		ULU.set(x1, y1);
		URU.set(x2, y1);
		ULD.set(x1, y1 + meshSpacing);
		URD.set(x2, y1 + meshSpacing);
		DLU.set(x1, y2 - meshSpacing);
		DRU.set(x2, y2 - meshSpacing);
		DLD.set(x1, y2);
		DRD.set(x2, y2);
        }
        public boolean barelyContains(Vector2f point) {
                float x = point.getX();
                float y = point.getY();
                if (x >= (float) x1 && x <= (float) x2 && (float) y >= y1 && y <= y2)
                        return true;
                return false;
        }
        public boolean contains(Vector2f point) {
                float x = point.getX();
                float y = point.getY();
                if (x > (float) x1 && x < (float) x2 && (float) y > y1 && y < y2)
                        return true;
                return false;
        }
	public Vector2f getTail() {
		return tail;
	}
	public void setTail(Vector2f newTail) {
		tail = newTail;
		calculatePosition();
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color newColor) {
		this.color = newColor;
	}
	public float getMass() {
		return mass;
	}
	public void setMass(float newMass) {
		mass = newMass;
	}
        public float getX1() {
                return x1;
        }
        public float getX2() {
                return x2;
        }
        public float getY1() {
                return y1;
        }
        public float getY2() {
                return y2;
        }
	public int getStroke() {
		return stroke;
	}
	public void setStroke(int newStroke) {
		this.stroke = newStroke;
	}
	public void initGraphics(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(stroke));
		g.setColor(color);
	}
	public void initGraphics(Graphics g, int stroke) {
		this.stroke = stroke;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(stroke));
		g.setColor(color);
	}
	public void initGraphics(Graphics g, Color color) {
		this.color = color;	
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(stroke));
		g.setColor(color);
	}
	public void initGraphics(Graphics g, int stroke, Color color) {
		this.stroke = stroke;
		this.color = color;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(stroke));
		g.setColor(color);
	}
	public void translate(Vector2f shift) {
		tail.set(tail.getX() + shift.getX(), tail.getY() + shift.getY());
		calculatePosition();
	}
	public void translate(float x, float y) {
		tail.set(tail.getX() + x, tail.getY() + y);
		calculatePosition();
	}
	public void translateBuffer(Vector2f shift) {
		tail.set(tail.getX() + shift.getX(), tail.getY() + shift.getY());
	}
	public void translateBuffer(float x, float y) {
		tail.set(tail.getX() + x, tail.getY() + y);
	}
	public Vector2f getAcceleration() {
		return acceleration;
	}
	public Vector2f getVelocity() {
		return velocity;
	}
	public void accelerate(Vector2f a) {
		acceleration = a;
	}
	public void accelerate(float ax, float ay) {
		acceleration.set(ax, ay);
	}
	public void addAcceleration(Vector2f a) {
		acceleration = acceleration.add(a);
	}
	public void addAcceleration(float ax, float ay) {
		acceleration = acceleration.add(ax, ay);
	}
	public void setVelocity(Vector2f v) {
		velocity = v;
	}
	public void setVelocity(float vx, float vy) {
		velocity.set(vx, vy);
	}
	public void addVelocity(Vector2f v) {
		velocity = velocity.add(v);
	}
	public void addVelocity(float vx, float vy) {
		velocity = velocity.add(vx, vy);
	}
	public void calculateMotion(float t) {
		float dt = (float) 0.001*t;
		addVelocity(acceleration.scalarMult(dt));
		translate(velocity.scalarMult(dt));
	}
	public void zeroAll() {
		velocity = Vector2f.zero();
		acceleration = Vector2f.zero();
		setTail(Vector2f.zero());
	}
	public static void force(Vector2f vec) {
		
	}
// collisions
	public short detectBarelyCollidedPoints(VectorShape other) {
		String collidedMesh = "";
		for (int i = 0; i < 8; i++) {
			if (other.barelyContains((Vector2f) cmesh[i])) {
				collidedMesh += "1";
			} else { 
				collidedMesh += "0"; 
			}
		}
		return Short.parseShort(collidedMesh, 2);
	}
	public short detectCollidedPoints(VectorShape other) {
		String collidedMesh = "";
		for (int i = 0; i < 8; i++) {
			if (other.contains((Vector2f) cmesh[i])) {
				collidedMesh += "1";
			} else { 
				collidedMesh += "0"; 
			}
		}
		return Short.parseShort(collidedMesh, 2);
	}
	public boolean detectCollision(VectorShape other) {
		for (Vector2f point : cmesh) {
			if (other.contains((Vector2f) point)) {
				System.out.println("Collision");
				return true; 
			}	
		}
		return false;
	}
	public void handleCollisionInelastic(VectorShape other) {
		handleXCollisionInelastic(other);
		handleYCollisionInelastic(other);
	}
	public void handleXCollisionInelastic(VectorShape other) {
		short collidedPoints = detectCollidedPoints(other);
		if (collidedPoints != 0) {
			if (RCollisions.contains(collidedPoints)) {
				translate(-1, 0);
				handleXCollisionInelastic(other);
				velocity.setX(0);
			}
			if (LCollisions.contains(collidedPoints)) {
				translate(1, 0);
				handleXCollisionInelastic(other);
				velocity.setX(0);
			}
		}
	}
	public void handleYCollisionInelastic(VectorShape other) {
		short collidedPoints = detectCollidedPoints(other);
		if (collidedPoints != 0) {
			if (DCollisions.contains(collidedPoints)) {
				translate(0, -1);
				handleYCollisionInelastic(other);
				velocity.setY(0);
			}
			if (UCollisions.contains(collidedPoints)) {
				translate(0, 1);
				handleYCollisionInelastic(other);
				velocity.setY(0);
			}
		}
	}
/*
        public char detectCollision(VectorShape other) {
                if ((other.getX2() >= x1 && other.getX2() <= x2) || (other.getX1() <= x2 && other.getX1() >= x1)) return 'x';
                if ((other.getY2() >= y1 && other.getY2() <= y2) || (other.getY1() <= y2 && other.getY1() >= y1)) return 'y';
                return '';
        }
        public void handleCollision(VectorShape other) {
                if ((other.getX2() >= x1 && other.getX2() <= x2) || (other.getX1() <= x2 && other.getX1() >= x1)) {
			velocity.setX(0);
			System.out.println("Colliding in X");
                }
		if ((other.getY2() >= y1 && other.getY2() <= y2) || (other.getY1() <= y2 && other.getY1() >= y1)) {
			velocity.setY(0);
			System.out.println("Colliding in Y");
		}
        } 
*/
}
