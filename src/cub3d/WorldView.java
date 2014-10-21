package cub3d;

import static javax.media.opengl.GL2.*;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import engine.common.Mat44;
import engine.common.Quaternion;
import engine.common.Vec3;
import engine.core.Entity;
import engine.core.World;
import engine.opengl.GL2Renderer;
import engine.opengl.GameCanvas;

/**
 * The WorldView is a View of a World. Renders all the Renderer components on every Entity in the world.
 * @author Hamish Rae-Hodgson.
 *
 */
public class WorldView extends GameCanvas {
	private static final long serialVersionUID = 8996675374479682200L;

	//gluPerspective params
	private static final float FIELD_OF_VIEW = 60;
	private static final float ZNEAR = 0.1f;
	private static final float ZFAR = 200;

	private World world;
	/** Create a new WorldView over the given World. */
	public WorldView(World world, int width, int height) {
		super(width, height);
		this.world = world;
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glClearColor(0, 0, 0.2f, 1);
		gl.glEnable(GL_CULL_FACE);
		gl.glClearDepth(1f);
		gl.glDepthFunc(GL_LEQUAL);
		gl.glEnable(GL_DEPTH_TEST);

		gl.glEnable(GL_BLEND);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		gl.glEnable(GL_LIGHTING);
		gl.glEnable(GL_LIGHT0);
		gl.glEnable(GL_COLOR_MATERIAL);
		gl.glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.createGLU(gl).gluPerspective(FIELD_OF_VIEW, width/height, ZNEAR, ZFAR);
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glViewport(0, 0, width, height);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		renderWorld(gl);

		checkError(gl); //prints out error code if we get an error.
	}

	@Override
	public void dispose(GLAutoDrawable drawable) { 	}

	/** Render the world. */
	private void renderWorld(GL2 gl) {
		gl.glPushMatrix();
			for(Entity entity : world.getEntities()) {
				gl.glPushMatrix();
//					gl.glLoadMatrixf(Mat44.createFromTransform(entity.getTransform()).getData(), 0);
					Quaternion q = entity.getTransform().worldRotation();
					Vec3 t = entity.getTransform().worldPosition();
					gl.glTranslatef(t.x(), t.y(), t.z());
					gl.glRotatef((float) (2D*Math.acos(q.w())*180f / Math.PI), q.x(), q.y(), q.z());
					for(GL2Renderer renderer : entity.getComponents(GL2Renderer.class)) {
						renderer.render(gl);
					}
				gl.glPopMatrix();
			}
		gl.glPopMatrix();
	}

}
