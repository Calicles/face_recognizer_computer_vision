package com.antoine.ui;

import com.github.sarxos.webcam.Webcam;

import java.awt.*;
import java.util.ArrayList;

public class PanelScan extends WebcamPanelDecorated {

    private final int SPEED = 3;
    private int y;
    private boolean movingUp;
    private ArrayList<Rectangle> boxes;

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

            if(boxes != null){
                for(Rectangle box: boxes) {
                    g.setColor(Color.GREEN);
                    g.drawRect((int) box.getX(), (int) box.getY(), (int) box.getWidth(), (int) box.getHeight());
                    g.setColor(Color.YELLOW);
                    g.drawString("Detecting", (int) box.getX(), (int) box.getHeight());
                }
            }

            g.setColor(old);
        }
    }


    public void setBox(ArrayList<Rectangle> boxes) {
        this.boxes = boxes;
    }
}
