package game.logic;


public class Lane {
    private int ID;
    private Position finishLine;



    //----------------------------------------------------------------------------------------------------------------------------------------------

    /*
    *  Constructor
    */

    public Lane(int ID) {

        this.ID = ID;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------

    /*
     *  get Methods
     */

    public int getID() {

        return ID;
    }
}