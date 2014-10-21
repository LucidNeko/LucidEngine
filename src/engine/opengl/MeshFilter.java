package engine.opengl;

import engine.core.Component;

/**
 * A MeshFilter is the component you use to get at a Mesh on an Entity.
 * @author Hamish Rae-Hodgson.
 *
 */
public class MeshFilter extends Component {

	private Mesh mesh;

	public Mesh getMesh() {
		return mesh;
	}
	
	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	

}
