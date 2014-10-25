package engine.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Time {
	
	/** Time simulated */
	private static long time = 0;
	private static long lastTime = 0;

	/** Fixed time simulated */
	private static long fixedTime = 0;
	private static final long fixedDeltaTime = secondsToNanos(1/120f);
	
	private Time() { /* Private constructor to prevent instantiation */ }
	
	/** Reset the Time */
	static void reset() {
		time = lastTime = fixedTime = 0;
	}
	
	/** The delta time to advance the simulation */
	public static float getDeltaTime() {
		return nanosToSeconds(time - lastTime);
	}
	
	/** The unchanging delta time to advance simulation inside fixedTick() */
	public static float getFixedDeltaTime() {
		return nanosToSeconds(fixedDeltaTime);
	}
	
	/** The time in seconds since the start of the game */
	public static float getTime() {
		return nanosToSeconds(time);
	}
	
	/** The time in seconds that has been passed in fixedTick updates */
	public static float getFixedTime() {
		return nanosToSeconds(fixedTime);
	}
	
	/** Progress time by nanos */
	static void progressTime(long nanos) {
		lastTime = time;
		time = time + nanos;
	}
	
	/** Progress fixedTime by fixedDeltaTime */
	static void progressFixedTime() {
		fixedTime = fixedTime + fixedDeltaTime;
	}
	
	/** Converts the given nanoseconds to seconds */
	private static float nanosToSeconds(long nanos) {
		return nanos*0.000000001f;
	}

	/** Converts the given seconds to nanoseconds */
	private static long secondsToNanos(float seconds) {
		return (long) (seconds*1000000000L);
	}
	
	public static void main(String[] args) {
		Logger log = LogManager.getLogger();
		log.trace("dt={} fdt={}, t={} ft={}", getDeltaTime(), getFixedDeltaTime(), getTime(), getFixedTime());
		for(int i = 0; i < 60000; i++) {
			progressTime(secondsToNanos(1/60f));
			progressFixedTime();
			log.trace("dt={} fdt={}, t={} ft={}", getDeltaTime(), getFixedDeltaTime(), getTime(), getFixedTime());
			progressFixedTime();
			log.trace("dt={} fdt={}, t={} ft={}", getDeltaTime(), getFixedDeltaTime(), getTime(), getFixedTime());
			log.trace("--------------------------------------------------------");
		}
	}
	
}
