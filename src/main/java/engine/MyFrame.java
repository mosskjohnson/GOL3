package engine;

import ui.UIPanel;

import javax.swing.*;
import java.awt.*;

public class MyFrame {

    public void start(String title) {
        JFrame frame = new JFrame(title);
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
