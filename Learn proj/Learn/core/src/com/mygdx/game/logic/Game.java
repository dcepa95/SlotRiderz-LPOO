package com.mygdx.game.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by digbe on 04/06/2016.
 */
public class Game {

    Player player;
    private Array<AI> ais;
    private Array<Car> cars;
    private Track track;
    private Array<Polygon> traps;
    int gameMode; //1 - normal race || 2 - drunk mode || 3 - explosion/surviving mode
    //variables for controlling car collisions and collisions animations
    private float explosionCounterCar1=0f;
    private boolean collisionCar1 = false;
    private float explosionCounterCar2=0f;
    private boolean collisionCar2 = false;
    private float explosionCounterCar3=0f;
    private boolean collisionCar3 = false;
    private float explosionCounterCar4=0f;
    private boolean collisionCar4 = false;
    private Array<Vector2> explosionPosition;

    private Texture explosion;
    private TextureRegion[] animatedExplosion;
    private Animation animation;
    private Sprite aiSprite;

    private boolean paused=false;
    private float resumeCounter=0;

    public void createExplosion(){
        explosion = new Texture("img/explosion.png");
        TextureRegion[][] tmp = TextureRegion.split(explosion,explosion.getWidth()/8,explosion.getHeight()/9);
        animatedExplosion=transformTo1D(tmp);
        animation = new Animation(1f/60f,animatedExplosion);

        explosionPosition = new Array<Vector2>();
        explosionPosition.add(new Vector2());
        explosionPosition.add(new Vector2());
        explosionPosition.add(new Vector2());
        explosionPosition.add(new Vector2());
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

    public Game(Player p, Track t, OrthographicCamera camera, SpriteBatch batch, ShapeRenderer sr){
        player=p;
        track=t;
        traps=t.getTraps();
        createExplosion();

        player.getCar().setSize(11,33);
        player.getCar().setOriginCenter();
        player.getCar().setCenter(track.getBeginningPos().x,track.getBeginningPos().y);
        player.getCar().setRotation(0); // because bug
        cars=new Array<Car>();
        cars.add(player.getCar());
        ais=new Array<AI>();

        aiSprite = new Sprite(new Texture("img/carRed.png"));
        aiSprite.setSize(11,33);
        aiSprite.setOriginCenter();
        aiSprite.setCenter(track.getBeginningPos().x-25,track.getBeginningPos().y);
        ais.add(new AI(aiSprite,track.getTrack(),track.getCurvePoints(),1,new Vector2(track.getBeginningPos().x-25,track.getBeginningPos().y),camera,batch,sr));
        cars.add(ais.get(0).getCar());
        cars.get(1).setPos(2);

        aiSprite.setTexture(new Texture("img/carBlue.png"));
        aiSprite.setCenter(track.getBeginningPos().x-50,track.getBeginningPos().y);
        ais.add(new AI(aiSprite,track.getTrack(),track.getCurvePoints(),2,new Vector2(track.getBeginningPos().x-50,track.getBeginningPos().y),camera,batch,sr));
        cars.add(ais.get(1).getCar());
        cars.get(2).setPos(3);

        aiSprite.setTexture(new Texture("img/carBlack.png"));
        aiSprite.setCenter(track.getBeginningPos().x-75,track.getBeginningPos().y);
        ais.add(new AI(aiSprite,track.getTrack(),track.getCurvePoints(),3,new Vector2(track.getBeginningPos().x-75,track.getBeginningPos().y),camera,batch,sr));
        cars.add(ais.get(2).getCar());
        cars.get(3).setPos(4);

    }

    public void update(float deltaTime){
        if(!paused) {
            if (player.getLap() < track.getLaps()) {
                player.update(deltaTime);
                for (AI ai : ais) {
                    ai.update(deltaTime);
                }
                updateRacePositions();
                checkCarCollisions();
            }
        }
    }

    public void draw(SpriteBatch batch){
        track.draw(batch);
        for(Car car:cars){
            car.draw(batch);
        }
        drawCollisions(batch);

    }

    public void drawDebug(ShapeRenderer sr){
        sr.set(ShapeRenderer.ShapeType.Line);
        for(Car car : cars) {
            for(Polygon p : car.getHitBox()) {
                sr.polygon(p.getTransformedVertices());
            }
        }
        //sr.polygon(intersection.getTransformedVertices());
        for(Polygon trap:traps){
            sr.polygon(trap.getTransformedVertices());
        }
    }

    private void updateRacePositions() {
        int pos;
        for (int i = 0; i < cars.size; i++) {
            pos=1;
            for (int j = 0; j < cars.size; j++) {
                if(i != j) {
                    if (cars.get(i).getLap() < cars.get(j).getLap()) {
                        pos++;
                    } else if (cars.get(i).getLap() == cars.get(j).getLap()) {
                        if(cars.get(i).getWaypoint() < cars.get(j).getWaypoint() && cars.get(i).getWaypoint()!=0 && cars.get(j).getWaypoint()!=0){
                            pos++;
                        }else if(cars.get(i).getWaypoint() == cars.get(j).getWaypoint()){
                            if(cars.get(i).getDistanceToWaypoint() > cars.get(j).getDistanceToWaypoint()){
                                pos++;
                            }
                        }
                    }
                }
            }
            cars.get(i).setPos(pos);
        }
    }

    public void checkCarCollisions() {
        for (int i = 0; i < cars.size; i++) {
            for (int j = 0; j < cars.size; j++) {
                if (j == i) continue;
                if (backCollision(cars.get(i).getFront(), cars.get(j).getBack()) || backCollision(cars.get(i).getFront(), cars.get(j).getLeft()) || backCollision(cars.get(i).getFront(), cars.get(j).getRight())) {
                    if (!cars.get(i).isExploding() && !cars.get(j).isExploding()) {
                        if(cars.get(i).getSpeed()>cars.get(j).getSpeed()) {
                            switch (j) {
                                case 0:
                                    collisionCar1 = true;
                                    explosionPosition.get(j).set(cars.get(j).getX() - 11, cars.get(j).getY() - 11);
                                    cars.get(j).setExploding(true);
                                    break;
                                case 1:
                                    collisionCar2 = true;
                                    explosionPosition.get(j).set(cars.get(j).getX() - 11, cars.get(j).getY() - 11);
                                    cars.get(j).setExploding(true);
                                    break;
                                case 2:
                                    collisionCar3 = true;
                                    explosionPosition.get(j).set(cars.get(j).getX() - 11, cars.get(j).getY() - 11);
                                    cars.get(j).setExploding(true);
                                    break;
                                case 3:
                                    collisionCar4 = true;
                                    explosionPosition.get(j).set(cars.get(j).getX() - 11, cars.get(j).getY() - 11);
                                    cars.get(j).setExploding(true);
                                    break;
                            }
                        }else if(cars.get(i).getSpeed()==cars.get(j).getSpeed()){
                            if(cars.get(i).getRotation()==cars.get(j).getRotation())
                                cars.get(i).setSpeed(cars.get(i).getSpeed()*0.5f);
                            else{
                                switch (j) {
                                    case 0:
                                        collisionCar1 = true;
                                        explosionPosition.get(j).set(cars.get(j).getX() - 11, cars.get(j).getY() - 11);
                                        cars.get(j).setExploding(true);
                                        break;
                                    case 1:
                                        collisionCar2 = true;
                                        explosionPosition.get(j).set(cars.get(j).getX() - 11, cars.get(j).getY() - 11);
                                        cars.get(j).setExploding(true);
                                        break;
                                    case 2:
                                        collisionCar3 = true;
                                        explosionPosition.get(j).set(cars.get(j).getX() - 11, cars.get(j).getY() - 11);
                                        cars.get(j).setExploding(true);
                                        break;
                                    case 3:
                                        collisionCar4 = true;
                                        explosionPosition.get(j).set(cars.get(j).getX() - 11, cars.get(j).getY() - 11);
                                        cars.get(j).setExploding(true);
                                        break;
                                }
                            }
                        }
                    }
                }else if (leftCollision(cars.get(i).getFront(), cars.get(j).getFront())) {
                    if (cars.get(i).isChangingLane()) {
                        if (i == 0) {
                            if (player.getLane() > 0) {
                                cars.get(i).setPath(track.getTrack().get(player.getLane() - 1));
                                player.setLane(player.getLane() - 1);
                            }
                        } else {
                            if (ais.get(i - 1).getLane() > 0) {
                                cars.get(i).setPath(track.getTrack().get(ais.get(i - 1).getLane() - 1));
                                ais.get(i - 1).setLane(ais.get(i - 1).getLane() - 1);
                            }
                        }
                    }
                }else if (rightCollision(cars.get(i).getFront(), cars.get(j).getFront())) {
                    if (cars.get(i).isChangingLane()) {
                        if (i == 0) {
                            if (player.getLane() < 3) {
                                cars.get(i).setPath(track.getTrack().get(player.getLane() + 1));
                                player.setLane(player.getLane() + 1);
                            }
                        } else {
                            if (ais.get(i - 1).getLane() < 3) {
                                cars.get(i).setPath(track.getTrack().get(ais.get(i - 1).getLane() + 1));
                                ais.get(i - 1).setLane(ais.get(i - 1).getLane() + 1);
                            }
                        }
                    }
                }
            }

            for (int j = 0; j < traps.size; j++) {
                System.out.printf("%d\n",cars.get(i).getWaypoint());
                if (backCollision(cars.get(i).getFront(),traps.get(j))) {
                    switch (i) {
                        case 0:
                            collisionCar1 = true;
                            cars.get(i).setExploding(true);
                            if (cars.get(i).isChangingLane()) {
                                cars.get(i).setWaypoint(cars.get(i).getWaypoint() - 2);
                                cars.get(i).setWaypointsPassed(cars.get(i).getWaypointsPassed() - 2);
                            } else {
                                cars.get(i).setWaypoint(cars.get(i).getWaypoint() + 1);
                                cars.get(i).setWaypointsPassed(cars.get(i).getWaypointsPassed() + 1);
                            }
                            explosionPosition.get(i).set(cars.get(i).getX(), cars.get(i).getY());
                            cars.get(i).setCenter(cars.get(i).getPath().get(cars.get(i).getWaypoint()).x, cars.get(i).getPath().get(cars.get(i).getWaypoint()).y);
                            break;
                        case 1:
                            collisionCar2 = true;
                            cars.get(i).setExploding(true);
                            if (cars.get(i).isChangingLane()) {
                                cars.get(i).setWaypoint(cars.get(i).getWaypoint() - 2);
                                cars.get(i).setWaypointsPassed(cars.get(i).getWaypointsPassed() - 2);
                            } else {
                                cars.get(i).setWaypoint(cars.get(i).getWaypoint() + 1);
                                cars.get(i).setWaypointsPassed(cars.get(i).getWaypointsPassed() + 1);
                            }
                            explosionPosition.get(i).set(cars.get(i).getX(), cars.get(i).getY());
                            cars.get(i).setCenter(cars.get(i).getPath().get(cars.get(i).getWaypoint()).x, cars.get(i).getPath().get(cars.get(i).getWaypoint()).y);
                            break;
                        case 2:
                            collisionCar3 = true;
                            cars.get(i).setExploding(true);
                            if (cars.get(i).isChangingLane()) {
                                cars.get(i).setWaypoint(cars.get(i).getWaypoint() - 2);
                                cars.get(i).setWaypointsPassed(cars.get(i).getWaypointsPassed() - 2);
                            } else {
                                cars.get(i).setWaypoint(cars.get(i).getWaypoint() + 1);
                                cars.get(i).setWaypointsPassed(cars.get(i).getWaypointsPassed() + 1);
                            }
                            explosionPosition.get(i).set(cars.get(i).getX(), cars.get(i).getY());
                            cars.get(i).setCenter(cars.get(i).getPath().get(cars.get(i).getWaypoint()).x, cars.get(i).getPath().get(cars.get(i).getWaypoint()).y);
                            break;
                        case 3:
                            collisionCar4 = true;
                            cars.get(i).setExploding(true);
                            if (cars.get(i).isChangingLane()) {
                                cars.get(i).setWaypoint(cars.get(i).getWaypoint() - 2);
                                cars.get(i).setWaypointsPassed(cars.get(i).getWaypointsPassed() - 2);
                            } else {
                                cars.get(i).setWaypoint(cars.get(i).getWaypoint() + 1);
                                cars.get(i).setWaypointsPassed(cars.get(i).getWaypointsPassed() + 1);
                            }
                            explosionPosition.get(i).set(cars.get(i).getX(), cars.get(i).getY());
                            cars.get(i).setCenter(cars.get(i).getPath().get(cars.get(i).getWaypoint()).x, cars.get(i).getPath().get(cars.get(i).getWaypoint()).y);
                            break;
                    }
                }
            }
        }
    }

    private boolean backCollision(Polygon car1front, Polygon car2back) {
        return Intersector.overlapConvexPolygons(car1front, car2back);
    }

    private boolean rightCollision(Polygon car1front, Polygon car2right) {
        return Intersector.overlapConvexPolygons(car1front, car2right);
    }

    private boolean leftCollision(Polygon car1front, Polygon car2left) {
        return Intersector.overlapConvexPolygons(car1front, car2left);
    }

    public void drawCollisions(SpriteBatch batch) {
        if (collisionCar1) {
            cars.get(0).setSpeed(0);
            explosionCounterCar1 += Gdx.graphics.getDeltaTime();
            batch.draw(animation.getKeyFrame(explosionCounterCar1),explosionPosition.get(0).x,explosionPosition.get(0).y,35,45);
            if(explosionCounterCar1 > 1){
                cars.get(0).setExploding(false);
                collisionCar1 = false;
                explosionCounterCar1=0f;
            }
        }

        if(collisionCar2){
            cars.get(1).setSpeed(0);
            explosionCounterCar2+=Gdx.graphics.getDeltaTime();
            batch.draw(animation.getKeyFrame(explosionCounterCar2),explosionPosition.get(1).x,explosionPosition.get(1).y,35,45);
            if(explosionCounterCar2 > 1){
                cars.get(1).setExploding(false);
                collisionCar2 = false;
                explosionCounterCar2=0f;
            }
        }

        if(collisionCar3){
            cars.get(2).setSpeed(0);
            explosionCounterCar3+=Gdx.graphics.getDeltaTime();
            batch.draw(animation.getKeyFrame(explosionCounterCar3),explosionPosition.get(2).x,explosionPosition.get(2).y,35,45);
            if(explosionCounterCar3 > 1){
                cars.get(2).setExploding(false);
                collisionCar3 = false;
                explosionCounterCar3=0f;
            }
        }

        if(collisionCar4){
            cars.get(3).setSpeed(0);
            explosionCounterCar4+=Gdx.graphics.getDeltaTime();
            batch.draw(animation.getKeyFrame(explosionCounterCar4),explosionPosition.get(3).x,explosionPosition.get(3).y,35,45);
            if(explosionCounterCar4 > 1 ){
                cars.get(3).setExploding(false);
                collisionCar4 = false;
                explosionCounterCar4=0f;
            }
        }
    }

    public Player getPlayer(){
        return player;
    }

    public void pause(){
        paused=true;
        resumeCounter=0;
    }

    public void resume(SpriteBatch batch){
        start(batch);
    }

    public void start(SpriteBatch batch){
        resumeCounter+=Gdx.graphics.getDeltaTime();
        if(resumeCounter<3){
            //countdown
        }else{
            //go!
            paused=false;
        }
    }

    public boolean paused(){
        return paused;
    }

    public boolean end(){
        return player.getLap()>=track.getLaps();
    }
}
