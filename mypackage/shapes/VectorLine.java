package mypackage.shapes;

import mypackage.Vector2f;
import java.lang.Math;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;

public class VectorLine extends VectorShape {

	private float m;

	public VectorLine(Vector2f vec, Vector2f tail) {
		this.vec = vec;
		this.tail = tail;
		calculatePosition();
	}
	public float getArea() {
		return 0;
	}
	public void calculatePosition() {
		this.head = vec.add(tail);
		x1 = tail.getX();
		x2 = head.getX();
		y1 = tail.getY();
		y2 = head.getY();
		m  = (y2 - y1) / (x2 - x1);
	}
	public static VectorLine lineFromPoints(Vector2f tail, Vector2f head) {
		return new VectorLine(head.add(tail.scalarMult(-1)), tail);
	}
	public Vector2f getVec() {
		return vec;
	}
	public Vector2f getHead() {
		return head;
	}
	public float funcX(float x) {
		return m * (x - x1) + y1;
	}
	public boolean contains(Vector2f point) {
		float x = point.getX();
		float y = point.getY();
		if (x >= (float) Math.min(x1, x2) && x <= (float) Math.max(x1, x2) && Vector2f.epsilonComp(funcX(x), y))
			return true;
		return false;
	}
	public boolean contains(Point point) {
		float x = (float) point.getX();
		float y = (float) point.getY();
		if (x >= (float) Math.min(x1, x2) && x <= (float) Math.max(x1, x2) && Vector2f.epsilonComp(funcX(x), y))
			return true;
		return false;
	}
	public boolean contains(float x, float y) {
		if (x >= (float) Math.min(x1, x2) && x <= (float) Math.max(x1, x2) && Vector2f.epsilonComp(funcX(x), y))
			return true;
		return false;
	}
	public void draw(Graphics g) {
		initGraphics(g);
                g.drawLine(Math.round(x1), Math.round(y1), Math.round(x2),Math.round(y2));
	}
	public void draw(Graphics g, int stroke) {
                initGraphics(g, stroke);
		g.drawLine(Math.round(x1), Math.round(y1), Math.round(x2),Math.round(y2));
	}
	public void draw(Graphics g, int stroke, Color c) {
		initGraphics(g, stroke, color);
                g.drawLine(Math.round(x1), Math.round(y1), Math.round(x2),Math.round(y2));
	}
	public void draw(Graphics g, Color c) {
		initGraphics(g, color);
                g.drawLine(Math.round(x1), Math.round(y1), Math.round(x2),Math.round(y2));
	}
        public void fill(Graphics g) {}
        public void fill(Graphics g, Color fill) {}
        public void fill(Graphics g, Color fill, Color outline) {}
	public void fill(Graphics g, int stroke, Color fill, Color outline) {}
}
