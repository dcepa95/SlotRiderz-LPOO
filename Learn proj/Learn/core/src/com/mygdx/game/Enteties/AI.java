package com.mygdx.game.Enteties;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    public AI(Sprite sprite, Array<Array<Vector2> > tracks, Array<Array<Integer> > curves, int initialLane, Vector2 bPos, OrthographicCamera camera, SpriteBatch batch, ShapeRenderer sr){
        this.tracks=tracks;
        curveRightPoints=curves.first();
        curveLeftPoints=curves.get(1);
        beginningPos=bPos;
        lane=initialLane;
        car= new Car(sprite, tracks.get(lane), camera, batch, sr);
    }

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

    private void updateLane(){
        //TODO change this to AI
        Random rand = new Random();
        int random;
        if(!car.isChangingLane()) {
            random=rand.nextInt(100)+1;
            if (random==1) {
                if (lane < 3) {
                    lane++;
                    if( (car.getWaypoint()+3)%car.getPath().size >1 && (car.getWaypoint()+3)%car.getPath().size <= 4){
                        car.setLap(car.getLap()+1);
                    }
                    car.setWaypoint((car.getWaypoint() + 3) % car.getPath().size);
                    car.setWaypointsPassed(car.getWaypointsPassed()+2);
                    car.changeLane();
                }
            } else if (random==2) {
                if (lane > 0) {
                    lane--;
                    if( (car.getWaypoint()+3)%car.getPath().size >1 && (car.getWaypoint()+3)%car.getPath().size <= 4){
                        car.setLap(car.getLap()+1);
                    }
                    car.setWaypoint((car.getWaypoint() + 3) % car.getPath().size);
                    car.setWaypointsPassed(car.getWaypointsPassed()+2);
                    car.changeLane();
                }
            }
        }
    }

    private void isInCurve(){
        if(curveRightPoints.contains(car.getWaypoint(),true)){
            car.setInCurveRight();
        }else if(curveLeftPoints.contains(car.getWaypoint(),true)){
            car.setInCurveLeft();
        }else
            car.setOutCurve();
    }

    public void update(float deltaTime){
        if(!car.isOffTrack()) {
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

    public Car getCar() {
        return car;
    }

    public int getLap(){
        return car.getLap();
    }

    private void rejoinRace(){
        rejoinTime+=Gdx.graphics.getDeltaTime();
        if(rejoinTime>0.7f){
            car.putInTrack();
        }
    }
}