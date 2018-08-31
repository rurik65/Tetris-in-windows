package Tetris.Model;

import java.awt.*;

public class BodyPart {
   private int x;
    private int y;

    private Color color;
    public BodyPart(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }
    public static Color getRandomColor(){

        return new Color(getIntRandom(),getIntRandom(),getIntRandom());
    }
    private static int getIntRandom(){
        return 255 * (int)(2*Math.random());// + 55 *(int)(2* Math.random());
    }
}
