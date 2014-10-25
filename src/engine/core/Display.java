package engine.core;

import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_CULL_FACE;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_FRONT;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static javax.media.opengl.GL.GL_SRC_ALPHA;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT_AND_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_COLOR_MATERIAL;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import java.awt.Dimension;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import cub3d.assets.Resources;
import engine.common.Quaternion;
import engine.common.Vec3;
import engine.input.Keyboard;
import engine.input.Mouse;
import engine.opengl.GL2Renderer;
import engine.opengl.GameCanvas;
import engine.opengl.Material;
import engine.opengl.Mesh;
import engine.opengl.Texture;
import engine.util.OBJBuilder;

public class Display extends GameCanvas {
	private static final long serialVersionUID = 4730629068123218844L;

	private static Display instance = null;
	
	private static JFrame frame = null;

	//gluPerspective params
	private static final float FIELD_OF_VIEW = 60;
	private static final float ZNEAR = 0.1f;
	private static final float ZFAR = 200;
	
	Mesh skybox_mesh = new OBJBuilder(Resources.getInputStream("skybox.obj")).getMesh();
	Material skybox_material = new Material(new Texture(Resources.getImage("skybox_dusk.png"), true), 1, 1, 1, 1);
	
	private Display() { /* Private constructor to prevent instantiation */ }
	
	public static void create(int width, int height) {
		if(instance == null) { //Only create once.
			instance = new Display();
			
			frame = new JFrame();
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			
			instance.setPreferredSize(new Dimension(width, height));
			Mouse.register(instance);
			Keyboard.register(instance);
			
			frame.getContentPane().add(instance);
			
			frame.pack();
			frame.setVisible(true);
		}
	}
	
	public static void setTitle(String title) {
		frame.setTitle(title);
	}
	
	public static void render() {
		instance.display();
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
	public void dispose(GLAutoDrawable drawable) { }

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glClear(GL_DEPTH_BUFFER_BIT);

		//clear the color buffer with the skybox.
		gl.glPushMatrix();
			gl.glDisable(GL_DEPTH_TEST);
			skybox_material.bind(gl);
			skybox_mesh.bind(gl);
			skybox_mesh.draw(gl);
			skybox_mesh.unbind(gl);
			skybox_material.unbind(gl);
			gl.glEnable(GL_DEPTH_TEST);
		gl.glPopMatrix();

		//render world
		gl.glPushMatrix();
			for(Entity entity : World.getEntities()) {
				gl.glPushMatrix();
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

		checkError(gl); //prints out error code if we get an error.
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

}
