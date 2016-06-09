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
import com.mygdx.game.logic.Track;

/**
 * Track Selection Screen
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

    private Sprite track1Button;
    private Sprite track2Button;
    private Sprite currTrack;

    private boolean track1Chosen;
    private boolean track2Chosen;

    /**
     * Creates a new track menu screen
     * @param game
     * @param currCar
     */
    public MenuTrackScreen(MyGdxGame game, Sprite currCar) {
        this.game = game;
        this.currCar = currCar;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(baseRes,baseRes*Gdx.graphics.getHeight()/Gdx.graphics.getWidth());
        batch = new SpriteBatch();
        backgroundImage = new Sprite(game.assets.get("img/logosemfundo.png",Texture.class));
        backgroundImage.setOriginCenter();
        backgroundImage.setCenter(0,0);
        nextButton = new Sprite(game.assets.get("img/next.png",Texture.class));
        nextButton.setSize(179.2f,64.6f);
        nextButton.setCenter(500,-255);
        backButton = new Sprite(game.assets.get("img/back.png",Texture.class));
        backButton.setSize(179.2f,64.6f);
        backButton.setCenter(-500,-255);


        track1Button = new Sprite(game.assets.get("img/track1.png",Texture.class));
        track1Button.setScale(0.1f);
        track1Button.setCenter(300,200);
        track2Button = new Sprite (game.assets.get("img/track2.png",Texture.class));
        track2Button.setScale(0.1f);
        track2Button.setCenter(300,-25);

        input=new Vector3();

        //currTrack
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        backgroundImage.draw(batch);
        nextButton.draw(batch);
        backButton.draw(batch);
        track1Button.draw(batch);
        track2Button.draw(batch);

        //touch next button
        if(isTouched(nextButton.getX(),nextButton.getX()+nextButton.getWidth(),nextButton.getY(),nextButton.getY()+nextButton.getHeight())){
            game.setScreen(new GameScreen(game,currCar,new Track(game)));
        }
        //touch back button
        if(isTouched(backButton.getX(),backButton.getX()+backButton.getWidth(),backButton.getY(),backButton.getY()+backButton.getHeight())){
            game.setScreen(new MenuCarScreen(game));
        }

        if(isTouched(track1Button.getX(),track1Button.getX()+track1Button.getWidth(),track1Button.getY(),track1Button.getY()+track1Button.getHeight())){
            track1Chosen = true;
            track2Chosen = false;
        }

        if(isTouched(track2Button.getX(),track2Button.getX()+track2Button.getWidth(),track2Button.getY(),track2Button.getY()+track2Button.getHeight())){
            track1Chosen = false;
            track2Chosen = true;
        }

        if (track1Chosen) {
            //print imagem da track1
        }
        else if (track2Chosen) {
            //print imagem da track2
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

    /**
     * Checks if the screen was touched in a specific square
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @return true is touched, flase if not
     */
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
