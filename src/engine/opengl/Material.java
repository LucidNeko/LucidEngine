package engine.opengl;

import static javax.media.opengl.GL2.GL_TEXTURE_2D;

import javax.media.opengl.GL2;

/**
 * A Material is the Combination of a texture and a color.<br>
 * A Material with a white color will render the texture as is. <br>
 * Changing the color you can add a hue to objects.
 * @author Hamish Rae-Hodgson.
 *
 */
public class Material {
	
	private float[] rgba = new float[4];
	private Texture texture;
	
	/**
	 * Create a Material with the given Texture and Color.
	 * @param texture The texture.
	 * @param color The color.
	 */
	public Material(Texture texture, float r, float g, float b, float a) {
		this.texture = texture;
		this.rgba[0] = r;
		this.rgba[1] = g;
		this.rgba[2] = b;
		this.rgba[3] = a;
	}
	
	public void bind(GL2 gl) {
		if(texture != null) {
			gl.glEnable(GL_TEXTURE_2D);
			texture.bind(gl);
		}
		gl.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
	}
	
	public void unbind(GL2 gl) {
		if(texture != null) {
			gl.glDisable(GL_TEXTURE_2D);
		}
	}
	

}
