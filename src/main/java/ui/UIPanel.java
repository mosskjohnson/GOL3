package ui;

import game.Game;
import game.TxtParser;
import game.UnexpectedCharException;
import util.UtilFile;
import util.UtilImg;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

public class UIPanel extends JPanel implements ActionListener, ChangeListener {

    private final int INITIAL_WIDTH = 300;
    private final int INITIAL_HEIGHT = 800;
    
    private Game game;

    private JTabbedPane tabbedPane;

    private JComponent panelTab1;
    private JPanel panelPausePlayStep;
    private JButton buttonPause;
    private JButton buttonPlay;
    private JButton buttonStep;
    private JLabel labelStepRate;
    private JSlider sliderStepRate;
    private JLabel labelGenerations;
    private JButton buttonClearBoard;
    private JCheckBox checkBoxGridLines;

    private JComponent panelTab2;
    private ButtonGroup buttonGroupBlueprints;

    private JComponent panelTab3;
    private JButton buttonBGColor;
    private JButton buttonCellColor;
    private JButton buttonGridLinesColor;

    public UIPanel(Game game) {
        this.game = game;

        this.tabbedPane = new JTabbedPane();
        tabbedPane.setFocusable(false);

        // tab 1
        this.panelTab1 = new JPanel();
        panelTab1.setLayout(new BoxLayout(panelTab1, BoxLayout.Y_AXIS));
        panelTab1.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        this.panelPausePlayStep = new JPanel();
        panelPausePlayStep.setLayout(new BoxLayout(panelPausePlayStep, BoxLayout.X_AXIS));
        panelPausePlayStep.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.buttonPlay = new JButtonBuilder()
                .setIcon(new ImageIcon(UtilImg.resize(UtilImg.loadImage("play.png"), 20, 20)))
                .setFocusable(false)
                .addActionListener(this)
                .build();
        this.buttonPause = new JButtonBuilder()
                .setIcon(new ImageIcon(UtilImg.resize(UtilImg.loadImage("pause.png"), 20, 20)))
                .setFocusable(false)
                .addActionListener(this)
                .build();
        this.buttonStep = new JButtonBuilder()
                .setIcon(new ImageIcon(UtilImg.resize(UtilImg.loadImage("step.png"), 20, 20)))
                .setFocusable(false)
                .addActionListener(this)
                .build();

        panelPausePlayStep.add(buttonPlay);
        panelPausePlayStep.add(buttonPause);
        panelPausePlayStep.add(buttonStep);

        this.labelStepRate = new JLabel("speed");
        this.sliderStepRate = new JSlider(game.MIN_STEP_RATE, game.MAX_STEP_RATE, game.INITIAL_STEP_RATE);
        sliderStepRate.setPaintTicks(true);
        sliderStepRate.setMinorTickSpacing(5);
        sliderStepRate.setMajorTickSpacing(10);
        sliderStepRate.setPaintTrack(true);
        sliderStepRate.setFocusable(false);
        sliderStepRate.addChangeListener(this);
        this.checkBoxGridLines = new JCheckBox("draw grid lines", true);
        checkBoxGridLines.setFocusable(false);
        checkBoxGridLines.addActionListener(this);
        this.labelGenerations = new JLabel("gen: " + 0);
        this.buttonClearBoard = new JButtonBuilder()
                .setText("Clear")
                .setFocusable(false)
                .addActionListener(this)
                .build();

        panelTab1.add(panelPausePlayStep);
        panelTab1.add(Box.createRigidArea(new Dimension(0, 30)));
        panelTab1.add(labelStepRate);
        panelTab1.add(Box.createRigidArea(new Dimension(0, 10)));
        panelTab1.add(sliderStepRate);
        panelTab1.add(Box.createRigidArea(new Dimension(0, 30)));
        panelTab1.add(checkBoxGridLines);
        panelTab1.add(Box.createRigidArea(new Dimension(0, 30)));
        panelTab1.add(labelGenerations);
        panelTab1.add(Box.createRigidArea(new Dimension(0, 30)));
        panelTab1.add(buttonClearBoard);

        // tab 2
        this.panelTab2 = new JPanel();
        panelTab2.setLayout(new BoxLayout(panelTab2, BoxLayout.Y_AXIS));
        panelTab2.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        buttonGroupBlueprints = new ButtonGroup();
        createBlueprintRadioButtons(panelTab2, buttonGroupBlueprints);


        // tab 3
        this.panelTab3 = new JPanel();
        panelTab3.setLayout(new BoxLayout(panelTab3, BoxLayout.Y_AXIS));
        panelTab3.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        this.buttonBGColor = new JButtonBuilder()
                .setText("BG color")
                .setFocusable(false)
                .addActionListener(this)
                .build();
        this.buttonCellColor = new JButtonBuilder()
                .setText("Cell color")
                .setFocusable(false)
                .addActionListener(this)
                .build();
        this.buttonGridLinesColor = new JButtonBuilder()
                .setText("Grid lines color")
                .setFocusable(false)
                .addActionListener(this)
                .build();

        panelTab3.add(buttonBGColor);
        panelTab3.add(Box.createRigidArea(new Dimension(0, 30)));
        panelTab3.add(buttonCellColor);
        panelTab3.add(Box.createRigidArea(new Dimension(0, 30)));
        panelTab3.add(buttonGridLinesColor);

        //
        tabbedPane.add("Controls", panelTab1);
        tabbedPane.add("Blueprints", panelTab2);
        tabbedPane.add("Colors", panelTab3);
        this.add(tabbedPane);
    }

    public void setLabelGenerations(int generationsSinceClear) {
        labelGenerations.setText("gen: " + generationsSinceClear);
    }

    public void setSliderStepRate(int value) {
        sliderStepRate.setValue(value);
    }

    public void createBlueprintRadioButtons() {
        clearButtonGroup(panelTab2, buttonGroupBlueprints);
        buttonGroupBlueprints = new ButtonGroup();
        createBlueprintRadioButtons(panelTab2, buttonGroupBlueprints);
        panelTab2.revalidate();
        panelTab2.repaint();
    }

    private void createBlueprintRadioButtons(JComponent target, ButtonGroup group) {
        boolean firstButton = true;
        for (File file : UtilFile.getUserDataFiles()) {
            String filename = file.getName();
            JRadioButton rButton = new JRadioButton(filename);
            rButton.setFocusable(false);
            rButton.addActionListener(actionEvent -> {
                try {
                    game.setActiveBlueprint(TxtParser.blueprintFromTxt(filename));
                } catch (IOException | UnexpectedCharException e) {
                    e.printStackTrace();
                }
            });
            group.add(rButton);
            target.add(rButton);

            if (firstButton) {
                group.setSelected(rButton.getModel(), true);
                try {
                    game.setActiveBlueprint(TxtParser.blueprintFromTxt(filename));
                } catch (IOException | UnexpectedCharException e) {
                    e.printStackTrace();
                }
                firstButton = false;
            }
        }
    }

    private void clearButtonGroup(JComponent target, ButtonGroup group) {
        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            target.remove(button);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(INITIAL_WIDTH, INITIAL_HEIGHT);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonPlay) {
            game.paused = false;
        } else if (e.getSource() == buttonPause) {
            game.paused = true;
        } else if (e.getSource() == buttonStep) {
            if (game.paused) {
                game.step();
            }
        } else if (e.getSource() == checkBoxGridLines) {
            game.drawGridLines = checkBoxGridLines.isSelected();
        } else if (e.getSource() == buttonClearBoard) {
            game.clearAliveCells();
        } else if (e.getSource() == buttonBGColor) {
            game.bgColor = JColorChooser.showDialog(null, "BG color", game.bgColor);
        } else if (e.getSource() == buttonCellColor) {
            game.cellColor = JColorChooser.showDialog(null, "cell color", game.cellColor);
        } else if (e.getSource() == buttonGridLinesColor) {
            game.gridLinesColor = JColorChooser.showDialog(null, "grid lines color", game.gridLinesColor);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == sliderStepRate) {
            game.setStepRate(sliderStepRate.getValue());
        }
    }
}
