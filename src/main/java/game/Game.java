package game;

import engine.GamePanel;
import ui.UIPanel;
import util.Camera;
import util.PairInt;
import util.UtilMath;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class Game implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private GamePanel gamePanel;
    private UIPanel uiPanel;
    private Board board;
    private Camera camera;

    private final int CELL_SIZE;

    private int screenWidth;
    private int screenHeight;

    private int FPS;
    private int stepRate;
    private int framesBetweenSteps;
    private int framesSinceLastStep;
    public final int INITIAL_STEP_RATE;
    public final int MIN_STEP_RATE;
    public final int MAX_STEP_RATE;

    public boolean paused = true;

    private Point mousePosition = new Point(0, 0);

    private int startPanX;
    private int startPanY;

    private Point startBlueprintPosition = null;
    public Blueprint activeBlueprint;

    private final double INITIAL_ZOOM;
    private final double MIN_ZOOM;
    private final double MAX_ZOOM;
    private final double ZOOM_IN_FACTOR = 1.3;
    private final double ZOOM_OUT_FACTOR = 1 / ZOOM_IN_FACTOR;

    public boolean drawGridLines = true;

    public Color bgColor = Color.BLACK;
    public Color cellColor = Color.YELLOW;
    public Color gridLinesColor = Color.DARK_GRAY;

    private int generationsSinceClear = 0;

    public Game(GamePanel gamePanel, int screenWidth, int screenHeight, int cellSize, int FPS, int initialStepRate, int maxStepRate) {
        this.gamePanel = gamePanel;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.CELL_SIZE = cellSize;

        this.FPS = FPS;
        this.stepRate = initialStepRate;
        this.framesBetweenSteps = FPS/initialStepRate;
        this.framesSinceLastStep = FPS;
        this.INITIAL_STEP_RATE = initialStepRate;
        this.MIN_STEP_RATE = 1;
        this.MAX_STEP_RATE = maxStepRate;

        this.INITIAL_ZOOM = 1.0 * CELL_SIZE;
        this.MIN_ZOOM = 1.0;
        this.MAX_ZOOM = 40 * CELL_SIZE;

        this.camera = new Camera(-screenWidth / 2.0, -screenWidth / 2.0, INITIAL_ZOOM, MIN_ZOOM, MAX_ZOOM);

        this.board = new Board();
    }

    public void setUIPanel(UIPanel uiPanel) {
        this.uiPanel = uiPanel;
    }

    public void setActiveBlueprint(Blueprint newBlueprint) {
        this.activeBlueprint = newBlueprint;
    }

    public void step() {
        board.step();
        generationsSinceClear++;
        uiPanel.setLabelGenerations(generationsSinceClear);
        framesSinceLastStep = 0;
    }

    public void setStepRate(int newStepRate) {
        stepRate = UtilMath.clamp(newStepRate, 1, MAX_STEP_RATE);
        framesBetweenSteps = FPS/stepRate;
    }

    public void clearAliveCells() {
        paused = true;
        board.clearAliveCells();
        generationsSinceClear = 0;
        uiPanel.setLabelGenerations(generationsSinceClear);
    }

    public void update() {
        if (framesSinceLastStep++ < framesBetweenSteps) {
            return;
        }
        if (paused) {
            return;
        }

        step();
    }

    public void draw(Graphics2D g2) {
        // bg
        g2.setColor(bgColor);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        // blueprint rect
        if (startBlueprintPosition != null) {
            g2.setColor(gridLinesColor);
            int screenXStart = camera.worldToScreenX(camera.mouseToWorldX(startBlueprintPosition.x));
            int screenYStart = camera.worldToScreenY(camera.mouseToWorldY(startBlueprintPosition.y));
            int screenXEnd = camera.worldToScreenX(camera.mouseToWorldX(mousePosition.x) + 1);
            int screenYEnd = camera.worldToScreenY(camera.mouseToWorldY(mousePosition.y) + 1);
            g2.fillRect(screenXStart, screenYStart, screenXEnd - screenXStart, screenYEnd - screenYStart);
        }

        // clip
        double[] bounds = camera.getBounds(screenWidth, screenHeight);
        double left = bounds[0];
        double top = bounds[1];
        double right = bounds[2];
        double bottom = bounds[3];

        // cells
        g2.setColor(cellColor);
        for (PairInt p : board.getAliveCells()) {
            if (p.x < left || p.x > right || p.y < top || p.y > bottom) {
                continue;
            }
            int sx = camera.worldToScreenX(p.x);
            int sy = camera.worldToScreenY(p.y);
            int ex = camera.worldToScreenX(p.x + 1);
            int ey = camera.worldToScreenY(p.y + 1);

            g2.fillRect(sx, sy, ex - sx, ey - sy);
        }

        // grid lines
        if (drawGridLines) {
            g2.setColor(gridLinesColor);
            for (int x = (int) left; x < right; x+=1) {
                int screenX = camera.worldToScreenX(x);
                g2.drawLine(screenX, 0, screenX, screenHeight);
            }
            for (int y = (int) top; y < bottom; y+=1) {
                int screenY = camera.worldToScreenY(y);
                g2.drawLine(0, screenY, screenWidth, screenY);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                paused = !paused;
                System.out.println("paused: " + paused);
                break;
            case KeyEvent.VK_C:
                paused = true;
                clearAliveCells();
                break;
            case KeyEvent.VK_RIGHT:
                if (paused) {
                    step();
                }
                break;
            case KeyEvent.VK_UP:
                setStepRate(stepRate + 1);
                uiPanel.setSliderStepRate(stepRate);
                break;
            case KeyEvent.VK_DOWN:
                setStepRate(stepRate - 1);
                uiPanel.setSliderStepRate(stepRate);
                break;
            case KeyEvent.VK_SHIFT:
                paused = true;
                if (startBlueprintPosition == null) {
                    startBlueprintPosition = new Point(mousePosition.x, mousePosition.y);
                }
                break;
            case KeyEvent.VK_B:
                paused = true;
                int cellX = camera.mouseToWorldX(mousePosition.x);
                int cellY = camera.mouseToWorldY(mousePosition.y);
                board.applyBlueprint(activeBlueprint, cellX, cellY);
                break;
            default:
                System.out.println("Key " + e.getKeyCode() + ": '" + e.getKeyChar() + "'" + " does not have command associated with it");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SHIFT:
                paused = true;

                int cellXStart = camera.mouseToWorldX(startBlueprintPosition.x);
                int cellYStart = camera.mouseToWorldY(startBlueprintPosition.y);
                int cellXEnd = camera.mouseToWorldX(mousePosition.x);
                int cellYEnd = camera.mouseToWorldY(mousePosition.y);

                Blueprint b = board.createBlueprint(cellXStart, cellYStart, cellXEnd - cellXStart + 1, cellYEnd - cellYStart + 1);
                String name = JOptionPane.showInputDialog(gamePanel, "Filename (excluding .txt)", null);
                if (name != null) {
                    String filename = name + ".txt";
                    try {
                        TxtParser.bluePrintToTxt(b, filename);
                        uiPanel.createBlueprintRadioButtons();
                    } catch (IOException | URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
                startBlueprintPosition = null;
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            paused = true;
            int cellX = camera.mouseToWorldX(e.getX());
            int cellY = camera.mouseToWorldY(e.getY());
            board.toggleAliveCell(cellX, cellY);
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            startPanX = e.getX();
            startPanY = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            camera.offsetX += (startPanX - e.getX()) / camera.zoomX;
            camera.offsetY += (startPanY - e.getY()) / camera.zoomY;

            startPanX = e.getX();
            startPanY = e.getY();
        }
        mousePosition = e.getPoint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
//        System.out.println("mm");
        mousePosition = e.getPoint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        double mouseBeforeX = camera.screenToWorldX(e.getX());
        double mouseBeforeY = camera.screenToWorldY(e.getY());

        if (e.getWheelRotation() < 0) {
            camera.zoom(ZOOM_IN_FACTOR);
        }
        if (e.getWheelRotation() > 0) {
            camera.zoom(ZOOM_OUT_FACTOR);
        }

        double mouseAfterX = camera.screenToWorldX(e.getX());
        double mouseAfterY = camera.screenToWorldY(e.getY());

        camera.offsetX += (mouseBeforeX - mouseAfterX);
        camera.offsetY += (mouseBeforeY - mouseAfterY);

        mousePosition = e.getPoint();
    }
}
