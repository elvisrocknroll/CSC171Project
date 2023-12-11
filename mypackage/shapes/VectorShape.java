package mypackage.shapes;
/*
Elvis Imamura
CSC 171 Final Project
VectorShape class

the parent abstract class of all other shapes; Vector2f based implementation of java swing graphics also contains all methods for motion, physics, and pathfinding
*/
import mypackage.Vector2f;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JComponent;
import java.util.ArrayList;
import java.lang.Short;

public abstract class VectorShape extends JComponent{

	protected Vector2f tail = Vector2f.zero();
	protected Vector2f vec = Vector2f.zero();
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
	protected Vector2f foot = new Vector2f(0, 0);
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
	protected byte meshSpacing = 10;
	protected boolean grounded;
	protected int bounceCount = 0;
	protected int stunTimer = 0;
	
	private ArrayList<Short> UCollisions = new ArrayList<>() {{
		this.add(Short.parseShort("10000000", 2)); 
		this.add(Short.parseShort("11000000", 2)); 
		this.add(Short.parseShort("01000000", 2)); 
		this.add(Short.parseShort("11110000", 2));
		this.add(Short.parseShort("11111100", 2));
	}};
	private ArrayList<Short> DCollisions = new ArrayList<>() {{
		this.add(Short.parseShort("00000010", 2)); 
		this.add(Short.parseShort("00000011", 2)); 
		this.add(Short.parseShort("00000001", 2)); 
		this.add(Short.parseShort("00001111", 2));
		this.add(Short.parseShort("00111111", 2));
	}};
	private ArrayList<Short> LCollisions = new ArrayList<>() {{
		this.add(Short.parseShort("10100000", 2));
		this.add(Short.parseShort("10101000", 2));
		this.add(Short.parseShort("10101010", 2));
		this.add(Short.parseShort("00101010", 2));
		this.add(Short.parseShort("00001010", 2));
	}};
	private ArrayList<Short> RCollisions = new ArrayList<>() {{
		this.add(Short.parseShort("01010000", 2));
		this.add(Short.parseShort("01010100", 2));
		this.add(Short.parseShort("01010101", 2));
		this.add(Short.parseShort("00010101", 2));
		this.add(Short.parseShort("00000101", 2));
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
                foot.set((float) ((x1 + x2) / 2), y2);
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
		return drawnTail;
	}
	public Vector2f getVec() {
		return drawnVec;
	}
	public Vector2f getFoot() {
		return foot;
	}
	public void setVec(Vector2f newVec) {
		vec.set(newVec);
		calculatePosition();
	}
	public void setTail(Vector2f newTail) {
		tail.set(newTail);
		calculatePosition();
	}
	public void setTail(float x, float y) {
		tail = new Vector2f(x, y);
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
	// kinematic physics methods
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
		acceleration.set(a);
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
		velocity.set(v);
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
		if (pathfinding) pathfind();
		float dt = (float) 0.001*t;
		if (stunTimer > 0) {
			stunTimer--;
			handleStunFrames();
		} else {
			addVelocity(acceleration.scalarMult(dt));
			translate(velocity.scalarMult(dt));
			handleMotionFrames();
		}
	}
	public void zeroAll() {
		velocity = Vector2f.zero();
		acceleration = Vector2f.zero();
		setTail(Vector2f.zero());
	}
	public static void force(Vector2f vec) {
		
	}
	public void updateFrame() {}
	// collisions
	public void setMeshSpacing(byte m) {
		meshSpacing = m;
	}
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
				return true; 
			}	
		}
		return false;
	}
	public void handleCollision(VectorShape other, boolean elastic) {
		if (elastic) {
			handleXCollisionElastic(other);
			handleYCollisionElastic(other);
		} else {
			handleXCollisionInelastic(other);
			handleYCollisionInelastic(other);
		}
	}
	public void handleXCollisionElastic(VectorShape other) {
		short collidedPoints = detectCollidedPoints(other);
		if (collidedPoints != 0) {
			if (RCollisions.contains(collidedPoints) || LCollisions.contains(collidedPoints)) {
				velocity.setX(-1 * velocity.getX());
				bounceCount++;
			}
		}
	}
	public void handleYCollisionElastic(VectorShape other) {
		short collidedPoints = detectCollidedPoints(other);
		if (collidedPoints != 0) {
			if (UCollisions.contains(collidedPoints) || DCollisions.contains(collidedPoints)) {
				velocity.setY(-1 * velocity.getY());
				bounceCount++;
			}
		}
	}
	public int getBounce() {
		return bounceCount;
	}
	public void handleXCollisionInelastic(VectorShape other) {
		short collidedPoints = detectCollidedPoints(other);
		if (collidedPoints != 0) {
			if (RCollisions.contains(collidedPoints)) {
				translate(-1, 0);
				handleXCollisionInelastic(other);
				if (pathfinding) jump();
				//stopRight();
				xColliding = true;
			}
			if (LCollisions.contains(collidedPoints)) {
				translate(1, 0);
				handleXCollisionInelastic(other);
				if (pathfinding) jump();
				//stopLeft();
				xColliding = true;
			}
		} else {
			xColliding = false;
		}
	}
	public void handleYCollisionInelastic(VectorShape other) {
		short collidedPoints = detectCollidedPoints(other);
		if (collidedPoints != 0) {
			if (DCollisions.contains(collidedPoints)) {
				translate(0, -1);
				handleYCollisionInelastic(other);
				velocity.setY(0);
				grounded = true;
				jumpCount = 0;
				jumping = false;
			}
			if (UCollisions.contains(collidedPoints)) {
				translate(0, 1);
				handleYCollisionInelastic(other);
				velocity.setY(0);
			}
		}
	}
	public boolean isGrounded() {
		return grounded;
	}
	public void ground() {
		grounded = true;
	}
	public void unground() {
		grounded = false;
	}
	// preset movements
	protected float movementSpeed = 400;
	protected float vjump = -800;
	protected boolean movingLeft = false;
	protected boolean movingRight = false;
	protected boolean facingRight = true;
	protected boolean jumping = false;
	protected boolean xColliding = false;
	protected byte movementDirection = 0;
	protected byte maxJump = 2;
	protected byte jumpCount = 0;
	public float getMovementSpeed() {
		return movementSpeed;
	}
	public void setMovementSpeed(float s) {
		movementSpeed = s;
	}
	public void setJump(float j) {
		vjump = j;
	}
	public void setMaxJump(byte n) {
		maxJump = n;
	}
	public void moveRight() {
		if (!movingRight) {
			movingRight = true;
			movementDirection++;
			handleMovement();
			facingRight = true;
		} 
	}
	public void stopRight() {
		if (movingRight) {
			movementDirection--;
			handleMovement();
			movingRight = false;
		}
	}
	public void moveLeft() {
		if (!movingLeft) {
			movingLeft = true;
			movementDirection--;
			handleMovement();
			facingRight = false;
		} 
	}
	public void stopLeft() {
		if (movingLeft) {
			movementDirection++;
			handleMovement();
			movingLeft = false;
		}
	}
	public void jump() {
                if (grounded) {
			jumpCount = 0;
		}
                if (jumpCount < maxJump) {
                        translate(0, -5);
                        addVelocity(0, vjump);
                        jumpCount++;
			jumping = true;
			grounded = false;
                } 
	}
	public void handleMovement() {
		if (xColliding) {
			velocity.setX(0);
		} else {
			velocity.setX(movementSpeed * movementDirection);
		}
	}
	public void handleStunFrames() {}
	public void handleMotionFrames() {}
	// pathfinding stuff
	protected boolean pathfinding = false;
	protected int pathfindVelocity = 500;
	protected int pathfindBoundary = 200;
	protected boolean boundToTarget = false;
	protected int maxSeparation = 400;
	protected VectorShape pathfindTarget;
	public void setPathfindTarget(VectorShape target, boolean bound) {
		pathfindTarget = target;
		boundToTarget = bound;
		pathfinding = true;
	}
	public VectorShape getPathfindTarget() {
		return pathfindTarget;
	}
	public void endPathfinding() {
		pathfinding = false;
	}
	public void setMaxSeparation(int sep) {
		maxSeparation = sep;
	}
	public void setPathfindBoundary(int bound) {
		pathfindBoundary = bound;
	}
	public void setPathfindVelocity(int v) {
		pathfindVelocity = v;
	}
	public void teleport(Vector2f pos) {
		translate(pos.sub(tail));
	}
	public void pathfind() {
		Vector2f pathfindVector = pathfindTarget.getFoot().sub(getFoot());
		if (boundToTarget && pathfindVector.magSquared() > maxSeparation*maxSeparation) {
			teleport(pathfindTarget.getTail());
		} else if (pathfindVector.magSquared() > maxSeparation*maxSeparation) {
			setMovementSpeed(0);
		} else {
			setMovementSpeed(pathfindVelocity);
			if (pathfindVector.getX() < -1 * pathfindBoundary) {
				if (movingRight) stopRight();
				moveLeft();
			} else if (pathfindVector.getX() > pathfindBoundary) {
				if (movingLeft) stopLeft();
				moveRight();
			} else {
				stopRight();
				stopLeft();
			}
			if (pathfindVector.getY() < -100) jump();
		}
	}
	public void drawVectors(Graphics g) {
		tail.draw(g, Vector2f.zero());
		vec.draw(g, tail);
		acceleration.draw(g, foot);
		velocity.draw(g, foot);
	}
	public String toString() {
		return "VectorShape at " + tail;
	}
}
