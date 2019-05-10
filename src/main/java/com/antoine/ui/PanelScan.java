package com.antoine.ui;

import com.github.sarxos.webcam.Webcam;

import java.awt.*;

public class PanelScan extends WebcamPanelDecorated {

    private final int SPEED = 3;
    private int y;
    private boolean movingUp;

    public PanelScan(Webcam webcam) {
        super(webcam, false);
        y = 0;
        movingUp = false;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if (scanning) {
            Graphics2D graphics2D = (Graphics2D) g;

            Color old = g.getColor();

            Color newColor = new Color(1f, 1f, 1f, .4f);

            g.setColor(newColor);

            g.fillRect(0, y, this.getWidth(), 20);

            if (movingUp) {
                y -= SPEED;
            } else {
                y += SPEED;
            }

            if (y <= 0) {
                movingUp = false;
            } else if (y + 20 >= this.getHeight()) {
                movingUp = true;
            }

            g.setColor(old);
        }
    }


}
