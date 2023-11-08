import mypackage.*;
import mypackage.shapes.*;

public class test3 {
	public static void main(String[] args) {
		Vector2f myTail = new Vector2f(2, 2);
		Vector2f vec = new Vector2f(3, 4);
		VectorShape shape = new ImageShape("sprites/mario.png", vec, myTail);
		System.out.println(shape.getTail());
		shape.translate(1, 2);
		System.out.println(shape.getTail());
	}
}
