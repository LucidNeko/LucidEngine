package engine.opengl;

import static javax.media.opengl.GL2.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;

import javax.media.opengl.GL2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Texture represents a Texture. The first time you bind it it will load it into graphics memory
 * storing the handle returned and nullifying the picel array that was passed in.
 * @author Hamish Rae-Hodgson.
 *
 */
public class Texture {
	private static final Logger log = LogManager.getLogger();

	private int id = -1; //-1 means not yet registered with OpenGL
	private int width;
	private int height;

	private byte[] pixels;

	/**
	 * Create a new Texture with the width and height and pixels containing the image data.
	 * @param width
	 * @param height
	 * @param pixels
	 */
	public Texture(BufferedImage image, boolean flip) {
		//@ensures image is of the right underlying format.
		BufferedImage buffy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = buffy.createGraphics();
		g.drawImage(image, null, 0, 0);
		g.dispose();
		image = buffy;
		
		DataBuffer imageBuffer = image.getRaster().getDataBuffer();
		//DataBuffer must be of type DataBufferByte
		byte[] pixels = ((DataBufferByte)imageBuffer).getData();
		convertABGRtoRGBAinPlace(pixels);
		if(flip) yFlipInPlace(pixels, image.getWidth(), image.getHeight());
		
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.pixels = pixels;
	}
	
	/**
	 * Bind this Texture to the gl context.
	 * @param gl The gl context.
	 */
	public void bind(GL2 gl) {
		if(id == -1) loadImageIntoGL(gl);

		gl.glBindTexture(GL_TEXTURE_2D, id);
	}

	/**
	 * The first time we try bind, the image isn't loaded into graphics memory. Load it.
	 * @param gl The gl context.
	 */
	private void loadImageIntoGL(GL2 gl) {
		int[] id = new int[1];
		gl.glGenTextures(1, id, 0);
		log.trace("loadImageIntoGL: id={}", id[0]);
		gl.glBindTexture(GL_TEXTURE_2D, id[0]);
		gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		gl.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, ByteBuffer.wrap(pixels));
		int errorCode = gl.glGetError();
		if(gl.glGetError() == GL_NO_ERROR) {
			this.id = id[0];
			pixels = null;
		} else {
			log.error("Failed binding texture. GL_ERROR={}", errorCode);
		}
	}
	
	/**
	 * Modifies data in place. Flips an image upsidedown.
	 * @param data The byte array containing pixel data
	 * @param width The width of the image.
	 * @param height The height of the image.
	 */
	private static void yFlipInPlace(byte[] data, int width, int height) {
		int colorsPerRow = 4*width; //How many array indicies per row
		int half = height/2; //We travel down half the rows swapping over
		for(int i = 0; i < half; i++) {
			int top = i*colorsPerRow; //index of the start of the row on the top side
			int bot = ((height-1)-i)*colorsPerRow; //index of the start of the row on the bottom side
			//travel across the columns swapping top and bot
			for(int x = 0; x < colorsPerRow; x++) {
				byte tmp = data[top+x];
				data[top+x] = data[bot+x];
				data[bot+x] = tmp;
			}
		}
	}

	/**
	 * Modifies data in place. Chages the byte order for each pixel from ABGR to RGBA.
	 * @param data The byte array containing pixel data.
	 */
	private static void convertABGRtoRGBAinPlace(byte[] data) {
		for(int i = 0; i < data.length; i+=4) {
			byte tmp;

			//swap (x, 0, 0, y)
			tmp = data[i+0];
			data[i+0] = data[i+3];
			data[i+3] = tmp;

			//swap (0, x, y, 0)
			tmp = data[i+1];
			data[i+1] = data[i+2];
			data[i+2] = tmp;
		}
	}

}
