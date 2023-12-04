/*
Elvis Imamura
CSC 171 Final Project
Level class

holds the information and methods related to storing and loading level/map data. also contains referencable instances of ImageShape which represent the characters in the game
*/

package mypackage;
import mypackage.*;
import mypackage.shapes.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;

public class Level {

	private static Vector2f gravity = new Vector2f(0, (float) 2000);
	private String filepath;
	private ImageShape mario;
	private ImageShape bella;
	private Vector2f originalspawn = Vector2f.zero();
	private VectorRect spawn = new VectorRect(new Vector2f(0, 0), new Vector2f(0, 0));
	private ImageShape background = new ImageShape("sprites/backgrounds/bg_mountains.png", new Vector2f(800, 800), Vector2f.zero());
	private ArrayList<ImageShape> environment = new ArrayList<>();
	private ArrayList<ImageShape> enemies = new ArrayList<>();
	private ArrayList<VectorRect> boundary = new ArrayList<>();
	private ArrayList<Door> doors = new ArrayList<>();
	private ArrayList<ImageShape> doorimg = new ArrayList<>();
	private ArrayList<ImageShape> focusedShapes = new ArrayList<>();
	private ArrayList<VectorShape> movableShapes = new ArrayList<>();
	private ArrayList<ImageShape> draggableShapes = new ArrayList<>();
	private ImageShape spray;

	public static ImageShape getPurtee(Vector2f tail) {
		ImageShape purtee = new ImageShape("sprites/characters/purtee/purtee_R_still", new Vector2f(80, 120), tail) {{
			accelerate(gravity);
			setMotionFrames(
				new String[]{"sprites/characters/purtee/purtee_R_walk1.png", "sprites/characters/purtee/purtee_R_still.png", "sprites/characters/purtee/purtee_R_walk2.png", "sprites/characters/purtee/purtee_R_still.png"},
				new String[]{"sprites/characters/purtee/purtee_R_still.png"},
				new String[]{"sprites/characters/purtee/purtee_L_walk1.png", "sprites/characters/purtee/purtee_L_still.png", "sprites/characters/purtee/purtee_L_walk2.png", "sprites/characters/purtee/purtee_L_still.png"},
				new String[]{"sprites/characters/purtee/purtee_L_still.png"},
				new String[]{"sprites/characters/purtee/purtee_R_jump.png"},
				new String[]{"sprites/characters/purtee/purtee_L_jump.png"}
			);
			setMaxJump((byte) 1);
			setJump(-1200);
		}};
		return purtee;
	}
	public static ImageShape getBella(Vector2f tail) {
		ImageShape bella = new ImageShape("sprites/characters/bella/bella_R_still.png", new Vector2f(90, 60), tail) {{
			accelerate(gravity);
			setMaxJump((byte) 1);
			setMotionFrames(
				new String[]{"sprites/characters/bella/bella_R_walk1.png", "sprites/characters/bella/bella_R_still.png", "sprites/characters/bella/bella_R_walk2.png", "sprites/characters/bella/bella_R_still.png"},
				new String[]{"sprites/characters/bella/bella_R_still.png"},
				new String[]{"sprites/characters/bella/bella_L_walk1.png", "sprites/characters/bella/bella_L_still.png", "sprites/characters/bella/bella_L_walk2.png", "sprites/characters/bella/bella_L_still.png"},
				new String[]{"sprites/characters/bella/bella_L_still.png"},
				new String[]{"sprites/characters/bella/bella_R_jump.png"},
				new String[]{"sprites/characters/bella/bella_L_jump.png"}
			);
			//setPathfindTarget(mario, true);
			setPathfindBoundary(100);
			setPathfindVelocity(300);
			setMaxSeparation(600);
		}};
		return bella;
	}
	public static ImageShape getTerminator(Vector2f tail) {
		ImageShape terminator = new ImageShape("sprites/characters/terminator/terminator_R_still.png", new Vector2f(80, 120), tail) {{
			accelerate(gravity);
			setMotionFrames(
				new String[]{"sprites/characters/terminator/terminator_R_walk1.png", "sprites/characters/terminator/terminator_R_still.png", "sprites/characters/terminator/terminator_R_walk2.png", "sprites/characters/terminator/terminator_R_still.png"},
				new String[]{"sprites/characters/terminator/terminator_R_still.png"},
				new String[]{"sprites/characters/terminator/terminator_L_walk1.png", "sprites/characters/terminator/terminator_L_still.png", "sprites/characters/terminator/terminator_L_walk2.png", "sprites/characters/terminator/terminator_L_still.png"},
				new String[]{"sprites/characters/terminator/terminator_L_still.png"},
				new String[]{"sprites/characters/terminator/terminator_R_jump.png"},
				new String[]{"sprites/characters/terminator/terminator_L_jump.png"}
			);
			setStunFrames(
				new String[]{"sprites/characters/terminator/terminator_R_stun1.png", "sprites/characters/terminator/terminator_R_stun2.png", "sprites/characters/terminator/terminator_R_stun3.png", "sprites/characters/terminator/terminator_R_stun2.png"},
				new String[]{"sprites/characters/terminator/terminator_L_stun1.png", "sprites/characters/terminator/terminator_L_stun2.png", "sprites/characters/terminator/terminator_L_stun3.png", "sprites/characters/terminator/terminator_L_stun2.png"}
			);
			setPathfindBoundary(10);
			setPathfindVelocity(200);
			setMaxSeparation(600);
		}};
		return terminator;
	}
	public static ImageShape getSpray(Vector2f tail) {
		ImageShape spray = new ImageShape("sprites/dirt.png", new Vector2f(15, 15), tail) {{
			accelerate(gravity);
			setMotionFrames(
				new String[]{"sprites/environment/dirt.png"},
				new String[]{"sprites/environment/dirt.png"},
				new String[]{"sprites/environment/dirt.png"},
				new String[]{"sprites/environment/dirt.png"},
				new String[]{"sprites/environment/dirt.png"},
				new String[]{"sprites/environment/dirt.png"}
			);
			setLife(900);
			setMaxBounce(3);
		}};
		return spray;
	}
	public Level(VectorRect spawn, ArrayList<ImageShape> environment, ArrayList<ImageShape> enemies, ArrayList<VectorRect> boundary, ArrayList<Door> doors) {
		this.spawn = spawn;
		this.environment = environment;
		this.enemies = enemies;
		this.boundary = boundary;
		this.doors = doors;
		for (Door door : doors) {
			doorimg.add(door.getImage());
		}
		movableShapes.add(spawn);
		movableShapes.addAll(enemies);
		movableShapes.addAll(environment);
		movableShapes.addAll(doorimg);
		focusedShapes.addAll(enemies);
		draggableShapes.addAll(focusedShapes);
		draggableShapes.addAll(environment);
		draggableShapes.addAll(doorimg);
	}
	public static Level load(String filepath) {
		String file;
		VectorRect spawn;
		ArrayList<ImageShape> environment = new ArrayList<>();
		ArrayList<ImageShape> enemies = new ArrayList<>();
		ArrayList<VectorRect> boundary = new ArrayList<>();
		ArrayList<Door> doors = new ArrayList<>();
		String bg;
		try {
			FileReader fr = new FileReader(filepath);
			Scanner s = new Scanner(fr);
			file = s.nextLine(); 
			s.nextLine();
			spawn = new VectorRect(new Vector2f(0, 0), new Vector2f(s.nextFloat(), s.nextFloat()));
			s.next();
			while (!s.hasNext("ENEMIES")) {
				environment.add(new ImageShape(s.next(), new Vector2f(s.nextFloat(), s.nextFloat()), new Vector2f(s.nextFloat(), s.nextFloat())));
			}
			s.next();
			while (!s.hasNext("DOORS")) {
				enemies.add(getTerminator(new Vector2f(s.nextFloat(), s.nextFloat())));
			}
			s.next();
			while (!s.hasNext("BOUNDARY")) {
				doors.add(new Door(s.next(), new ImageShape(s.next(), new Vector2f(s.nextFloat(), s.nextFloat()), new Vector2f(s.nextFloat(), s.nextFloat()))));
			}
			s.next();
			while (!s.hasNext("BACKGROUND")) {
				boundary.add(new VectorRect(s.nextFloat(), s.nextFloat(), s.nextFloat(), s.nextFloat()));
			}
			s.next();
			if (s.hasNext()) {
				bg = s.next();
			} else {
				bg = "sprites/backgrounds/bg_mountains.png";
			}
			fr.close();
			System.out.println("Successfully loaded level from " + filepath);
			return new Level(spawn, environment, enemies, boundary, doors) {{
				setFilepath(file);
				setBackground(bg);
			}};
		} catch(Exception e) {
			System.out.println("Error loading map");
			System.out.println(e.getStackTrace());
			return null;
		}
	}
	public void save(String filepath) {
		try {
			FileWriter fw = new FileWriter(filepath);
			System.out.println("Saving level to " + filepath);
			fw.write(filepath + "\nSPAWNPOINT\n");
			fw.write("%f %f\nENVIRONMENT\n".formatted(spawn.getTail().getX(), spawn.getTail().getY()));
			for (ImageShape env : environment) {
				fw.write("%s %f %f %f %f\n".formatted(env.getPath(), env.getVec().getX(), env.getVec().getY(), env.getTail().getX(), env.getTail().getY()));
			}
			fw.write("ENEMIES\n");
			for (ImageShape enemy : enemies) {
				fw.write("%f %f\n".formatted(enemy.getTail().getX(), enemy.getTail().getY()));			
			}
			fw.write("DOORS\n");
			for (Door door: doors) {
				ImageShape img = door.getImage();
				fw.write("%s %s %f %f %f %f\n".formatted(door.getDestination(), img.getPath(), img.getVec().getX(), img.getVec().getY(), img.getTail().getX(), img.getTail().getY()));
			}
			fw.write("BOUNDARY\n");
			for (VectorShape bd : boundary) {
				fw.write("%f %f %f %f\n".formatted(bd.getVec().getX(), bd.getVec().getY(), bd.getTail().getX(), bd.getTail().getY()));
			}
			fw.write("BACKGROUND\n");
			fw.write(background.getPath());
			fw.close();
			System.out.println("Saved level file to " + filepath);
		} catch(Exception e) {
			System.out.println("Error saving level");
			System.out.println(e.getStackTrace());
		}
	}
	public ImageShape getBackground() {
		return background;
	}
	public void setBackground(String path) {
		background.setPath(path);
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String s) {
		filepath = s;
	}
	public Vector2f getOriginalSpawn() {
		return originalspawn;
	}
	public VectorRect getSpawnpoint() {
		return spawn;
	}
	public void setSpawnpoint(Vector2f spawnpoint) {
		spawn.setTail(spawnpoint);
		originalspawn = spawnpoint;
	}
	public ArrayList<ImageShape> getEnvironment() {
		return environment;
	}
	public void addEnvironment(ImageShape env) {
		environment.add(env);
		movableShapes.add(env);
		draggableShapes.add(env);
	}
	public void removeEnvironment(ImageShape env) {
		environment.remove(env);
		movableShapes.remove(env);
		focusedShapes.remove(env);
		draggableShapes.remove(env);
	}
	public ArrayList<ImageShape> getEnemies() {
		return enemies;
	}
	public void addEnemy(ImageShape enemy) {
		enemies.add(enemy);
		movableShapes.add(enemy);
		focusedShapes.add(enemy);
		draggableShapes.add(enemy);
	}
	public void removeEnemy(ImageShape enemy) {
		enemies.remove(enemy);
		movableShapes.remove(enemy);
		focusedShapes.remove(enemy);
		draggableShapes.remove(enemy);
	}
	public void addDoor(Door door) {
		doors.add(door);
		doorimg.add(door.getImage());
		movableShapes.add(door.getImage());
		draggableShapes.add(door.getImage());
	}
	public void removeDoor(Door door) {
		doors.remove(door);
		doorimg.remove(door.getImage());
		movableShapes.remove(door.getImage());
		draggableShapes.remove(door.getImage());
	}
	public ArrayList<Door> getDoors() {
		return doors;
	}
	public ArrayList<ImageShape> getDoorimg() {
		return doorimg;
	}
	public void addMario(ImageShape purtee) {
		this.mario = purtee;
		focusedShapes.add(purtee);
		movableShapes.add(purtee);
	}
	public void resetMario(ImageShape purtee) {
		mario = purtee;
	}
	public void addBella(ImageShape bella) {
		this.bella = bella;
		focusedShapes.add(bella);
		movableShapes.add(bella);
	}
	public void resetBella(ImageShape bella) {
		this.bella = bella;
	}
	public ArrayList<VectorRect> getBoundary() {
		return boundary;
	}
	public ArrayList<ImageShape> getFocusedShapes() {
		return focusedShapes;
	}
	public ArrayList<VectorShape> getMovableShapes() {
		return movableShapes;
	}
	public ArrayList<ImageShape> getDraggableShapes() {
		return draggableShapes;
	}
	public ImageShape getLevelSpray() {
		return spray;
	}
	public void addSpray(Vector2f tail, Vector2f velocity) {
		spray = getSpray(tail);
		spray.setVelocity(velocity);
		movableShapes.add(spray);
	}
	public void removeSpray() {
		movableShapes.remove(spray);
		spray = null;
	}
	public void clear() {
		environment.clear();
		enemies.clear();
		boundary.clear();
		focusedShapes.clear();
		movableShapes.clear();
		doors.clear();
		draggableShapes.clear();
		doorimg.clear();
	}
}
