package engine.components;

import engine.common.AABB;
import engine.core.Component;

/** 
 * Defines a 3D collision shape.
 * @author Hamish Rae-Hodgson.
 */
public abstract class Collider extends Component {

	/**
	 * Gets the smallest AABB that fully encloses this Collider
	 * @return The AABB.
	 */
	public abstract AABB getAABB();
	
}
