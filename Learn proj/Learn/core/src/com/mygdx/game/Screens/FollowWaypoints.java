package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Enteties.AI;
import com.mygdx.game.Enteties.Car;
import com.mygdx.game.Enteties.Game;
import com.mygdx.game.Enteties.Player;
import com.mygdx.game.Enteties.Track;

public class FollowWaypoints implements Screen {
    private ShapeRenderer sr;
    private ShapeRenderer GUI;
    private SpriteBatch b2;
    private SpriteBatch batch;
    private Sprite sprite;
    private Sprite trackSprite;
    private int trackHelper;
    private Player player;
    private Array<AI> ais;

    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private OrthographicCamera GUIcamera;
    private BitmapFont bitmapFont;
    private Track track;

    private Viewport GUIport;
    private Viewport gamePort;

    private int baseRes = 1280;

    private Sprite rightArrow;
    private Sprite leftArrow;
    private Sprite pedal;
    private Sprite trap;
    private Vector3 input;

    private Texture explosion;
    private TextureRegion[] animatedExplosion;
    private Animation animation;
    private float explosionCounterCar1=0f;
    private boolean collisionCar1 = false;
    private float explosionCounterCar2=0f;
    private boolean collisionCar2 = false;
    private float explosionCounterCar3=0f;
    private boolean collisionCar3 = false;
    private float explosionCounterCar4=0f;
    private boolean collisionCar4 = false;

    private Array<Rectangle> traps;
    private Array<Vector2> explosionPosition;

    private Game game;


    public void createExplosion(){
        explosion = new Texture("img/explosion.png");
        TextureRegion[][] tmp = TextureRegion.split(explosion,explosion.getWidth()/8,explosion.getHeight()/9);
        animatedExplosion=transformTo1D(tmp);
        animation = new Animation(1f/60f,animatedExplosion);
    }

    public TextureRegion[] transformTo1D(TextureRegion[][] tmp){
        TextureRegion[] walkFrames = new TextureRegion[72];
        int index=0;
        for(int i=0;i<9;i++){
            for(int j=0;j<8;j++){
                walkFrames[index]=tmp[i][j];
                index++;
            }
        }
        return walkFrames;
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(0, 0, 0, 1);

        bitmapFont = new BitmapFont();
        bitmapFont.getData().scale(3);

        sr = new ShapeRenderer();
        GUI = new ShapeRenderer();
        batch = new SpriteBatch();
        b2=new SpriteBatch();

        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(baseRes, baseRes*Gdx.graphics.getHeight()/Gdx.graphics.getWidth());
        camera.zoom=0.5f;
        GUIcamera = new OrthographicCamera(baseRes,baseRes*Gdx.graphics.getHeight()/Gdx.graphics.getWidth());
        track=new Track();


        sprite = new Sprite(new Texture("img/carGreen.png"));

        sprite.setSize(11,33);
        sprite.setOriginCenter();
        sprite.setCenter(track.getBeginningPos().x,track.getBeginningPos().y);
        player = new Player(sprite,track.getTrack(),track.getCurvePoints(),0,new Vector2(track.getBeginningPos().x,track.getBeginningPos().y),camera,batch,sr);

        game= new Game(player,track,camera,batch,sr);

        sprite = new Sprite(new Texture("img/finishLine2.png"));
        sprite.setOriginCenter();
        sprite.setCenter(track.getBeginningPos().x-37.5f,track.getBeginningPos().y+10);

        trackSprite = new Sprite(new Texture("img/trackPiece2.png"));
        trackSprite.setOriginCenter();

        rightArrow = new Sprite(new Texture("img/rightArrow.png"));
        rightArrow.setPosition(500,-275);

        leftArrow = new Sprite(new Texture("img/leftArrow.png"));
        //leftArrow.setSize(50,50);
        leftArrow.setPosition(350,-275);

        pedal = new Sprite(new Texture("img/pedal.png"));
        pedal.setSize(120,120);
        pedal.setPosition(-525,-275);

        createExplosion();
        trap = new Sprite(new Texture("img/trap.png"));
        trap.setScale(0.5f);
        trap.setPosition(370,452.3f);
        traps = new Array<Rectangle>();
        traps.add(trap.getBoundingRectangle());

        explosionPosition = new Array<Vector2>();
        explosionPosition.add(new Vector2());
        explosionPosition.add(new Vector2());
        explosionPosition.add(new Vector2());
        explosionPosition.add(new Vector2());
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(0.3f + 0.1f*player.getCar().getSpeed()*Gdx.graphics.getDeltaTime()/5<0.3f)
            camera.zoom=0.3f;
        else if(0.3f + 0.1f*player.getCar().getSpeed()*Gdx.graphics.getDeltaTime()/5>0.4f)
            camera.zoom=0.4f;
        else
            camera.zoom=0.3f + 0.1f*player.getCar().getSpeed()*Gdx.graphics.getDeltaTime()/5;
        //drunk MODE
        //camera.rotate(100*Gdx.graphics.getDeltaTime());

        if(!game.getPlayer().getCar().isOffTrack() && !game.getPlayer().getCar().isExploding()){
            updatePlayerBehaviour(delta);
        }
        game.update(delta);
        batch.begin();
        game.draw(batch);
        batch.end();

       /* sr.setColor(Color.WHITE);
        sr.begin(ShapeRenderer.ShapeType.Line);
        trackHelper=0;
        for( Array<Vector2> path : track.getTrack()){

            previous = path.first();

            for(Vector2 waypoint : path){
                if(trackHelper==0){
                    trackSprite.setCenter(waypoint.x-38,waypoint.y);
                    //trackSprite.draw(batch);
                }
                sr.line(previous,waypoint);
                previous=waypoint;
            }
            trackHelper++;
            //sr.line(previous,path.first());
        }
        sr.end();*/

        b2.setProjectionMatrix(GUIcamera.combined);
        b2.begin();
        leftArrow.draw(b2);
        rightArrow.draw(b2);
        pedal.draw(b2);
        bitmapFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        bitmapFont.draw(b2,"pos: " + Integer.toString(game.getPlayer().getCar().getPos()),-250,290);
        bitmapFont.draw(b2,"lap: " + Integer.toString(game.getPlayer().getLap()),50,290);
        bitmapFont.draw(b2,"fps: " + Integer.toString(Gdx.graphics.getFramesPerSecond()),-600,-300);
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
        GUI.dispose();
        b2.dispose();
        sprite.getTexture().dispose();
        sr.dispose();
        debugRenderer.dispose();
    }

    private void updatePlayerBehaviour(float deltaTime){
        if (!game.getPlayer().getCar().isChangingLane()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || isTouched(rightArrow.getX(), rightArrow.getX() + rightArrow.getWidth(), rightArrow.getY(), rightArrow.getY() + rightArrow.getHeight())) {
                if (game.getPlayer().getLane() > 0) {
                    game.getPlayer().setLane(game.getPlayer().getLane() - 1);
                    if ((game.getPlayer().getCar().getWaypoint() + 3) % game.getPlayer().getCar().getPath().size > 1 && (game.getPlayer().getCar().getWaypoint() + 3) % game.getPlayer().getCar().getPath().size <= 4) {
                        game.getPlayer().getCar().setLap(game.getPlayer().getCar().getLap() + 1);
                    }
                    game.getPlayer().getCar().setWaypoint((game.getPlayer().getCar().getWaypoint() + 3) % game.getPlayer().getCar().getPath().size);
                    game.getPlayer().getCar().setWaypointsPassed(game.getPlayer().getCar().getWaypointsPassed() + 2);
                    game.getPlayer().getCar().changeLane();
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || isTouched(leftArrow.getX(), leftArrow.getX() + leftArrow.getWidth(), leftArrow.getY(), leftArrow.getY() + leftArrow.getHeight())) {
                if (game.getPlayer().getLane() < 3) {
                    game.getPlayer().setLane(game.getPlayer().getLane() + 1);
                    if ((game.getPlayer().getCar().getWaypoint() + 3) % game.getPlayer().getCar().getPath().size > 1 && (game.getPlayer().getCar().getWaypoint() + 3) % game.getPlayer().getCar().getPath().size <= 4) {
                        game.getPlayer().getCar().setLap(game.getPlayer().getCar().getLap() + 1);
                    }
                    game.getPlayer().getCar().setWaypoint((game.getPlayer().getCar().getWaypoint() + 3) % game.getPlayer().getCar().getPath().size);
                    game.getPlayer().getCar().setWaypointsPassed(game.getPlayer().getCar().getWaypointsPassed() + 2);
                    game.getPlayer().getCar().changeLane();
                }
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || isTouched(pedal.getX(), pedal.getX() + pedal.getWidth(), pedal.getY(), pedal.getY() + pedal.getHeight())) {
            pedal.setTexture(new Texture("img/pedalDown.png"));
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
            pedal.setTexture(new Texture("img/pedal.png"));
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
    }

    public boolean isTouched(float x1, float x2, float y1, float y2) {
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
}
