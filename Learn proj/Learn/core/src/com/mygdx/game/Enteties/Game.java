package com.mygdx.game.Enteties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private Array<Rectangle> traps;
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
    private Sprite trap;

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

        traps=new Array<Rectangle>();

        trap = new Sprite(new Texture("img/trap.png"));
        trap.setScale(0.5f);
        trap.setPosition(370,452.3f);
        traps.add(new Rectangle(trap.getX()+6,trap.getY()-6,trap.getWidth()/2,trap.getHeight()/2));
    }

    public void update(float deltaTime){
        player.update(deltaTime);
        for(AI ai: ais){
            ai.update(deltaTime);
        }
        updateRacePositions();
        checkCarCollisions();
    }

    public void draw(SpriteBatch batch){
        track.draw(batch);
        trap.draw(batch);
        for(Car car:cars){
            car.draw(batch);
        }
        drawCollisions(batch);

    }

    private void updateRacePositions() {
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

    public void checkCarCollisions(){
        for(int i=0;i<cars.size;i++){
            for(int j=0;j<cars.size;j++){
                if(j==i) continue;
                if(cars.get(i).getHitBox().overlaps(cars.get(j).getHitBox())){
                    if(cars.get(i).getSpeed()>cars.get(j).getSpeed()){
                        switch(j){
                            case 0:
                                collisionCar1=true;
                                explosionPosition.get(j).set(cars.get(j).getX()-11,cars.get(j).getY()-11);
                                cars.get(j).setExploding(true);
                                break;
                            case 1:
                                collisionCar2=true;
                                explosionPosition.get(j).set(cars.get(j).getX()-11,cars.get(j).getY()-11);
                                cars.get(j).setExploding(true);
                                break;
                            case 2:
                                collisionCar3=true;
                                explosionPosition.get(j).set(cars.get(j).getX()-11,cars.get(j).getY()-11);
                                cars.get(j).setExploding(true);
                                break;
                            case 3:
                                collisionCar4=true;
                                explosionPosition.get(j).set(cars.get(j).getX()-11,cars.get(j).getY()-11);
                                cars.get(j).setExploding(true);
                                break;
                        }
                        //car(j) blow
                    }else if(cars.get(i).getSpeed()<cars.get(j).getSpeed()){
                        switch(i){
                            case 0:
                                collisionCar1=true;
                                explosionPosition.get(i).set(cars.get(i).getX()-11,cars.get(i).getY()-11);
                                cars.get(i).setExploding(true);
                                break;
                            case 1:
                                collisionCar2=true;
                                explosionPosition.get(i).set(cars.get(i).getX()-11,cars.get(i).getY()-11);
                                cars.get(i).setExploding(true);
                                break;
                            case 2:
                                collisionCar3=true;
                                explosionPosition.get(i).set(cars.get(i).getX()-11,cars.get(i).getY()-11);
                                cars.get(i).setExploding(true);
                                break;
                            case 3:
                                collisionCar4=true;
                                explosionPosition.get(i).set(cars.get(i).getX()-11,cars.get(i).getY()-11);
                                cars.get(i).setExploding(true);
                                break;
                        }
                        //car(i) blow
                    }else{
                        /*switch(j){
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
                        }*/
                    }
                }
            }

            for(int j=0;j<traps.size;j++){
                if(cars.get(i).getHitBox().overlaps(traps.get(j))){
                    switch(i){
                        case 0:
                            collisionCar1=true;
                            cars.get(i).setExploding(true);
                            if(cars.get(i).isChangingLane())
                                cars.get(i).setWaypoint(cars.get(i).getWaypoint()-2);
                            else
                                cars.get(i).setWaypoint(cars.get(i).getWaypoint()+1);
                            cars.get(i).setCenter(cars.get(i).getPath().get(cars.get(i).getWaypoint()).x,cars.get(i).getPath().get(cars.get(i).getWaypoint()).y );
                            explosionPosition.get(i).set(traps.get(j).getPosition(explosionPosition.get(i)).x - 25, traps.get(j).getPosition(explosionPosition.get(i)).y - 13);
                            break;
                        case 1:
                            collisionCar2=true;
                            cars.get(i).setExploding(true);
                            cars.get(i).setPosition(cars.get(i).getPath().get(cars.get(i).getWaypoint()+1).x-6.8f,cars.get(i).getPath().get(cars.get(i).getWaypoint()+1).y -16.5f);
                            cars.get(i).setWaypoint(cars.get(i).getWaypoint()+1);
                            explosionPosition.get(i).set(traps.get(j).getPosition(explosionPosition.get(i)).x - 25, traps.get(j).getPosition(explosionPosition.get(i)).y - 13);
                            break;
                        case 2:
                            collisionCar3=true;
                            cars.get(i).setExploding(true);
                            cars.get(i).setPosition(cars.get(i).getPath().get(cars.get(i).getWaypoint()+1).x-6.8f,cars.get(i).getPath().get(cars.get(i).getWaypoint()+1).y -16.5f);
                            cars.get(i).setWaypoint(cars.get(i).getWaypoint()+1);
                            explosionPosition.get(i).set(traps.get(j).getPosition(explosionPosition.get(i)).x - 25, traps.get(j).getPosition(explosionPosition.get(i)).y - 13);
                            break;
                        case 3:
                            collisionCar4=true;
                            cars.get(i).setExploding(true);
                            cars.get(i).setPosition(cars.get(i).getPath().get(cars.get(i).getWaypoint()+1).x-6.8f,cars.get(i).getPath().get(cars.get(i).getWaypoint()+1).y -16.5f);
                            cars.get(i).setWaypoint(cars.get(i).getWaypoint()+1);
                            explosionPosition.get(i).set(traps.get(j).getPosition(explosionPosition.get(i)).x - 25, traps.get(j).getPosition(explosionPosition.get(i)).y - 12);
                            break;
                    }
                }
            }
        }
    }

    public void drawCollisions(SpriteBatch batch){
        if(collisionCar1){
            cars.get(0).setSpeed(0);
            explosionCounterCar1+= Gdx.graphics.getDeltaTime();
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
            batch.draw(animation.getKeyFrame(explosionCounterCar2),cars.get(1).getX()-10,cars.get(1).getY()-11,35,45);
            if(explosionCounterCar2 > 1){
                cars.get(1).setExploding(false);
                collisionCar2 = false;
                explosionCounterCar2=0f;
            }
        }

        if(collisionCar3){
            cars.get(2).setSpeed(0);
            cars.get(2).setExploding(false);
            explosionCounterCar3+=Gdx.graphics.getDeltaTime();
            batch.draw(animation.getKeyFrame(explosionCounterCar3),cars.get(2).getX()-10,cars.get(2).getY()-11,35,45);
            if(explosionCounterCar3 > 1){
                collisionCar3 = false;
                explosionCounterCar3=0f;
            }
        }

        if(collisionCar4){
            cars.get(3).setSpeed(0);
            cars.get(3).setExploding(false);
            explosionCounterCar4+=Gdx.graphics.getDeltaTime();
            batch.draw(animation.getKeyFrame(explosionCounterCar4),cars.get(3).getX()-10,cars.get(3).getY()-11,35,45);
            if(explosionCounterCar4 > 1 ){
                collisionCar4 = false;
                explosionCounterCar4=0f;
            }
        }
    }

    public Player getPlayer(){
        return player;
    }
}
