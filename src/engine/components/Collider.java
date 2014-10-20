package engine.components;

import engine.core.Component;
import engine.physics.AABB;

/** 
 * Defines a 3D collision shape.
 * @author Hamish Rae-Hodgson.
 */
public abstract class Collider extends Component {

	private boolean isTrigger = false;

	/**
	 * Gets the smallest AABB that fully encloses this Collider
	 * @return The AABB.
	 */
	public abstract AABB getAABB();
	
	/**
	 * Sets this colliders trigger state.
	 * @param isTrigger <br>If true -> isTrigger() will return true. <br>If false -> isTrigger() will return false.
	 */
	public void setTrigger(boolean isTrigger) {
		this.isTrigger = isTrigger;
	}
	
	/** Returns true if this Collider is a trigger. */
	public boolean isTrigger() {
		return isTrigger;
	}
	
}
