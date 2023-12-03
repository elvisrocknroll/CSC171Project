import mypackage.*;
import mypackage.shapes.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;

public class loadtest2 {
	public static void main(String[] args) {
		try {
                        FileReader fr = new FileReader("testmapsave.txt");
                        Scanner s = new Scanner(fr);
                        s.nextLine(); s.nextLine();
                        System.out.println(new Vector2f(s.nextFloat(), s.nextFloat()));
                        s.next();
                        while (!s.hasNext("ENEMIES")) {
                                System.out.println(new ImageShape(s.next(), new Vector2f(s.nextFloat(), s.nextFloat()), new Vector2f(s.nextFloat(), s.nextFloat())));
                        }
                        s.next();
                        while (!s.hasNext("BOUNDARY")) {
                                System.out.println(Level.getTerminator(s.nextFloat(), s.nextFloat()));
                        }
                        s.next();
                        while (s.hasNext()) {
                                System.out.println(new VectorRect(s.nextFloat(), s.nextFloat(), s.nextFloat(), s.nextFloat()));
                        }
                        fr.close();
                        System.out.println("Successfully loaded level from haha");
		} catch(Exception e) {
                        System.out.println("Error loading map");
                        System.out.println(e.getStackTrace());
                }
        }
/*
        public void save(String filepath) {
                try {
                        FileWriter fw = new FileWriter(filepath);
                        System.out.println("Saving level to " + filepath);
                        fw.write(filepath + "\nSPAWNPOINT\n");
                        fw.write("%f %f\nENVIRONMENT\n".formatted(spawn.getX(), spawn.getY()));
                        for (ImageShape env : environment) {
                                fw.write("%s %f %f %f %f\n".formatted(env.getPath(), env.getVec().getX(), env.getVec().getY(), env.getTail().getX(), env.getTail().getY()));
                        }
                        fw.write("ENEMIES\n");
                        for (ImageShape enemy : enemies) {
                                fw.write("%f %f\n".formatted(enemy.getTail().getX(), enemy.getTail().getY()));                  
                        }
                        fw.write("BOUNDARY\n");
                        for (VectorShape bd : boundary) {
                                fw.write("%f %f %f %f\n".formatted(bd.getVec().getX(), bd.getVec().getY(), bd.getTail().getX(), bd.getTail().getY()));
                        }
                        System.out.println("Saved level file to " + filepath);
                        fw.close();
	}
*/
}
