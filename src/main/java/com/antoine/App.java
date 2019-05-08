package com.antoine;

import com.antoine.ui.ProgressBar;
import lombok.extern.slf4j.Slf4j;
import com.antoine.ui.RecognizeUI;

import javax.swing.*;

import java.util.concurrent.Executors;


/**
 * Hello world!
 *
 */
@Slf4j
public class App 
{
    public static void main( String[] args ) {

        final JFrame mainFraim = new JFrame();
        ProgressBar progressBar = new ProgressBar(mainFraim, true);
        progressBar.showProgressBar("Loading component");
        RecognizeUI ui = new RecognizeUI();

        Executors.newCachedThreadPool().submit(() -> {
            try {
                ui.init();
                ui.startRecognizer();
            } catch (Throwable e) {
                log.error("Loding Data error", e);
                throw new RuntimeException();
            } finally {
                mainFraim.dispose();
            }
        });
    }
}
