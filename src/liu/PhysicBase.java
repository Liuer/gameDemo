package liu;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.FloatCounter;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.linearmath.LinearMath;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.PerformanceCounter;

public class PhysicBase extends InputAdapter implements ApplicationListener {
	private String title = "Bullet physic test";
	public String getTitle(){
		return title;
	}
	
	public PerspectiveCamera cam;
	public CameraInputController inputController;
	public Environment environment;
	
	public PerformanceCounter performanceCounter = new PerformanceCounter(this.getClass().getSimpleName());
	public FloatCounter fpsCounter = new FloatCounter(5);
	
	private static final String customDesktopLib = null;
	private static boolean initialized = false;
	public static void init() {
		if (initialized) return;
		// Need to initialize bullet before using it.
		if (Gdx.app.getType() == ApplicationType.Desktop && customDesktopLib != null) {
			System.load(customDesktopLib);
		} 
		else{
			Bullet.init();
		}
		Gdx.app.log("Bullet", "Version = "+LinearMath.btGetVersion());
		initialized = true;
	}
	
	public BulletWorld world;
	public ObjLoader objLoader = new ObjLoader();
	public ModelBuilder modelBuilder = new ModelBuilder();
	public ModelBatch modelBatch;
	public Array<Disposable> disposables = new Array<Disposable>();
	
	public StringBuilder performance = new StringBuilder();
	
	public BulletWorld createBulletWorld(){
		return new BulletWorld();
	}

	@Override
	public void create() {
		init();
		world = createBulletWorld();
		world.performanceCounter = performanceCounter;
		
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		modelBatch = new ModelBatch();
		
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		
		cam = new PerspectiveCamera(67f, 3f * width / height, 3f);
//		cam.position.set(30f, 10f, 30f);
//		cam.lookAt(0,0,0);
//		cam.near = 0.1f;
//		cam.far = 45f;
//		cam.update();
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0, 0, 0);
		cam.update();
		
		Gdx.input.setInputProcessor(new InputMultiplexer(this, inputController = new CameraInputController(cam)));
		
		final Model groundModel = modelBuilder.createRect(20f, 0f, -20f, -20f, 0f, -20f, -20f, 0f, 20f, 20f, 0f, 20f, 0, 1, 0, 
				new Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE), FloatAttribute.createShininess(16f)),
				Usage.Position | Usage.Normal);
		disposables.add(groundModel);
		final Model boxModel = modelBuilder.createBox(1f, 1f, 1f,
				new Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE), FloatAttribute.createShininess(64f)), 
				Usage.Position | Usage.Normal);
		disposables.add(boxModel);
		
		world.addConstructor("ground", new BulletConstructor(groundModel, 0f)); // mass = 0: static body
		world.addConstructor("box", new BulletConstructor(boxModel, 1f)); // mass = 1kg: dynamic body
		world.addConstructor("staticbox", new BulletConstructor(boxModel, 0f)); // mass = 0: static body
	}

	@Override
	public void dispose() {
		world.dispose();
		world = null;
		
		for (Disposable disposable : disposables)
			disposable.dispose();
		disposables.clear();
		
		modelBatch.dispose();
		modelBatch = null;
		
	}
	
	@Override
	public void render() {
		inputController.update();
		update();
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0.13f, 0.13f, 0.13f, 1);
		
		renderWorld();

		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		
		performance.setLength(0);
		performance.append("FPS: ").append(fpsCounter.value).append(", Bullet: ")
			.append((int)(performanceCounter.load.value*100f)).append("%");
		
		
		
	}

	protected void renderWorld(){
		modelBatch.begin(cam);
		world.render(modelBatch, environment);
		modelBatch.end();
	}
	
	public void update() {
		world.update();
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
}
