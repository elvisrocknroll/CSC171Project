package mypackage.shapes;
/*
Elvis Imamura
CSC 171 Final Project
VectorOval class

the Vector2f based implementation of swing graphics ovals. inherits from VectorShape
*/
import mypackage.Vector2f;
import java.lang.Math;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;

public class VectorOval extends VectorShape {

	public VectorOval(Vector2f vec, Vector2f tail) {
		this.vec = vec;
		this.tail = tail;
		calculatePosition();
	}
	public VectorOval(float x1, float y1, float x2, float y2) {
		this.vec = new Vector2f(x2-x1, y2-y1);
		this.tail = new Vector2f(x1, y1);
		calculatePosition();
	}
	public float getArea() {
		return (float) (Math.PI * height * width / 4.0); 
	}
	public Vector2f getVec() {
		return vec;
	}
	public Vector2f getTail() {
		return tail;
	}
	public Vector2f getHead() {
		return head;
	}
	public Vector2f getCenter() {
		return tail.add(vec.scalarMult((float) -0.5));
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
		g.drawOval(Math.round(x1), Math.round(y1), Math.round(width), Math.round(height));
	}
	public void draw(Graphics g, int stroke) {
		initGraphics(g, stroke);
		g.drawOval(Math.round(x1), Math.round(y1), Math.round(width), Math.round(height));
	}
	public void draw(Graphics g, Color outline) {
		initGraphics(g, outline);
		g.setColor(outline);
		g.drawOval(Math.round(x1), Math.round(y1), Math.round(width), Math.round(height));
	}
	public void draw(Graphics g, int stroke, Color outline) {
		initGraphics(g, stroke, outline);
		g.setColor(outline);
		g.drawOval(Math.round(x1), Math.round(y1), Math.round(width), Math.round(height));
	}
        public void fill(Graphics g) {
		initGraphics(g);
		g.fillOval(Math.round(x1), Math.round(y1), Math.round(width), Math.round(height));
	}
        public void fill(Graphics g, Color fill) {
		initGraphics(g, fill);
		g.fillOval(Math.round(x1), Math.round(y1), Math.round(width), Math.round(height));
	}
        public void fill(Graphics g, Color fill, Color outline) {
		initGraphics(g, fill);
		g.setColor(fill);
		g.fillOval(Math.round(x1), Math.round(y1), Math.round(width), Math.round(height));
		g.setColor(outline);
		g.drawOval(Math.round(x1), Math.round(y1), Math.round(width), Math.round(height));
	}
        public void fill(Graphics g, int stroke, Color fill, Color outline) {
		initGraphics(g, fill);
		g.setColor(fill);
		g.fillOval(Math.round(x1), Math.round(y1), Math.round(width), Math.round(height));
		initGraphics(g, stroke);
		g.setColor(outline);
		g.drawOval(Math.round(x1), Math.round(y1), Math.round(width), Math.round(height));
	}
}
