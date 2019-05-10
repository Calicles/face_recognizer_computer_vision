package com.antoine.dl;

import com.antoine.io.IOHelper;
import org.apache.commons.io.FileUtils;
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
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Adler32;

public class FaceRecognition {

    private static Logger log = LoggerFactory.getLogger(com.antoine.dl.FaceRecognition.class);

    private static final double THRESHOLD = 0.50;
    private FaceNetSmallV2Model faceNetSmallV2Model;
    private ComputationGraph computationGraph;
    private static final NativeImageLoader LOADER = new NativeImageLoader(96, 96, 3);
    private final HashMap<String, INDArray> memberEncodingsMap = new HashMap<>();
    private final String SAVE_FILE = "save";
    private final String MODELSAVED_FILE = "modelSaved.zip";
    private final String CHECKSUM_FILE = "lastChecksum.txt";

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
        String modelLoaded = null;
        File data = Paths.get(absoluteProgrammePath, SAVE_FILE, MODELSAVED_FILE).toFile();
        File lastChecksumFile = Paths.get(absoluteProgrammePath, SAVE_FILE, CHECKSUM_FILE).toFile();

        if (data.exists()
                && lastChecksumFile.exists()
                && FileUtils.checksum(data, new Adler32()).getValue() == Long.parseLong(IOHelper.readFile(lastChecksumFile)))
        {
                computationGraph = ModelSerializer.restoreComputationGraph(data.getAbsolutePath());
                modelLoaded = "modèle enregistré";

        }else {
                faceNetSmallV2Model = new FaceNetSmallV2Model();
                computationGraph= faceNetSmallV2Model.init();
                modelLoaded = "modèle recréé";
        }
        log.info(computationGraph.summary());
        log.info("modèle chargé :      " + modelLoaded);
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

    public void serializeModel(String absoluteProgrammePath) throws IOException
    {
        File save = new File(absoluteProgrammePath, SAVE_FILE), checksumFile;
        long checksum;
        if (!save.exists())
            save.mkdir();

        save = new File(save, MODELSAVED_FILE);
        checksumFile = new File(save.getParent(), CHECKSUM_FILE);
        ModelSerializer.writeModel(computationGraph, save.getAbsolutePath(), false);
        checksum = FileUtils.checksum(save, new Adler32()).getValue();
        IOHelper.writeFile(checksumFile, ""+checksum);

        log.info("Modèle enregistré");
    }
}
