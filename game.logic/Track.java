package game.logic;

import java.util.ArrayList;


public class Track {
    private ArrayList<Lane> lanes;
    private ArrayList<char[][]> playersStartingPositions;
    private ArrayList<char[][]> finishLine;

    //----------------------------------------------------------------------------------------------------------------------------------------------

     /*
     *  Constructor
     */

    public Track() {
        lanes = new ArrayList<Lane>();
        finishLine = new ArrayList<char[][]>();
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------

    /*
     *  get Methods
     */

    public ArrayList<char[][]> getFinishLine() {
        return finishLine;
    }

    public ArrayList<char[][]> getPlayersStartingPositions() {
        return playersStartingPositions;
    }

    public ArrayList<Lane> getLanes() {
        return lanes;
    }
}
