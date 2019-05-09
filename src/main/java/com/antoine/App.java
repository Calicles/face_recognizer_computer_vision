package com.antoine;

import com.antoine.ui.ProgressBar;
import com.antoine.ui.RecognizeUI;

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
            try {
                ui.init();
                ui.startRecognizer();
            } catch (Throwable e) {
                JFrame frame = new JFrame("test");
                JLabel label = new JLabel(e.getMessage());
                JPanel p = new JPanel();
                p.add(label);
                frame.getContentPane().add(p);
                frame.pack();
                frame.setVisible(true);


                //log.error("Loding Data error", e);
                throw new RuntimeException();
            } finally {
                mainFraim.dispose();
            }
        });
    }
}
