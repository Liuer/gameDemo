package liu;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;


public class bulletPhysic3DTest {
	public static void main(String argv[]){
		Test1 t1 = new Test1(){
			public String getTitle(){
				String title = super.getTitle();
				return title + "!!!";
			}
		};
		new LwjglApplication(t1, t1.getTitle(), 640, 480);
	}
}
