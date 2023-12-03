import mypackage.*;
import mypackage.shapes.*;
import java.util.ArrayList;

public class savetest {
	public static void main(String[] args) {
		Vector2f spawn = new Vector2f(0, 0);
		ArrayList<ImageShape> environment = new ArrayList<>() {{
			add(new ImageShape("sprites/dirt.png", new Vector2f(10, 10), new Vector2f(20, 20)));
		}};
		ArrayList<ImageShape> enemies = new ArrayList<>() {{
			add(Level.getTerminator(420, 69));
		}};
		ArrayList<VectorRect> boundary = new ArrayList<>() {{
			add(new VectorRect(new Vector2f(101, 10), new Vector2f(20, 20)));
		}};
		Level map = new Level(spawn, environment, enemies, boundary);
		map.save("testmapsave.txt");
	}
}
