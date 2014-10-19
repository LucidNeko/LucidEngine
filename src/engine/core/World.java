package engine.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The world acts as both a container and factory for entities.
 * @author Hamish Rae-Hodgson.
 */
public class World {

	/** All the Entities in the world */
	private Map<Integer, Entity> entities = Collections.synchronizedMap(new HashMap<Integer, Entity>());

	/**
	 * Create an Entity with the given name and register it in this world.
	 * @param name The name of the Entity.
	 * @return The Entity that was created and added to this world.
	 */
	public Entity createEntity(String name) {
		return createEntity(getFreeID(), name);
	}

	/**
	 * Tries to create the Entity with the given ID.
	 * If that ID is already taken throws error.
	 * Otherwise creates the entity.
	 * @param id The id you want the Entity to have.
	 * @param name The name of the Entity.
	 * @return The entity.
	 */
	public Entity createEntity(int id, String name) {
		if(getEntity(id) != null)
			throw new IllegalStateException("You can't create an Entity with id=" + id + " because one already exists.");

		synchronized(entities) {
			Entity entity = new Entity(id, name);
			entities.put(entity.getID(), entity);
			return entity;
		}
	}
	
	/**
	 * Destroys the given entity from the world.
	 * @param entity The entity to destroy.
	 * @return true if the entity was in the world and it was removed<br>
	 * false if the entity was not in the world - thus no removal was performed.
	 */
	public boolean destroy(Entity entity) {
		return destroyEntity(entity.getID());
	}

	/**
	 * Destroy the Entity with id.
	 * @param id The id of the Entity to destroy.
	 * @return Returns true if there was an Entity with the id and it was removed.<br>
	 * 		   Returns false if there was no Entity meaning no removal.
	 */
	public boolean destroyEntity(int id) {
		synchronized(entities) {
			return entities.remove(id) != null;
		}
	}

	/**
	 * <b>O(1)</b> - Get the Entity in this world that has the id.
	 * @param id The id of the Entity you want.
	 * @return The Entity, or null if not found.
	 */
	public Entity getEntity(int id) {
		synchronized(entities) {
			return entities.get(id);
		}
	}
	
	/**
	 * <b>O(n)</b> - Returns an Entity with the given name. If more than one entity with 
	 * the name exist there is no guarantee which one will be returned.
	 * @param name The name of the Entity you are searching for.
	 * @return The Entity or null if none is found.
	 */
	public Entity getEntity(String name) {
		synchronized(entities) {
			for(Entity entity : entities.values())
				if(entity.getName().equals(name))
					return entity;
		}
		return null;
	}

	/**
	 * <b>O(n)</b> - Gets a List of Entities from this world that have the given name.
	 * @param name The name of the Entity.
	 * @return The list of entities. An empty list if none found. Never null.
	 */
	public List<Entity> getEntities(String name) {
		List<Entity> out = new ArrayList<Entity>(2);
		synchronized(entities) {
			for(Entity entity : entities.values())
				if(entity.getName().equals(name))
					out.add(entity);
		}
		return out;
	}

	/**
	 * Gets a snapshot of the entities currently in the world.
	 * @return The Collection of Entities.
	 */
	public synchronized Collection<Entity> getEntities() {
		synchronized(entities) {
			return new LinkedList<Entity>(entities.values());
		}
	}

	/**
	 * Gets an ID that doesn't clash with any of the other Entities in the world.
	 * @return The available/free ID.
	 */
	private int getFreeID() {
		int id;
		while(getEntity((id = ((int)(Math.random()*Integer.MAX_VALUE)))) != null);
		return id;
	}


}
