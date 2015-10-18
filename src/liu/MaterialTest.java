package liu;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class MaterialTest extends InputAdapter implements ApplicationListener {
	float angleY = 0;
	
	Model model, backModel;
	ModelInstance modelInstance;
	ModelInstance background;
	ModelBatch modelBatch;
	
	TextureAttribute textureAttribute;
	ColorAttribute colorAttribute;
	BlendingAttribute blendingAttribute;

	Material material;
	
	Texture texture;
	
	Camera camera;

	@Override
	public void create() {
		FileHandle fd = Gdx.files.internal("assets/badlogic.jpg");
		
		texture = new Texture(fd, true);
		
		// Create material attributes. Each material can contain x-number of attributes.
		textureAttribute = new TextureAttribute(TextureAttribute.Diffuse, texture);
		colorAttribute = new ColorAttribute(ColorAttribute.Diffuse, Color.ORANGE);
		blendingAttribute = new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		
		ModelBuilder builder = new ModelBuilder();
		model = builder.createBox(1, 1, 1, new Material(), Usage.Position | Usage.Normal | Usage.TextureCoordinates);
		model.manageDisposable(texture);
		modelInstance = new ModelInstance(model);
		modelInstance.transform.rotate(Vector3.X, 45);
		
		material = modelInstance.materials.get(0);
		
		builder.begin();
		MeshPartBuilder mpb = builder.part("back", GL20.GL_TRIANGLES, 
			Usage.Position | Usage.TextureCoordinates, new Material(textureAttribute));
		mpb.rect(-2, -2, -2, 2, -2, -2, 2, 2, -2, -2, 2, -2, 0, 0, 1);
		backModel = builder.end();
		background = new ModelInstance(backModel);
		
		modelBatch = new ModelBatch();
		
		camera = new PerspectiveCamera(45, 4, 4);
		camera.position.set(0, 0, 3);
		camera.direction.set(0, 0, -1);
		camera.update();
		
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}
	
	private float counter = 0.f;
	@Override
	public void render() {
		counter = (counter + Gdx.graphics.getDeltaTime()) % 1.f;
		blendingAttribute.opacity = 0.25f + Math.abs(0.5f - counter);
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelInstance.transform.rotate(Vector3.Y, 30 * Gdx.graphics.getDeltaTime());
		modelBatch.begin(camera);
		modelBatch.render(modelInstance);
		modelBatch.render(background);
		modelBatch.end();
	}
	
	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		
		if(!material.has(TextureAttribute.Diffuse))
			material.set(textureAttribute);
		else if(!material.has(ColorAttribute.Diffuse))
			material.set(colorAttribute);
		else if(!material.has(BlendingAttribute.Type))
			material.set(blendingAttribute);
		else
			material.clear();
		
		return super.touchUp(screenX, screenY, pointer, button);
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
		model.dispose();
		backModel.dispose();
		modelBatch.dispose();

	}

}
