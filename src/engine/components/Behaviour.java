package engine.components;

import engine.core.Component;
import engine.physics.Collision;

/**
 * The Behaviour class encapsulates updateable behaviours.<br>
 * Example uses:
 *  - Player movement from input devices.
 *  - AI movement along a set path.
 * @author Hamish Rae-Hodgson.
 */
public abstract class Behaviour extends Component {
	
	/** Perform initialization here. Called once before the script starts. */
	public abstract void start();
	
	/**
	 * Update this behaviour. Use Time.getDeltaTime(). 
	 */
	public abstract void update();
	
	public void fixedUpdate(){ }
	
	public void onCollisionEnter(Collision collision){ }
	
	public void onCollisionExit(Collision collision){ }
	
	public void onCollisionStay(Collision collision){ }
	
	public void onTriggerEnter(Collision collision){ }
	
	public void onTriggerExit(Collision collision){ }
	
	public void onTriggerStay(Collision collision){ }
	
}
