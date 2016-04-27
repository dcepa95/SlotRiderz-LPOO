package game.logic;

import java.util.ArrayList;


public class Track {
    private Piece[][] track;

    //----------------------------------------------------------------------------------------------------------------------------------------------

     /*
     *  Constructor
     */
    //1-4 retas 5-12 curvas
    public Track() {
        track=new Piece[][]{{new Curve(90,1),new Line(3),new Line(3),new Line(3),new Curve(90,3)},
                            {new Line(1),new Line(0),new Line(0),new Line(0),new Line(2)},
                            {new Line(1),new Line(0),new Line(0),new Line(0),new Line(2)},
                            {new Curve(90,8),new Line(4),new Line(4),new Line(4),new Curve(90,6)}};
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------

    /*
     *  get Methods
     */
    public Piece[][] getTrack(){
        return track;
    }

    public void printTrack(){
        for(int i=0;i<track.length*6;i++){
            for(int j=0;j<track[i/6].length;j++){
                track[i/6][j/6].printLine(j%6);
            }
        }
    }

}
