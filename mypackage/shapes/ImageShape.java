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

public class ImageShape extends VectorShape {

	private String path;
	private BufferedImage img = null;

        public ImageShape(String path, Vector2f vec, Vector2f tail) {
                this.vec = vec; 
                this.tail = tail;
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
	public float getArea() {
		return height * width;
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
                initGraphics(g);
                g.drawImage(img, Math.round(x1), Math.round(y1), Math.round(width), Math.round(height), null);
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
