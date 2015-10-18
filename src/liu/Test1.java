package liu;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.InputAdapter;

public class Test1 extends InputAdapter implements ApplicationListener {
	public PerspectiveCamera cam;
	public CameraInputController inputController;
	public ModelBatch modelBatch;
	public Model model;
	public ModelInstance instance;
	public Environment environment;
	
	public final static String title = "title1";
	
	@Override
	public void create() {
		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.f));
		environment.set(new ColorAttribute(ColorAttribute.Fog, 0.13f, 0.13f, 0.13f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(30f, 10f, 30f);
		cam.lookAt(0,0,0);
		cam.near = 0.1f;
		cam.far = 45f;
		cam.update();
		
		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(10f, 10f, 10f, new Material(ColorAttribute.createDiffuse(Color.BLUE)), Usage.Position | Usage.Normal);
		instance = new ModelInstance(model);

		Gdx.input.setInputProcessor(new InputMultiplexer(this, inputController = new CameraInputController(cam)));
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {

		animate();

        inputController.update();
		
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        Gdx.gl.glClearColor(0.13f, 0.13f, 0.13f, 1);

		modelBatch.begin(cam);
		modelBatch.render(instance, environment);
		modelBatch.end();

	}
	
	float delta = 0f, dir = 1;
	private void animate(){
		delta = Gdx.graphics.getDeltaTime();

        instance.transform.val[14] += delta*8*dir;

        if(Math.abs(instance.transform.val[14]) > 9) {
            dir *= -1;
        }
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
	public void dispose() {
		modelBatch.dispose();
		model.dispose();

	}
	
	public final String getTitle(){
		return title;
	}

}
