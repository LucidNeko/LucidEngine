package cub3d;

import engine.components.Behaviour;

public class Cloth extends Behaviour {
	
	public float width;
	public float height;
	
	private float _width;
	private float _height;
	

	@Override
	public void start() {
		setSize(width, height);
	}
	
	public void setSize(float width, float height) {
		this._width = width;
		this._height = height;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
