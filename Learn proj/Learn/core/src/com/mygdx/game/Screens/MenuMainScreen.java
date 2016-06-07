package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
    private Texture singlePlayerButtonActive;
    private Texture singlePlayerButtonInactive;
    //Texture multiPlayerButtonActive;
    //Texture multiPlayerButtonInactive;
    private Texture settingsButtonActive;
    private Texture settingsButtonInactive;
    private Texture backgroundMenuImage;

    public MenuMainScreen(MyGdxGame game) {
        this.game = game;
        singlePlayerButtonActive = this.game.assets.get("img/singlePlayerDown.png");
        singlePlayerButtonInactive  = this.game.assets.get("img/singleplayer.png");
        //multiPlayerButtonActive = new Texture()
        //multiPlayerButtonInactive  = new Texture()
        settingsButtonActive  = this.game.assets.get("img/settingsButtonActive.png");
        settingsButtonInactive  = this.game.assets.get("img/settingsButtonInactive.png");
        backgroundMenuImage = this.game.assets.get("img/backgroundMenuImage2.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        //Print background menu image
        game.batch.draw(backgroundMenuImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        //SINGLEPLAYER BUTTON
        int xSinglePlayer = (MyGdxGame.WIDTH / 2) - (SINGLEPLAYER_BUTTON_WIDTH / 2);
        game.batch.draw(singlePlayerButtonInactive, xSinglePlayer, SINGLEPLAYER_BUTTON_Y, SINGLEPLAYER_BUTTON_WIDTH, SINGLEPLAYER_BUTTON_HEIGHT);
        if (Gdx.input.getX() < xSinglePlayer + SINGLEPLAYER_BUTTON_WIDTH && Gdx.input.getX() > xSinglePlayer && MyGdxGame.HEIGHT - Gdx.input.getY() < SINGLEPLAYER_BUTTON_Y + SINGLEPLAYER_BUTTON_HEIGHT && MyGdxGame.HEIGHT - Gdx.input.getY() > SINGLEPLAYER_BUTTON_Y) {
            if (Gdx.input.isTouched()) {
                game.batch.draw(singlePlayerButtonActive, xSinglePlayer, SINGLEPLAYER_BUTTON_Y, SINGLEPLAYER_BUTTON_WIDTH, SINGLEPLAYER_BUTTON_HEIGHT);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            game.setScreen(new MenuCarScreen(game));
                        }
                    }, (float) 0.0005);
            }
        }


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

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

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
        singlePlayerButtonActive.dispose();
        singlePlayerButtonInactive.dispose();
        //multiPlayerButtonActive.dispose();
        //multiPlayerButtonInactive.dispose();
        //SETTINGSButtonActive.dispose();
        //SETTINGSButtonInactive.dispose();
        backgroundMenuImage.dispose();
    }
}
