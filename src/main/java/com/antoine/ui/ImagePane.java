package com.antoine.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePane extends JPanel {

    private BufferedImage img;

    public ImagePane(Color backgroundCOlor){
        super();
        super.setBackground(backgroundCOlor);
    }

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
