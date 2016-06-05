package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.MyGdxGame;


/**
 * Created by Amp on 01/06/2016.
 */
public class MenuCarScreen implements Screen {

    private static final int SMALLCAR_WIDTH = 30;
    private static final int SMALLCAR_HEIGHT = 90;
    private static final int DISPLAYCAR_WIDTH = 100;
    private static final int DISPLAYCAR_HEIGHT = 300;
    private int baseRes = 1280;

    private MyGdxGame game;
    private SpriteBatch batch;
    private Sprite backgroundImage;
    private Sprite SlotRiderzLogo;
    private Sprite selectACar;
    private Sprite nextButton;
    private Sprite backButton;
    private Sprite spriteCar1;
    private Sprite spriteCar2;
    private Sprite spriteCar3;
    private Sprite spriteCar4;
    private Sprite currCar;
    private OrthographicCamera camera;
    private Vector3 input;
    private boolean car1pressed=true;
    private boolean car2pressed=false;
    private boolean car3pressed=false;
    private boolean car4pressed=false;

    Vector3 touchPos; // creates a vector3 object for our touch event




    public MenuCarScreen(MyGdxGame game) {
        this.game = game;
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

    @Override
    public void show() {
        camera = new OrthographicCamera(baseRes, baseRes*Gdx.graphics.getHeight()/Gdx.graphics.getWidth());

        backgroundImage = new Sprite(new Texture("img/backgroundImage.png"));
        backgroundImage.setCenter(0,0);
        nextButton = new Sprite(new Texture("img/nextButton.png"));
        nextButton.scale(0.00001f);
        nextButton.setCenter(300,-300);
        backButton = new Sprite(new Texture("img/backButton.png"));
        backButton.scale(0.00001f);
        backButton.setCenter(-300,-300);


        selectACar = new Sprite(new Texture("img/SELECTCAR.png"));
        batch = new SpriteBatch();

        spriteCar1 = new Sprite(new Texture("img/carBlue.png"));
        spriteCar1.setOriginCenter();
        spriteCar1.setSize(SMALLCAR_WIDTH, SMALLCAR_HEIGHT);
        spriteCar1.setCenter(300,300);

        spriteCar2 = new Sprite(new Texture("img/carRed.png"));
        spriteCar2.setOriginCenter();
        spriteCar2.setSize(SMALLCAR_WIDTH, SMALLCAR_HEIGHT);
        spriteCar2.setCenter(300,200);

        spriteCar3 = new Sprite(new Texture("img/carGreen.png"));
        spriteCar3.setOriginCenter();
        spriteCar3.setSize(SMALLCAR_WIDTH, SMALLCAR_HEIGHT);
        spriteCar3.setCenter(300,100);

        spriteCar4 = new Sprite(new Texture("img/carBlack.png"));
        spriteCar4.setOriginCenter();
        spriteCar4.setSize(SMALLCAR_WIDTH, SMALLCAR_HEIGHT);
        spriteCar4.setCenter(300,0);

        currCar = new Sprite(new Texture("img/carBlue.png"));
        currCar.setSize(DISPLAYCAR_WIDTH, DISPLAYCAR_HEIGHT);
        currCar.setOriginCenter();
        currCar.setCenter(-300,100);

        input = new Vector3();
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
            game.setScreen(new MenuTrackScreen(game,currCar));
        }
        //touch back button
        if(isTouched(backButton.getX(),backButton.getX()+backButton.getWidth(),backButton.getY(),backButton.getY()+backButton.getHeight())){
            game.setScreen(new MenuMainScreen(game));
        }

        //touch car 1
        if(isTouched(spriteCar1.getX(),spriteCar1.getX()+spriteCar1.getWidth(),spriteCar1.getY(),spriteCar1.getY()+spriteCar1.getHeight())){
            car1pressed=true;
            car2pressed=false;
            car3pressed=false;
            car4pressed=false;
            currCar.setTexture(spriteCar1.getTexture());
        }
        //touch car 2
        if(isTouched(spriteCar2.getX(),spriteCar2.getX()+spriteCar2.getWidth(),spriteCar2.getY(),spriteCar2.getY()+spriteCar2.getHeight())){
            car1pressed=false;
            car2pressed=true;
            car3pressed=false;
            car4pressed=false;
            currCar.setTexture(spriteCar2.getTexture());
        }
        //touch car 3
        if(isTouched(spriteCar3.getX(),spriteCar3.getX()+spriteCar3.getWidth(),spriteCar3.getY(),spriteCar3.getY()+spriteCar3.getHeight())){
            car1pressed=false;
            car2pressed=false;
            car3pressed=true;
            car4pressed=false;
            currCar.setTexture(spriteCar3.getTexture());
        }
        //touch car 2
        if(isTouched(spriteCar4.getX(),spriteCar4.getX()+spriteCar4.getWidth(),spriteCar4.getY(),spriteCar4.getY()+spriteCar4.getHeight())){
            car1pressed=false;
            car2pressed=false;
            car3pressed=false;
            car4pressed=true;
            currCar.setTexture(spriteCar4.getTexture());
        }

        if(car1pressed)
            spriteCar1.rotate(10*Gdx.graphics.getDeltaTime());
        else
            spriteCar1.setRotation(0);

        if(car2pressed)
            spriteCar2.rotate(10*Gdx.graphics.getDeltaTime());
        else
            spriteCar2.setRotation(0);

        if(car3pressed)
            spriteCar3.rotate(10*Gdx.graphics.getDeltaTime());
        else
            spriteCar3.setRotation(0);

        if(car4pressed)
            spriteCar4.rotate(10*Gdx.graphics.getDeltaTime());
        else
            spriteCar4.setRotation(0);

        spriteCar1.draw(batch);
        spriteCar2.draw(batch);
        spriteCar3.draw(batch);
        spriteCar4.draw(batch);
        currCar.rotate(10*Gdx.graphics.getDeltaTime());
        currCar.draw(batch);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight=baseRes* height/width;
        camera.viewportWidth=baseRes;
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


}
