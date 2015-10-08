package liu;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import java.util.ArrayList;


public class bulletPhysic3DTest {
	public static void main(String argv[]){
		ArrayList<ApplicationListener> arr = new ArrayList<ApplicationListener>();
		
		Test1 t1 = new Test1(){
			public String getTitle(){
				String title = super.getTitle();
				return title + "!!!";
			}
		};
		
		PhysicBase pb = new PhysicBase();
		
		arr.add(t1);
		arr.add(pb);
		
		int index = arr.indexOf(pb);
		
		FirstDemo fd = new FirstDemo();
		
		new LwjglApplication(fd, "first demo!", 640, 480);
	}
}
