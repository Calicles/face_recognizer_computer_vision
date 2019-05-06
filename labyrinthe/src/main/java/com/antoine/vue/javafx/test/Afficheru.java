package com.antoine.vue.javafx.test;

import com.antoine.contracts.IAfficheur;
import com.antoine.contracts.IEntity;
import com.antoine.contracts.IMap;
import com.antoine.contracts.IStructure;
import com.antoine.geometry.Tile;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Afficheru extends Parent implements IAfficheur {

    private ImageView pl;


    public Afficheru(){

    }

    @Override
    public void visit(IStructure structure) {

        Group root = new Group();

        IMap map =  structure.getMap();

        drawMap(root, map);

        drawPlayer(root, structure);

        this.getChildren().add(root);
    }

    private void drawPlayer(Group root, IStructure structure) {
        IEntity player = structure.getPlayer();
        ImageView playerImage = new ImageView(SwingFXUtils.toFXImage(player.getImage(), null));
        playerImage.setX(player.getX());
        playerImage.setY(player.getY());


        root.getChildren().add(playerImage);
    }

    private void drawMap(Group root, IMap map) {
        Tile[][] carte = map.getMap();

        HashMap<Integer, BufferedImage> tileSet = map.getTileSet();

        for (int i = 0; i < carte[0].length; i++) {
            for (int j = 0; j < carte.length; j++) {
                Tile tile = carte[i][j];
                Image imageTile = SwingFXUtils.toFXImage(tileSet.get(carte[i][j].getTile_num()), null);
                ImageView view = new ImageView(imageTile);
                view.setX(tile.getX());
                view.setY(tile.getY());
                root.getChildren().add(view);
            }
        }
    }

    @Override
    public void setGraphics(Graphics g) {

    }

    @Override
    public void freeGraphics() {

    }
}
