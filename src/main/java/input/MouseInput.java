package input;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseInputAdapter {

    public Point mousePosition = new Point(0, 0);
    public boolean mousePressed = false;

    public MouseInput() {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mousePosition.setLocation(e.getPoint());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePosition.setLocation(e.getPoint());
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePressed = false;
    }
}
