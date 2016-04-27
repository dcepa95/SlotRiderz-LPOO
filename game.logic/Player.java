package game.logic;

public class Player {
    private Vehicle myVehicle;
    private int ID;
    private int position;
    private int currentLap;

    //----------------------------------------------------------------------------------------------------------------------------------------------

    /*
     *  Constructor
     */

    public Player(int ID) {
        myVehicle = null;
        this.ID = ID;
        currentLap = 1;
        position = 1;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------

    /*
     *  set Methods
     */

    public void addVehicle(Vehicle newVehicle) {

        myVehicle = newVehicle;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------

    /*
     *  get Methods
     */

    public int getCurrentLap() {

        return currentLap;
    }

    public int getPosition() {

        return position;
    }

    public Vehicle getVehicle() {

        return myVehicle;
    }

    public int getID() {

        return ID;
    }


}
