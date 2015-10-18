package liu;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import java.util.HashMap;
import java.util.Map;


public class bulletPhysic3DTest {
	public static void main(String argv[]){
		bulletPhysic3DTest.initApplications();
		
		String title = "MaterialTest";//"Test1";
		ApplicationListener app;
		app = applications.get(title);
		
		new LwjglApplication(app, title, 640, 480);
	}
	
	public static Map<String, ApplicationListener> applications = new HashMap<String, ApplicationListener>();
	public static void initApplications(){
		Test1 t1 = new Test1();
		applications.put(t1.getClass().getSimpleName(), t1);
		
		PhysicBase pb = new PhysicBase();
		applications.put(pb.getClass().getSimpleName(), pb);
		
		FirstDemo fd = new FirstDemo();
		applications.put(fd.getClass().getSimpleName(), fd);
		
		MaterialTest mt = new MaterialTest();
		applications.put(mt.getClass().getSimpleName(), mt);
	}
}
