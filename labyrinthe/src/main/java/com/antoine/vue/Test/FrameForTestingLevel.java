package com.antoine.vue.Test;

import com.antoine.contracts.ILevel;
import com.antoine.services.Assembler;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FrameForTestingLevel extends JFrame {

    private JPanelForTestingLevel panel;

    private PresentateurTest presentateurTest;

    private ILevel levelToTest;

    public FrameForTestingLevel(ILevel levelToTest) {
        this.levelToTest = levelToTest;
        init();
    }

    private void init(){


        presentateurTest = new PresentateurTest(levelToTest);

        panel = new JPanelForTestingLevel(presentateurTest);

        this.getContentPane().add(panel);

        this.addKeyListener(new InternImageListener());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setTitle("My Little Pony Testing Now");
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private class InternImageListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

            this.moves(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if ((e.getKeyCode() == KeyEvent.VK_RIGHT) || (e.getKeyCode() == KeyEvent.VK_LEFT)||
                    (e.getKeyCode() == KeyEvent.VK_UP) || (e.getKeyCode() == KeyEvent.VK_DOWN))
                panel.playerMovesReleased();
        }

        private void moves(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                panel.playerMovesRight();
            else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                panel.playerMovesLeft();
            } else if (e.getKeyCode() == KeyEvent.VK_UP)
                panel.playerMovesUp();
            else if (e.getKeyCode() == KeyEvent.VK_DOWN)
                panel.playerMovesDown();

        }

    }

    public static void main(String[] arg) {

        SwingUtilities.invokeLater(()-> {
            FrameForTestingLevel frame;
            Assembler assembler = new Assembler("./src/main/conf/config/conf.xml");
            ILevel levelTwilight = (ILevel) assembler.newInstance("levelTwilight");

            frame = new FrameForTestingLevel(levelTwilight);
        });
    }

}
