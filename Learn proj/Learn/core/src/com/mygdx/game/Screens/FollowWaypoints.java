package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Enteties.AI;
import com.mygdx.game.Enteties.Car;
import com.mygdx.game.Enteties.Player;
import com.mygdx.game.Enteties.Track;

public class FollowWaypoints implements Screen {
    private ShapeRenderer sr;
    private ShapeRenderer GUI;
    private SpriteBatch b2;
    private SpriteBatch batch;
    private Array<Car> cars;
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

        ais = new Array<AI>();
        cars = new Array<Car>();
        cars.add(player.getCar());
        cars.get(0).setPos(1);

        sprite = new Sprite(new Texture("img/carRed.png"));

        sprite.setSize(11,33);
        sprite.setOriginCenter();
        sprite.setCenter(track.getBeginningPos().x-25,track.getBeginningPos().y);
        ais.add(new AI(sprite,track.getTrack(),track.getCurvePoints(),1,new Vector2(track.getBeginningPos().x-25,track.getBeginningPos().y),camera,batch,sr));
        cars.add(ais.get(0).getCar());
        cars.get(1).setPos(2);

        sprite = new Sprite(new Texture("img/carBlue.png"));

        sprite.setSize(11,33);
        sprite.setOriginCenter();
        sprite.setCenter(track.getBeginningPos().x-50,track.getBeginningPos().y);
        ais.add(new AI(sprite,track.getTrack(),track.getCurvePoints(),2,new Vector2(track.getBeginningPos().x-50,track.getBeginningPos().y),camera,batch,sr));
        cars.add(ais.get(1).getCar());
        cars.get(2).setPos(3);

        sprite = new Sprite(new Texture("img/carBlack.png"));

        sprite.setSize(11,33);
        sprite.setOriginCenter();
        sprite.setCenter(track.getBeginningPos().x-75,track.getBeginningPos().y);
        ais.add(new AI(sprite,track.getTrack(),track.getCurvePoints(),3,new Vector2(track.getBeginningPos().x-75,track.getBeginningPos().y),camera,batch,sr));
        cars.add(ais.get(2).getCar());
        cars.get(3).setPos(4);

        sprite = new Sprite(new Texture("img/finishLine2.png"));
        //sprite.setSize(83,13);
        sprite.setOriginCenter();
        //sprite.rotate90(true);
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
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(0.3f + 0.1f*player.getCar().getSpeed()*Gdx.graphics.getDeltaTime()/5<0.3f)
            camera.zoom=0.3f;
        else if(0.3f + 0.1f*player.getCar().getSpeed()*Gdx.graphics.getDeltaTime()/5>0.5f)
            camera.zoom=0.5f;
        else
            camera.zoom=0.3f + 0.1f*player.getCar().getSpeed()*Gdx.graphics.getDeltaTime()/5;
        //drunk MODE
        //camera.rotate(100*Gdx.graphics.getDeltaTime());

        //update player behaviour
        updatePlayerBehaviour(delta);
        player.update(delta);
        for (AI car : ais) {
            car.update(delta);
        }
        updatePos();

        //draw the track
        batch.begin();
        track.draw(batch);
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

        //draw cars
        batch.begin();
        sprite.draw(batch);
        for(Car car : cars ){
            car.draw(batch);
        }
        checkCarCollisions();
        batch.end();


        //draw GUI
        /*GUI.setProjectionMatrix(GUIcamera.combined);
        GUI.setColor(Color.RED);
        GUI.begin(ShapeRenderer.ShapeType.Filled);
        GUI.circle(-500,-250,GUIcamera.viewportHeight/15);
        GUI.box(300,-270,0,GUIcamera.viewportHeight/10,GUIcamera.viewportHeight/10,0);
        GUI.box(300+50+GUIcamera.viewportHeight/10, -270,0,GUIcamera.viewportHeight/10,GUIcamera.viewportHeight/10,0);
        GUI.end();*/


        b2.setProjectionMatrix(GUIcamera.combined);
        b2.begin();
        leftArrow.draw(b2);
        rightArrow.draw(b2);
        pedal.draw(b2);
        bitmapFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        /*for(int i=0;i<cars.size;i++) {
            bitmapFont.draw(b2, "Car " + Integer.toString(i + 1) + " -> waypoints: " + Integer.toString(cars.get(i).getWaypointsPassed()), -600, 0 - i * 25);
            bitmapFont.draw(b2, "Car " + Integer.toString(i + 1) + " -> pos: " + Integer.toString(cars.get(i).getPos()), -600, -100 - i * 25);
            bitmapFont.draw(b2, "Car " + Integer.toString(i + 1) + " -> lap: " + Integer.toString(cars.get(i).getLap()), -600, -200 - i * 25);
        }*/
        bitmapFont.draw(b2,"fps: " + Integer.toString(Gdx.graphics.getFramesPerSecond()),-600,-300);
        //bitmapFont.draw(b2,"lap: " + Integer.toString(player.getLap()),25,75);
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

    private void updatePos() {
        int pos;
        for (int i = 0; i < cars.size; i++) {
            pos=1;
            for (int j = 0; j < cars.size; j++) {
                if(i!=j) {
                    if (cars.get(i).getWaypointsPassed() < cars.get(j).getWaypointsPassed()) {
                        pos++;
                    } else if (cars.get(i).getWaypointsPassed() == cars.get(j).getWaypointsPassed()) {
                        if(cars.get(i).getDistanceToWaypoint() < cars.get(i).getDistanceToWaypoint()){
                            pos++;
                        }
                    }
                }
            }
            cars.get(i).setPos(pos);
        }
    }

    private void updatePlayerBehaviour(float deltaTime){
        if(!player.getCar().isChangingLane()) {
            if(isTouched(rightArrow.getX() , rightArrow.getX()+rightArrow.getWidth() , rightArrow.getY() , rightArrow.getY()+rightArrow.getHeight())) {
                if (Gdx.input.isTouched()) {
                    if (player.getLane() > 0) {
                        player.setLane(player.getLane()-1);
                        if( (player.getCar().getWaypoint()+3)%player.getCar().getPath().size >1 && (player.getCar().getWaypoint()+3)%player.getCar().getPath().size <= 4){
                            player.getCar().setLap(player.getCar().getLap()+1);
                        }
                        player.getCar().setWaypoint((player.getCar().getWaypoint() + 3) % player.getCar().getPath().size);
                        player.getCar().setWaypointsPassed(player.getCar().getWaypointsPassed()+2);
                        player.getCar().changeLane();
                    }
                }
            }else if(isTouched(leftArrow.getX(), leftArrow.getX()+leftArrow.getWidth() , leftArrow.getY() , leftArrow.getY()+leftArrow.getHeight())) {
                if (Gdx.input.isTouched()) {
                    if (player.getLane() < 3) {
                        player.setLane(player.getLane() + 1);
                        if ((player.getCar().getWaypoint() + 3) % player.getCar().getPath().size > 1 && (player.getCar().getWaypoint() + 3) % player.getCar().getPath().size <= 4) {
                            player.getCar().setLap(player.getCar().getLap() + 1);
                        }
                        player.getCar().setWaypoint((player.getCar().getWaypoint() + 3) % player.getCar().getPath().size);
                        player.getCar().setWaypointsPassed(player.getCar().getWaypointsPassed() + 2);
                        player.getCar().changeLane();
                    }
                }
            }
        }

        if(isTouched(pedal.getX(), pedal.getX()+pedal.getWidth(), pedal.getY(), pedal.getY()+pedal.getHeight())) {
                pedal.setTexture(new Texture("img/pedalDown.png"));
                player.getCar().setAccelerating(true);
                if (player.getCar().getSpeed() < player.getCar().getMaxSpeed()) {
                    if(player.getCar().getSpeed()+player.getCar().getAcceleration()*deltaTime>player.getCar().getMaxSpeed())
                        player.getCar().setSpeed(player.getCar().getMaxSpeed());
                    else
                        player.getCar().setSpeed(player.getCar().getSpeed()+player.getCar().getAcceleration()*deltaTime);

                }else{
                    player.getCar().setSpeed((float)player.getCar().getMaxSpeed());
                }
        }else{
            pedal.setTexture(new Texture("img/pedal.png"));
            player.getCar().setAccelerating(false);
            if(player.getCar().getSpeed()>0){
                if(player.getCar().getSpeed()*0.95f<0)
                    player.getCar().setSpeed((float)0);
                else
                    player.getCar().setSpeed(player.getCar().getSpeed()*0.95f);
            }else{
                player.getCar().setSpeed((float)0);
            }
        }
    }

    public boolean isTouched(float x1, float x2, float y1, float y2){
        for (int i = 0; i < 20; i++) {
            if(!Gdx.input.isTouched(i))
                continue;
            input = new Vector3(Gdx.input.getX(i),Gdx.input.getY(i),0);
            GUIcamera.unproject(input);
            if ( input.x >= x1 && input.x <= x2 && input.y >= y1 && input.y <=y2){
                return true;
            }
        }
        return false;
    }

    public void checkCarCollisions(){
        for(int i=0;i<cars.size;i++){
            for(int j=0;j<cars.size;j++){
                if(j==i) continue;
                if(cars.get(i).getHitBox().overlaps(cars.get(j).getHitBox())){
                    if(cars.get(i).getSpeed()>cars.get(j).getSpeed()){
                        switch(j){
                            case 0:
                                collisionCar1=true;
                                break;
                            case 1:
                                collisionCar2=true;
                                break;
                            case 2:
                                collisionCar3=true;
                                break;
                            case 3:
                                collisionCar4=true;
                                break;
                        }
                        //car(j) blow
                    }else if(cars.get(i).getSpeed()<cars.get(j).getSpeed()){
                        switch(i){
                            case 0:
                                collisionCar1=true;
                                break;
                            case 1:
                                collisionCar2=true;
                                break;
                            case 2:
                                collisionCar3=true;
                                break;
                            case 3:
                                collisionCar4=true;
                                break;
                        }
                        //car(i) blow
                    }
                }
            }
        }

        if(collisionCar1){
            cars.get(0).setSpeed(0);
            explosionCounterCar1+=Gdx.graphics.getDeltaTime();
            batch.draw(animation.getKeyFrame(explosionCounterCar1),cars.get(0).getX()-10,cars.get(0).getY()-11,35,45);
            if(explosionCounterCar1 > 1){
                collisionCar1 = false;
                explosionCounterCar1=0f;
            }
        }

        if(collisionCar2){
            cars.get(1).setSpeed(0);
            explosionCounterCar2+=Gdx.graphics.getDeltaTime();
            batch.draw(animation.getKeyFrame(explosionCounterCar2),cars.get(1).getX()-10,cars.get(1).getY()-11,35,45);
            if(explosionCounterCar2 > 1){
                collisionCar2 = false;
                explosionCounterCar2=0f;
            }
        }

        if(collisionCar3){
            cars.get(2).setSpeed(0);
            explosionCounterCar3+=Gdx.graphics.getDeltaTime();
            batch.draw(animation.getKeyFrame(explosionCounterCar3),cars.get(2).getX()-10,cars.get(2).getY()-11,35,45);
            if(explosionCounterCar3 > 1){
                collisionCar3 = false;
                explosionCounterCar3=0f;
            }
        }

        if(collisionCar4){
            cars.get(3).setSpeed(0);
            explosionCounterCar4+=Gdx.graphics.getDeltaTime();
            batch.draw(animation.getKeyFrame(explosionCounterCar4),cars.get(3).getX()-10,cars.get(3).getY()-11,35,45);
            if(explosionCounterCar4 > 1 ){
                collisionCar4 = false;
                explosionCounterCar4=0f;
            }
        }
    }
}
