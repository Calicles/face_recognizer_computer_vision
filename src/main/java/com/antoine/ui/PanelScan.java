package com.antoine.ui;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import java.awt.*;

public class PanelScan extends WebcamPanel {

    private final int SPEED = 3;
    private int y;
    private boolean movingUp, scanning;

    public PanelScan(Webcam webcam) {
        super(webcam);
        y = 0;
        movingUp = false;
        scanning = false;
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

            g.setColor(Color.BLACK);

            g.drawOval(this.getWidth() / 4, this.getHeight() / 10, this.getWidth() / 2, this.getHeight() - (this.getHeight() / 6));


            g.setColor(old);
        }
    }

    public void startScanning(){
        scanning = true;
    }

    public void stopScanning(){
        scanning = false;
    }
}
