package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.logic.AI;
import com.mygdx.game.logic.Game;
import com.mygdx.game.logic.Player;
import com.mygdx.game.logic.Track;

public class FollowWaypoints implements Screen {
    private ShapeRenderer sr;
    private SpriteBatch b2;
    private SpriteBatch batch;
    private Sprite sprite;
    private Player player;

    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private OrthographicCamera GUIcamera;
    private BitmapFont bitmapFont;
    private Track track;


    private int baseRes = 1280;

    private Sprite rightArrow;
    private Texture rightArrowTex;
    private Texture rightArrowDownTex;

    private Sprite leftArrow;
    private Texture leftArrowTex;
    private Texture leftArrowDownTex;

    private Sprite pedal;
    private Texture pedalTex;
    private Texture pedalDownTex;

    private Sprite pause;
    private Texture pauseTex;
    private Texture pauseDownTex;

    private Sprite first;
    private Sprite second;
    private Sprite third;
    private Sprite fourth;
    private Sprite lapSprite;

    private Sprite digit1;
    private Sprite digit2;
    private Sprite bar;
    private Sprite digit3;
    private Sprite digit4;

    private Texture num0;
    private Texture num1;
    private Texture num2;
    private Texture num3;
    private Texture num4;
    private Texture num5;
    private Texture num6;
    private Texture num7;
    private Texture num8;
    private Texture num9;
    private Texture go;


    private Sprite resumeGameButton;
    private Sprite countDown;


    private Vector3 input;

    private Game game;
    private MyGdxGame app;

    private boolean gameStarted=false;
    private boolean gamePaused=false;
    private float startCount=0;

    public FollowWaypoints(MyGdxGame app, Sprite playerCar, Track t){
        this.app=app;
        sprite=playerCar;
        track=t;
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(0, 0, 0, 1);

        bitmapFont = new BitmapFont();
        bitmapFont.getData().scale(3);
        GUIcamera = new OrthographicCamera(baseRes,baseRes*Gdx.graphics.getHeight()/Gdx.graphics.getWidth());
        camera = new OrthographicCamera(baseRes, baseRes*Gdx.graphics.getHeight()/Gdx.graphics.getWidth());
        camera.zoom=0.3f;
        sr = new ShapeRenderer();
        batch = new SpriteBatch();
        b2=new SpriteBatch();

        track=new Track();

        sprite.setSize(11,33);
        sprite.setOriginCenter();
        sprite.setCenter(track.getBeginningPos().x,track.getBeginningPos().y);
        player = new Player(sprite,track.getTrack(),track.getCurvePoints(),0,new Vector2(track.getBeginningPos().x,track.getBeginningPos().y),camera,batch,sr);

        game= new Game(player,track,camera,batch,sr);

        sprite = new Sprite(new Texture("img/finishLine2.png"));
        sprite.setOriginCenter();
        sprite.setCenter(track.getBeginningPos().x-37.5f,track.getBeginningPos().y+9);

        rightArrowTex=new Texture("img/rightArrow.png");
        rightArrowDownTex=new Texture("img/rightArrowDown.png");
        leftArrowTex=new Texture("img/leftArrow.png");
        leftArrowDownTex=new Texture("img/leftArrowDown.png");
        pedalTex=new Texture("img/pedal.png");
        pedalDownTex=new Texture("img/pedalDown.png");
        pauseTex=new Texture("img/pause.png");
        pauseDownTex = new Texture("img/pauseDown.png");
        rightArrow = new Sprite(rightArrowTex);
        rightArrow.setSize(100,100);
        rightArrow.setPosition(500,-275);

        leftArrow = new Sprite(leftArrowTex);
        leftArrow.setSize(100,100);
        leftArrow.setPosition(350,-275);

        pedal = new Sprite(pedalTex);
        //pedal.setSize(60,120);
        pedal.setPosition(-525,-275);

        pause=new Sprite(pauseTex);
        pause.setSize(100,100);
        pause.setPosition(500,250);

        camera.position.set(game.getPlayer().getCar().getX() + game.getPlayer().getCar().getWidth() / 2, game.getPlayer().getCar().getY() + game.getPlayer().getCar().getHeight() / 2, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        first = new Sprite(new Texture("img/first.png"));
        first.setPosition(-100,190);
        second = new Sprite(new Texture("img/second.png"));
        second.setPosition(-100,190);
        third = new Sprite(new Texture("img/third.png"));
        third.setPosition(-100,190);
        fourth = new Sprite(new Texture("img/fourth.png"));
        fourth.setPosition(-100,190);

        //Show Lap

        num0=new Texture("img/zero.png");
        num1=new Texture("img/one.png");
        num2=new Texture("img/two.png");
        num3=new Texture("img/three.png");
        num4=new Texture("img/four.png");
        num5=new Texture("img/five.png");
        num6=new Texture("img/six.png");
        num7=new Texture("img/seven.png");
        num8=new Texture("img/eight.png");
        num9=new Texture("img/nine.png");
        go = new Texture("img/go.png");


        lapSprite=new Sprite(new Texture("img/lap.png"));
        lapSprite.setScale(0.75f);
        lapSprite.setPosition(-600,230);

        digit1=new Sprite(num0);
        digit1.setScale(0.5f);
        digit1.setPosition(-500,190);
        digit2=new Sprite(num1);
        digit2.setScale(0.5f);
        digit2.setPosition(-450,190);
        bar=new Sprite(new Texture("img/bar.png"));
        bar.setScale(0.5f);
        bar.setPosition(-400,200);
        digit3=new Sprite(num1);
        digit3.setScale(0.5f);
        digit3.setPosition(-350,190);
        digit4=new Sprite(num0);
        digit4.setScale(0.5f);
        digit4.setPosition(-300,190);


        resumeGameButton = new Sprite (new Texture("img/resume.png"));
        resumeGameButton.setScale(0.5f);
        resumeGameButton.setPosition(-100,-50);
        countDown=new Sprite(num3);
        countDown.setPosition(-50,-100);
    }


    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(game.end()){
            //TODO FINISHSCREEN
            app.setScreen(new MenuMainScreen(app));
        }

        if(0.3f + 0.1f*player.getCar().getSpeed()*Gdx.graphics.getDeltaTime()/5<0.3f)
            camera.zoom=0.3f;
        else if(0.3f + 0.1f*player.getCar().getSpeed()*Gdx.graphics.getDeltaTime()/5>0.4f)
            camera.zoom=0.4f;
        else
            camera.zoom=0.3f + 0.1f*player.getCar().getSpeed()*Gdx.graphics.getDeltaTime()/5;
        //drunk MODE
        //camera.rotate(100*Gdx.graphics.getDeltaTime());
        if(!gameStarted){
            startCount+=Gdx.graphics.getDeltaTime();
            if(startCount>3) {
                gameStarted = true;
            }else{
                if(startCount<1){
                    countDown.setTexture(num3);
                    countDown.setSize(86,188);
                    countDown.setPosition(-43,-94);
                }else if(startCount<2){
                    countDown.setTexture(num2);
                }else if(startCount<3){
                    countDown.setTexture(num1);
                }
            }
        }else{
            if(startCount<4){
                startCount+=Gdx.graphics.getDeltaTime();
                countDown.setTexture(go);
                countDown.setSize(241,188);
                countDown.setPosition(-120.5f,-94);
            }
        }

        if(gamePaused){
            batch.begin();
            pauseBehaviour(batch);
            batch.end();
        }


        if(!gamePaused && gameStarted) {
            if (!game.getPlayer().getCar().isOffTrack() && !game.getPlayer().getCar().isExploding()) {
                updatePlayerBehaviour(delta);
            }
            game.update(delta);
        }
        batch.begin();
        game.draw(batch);
        sprite.draw(batch);
        batch.end();

        sr.setColor(Color.WHITE);
        sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeRenderer.ShapeType.Line);
        game.drawDebug(sr);
        sr.end();

        b2.setProjectionMatrix(GUIcamera.combined);
        b2.begin();
        leftArrow.draw(b2);
        rightArrow.draw(b2);
        pedal.draw(b2);
        pause.draw(b2);
        //draw position
        switch (game.getPlayer().getCar().getPos()){
            case 1:
                first.draw(b2);
                break;
            case 2:
                second.draw(b2);
                break;
            case 3:
                third.draw(b2);
                break;
            case 4:
                fourth.draw(b2);
                break;
        }
        //draw laps
        updateLapSprites();
        lapSprite.draw(b2);
        digit1.draw(b2);
        digit2.draw(b2);
        bar.draw(b2);
        digit3.draw(b2);
        digit4.draw(b2);
        //debug
        bitmapFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        bitmapFont.draw(b2,"fps: " + Integer.toString(Gdx.graphics.getFramesPerSecond()),-600,-300);
        //counter draw
        if(startCount<4)
            countDown.draw(b2);
        //draw pause menu
        if(gamePaused){
            resumeGameButton.draw(b2);
        }
        b2.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight=baseRes* height/width;
        camera.viewportWidth=baseRes;
        GUIcamera.viewportHeight=baseRes* height/width;
        GUIcamera.viewportWidth=baseRes;

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
        b2.dispose();
        sprite.getTexture().dispose();
        pedal.getTexture().dispose();
        rightArrow.getTexture().dispose();
        leftArrow.getTexture().dispose();
        pause.getTexture().dispose();
        rightArrowTex.dispose();
        rightArrowDownTex.dispose();
        leftArrowTex.dispose();
        leftArrowDownTex.dispose();
        pedalTex.dispose();
        pedalDownTex.dispose();
        sr.dispose();
        debugRenderer.dispose();
    }

    private void updatePlayerBehaviour(float deltaTime){
        if (!game.getPlayer().getCar().isChangingLane()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || isTouched(rightArrow.getX(), rightArrow.getX() + rightArrow.getWidth(), rightArrow.getY(), rightArrow.getY() + rightArrow.getHeight())) {
                rightArrow.setTexture(rightArrowDownTex);
                rightArrow.setSize(100,100);
                rightArrow.setPosition(500,-275);
                leftArrow.setTexture(leftArrowTex);
                leftArrow.setSize(100,100);
                leftArrow.setPosition(350,-275);
                if (game.getPlayer().getLane() > 0) {
                    game.getPlayer().setLane(game.getPlayer().getLane() - 1);
                    if ((game.getPlayer().getCar().getWaypoint() + 3) % game.getPlayer().getCar().getPath().size > 0 && (game.getPlayer().getCar().getWaypoint() + 3) % game.getPlayer().getCar().getPath().size <= 3) {
                        game.getPlayer().getCar().setLap(game.getPlayer().getCar().getLap() + 1);
                    }
                    game.getPlayer().getCar().setWaypoint((game.getPlayer().getCar().getWaypoint() + 3) % game.getPlayer().getCar().getPath().size);
                    game.getPlayer().getCar().setWaypointsPassed(game.getPlayer().getCar().getWaypointsPassed() + 2);
                    game.getPlayer().getCar().changeLane();
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || isTouched(leftArrow.getX(), leftArrow.getX() + leftArrow.getWidth(), leftArrow.getY(), leftArrow.getY() + leftArrow.getHeight())) {
                rightArrow.setTexture(rightArrowTex);
                rightArrow.setSize(100,100);
                rightArrow.setPosition(500,-275);
                leftArrow.setTexture(leftArrowDownTex);
                leftArrow.setSize(100,100);
                leftArrow.setPosition(350,-275);
                if (game.getPlayer().getLane() < 3) {
                    game.getPlayer().setLane(game.getPlayer().getLane() + 1);
                    if ((game.getPlayer().getCar().getWaypoint() + 3) % game.getPlayer().getCar().getPath().size > 0 && (game.getPlayer().getCar().getWaypoint() + 3) % game.getPlayer().getCar().getPath().size <= 3) {
                        game.getPlayer().getCar().setLap(game.getPlayer().getCar().getLap() + 1);
                    }
                    game.getPlayer().getCar().setWaypoint((game.getPlayer().getCar().getWaypoint() + 3) % game.getPlayer().getCar().getPath().size);
                    game.getPlayer().getCar().setWaypointsPassed(game.getPlayer().getCar().getWaypointsPassed() + 2);
                    game.getPlayer().getCar().changeLane();
                }
            }else{
                leftArrow.setTexture(leftArrowTex);
                leftArrow.setSize(100,100);
                leftArrow.setPosition(350,-275);
                rightArrow.setTexture(rightArrowTex);
                rightArrow.setSize(100,100);
                rightArrow.setPosition(500,-275);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || isTouched(pedal.getX(), pedal.getX() + pedal.getWidth(), pedal.getY(), pedal.getY() + pedal.getHeight())) {
            pedal.setTexture(pedalDownTex);
            pedal.setSize(136,182);
            pedal.setPosition(-521,-275);
            game.getPlayer().getCar().setAccelerating(true);
            if (game.getPlayer().getCar().getSpeed() < game.getPlayer().getCar().getMaxSpeed()) {
                if (game.getPlayer().getCar().getSpeed() + game.getPlayer().getCar().getAcceleration() * deltaTime > game.getPlayer().getCar().getMaxSpeed())
                    game.getPlayer().getCar().setSpeed(game.getPlayer().getCar().getMaxSpeed());
                else
                    game.getPlayer().getCar().setSpeed(game.getPlayer().getCar().getSpeed() + game.getPlayer().getCar().getAcceleration() * deltaTime);

            } else {
                game.getPlayer().getCar().setSpeed((float) game.getPlayer().getCar().getMaxSpeed());
            }
        } else {
            pedal.setTexture(pedalTex);
            pedal.setSize(140,187);
            pedal.setPosition(-525,-275);
            game.getPlayer().getCar().setAccelerating(false);
            if (game.getPlayer().getCar().getSpeed() > 0) {
                if (game.getPlayer().getCar().getSpeed() * 0.95f < 0)
                    game.getPlayer().getCar().setSpeed((float) 0);
                else
                    game.getPlayer().getCar().setSpeed(game.getPlayer().getCar().getSpeed() * 0.95f);
            } else {
                game.getPlayer().getCar().setSpeed((float) 0);
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.P) || isJustTouched(pause.getX(), pause.getX() + pause.getWidth(), pause.getY(), pause.getY() + pause.getHeight())){
            pause.setTexture(pauseDownTex);
            gamePaused=true;
        }else{
            pause.setTexture(pauseTex);
        }
    }

    private void pauseBehaviour(SpriteBatch batch){
        if(Gdx.input.isKeyJustPressed(Input.Keys.P) || isJustTouched(resumeGameButton.getX(),resumeGameButton.getX()+resumeGameButton.getWidth(),resumeGameButton.getY(),resumeGameButton.getY()+resumeGameButton.getHeight())){

            gamePaused=false;
            gameStarted=false;
            startCount=0;
        }else{

        }
    }

    private boolean isTouched(float x1, float x2, float y1, float y2) {
        for (int i = 0; i < 20; i++) {
            if (!Gdx.input.isTouched(i))
                continue;
            input = new Vector3(Gdx.input.getX(i), Gdx.input.getY(i), 0);
            GUIcamera.unproject(input);
            if (input.x >= x1 && input.x <= x2 && input.y >= y1 && input.y <= y2) {
                return true;
            }
        }
        return false;
    }

    private boolean isJustTouched(float x1, float x2, float y1, float y2) {
        for (int i = 0; i < 20; i++) {
            if (!Gdx.input.isTouched(i))
                continue;
            input = new Vector3(Gdx.input.getX(i), Gdx.input.getY(i), 0);
            GUIcamera.unproject(input);
            if(Gdx.input.justTouched()) {
                if (input.x >= x1 && input.x <= x2 && input.y >= y1 && input.y <= y2) {
                    return true;
                }
            }
        }
        return false;
    }

    private void updateLapSprites(){
        switch (game.getPlayer().getLap()/10){
            case 0:
                digit1.setTexture(num0);
                break;
            case 1:
                digit1.setTexture(num1);
                break;
            case 2:
                digit1.setTexture(num2);
                break;
            case 3:
                digit1.setTexture(num3);
                break;
            case 4:
                digit1.setTexture(num4);
                break;
            case 5:
                digit1.setTexture(num5);
                break;
            case 6:
                digit1.setTexture(num6);
                break;
            case 7:
                digit1.setTexture(num7);
                break;
            case 8:
                digit1.setTexture(num8);
                break;
            case 9:
                digit1.setTexture(num9);
                break;
        }

        switch (game.getPlayer().getLap()%10){
            case 0:
                digit2.setTexture(num0);
                break;
            case 1:
                digit2.setTexture(num1);
                break;
            case 2:
                digit2.setTexture(num2);
                break;
            case 3:
                digit2.setTexture(num3);
                break;
            case 4:
                digit2.setTexture(num4);
                break;
            case 5:
                digit2.setTexture(num5);
                break;
            case 6:
                digit2.setTexture(num6);
                break;
            case 7:
                digit2.setTexture(num7);
                break;
            case 8:
                digit2.setTexture(num8);
                break;
            case 9:
                digit2.setTexture(num9);
                break;
        }

        switch (track.getLaps()/10){
            case 0:
                digit3.setTexture(num0);
                break;
            case 1:
                digit3.setTexture(num1);
                break;
            case 2:
                digit3.setTexture(num2);
                break;
            case 3:
                digit3.setTexture(num3);
                break;
            case 4:
                digit3.setTexture(num4);
                break;
            case 5:
                digit3.setTexture(num5);
                break;
            case 6:
                digit3.setTexture(num6);
                break;
            case 7:
                digit3.setTexture(num7);
                break;
            case 8:
                digit3.setTexture(num8);
                break;
            case 9:
                digit3.setTexture(num9);
                break;
        }

        switch (track.getLaps()%10){
            case 0:
                digit4.setTexture(num0);
                break;
            case 1:
                digit4.setTexture(num1);
                break;
            case 2:
                digit4.setTexture(num2);
                break;
            case 3:
                digit4.setTexture(num3);
                break;
            case 4:
                digit4.setTexture(num4);
                break;
            case 5:
                digit4.setTexture(num5);
                break;
            case 6:
                digit4.setTexture(num6);
                break;
            case 7:
                digit4.setTexture(num7);
                break;
            case 8:
                digit4.setTexture(num8);
                break;
            case 9:
                digit4.setTexture(num9);
                break;
        }
    }
}
