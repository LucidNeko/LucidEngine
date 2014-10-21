package engine.opengl;

import javax.media.opengl.GL2;

import engine.core.Component;

/**
 * An interface that all OpenGL renderable components must implement
 * @author Hamish Rae-Hodgson.
 *
 */
public abstract class GL2Renderer extends Component {

	/**
	 * Render this component to the provided gl context.
	 * @param gl The OpenGL context.
	 */
	public abstract void render(GL2 gl);
	
}
