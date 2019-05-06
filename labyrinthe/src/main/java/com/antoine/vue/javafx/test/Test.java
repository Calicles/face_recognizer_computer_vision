package com.antoine.vue.javafx.test;

import com.antoine.contracts.IStructure;
import com.antoine.contracts.Presentateur;
import com.antoine.manager.niveau.LevelManager;
import com.antoine.services.ImageReader;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;


public class Test extends Application {


    public static void main(String[] args){
        Application.launch(Test.class);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Presentateur presentateur = new LevelManager();
        Dimension dim = presentateur.getDimension();
        primaryStage.setTitle("Hello World");
        Group root = new Group();
        Scene scene = new Scene(root, dim.width, dim.height, Color.LIGHTGREEN);

        Afficheru afficheur = new Afficheru();
        presentateur.accept(afficheur);

        Button btn = new Button();
        btn.setLayoutX(100);
        btn.setLayoutY(80);
        btn.setText("Hello World");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                System.out.println("Hello World");
            }
        });
        root.getChildren().add(afficheur);
        primaryStage.setScene(scene);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.LEFT) {
                    presentateur.playerMovesLeft();
                }else if (event.getCode() == KeyCode.RIGHT){
                    presentateur.playerMovesRight();
                }

                presentateur.accept(afficheur);
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                presentateur.playerMovesReleased();
                presentateur.accept(afficheur);
            }
        });

        primaryStage.show();

    }
}
