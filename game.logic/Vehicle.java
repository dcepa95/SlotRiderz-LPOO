package game.logic;

public class Vehicle {
    private Position position;
    private int lane;
    private double velocity;

    //----------------------------------------------------------------------------------------------------------------------------------------------

    //making changes
     /*
     * -- Constructor --
     */

    public Vehicle() {

        velocity = 0;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------

    /*
     * -- get Methods --
     */

    public Position getPosition() {

        return position;
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

    public void setPosition(int x, int y){

        position.x=x;
        position.y=y;
    }

    public void setVelocity(double v){
        velocity=v;
    }

    public void setLane(int lane) {
        this.lane=lane;
    }
}
