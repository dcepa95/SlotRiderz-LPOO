package com.mygdx.game.Enteties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.concurrent.Callable;

/**
 * Created by digbe on 10/05/2016.
 */
public class Car extends Sprite{
    private Vector2 velocity = new Vector2();
    private float speed = 0;
    private float driftSpeed = 300;
    private float acceleration=500;
    private float breaking = 500;
    private int maxSpeed=700;
    private Array<Vector2> path;
    private int nextWaypoint=0;
    private int pos=0;
    private boolean changingLane=false;
    private int inCurve=0;  //0 out, 1 right , 2 left
    private int inCurveDrift=0;
    private float angle;
    private float driftAngle=0;

    private int lap=0;

    private ShapeRenderer sr;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    public Car(Sprite sprite, Array<Vector2> path, OrthographicCamera camera, SpriteBatch batch, ShapeRenderer sr){
        super(sprite);
        this.path=path;
        this.camera=camera;
        this.batch=batch;
        this.sr=sr;
    }

    @Override
    public void draw(Batch batch) {
        //update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    public void update(float deltaTime, int lane) {
        //updateSpeed(deltaTime);
        angle =(float) Math.atan2(path.get(nextWaypoint).y - getY()-getHeight()/2 , path.get(nextWaypoint).x - getX()-getWidth()/2);
        velocity.set( (float) Math.cos(angle)*speed , (float) Math.sin(angle)*speed );
        setCenter( getX() + getWidth()/2 + velocity.x * deltaTime , getY()+getHeight()/2 + velocity.y * deltaTime );
        setOriginCenter();
        if(inCurve == 1) {
            if(speed>driftSpeed) {
                switch(lane){
                    case 0:
                        this.driftAngle += speed / 4 * deltaTime;
                        break;
                    case 1:
                        this.driftAngle += speed / 8 * deltaTime;
                        break;
                    case 2:
                        this.driftAngle += speed / 12 * deltaTime;
                        break;
                    case 3:
                        this.driftAngle += speed / 16 * deltaTime;
                        break;
                }
            }
        } else if(inCurve == 2){
            if(speed>driftSpeed) {
                switch(lane){
                    case 0:
                        this.driftAngle -= speed / 16 * deltaTime;
                        break;
                    case 1:
                        this.driftAngle -= speed / 12 * deltaTime;
                        break;
                    case 2:
                        this.driftAngle -= speed / 8 * deltaTime;
                        break;
                    case 3:
                        this.driftAngle -= speed / 4 * deltaTime;
                        break;
                }
            }
        }else
            this.driftAngle *= .8;

        setRotation(angle * MathUtils.radiansToDegrees - 90 - (driftAngle *(speed) / 500));
//        setRotation(angle * MathUtils.radiansToDegrees - 90);

        if(isWaypointReached()){
            //setCenter(path.get(nextWaypoint).x , path.get(nextWaypoint).y);
            changingLane=false;
            if(nextWaypoint+1 < path.size){
                if(nextWaypoint==1) {
                    lap++;
                }
                nextWaypoint++;
            }else{
                nextWaypoint=0;
            }
        }

        if(pos==1) {
            camera.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2, 0);
        }
        //camera.rotate();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);
    }

    private boolean isWaypointReached() {
        //return Math.abs(path.get(nextWaypoint).x - getX()-getWidth()/2) <= speed*Gdx.graphics.getDeltaTime()  && Math.abs(path.get(nextWaypoint).y - getY()-getHeight()/2) <= speed*Gdx.graphics.getDeltaTime();
        return Math.sqrt(Math.pow(path.get(nextWaypoint).x - getX()-getWidth()/2,2) + Math.pow(path.get(nextWaypoint).y - getY()-getHeight()/2,2)) <= speed*Gdx.graphics.getDeltaTime();
    }

    public Array<Vector2> getPath(){
        return path;
    }

    public int getWaypoint() {
        return nextWaypoint;
    }

    public void updateSpeed(float deltaTime) {
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {

            if (speed < maxSpeed) {
                if(speed+acceleration*deltaTime>maxSpeed)
                    speed=maxSpeed;
                else
                    speed+=acceleration*deltaTime;

            }else{
                speed=(float)maxSpeed;
            }

        }else if(Gdx.input.isTouched()) {

            if (speed < maxSpeed) {
                if(speed+acceleration*deltaTime>maxSpeed)
                    speed=maxSpeed;
                else
                    speed+=acceleration*deltaTime;

            }else{
                speed=(float)maxSpeed;
            }

        }else{

            if(speed>0){
                if(speed-breaking*deltaTime<0)
                    speed=0;
                else
                    speed-=breaking*deltaTime;
            }else{
                speed=0;
            }
        }



    }

    public void setPath(Array<Vector2> path){
        this.path=path;
    }

    public void setPos (int pos){
        this.pos=pos;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float s) {
        speed=s;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public float getBreaking() {
        return breaking;
    }

    public void setWaypoint(int waypoint) {
        nextWaypoint = waypoint;
    }

    public boolean isChangingLane() {
        return changingLane;
    }

    public void changeLane(){
        changingLane=true;
    }

    public void setInCurveRight(){
        inCurve=1;
    }

    public void setInCurveLeft(){
        inCurve=2;
    }

    public void setOutCurve(){
        inCurve=0;
    }

    public int getLap() {
        return lap;
    }

    public void setLap(int lap) {
        this.lap = lap;
    }
}