package cub3d;

import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cub3d.assets.Resources;
import engine.common.Mathf;
import engine.common.Vec3;
import engine.components.Behaviour;
import engine.components.Transform;
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
import engine.opengl.Texture;
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

		try {
			setup();
		} catch (IOException e) {
			log.error("Failed setting up game");
			e.printStackTrace();
		}
		
		frame.getContentPane().add(view);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	private void setup() throws IOException {
		Mesh mesh;
		Material material;
		
//		mesh = new OBJBuilder(Resources.getInputStream("teddy.obj")).getMesh().getScaledInstance(0.25f);
//		material = new Material(new Texture(Resources.getImage("teddy.png"), true), 1, 1, 1, 1);
		
		mesh = new OBJBuilder(Resources.getInputStream("link.obj")).getMesh();
		material = new Material(new Texture(Resources.getImage("link.png"), true), 1, 1, 1, 1);
		
		Entity link = world.createEntity("Link");
		link.attachComponent(MeshFilter.class).setMesh(mesh);
		link.attachComponent(MeshRenderer.class).setMaterial(material);
		link.attachComponent(new Behaviour() {

			@Override
			public void start() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void update(float delta) {
				Transform transform = getOwner().getTransform();
				if(Keyboard.isKeyDown(KeyEvent.VK_W))
					transform.translate(0, 0, 1*delta, Space.LOCAL);
				if(Keyboard.isKeyDown(KeyEvent.VK_A))
					transform.rotate(Mathf.degToRad(360*delta), Vec3.UP(), Space.LOCAL);
				if(Keyboard.isKeyDown(KeyEvent.VK_S))
					transform.translate(0, 0, -1*delta, Space.LOCAL);
				if(Keyboard.isKeyDown(KeyEvent.VK_D))
					transform.rotate(-Mathf.degToRad(360*delta), Vec3.UP(), Space.WORLD);		
				if(Keyboard.isKeyDown(KeyEvent.VK_SPACE))
					transform.rotate(Mathf.degToRad(360*delta), Vec3.RIGHT(), Space.LOCAL);
			}
		});

		Entity last = link;
		for(int i = 0; i < 20; i++) {
			Entity teddy = world.createEntity("Teddy" + i);
			teddy.attachComponent(MeshFilter.class).setMesh(mesh);
			teddy.attachComponent(MeshRenderer.class).setMaterial(material);
			teddy.getTransform().setParent(last.getTransform());
			teddy.getTransform().translate(0, Mathf.sin(i), -0.1f*(i+5), Space.LOCAL);
			teddy.attachComponent(new Behaviour() {

				@Override
				public void start() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void update(float delta) {
					getOwner().getTransform().rotate(Mathf.degToRad(10*delta), Vec3.UP(), Space.LOCAL);
				}
				
			});
			last = teddy;
		}
		
		
		
	}

	@Override
	protected void tick(float delta) {
		if(Keyboard.isKeyDown(KeyEvent.VK_ESCAPE))
			System.exit(0);
		
		for(Entity e : world.getEntities())
			for(Behaviour b : e.getComponents(Behaviour.class))
				b.update(delta);
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
