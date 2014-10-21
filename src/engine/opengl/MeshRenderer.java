package engine.opengl;

import javax.media.opengl.GL2;

public class MeshRenderer extends GL2Renderer {

	private Material material;

	/**
	 * Sets the Material that this MeshRenderer uses.
	 * @param material The Material. Cannot be null.
	 */
	public void setMaterial(Material material) {
		this.material = material;
	}

	public Material getMaterial() {
		return material;
	}

	@Override
	public void render(GL2 gl) {
		requires(MeshFilter.class);

		Mesh mesh = getOwner().getComponent(MeshFilter.class).getMesh();
		if(mesh == null) return;

		if(material != null) material.bind(gl);
		mesh.bind(gl);
		mesh.draw(gl);
		mesh.unbind(gl);
		if(material != null) material.unbind(gl);
	}

}
