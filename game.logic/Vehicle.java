package game.logic;

public class Vehicle {
    private Position positionMap;
    private Position positionPiece;

    private int lane;
    private int orientation; //1->U, 2->D, 3->R, 4->L
    private double velocity;

    //----------------------------------------------------------------------------------------------------------------------------------------------

    //making changes
     /*
     * -- Constructor --
     */

    public Vehicle(Position inicialMap, int lane) {
        positionMap = inicialMap;
        this.lane = lane;
        velocity = 0;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------

    /*
     * -- get Methods --
     */

    public Position getPositionMap() {

        return positionMap;
    }

    public Position getPositionPiece() {

        return positionPiece;
    }

    public double getVelocity() {

        return velocity;
    }

    public int getLane() {
        return lane;
    }

    /*
     * -- set Methods --
     */

    public void setPositionMap(int x, int y){

        positionMap.x=x;
        positionMap.y=y;
    }

    public void setPositionPiece(int x, int y){

        positionPiece.x=x;
        positionPiece.y=y;
    }

    public void setVelocity(double v){

        velocity=v;
    }

    public void setLane(int lane) {

        this.lane=lane;
    }

    public void setOrientation(int ori){
        orientation=ori;
    }
}
