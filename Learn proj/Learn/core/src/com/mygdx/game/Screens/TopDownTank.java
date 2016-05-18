package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Enteties.Tank;

public class TopDownTank implements Screen {

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private float timestep = 1 / 60f;

    private Tank tank;

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tank.update();
        world.step(timestep, 8, 3);

        camera.position.set(tank.getChassis().getPosition().x, tank.getChassis().getPosition().y, 0);
        camera.update();

		/*batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(Assets.luigiFront, -Assets.luigiFront.getWidth() / 2, -Assets.luigiFront.getHeight() / 2);
		Box2DSprite.draw(batch, world);
		batch.end();*/

        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / 25;
        camera.viewportHeight = height / 25;
    }

    @Override
    public void show() {
        world = new World(new Vector2(), true);
        batch = new SpriteBatch();
        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera();

        Gdx.input.setInputProcessor(tank = new Tank(world, 0, 0, 3, 5));
        tank.getChassis().setLinearDamping(3);
        tank.getChassis().setAngularDamping(3);
        tank.getCannon().setLinearDamping(3);
        tank.getCannon().setAngularDamping(3);

        //tank.getChassis().setUserData(new Box2DSprite(Assets.tank));
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        world.dispose();
        batch.dispose();
        debugRenderer.dispose();
    }

}
