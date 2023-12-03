import mypackage.*;
import mypackage.shapes.*;
import java.util.ArrayList;

public class loadtest {
	public static void main(String[] args) {
		Level map = Level.load("testmapsave.txt");
		System.out.println(map.getEnemies());
		System.out.println(map.getEnvironment());
	}
}
