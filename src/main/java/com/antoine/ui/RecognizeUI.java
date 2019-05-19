package com.antoine.ui;

import com.antoine.dl.FaceRecognition;
import com.antoine.photoRegister.Photo;
import com.antoine.vue.frame.Frame;
import org.bytedeco.javacpp.*;

import org.bytedeco.javacv.FrameFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class RecognizeUI {

    private static Logger log = LoggerFactory.getLogger(com.antoine.ui.RecognizeUI.class);

    private final String PROFILS_FILE = "profil";

    private FaceRecognition faceRecognition;

    private Webcam webcam;
    private JLabel info;

    private String absoluteProgrammePath;


    public RecognizeUI(String absoluteProgrammePath)
    {
        this.absoluteProgrammePath = absoluteProgrammePath;
    }

    public void initWebCam() throws Exception {
        info = new JLabel("SCANNING");
        info.setForeground(Color.WHITE);
        info.setBackground(Color.BLACK);
        info.setHorizontalTextPosition(JLabel.CENTER);
        JPanel buff = new JPanel();
        buff.add(info);
        buff.setBackground(Color.BLACK);

        webcam = Webcam.builder()
                .init("Face Recognizer")
                .setResourcesDirectoryPath(Paths.get(absoluteProgrammePath, "resources").toFile().getAbsolutePath())
                .setLayout(new BorderLayout())
                .initGrabber()
                .setDefaultClassifier()
                .setOnlyOneFaceCareTaker(false)
                .setFaceRecognizer(faceRecognition)
                .addComponent(buff, BorderLayout.SOUTH)
                .packAndShow()
                .build();
    }


    public void loadModel() throws Exception {
        double threshold = readConfig();
        faceRecognition = new FaceRecognition(threshold);
        faceRecognition.loadModel(absoluteProgrammePath);
    }

    private double readConfig() {
        double threshold = 0;
        try(BufferedReader reader = new BufferedReader(new FileReader(Paths.get(absoluteProgrammePath, "config", "config_cam", "conf.txt").toFile())))
        {
            threshold = Double.parseDouble(reader.readLine());
        } catch (Exception e) {
            log.info("", e);
            throw new RuntimeException(e);
        }
        return threshold;
    }

    public void loadProfils(ProgressBar progressBar) throws IOException, InterruptedException {

        File profil = new File(absoluteProgrammePath, PROFILS_FILE);

        if (!profil.exists() || profil.list().length == 0)
        {
            progressBar.setVisible(false);
            showErrorDialog("Pas de profile enregistré, lancement automatique du programme PhotoRegister", false);
            Lock lock = new ReentrantLock();
            Photo photo = new Photo(absoluteProgrammePath);
            photo.init(lock);

            synchronized (lock){
                 lock.wait();
            }

            progressBar.setVisible(true);
            log.info("reprise du programme");
        }

        File[] files = profil.listFiles();

        for (File f : files)
        {
            opencv_core.Mat imread = opencv_imgcodecs.imread(f.toURI().getPath());

            faceRecognition.registerNewMember(f.getName().substring(0, f.getName().indexOf('.')), imread);
        }
    }


    public static void showErrorDialog(String msg, boolean haveToExitSystem)
    {
        JFrame errorFrame = new JFrame();
        PanelDialog dial = new PanelDialog(true);
        dial.showMessage(msg, errorFrame, true);

        threadSleep(6000);

        errorFrame.dispose();

        if (haveToExitSystem)
            System.exit(0);
    }


    public void startRecognizer()
    {
        new Thread(()->{
            try {


                while (webcam.isRecognizingFace())
                {
                    webcam.grab();

                    webcam.detectFace();

                    webcam.showImage();
                }

                String[] userNames = webcam.getRecognizedNames();
                String res = Arrays.stream(userNames).filter((s -> !s.toLowerCase().contains("unknown")))
                        .collect(Collectors.joining(" et "));

                info.setText("Bonjour " + res);
                webcam.showImage();

                threadSleep(3000);

                webcam.close();
                webcam.dispose();

                JFrame interFram = new JFrame();
                ProgressBar analyzing = new ProgressBar(interFram, true, true);
                analyzing.showProgressBar("DEVEROUILLAGE", Color.GREEN);

                Executors.newCachedThreadPool().submit(() -> {

                    threadSleep(2000);

                    try {
                        faceRecognition.serializeModel(absoluteProgrammePath);
                    } catch (IOException e) {
                        interFram.setVisible(false);
                        log.error("Error Serializing model", e);
                        showErrorDialog("Erreur d'enregistrement du modèle de Deep Learning", false);
                    }finally {
                        new Frame();
                        interFram.dispose();
                    }
                });

            }catch (Throwable throwable)
            {
                log.info("", throwable);
                showErrorDialog("erreur: " + throwable.getMessage(), true);
            }


        })
                .start();
    }

    private static void threadSleep(long timeToSleep){
        try{
            Thread.sleep(timeToSleep);
        }catch (InterruptedException ignored){}
    }
}
