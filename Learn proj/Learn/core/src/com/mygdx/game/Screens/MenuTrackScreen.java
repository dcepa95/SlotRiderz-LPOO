package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.MyGdxGame;

/**
 * Created by Amp on 03/06/2016.
 */
public class MenuTrackScreen implements Screen {
    private Sprite backgroundImage;
    private SpriteBatch batch;
    private MyGdxGame game;
    private Sprite currCar;
    private OrthographicCamera camera;
    private int baseRes=1280;
    private Vector3 input;


    private Sprite nextButton;
    private Sprite backButton;


    public MenuTrackScreen(MyGdxGame game, Sprite currCar) {
        this.game = game;
        this.currCar = currCar;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(baseRes,baseRes*Gdx.graphics.getHeight()/Gdx.graphics.getWidth());
        batch = new SpriteBatch();
        backgroundImage = new Sprite(new Texture("img/backgroundImage.png"));
        backgroundImage.setOriginCenter();
        backgroundImage.setCenter(0,0);
        nextButton = new Sprite(new Texture("img/nextButton.png"));
        nextButton.scale(0.00001f);
        nextButton.setCenter(300,-300);
        backButton = new Sprite(new Texture("img/backButton.png"));
        backButton.scale(0.00001f);
        backButton.setCenter(-300,-300);

        input=new Vector3();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        backgroundImage.draw(batch);
        nextButton.draw(batch);
        backButton.draw(batch);
        //touch next button
        if(isTouched(nextButton.getX(),nextButton.getX()+nextButton.getWidth(),nextButton.getY(),nextButton.getY()+nextButton.getHeight())){
            game.setScreen(new FollowWaypoints());
        }
        //touch back button
        if(isTouched(backButton.getX(),backButton.getX()+backButton.getWidth(),backButton.getY(),backButton.getY()+backButton.getHeight())){
            game.setScreen(new MenuCarScreen(game));
        }
        batch.end();
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
        batch.dispose();
    }

    public boolean isTouched(float x1, float x2, float y1, float y2){
        input.set(Gdx.input.getX(),Gdx.input.getY(),0);
        camera.unproject(input);
        if ( input.x >= x1 && input.x <= x2 && input.y >= y1 && input.y <=y2){
            if(Gdx.input.justTouched())
                return true;
        }
        return false;
    }
}
