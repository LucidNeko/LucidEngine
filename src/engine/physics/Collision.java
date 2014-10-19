package engine.physics;

import engine.components.Collider;
import engine.components.Transform;
import engine.core.Entity;

/**
 * Describes a collision.
 * @author Hamish Rae-Hodgson.
 */
public class Collision {
	
	private final Entity entity;
	private final Collider collider;
	private final Transform transform;
	
	/**
	 * Constructs a new Collision object containing the entity and collider that we hit.
	 * @param otherEntity The entity we hit.
	 * @param otherCollider The collider of the entity we hit.
	 */
	public Collision(Entity otherEntity, Collider otherCollider, Transform otherTransform) {
		this.entity = otherEntity;
		this.collider = otherCollider;
		this.transform = otherTransform;
	}

	/**
	 * Gets the entity that we hit.
	 * @return The entity that we hit.
	 */
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * Gets the Collider that we hit.
	 * @return The Collider that we hit.
	 */
	public Collider getCollider() {
		return collider;
	}
	
	/**
	 * The Transform of the entity we hit.
	 * @return The Transform of the entity we hit.
	 */
	public Transform getTransform() {
		return transform;
	}
	
}
