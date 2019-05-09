package com.antoine.dl;

import org.bytedeco.javacpp.opencv_core;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.graph.vertex.GraphVertex;
import org.deeplearning4j.nn.workspace.LayerWorkspaceMgr;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FaceRecognition {

    private static Logger log = LoggerFactory.getLogger(com.antoine.dl.FaceRecognition.class);

    private static final double THRESHOLD = 0.50;
    //private FaceNetSmallV2Model faceNetSmallV2Model;
    private ComputationGraph computationGraph;
    private static final NativeImageLoader LOADER = new NativeImageLoader(96, 96, 3);
    private final HashMap<String, INDArray> memberEncodingsMap = new HashMap<>();

    private INDArray transpose(INDArray indArray1) {
        INDArray one = Nd4j.create(new int[]{1, 96, 96});
        one.assign(indArray1.get(NDArrayIndex.point(0), NDArrayIndex.point(2)));
        INDArray two = Nd4j.create(new int[]{1, 96, 96});
        two.assign(indArray1.get(NDArrayIndex.point(0), NDArrayIndex.point(1)));
        INDArray three = Nd4j.create(new int[]{1, 96, 96});
        three.assign(indArray1.get(NDArrayIndex.point(0), NDArrayIndex.point(0)));
        return Nd4j.concat(0, one, two, three).reshape(new int[]{1, 3, 96, 96});
    }

    private INDArray read(opencv_core.Mat imread) throws IOException {
        INDArray indArray = LOADER.asMatrix(imread);
        return transpose(indArray);
    }

    private INDArray forwardPass(INDArray indArray) {
        Map<String, INDArray> output = computationGraph.feedForward(indArray, false);
        GraphVertex embeddings = computationGraph.getVertex("encodings");
        INDArray dense = output.get("dense");
        embeddings.setInputs(dense);
        INDArray embeddingValues = embeddings.doForward(false, LayerWorkspaceMgr.builder().defaultNoWorkspace().build());
        log.debug("dense =                 " + dense);
        log.debug("encodingsValues =                 " + embeddingValues);
        return embeddingValues;
    }

    private double distance(INDArray a, INDArray b) {
        return a.distance2(b);
    }

    public void loadModel(String absoluteProgrammePath) throws Exception {
        String modèleChargé = null;
//        faceNetSmallV2Model = new FaceNetSmallV2Model();
        File parent = new File(absoluteProgrammePath);
        File modelFile;
        if ((modelFile = new File(parent, "save")).exists())
        {
            modelFile = modelFile.listFiles()[0];
            computationGraph = ModelSerializer.restoreComputationGraph(modelFile.getPath());
            modèleChargé = "modèle enregistré";
        }else {
            modelFile = new File(parent, "model.zip");
            computationGraph = ModelSerializer.restoreComputationGraph(Paths.get("model.zip").toAbsolutePath().toUri().getPath());
            modèleChargé = "premierModèle";
            //faceNetSmallV2Model.init();
        }
        log.info(computationGraph.summary());
        log.info("modèle chargé :      " + modèleChargé);
    }

    public void registerNewMember(String memberId, opencv_core.Mat imageread) throws IOException {
        INDArray read = read(imageread);
        memberEncodingsMap.put(memberId, forwardPass(normalize(read)));
    }

    private static INDArray normalize(INDArray read) {
        return read.div(255.0);
    }

    public String whoIs(opencv_core.Mat imageread) throws IOException {
        INDArray read = read(imageread);
        INDArray encodings = forwardPass(normalize(read));
        double minDistance = Double.MAX_VALUE;
        String foundUser = "";
        for (Map.Entry<String, INDArray> entry : memberEncodingsMap.entrySet()) {
            INDArray value = entry.getValue();
            double distance = distance(value, encodings);
            //log.info("distance of " + entry.getKey() + " with " + new File(imageread.toString()).getName() + " is " + distance);
            if (distance < minDistance) {
                minDistance = distance;
                foundUser = entry.getKey();
            }
        }
        if (minDistance > THRESHOLD) {
            foundUser = "Unknown user";
        }
        log.info(foundUser + " with distance " + minDistance);
        return foundUser;
    }

    public void serializeModel(String absoluteProgrammePath) throws IOException {

        File modelSavedPath = new File(absoluteProgrammePath, "save");
        ModelSerializer.writeModel(computationGraph, modelSavedPath.getPath()+"modelSaved.zip", false);
        log.info("Modèle enregistré");
    }
}
