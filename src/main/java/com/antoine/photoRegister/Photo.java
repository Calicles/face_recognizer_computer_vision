package com.antoine.photoRegister;

import com.antoine.ui.WebcamPanelDecorated;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.Lock;

public class Photo {

    private static Logger log = LoggerFactory.getLogger(com.antoine.photoRegister.Photo.class);

    private static Webcam webcam;
    private static JTextField userName;
    private static JLabel info;
    private static JFrame window;

    private String absoluteProgrammePath;

    public Photo(String absoluteProgrammePath)
    {
        this.absoluteProgrammePath = absoluteProgrammePath;
    }

    public void init(Lock lock) {

        boolean istrue = false;
        try {
            webcam = Webcam.getDefault();

            webcam.setViewSize(WebcamResolution.VGA.getSize());

            WebcamPanelDecorated panel = new WebcamPanelDecorated(webcam);
            panel.setFPSDisplayed(true);
            panel.setDisplayDebugInfo(true);
            panel.setImageSizeDisplayed(true);
            panel.setMirrored(true);

            if (istrue)
            throw new RuntimeException();


        userName = new JTextField();

        JButton enregistrer = new JButton("enregistrer");
        enregistrer.addActionListener((event)-> registerPhoto(lock));

        info = new JLabel("Entrez votre nom et placez votre visage dans le guide");
        info.setBackground(Color.BLACK);
        info.setForeground(Color.WHITE);

        JPanel panelBas = new JPanel();

        panelBas.setLayout(new BorderLayout());
        panelBas.add(userName, BorderLayout.NORTH);
        panelBas.add(enregistrer, BorderLayout.CENTER);
        panelBas.add(info, BorderLayout.SOUTH);
        panelBas.setBackground(Color.BLACK);

        window = new JFrame("Enregistreur de profile");
        Container container = window.getContentPane();
        container.setLayout(new BorderLayout());
        container.add(panel, BorderLayout.CENTER);
        container.add(panelBas, BorderLayout.SOUTH);
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
 }catch (Throwable t)
        {
            JFrame frame = new JFrame("Erreur");
            JPanel panel2 = new JPanel();
            JLabel label = new JLabel("ERREUR");
            label.setForeground(Color.RED);
            label.setBackground(Color.BLACK);
            panel2.setBackground(Color.BLACK);
            panel2.add(label);
            frame.getContentPane().add(panel2);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        }
    }

    private void registerPhoto(Lock lock) {
        if (!userName.getText().isEmpty())
        {
            try {

                File profilFile = new File(absoluteProgrammePath,"profil");

                if (!profilFile.exists()) {
                    log.info("création du dossier 'profil'");
                    profilFile.mkdir();
                }

                ImageIO.write(webcam.getImage(), "PNG",
                        new File(profilFile, userName.getText() + ".png"));

            } catch (IOException e) {
                log.info("erreur lors de la créationd de la photo", e);
                info.setText("Erreur");
            }

            info.setText("Photo enregistré");

            window.repaint();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) { }
            webcam.close();
            window.dispose();
            synchronized (lock){
                lock.notify();
            }
        }else {

            info.setText("Vous devez rentrez un nom");
            window.repaint();
        }
    }
}
