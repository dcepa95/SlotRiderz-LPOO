package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.MyGdxGame;

/**
 * Created by Amp on 27/05/2016.
 */
public class MenuMainScreen implements Screen {

    private static final int SINGLEPLAYER_BUTTON_WIDTH = 350; //330
    private static final int SINGLEPLAYER_BUTTON_HEIGHT = 150; //150
    private static final int SINGLEPLAYER_BUTTON_Y = 300; //100
    //private static final int MULTIPLAYER_BUTTON_WIDTH = 300;
    //private static final int MULTIPLAYER_BUTTON_HEIGHT = 300;
    //private static final int MULTIPLAYER_BUTTON_Y = 300;
    private static final int SETTINGS_BUTTON_WIDTH = 350;
    private static final int SETTINGS_BUTTON_HEIGHT = 150;
    private static final int SETTINGS_BUTTON_Y = 100;

    private MyGdxGame game;
    private Sprite singlePlayer;
    private Sprite exit;
    private Sprite backgroundMenuImage;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private int baseRes=1280;

    private Vector3 input;

    public MenuMainScreen(MyGdxGame game) {
        this.game = game;
        camera = new OrthographicCamera(baseRes,baseRes*Gdx.graphics.getHeight()/Gdx.graphics.getWidth());
        batch = new SpriteBatch();
        singlePlayer  = new Sprite(this.game.assets.get("img/singleplayer.png",Texture.class));
        exit = new Sprite(this.game.assets.get("img/quit.png",Texture.class));
        backgroundMenuImage = new Sprite(this.game.assets.get("img/logosemfundo.png",Texture.class));
        input=new Vector3();
    }

    @Override
    public void show() {

        singlePlayer.setSize(358.4f,129.2f);
        singlePlayer.setCenter(0,0);
        exit.setSize(358.4f,129.2f);
        exit.setCenter(0,-150);
        backgroundMenuImage.setCenter(0,200);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        //Print background menu image
        backgroundMenuImage.draw(batch);
        singlePlayer.draw(batch);
        exit.draw(batch);

        if(isTouched(singlePlayer.getX(),singlePlayer.getX()+singlePlayer.getWidth(),singlePlayer.getY(),singlePlayer.getY()+singlePlayer.getHeight())){
            singlePlayer.setTexture(game.assets.get("img/singlePlayerDown.png",Texture.class));
            singlePlayer.draw(batch);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    game.setScreen(new MenuCarScreen(game));
                }
            }, (float) 0.0005);
        }

        if(isTouched(exit.getX(),exit.getX()+exit.getWidth(),exit.getY(),exit.getY()+exit.getHeight())){
            exit.setTexture(game.assets.get("img/quitDown.png",Texture.class));
            exit.draw(batch);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    game.dispose();
                    Gdx.app.exit();
                }
            }, (float) 0.0005);
        }
        batch.end();


        //--------------------------------------------------------------------------------------------

        /*
        //Print multiPlayerButton
        if (Gdx.input.getX() < x + SINGLEPLAYER_BUTTON_WIDTH && Gdx.input.getX() > x && MyGdxGame.HEIGHT - Gdx.input.getY() < SINGLEPLAYER_BUTTON_Y + SINGLEPLAYER_BUTTON_HEIGHT && MyGdxGame.HEIGHT - Gdx.input.getY() > SINGLEPLAYER_BUTTON_Y) {
            game.batch.draw(singlePlayerButtonActive, x, SINGLEPLAYER_BUTTON_Y, SINGLEPLAYER_BUTTON_WIDTH, SINGLEPLAYER_BUTTON_HEIGHT);
        }
        else {
            game.batch.draw(singlePlayerButtonInactive, x, SINGLEPLAYER_BUTTON_Y, SINGLEPLAYER_BUTTON_WIDTH, SINGLEPLAYER_BUTTON_HEIGHT);
        }
        */

        //--------------------------------------------------------------------------------------------

        /*
        //SETTINGS BUTTON
        int xSettings = (MyGdxGame.WIDTH / 2) - (SETTINGS_BUTTON_WIDTH / 2);
        game.batch.draw(settingsButtonInactive, xSettings, SETTINGS_BUTTON_Y, SETTINGS_BUTTON_WIDTH, SETTINGS_BUTTON_HEIGHT);

        if (Gdx.input.getX() < xSettings + SETTINGS_BUTTON_WIDTH && Gdx.input.getX() > xSettings && MyGdxGame.HEIGHT - Gdx.input.getY() < SETTINGS_BUTTON_Y + SETTINGS_BUTTON_HEIGHT && MyGdxGame.HEIGHT - Gdx.input.getY() > SETTINGS_BUTTON_Y) {
            if (Gdx.input.isTouched()) {
                game.batch.draw(settingsButtonActive, xSettings, SETTINGS_BUTTON_Y, SETTINGS_BUTTON_WIDTH, SETTINGS_BUTTON_HEIGHT);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        game.setScreen(new MenuSettingsScreen());
                    }
                }, (float) 0.0005);
            }
        }
        */
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
        singlePlayer.getTexture().dispose();
        //multiPlayerButtonActive.dispose();
        //multiPlayerButtonInactive.dispose();
        //SETTINGSButtonActive.dispose();
        //SETTINGSButtonInactive.dispose();
        backgroundMenuImage.getTexture().dispose();
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
