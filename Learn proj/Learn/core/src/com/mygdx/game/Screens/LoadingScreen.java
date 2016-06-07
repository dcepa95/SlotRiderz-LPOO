package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.MyGdxGame;

/**
 * Created by digbe on 07/06/2016.
 */
public class LoadingScreen implements Screen{

    private MyGdxGame app;

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private float progress;
    private Sprite logo;

    private int baseRes=1280;

    public LoadingScreen(MyGdxGame app) {
        this.app = app;
        this.shapeRenderer = new ShapeRenderer();
        this.camera=new OrthographicCamera(baseRes,baseRes*Gdx.graphics.getHeight()/Gdx.graphics.getWidth());
        batch=new SpriteBatch();
        logo = new Sprite(new Texture("img/logosemfundo.png"));
        logo.setCenter(0,100);
    }

    private void queueAssets() {
        //buttons
        app.assets.load("img/back.png", Texture.class);
        app.assets.load("img/leftArrow.png", Texture.class);
        app.assets.load("img/leftArrowDown.png", Texture.class);
        app.assets.load("img/rightArrow.png", Texture.class);
        app.assets.load("img/rightArrowDown.png", Texture.class);
        app.assets.load("img/next.png", Texture.class);
        app.assets.load("img/pause.png", Texture.class);
        app.assets.load("img/pauseDown.png", Texture.class);
        app.assets.load("img/pedal.png", Texture.class);
        app.assets.load("img/pedalDown.png", Texture.class);
        app.assets.load("img/quit.png", Texture.class);
        app.assets.load("img/quitDown.png", Texture.class);
        app.assets.load("img/resume.png", Texture.class);
        app.assets.load("img/resumeDown.png", Texture.class);
        app.assets.load("img/singleplayer.png",Texture.class);
        app.assets.load("img/singlePlayerDown.png",Texture.class);
        app.assets.load("img/track1.png",Texture.class);
        app.assets.load("img/track2.png",Texture.class);
        app.assets.load("img/settingsButtonActive.png",Texture.class);
        app.assets.load("img/settingsButtonInactive.png",Texture.class);
        //game elements
        app.assets.load("img/carBlack.png",Texture.class);
        app.assets.load("img/carBlue.png",Texture.class);
        app.assets.load("img/carGreen.png",Texture.class);
        app.assets.load("img/carRed.png",Texture.class);
        app.assets.load("img/carYellow.png",Texture.class);
        app.assets.load("img/explosion.png",Texture.class);
        app.assets.load("img/finishLine.png",Texture.class);
        app.assets.load("img/mine.png",Texture.class);
        app.assets.load("img/trackPiece1.png",Texture.class);
        app.assets.load("img/trackPiece3.png",Texture.class);
        app.assets.load("img/trackPiece4.png",Texture.class);
        app.assets.load("img/cross.png",Texture.class);
        //other images
        //TODO ver uso
        app.assets.load("img/backgroundImage.png",Texture.class);
        app.assets.load("img/backgroundMenuImage.png",Texture.class);
        app.assets.load("img/backgroundMenuImage2.png",Texture.class);
        app.assets.load("img/bar.png",Texture.class);
        //positions
        app.assets.load("img/first.png",Texture.class);
        app.assets.load("img/second.png",Texture.class);
        app.assets.load("img/third.png",Texture.class);
        app.assets.load("img/fourth.png",Texture.class);
        //numbers
        app.assets.load("img/one.png",Texture.class);
        app.assets.load("img/two.png",Texture.class);
        app.assets.load("img/three.png",Texture.class);
        app.assets.load("img/four.png",Texture.class);
        app.assets.load("img/five.png",Texture.class);
        app.assets.load("img/six.png",Texture.class);
        app.assets.load("img/seven.png",Texture.class);
        app.assets.load("img/eight.png",Texture.class);
        app.assets.load("img/nine.png",Texture.class);
        app.assets.load("img/zero.png",Texture.class);
        //words
        app.assets.load("img/carSelect.png",Texture.class);
        app.assets.load("img/go.png",Texture.class);
        app.assets.load("img/lap.png",Texture.class);
        app.assets.load("img/placed.png",Texture.class);
    }

    @Override
    public void show() {
        System.out.println("LOADING");
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
        this.progress = 0f;
        queueAssets();
    }

    private void update(float delta) {
        progress = MathUtils.lerp(progress, app.assets.getProgress(), .1f);
        if (app.assets.update() && progress >= app.assets.getProgress() - .001f) {
            app.setScreen(new MenuMainScreen(app));
        }
    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        batch.begin();
        logo.draw(batch);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.rect(-(camera.viewportWidth - 64)/2, -8, camera.viewportWidth - 64, 16);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(-(camera.viewportWidth - 64)/2, -8, progress * (camera.viewportWidth - 64), 16);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth=baseRes;
        camera.viewportHeight=baseRes*height/width;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
