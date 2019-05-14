package com.antoine.ui;

import com.antoine.dl.FaceRecognition;
import com.antoine.photoRegister.Photo;
import com.antoine.vue.frame.Frame;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.bytedeco.javacpp.opencv_core.FONT_HERSHEY_PLAIN;
import static org.bytedeco.javacpp.opencv_imgproc.*;


public class RecognizeUI {

    private static Logger log = LoggerFactory.getLogger(com.antoine.ui.RecognizeUI.class);


    private FaceRecognition faceRecognition;
    private org.bytedeco.javacpp.opencv_face.FaceRecognizer lbphFaceRecognizer;

    private opencv_objdetect.CascadeClassifier face_cascade;
    private opencv_core.Mat mat;
    private CanvasFrame cFrame;
    private OpenCVFrameGrabber grabber;

    private String absoluteProgrammePath;
    private final String PROFILS_FILE = "profil";


    public RecognizeUI(String absoluteProgrammePath)
    {
        this.absoluteProgrammePath = absoluteProgrammePath;
        try {
            face_cascade = new opencv_objdetect.CascadeClassifier(
                    getClass().getResource("/resources/haarcascade_frontalface_default.xml").getPath()
            );
           /* lbphFaceRecognizer = opencv_face.LBPHFaceRecognizer.create();
            lbphFaceRecognizer.read(
                    getClass().getResource("/resources/haarcascade_fraontalface_alt.xml").getPath()
            );*/
        }catch (Exception e) {
            log.info("erreur de cascade");

        }
    }

    public void initWindow()
    {
    }


    public void initWebCam()
    {
        try {

            grabber = new OpenCVFrameGrabber(0);
            cFrame = new CanvasFrame("Capture Preview", CanvasFrame.getDefaultGamma() / grabber.getGamma());

        }catch (Throwable t)
        {
            String error = "webcam non détéctée";
            log.debug(error, t);
            throw new RuntimeException(error);
        }
    }


    public void loadModel() throws Exception {
        faceRecognition = new FaceRecognition();
        faceRecognition.loadModel(absoluteProgrammePath);
        //TinyYoloPrediction.setPROGRAMME_PATH(absoluteProgrammePath);
    }

    public void loadProfils() throws IOException, InterruptedException {

        File profil = new File(absoluteProgrammePath, PROFILS_FILE);

        if (!profil.exists() || profil.list().length == 0)
        {
         /*   profil.mkdir();
            throw new RuntimeException("Dossier profil inexistant, création en cours, Veuillez enregistrer un profil avec le PhotoRegister");*/
         showErrorDialog("Pas de profile enregistré, lancement automatique du programme PhotoRegister", false);
            Lock lock = new ReentrantLock();
         Photo photo = new Photo(absoluteProgrammePath);
         photo.init(lock);

         synchronized (lock){
             lock.wait();
         }

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
                boolean isSame = false;
                String whoIs = "unknown";


                org.bytedeco.javacv.Frame img = null;

                grabber.start();

                cFrame.showImage(grabber.grab());

                while (!isSame && cFrame.isActive()) {
                    //threadSleep(100);

                    img = grabber.grab();

                    mat = new OpenCVFrameConverter.ToMat().convert(img);

                    try {

                        drawRect(mat);

                        whoIs = faceRecognition.whoIs(mat);
                        if (false)
                            throw new Exception();
                    } catch (IOException e) {
                        String error = "Erreur lors de la reconnaissance";
                        log.error(error, e);
                        showErrorDialog(error, true);
                    }

                    isSame = !whoIs.toLowerCase().contains("unknown");

                    if (isSame) {
                        threadSleep(4000);
                    }
                }

                grabber.stop();
                if (cFrame.isVisible())
                    cFrame.dispose();

                if (!isSame) {
                    cFrame.dispose();
                    showErrorDialog("Detection interrompue", true);
                }

                JFrame interFram = new JFrame();
                ProgressBar analyzing = new ProgressBar(interFram, true);
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

    private void drawRect(opencv_core.Mat imread){
            opencv_core.Mat videoMatGray = new opencv_core.Mat();
            //Convert the current frame to grayscale:
            cvtColor(imread, videoMatGray, COLOR_BGRA2GRAY);
            equalizeHist(videoMatGray, videoMatGray);

            // Find the faces in the frame:*/
            opencv_core.RectVector faces = new opencv_core.RectVector();
            face_cascade.detectMultiScale(videoMatGray, faces);

            // At this point you have the position of the faces in
            // faces. Now we'll get the faces, make a prediction and
            // annotate it in the video. Cool or what?
            for (int i = 0; i < faces.size(); i++) {
                opencv_core.Rect face_i = faces.get(i);

                //opencv_core.Mat face = new opencv_core.Mat(videoMatGray, face_i);
                // If fisher face recognizer is used, the face need to be
                // resized.
                //resize(face, face_resized, new opencv_core.Size(im_width, im_height),
                // 1.0, 1.0, INTER_CUBIC);

                // Now perform the prediction, see how easy that is:
                IntPointer label = new IntPointer(1);
                DoublePointer confidence = new DoublePointer(1);
                //lbphFaceRecognizer.predict(face, label, confidence);
                int prediction = label.get(0);

                // And finally write all we've found out to the original image!
                // First of all draw a green rectangle around the detected face:
                rectangle(imread, face_i, new opencv_core.Scalar(0, 255, 0, 1));

                // Create the text we will annotate the box with:
                String box_text = "Prediction = " + prediction;
                // Calculate the position for annotated text (make sure we don't
                // put illegal values in there):
                int pos_x = Math.max(face_i.tl().x() - 10, 0);
                int pos_y = Math.max(face_i.tl().y() - 10, 0);
                // And now put it into the image:
                //putText(videoMat, box_text, new Point(pos_x, pos_y),
                  //      FONT_HERSHEY_PLAIN, 1.0, new opencv_core.Scalar(0, 255, 0, 2.0));
            }
            cFrame.showImage(new OpenCVFrameConverter.ToMat().convert(imread));
    }

    private static void threadSleep(long timeToSleep){
        try{
            Thread.sleep(timeToSleep);
        }catch (InterruptedException ignored){}
    }
}
