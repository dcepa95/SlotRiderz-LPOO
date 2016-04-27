package game.logic;


import java.util.ArrayList;
import java.util.HashMap;

public class Race {
    private Track myTrack;
    private ArrayList<Player> myPlayers;
    private int maxLaps;

    //----------------------------------------------------------------------------------------------------------------------------------------------

     /*
     *  Constructor
     */

    public Race(Track myTrack) {
        this.myTrack = myTrack;
        myPlayers = new ArrayList<Player>();
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------

    /*
     *  get Methods
     */

    public int getMaxLaps() {
        return maxLaps;
    }

    public Track getTrack() {
        return myTrack;
    }

}
