package camera;

/*

遊戲玩家視角的相機。

Rotation 更新部分因為使用到太多 Snake 自身的函數，

所以索性直接寫在 Snake Class 中。

*/


import base.Entity;

import javafx.scene.PerspectiveCamera;


public class SnakeCamera extends Entity {
	public SnakeCamera(PerspectiveCamera c) {
		super(c);
		
		// initialize camera
		c.setNearClip(1);
		c.setFarClip(20000);
	}
}
