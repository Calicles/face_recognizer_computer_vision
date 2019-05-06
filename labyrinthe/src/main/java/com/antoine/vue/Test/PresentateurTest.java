package com.antoine.vue.Test;

import com.antoine.contracts.*;
import com.antoine.events.LevelChangeEvent;
import com.antoine.manager.musique.Jukebox;
import com.antoine.modele.level.Level;

import java.awt.*;
import java.util.ArrayList;

public class PresentateurTest implements Presentateur {

    private ILevel leveltoTest;

    private ArrayList<LevelListener> listeners;

    public PresentateurTest(ILevel leveltoTest) {

        listeners = new ArrayList<>();
        this.leveltoTest = leveltoTest;

        LevelChangeEvent e = new LevelChangeEvent();

        leveltoTest.setListeners(listeners);
        leveltoTest.setEvent(e);
        leveltoTest.start();

    }


    @Override
    public void playerMovesLeft() {
        leveltoTest.playerMovesLeft();
    }

    @Override
    public void playerMovesRight() {
        leveltoTest.playerMovesRight();
    }

    @Override
    public void playerMovesUp() {
        leveltoTest.playerMovesUp();
    }

    @Override
    public void playerMovesDown() {
        leveltoTest.playerMovesDown();
    }

    @Override
    public Dimension getDimension() {
        return leveltoTest.getDimension();
    }

    @Override
    public void AddListener(LevelListener listener) {
        ArrayList<LevelListener> list = new ArrayList<>();
        list.add(listener);
        leveltoTest.setListeners(list);
    }

    @Override
    public void switchLeveApple() {

    }

    @Override
    public void switchLevelRarity() {

    }

    @Override
    public void switchLevelRainbow() {

    }

    @Override
    public void playerMovesReleased() {
        leveltoTest.playerMovesReleased();
    }

    @Override
    public int getMapHeight() {
        return leveltoTest.getMapHeight();
    }

    @Override
    public void accept(IAfficheur afficheur) {
        afficheur.visit((IStructure) leveltoTest);
    }

    @Override
    public Jukebox getJukebox() {
        return null;
    }
}
