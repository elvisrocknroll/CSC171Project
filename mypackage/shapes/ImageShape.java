package mypackage.shapes;

import mypackage.Vector2f;
import java.lang.Math;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.*;
import javax.imageio.*;

public class ImageShape extends VectorShape implements Cloneable{

	protected String path;
	protected BufferedImage img = null;
	protected byte frameNumber = 0;
	protected String[] rightFrames;
	protected String[] rightStillFrames;
	protected String[] leftFrames;
	protected String[] leftStillFrames;
	protected String[] rightJumpFrames;
	protected String[] leftJumpFrames;
	protected String[] rightStunFrames;
	protected String[] leftStunFrames;
	protected String[] frames = rightStillFrames;
	protected boolean hasLife = false;
	protected int maxLife;
	protected int life = 0;
	protected int maxBounce = 100;
	
        public ImageShape(String path, Vector2f vec, Vector2f tail) {
                this.vec.set(vec); 
                this.tail.set(tail);
		this.path = path;
                calculatePosition(); 
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {}
        } 
	public String getPath() {
		return path;
	}
	public void setPath(String newPath) {
		this.path = newPath;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {}
	}
	public void updateFrame() {
		if (frameNumber >= frames.length - 1) {
			frameNumber = 0;
		} else {
			frameNumber++;
		}
		setPath(frames[frameNumber]);
	}
	public void setMotionFrames(String[] r, String[] rs, String[] l, String[] ls, String[] rj, String[] lj) {
		rightFrames = r;
		rightStillFrames = rs;
		leftFrames = l;
		leftStillFrames = ls;
		rightJumpFrames = rj;
		leftJumpFrames = lj;
	}
	public void setStunFrames(String[] right, String[] left) {
		leftStunFrames = left;
		rightStunFrames = right;
	}
	public void handleStunFrames() {
		if (facingRight) {
			setFrames(rightStunFrames);
		} else {
			setFrames(leftStunFrames);
		}
	}
	public void handleMotionFrames() {
		if (jumping && facingRight) {
			setFrames(rightJumpFrames);
		} else if (jumping && !facingRight) {
			setFrames(leftJumpFrames);
		} else {
			switch(movementDirection) {
			case -1:
				setFrames(leftFrames);
				break;
			case 1:
				setFrames(rightFrames);
				break;
			case 0:
				if (facingRight) {
					setFrames(rightStillFrames);
				} else {
					setFrames(leftStillFrames);
				}
				break;

			}
		}
	}
	public void setFrames(String[] newFrames) {
		frames = newFrames;
	}
	public float getArea() {
		return height * width;
	}
	public void setMaxBounce(int b) {
		maxBounce = b;
	}
	public void setLife(int newLife) {
		hasLife = true;
		life = newLife;
	}
	public boolean isAlive() {
		if (life <= 0 || bounceCount >= maxBounce) {
			return false;
		} else {
			return true;
		}
	}
	public int getLife() {
		return life;
	}
	public void stun(int time) {
		stunTimer = time;
	}
	public boolean contains(Vector2f point) {
		float x = point.getX();
		float y = point.getY();
                if (x >= (float) x1 && x <= (float) x2 && (float) y >= y1 && y <= y2)
			return true;
		return false;
	}
	public boolean contains(Point point) {
		float x = (float) point.getX();
		float y = (float) point.getY();
                if (x >= (float) x1 && x <= (float) x2 && (float) y >= y1 && y <= y2)
			return true;
		return false;
	}
	public boolean contains(float x, float y) {
                if (x >= (float) x1 && x <= (float) x2 && (float) y >= y1 && y <= y2)
			return true;
		return false;
	}
        public void draw(Graphics g) {
		if (!hasLife) {
			initGraphics(g);
			g.drawImage(img, Math.round(x1), Math.round(y1), Math.round(width), Math.round(height), null);
		} else if (isAlive()) {
			initGraphics(g);
			g.drawImage(img, Math.round(x1), Math.round(y1), Math.round(width), Math.round(height), null);
			life--;
		}
        }
        public void draw(Graphics g, int stroke) {
        }
        public void draw(Graphics g, Color outline) {
        }
        public void draw(Graphics g, int stroke, Color outline) {
        }
        public void fill(Graphics g) {
        }
        public void fill(Graphics g, Color fill) {
        }
        public void fill(Graphics g, Color fill, Color outline) {
        }
	public void fill(Graphics g, int stroke, Color fill, Color outline) {
        }
}
