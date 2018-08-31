package Tetris;

import java.awt.*;

public abstract class Scene {
    protected final Game game;
    public Scene(Game game) {
        this.game = game;
    }
    public abstract void update(long nanosPassed);
    public abstract void draw(Graphics2D g);
}

