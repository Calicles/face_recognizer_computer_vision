package com.antoine;

import com.antoine.ui.ProgressBar;
import com.antoine.ui.RecognizeUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.concurrent.Executors;


/**
 * Hello world!
 *
 */
public class App
{

    private static Logger log = LoggerFactory.getLogger(com.antoine.App.class);

    public static void main( String[] args ) {
        String loadingState = "Loading ";

        CodeSource src = App.class.getProtectionDomain().getCodeSource();
        File pathToJar = null;
        try {
            pathToJar = new File(src.getLocation().toURI());
        } catch (URISyntaxException e) {
            log.debug("erreur", e);
            RecognizeUI.showErrorDialog("Erreur de localisation du fichier source", true);
        }

        String absoluteProgrammPath = pathToJar.getParentFile().getAbsolutePath();

        JFrame mainFraim = new JFrame();
        ProgressBar progressBar = new ProgressBar(mainFraim, true);
        progressBar.showProgressBar(loadingState + "component");
        RecognizeUI ui = new RecognizeUI(absoluteProgrammPath);

        Executors.newCachedThreadPool().submit(() -> {
            try
            {
                progressBar.updateLoadingState(loadingState + "model");
                ui.loadModel();
                progressBar.updateLoadingState(loadingState + "webcam");
                ui.initWebCam();
                progressBar.updateLoadingState(loadingState + "data");
                ui.initWindow();
                progressBar.updateLoadingState("Starting Scan");
                ui.startRecognizer();

            }catch (Throwable t)
            {
                RecognizeUI.showErrorDialog("Erreur de chargement des données", true);
            }finally
            {
                mainFraim.dispose();
            }
        });
    }
}
