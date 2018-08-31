package Tetris;

import Tetris.Model.BodyPart;
import Tetris.Model.Hole;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;

import static Tetris.Constants.*;

public class MainScene extends Scene {
    private Hole hole;                              // game field
    private Delay holeMoveDelay;

    public MainScene(Game game) {
        super(game);
        hole = new Hole();
        holeMoveDelay = new Delay(SCORE);
    }
    @Override
    public void update(long nanosPassed) {
        processInput();
        if (holeMoveDelay.updateAndCheck(nanosPassed)) {
            hole.move();
        }
        if (hole.isGameOver())game.setScene(new GameOverScene(game));
    }
    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, game.getScreenSize().width - CELL_SIZE * 6, game.getScreenSize().height);
     /*  Shape r = new Rectangle2D.Float(CELL_SIZE,2 * CELL_SIZE,(WORLD_WIDTH - 8)*CELL_SIZE ,(WORLD_HEIGHT-2)*CELL_SIZE);
       g.setStroke(new BasicStroke(1));
       g.setPaint(Color.yellow);
       g.draw(r);*/

        Image image = Toolkit.getDefaultToolkit().getImage("./resources/mountain.jpg");
        g.drawImage(image, CELL_SIZE , CELL_SIZE * 2,(WORLD_WIDTH - 8)*CELL_SIZE,(WORLD_HEIGHT-2)*CELL_SIZE, new ImageObserver() {
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                return true;
            }
        });
        g.setColor(Color.yellow);
        g.fillRect((WORLD_WIDTH - 6) * CELL_SIZE,0,6 * CELL_SIZE,game.getScreenSize().height);
        g.setFont(new Font("Times New Roman", Font.BOLD,30));
        g.setPaint(Color.red);
        String message = "TETRIS";
        Rectangle2D messageBounds = g.getFontMetrics().getStringBounds(message, g);
        int messageWidth = (int)(messageBounds.getWidth());
        g.drawString(message,
                (game.getScreenSize().width - CELL_SIZE * 6) / 2 - messageWidth / 2,
                CELL_SIZE * 2 -4
        );

        g.setFont(new Font("Arial", Font.BOLD,14));
        g.setColor(Color.black);
        g.drawString("COUNT",(WORLD_WIDTH - 5) * CELL_SIZE + 8,CELL_SIZE * 8);
        g.setColor(Color.black);
        g.drawString(String.valueOf(hole.getCount()),(WORLD_WIDTH - 3) * CELL_SIZE,CELL_SIZE * 10);

        g.drawString("SCORE",(WORLD_WIDTH - 5) * CELL_SIZE + 8,CELL_SIZE * 12);
        g.drawString(String.valueOf(SCORE - holeMoveDelay.getDelayNanos()),(WORLD_WIDTH - 3) * CELL_SIZE,CELL_SIZE * 14);
        drawHole(g);
    }
    private void drawHole(Graphics2D g) {
     /*   g.setColor(Color.green);
        for (BodyPart bodyPart : hole.getBody()) {
            g.fillRoundRect(
                    bodyPart.getX() * CELL_SIZE - CELL_SIZE,
                    game.getScreenSize().height - (bodyPart.getY() * CELL_SIZE),
                    CELL_SIZE,
                    CELL_SIZE,5,5
            );
        }*/
        holeMoveDelay.diffDelayNanos();

        for (BodyPart bodyPart : hole.getBodyFig()) {
            g.setColor(bodyPart.getColor());
            g.fillRoundRect(
                    bodyPart.getX() * CELL_SIZE - CELL_SIZE,
                    game.getScreenSize().height - (bodyPart.getY() * CELL_SIZE),
                    CELL_SIZE,
                    CELL_SIZE,7,7
            );
        }

        for (BodyPart bodyPart : hole.getBodyHole()) {
            g.setColor(bodyPart.getColor());
            g.fillRoundRect(

                    bodyPart.getX() * CELL_SIZE - CELL_SIZE,
                    game.getScreenSize().height - (bodyPart.getY() * CELL_SIZE),
                    CELL_SIZE,
                    CELL_SIZE,8,8
            );
        }
    }
    private void processInput() {
        for (KeyEvent event : game.getInput().getKeyPressedEvents()) {
            hole.processTetris(event);

        }
    }
}
