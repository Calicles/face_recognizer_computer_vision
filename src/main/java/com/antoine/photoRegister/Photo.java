package com.antoine.photoRegister;

import com.antoine.ui.RecognizeUI;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.*;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.locks.Lock;

import static org.bytedeco.javacpp.opencv_core.FONT_HERSHEY_PLAIN;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class Photo {

    private static Logger log = LoggerFactory.getLogger(com.antoine.photoRegister.Photo.class);

    private CanvasFrame canvasFrame;
    private OpenCVFrameGrabber grabber;
    private Mat face;
    private opencv_objdetect.CascadeClassifier face_cascade;
    private JTextField userName;
    private JLabel info;
    private Thread thread;

    private String absoluteProgrammePath;

    public Photo(String absoluteProgrammePath)
    {
        this.absoluteProgrammePath = absoluteProgrammePath;
    }

    public void init(Lock lock) {
        face_cascade = new opencv_objdetect.CascadeClassifier(
                getClass().getResource("/resources/haarcascade_frontalface_default.xml").getPath()
        );

        try {
            grabber = new OpenCVFrameGrabber(0);
            canvasFrame = new CanvasFrame("Detection", CanvasFrame.getDefaultGamma() / grabber.getGamma());
            grabber.start();
        userName = new JTextField();
        JFrame frame = new JFrame("PhotoRegister");


        JButton enregistrer = new JButton("enregistrer");
        enregistrer.addActionListener((event)-> {
            try {
                registerPhoto(lock);
            } catch (FrameGrabber.Exception e) {
                throw new RuntimeException(e);
            }
        });

        info = new JLabel("Entrez votre nom et placez votre visage dans le guide");
        info.setBackground(Color.BLACK);
        info.setForeground(Color.WHITE);

        JPanel panelBas = new JPanel();

        panelBas.setLayout(new BorderLayout());
        panelBas.add(userName, BorderLayout.NORTH);
        panelBas.add(enregistrer, BorderLayout.CENTER);
        panelBas.add(info, BorderLayout.SOUTH);
        panelBas.setBackground(Color.BLACK);

        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());
        container.add(panelBas, BorderLayout.SOUTH);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        initThread();
 }catch (Throwable t)
        {
            RecognizeUI.showErrorDialog(t.toString(), true);
        }
    }

    private void initThread() {
        thread = new Thread(
                ()->{
                    Frame frame = null;
                    try {

                       frame = grabber.grab();
                       canvasFrame.showImage(frame);
                    } catch (Throwable t) {
                        RecognizeUI.showErrorDialog(t.toString(), true);
                    }

                    canvasFrame.pack();
                    canvasFrame.setVisible(true);
                    org.bytedeco.javacv.OpenCVFrameConverter converter = new org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage();
                    while (canvasFrame.isVisible())
                    {
                        try {
                            frame = grabber.grab();
                            Mat img = converter.convertToMat(frame);

                            face = detectFace(img);

                            canvasFrame.showImage(converter.convert(img));

                        } catch (FrameGrabber.Exception e) {
                            RecognizeUI.showErrorDialog(e.toString(), true);
                        }
                    }
                }
        );
        thread.start();
    }

    private void registerPhoto(Lock lock) throws FrameGrabber.Exception {
        if (!userName.getText().isEmpty() && face != null)
        {
            Mat buff = face.clone();
            try {

                File profilFile = new File(absoluteProgrammePath,"profil");

                if (!profilFile.exists()) {
                    log.info("création du dossier 'profil'");
                    profilFile.mkdir();
                }

                org.bytedeco.javacpp.opencv_imgcodecs.imwrite(Paths.get(absoluteProgrammePath, userName+"png").toFile().getAbsolutePath(), buff);

            } catch (Throwable e) {
                log.info("erreur lors de la créationd de la photo", e);
                info.setText("Erreur");
            }

            info.setText("Photo enregistré");

            canvasFrame.repaint();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) { }
            grabber.close();
            canvasFrame.dispose();
            synchronized (lock){
                lock.notify();
            }
        }else {

            info.setText("Vous devez rentrez un nom");
            canvasFrame.repaint();
        }
    }

    private Mat detectFace(Mat imread)
    {
        Mat videoMatGray = new Mat();
        Mat face = null;
        //Convert the current frame to grayscale:
        cvtColor(imread, videoMatGray, COLOR_BGRA2GRAY);
        equalizeHist(videoMatGray, videoMatGray);

        // Find the faces in the frame:*/
        opencv_core.RectVector faces = new opencv_core.RectVector();
        face_cascade.detectMultiScale(videoMatGray, faces);

        // At this point you have the position of the faces in
        // faces. Now we'll get the faces, make a prediction and
        // annotate it in the video. Cool or what?
        //for (int i = 0; i < faces.size(); i++) {
        opencv_core.Rect face_i = null;

        if (!faces.empty())
        {
            face_i = faces.get(0);

            face = new opencv_core.Mat(videoMatGray, face_i);
            // If fisher face recognizer is used, the face need to be
            // resized.
            //resize(face, 100, new opencv_core.Size(200, 200),
            //1.0, 1.0, INTER_CUBIC);

            // Now perform the prediction, see how easy that is:
            //IntPointer label = new IntPointer(1);
            //DoublePointer confidence = new DoublePointer(1);
            //lbphFaceRecognizer.predict(face, label, confidence);
            //int prediction = label.get(0);

            // And finally write all we've found out to the original image!
            // First of all draw a green rectangle around the detected face:
            rectangle(imread, face_i, new opencv_core.Scalar(0, 255, 0, 1));

            // Calculate the position for annotated text (make sure we don't
            // put illegal values in there):
            int pos_x = Math.max(face_i.tl().x() - 10, 0);
            int pos_y = Math.max(face_i.tl().y() - 10, 0);
            // And now put it into the image:
            putText(imread, "Detecting", new opencv_core.Point(pos_x, pos_y),
                    FONT_HERSHEY_PLAIN, 1.0, new opencv_core.Scalar(0, 255, 0, 2.0));
        }
        return face;
    }

}
