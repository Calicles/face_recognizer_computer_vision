package com.antoine.vue.Test;

import com.antoine.contracts.IAfficheur;
import com.antoine.contracts.LevelListener;
import com.antoine.contracts.Presentateur;
import com.antoine.events.LevelChangeEvent;

import javax.swing.*;
import java.awt.*;

public class JPanelForTestingLevel extends JPanel implements LevelListener {

    private Presentateur presentateurTest;
    private IAfficheur afficheurTest;

    public JPanelForTestingLevel(Presentateur presentateurTest) {
        this.presentateurTest = presentateurTest;
        this.afficheurTest = new AfficheurLevelForTest();
        presentateurTest.AddListener(this);
    }

    @Override
    public Dimension getPreferredSize() {

        return presentateurTest.getDimension();
    }

    @Override
    public void paintComponent(Graphics g) {
        afficheurTest.setGraphics(g);

        presentateurTest.accept(afficheurTest);

        afficheurTest.freeGraphics();
    }

    @Override
    public void update(LevelChangeEvent lve) {
        super.repaint();
    }

    public void playerMovesLeft(){
        presentateurTest.playerMovesLeft();
    }

    public void playerMovesRight(){
        presentateurTest.playerMovesRight();
    }

    public void playerMovesUp(){
        presentateurTest.playerMovesUp();
    }

    public void playerMovesDown(){
        presentateurTest.playerMovesDown();
    }

    public void playerMovesReleased() {
        presentateurTest.playerMovesReleased();
    }
}
