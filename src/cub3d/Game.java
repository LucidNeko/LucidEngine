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
import engine.core.Camera;
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

		Mouse.setGrabbed(true);

		Engine.start();
	}

	private void setup() throws IOException {
		Mesh mesh;
		Material material;

//		mesh = new OBJBuilder(Resources.getInputStream("teddy.obj")).getMesh().getScaledInstance(0.25f);
//		material = new Material(new Texture(Resources.getImage("teddy.png"), true), 1, 1, 1, 1);

		mesh = new OBJBuilder(Resources.getInputStream("link.obj")).getMesh();
		material = new Material(new Texture(Resources.getImage("link.png"), true), 1, 1, 1, 1);

		Entity cloth = World.createEntity("Cloth");
		Cloth c = cloth.attachComponent(Cloth.class);
		c._width = 2;
		c._height = 2;
		c._precision = 10;
		c.start();

		Entity link = World.createEntity("Player");
		link.attachComponent(new Behaviour() {

			@Override
			public void start() {
				// TODO Auto-generated method stub

			}

			@Override
			public void update() {

				if(Keyboard.isKeyDown(KeyEvent.VK_ESCAPE))
					System.exit(0);

				float delta = Time.getDeltaTime();
				Transform transform = getOwner().getTransform();

				float mdx = Mouse.getDX();
				float mdy = Mouse.getDY();

				if(mdx != 0) {
					transform.rotate(-mdx*delta*0.2f, Vec3.UP(), Space.WORLD);
				}

				if(mdy != 0) {
					transform.rotate(-mdy*delta*0.2f, Vec3.RIGHT(), Space.LOCAL);
				}

				if(Keyboard.isKeyDown(KeyEvent.VK_W))
					transform.translate(0, 0, 5*delta, Space.LOCAL);
				if(Keyboard.isKeyDown(KeyEvent.VK_A))
					transform.translate(5*delta, 0, 0, Space.LOCAL);
				if(Keyboard.isKeyDown(KeyEvent.VK_S))
					transform.translate(0, 0, -5*delta, Space.LOCAL);
				if(Keyboard.isKeyDown(KeyEvent.VK_D))
					transform.translate(-5*delta, 0, 0, Space.LOCAL);
			}

			public void fixedUpdate() {


			}
		});

//		Camera.getInstance().getTransform().translate(0, 0, -1, Space.LOCAL);
		Camera.getInstance().getTransform().setParent(link.getTransform());

		final Entity flink = link;
		final Mesh fmesh = mesh;
		final Material fmet = material;

//		TaskManager.addTask(new Task() {
//
//			float time = 1f;
//
//			@Override
//			public boolean isFinished() {
//				return time <= 0;
//			}
//
//			@Override
//			public Object execute() {
//				time -= Time.getDeltaTime();
//
//				final Entity teddy = World.createEntity("Teddy" + time);
//				teddy.attachComponent(MeshFilter.class).setMesh(fmesh);
//				teddy.attachComponent(MeshRenderer.class).setMaterial(fmet);
//				teddy.getTransform().translate(-20+Mathf.random()*40, 0, -20+Mathf.random()*40, Space.LOCAL);
//				teddy.getTransform().rotate(Mathf.PI*2*Mathf.random(), Vec3.UP(), Space.LOCAL);
//
//				TaskManager.addTask(new Task() {
//
//					float time = 0;
//
//					Entity last = teddy;
//
//					@Override
//					public boolean isFinished() {
//						return time > 3.5f;
//					}
//
//					@Override
//					public Object execute() {
//						time += Time.getDeltaTime();
//
//						Entity teddy = World.createEntity("Teddy" + time);
//						teddy.attachComponent(MeshFilter.class).setMesh(fmesh);
//						teddy.attachComponent(MeshRenderer.class).setMaterial(fmet);
//						teddy.getTransform().setParent(last.getTransform());
//						teddy.getTransform().translate(0, Mathf.sin(1*time), 1*time, Space.LOCAL);
//						teddy.attachComponent(new Behaviour() {
//							public void start() { }
//							public void update() {
//								getOwner().getTransform().rotate(Mathf.degToRad(10*Time.getDeltaTime()), Vec3.UP(), Space.LOCAL);
//							}
//						});
//						last = teddy;
//
//						return null;
//					}
//
//					@Override
//					public void remove() {
//						// TODO Auto-generated method stub
//
//					}
//
//				});
//
//				return null;
//			}
//
//			@Override
//			public void remove() {
//				// TODO Auto-generated method stub
//
//			}
//
//		});

	}

	public static void main(String[] args) {
		new Game();
	}

}
