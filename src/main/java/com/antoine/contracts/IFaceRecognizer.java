package com.antoine.contracts;

public interface IFaceRecognizer {

    String whoIs(org.bytedeco.javacpp.opencv_core.Mat face);
}
