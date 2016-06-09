package com.mygdx.game.logic;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Class Player, manages a player
 */
public class Player {

    private Car car;
    private int lane;
    private Vector2 beginningPos;
    private Array<Array<Vector2> > tracks;
    private Array<Integer> curveRightPoints;
    private Array<Integer> curveLeftPoints;
    private float rejoinTime;
    private ShapeRenderer sr;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    /**
     * Creates a new Player
     * @param sprite Player's car sprite
     * @param tracks Track tracks/lanes
     * @param curves Points that belong to a curve
     * @param initialLane Initial lane of the car
     * @param bPos Beginning position of the car
     * @param camera
     * @param batch
     * @param sr
     */
    public Player(Sprite sprite, Array<Array<Vector2> > tracks, Array<Array<Integer> > curves, int initialLane, Vector2 bPos, OrthographicCamera camera, SpriteBatch batch, ShapeRenderer sr){
        this.tracks=tracks;
        curveRightPoints=curves.first();
        curveLeftPoints=curves.get(1);
        beginningPos=bPos;
        lane=initialLane;
        this.camera=camera;
        this.batch=batch;
        this.sr=sr;
        car= new Car(sprite, tracks.get(lane), camera, batch, sr);
    }

    /**
     * returns if car is in Curve
     */
    private void isInCurve(){
        if(curveRightPoints.contains(car.getWaypoint(),true)){
            car.setInCurveRight();
        }else if(curveLeftPoints.contains(car.getWaypoint(),true)){
            car.setInCurveLeft();
        }else
            car.setOutCurve();
    }

    /**
     * Updates Players car movement
     * @param deltaTime time elapsed from last frame
     */
    public void update(float deltaTime){
        if(!car.isOffTrack() && !car.isExploding()) {
            car.setPath(tracks.get(lane));
            isInCurve();
            car.update(deltaTime, lane);
            camera.position.set(car.getX() + car.getWidth() / 2, car.getY() + car.getHeight() / 2, 0);
            camera.update();
            batch.setProjectionMatrix(camera.combined);
            sr.setProjectionMatrix(camera.combined);
            sr.updateMatrices();
            rejoinTime=0;
        }else{
            car.setSpeed(car.getSpeed()*0.9f);
            car.update(deltaTime, lane);
            rejoinRace();
        }
    }

    /**
     * Returns Player's car
     * @return car
     */
    public Car getCar() {
        return car;
    }

    /**
     * Returns current Player's lap
     * @return lap
     */
    public int getLap(){
        return car.getLap();
    }

    /**
     * Puts the player in race after a short delay
     */
    private void rejoinRace(){
        rejoinTime+=Gdx.graphics.getDeltaTime();
        if(rejoinTime>0.7f){
            car.putInTrack();
        }
    }

    /**
     * Returns current Player's lane
     * @return lane
     */
    public int getLane(){
        return lane;
    }

    /**
     * Sets player's lane as a new one
     * @param lane new lane
     */
    public void setLane(int lane){
        this.lane=lane;
    }

}
