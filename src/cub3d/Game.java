package cub3d;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cub3d.assets.Resources;
import engine.common.Mathf;
import engine.common.Vec3;
import engine.components.Transform.Space;
import engine.core.Entity;
import engine.core.GameLoop;
import engine.core.World;
import engine.input.Keyboard;
import engine.input.Mouse;
import engine.opengl.Material;
import engine.opengl.MeshFilter;
import engine.opengl.MeshRenderer;
import engine.util.OBJBuilder;

public class Game extends GameLoop {
	private static final Logger log = LogManager.getLogger();

	private static final int FPS = 60;
	private static final int FUPS = 30;
	
	World world;
	WorldView view;
	
	public Game() {
		super(FPS, FUPS);
		
		JFrame frame = new JFrame("Game");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		world = new World();
		view = new WorldView(world, 1920, 1080);
		Mouse.register(view);
		Keyboard.register(view);

		setup();
		
		frame.getContentPane().add(view);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	private void setup() {
		Entity link = world.createEntity("Link");
		link.attachComponent(MeshFilter.class).setMesh(new OBJBuilder(Resources.getInputStream("link.obj")).getMesh());
		link.attachComponent(MeshRenderer.class).setMaterial(new Material(Resources.getTexture("link.png", true), 1, 1, 1, 1));
		
		Entity teddy = world.createEntity("Teddy");
		teddy.attachComponent(MeshFilter.class).setMesh(new OBJBuilder(Resources.getInputStream("teddy.obj")).getMesh().getScaledInstance(0.5f));
		teddy.attachComponent(MeshRenderer.class).setMaterial(new Material(Resources.getTexture("teddy.png", true), 1, 1, 1, 1));
		teddy.getTransform().setParent(link.getTransform());
		teddy.getTransform().translate(0, 0, -1, Space.LOCAL);
		teddy.getTransform().rotate(Mathf.degToRad(45), Vec3.RIGHT(), Space.LOCAL);
	}

	@Override
	protected void tick(float delta) {
		if(Keyboard.isKeyDown(KeyEvent.VK_ESCAPE))
			System.exit(0);
		
		Entity link = world.getEntity("Link");
		
		if(Keyboard.isKeyDown(KeyEvent.VK_W))
			link.getTransform().translate(0, 0, 1*delta, Space.LOCAL);
		if(Keyboard.isKeyDown(KeyEvent.VK_A))
			link.getTransform().rotate(Mathf.degToRad(360*delta), Vec3.UP(), Space.LOCAL);
		if(Keyboard.isKeyDown(KeyEvent.VK_S))
			link.getTransform().translate(0, 0, -1*delta, Space.LOCAL);
		if(Keyboard.isKeyDown(KeyEvent.VK_D))
			link.getTransform().rotate(-Mathf.degToRad(360*delta), Vec3.UP(), Space.WORLD);		
		if(Keyboard.isKeyDown(KeyEvent.VK_SPACE))
			link.getTransform().rotate(Mathf.degToRad(360*delta), Vec3.RIGHT(), Space.LOCAL);
	}

	@Override
	protected void fixedTick(float delta) {
		
	}

	@Override
	protected void render() {
		view.display();
	}
	
	public static void main(String[] args) {
		new Game().start();
	}

}
