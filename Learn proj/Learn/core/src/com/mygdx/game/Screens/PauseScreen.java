package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.MyGdxGame;

/**
 * Created by Amp on 05/06/2016.
 */
public class PauseScreen implements Screen {
    private Sprite resumeGameButton;
    private MyGdxGame app;
    private FollowWaypoints game;
    private Vector3 input;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private int baseRes=1280;

    public PauseScreen(MyGdxGame app, FollowWaypoints game){
        this.app=app;
        this.game=game;
    }

    @Override
    public void show() {
        camera=new OrthographicCamera(baseRes,baseRes*Gdx.graphics.getHeight()/Gdx.graphics.getWidth());
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        input=new Vector3();
    }

    @Override
    public void render(float delta) {
        batch.begin();
        resumeGameButton.draw(batch);
        batch.end();

        if(isTouched(resumeGameButton.getX(),resumeGameButton.getX()+resumeGameButton.getWidth(),resumeGameButton.getY(),resumeGameButton.getY()+resumeGameButton.getHeight())){
            app.setScreen(game);
        }
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

    }

    private boolean isTouched(float x1, float x2, float y1, float y2){
        input.set(Gdx.input.getX(),Gdx.input.getY(),0);
        camera.unproject(input);
        if ( input.x >= x1 && input.x <= x2 && input.y >= y1 && input.y <=y2){
            if(Gdx.input.justTouched())
                return true;
        }
        return false;
    }
}
