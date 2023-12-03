import mypackage.*;
import mypackage.shapes.*;
import java.util.ArrayList;

public class savetest2 {
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

		String filepath = "haha";

		System.out.println(filepath + "\nSPAWNPOINT");
		System.out.println("%f %f\nENVIRONMENT".formatted(spawn.getX(), spawn.getY()));
		for (ImageShape env : environment) {
			System.out.println("%s %f %f %f %f".formatted(env.getPath(), env.getVec().getX(), env.getVec().getY(), env.getTail().getX(), env.getTail().getY()));
		}
		System.out.println("ENEMIES");
		for (ImageShape enemy : enemies) {
			System.out.println("%f %f".formatted(enemy.getTail().getX(), enemy.getTail().getY()));                  
		}
		System.out.println("BOUNDARY");
		for (VectorShape bd : boundary) {
			System.out.println("%f %f %f %f".formatted(bd.getVec().getX(), bd.getVec().getY(), bd.getTail().getX(), bd.getTail().getY()));
		}



//		map.save("testmapsave.txt");
	}
}
