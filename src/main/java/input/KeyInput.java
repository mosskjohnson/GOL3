package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class KeyInput implements KeyListener {

    private HashMap<Integer, Boolean> keysPressed = new HashMap<>();

    public KeyInput() {
        for (int i = 32; i <= 90; i++) {
            keysPressed.put(i, false);
        }
    }

    public HashMap<Integer, Boolean> getKeysPressed() {
        return keysPressed;
    }

    public boolean isKeyPressed(int keyCode) {
        return keysPressed.get(keyCode);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keysPressed.put(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed.put(e.getKeyCode(), false);
    }
}
