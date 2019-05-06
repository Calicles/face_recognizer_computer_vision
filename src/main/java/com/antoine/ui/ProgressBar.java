package com.antoine.ui;

import javax.swing.*;

public class ProgressBar {

    private JProgressBar progressBar;
    private JFrame mainFraim;
    private boolean undecorated;

    public ProgressBar(JFrame mainFraim, boolean undecorated)
    {
        this.mainFraim = mainFraim;
        this.undecorated = undecorated;
    }

    public void showProgessBar(String msg)
    {
        SwingUtilities.invokeLater(()-> {
                this.progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
        progressBar.setString(msg);
        progressBar.setStringPainted(true);
        progressBar.setIndeterminate(true);
        progressBar.setVisible(true);
        mainFraim.getContentPane().add(progressBar);
        mainFraim.setLocationRelativeTo(null);
        mainFraim.setUndecorated(undecorated);
        mainFraim.pack();
        mainFraim.setVisible(true);
        mainFraim.repaint();
        });
    }
}
