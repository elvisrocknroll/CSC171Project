package mypackage.shapes;

import mypackage.Vector2f;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JComponent;
import java.util.ArrayList;

public abstract class VectorShape extends JComponent{

	protected Vector2f tail = Vector2f.origin;
	protected Vector2f vec;
	protected Color color = Color.black;
	protected int stroke = 2;
	protected Vector2f velocity = Vector2f.origin;
	protected Vector2f acceleration = Vector2f.origin;
	//protected Vector2f velocity = new Vector2f(0, 0);
	//protected Vector2f acceleration = new Vector2f(0, 0);
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
	protected Vector2f UL = Vector2f.origin;
	protected Vector2f UR = Vector2f.origin;
	protected Vector2f DL = Vector2f.origin;
	protected Vector2f DR = Vector2f.origin;
	protected Vector2f[] corners = {UL, UR, DL, DR};

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
		UL.set(x1, y1);
		UR.set(x2, y1);
		DL.set(x1, y2);
		DR.set(x2, y2);
        }
        public boolean contains(Vector2f point) {
                float x = point.getX();
                float y = point.getY();
                if (x >= (float) x1 && x <= (float) x2 && (float) y >= y1 && y <= y2)
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
	public void setVelocity(Vector2f v) {
		velocity = v;
	}
	public void setVelocity(float vx, float vy) {
		velocity.set(vx, vy);
	}
	public void calculateMotion(float t) {
		float dt = (float) 0.001*t;
		//velocity = velocity.add(acceleration.scalarMult(dt));
		tail = tail.add(velocity.scalarMult(dt));
		calculatePosition();
	}
	public void zeroAll() {
		velocity = Vector2f.origin;
		acceleration = Vector2f.origin;
		setTail(Vector2f.origin);
	}
	public static void force(Vector2f vec) {
		
	}
// collisions
	public boolean detectCollision(VectorShape other) {
		for (Vector2f corner : corners) {
			if (other.contains((Vector2f) corner)) return true; 
		}
		return false;
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
