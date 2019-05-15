package com.antoine.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class WebcamPane extends JPanel {

    private BufferedImage img;

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.drawImage(img, 0, 0, null);
    }

    public void updateImage(BufferedImage img){
        this.img = img;
        super.repaint();
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(img.getWidth(), img.getHeight());
    }
}
