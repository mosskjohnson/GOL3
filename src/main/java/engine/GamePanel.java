package engine;

import game.Game;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    public final int INITIAL_SCREEN_WIDTH = 1100;
    public final int INITIAL_SCREEN_HEIGHT = 800;

    public final int CELL_SIZE = 10;

    // FPS
    public final int FPS = 60;
    public final int INITIAL_STEP_RATE = 5;
    public final int MAX_STEP_RATE = 60;

    private Thread gameThread;

    private Game game = new Game(this, INITIAL_SCREEN_WIDTH, INITIAL_SCREEN_HEIGHT, CELL_SIZE, FPS, INITIAL_STEP_RATE, MAX_STEP_RATE);

    public GamePanel() {
//        this.setPreferredSize(new Dimension(INITIAL_SCREEN_WIDTH, INITIAL_SCREEN_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.addKeyListener(game);
        this.addMouseListener(game);
        this.addMouseMotionListener(game);
        this.addMouseWheelListener(game);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        game.update();
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        game.draw(g2);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(INITIAL_SCREEN_WIDTH, INITIAL_SCREEN_HEIGHT);
    }
}