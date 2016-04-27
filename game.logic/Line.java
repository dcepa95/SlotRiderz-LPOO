package game.logic;

/**
 * Created by digbe on 20/04/2016.
 */
public class Line extends Piece{
    private int lineType; //1->U, 2->D, 3->R, 4->L
    private int lane; //1,2,3,4
    char[][] line;

    public Line(int type) {
        lineType=type;
        line=new char[6][6];
        if(lineType==0){
            line = new char[][]{{'x','1','2','3','4','x'},
                    {'x','1','2','3','4','x'},
                    {'x','1','2','3','4','x'},
                    {'x','1','2','3','4','x'},
                    {'x','1','2','3','4','x'},
                    {'x','1','2','3','4','x'} };
        }
        if(lineType==1){
            line = new char[][]{{'x','1','2','3','4','x'},
                                {'x','1','2','3','4','x'},
                                {'x','1','2','3','4','x'},
                                {'x','1','2','3','4','x'},
                                {'x','1','2','3','4','x'},
                                {'x','1','2','3','4','x'} };
        }else if(lineType==2) {
            line = new char[][]{{'x', '4', '3', '2', '1', 'x'},
                                {'x', '4', '3', '2', '1', 'x'},
                                {'x', '4', '3', '2', '1', 'x'},
                                {'x', '4', '3', '2', '1', 'x'},
                                {'x', '4', '3', '2', '1', 'x'},
                                {'x', '4', '3', '2', '1', 'x'}};
        }else if(lineType==3) {
            line = new char[][]{{'x', 'x', 'x', 'x', 'x', 'x'},
                                {'1', '1', '1', '1', '1', '1'},
                                {'2', '2', '2', '2', '2', '2'},
                                {'3', '3', '3', '3', '3', '3'},
                                {'4', '4', '4', '4', '4', '4'},
                                {'x', 'x', 'x', 'x', 'x', 'x'}};
        }else if(lineType==4) {
            line = new char[][]{{'x', 'x', 'x', 'x', 'x', 'x'},
                                {'4', '4', '4', '4', '4', '4'},
                                {'3', '3', '3', '3', '3', '3'},
                                {'2', '2', '2', '2', '2', '2'},
                                {'1', '1', '1', '1', '1', '1'},
                                {'x', 'x', 'x', 'x', 'x', 'x'}};
        }
    }

    public void driveLine(Vehicle car){
        if(lineType==1) {
            if (car.getLane() == 1) {
                car.setPositionPiece(1, 5);
            }else if (car.getLane() == 2){
                car.setPositionPiece(2, 5);
            }else if (car.getLane() == 3){
                car.setPositionPiece(3, 5);
            }else if (car.getLane() == 4){
                car.setPositionPiece(4, 5);
            }
            car.setOrientation(1);
        }else if(lineType==2) {
            if (car.getLane() == 1) {
                car.setPositionPiece(4, 0);
            }else if (car.getLane() == 2){
                car.setPositionPiece(3, 0);
            }else if (car.getLane() == 3){
                car.setPositionPiece(2, 0);
            }else if (car.getLane() == 4){
                car.setPositionPiece(1, 0);
            }
            car.setOrientation(2);
        }else if(lineType==3) {
            if (car.getLane() == 1) {
                car.setPositionPiece(0, 1);
            }else if (car.getLane() == 2){
                car.setPositionPiece(0, 2);
            }else if (car.getLane() == 3){
                car.setPositionPiece(0, 3);
            }else if (car.getLane() == 4){
                car.setPositionPiece(0, 4);
            }
            car.setOrientation(3);
        }else if(lineType==4) {
            if (car.getLane() == 1) {
                car.setPositionPiece(5, 4);
            }else if (car.getLane() == 2){
                car.setPositionPiece(5, 3);
            }else if (car.getLane() == 3){
                car.setPositionPiece(5, 2);
            }else if (car.getLane() == 4){
                car.setPositionPiece(5, 1);
            }
            car.setOrientation(4);
        }
    }
}