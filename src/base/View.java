package base;

import javafx.scene.SubScene;

/*

classes that implements the View interface must be blank at first,
then initialize its root when the load() method is called.

load() is to load all the needed entities for the view into the class.

*/

public interface View {
	public boolean isLoaded();
	public void load();
	public void unload();
	
	public SubScene getSubScene();
}
