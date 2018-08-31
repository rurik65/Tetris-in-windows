package Tetris.Model;

import java.lang.reflect.Array;

import static Tetris.Constants.*;

public class FigureInHole {
    private Figure figure;
  //  private Figure followingFigure;
    private int column;
    private int currentFig;
    private int view;
    private int holeWidth;                 //ширина колодца
    private final int binarWidthHole;       //
    public FigureInHole(Figure figure){
        this.figure = figure;

        holeWidth = WORLD_WIDTH - 8;
        binarWidthHole = (1<<holeWidth) - 1;
        currentFig = WORLD_HEIGHT -1;             // начальное положение фигуры по оси Y
        column = holeWidth / 2 -2;             // начальное положение фигуры по оси Х
     //   view = figure.getView();            // одна из 4-х проекций фигуры
    }
    public int getBinarWidthHole() {
        return binarWidthHole;
    }

    public void setView(int view) {
        this.view = view;
    }
    public int getHoleWidth() {
        return holeWidth;
    }

    public int getCurrentFig() {
        return currentFig;
    }

    public int[] getFigurHole() {
        if (column == -2)column++;
        int[] temp = new int[WORLD_HEIGHT + 2];
        if (column > holeWidth - 4){                //проверка выхода за левую границу
            int t;
            for (int j= figure.getView()*4;j<figure.getView()*4+4;j++){
                t = figure.getFig()[j]<<column;
                if (t >binarWidthHole ){column--;break;}
            }
        }
        if (column <= -1){                          //проверка выхода за правую границу
            int t;boolean isRight = false;
            for (int j= figure.getView()*4;j<figure.getView()*4+4;j++){
                t = figure.getFig()[j]%2;
                if (t > 0){isRight = true;break;}
            }
            if (!isRight){
            for (int i= 0;i < 4;i++){
                temp[WORLD_HEIGHT - currentFig-1 + i] = figure.getFig()[i + figure.getView()*4]>>1; //наложение фигуры на поле колодца
                                                                                        //
            }

            return temp;}
            column++;
        }
        try {
            for (int i= 0;i < 4;i++){
                temp[WORLD_HEIGHT - currentFig -1 + i] = figure.getFig()[i + figure.getView()*4]<<column; //наложение фигуры на поле колодца
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println(currentFig);e.printStackTrace();}

        //   System.out.println();
        return temp;
    }


    public Figure getFigure() {
        return figure;
    }

    public void turneFig(){figure.rotate();}
    public void leftMove(){                             //предварительный сдвиг, подтверждается в getFigureInHole
      //  if (column < WORLD_WIDTH - 1)
            column++;
      // System.out.println(column);
    }
    public void rightMove(){column--;                   //предварительный сдвиг, подтверждается в getFigureInHole
      //  System.out.println(column);
        }
    public void downMove(){
       // if (currentFig == 3 ){figure = new Figure();}
       //     else {
        currentFig--;}//}
}
