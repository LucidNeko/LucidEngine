package cub3d;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cub3d.assets.Resources;
import engine.common.Mathf;
import engine.common.Vec3;
import engine.components.Behaviour;
import engine.components.Transform;
import engine.components.Transform.Space;
import engine.core.Display;
import engine.core.Engine;
import engine.core.Entity;
import engine.core.GameLoop;
import engine.core.Time;
import engine.core.World;
import engine.input.Keyboard;
import engine.input.Mouse;
import engine.opengl.Material;
import engine.opengl.Mesh;
import engine.opengl.MeshFilter;
import engine.opengl.MeshRenderer;
import engine.opengl.Texture;
import engine.tasks.Task;
import engine.tasks.TaskManager;
import engine.util.OBJBuilder;

public class Game {
	private static final Logger log = LogManager.getLogger();
	
	public Game() {
		Display.create(1920, 1080);
		
		try {
			setup();
		} catch (IOException e) {
			log.error("Failed setting up game");
			e.printStackTrace();
		}
		
		Engine.start();
	}
	
	private void setup() throws IOException {
		Mesh mesh;
		Material material;
		
//		mesh = new OBJBuilder(Resources.getInputStream("teddy.obj")).getMesh().getScaledInstance(0.25f);
//		material = new Material(new Texture(Resources.getImage("teddy.png"), true), 1, 1, 1, 1);
		
		mesh = new OBJBuilder(Resources.getInputStream("link.obj")).getMesh();
		material = new Material(new Texture(Resources.getImage("link.png"), true), 1, 1, 1, 1);
		
		Entity link = World.createEntity("Link");
		link.getTransform().translate(0, 0, -10, Space.WORLD);
		link.attachComponent(MeshFilter.class).setMesh(mesh);
		link.attachComponent(MeshRenderer.class).setMaterial(material);
		link.attachComponent(new Behaviour() {

			@Override
			public void start() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void update() {
				float delta = Time.getDeltaTime();
				log.trace("time={}", delta);
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
			
			public void fixedUpdate() {
				log.trace("fixedTime={}", Time.getFixedDeltaTime());
			}
		});

		final Entity flink = link;
		final Mesh fmesh = mesh;
		final Material fmet = material;
		final Task task = new Task() {
			
			float time = 0;
			
			Entity last = flink;

			@Override
			public boolean isFinished() {
				return time > 1;
			}

			@Override
			public Object execute() {
				time += Time.getDeltaTime();
				
				Entity teddy = World.createEntity("Teddy" + time);
				teddy.attachComponent(MeshFilter.class).setMesh(fmesh);
				teddy.attachComponent(MeshRenderer.class).setMaterial(fmet);
				teddy.getTransform().setParent(last.getTransform());
				teddy.getTransform().translate(0, Mathf.sin(1*time), 1*time, Space.LOCAL);
				teddy.attachComponent(new Behaviour() { 
					public void start() { } 
					public void update() {
						getOwner().getTransform().rotate(Mathf.degToRad(10*Time.getDeltaTime()), Vec3.UP(), Space.LOCAL);
					} 
				});
				last = teddy;

				return null;
			}
			
		};
		TaskManager.addTask(task);
		
	}
	
	public static void main(String[] args) {
		new Game();
	}

}
