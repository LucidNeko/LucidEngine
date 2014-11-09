package engine.core;

import engine.common.Mat44;
import engine.common.Vec3;
import engine.components.Transform;

public class Camera {
	
	private static Camera instance = new Camera();
	
	private Transform transform = new Transform();
	
	public static Camera getInstance() {
		return instance;
	}
	
	public Transform getTransform() {
		return transform;
	}
	
	public Mat44 getMatrix() {
		Vec3 xAxis = transform.along();
		Vec3 yAxis = transform.up();
		Vec3 zAxis = transform.forward();
		Vec3 trans = transform.worldPosition();
		
		//yaw 180
		zAxis.negateLocal();
		xAxis.negateLocal();
		
		return new Mat44(
			xAxis.x(), xAxis.y(), xAxis.z(), -xAxis.dot(trans),
			yAxis.x(), yAxis.y(), yAxis.z(), -yAxis.dot(trans),
			zAxis.x(), zAxis.y(), zAxis.z(), zAxis.negateLocal().dot(trans),
			0, 0, 0, 1
		);
	}

}
