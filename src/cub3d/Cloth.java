package cub3d;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL2;

import engine.common.Mathf;
import engine.common.Vec3;
import engine.components.Behaviour;
import engine.core.Time;
import engine.input.Keyboard;
import engine.opengl.GL2Renderer;

public class Cloth extends Behaviour {

	public int _width;
	public int _height;
	public int _precision = 1;

	private int width;
	private int height;
	private int precision;

	private List<Particle> particles = new LinkedList<Particle>();
	private List<Constraint> constraints = new LinkedList<Constraint>();


	@Override
	public void start() {
		setSize(_width, _height);
		precision = _precision;
		reset();

//		getOwner().attachComponent(new GL2Renderer() {
//
//			@Override
//			public void render(GL2 gl) {
//				gl.glDisable(GL2.GL_LIGHTING);
//				gl.glColor3f(0, 0, 1);
//				gl.glPointSize(5);
//				gl.glBegin(GL2.GL_POINTS);
//					for(Particle p : particles) {
//						gl.glVertex3f(p.pos.x(), p.pos.y(), p.pos.z());
//					}
//				gl.glEnd();
//
//
//					for(Constraint c : constraints) {
//						if(c instanceof DistanceConstraint) {
//							gl.glColor3f(0, 1, 0);
//							gl.glBegin(GL2.GL_LINES);
//								DistanceConstraint dc = (DistanceConstraint)c;
//								gl.glVertex3f(dc.a.pos.x(), dc.a.pos.y(), dc.a.pos.z());
//								gl.glVertex3f(dc.b.pos.x(), dc.b.pos.y(), dc.b.pos.z());
//							gl.glEnd();
//						} else if(c instanceof PinConstraint) {
//							gl.glColor3f(1, 0, 0);
//							gl.glPointSize(3);
//							gl.glBegin(GL2.GL_POINTS);
//								PinConstraint pc = (PinConstraint)c;
//								gl.glVertex3f(pc.a.pos.x(), pc.a.pos.y(), pc.a.pos.z());
//							gl.glEnd();
//						}
//					}
//					gl.glEnable(GL2.GL_LIGHTING);
//			}
//
//		});
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void update() {
		for(int i = 0; i < 5; i++) {
			integrate(Time.getDeltaTime()*Time.getDeltaTime());
			solve();
		}
	}

	private void reset() {
		particles.clear();
		constraints.clear();

//		for(int y = 0; y < height; y++) {
//			for(int x = 0; x < width; x++) {
//				float step = 1/precision;
//				float currentStep = 0;
//				for(int i = 0; i < precision; i++) {
//					particles
//					currentStep += step;
//				}
//			}
//		}

		final Particle[][] ps = new Particle[height*precision][width*precision];
		for(int row = 0; row < ps.length; row++) {
			for(int col = 0; col < ps[row].length; col++) {
				ps[row][col] = new Particle(col*(width/(float)precision), row*(height/(float)precision), 0);
				particles.add(ps[row][col]);
			}
		}

		for(int row = 0; row < ps.length; row++) {
			for(int col = 0; col < ps[row].length; col++) {
				if(col+1 < ps[row].length)
					constraints.add(new DistanceConstraint(ps[row][col], ps[row][col+1]));
				if(row+1 < ps.length)
					constraints.add(new DistanceConstraint(ps[row][col], ps[row+1][col]));
			}
		}

		for(int col = 0; col < ps[0].length; col++) {
			constraints.add(new PinConstraint(ps[0][col]));
		}

		getOwner().attachComponent(new GL2Renderer() {



			@Override
			public void render(GL2 gl) {
				gl.glColor3f(0, 0, 0.8f);
				for(int row = 0; row < ps.length; row++) {
					for(int col = 0; col < ps[row].length; col++) {
						try {
							Vec3 a = ps[row][col].pos;
							Vec3 b = ps[row+1][col].pos;
							Vec3 c = ps[row+1][col+1].pos;
							Vec3 d = ps[row][col+1].pos;

							//tri 1
							{
								Vec3 normal = (b.sub(a)).cross(d.sub(a));
								normal.normalize();
								gl.glBegin(GL2.GL_TRIANGLES);
									gl.glVertex3f(a.x(), a.y(), a.z());
									gl.glNormal3f(normal.x(), normal.y(), normal.z());
									gl.glVertex3f(b.x(), b.y(), b.z());
									gl.glNormal3f(normal.x(), normal.y(), normal.z());
									gl.glVertex3f(d.x(), d.y(), d.z());
									gl.glNormal3f(normal.x(), normal.y(), normal.z());
								gl.glEnd();
							}
							
							//tri 1 - rev
							{
								Vec3 normal = (b.sub(a)).cross(d.sub(a));
								normal.normalize();
								gl.glBegin(GL2.GL_TRIANGLES);
									gl.glVertex3f(b.x(), b.y(), b.z());
									gl.glNormal3f(-normal.x(), -normal.y(), -normal.z());
									gl.glVertex3f(a.x(), a.y(), a.z());
									gl.glNormal3f(-normal.x(), -normal.y(), -normal.z());
									gl.glVertex3f(d.x(), d.y(), d.z());
									gl.glNormal3f(-normal.x(), -normal.y(), -normal.z());
								gl.glEnd();
							}

							//tri 2
							{
								Vec3 normal = (b.sub(d)).cross(c.sub(d));
								normal.normalize();
								gl.glBegin(GL2.GL_TRIANGLES);
									gl.glVertex3f(d.x(), d.y(), d.z());
									gl.glNormal3f(normal.x(), normal.y(), normal.z());
									gl.glVertex3f(b.x(), b.y(), b.z());
									gl.glNormal3f(normal.x(), normal.y(), normal.z());
									gl.glVertex3f(c.x(), c.y(), c.z());
									gl.glNormal3f(normal.x(), normal.y(), normal.z());
								gl.glEnd();
							}
							
							//tri 2 - rev
							{
								Vec3 normal = (b.sub(d)).cross(c.sub(d));
								normal.normalize();
								gl.glBegin(GL2.GL_TRIANGLES);
									gl.glVertex3f(b.x(), b.y(), b.z());
									gl.glNormal3f(-normal.x(), -normal.y(), -normal.z());
									gl.glVertex3f(d.x(), d.y(), d.z());
									gl.glNormal3f(-normal.x(), -normal.y(), -normal.z());
									gl.glVertex3f(c.x(), c.y(), c.z());
									gl.glNormal3f(-normal.x(), -normal.y(), -normal.z());
								gl.glEnd();
							}
						} catch(Exception e) {

						}
					}
				}
			}

		});

		getOwner().attachComponent(new Behaviour() {

			@Override
			public void start() {
				// TODO Auto-generated method stub

			}

			@Override
			public void update() {
				if(Keyboard.isKeyDown(KeyEvent.VK_P)) {
					ps[ps.length-1][0].pos.addLocal(0, 0, 100*Time.getDeltaTime());
					ps[ps.length-1][ps[0].length-1].pos.addLocal(0, 0, 100*Time.getDeltaTime());
				}
			}

		});
	}

	private void integrate(float deltaSquared) {
//		for(int i = 0; i < 2; i++)
			for(Particle p : particles)
				p.integrate(deltaSquared);
	}

	private void solve() {
		for(int i = 0; i < 7; i++)
			for(Constraint c : constraints)
				c.solve();
	}

	private class Particle {

		Vec3 oldPos, pos;

		public Particle(float x, float y, float z) {
			oldPos = new Vec3(x, y, z);
			pos = new Vec3(x, y, z);
		}

		public void integrate(float deltaSquared) {
			float nx = pos.x() + (pos.x() - oldPos.x())*0.98f + 0*deltaSquared;
			float ny = pos.y() + (pos.y() - oldPos.y())*0.98f - 1*deltaSquared;
			float nz = pos.z() + (pos.z() - oldPos.z())*0.98f + 0*deltaSquared;
			oldPos.set(pos);
			pos.set(nx, ny, nz);
		}

	}

	private interface Constraint {
		public void solve();
	}

	private class DistanceConstraint implements Constraint {

		private Particle a;
		private Particle b;
		private float length;

		public DistanceConstraint(Particle a, Particle b) {
			this.a = a;
			this.b = b;
			this.length = a.pos.sub(b.pos).length();
		}

		public void solve() {
			Vec3 diff = a.pos.sub(b.pos);
			float d = diff.length();
			float difference = (length - d) / d;
			Vec3 trans = diff.mul(0.5f).mul(difference);
			a.pos.addLocal(trans);
			b.pos.subLocal(trans);
		}

	}

	private class PinConstraint implements Constraint {

		private Particle a;
		private Vec3 pin;


		public PinConstraint(Particle a) {
			this.a = a;
			pin = a.pos.clone();
		}

		@Override
		public void solve() {
			a.pos.set(pin);
		}

	}

}
