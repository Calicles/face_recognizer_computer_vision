package com.antoine.ui;

import javax.swing.*;
import java.awt.*;

public class PanelDialog extends JPanel {


    public PanelDialog(boolean undecorated){
        super();
    }

    public void showMessage(String msg, JFrame mainfram, boolean undecorated){
        initPanel(new JLabel(msg));
        mainfram.getContentPane().add(this);

        if (undecorated)
            mainfram.setUndecorated(true);

        mainfram.pack();
        mainfram.setLocationRelativeTo(null);
        mainfram.setVisible(true);
        mainfram.repaint();
    }

    private void initPanel(JLabel label) {
        this.setBackground(Color.BLACK);
        label.setForeground(Color.RED);

        this.add(label);
        this.setBorder(BorderFactory.createRaisedBevelBorder());
    }
}
