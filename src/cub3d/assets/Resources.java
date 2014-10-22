package cub3d.assets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Resource class has static methods for loading assets.<br>
 * Assets must be located in the root assets directory/package (or a subfolder therein).
 * @author Hamish Rae-Hodgson.
 *
 */
public class Resources {
	
	/**
	 * Gets an InputStream over the file at fname.
	 * @param fname The file name.
	 * @return The InputStream.
	 */
	public static InputStream getInputStream(String fname) {
		return Resources.class.getResourceAsStream(fname);
	}

	/**
	 * Gets a URL representing the file located at fname.
	 * @param fname The file name
	 * @return The URL.
	 */
	public static URL getURL(String fname) {
		return Resources.class.getResource(fname);
	}

	/** 
	 * Loads an image from the assets directory.
	 * @param fname The file name.
	 * @return The loaded image.
	 * @throws IOException If there is an error loading the image.
	 */
	public static BufferedImage getImage(String fname) {
		try {
			return ImageIO.read(getURL(fname));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
