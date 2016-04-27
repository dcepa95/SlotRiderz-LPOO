package game.logic;

/**
 * Created by digbe on 20/04/2016.
 */
public class Curve {
    private int curveType; //1=90º, 2=45º
    private int lane; //1,2,3,4
    private double maxSpeed[];
    private double driftSpeed[];
    private int angle;

    public Curve(int angle) {
        this.angle = angle; //por defeito a curva é de 90 graus
        maxSpeed = new double[3];
        driftSpeed = new double[3];
        if (angle == 90) {
            maxSpeed[0] = angle * 1.4;
            maxSpeed[1] = angle * 1.3;
            maxSpeed[2] = angle * 1.2;
            maxSpeed[3] = angle;
            driftSpeed[0] = angle * 0.8;
            driftSpeed[1] = angle * 0.7;
            driftSpeed[2] = angle * 0.6;
            driftSpeed[3] = angle * 0.5;
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
