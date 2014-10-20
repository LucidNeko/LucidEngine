package engine.common;

import java.util.Arrays;

/**
 * Mat44 is a class representing a 4x4 matrix.
 * @author Hamish Rae-Hodgson
 */
public class Mat44 {
	
	/////////////////
	//  Structure  //
	/////////////////
	// 00 04 08 12 //
	// 01 05 09 13 //
	// 02 06 10 14 //
	// 03 07 11 15 //
	/////////////////
	
	/** Column-Major Order - OpenGL memory layout */
	private float[] m;
	
	/** Construct a new Mat44 set to the identity matrix */
	public Mat44() {
		m = new float[16];
		m[0] = m[5] = m[10] = m[15] = 1;
	}
	
	/** Construct a new Mat44 with the values from source */
	public Mat44(Mat44 source) {
		m = Arrays.copyOf(source.m, 16);
	}
	
	/**
	 * Returns the matrix resulting from (this matrix (MULTIPLY) other matrix)<br>
	 * DOES NOT MODIFY LOCALLY.
	 * @param other matrix.
	 * @return The resulting matrix.
	 */
	public Mat44 mul(Mat44 other) {
		Mat44 out = new Mat44();
		out.m[0]  = this.m[0]*other.m[0]  + this.m[4]*other.m[1]  + this.m[8]*other.m[2]  + this.m[12]*other.m[3];
		out.m[4]  = this.m[0]*other.m[4]  + this.m[4]*other.m[5]  + this.m[8]*other.m[6]  + this.m[12]*other.m[7];
		out.m[8]  = this.m[0]*other.m[8]  + this.m[4]*other.m[9]  + this.m[8]*other.m[10] + this.m[12]*other.m[11];
		out.m[12] = this.m[0]*other.m[12] + this.m[4]*other.m[13] + this.m[8]*other.m[14] + this.m[12]*other.m[15];
		
		out.m[1]  = this.m[1]*other.m[0]  + this.m[5]*other.m[1]  + this.m[9]*other.m[2]  + this.m[13]*other.m[3];
		out.m[5]  = this.m[1]*other.m[4]  + this.m[5]*other.m[5]  + this.m[9]*other.m[6]  + this.m[13]*other.m[7];
		out.m[9]  = this.m[1]*other.m[8]  + this.m[5]*other.m[9]  + this.m[9]*other.m[10] + this.m[13]*other.m[11];
		out.m[13] = this.m[1]*other.m[12] + this.m[5]*other.m[13] + this.m[9]*other.m[14] + this.m[13]*other.m[15];
		
		out.m[2]  = this.m[2]*other.m[0]  + this.m[6]*other.m[1]  + this.m[10]*other.m[2]  + this.m[14]*other.m[3];
		out.m[6]  = this.m[2]*other.m[4]  + this.m[6]*other.m[5]  + this.m[10]*other.m[6]  + this.m[14]*other.m[7];
		out.m[10] = this.m[2]*other.m[8]  + this.m[6]*other.m[9]  + this.m[10]*other.m[10] + this.m[14]*other.m[11];
		out.m[14] = this.m[2]*other.m[12] + this.m[6]*other.m[13] + this.m[10]*other.m[14] + this.m[14]*other.m[15];
		
		out.m[3]  = this.m[3]*other.m[0]  + this.m[7]*other.m[1]  + this.m[11]*other.m[2]  + this.m[15]*other.m[3];
		out.m[7]  = this.m[3]*other.m[4]  + this.m[7]*other.m[5]  + this.m[11]*other.m[6]  + this.m[15]*other.m[7];
		out.m[11] = this.m[3]*other.m[8]  + this.m[7]*other.m[9]  + this.m[11]*other.m[10] + this.m[15]*other.m[11];
		out.m[15] = this.m[3]*other.m[12] + this.m[7]*other.m[13] + this.m[11]*other.m[14] + this.m[15]*other.m[15];
		return out;
	}
	
	/**
	 * Returns the vector resulting from (this matrix (MULTIPLY) vector).<br>
	 * DOES NOT MODIFY EITHER LOCALLY.
	 * @param vector
	 * @return The resulting vector.
	 */
	public Vec3 mul(Vec3 vector) {
		return new Vec3(m[0]*vector.x() + m[4]*vector.y() + m[ 8]*vector.z() + m[12], 
						m[1]*vector.x() + m[5]*vector.y() + m[ 9]*vector.z() + m[13], 
						m[2]*vector.x() + m[6]*vector.y() + m[10]*vector.z() + m[14]);
	}
	
	/** Get the Column-Major ordered data backing this matrix. */
	public float[] getData() {
		return m;
	}
	
	/** Create a Mat44 representing the translation by (dx, dy, dz). */
	public static Mat44 createTranslation(float dx, float dy, float dz) {
		Mat44 out = new Mat44();
		out.m[12] = dx;
		out.m[13] = dy;
		out.m[14] = dz;
		return out;
	}
	
	/** Create a Mat44 representing the scale of (sx, sy, sz). */
	public static Mat44 createScale(float sx, float sy, float sz) {
		Mat44 out = new Mat44();
		out.m[0]  = sx;
		out.m[5]  = sy;
		out.m[10] = sz;
		return out;
	}
	
	/** 
	 * Creates a Mat44 representing a rotation about the x axis by theta radians.
	 * @param theta Amount of rotation in radians.
	 * @return The matrix representing the rotation.
	 */
	public static Mat44 createRotationAroundX(float theta) {
		Mat44 out = new Mat44();
		float sin = Mathf.sin(theta);
		float cos = Mathf.sin(theta);
		out.m[5] = cos; out.m[9] = -sin;
		out.m[6] = sin; out.m[10] = cos;
		return out;
	}
	
	/** 
	 * Creates a Mat44 representing a rotation about the y axis by theta radians.
	 * @param theta Amount of rotation in radians.
	 * @return The matrix representing the rotation.
	 */
	public static Mat44 createRotationAroundY(float theta) {
		Mat44 out = new Mat44();
		float sin = Mathf.sin(theta);
		float cos = Mathf.sin(theta);
		out.m[0] = cos; out.m[8] = sin;
		out.m[2] = -sin; out.m[10] = cos;
		return out;
	}
	
	/** 
	 * Creates a Mat44 representing a rotation about the z axis by theta radians.
	 * @param theta Amount of rotation in radians.
	 * @return The matrix representing the rotation.
	 */
	public static Mat44 createRotationAroundZ(float theta) {
		Mat44 out = new Mat44();
		float sin = Mathf.sin(theta);
		float cos = Mathf.sin(theta);
		out.m[0] = cos; out.m[4] = -sin;
		out.m[1] = sin; out.m[5] = cos;
		return out;
	}

	/**
	 * Creates a Mat44 representing the rotation that the given Quaternion describes 
	 * @param q The Quaternion.
	 * @return The matrix representing the rotation.
	 */
	public static Mat44 createFromQuaternion(Quaternion q) {
		float w = q.w();
		float x = q.x();
		float y = q.y();
		float z = q.z();

		Mat44 out = new Mat44();
		
		if(q.magnitudeSquared() == 1) {
			//Unit Quaternion, speed hacks.
			//m[6] and m[9] should the addition/subtraction sign be swapped? TODO: Research.
			out.m[0] = 1 - 2*y*y - 2*z*z; out.m[4] = 2*x*y - 2*w*z;     out.m[8] = 2*x*z + 2*w*y;
			out.m[1] = 2*x*y + 2*w*z;     out.m[5] = 1 - 2*x*x - 2*z*z; out.m[9] = 2*y*z - 2*w*x; 
			out.m[2] = 2*x*z - 2*w*y;     out.m[6] = 2*y*z + 2*w*x;     out.m[10] = 1 - 2*x*x - 2*y*y;
		} else {
			//Non-unit quaternions don't represent true rotation... Should we just normalize first?
			//m[6] and m[9] should the addition/subtraction sign be swapped? TODO: Research.
			out.m[0] = w*w + x*x - y*y - z*z; out.m[4] = 2*x*y - 2*w*z;         out.m[8] = 2*x*z + 2*w*y;
			out.m[1] = 2*x*y + 2*w*z;         out.m[5] = w*w - x*x + y*y - z*z; out.m[9] = 2*y*z - 2*w*x; 
			out.m[2] = 2*x*z - 2*w*y;         out.m[6] = 2*y*z + 2*w*x;         out.m[10] = w*w - x*x - y*y + z*z;
			out.m[15] = w*w + x*x + y*y + z*z;
		}
		
		return out;
	}
	
	public Mat44 clone() {
		return new Mat44(this);
	}

	@Override
	public String toString() {
		return "Mat44 [" + Arrays.toString(Arrays.copyOfRange(m, 0, 4))   + ", " 
						 + Arrays.toString(Arrays.copyOfRange(m, 4, 8))   + ", "
						 + Arrays.toString(Arrays.copyOfRange(m, 8, 12))  + ", "
						 + Arrays.toString(Arrays.copyOfRange(m, 12, 16)) + "]";
	}

}
