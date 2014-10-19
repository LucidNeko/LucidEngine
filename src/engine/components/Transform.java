package engine.components;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import engine.common.Quaternion;
import engine.common.Vec3;
import engine.core.Component;

/**
 * The Transform class represents a position and rotation in 3D space.
 * @author Hamish Rae-Hodgson.
 */
public class Transform extends Component implements Iterable<Transform> {
	
	public enum Space { LOCAL, WORLD };
	
	private Transform parent = null;
	private List<Transform> children = new LinkedList<Transform>();
	
	private Vec3 localPosition = new Vec3();
	private Quaternion localRotation = new Quaternion();
	
	private Vec3 worldPosition = new Vec3();
	private Quaternion worldRotation = new Quaternion();
	
	private boolean dirty = true;
	
	public Transform() { }
	
	public Vec3 localPosition() {
		return localPosition.clone();
	}
	
	public Quaternion localRotation() {
		return localRotation.clone();
	}
	
	public Vec3 worldPosition() {
		if(isDirty()) 
			recalculate();
		return worldPosition.clone();
	}
	
	public Quaternion worldRotation() {
		if(isDirty()) 
			recalculate();
		return worldRotation.clone();
	}
	
	public Vec3 forward() {
		if(isDirty()) 
			recalculate();
		return worldRotation.mul(Vec3.FORWARD);
	}
	
	public Vec3 along() {
		if(isDirty()) 
			recalculate();
		return worldRotation.mul(Vec3.RIGHT);
	}
	
	public Vec3 up() {
		if(isDirty())
			recalculate();
		return worldRotation.mul(Vec3.UP);
	}
	
	public Transform root() {
		if(parent != null)
			return parent.root();
		return this;
	}
	
	public void detachChildren() {
		if(children.size() == 0) return;
		
		for(Transform child : this) {
			child.parent = null;
			child.setChanged();
		}
		
		children.clear();
	}
	
	/**
	 * Translate this transform by translation.<br>
	 * If space is Space.LOCAL then the translation happens in local space (strafe/walk/fly)<br>
	 * If space is Space.WORLD then the translation happens in world space. <br>
	 * @param Translation The amount to translate
	 * @param Space Relative space to translate to.
	 */
	public void translate(Vec3 translation, Space space) {
		translate(translation.x(), translation.y(), translation.z(), space);
	}
	
	/**
	 * Translate this transform by translation.<br>
	 * If space is Space.LOCAL then the translation happens in local space (strafe/walk/fly)<br>
	 * If space is Space.WORLD then the translation happens in world space. <br>
	 * @param dx Translation amount along x.
	 * @param dy Translation amount along y.
	 * @param dz Translation amount along z.
	 * @param space Space Relative space to translate to.
	 */
	public void translate(float dx, float dy, float dz, Space space) {
		switch(space) {
		case LOCAL :
			localPosition.addLocal(forward().mul(dz));
			localPosition.addLocal(along().mul(dx));
			localPosition.addLocal(up().mul(dy));
			setChanged();
			break;
		case WORLD :
			localPosition.addLocal(dx, dy, dz);
			setChanged();
			break;
		}
	}
	
	/**
	 * TODO: TEST ME.
	 * @param axis
	 * @param thetaRadians
	 */
	public void rotate(Vec3 axis, float thetaRadians, Space space) {
		switch(space) {
		case LOCAL :
			localRotation.mulLocal(Quaternion.createRotation(axis, thetaRadians));
			setChanged();
			break;
		case WORLD :
			if(isDirty()) 
				recalculate();
			
			localRotation.mulLocal(Quaternion.createRotation(worldRotation.mul(axis), thetaRadians));
			setChanged();
			break;
		}
		
	}

	/** Tests if this object is dirty - meaning it needs to be recalculated */
	public boolean isDirty() {
		return dirty;
	}
	
	/** 
	 * Marks this Transform as having been changed; 
	 * The hasChanged method will now return true.
	 * Recurses down through the children setting their flag too. 
	 * */
	public void setChanged() {
		dirty = true;
		for(Transform child : this)
			child.setChanged();
	}
	
	/** Indicates that this Transform has no longer changed. So that the hasChanged method will now return false. */
	public void clearChanged() {
		dirty = false;
	}
	
	/**
	 * Climbs to the root and then recalculates as needed down the tree to this Transform
	 */
	private void recalculate() {
		if(parent != null)
			parent.recalculate(); //first we climb to the top.
		
		if(!isDirty())
			return;
		
		if(parent != null) {
			worldPosition.set(parent.worldPosition.add(this.localPosition));
			worldRotation.set(parent.worldRotation.mul(this.localRotation));
			setChanged(); //set all children
		} else {
			worldPosition.set(localPosition);
			worldRotation.set(localRotation);
			setChanged(); //set all children
		}
		
		clearChanged(); //clear for self but all children still changed.
	}
	
	@Override
	public Iterator<Transform> iterator() {
		return children.iterator();
	}
	
	@Override
	public String toString() {
		if(isDirty()) recalculate();
		return "Transform [position=" + worldPosition + ", rotation=" + worldRotation
				+ ", localPosition=" + localPosition + ", localRotation="
				+ localRotation + "]";
	}
	
}
