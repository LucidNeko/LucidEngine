package cub3d;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cub3d.assets.Resources;
import engine.common.Mathf;
import engine.common.Vec3;
import engine.components.Behaviour;
import engine.components.Transform.Space;
import engine.core.Entity;
import engine.core.GameLoop;
import engine.core.World;
import engine.input.Keyboard;
import engine.input.Mouse;
import engine.opengl.Material;
import engine.opengl.Mesh;
import engine.opengl.MeshFilter;
import engine.opengl.MeshRenderer;
import engine.physics.Collision;
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
		
		Mesh mesh = new OBJBuilder(Resources.getInputStream("teddy.obj")).getMesh().getScaledInstance(0.25f);
		Material material = new Material(Resources.getTexture("teddy.png", true), 1, 1, 1, 1);
		Entity last = link;
		for(int i = 0; i < 20; i++) {
			Entity teddy = world.createEntity("Teddy" + i);
			teddy.attachComponent(MeshFilter.class).setMesh(mesh);
			teddy.attachComponent(MeshRenderer.class).setMaterial(material);
			teddy.getTransform().setParent(last.getTransform());
			teddy.getTransform().translate(0, Mathf.sin(i), -0.5f*(i+1), Space.LOCAL);
			teddy.attachComponent(new Behaviour() {

				float theta = Mathf.random()*360;
				
				@Override
				public void update(float delta) {
					getOwner().getTransform().rotate(Mathf.degToRad(theta*delta), Vec3.UP(), Space.LOCAL);
				}
				
			});
		}
		
		
		
	}

	@Override
	protected void tick(float delta) {
		if(Keyboard.isKeyDown(KeyEvent.VK_ESCAPE))
			System.exit(0);
		
		for(Entity e : world.getEntities())
			for(Behaviour b : e.getComponents(Behaviour.class))
				b.update(delta);
		
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
