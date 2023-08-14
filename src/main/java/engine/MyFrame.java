package engine;

import ui.UIPanel;

import javax.swing.*;
import java.awt.*;

public class MyFrame {

    public void start() {
        //Create and set up the window.
        JFrame frame = new JFrame("MyFrame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel gamePanel = new GamePanel();
        UIPanel uiPanel = new UIPanel(gamePanel.getGame());
        gamePanel.getGame().setUIPanel(uiPanel);

        frame.setLayout(new GridBagLayout());
        frame.add(gamePanel);
        frame.add(uiPanel);

        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        gamePanel.startGameThread();
    }
}
