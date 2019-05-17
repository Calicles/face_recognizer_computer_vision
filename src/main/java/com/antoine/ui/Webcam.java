package com.antoine.ui;

import com.antoine.contracts.IFaceRecognizer;
import org.apache.commons.io.FileUtils;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.FrameGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.zip.Adler32;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class Webcam {

    private Logger log = LoggerFactory.getLogger(getClass());

    private JFrame frame;
    private ImagePane webcamPane;
    private org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier classifier;
    private org.bytedeco.javacv.OpenCVFrameGrabber grabber;
    private org.bytedeco.javacv.OpenCVFrameConverter converter;
    private org.bytedeco.javacv.Java2DFrameConverter java2DFrameConverter;
    private org.bytedeco.javacv.Frame image;
    private org.bytedeco.javacpp.opencv_core.Mat face;
    private IFaceRecognizer faceRecognizer;
    private Thread thread;

    private String[] recognizedNames;

    private boolean onlyOneFaceCareTaker, faceRecognized;


    public org.bytedeco.javacpp.opencv_core.Mat getFace(){

        return face;

    }

    public String[] getRecognizedNames(){
        return recognizedNames.clone();
    }

    public void grab() throws FrameGrabber.Exception {
        image = grabber.grab();
    }

    public void showImage(){
        webcamPane.updateImage(java2DFrameConverter.convert(image));
    }

    public void detectFace()
    {
        opencv_core.Mat videoMatGray = new opencv_core.Mat();

        opencv_core.Mat imageToMat = converter.convertToMat(image);

        //Convert the current frame to grayscale:
        cvtColor(imageToMat, videoMatGray, COLOR_BGRA2GRAY);
        equalizeHist(videoMatGray, videoMatGray);

        opencv_core.RectVector faces = new opencv_core.RectVector();
        classifier.detectMultiScale(videoMatGray, faces);
        opencv_core.Rect face_i = null;

        recognizedNames = new String[(int) faces.size()];


        if (!faces.empty())
        {
            long size = 1;

            if (!onlyOneFaceCareTaker)
                size = faces.size();

            for (int i = 0; i < size; i++)
            {
                face_i = faces.get(i);

                face = new opencv_core.Mat(videoMatGray, face_i);
                // If fisher face recognizer is used, the face need to be
                // resized.
                //resize(face, 100, new opencv_core.Size(200, 200),
                //1.0, 1.0, INTER_CUBIC);

                // Now perform the prediction, see how easy that is:
                //IntPointer label = new IntPointer(1);

                String name;
                if (faceRecognizer != null)
                    recognizedNames[i] = name = faceRecognizer.whoIs(face);
                else
                    name = "unknown";

                int pos_x = Math.max(face_i.tl().x() - 10, 0);
                int pos_y = Math.max(face_i.tl().y() - 10, 0);

                if (name.toLowerCase().contains("unknown")) {
                    rectangle(imageToMat, face_i, new opencv_core.Scalar(200, 0, 255, 1), 2, 0, 0);

                    putText(imageToMat, "Detecting", new opencv_core.Point(pos_x, pos_y),
                            FONT_HERSHEY_COMPLEX, 0.6, new opencv_core.Scalar(200, 0, 255, 1));
                }else{

                    if (faceRecognized)
                        faceRecognized = false;

                    rectangle(imageToMat, face_i, new opencv_core.Scalar(0, 255, 0, 1));

                    putText(imageToMat, "OK", new opencv_core.Point(pos_x, pos_y),
                            FONT_HERSHEY_COMPLEX, 0.6, new opencv_core.Scalar(0, 255, 0, 2.0));
                }
            }
        }
        image = converter.convert(imageToMat);
    }

    public void start(){
        thread.start();
    }

    public BufferedImage getImage(){
        if (face != null)
            return java2DFrameConverter.convert(converter.convert(face));
        else
            return null;
    }

    public void close() throws FrameGrabber.Exception {
        grabber.close();
    }

    public void dispose(){
        frame.dispose();
    }

    public void repaint(){
        frame.repaint();
    }

    public void removeComponent(Component component){
        frame.remove(component);
        frame.repaint();
    }

    public void addComponent(Component component, String position){
        frame.add(component, position);
        frame.repaint();
    }

    public static WebcamBuilder builder(){
        return new WebcamBuilder();
    }

    public boolean isRecognizingFace() {
        return faceRecognized;
    }

    public static class WebcamBuilder {

        private Webcam webcam;
        private String resourcesDirectoryPath;


        public WebcamBuilder init(String frameName) {
            webcam = new Webcam();
            webcam.faceRecognized = true;
            webcam.frame = new JFrame(frameName);
            webcam.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            webcam.webcamPane = new ImagePane(Color.BLACK);
            webcam.grabber = new org.bytedeco.javacv.OpenCVFrameGrabber(0);
            webcam.converter = new org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage();
            webcam.java2DFrameConverter = new org.bytedeco.javacv.Java2DFrameConverter();
            return this;
        }

        public WebcamBuilder setResourcesDirectoryPath(String resourcesDirectoryPath){
            this.resourcesDirectoryPath = resourcesDirectoryPath;
            return this;
        }

        public WebcamBuilder setLayout(LayoutManager layoutManager) {
            webcam.frame.setLayout(layoutManager);
            return this;
        }

        public WebcamBuilder addComponent(Component component, String position) {
            webcam.frame.getContentPane().add(component, position);
            return this;
        }

        public WebcamBuilder setDefaultClassifier() {
            try {
                File cascadeValues = Paths.get(resourcesDirectoryPath, "haarcascade_frontalface_default.xml").toFile();
                if (FileUtils.checksum(cascadeValues, new Adler32()).getValue() != 1694615656)
                    throw new RuntimeException("la ressource /resources/haarcascade... est corrompue, veuillez la télécharger");

                webcam.classifier = new opencv_objdetect.CascadeClassifier(
                       cascadeValues.getAbsolutePath()
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        public WebcamBuilder initGrabber() throws FrameGrabber.Exception {
            webcam.grabber.start();
            webcam.grab();
            webcam.webcamPane.updateImage(webcam.java2DFrameConverter.convert(webcam.image));
            webcam.frame.add(webcam.webcamPane, BorderLayout.CENTER);
            return this;
        }

        public WebcamBuilder setFaceRecognizer(IFaceRecognizer faceRecognizer){
            webcam.faceRecognizer = faceRecognizer;
            return this;
        }

        public WebcamBuilder packAndShow(){
            webcam.frame.pack();
            webcam.frame.setVisible(true);

            return this;
        }

        public WebcamBuilder setDefaultThread(){
            webcam.thread = new Thread(() ->{
                webcam.frame.pack();
                webcam.frame.setVisible(true);
               while (webcam.frame.isVisible()){
                   try {
                       webcam.grab();
                       webcam.showImage();
                   } catch (FrameGrabber.Exception e) {
                       throw new RuntimeException(e);
                   }
               }
            });
            return this;
        }

        public WebcamBuilder setFaceDetectorThread(){
            webcam.thread = new Thread(()->{
                webcam.frame.pack();
                webcam.frame.setVisible(true);
                while (webcam.frame.isVisible())
                {
                    try{
                        webcam.grab();
                        webcam.detectFace();
                        webcam.showImage();
                    }catch (FrameGrabber.Exception e){
                        throw new RuntimeException(e);
                    }
                }
            });
            return this;
        }

        public WebcamBuilder setOnlyOneFaceCareTaker(boolean onlyOneFaceCareTaker){
            webcam.onlyOneFaceCareTaker = onlyOneFaceCareTaker;
            return this;
        }

        public Webcam build(){

            return webcam;
        }
    }
}