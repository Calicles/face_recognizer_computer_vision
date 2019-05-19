package com.antoine.photoRegister;

import com.antoine.ui.ImagePane;
import com.antoine.ui.Webcam;
import org.bytedeco.javacv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.locks.Lock;

public class Photo {

    private static Logger log = LoggerFactory.getLogger(com.antoine.photoRegister.Photo.class);

    private JTextField userName;
    private JLabel info;
    private ImagePane photoPane;
    private JPanel containerPane;
    private Webcam webcam;
    private BufferedImage photo;
    private JButton enregistrer;
    private String absoluteProgrammePath;

    public Photo(String absoluteProgrammePath)
    {
        this.absoluteProgrammePath = absoluteProgrammePath;
    }

    public void init(Lock lock) throws FrameGrabber.Exception {
        enregistrer = new JButton("photographier")
                ;
        enregistrer.addActionListener((event)-> registerPhoto());

        info = new JLabel("Entrez votre nom et enregistrez une photo");
        info.setBackground(Color.BLACK);
        info.setForeground(Color.WHITE);

        constructPhotoPane(lock);

        JPanel panelBas = new JPanel();
        userName = new JTextField();
        userName.addActionListener((e -> registerPhoto()));

        panelBas.setLayout(new BorderLayout());
        panelBas.add(userName, BorderLayout.NORTH);
        panelBas.add(enregistrer, BorderLayout.CENTER);
        panelBas.add(info, BorderLayout.SOUTH);
        panelBas.setBackground(Color.BLACK);

        webcam = Webcam.builder()
                .init("PhotoRegister")
                .setResourcesDirectoryPath(Paths.get(absoluteProgrammePath, "resources").toFile().getAbsolutePath())
                .setLayout(new BorderLayout())
                .initGrabber()
                .setDefaultClassifier()
                .setOnlyOneFaceCareTaker(true)
                .addComponent(panelBas, BorderLayout.SOUTH)
                .addComponent(containerPane, BorderLayout.EAST)
                .setFaceDetectorThread()
                .build();

        containerPane.setVisible(false);
        webcam.start();
    }


    private void registerPhoto() {
        photo = webcam.getImage();
        if (!userName.getText().isEmpty() &&  photo != null)
        {
            try {
                enregistrer.setEnabled(false);

                photoPane.updateImage(photo);

                containerPane.setVisible(true);

            } catch (Throwable e) {
                log.info("erreur lors de la création de la photo", e);
                info.setText("Erreur");
            }
        }else {
            if (photo == null)
                info.setText("Visage non détécté");
            else
                info.setText("Vous devez rentrez un nom");
            webcam.repaint();
        }
    }

    private void constructPhotoPane(Lock lock) {
        photoPane = new ImagePane(Color.BLACK);
        containerPane = new JPanel(new BorderLayout());
        JLabel info = new JLabel("Valider la photo ?");
        info.setForeground(Color.WHITE);
        JPanel panelBas = new JPanel(new BorderLayout());
        panelBas.setBackground(Color.BLACK);
        JPanel paneButton = new JPanel();
        paneButton.setBackground(Color.BLACK);

        photoPane.updateImage(photo);
        containerPane = new JPanel(new BorderLayout());
        containerPane.setBackground(Color.BLACK);
        containerPane.add(photoPane, BorderLayout.CENTER);
        JButton okButton = new JButton("ok");
        okButton.addActionListener((event)-> {
            try{
            File profilFile = new File(absoluteProgrammePath, "profil");

            if (!profilFile.exists()) {
                log.info("création du dossier 'profil'");
                profilFile.mkdir();
            }

                ImageIO.write(photo, "PNG", new File(profilFile, userName.getText() + ".png"));

                webcam.dispose();
                webcam.close();
                synchronized (lock){
                    lock.notify();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        JButton cancelButton = new JButton("annuler");
        cancelButton.addActionListener((event)->{
            containerPane.setVisible(false);
            enregistrer.setEnabled(true);
        });
        paneButton.add(okButton);
        paneButton.add(cancelButton);
        panelBas.add(info, BorderLayout.CENTER);
        panelBas.add(paneButton, BorderLayout.SOUTH);
        containerPane.add(panelBas, BorderLayout.SOUTH);
    }
}
