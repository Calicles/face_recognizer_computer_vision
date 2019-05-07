package com.antoine.ui;

import lombok.extern.slf4j.Slf4j;
import com.antoine.dl.FaceRecognition;
import com.antoine.vue.frame.Frame;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgcodecs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

@Slf4j
public class RecognizeUI {

    private FaceRecognition faceRecognition;

    private Webcam webcam;
    private JFrame window;
    private JLabel label;
    private PanelScan panel;


    public void init() throws Exception {



            faceRecognition = new FaceRecognition();
            faceRecognition.loadModel();


            webcam = Webcam.getDefault();
            webcam.setViewSize(WebcamResolution.VGA.getSize());


            //WebcamPanel panel = new WebcamPanel(webcam);
            panel = new PanelScan(webcam);
            panel.setFPSDisplayed(true);
            panel.setDisplayDebugInfo(true);
            panel.setImageSizeDisplayed(true);
            panel.setMirrored(true);

            try {


                File profil = Paths.get("profil").toFile();
                File[] files = profil.listFiles();
                for (File f : files) {
                    opencv_core.Mat imread = opencv_imgcodecs.imread(f.toURI().getPath());

                    faceRecognition.registerNewMember(f.getName().substring(0, f.getName().indexOf('.')), imread);
                }
            } catch (IOException e) {
                log.error("Error loading profiles", e);
                System.exit(0);
                throw new RuntimeException();
            }

            label = new JLabel("SCANNING");
            label.setBackground(Color.BLACK);
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setForeground(Color.WHITE);
            JPanel labelContainer = new JPanel();
            labelContainer.add(label);
            labelContainer.setBackground(Color.BLACK);
            window = new JFrame("FACIAL RECOGNIZER");
            Container container = window.getContentPane();
            container.setLayout(new BorderLayout());
            container.add(panel, BorderLayout.CENTER);
            container.add(labelContainer, BorderLayout.SOUTH);
            window.setResizable(false);
            //window.setLocationRelativeTo(null);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.pack();
            window.setVisible(true);


    }

    public void start(){
        new Thread(()->{
            boolean isSame = false;

            panel.startScanning();

            while (!isSame)
            {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}

                try {
                    ImageIO.write(webcam.getImage(), "PNG", new File("./scanning.png"));
                } catch (IOException e) {
                    log.error("Error writing image", e);
                    throw new RuntimeException();
                }

                String whoIs = null;
                try {
                    opencv_core.Mat imread = opencv_imgcodecs.imread(Paths.get("scanning.png").toAbsolutePath().toUri().getPath());
                    whoIs = faceRecognition.whoIs(imread);
                } catch (IOException e) {
                    log.error("Error scanning", e);
                    throw new RuntimeException();
                }

                isSame = !whoIs.toLowerCase().contains("unknown");

                if (isSame)
                {
                    panel.stopScanning();
                    label.setText("Bonjour " + whoIs);
                    window.repaint();
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException ignored){}
                }
            }
            webcam.getDevice().close();
            window.dispose();

            JFrame interFram = new JFrame();
            com.antoine.ProgressBar analyzing = new com.antoine.ProgressBar(interFram, true);
            analyzing.showProgressBar("DEVEROUILLAGE");
            Executors.newCachedThreadPool().submit(()->{
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {}
                interFram.dispose();
                try {
                    faceRecognition.serializeModel(Paths.get("save/"));
                } catch (IOException e) {
                    log.error("Error Serialing model", e);
                }
                new Frame();
            });
        })
                .start();
    }
}
