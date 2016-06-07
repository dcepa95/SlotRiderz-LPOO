package com.mygdx.game.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


/**
 * Created by digbe on 10/05/2016.
 */
public class Car extends Sprite{
    private Vector2 velocity = new Vector2();
    private Vector2 accelerationVec = new Vector2();
    private float speed = 0;
    private float driftSpeed = 300;
    private float acceleration=500;
    private float breaking = 700;
    private int maxSpeed=600;
    private Array<Vector2> path;
    private int nextWaypoint=0;
    private Array<Polygon> hitbox;

    private int pos=0;

    private boolean changingLane=false;
    private int inCurve=0;  //0 out, 1 right , 2 left
    private float angle;
    private float driftAngle=0;

    private int waypointsPassed=0;

    private boolean accelerating=false;

    private boolean offTrack=false;
    private boolean exploding=false;
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
        hitbox = new Array<Polygon>();
        //bottom
        hitbox.add(new Polygon(new float[]{0,0,
                0, getHeight() / 6,
                getWidth(), getHeight() / 6,
                getWidth(), 0
                }));
        //top
        hitbox.add(new Polygon(new float[]{0,getHeight()/3*2/4*3,
                0, getHeight() / 3*2,
                getWidth(), getHeight() / 3*2,
                getWidth(), getHeight()/3*2/4*3
        }));
        //left
        hitbox.add(new Polygon(new float[]{0,getHeight()/6,
                0, getHeight()/3*2/4*3,
                getWidth()/2, getHeight() / 3*2/4*3,
                getWidth()/2, getHeight()/6
        }));
        //right
        hitbox.add(new Polygon(new float[]{getWidth()/2,getHeight()/6,
                getWidth()/2, getHeight()/3*2/4*3,
                getWidth(), getHeight() / 3*2/4*3,
                getWidth(), getHeight()/6
        }));
        for(Polygon p:hitbox) {
            p.setOrigin(getWidth() / 2, getHeight() / 2);
        }
    }

    @Override
    public void draw(Batch batch) {
        if(!exploding)
            super.draw(batch);
    }

    public void update(float deltaTime, int lane) {
        angle =(float) Math.atan2(path.get(nextWaypoint).y - getY()-getHeight()/2 , path.get(nextWaypoint).x - getX()-getWidth()/2);
        velocity.set( (float) Math.cos(angle)*speed , (float) Math.sin(angle)*speed );
        setCenter( getX() + getWidth()/2 + velocity.x * deltaTime , getY()+getHeight()/2 + velocity.y * deltaTime );
        if(inCurve == 1) {
            if(accelerating) {
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
            }else{
                this.driftAngle *= 0.95f;
            }
        } else if(inCurve == 2){
            if(accelerating) {
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
            }else{
                this.driftAngle *= 0.95f;
            }
        }else
            this.driftAngle *= 0.8f;

        if(driftAngle>50 || driftAngle<-50){
            offTrack=true;
            if(driftAngle>0)
                driftAngle+=20;
            else
                driftAngle-=20;
            driftAngle*=0.95;
            setRotation(angle * MathUtils.radiansToDegrees - 90 - (driftAngle));
        }else
            setRotation(angle * MathUtils.radiansToDegrees - 90 - (driftAngle *(speed) / 500));

        if(isWaypointReached()){
            waypointsPassed++;
            changingLane=false;
            if(nextWaypoint+1 < path.size){
                if(nextWaypoint==0) {
                    lap++;
                }
                nextWaypoint++;
            }else{
                nextWaypoint=0;
            }
        }

        for(Polygon p : hitbox) {
            p.setPosition(getX(), getY());
            p.setRotation(getRotation());
        }

        /* //for Other game MOODE
        if(pos==1) {
            camera.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2, 0);
            //camera.rotate(angle*MathUtils.radiansToDegrees-90 -previousCamAngle);
        }*/
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

    public void setPath(Array<Vector2> path){
        this.path=path;
    }

    public void setPos (int pos){
        this.pos=pos;
    }

    public int getPos() {
        return pos;
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

    public boolean isOffTrack() {
        return offTrack;
    }

    public void putInTrack(){
        angle =(float) Math.atan2(path.get(nextWaypoint).y - getY()-getHeight()/2 , path.get(nextWaypoint).x - getX()-getWidth()/2);
        driftAngle=0;
        speed=0;
        offTrack=false;
    }

    public void setAccelerating(boolean accelerating) {
        this.accelerating = accelerating;
    }

    public double getDistanceToWaypoint(){
        return Math.sqrt(Math.pow(path.get(nextWaypoint).x - getX()-getWidth()/2,2) + Math.pow(path.get(nextWaypoint).y - getY()-getHeight()/2,2));
    }

    public int getWaypointsPassed() {
        return waypointsPassed;
    }

    public void setWaypointsPassed(int waypointsPassed) {
        this.waypointsPassed = waypointsPassed;
    }

    public Array<Polygon> getHitBox(){
        return hitbox;
    }

    public void setExploding(boolean exploding){
        this.exploding=exploding;
    }

    public boolean isExploding(){
        return exploding;
    }

    public Polygon getFront(){
        return hitbox.get(1);
    }
    public Polygon getBack(){
        return hitbox.get(0);
    }
    public Polygon getLeft(){
        return hitbox.get(2);
    }
    public Polygon getRight(){
        return hitbox.get(3);
    }
}
