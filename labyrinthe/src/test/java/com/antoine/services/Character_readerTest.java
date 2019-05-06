package com.antoine.services;

import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import static org.junit.Assert.*;

public class Character_readerTest {

    String setPath= "./ressources/setPonyAnimation/set.txt";
    HashMap<Integer, BufferedImage[]> anim;


    /**
     * test de non nullité
     */
    @Test
    public void readCharactereAnimation() {
        anim= Character_reader.readCharactereAnimation(setPath);
        assertTrue(( anim != null ));
    }


    /**
     * test de valeur sur longueur
     */
    @Test
    public void readCharactereAnimation1() {
        anim= Character_reader.readCharactereAnimation(setPath);
        assertTrue((anim.get(0)[0].getWidth() == 34));
    }


    /**
     * test
     * lancement RuntimeException
     */
    @Test(expected = RuntimeException.class)
    public void readCharacterAnimation2(){
        Character_reader.readCharactereAnimation(" ");
    }


    /**
     * test la longueur d'un array d'animation
     */
    @Test
    public void readCharactereAnimation3() {
        anim= Character_reader.readCharactereAnimation(setPath);
        assertTrue((anim.get(0).length == 3));
    }


    /**
     * test le nombre d'array présent
     */
    @Test
    public void readCharactereAnimation4() {
        anim= Character_reader.readCharactereAnimation(setPath);
        assertTrue((anim.size() == 4));
    }
}