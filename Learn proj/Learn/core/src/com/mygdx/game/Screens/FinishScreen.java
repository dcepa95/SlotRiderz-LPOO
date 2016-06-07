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
 * Created by Amp on 05/06/2016.
 */
public class FinishScreen implements Screen {
    private Sprite position;
    private Sprite placed;
    private MyGdxGame app;
    private Vector3 input;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private int baseRes=1280;
    private int pos;

    public FinishScreen(MyGdxGame app, int pos){
        this.pos=pos;
        this.app=app;
    }

    @Override
    public void show() {
        camera=new OrthographicCamera(baseRes,baseRes*Gdx.graphics.getHeight()/Gdx.graphics.getWidth());
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        position = new Sprite(app.assets.get("img/first.png",Texture.class));
        switch(pos){
            case 2:
                position.setTexture(app.assets.get("img/second.png",Texture.class));
                break;
            case 3:
                position.setTexture(app.assets.get("img/third.png",Texture.class));
                break;
            case 4:
                position.setTexture(app.assets.get("img/fourth.png",Texture.class));
                break;
        }
        position.setCenter(0,-100);
        placed = new Sprite(app.assets.get("img/placed.png",Texture.class));
        placed.setCenter(0,100);
        input=new Vector3();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        placed.draw(batch);
        position.draw(batch);
        batch.end();

        if(Gdx.input.justTouched()){
            app.setScreen(new MenuMainScreen(app));
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
