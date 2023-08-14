package ui;

import util.Builder;

import javax.swing.*;
import java.awt.event.ActionListener;

public class JButtonBuilder implements Builder<JButton> {

    private JButton button = null;

    public JButtonBuilder() {
        reset();
    }

    public JButtonBuilder setText(String text) {
        button.setText(text);
        return this;
    }

    public JButtonBuilder setIcon(Icon defaultIcon) {
        button.setIcon(defaultIcon);
        return this;
    }

    public JButtonBuilder setFocusable(boolean focusable) {
        button.setFocusable(focusable);
        return this;
    }

    public JButtonBuilder addActionListener(ActionListener l) {
        button.addActionListener(l);
        return this;
    }

    @Override
    public JButtonBuilder reset() {
        this.button = new JButton();
        return this;
    }

    @Override
    public JButton build() {
        JButton result = button;
        reset();
        return result;
    }
}
