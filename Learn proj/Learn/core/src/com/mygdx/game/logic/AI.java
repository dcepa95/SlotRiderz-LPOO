package com.mygdx.game.logic;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class AI {

    private Car car;
    private int lane;
    private Vector2 beginningPos;
    private Array<Array<Vector2> > tracks;
    private Array<Integer> curveRightPoints;
    private Array<Integer> curveLeftPoints;
    private float rejoinTime;
    private float AImaxSpeed=500;

    /**
     * Creates a AI
     *
     * @param sprite car sprite
     * @param tracks lanes of the track
     * @param curves points that belong to a curve
     * @param initialLane lane that the car starts
     * @param bPos beginning position
     * @param camera
     * @param batch
     * @param sr
     */
    public AI(Sprite sprite, Array<Array<Vector2> > tracks, Array<Array<Integer> > curves, int initialLane, Vector2 bPos, OrthographicCamera camera, SpriteBatch batch, ShapeRenderer sr){
        this.tracks=tracks;
        curveRightPoints=curves.first();
        curveLeftPoints=curves.get(1);
        beginningPos=bPos;
        lane=initialLane;
        car= new Car(sprite, tracks.get(lane), camera, batch, sr);
    }

    /**
     * Updates de AI's car speed
     * @param deltaTime time elapsed from last frame
     */
    private void updateCarSpeed(float deltaTime) {
        //TODO change this to AI
            car.setAccelerating(true);
            if (car.getSpeed() < car.getMaxSpeed()) {
                if(car.getSpeed() + car.getAcceleration()*deltaTime > car.getMaxSpeed())
                    car.setSpeed(car.getMaxSpeed());
                else
                    car.setSpeed(car.getSpeed() + car.getAcceleration() * deltaTime);

            }else{
                car.setSpeed(car.getMaxSpeed());
            }
    }

    /**
     * Randomly updates AI's lane
     */
    private void updateLane(){
        //TODO change this to AI
        Random rand = new Random();
        int random;
        if(!car.isChangingLane()) {
            random=rand.nextInt(100)+1;
            if (random==1) {
                if (lane < 3) {
                    lane++;
                    if( (car.getWaypoint()+3)%car.getPath().size >0 && (car.getWaypoint()+3)%car.getPath().size <= 3){
                        car.setLap(car.getLap()+1);
                    }
                    car.setWaypoint((car.getWaypoint() + 3) % car.getPath().size);
                    car.setWaypointsPassed(car.getWaypointsPassed()+2);
                    car.changeLane();
                }
            } else if (random==2) {
                if (lane > 0) {
                    lane--;
                    if( (car.getWaypoint()+3)%car.getPath().size >0 && (car.getWaypoint()+3)%car.getPath().size <= 3){
                        car.setLap(car.getLap()+1);
                    }
                    car.setWaypoint((car.getWaypoint() + 3) % car.getPath().size);
                    car.setWaypointsPassed(car.getWaypointsPassed()+2);
                    car.changeLane();
                }
            }
        }
    }

    /**
     * Verifies and notifies the AI's car if it's on a curve
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
     * updates the AI's car position
     *
     * @param deltaTime time elapsed from last frame
     */
    public void update(float deltaTime){
        if(!car.isOffTrack() && !car.isExploding()) {
            updateCarSpeed(deltaTime);
            updateLane();
            car.setPath(tracks.get(lane));
            isInCurve();
            car.update(deltaTime, lane);
            rejoinTime=0;
        }else{
            car.setSpeed(car.getSpeed()*0.9f);
            car.update(deltaTime, lane);
            rejoinRace();
        }
    }

    /**
     * Returns AI's car
     * @return AI's car
     */
    public Car getCar() {
        return car;
    }

    /**
     * Returns current lap
     * @return current lap
     */
    public int getLap(){
        return car.getLap();
    }

    /**
     * sets the AI back in race after a short delay
     */
    private void rejoinRace(){
        rejoinTime+=Gdx.graphics.getDeltaTime();
        if(rejoinTime>0.7f){
            car.putInTrack();
        }
    }

    /**
     * Sets Ai's lane as a new one
     * @param lane new lane.
     */
    public void setLane(int lane){
        this.lane=lane;
    }

    /**
     * Returns the current AI's lane
     * @return current lane
     */
    public int getLane(){
        return lane;
    }
}