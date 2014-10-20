package engine.common;

/**
 * The Quaternion class represents a Quaternion.
 * @author Hamish Rae-Hodgson
 */
public class Quaternion {
	
	private float w;
	private float x;
	private float y;
	private float z;
	
	//
	// CONSTRUCTORS
	//
	
	/** Construct a Quaternion initialized to the identity Quaternion. */
	public Quaternion() {
		setIdentity();
	}
	
	/** Construct a new Quaternion from the values. */
	public Quaternion(float w, float x, float y, float z) {
		set(w, x, y, z);
	}	
	
	/** Construct a new Quaternion from the values in the source Quaternion */
	public Quaternion(Quaternion source) {
		set(source);
	}
	
	//
	// GET
	//
	
	/** Get the w component of this Quaternion */
	public float w() { 
		return w; 
	}
	
	/** Get the x component of this Quaternion */
	public float x() { 
		return x; 
	}
	
	/** Get the y component of this Quaternion */
	public float y() { 
		return y; 
	}
	
	/** Get the z component of this Quaternion */
	public float z() { 
		return z; 
	}
	
	/** Get the w component of this Quaternion */
	public float getW() { 
		return w; 
	}
	
	/** Get the x component of this Quaternion */
	public float getX() { 
		return x; 
	}
	
	/** Get the y component of this Quaternion */
	public float getY() { 
		return y; 
	}
	
	/** Get the z component of this Quaternion */
	public float getZ() { 
		return z; 
	}
	
	//
	// SET
	//
	
	/** Set this Quaternion to the identity Quaternion (w=1, x=0, y=0, z=0) */
	public void setIdentity() {
		set(1, 0, 0, 0);
	}
	
	/** Set this Quaternion to the values found in source */
	public void set(Quaternion source) {
		set(source.w, source.x, source.y, source.z);
	}
	
	/** Set this Quaternion to the values passed in as parameters */
	public void set(float w, float x, float y, float z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}	
	
	/**
	 * Multiply this Quaternion by the other Quaternion. this * other.
	 * DOES NOT MODIFY LOCALLY.
	 * @param other The Quaternion to multiply by. 
	 * @return The resulting Quaternion.
	 */
	public Quaternion mul(Quaternion other) {
		return new Quaternion(this.w*other.w - this.x*other.x - this.y*other.y - this.z*other.z,
							  this.w*other.x + this.x*other.w + this.y*other.z - this.z*other.y,
							  this.w*other.y - this.x*other.z + this.y*other.w + this.z*other.x,
							  this.w*other.z + this.x*other.y - this.y*other.x + this.z*other.w);
	}
	
	/**
	 * Multiply this Quaternion by the other Quaternion. this * other. <br>
	 * MODIFIES LOCALLY.
	 * @param other The other Quaternion to multiply by.
	 * @return This Quaternion.
	 */
	public Quaternion mulLocal(Quaternion other) {
		float w = this.w*other.w - this.x*other.x - this.y*other.y - this.z*other.z;
		float x = this.w*other.x + this.x*other.w + this.y*other.z - this.z*other.y;
		float y = this.w*other.y - this.x*other.z + this.y*other.w + this.z*other.x;
		float z = this.w*other.z + this.x*other.y - this.y*other.x + this.z*other.w;
		set(w, x, y, z);
		return this;
	}
	
	/**
	 * Multiplies the given vector by this Quaternion. this * v<br>
	 * http://gamedev.stackexchange.com/a/50545
	 * @param v The vector. It should be a unit vector unless you know what you're doing..
	 * @return A new vector containing the result.
	 */
	public Vec3 mul(Vec3 v) {
		Vec3 u = new Vec3(x, y, z);
		float s = w;
		
		float dotUV = u.dot(v);
		float dotUU = u.dot(u);
		return u.mul(dotUV+dotUV).addLocal(v.mul(s*s - dotUU)).addLocal(u.cross(v).mul(s+s));
	}
	
//	/**
//	 * SLOW<br>
//	 * q*v*(q's conjugate)
//	 * Seems to drift way faster than other mul (mul2)
//	 * @param q Rotation Quaternion
//	 * @return The rotated vector. Does not modify param.
//	 */
//	public Vec3 mulSlow(Vec3 v) {
//		Quaternion V = new Quaternion(0, v.x(), v.y(), v.z());
//		Quaternion R = this.mul(V).mul(this.conjugate());
//		return new Vec3(R.x, R.y, R.z);
//	}
	
	/** Returns the conjugate of this Quaternion (w, -x, -y, -z). <br> DOES NOT MODIFY LOCALLY. */
	public Quaternion conjugate() {
		return new Quaternion(w, -x, -y, -z);
	}
	
	/** Sets this Quaternion as it's conjugate (w, -x, -y, -z).<br> MODIFIES LOCALLY. */
	public Quaternion conjugateLocal() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	
	/** Returns the length of this Quaternion */
	public float length() {
		return Mathf.sqrt(w*w + x*x + y*y + z*z);
	}
	
	/** Returns the squared length of this Quaternion */
	public float lengthSquared() {
		return w*w + x*x + y*y + z*z;
	}
	
	/**
	 * Normalize this Quaternion and return the length before normalization.
	 * @return The length before the Quaternion was normalized.
	 */
	public float normalize() {
		float lengthSquared = lengthSquared();
		if(lengthSquared <= 0) return 0;
		float length = Mathf.sqrt(lengthSquared);
		float invMag = 1/length;
		w *= invMag;
		x *= invMag;
		y *= invMag;
		z *= invMag;
		return length;
	}
	
	/**
	 * nlerps between a and b. t is clamped between 0..1.
	 * @return The resulting Quaternion.
	 */
	public static Quaternion nlerp(Quaternion a, Quaternion b, float t) {
		t = Mathf.clamp(t, 0, 1);
		return new Quaternion((1-t)*a.w + t*b.w,
							  (1-t)*a.x + t*b.x,
							  (1-t)*a.y + t*b.y,
							  (1-t)*a.z + t*b.z);
	}
	
	/** Creates a Quaternion representing a rotation of thetaRadians around the axis */
	public static Quaternion createRotation(float thetaRadians, Vec3 axis) {
		return createRotation(thetaRadians, axis.x(), axis.y(), axis.z());
	}
	
	/**
	 * Create a new Quaternion of theta/2 radians about the angle defined by the UNIT vector (x, y, z)
	 * @param theta The angle of rotation in radians.
	 * @param x Axis x component.
	 * @param y Axis y component.
	 * @param z Axis z component.
	 * @return The Quaternion.
	 */
	public static Quaternion createRotation(float theta, float x, float y, float z) {
		float halfTheta = theta/2;
		return new Quaternion(Mathf.cos(halfTheta), 
							x*Mathf.sin(halfTheta), 
							y*Mathf.sin(halfTheta), 
							z*Mathf.sin(halfTheta));
	}

	@Override
	public String toString() {
		return "Quaternion [w=" + w + ", x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
	public Quaternion clone() {
		return new Quaternion(w, x, y, z);
	}

}
