package game.logic;

/**
 * Created by digbe on 20/04/2016.
 */
public class Curve extends Piece{
    private int curveType; //1->UR, 2->UL, 3->RD,4->LD,5->DR,6->DL,7->RU,8->LU
    private int lane; //1,2,3,4
    private double maxSpeed[];
    private double driftSpeed[];
    private int angle;
    private char[][] curve;

    public Curve(int angle,int tipo) {
        this.angle = angle; //por defeito a curva é de 90 graus
        maxSpeed = new double[4];
        driftSpeed = new double[4];
        if (angle == 90) {
            maxSpeed[0] = angle * 1.4;
            maxSpeed[1] = angle * 1.3;
            maxSpeed[2] = angle * 1.2;
            maxSpeed[3] = angle;
            driftSpeed[0] = angle * 0.8;
            driftSpeed[1] = angle * 0.7;
            driftSpeed[2] = angle * 0.6;
            driftSpeed[3] = angle * 0.5;
            curve=new char[6][6];
            curve=new char[][]{ {'x','x','x','x','x','x'},
                                {'x','t','1','1','1','1'},
                                {'x','1','t','2','2','2'},
                                {'x','1','2','t','3','3'},
                                {'x','1','2','3','t','4'},
                                {'x','1','2','3','4','x'}};
        }else if(angle == 45){
            maxSpeed[0] = 140;
            maxSpeed[1] = 130;
            maxSpeed[2] = 120;
            maxSpeed[3] = 110;
            driftSpeed[0] = 120;
            driftSpeed[1] = 130;
            driftSpeed[2] = 100;
            driftSpeed[3] = 90;
        }
    }


}
