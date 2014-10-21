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

	/**
	 * Update this behaviour. Should be called every tick with the delta time.
	 * @param delta Delta time. i.e move(100*delta) would move 100 units per second. 
	 */
	public void update(float delta){ }
	
	public void fixedUpdate(float delta){ }
	
	public void onCollisionEnter(Collision collision){ }
	
	public void onCollisionExit(Collision collision){ }
	
	public void onCollisionStay(Collision collision){ }
	
	public void onTriggerEnter(Collision collision){ }
	
	public void onTriggerExit(Collision collision){ }
	
	public void onTriggerStay(Collision collision){ }
	
}
