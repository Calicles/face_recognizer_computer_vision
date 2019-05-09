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


        CodeSource src = App.class.getProtectionDomain().getCodeSource();
        File pathToJar = null;
        try {
            pathToJar = new File(src.getLocation().toURI());
        } catch (URISyntaxException e) {
        }
        String absoluteProgrammPath = pathToJar.getParentFile().getAbsolutePath();

        JFrame mainFraim = new JFrame();
        ProgressBar progressBar = new ProgressBar(mainFraim, true);
        progressBar.showProgressBar("Loading component");
        RecognizeUI ui = new RecognizeUI(absoluteProgrammPath);

        Executors.newCachedThreadPool().submit(() -> {

            ui.init();
            ui.startRecognizer();

            mainFraim.dispose();
        });
    }
}
