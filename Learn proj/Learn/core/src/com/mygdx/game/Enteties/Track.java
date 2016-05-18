package com.mygdx.game.Enteties;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by digbe on 10/05/2016.
 */
public class Track {

    private Array<Array<Vector2> > tracks;
    private Array<Integer> curveLeftPoints;
    private Array<Integer> curveRightPoints;
    private float actualAngle = 0; //in rad, com objetivo de fazer retas em varios angulos. Ainda nao implementado.
    private Vector2 beginningPos = new Vector2((float) 200,(float) 200);
    private Vector2 currentPos = new Vector2((float) 200,(float) 200);
    private int radius=105;
    private int counter=-1;

    private void makeCurve(float cAngle, int direction, int points){
        float incAngle = cAngle/points;
        float finalAngle = actualAngle + cAngle;
        int initialRadius=radius;
        Vector2 temp;
        Vector2 inicialPos = currentPos;
        Vector2 finalPos = new Vector2();
        int it=0;

        if(direction == 2 || direction == 3 || direction == 5 || direction == 8){
            radius+=75;
            initialRadius=radius;
        }

        for(Array v : tracks) {
            currentPos=inicialPos;
            if (direction == 1) { //up and Right
                Vector2 center = new Vector2(currentPos.x + initialRadius, currentPos.y);
                for (float i = actualAngle + incAngle; i < finalAngle + incAngle; i += incAngle) {
                    temp = new Vector2(center.x - (float) radius * MathUtils.cos(i), center.y + (float) radius * MathUtils.sin(i));
                    v.add(temp);
                    currentPos = new Vector2(center.x - (float) radius * MathUtils.cos(i), center.y + (float) radius * MathUtils.sin(i));
                }
                radius += 25;
            } else if (direction == 2) { //up and Left
                Vector2 center = new Vector2(currentPos.x - initialRadius, currentPos.y);
                for (float i = actualAngle + incAngle; i < finalAngle + incAngle; i += incAngle) {
                    temp = new Vector2(center.x + (float) radius * MathUtils.cos(i), center.y + (float) radius * MathUtils.sin(i));
                    v.add(temp);
                    currentPos = new Vector2(center.x + (float) radius * MathUtils.cos(i), center.y + (float) radius * MathUtils.sin(i));
                }
                radius -= 25;
            } else if (direction == 3) { //down and Right
                Vector2 center = new Vector2(currentPos.x + initialRadius, currentPos.y);
                for (float i = actualAngle + incAngle; i < finalAngle + incAngle; i += incAngle) {
                    temp = new Vector2(center.x - (float) radius * MathUtils.cos(i), center.y - (float) radius * MathUtils.sin(i));
                    v.add(temp);
                    currentPos = new Vector2(center.x - (float) radius * MathUtils.cos(i), center.y - (float) radius * MathUtils.sin(i));
                }
                radius -= 25;
            } else if (direction == 4) { //down and Left
                Vector2 center = new Vector2(currentPos.x - initialRadius, currentPos.y);
                for (float i = actualAngle + incAngle; i < finalAngle + incAngle; i += incAngle) {
                    temp = new Vector2(center.x + (float) radius * MathUtils.cos(i), center.y - (float) radius * MathUtils.sin(i));
                    v.add(temp);
                    currentPos = new Vector2(center.x + (float) radius * MathUtils.cos(i), center.y - (float) radius * MathUtils.sin(i));
                }
                radius += 25;
            } else if (direction == 5) { //Left and down
                Vector2 center = new Vector2(currentPos.x, currentPos.y - initialRadius);
                for (float i = actualAngle + incAngle; i < finalAngle + incAngle; i += incAngle) {
                    temp = new Vector2(center.x - (float) radius * MathUtils.sin(i), center.y + (float) radius * MathUtils.cos(i));
                    v.add(temp);
                    currentPos = new Vector2(center.x - (float) radius * MathUtils.sin(i), center.y + (float) radius * MathUtils.cos(i));
                }
                radius -= 25;
            } else if (direction == 6) { //Right and Down
                Vector2 center = new Vector2(currentPos.x, currentPos.y - initialRadius);
                for (float i = actualAngle + incAngle; i < finalAngle + incAngle; i += incAngle) {
                    temp = new Vector2(center.x + (float) radius * MathUtils.sin(i), center.y + (float) radius * MathUtils.cos(i));
                    v.add(temp);
                    currentPos = new Vector2(center.x + (float) radius * MathUtils.sin(i), center.y + (float) radius * MathUtils.cos(i));
                }
                radius += 25;
            } else if (direction == 7) { //Left and up
                Vector2 center = new Vector2(currentPos.x, currentPos.y + initialRadius);
                for (float i = actualAngle + incAngle; i < finalAngle + incAngle; i += incAngle) {
                    temp = new Vector2(center.x - (float) radius * MathUtils.sin(i), center.y - (float) radius * MathUtils.cos(i));
                    v.add(temp);
                    currentPos = new Vector2(center.x - (float) radius * MathUtils.sin(i), center.y - (float) radius * MathUtils.cos(i));
                }
                radius += 25;
            } else if (direction == 8) { //Right and up
                Vector2 center = new Vector2(currentPos.x, currentPos.y + initialRadius);
                for (float i = actualAngle + incAngle; i < finalAngle + incAngle; i += incAngle) {
                    temp = new Vector2(center.x + (float) radius * MathUtils.sin(i), center.y - (float) radius * MathUtils.cos(i));
                    v.add(temp);
                    currentPos = new Vector2(center.x + (float) radius * MathUtils.sin(i), center.y - (float) radius * MathUtils.cos(i));
                }
                radius -= 25;
            }
            if (it == 0)
                finalPos = currentPos;
            //radius += 25;
            it++;

        }
        radius = 100;
        currentPos=finalPos;

        for(int i=0;i<points;i++){
            if(direction==1 || direction==6 || direction==7 || direction==4) {
                curveRightPoints.add(counter);
            }
            else {
                curveLeftPoints.add(counter-4);
            }
            counter++;
        }
    }

    private void makeLine(int points, int type){
        Vector2 internPos = currentPos;
        Vector2 finalPos = new Vector2();
        int it=0;
        for(Array v : tracks) {
            currentPos=internPos;
            if (type == 1) { // up
                for (int i = 1; i < points; i++) {
                    v.add(new Vector2(currentPos.x, currentPos.y + i * 20));
                }
                internPos=new Vector2(internPos.x-25,internPos.y);
                if(it==0)
                    finalPos = new Vector2(currentPos.x, currentPos.y + (points - 1) * 20);
            } else if (type == 2) { // down
                for (int i = 1; i < points; i++) {
                    v.add(new Vector2(currentPos.x, currentPos.y - i * 20));
                }
                internPos=new Vector2(internPos.x+25,internPos.y);
                if(it==0)
                    finalPos = new Vector2(currentPos.x, currentPos.y - (points - 1) * 20);
            } else if (type == 3) { // right
                for (int i = 1; i < points; i++) {
                    v.add(new Vector2(currentPos.x + i * 20, currentPos.y));
                }
                internPos=new Vector2(internPos.x,internPos.y+25);
                if(it==0)
                    finalPos = new Vector2(currentPos.x + (points - 1) * 20, currentPos.y);
            } else if (type == 4) { // left
                for (int i = 1; i < points; i++) {
                    v.add(new Vector2(currentPos.x - i * 20, currentPos.y));
                }
                internPos=new Vector2(internPos.x,internPos.y-25);
                if(it==0)
                    finalPos = new Vector2(currentPos.x - (points - 1) * 20, currentPos.y);
            }
            it++;
        }
        currentPos=finalPos;

        for(int i=0;i<points;i++){
            counter++;
        }
    }

    private void track1(){
        tracks = new Array<Array<Vector2> >();
        tracks.add(new Array<Vector2>());
        tracks.add(new Array<Vector2>());
        tracks.add(new Array<Vector2>());
        tracks.add(new Array<Vector2>());

        curveRightPoints = new Array<Integer>();
        curveLeftPoints = new Array<Integer>();

        makeLine(9,1);
        makeCurve((float) 90*MathUtils.degreesToRadians,1,10);
        makeLine(8,3);
        makeCurve((float) 90*MathUtils.degreesToRadians,6,10);
        makeCurve((float) 90*MathUtils.degreesToRadians,4,10);
        makeLine(8,4);
        makeLine(8,4);
        makeLine(8,4);
        makeCurve((float) 90*MathUtils.degreesToRadians,5,10);
        makeLine(8,2);
        makeCurve((float) 90*MathUtils.degreesToRadians,3,10);
        makeCurve((float) 90*MathUtils.degreesToRadians,8,10);
        makeLine(8,1);
        makeLine(7,1);
    }

    private void track2(){
        tracks=new Array<Array<Vector2> >();
        tracks.add(new Array<Vector2>());
        tracks.add(new Array<Vector2>());
        tracks.add(new Array<Vector2>());
        tracks.add(new Array<Vector2>());
        makeLine(3,1);
        makeCurve((float) 90*MathUtils.degreesToRadians,1,10);
        makeLine(3,3);
        makeCurve((float) 90*MathUtils.degreesToRadians,6,10);
        makeLine(3,2);
        makeCurve((float) 90*MathUtils.degreesToRadians,4,10);
        makeLine(3,4);
        makeCurve((float) 90*MathUtils.degreesToRadians,7,10);
    }

    private void track3(){
        tracks=new Array<Array<Vector2> >();
        tracks.add(new Array<Vector2>());
        tracks.add(new Array<Vector2>());
        tracks.add(new Array<Vector2>());
        tracks.add(new Array<Vector2>());
        makeLine(3,1);
        makeCurve((float) 90*MathUtils.degreesToRadians,1,10);
        makeLine(3,3);
        makeCurve((float) 90*MathUtils.degreesToRadians,6,10);
        makeLine(3,2);
        makeCurve((float) 90*MathUtils.degreesToRadians,4,10);
        makeLine(3,4);
        makeCurve((float) 90*MathUtils.degreesToRadians,7,10);
    }

    private void track4(){
        tracks=new Array<Array<Vector2> >();
        tracks.add(new Array<Vector2>());
        tracks.add(new Array<Vector2>());
        tracks.add(new Array<Vector2>());
        tracks.add(new Array<Vector2>());
        makeLine(3,1);
        makeCurve((float) 90*MathUtils.degreesToRadians,1,10);
        makeLine(3,3);
        makeCurve((float) 90*MathUtils.degreesToRadians,6,10);
        makeLine(3,2);
        makeCurve((float) 90*MathUtils.degreesToRadians,4,10);
        makeLine(3,4);
        makeCurve((float) 90*MathUtils.degreesToRadians,7,10);
    }

    public Track(){
        track1();
    }

    public Array<Array<Vector2>> getTrack() {
        return tracks;
    }

    public Vector2 getBeginningPos() {
        return beginningPos;
    }

    public void setTrack(int number){
        switch(number){
            case 1:
                track1();
            case 2:
                track2();
            case 3:
                track3();
            case 4:
                track4();
        }
    }

    public Array<Array<Integer> > getCurvePoints() {
        Array<Array<Integer> > temp = new Array<Array<Integer> >();
        temp.add(curveRightPoints);
        temp.add(curveLeftPoints);
        return temp;
    }
}
