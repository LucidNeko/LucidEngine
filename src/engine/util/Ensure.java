package engine.util;

import engine.components.Component;

/**
 * Ensure is a class that allows you ensure things about a given object.<br>
 * Ensure.that(object).isNotNull().isOfType(String.class);<br>
 * It will throw exceptions if anything is not as you ensure.
 * @author Hamish Rae-Hodgson.
 *
 */
public class Ensure {
	
	public static Ensureable that(Object object) {
		return new Ensureable(object);
	}

	public static class Ensureable {
		
		protected Object object;
		
		public Ensureable(Object object) {
			this.object = object;
		}
		
		public Ensureable isNull() {
			if(object != null)
				throw new IllegalStateException("Object must be null");
			return this;
		}
		
		public Ensureable isNotNull() {
			if(object == null)
				throw new IllegalStateException("Object cannot be null.");
			return this;
		}
		
		public Ensureable hasNoOwner() {
			if(Component.class.isAssignableFrom(object.getClass()) && ((Component)object).getOwner() != null)
				throw new IllegalStateException("Component cannot have an owner.");
			return this;
		}
		
		public Ensureable isOfType(Class<?> type) {
			if(type != object.getClass())
				throw new IllegalStateException("Object must be of type " + type.getName());
			return this;
		}
		
		public Ensureable isNotOfType(Class<?> type) {
//			if(type.isAssignableFrom(object.getClass())) //allows subtypes to fail too.
			if(type == object.getClass())
				throw new IllegalStateException("Object cannot be of type " + type.getName());
			return this;
		}

	}
	
}
