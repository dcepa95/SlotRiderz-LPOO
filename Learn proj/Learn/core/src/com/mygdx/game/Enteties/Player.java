package com.mygdx.game.Enteties;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

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

    private void updateCarSpeed(float deltaTime) {
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            car.setAccelerating(true);
            if (car.getSpeed() < car.getMaxSpeed()) {
                if(car.getSpeed() + car.getAcceleration()*deltaTime > car.getMaxSpeed())
                    car.setSpeed(car.getMaxSpeed());
                else
                    car.setSpeed(car.getSpeed() + car.getAcceleration() * deltaTime);

            }else{
                car.setSpeed((float)car.getMaxSpeed());
            }

        }else if(Gdx.input.isTouched()) {
            car.setAccelerating(true);
            if (car.getSpeed() < car.getMaxSpeed()) {
                if(car.getSpeed()+car.getAcceleration()*deltaTime>car.getMaxSpeed())
                    car.setSpeed(car.getMaxSpeed());
                else
                    car.setSpeed(car.getSpeed()+car.getAcceleration()*deltaTime);

            }else{
                car.setSpeed((float)car.getMaxSpeed());
            }

        }else{
            car.setAccelerating(false);
            if(car.getSpeed()>0){
                if(car.getSpeed()*0.95f<0)
                    car.setSpeed((float)0);
                else
                    car.setSpeed(car.getSpeed()*0.95f);
            }else{
                car.setSpeed((float)0);
            }
        }
    }

    private void updateLane(){
        if(!car.isChangingLane()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                if (lane < 3) {
                    lane++;
                    if( (car.getWaypoint()+3)%car.getPath().size >1 && (car.getWaypoint()+3)%car.getPath().size <= 4){
                        car.setLap(car.getLap()+1);
                    }
                    car.setWaypoint((car.getWaypoint() + 3) % car.getPath().size);
                    car.setWaypointsPassed(car.getWaypointsPassed()+2);
                    car.changeLane();
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
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

            if(Gdx.input.getX() > 300 && Gdx.input.getX() < 350 && Gdx.input.getY() > -270 && Gdx.input.getY() < -220){

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
            //updateCarSpeed(deltaTime);
            //updateLane();
            car.setPath(tracks.get(lane));
            isInCurve();
            car.update(deltaTime, lane);
            camera.position.set(car.getX() + car.getWidth() / 2, car.getY() + car.getHeight() / 2, 0);
            //camera.rotate(angle*MathUtils.radiansToDegrees-90 -previousCamAngle);
            //camera.rotate();
            camera.update();
            batch.setProjectionMatrix(camera.combined);
            sr.setProjectionMatrix(camera.combined);
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

    public int getDistanceDrove(){
        return car.getWaypointsPassed();
    }

    public int getLane(){
        return lane;
    }

    public void setLane(int lane){
        this.lane=lane;
    }
}
