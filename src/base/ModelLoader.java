package base;

import java.io.File;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;

public class ModelLoader {
	private String baseFileURL = "src/resources/models/",
			baseStreamURL = "/resources/models/";
	
	private PhongMaterial material = new PhongMaterial();
	
	private String objName;
	
	public ModelLoader(String objName) {
		this.objName = objName;
		
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
	    File file = new File(baseFileURL + objName + ".obj");
		ObjModelImporter importer = new ObjModelImporter();
		importer.read(file);
		MeshView[] meshes = importer.getImport();
		
		meshes[0].setMaterial(material);
		
		Group obj = new Group();
		obj.getChildren().addAll(meshes);
		return obj;
	}
}
