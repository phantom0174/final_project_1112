package base;

import java.io.File;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;

public class ModelLoader {
	private String baseFileURL = "src/resources/models/",
			baseStreamURL = "/resources/models/";
	
	public PhongMaterial material = new PhongMaterial();
	
	private String objName;
	private String type;
	
	public ModelLoader(String objName, String type) {
		this.objName = objName;
		this.type = type;
		
		baseFileURL += objName + "/";
		baseStreamURL += objName + "/";
	}
	
	public void setDiff(String fileName) {
		Image img = new Image(
			getClass().getResourceAsStream(baseStreamURL + fileName)
		);
		material.setDiffuseMap(img);
	}
	
	public void setSpec(String fileName) {
		Image img = new Image(
			getClass().getResourceAsStream(baseStreamURL + fileName)
		);
		material.setSpecularMap(img);
	}
	
	public Group getMesh() {
		if (type.equals("obj")) {
			File file = new File(baseFileURL + objName + ".obj");
			ObjModelImporter importer = new ObjModelImporter();
			importer.read(file);
			MeshView[] meshes = importer.getImport();
			
			meshes[0].setMaterial(material);
			
			Group obj = new Group();
			obj.getChildren().addAll(meshes);
			return obj;			
		} else if (type.equals("stl")) {
			File file = new File(baseFileURL + objName + ".stl");
			StlMeshImporter importer = new StlMeshImporter();
			importer.read(file);
			MeshView mesh = new MeshView(importer.getImport());
			
			mesh.setMaterial(material);
			
			Group obj = new Group();
			obj.getChildren().add(mesh);
			return obj;
		}
		
		return null;
	}
}
