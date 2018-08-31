package Tetris.Model;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import static Tetris.Constants.*;

public class Hole {
    private List<BodyPart> body;
    private int count;
    private FigureInHole figureInHole;
    private int[] lines;
    private Figure figure;
    private boolean gameOver;


    private Figure followingFigure;
    private long timePassed;
    private long delta;
    public Hole() {
        followingFigure = new Figure();
        newFigure();
        figureInHole = new FigureInHole(figure);
        timePassed = 2;
        count = 0;
        delta = 0;
        gameOver = false;
        lines = new int[WORLD_HEIGHT + 1];              //колодец пустой
        for (int i = 0; i< WORLD_HEIGHT + 1; i++){
            lines[i] = 0 ;
        }
        lines[WORLD_HEIGHT] = figureInHole.getBinarWidthHole();                  //дно колодца (все 1) (невидимое)
    }

    public void newFigure(){
        figure = followingFigure;
        followingFigure = new Figure();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getCount(){return count;}


    private void updateLines(){
        for (int i = 0; i< WORLD_HEIGHT; i++){
            lines[i] = lines[i] | figureInHole.getFigurHole()[i];  //укладка фигуры на кучу (или дно)
        }
        count+=4;                                           //начисление очков за фигуру
        for (int j = 0; j< WORLD_HEIGHT; j++){            //удаление заполненных линий
            if (lines[j] == figureInHole.getBinarWidthHole()){
                try {
                Thread.sleep(400);                  //задержка при удалении линий
            }catch (Exception e){e.printStackTrace();}
                count = count + 20;                         //начисление очков за удаленную линию
             //   timePassed--;
                for (int k=j;k>0;k--)lines[k] = lines[k-1];
            }
        }
    }
    public long getTimePassed() {
        return timePassed;
    }
    public void move() {

        if (isStopLine()){
            if (figureInHole.getCurrentFig() == WORLD_HEIGHT -1){ //System.out.println("Игра закончена");
                try {
                    Thread.sleep(1000);
                }catch (Exception e){e.printStackTrace();}
                gameOver = true; return;//Требуются действия
                }

            updateLines();
            newFigure();
            figureInHole = new FigureInHole(figure);            //обновление фигуры
        }else {
            if (++delta == timePassed){
      /*      try {
            Thread.sleep(timePassed == 0? timePassed = 1: timePassed);
        }catch (Exception e){e.printStackTrace();}*/
            delta = 0;
            figureInHole.downMove();}
        }
    }
    private List<BodyPart> getFollowingFigure() {                   //формирование показа следующей фигуры
        List<BodyPart> listFollowingFigure = new LinkedList<BodyPart>();
        for (int i=0;i < 4;i++){
            String a = Integer.toBinaryString(followingFigure.getFig()[i + 4 * followingFigure.getView()]);
            while (a.length()< 4)a = "0" + a;
            char[] b = a.toCharArray();
            for (int j=0; j<b.length;j++){
                if (b[j] == '1')listFollowingFigure.add(new BodyPart(j + WORLD_WIDTH - 5, WORLD_HEIGHT - i -2,followingFigure.getColorFig()));
            }
        }
        return listFollowingFigure;
    }
    private List<BodyPart> intToBodyPart(int dimX, int spaceX,int spaceY,int[] fig, Color color){
        body = new LinkedList<BodyPart>();
        for (int i=0;i < fig.length ;i++){
            String a = Integer.toBinaryString(fig[i]);
            while (a.length()< dimX)a = "0" + a;
            char[] b = a.toCharArray();
            body.add(new BodyPart(1,i-1,color));body.add(new BodyPart(dimX + 2,i-1,color));
            for (int j=b.length-1;j>=0;j--){
                if (b[j] == '1')body.add(new BodyPart(j + 2 + spaceX, spaceY - i,color));
            }
        }
        return body;
    }


    public List<BodyPart> getBody() {               //все вместе
        body = new LinkedList<BodyPart>();
        for (int i=2;i < WORLD_HEIGHT ;i++){
            String a = Integer.toBinaryString(figureInHole.getFigurHole()[i] | lines[i]);
            while (a.length()< figureInHole.getHoleWidth())a = "0" + a; //System.out.println(i + " " + a);
            char[] b = a.toCharArray();
            body.add(new BodyPart(1,i-1,Color.BLUE));body.add(new BodyPart(figureInHole.getHoleWidth() + 2,i-1,Color.BLUE));
            for (int j=b.length-1;j>=0;j--){
                if (b[j] == '1')body.add(new BodyPart(j + 2, WORLD_HEIGHT - i,Color.blue));
            }
        }body.addAll(getFollowingFigure());
        return body;
   // return intToBodyPart(figureInHole.getHoleWidth(),0,WORLD_HEIGHT,figureInHole.getFigurHole(),Color.green);
    }
    public List<BodyPart> getBodyFig() {            //фигура падающая

        List<BodyPart> bodyFig = new LinkedList<BodyPart>();
        for (int i=2;i < WORLD_HEIGHT ;i++){
            String a = Integer.toBinaryString(figureInHole.getFigurHole()[i]);
            while (a.length()< figureInHole.getHoleWidth())a = "0" + a; //System.out.println(i + " " + a);
            char[] b = a.toCharArray();

            for (int j=0;j < b.length;j++){
                if (b[j] == '1')bodyFig.add(new BodyPart(j + 2, WORLD_HEIGHT - i,figure.getColorFig()));
            }
        }
        bodyFig.addAll(getFollowingFigure());       //добавление показа следующей фигуры
        return bodyFig;
    }
    public List<BodyPart> getBodyHole() {           //границы колодца и куча на дне
        Color color = new Color(255,128,0);
        List<BodyPart> bodyHole = new LinkedList<BodyPart>();
        for (int i=2;i < WORLD_HEIGHT ;i++){
            String a = Integer.toBinaryString(lines[i]);
            while (a.length()< figureInHole.getHoleWidth())a = "0" + a;
            char[] b = a.toCharArray();
            bodyHole.add(new BodyPart(1,i-1,color));bodyHole.add(new BodyPart(figureInHole.getHoleWidth() + 2,i-1,color));
            for (int j=b.length-1;j>=0;j--){
                if (b[j] == '1')bodyHole.add(new BodyPart(j + 2, WORLD_HEIGHT - i,color));
            }
        }

        return bodyHole;
    }

    public void processTetris(KeyEvent event){             //опрос клавиатуры
        switch (event.getKeyCode()) {
            case  KeyEvent.VK_NUMPAD5:
                isTurne();
                break;
            case  KeyEvent.VK_UP :
                isTurne();
                break;
            case KeyEvent.VK_RIGHT:
                if (isRight())
                figureInHole.rightMove();
                break;
            case KeyEvent.VK_NUMPAD6:
                if (isRight())
                figureInHole.rightMove();
                break;
            case KeyEvent.VK_DOWN:
                if (isStopLine())break;
               figureInHole.downMove();
                break;
            case KeyEvent.VK_SPACE:
                while (!isStopLine())
               figureInHole.downMove();
                break;
            case KeyEvent.VK_NUMPAD0:
                while (!isStopLine())
               figureInHole.downMove();
                break;
            case KeyEvent.VK_LEFT:
                if (isLeft())
                figureInHole.leftMove();
                break;
            case KeyEvent.VK_NUMPAD4:
                if (isLeft())
                figureInHole.leftMove();
                break;
        }
    }
    public boolean isStopLine(){                    //проверка контакта фигуры с кучей или дном
        for (int i = 0; i < WORLD_HEIGHT; i++){
            if ((figureInHole.getFigurHole()[i] & lines[i+1]) == 0)continue;
            return true;
        }
        return false;
    }
    public boolean isLeft() {
        for (int i = 0; i< WORLD_HEIGHT; i++) {
            if (((figureInHole.getFigurHole()[i] << 1) & lines[i]) == 0) continue;
            return false;
        }
        return true;
    }
    public boolean isRight(){
        for (int i=0; i < WORLD_HEIGHT; i++){
            if (((figureInHole.getFigurHole()[i]>>1) & lines[i]) == 0)continue;
            return false;
        }
        return true;
    }
    public void isTurne(){                              //проверка воэможности поворота и поворот
        int tempWiev = figureInHole.getFigure().getView();
        figureInHole.turneFig();
        for (int i=0; i < WORLD_HEIGHT; i++){
            if (((figureInHole.getFigurHole()[i]) & lines[i]) == 0)continue;
            figureInHole.getFigure().setView(tempWiev);break;
        }
    }
}
