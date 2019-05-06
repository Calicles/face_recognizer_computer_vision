package com.antoine.Main;

import com.antoine.vue.frame.Frame;

import javax.swing.*;

/**
 * <b>Lanceur de l'application.</b>
 *
 * @author Antoine
 */
public class Main {

    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            new Frame();
         });
    }
}
