package com.antoine.services;

import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.Assert.*;

public class ImageReaderTest {

    String path= "./ressources/images/tapis1.png";


    /**
     * test de
     * @throws IOException
     */
    @Test (expected = IOException.class)
    public void lireImage() throws IOException {
        ImageReader.lireImage("taratata");
    }


    /**
     * test de valeur de longueur d'une image
     * vérifie le chargement
     * @throws IOException
     */
    @Test
    public void lireImage1() throws IOException {
        BufferedImage image= ImageReader.lireImage(path);

        Assert.assertTrue(image.getWidth() == 32);
    }
}