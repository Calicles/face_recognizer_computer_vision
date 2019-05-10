package com.antoine.ui;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import java.awt.*;

public class WebcamPanelDecorated extends WebcamPanel {

    protected boolean scanning;

    public WebcamPanelDecorated(Webcam webcam){
        super(webcam);
        scanning = true;
    }

    public WebcamPanelDecorated(Webcam webcam, boolean scanning){
        super(webcam);
        this.scanning = scanning;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        if (scanning) {
            Color old = g.getColor();

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
