package mypackage;

import java.lang.Math;
import java.awt.Graphics;
import mypackage.shapes.*;

public class Vector2f {

	protected float x;
	protected float y;
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public static Vector2f vecFromPoints(Vector2f head, Vector2f tail) {
		return new Vector2f(head.getX() - tail.getX(), head.getY() - tail.getY());
	}
	public static Vector2f vecFromPoints(float x1, float x2, float y1, float y2) {
		return new Vector2f(x2 - x1, y2 - y1);
	}
	public String toString() {
		return "<%.1f, %.1f>".formatted(x, y);	
	}
	public static final Vector2f zero() {
		return new Vector2f(0, 0);
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public void addX(float dx) {
		x += dx;
	}
	public void addY(float dy) {
		y += dy;
	}
	public void setX(float newX) {
		x = newX;
	}
	public void setY(float newY) {
		y = newY;
	}
	public void set(float newX, float newY) {
		x = newX;
		y = newY;
	}
	public void set(Vector2f other) {
		x = other.getX();
		y = other.getY();
	}
	public float magnitude() {
		return (float) Math.sqrt(x * x + y * y);
	}
	public float magSquared() {
		return (float) x * x + y * y;
	}
	public Vector2f add(Vector2f other) { // vector/component-wise addition
		return new Vector2f(x + other.getX(), y + other.getY()); 
	}
	public Vector2f add(float dx, float dy) {
		return new Vector2f(x + dx, y + dy);
	}
	public Vector2f sub(Vector2f other) {
		return new Vector2f(x - other.getX(), y - other.getY());
	}
	public Vector2f sub(float dx, float dy) {
		return new Vector2f(x - dx, y - dy);
	}
	public Vector2f unitVector() {
		return new Vector2f(x/magnitude(), y/magnitude());
	}
	public Vector2f unitVector(float c) {
		return new Vector2f(c * x/magnitude(), c * y/magnitude());
	}
	public float dot(Vector2f other) {
		return x * other.getX() + y * other.getY();
	}
	public float cross(Vector2f other) { // returns magnitude of cross product
		return (float) (this.x * other.getY() - this.y - other.getX());
	}
	public Vector2f scalarMult(float c) { // scalar multiplication
		return new Vector2f(x * c, y * c);
	}
	public Vector2f rotate(float theta, Vector2f tail) { // uses linear transformations to rotate about a given origin
		float cos = (float) Math.cos(Math.toRadians(theta));
		float sin = (float) Math.sin(Math.toRadians(theta));
		float tempX = x - tail.getX();
		float tempY = y - tail.getY();
		float newX = (float) (tempX * cos - tempY * sin) + tail.getX();
		float newY = (float) (tempX * sin - tempY * cos) + tail.getY();
		return new Vector2f(newX, newY);
	}
        public static boolean epsilonComp(float a, float b) {
                float epsilon = (float) (0.003 * Math.max(a, b));
                if (Math.abs(a - b) <= epsilon)
                        return true;
                return false;
	}
	public void draw(Graphics g, Vector2f tail) {
		VectorLine line = new VectorLine(this, tail) {{
			draw(g);
		}};
	}
}
