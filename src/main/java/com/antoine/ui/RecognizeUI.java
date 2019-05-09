package com.antoine.ui;

import com.antoine.dl.FaceRecognition;
import com.antoine.vue.frame.Frame;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

public class RecognizeUI {

    private static Logger log = LoggerFactory.getLogger(com.antoine.ui.RecognizeUI.class);

    private String absoluteProgrammePath;

    private FaceRecognition faceRecognition;

    private Webcam webcam;
    private JFrame window;
    private JLabel label;
    private PanelScan panel;

    private final String PHOTOSCANNING = "scanning.png";
    private final String PROFILS_FILE = "profil";

    public RecognizeUI(String absoluteProgrammePath)
    {
        this.absoluteProgrammePath = absoluteProgrammePath;
    }

    public void initWindow()
    {
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
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
    }


    public void initWebCam()
    {
        try {
            webcam = Webcam.getDefault();
            webcam.setViewSize(WebcamResolution.VGA.getSize());

            panel = new PanelScan(webcam);
            panel.setFPSDisplayed(true);
            panel.setDisplayDebugInfo(true);
            panel.setImageSizeDisplayed(true);
            panel.setMirrored(true);

        }catch (Throwable t)
        {
            String error = "erruer de detection de la webcam";
            log.debug(error, t);
            showErrorDialog(error, true);
        }
    }


    public void loadModel()
    {
        try
        {
            faceRecognition = new FaceRecognition();
            faceRecognition.loadModel(absoluteProgrammePath);
        }catch (Throwable t)
        {
            String error = "Erreur de chargement du modèle de deep learning";
            log.debug(error, t);
            showErrorDialog(error, true);
        }

        try
        {
            File profil = new File(absoluteProgrammePath, PROFILS_FILE);

            if (!profil.exists())
            {
                profil.mkdir();
                showErrorDialog("Dossier profil inexistant, Création en cours, vous devrez enregistrer un profil avec le PhotoRegister", true);
            }

            File[] files = profil.listFiles();

            if (files.length == 0)
                showErrorDialog("Pas de profil utilisateur enregistré", true);

            for (File f : files)
            {
                opencv_core.Mat imread = opencv_imgcodecs.imread(f.toURI().getPath());

                faceRecognition.registerNewMember(f.getName().substring(0, f.getName().indexOf('.')), imread);
            }
        } catch (IOException e)
        {
            String error = "Error loading profiles in model computaion";
            log.error(error, e);
            showErrorDialog(error, true);
        }
    }


    public static void showErrorDialog(String msg, boolean haveToExitSystem)
    {
        JFrame errorFrame = new JFrame();
        PanelDialog dial = new PanelDialog(true);
        dial.showMessage(msg, errorFrame, true);

        threadSleep(4000);

        errorFrame.dispose();

        if (haveToExitSystem)
            System.exit(0);
    }


    public void startRecognizer()
    {
        new Thread(()->{
            boolean isSame = false;

            panel.startScanning();

            File photoScanning = new File(PHOTOSCANNING);

            while (!isSame)
            {
                threadSleep(2000);

                try {
                    ImageIO.write(webcam.getImage(), "PNG", photoScanning);
                } catch (IOException e) {
                    String error = "Error writing image for scanner";
                    log.error(error, e);
                    showErrorDialog(error, true);
                }

                String whoIs = null;
                try {
                    opencv_core.Mat imread = opencv_imgcodecs.imread(photoScanning.getAbsolutePath());
                    whoIs = faceRecognition.whoIs(imread);
                } catch (IOException e) {
                    String error = "Erreur lors de la reconnaissance";
                    log.error(error, e);
                    showErrorDialog(error, true);
                }

                isSame = !whoIs.toLowerCase().contains("unknown");

                if (isSame)
                {
                    panel.stopScanning();
                    label.setText("Bonjour " + whoIs);
                    window.repaint();

                    threadSleep(4000);
                }
            }
            window.dispose();
            webcam.close();

            photoScanning.delete();

            JFrame interFram = new JFrame();
            ProgressBar analyzing = new ProgressBar(interFram, true);
            analyzing.showProgressBar("DEVEROUILLAGE", Color.GREEN);

            Executors.newCachedThreadPool().submit(()->{

                threadSleep(2000);

                try {
                    faceRecognition.serializeModel(absoluteProgrammePath);
                } catch (IOException e)
                {
                    log.error("Error Serializing model", e);
                    showErrorDialog("Erreur d'enregistrement du modèle de Deep Learning", false);
                }
                new Frame();

                interFram.dispose();
            });


        })
                .start();
    }

    private static void threadSleep(long timeToSleep){
        try{
            Thread.sleep(timeToSleep);
        }catch (InterruptedException ignored){}
    }
}
