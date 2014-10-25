package engine.core;

import java.util.concurrent.locks.LockSupport;

import engine.components.Behaviour;
import engine.tasks.TaskManager;

public class Engine {
	
	private static long then;
	
	private static boolean runningSlow = false;
	
	private static final long FPS60 = (long) ((1D/60)*1000000000);
	
	public static void start() {
		Time.reset();
		then = System.nanoTime();
		new Thread(new Runnable() {
			public void run() {
				for(;;) {
					step();
				}
			}
		}).start();
	}
	
	private static void step() {
		long now = System.nanoTime();
		Time.progressTime(now - then);
		then = now;
		tick();
		while(Time.getFixedTime() < Time.getTime()) {
			Time.progressFixedTime();
			fixedTick();
		}
		render();
		sleepforabit(FPS60-(System.nanoTime()-now));
	}
	
	private static void tick() {
		for(Entity e : World.getEntities()) {
			for(Behaviour b : e.getComponents(Behaviour.class)) {
				b.update();
			}
		}
	}
	
	private static void fixedTick() {
		for(Entity e : World.getEntities())
			for(Behaviour b : e.getComponents(Behaviour.class))
				b.fixedUpdate();
		
		TaskManager.tick();
	}
	
	private static void render() {
		Display.render();
	}
	
	private static void sleepforabit(long nanos) {
		if(nanos > 0)  {
			runningSlow = false;
			LockSupport.parkNanos(nanos);
		} else {
			runningSlow = true;
		}
	}

}
