package com.antoine.ui;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.FrameGrabber;

import javax.swing.*;
import java.awt.*;

import static org.bytedeco.javacpp.opencv_core.FONT_HERSHEY_PLAIN;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class Webcam {

    private static Webcam INSTANCE;

    private JFrame frame;
    private WebcamPane webcamPane;
    private org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier classifier;
    private org.bytedeco.javacv.OpenCVFrameGrabber grabber;
    private org.bytedeco.javacv.OpenCVFrameConverter converter;
    private org.bytedeco.javacv.Java2DFrameConverter java2DFrameConverter;
    private org.bytedeco.javacv.Frame image;
    private org.bytedeco.javacpp.opencv_core.Mat face;

    public void startGrabbing() throws FrameGrabber.Exception {
        grabber.start();
    }

    public org.bytedeco.javacpp.opencv_core.Mat getFace(){

        return face;

    }

    public void grab() throws FrameGrabber.Exception {
        image = grabber.grab();
        face = detectFace(converter.convertToMat(image));
    }

    public void showImage(){
        webcamPane.updateImage(java2DFrameConverter.convert(image));
    }

    private opencv_core.Mat detectFace(opencv_core.Mat imread)
    {
        opencv_core.Mat videoMatGray = new opencv_core.Mat();
        opencv_core.Mat face = null;
        //Convert the current frame to grayscale:
        cvtColor(imread, videoMatGray, COLOR_BGRA2GRAY);
        equalizeHist(videoMatGray, videoMatGray);

        // Find the faces in the frame:*/
        opencv_core.RectVector faces = new opencv_core.RectVector();
        classifier.detectMultiScale(videoMatGray, faces);

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

    public static Webcam build(String frameName){
        INSTANCE.frame = new JFrame(frameName);
        INSTANCE.webcamPane = new WebcamPane();
        INSTANCE.grabber = new org.bytedeco.javacv.OpenCVFrameGrabber(0);
        INSTANCE.converter = new org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage();
        INSTANCE.java2DFrameConverter = new org.bytedeco.javacv.Java2DFrameConverter();
        return INSTANCE;
    }

    public static Webcam setLayout(LayoutManager layoutManager){
        INSTANCE.frame.setLayout(layoutManager);
        return INSTANCE;
    }

    public static Webcam addComponent(Component component, int position){
        INSTANCE.frame.add(component, position);
        return INSTANCE;
    }

    public Webcam setDefaultClassifier(){
        INSTANCE.classifier = new opencv_objdetect.CascadeClassifier(
                getClass().getResource("/resources/haarcascade_frontalface_default.xml").getPath()
        );
        return INSTANCE;
    }

    public static void freeINSTANCE() throws FrameGrabber.Exception {
        INSTANCE.grabber.stop();
        INSTANCE.frame.dispose();
        INSTANCE = null;
    }

    public static Webcam initGrabber() throws FrameGrabber.Exception {
        INSTANCE.startGrabbing();
        INSTANCE.grab();
        INSTANCE.webcamPane.updateImage(INSTANCE.java2DFrameConverter.convert(INSTANCE.image));
        INSTANCE.frame.add(INSTANCE.webcamPane, BorderLayout.CENTER);
        return INSTANCE;
    }
}
