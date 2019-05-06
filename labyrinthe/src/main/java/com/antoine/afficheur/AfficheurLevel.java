package com.antoine.afficheur;

import com.antoine.contracts.IEntity;
import com.antoine.contracts.IMap;
import com.antoine.contracts.IStructure;
import com.antoine.contracts.IAfficheur;
import com.antoine.geometry.Rectangle;
import com.antoine.geometry.Tile;
import com.antoine.modele.level.Level;
import com.antoine.modele.level.Level2;
import com.antoine.modele.level.Level3;
import com.antoine.modele.level.Level4;
import com.antoine.services.ImageReader;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * <b>Classe de lien entre la vue et le modèle.</b>
 * <p>Visite le niveau pour afficher les composants</p>
 *
 * @author Antoine
 */
public class AfficheurLevel extends AbstractAfficheur implements IAfficheur {


    /**
     * @see IAfficheur#visit(IStructure)
     *
     * @param structure la structure à visiter
     */
    @Override
    public void visit(IStructure structure) {

        if(structure instanceof Level){
            drawLevel1(structure);
        }else if(structure instanceof Level2) {
            drawLevel2(structure);
        }else if(structure instanceof Level4){
            drawLevel4(structure);
        }else if(structure instanceof Level3){
            drawLevel3(structure);
        }

    }

    /**
     * <p>Dessine ce qui n'est pas présent dans un Level3</p>
     * @param structure à déssiner
     */
    protected void drawLevel4(IStructure structure) {
        IEntity boss= structure.getBoss();
        Rectangle screen= structure.getScreen();
        drawLevel3(structure);

        if (structure.isRunning())
            g.drawImage(boss.getImage(), playerScreenPositionX(boss, screen), playerScreenPositionY(boss, screen), null);
    }


    /**
     * <p>Affichage principale</p>
     * si le niveau est en court appelle
     * @see AfficheurLevel#drawLevel(IStructure)
     * sinon affiche l'image de fin.
     * @param structure un niveau à afficher
     */
    private void drawLevel1(IStructure structure) {

        if(!structure.isRunning()) {
            String endImageUrl= structure.getEndImageUrl();
            BufferedImage endImage= ImageReader.lireImage(endImageUrl);
            g.drawImage(endImage, 0, 0, null);
        }else
            drawLevel(structure);

    }

    /**
     * <p>Affiche les composants principaux d'un niveau.</p>
     * @param structure à afficher
     */
    private void drawLevel(IStructure structure) {
        IEntity player= structure.getPlayer();
        IMap map= structure.getMap();
        drawMap(map);
        g.drawImage(player.getImage(), player.getX(), player.getY(), null);
    }

    /**
     * <p>Affiche le fond du décors en posant les tuiles qui sont associées à un numéro.</p>
     * @param mapStruct la carte.
     */
    private void drawMap(IMap mapStruct) {
        HashMap<Integer, BufferedImage> tileSet= mapStruct.getTileSet();
        Tile[][] map= mapStruct.getMap();
        Tile tile;
        for(int i=0;i<map.length;i++)
        {
            for(int j=0;j<map[0].length;j++)
            {
                tile= map[i][j];
                g.drawImage(tileSet.get(tile.getTile_num()),
                        tile.getX(), tile.getY(), null);
            }
        }
    }

    /**
     * <p>Affiche les composants d'un niveau de type 2.</p>
     * @param structure à afficher
     */
    private void drawLevel2(IStructure structure) {
        if(structure.isRunning()) {
            drawScreen(structure);
            drawPlayer(structure);
        } else {
            BufferedImage image = ImageReader.lireImage(structure.getEndImageUrl());
            g.drawImage(image, 0, 0, null);
        }
    }

    /**
     * <p>Affiche le joueur et le positionne relativement au repère de l'écran.</p>
     * @param structure à afficher
     */
    private void drawPlayer(IStructure structure) {
        IMap map = structure.getMap();
        IEntity player= structure.getPlayer();
        int screenPosY= playerScreenPositionY(player, map);
        g.drawImage(player.getImage(), player.getX(), screenPosY, null);
    }

    /**
     * <p>Affiche ce qui apparaît à l'écran.</p>
     * @param structure à afficher
     */
    private void drawScreen(IStructure structure) {
        IMap mapStruct= structure.getMap();
        Rectangle screen= structure.getScreen();
        Tile tile;
        Tile[][] map= mapStruct.getMap();
        HashMap<Integer, BufferedImage> set= mapStruct.getTileSet();
        int row= screen.getBeginY();
        int col= screen.getBeginX();
        int rowMax= screen.getEndY();
        int colMax= screen.getEndX();
        int x= 0, y= 0;

        for(int i= row; i<rowMax; i++) {
            for(int j= col; j<colMax; j++) {
                tile= map[i][j];
                g.drawImage(set.get(tile.getTile_num()), x * mapStruct.getTile_width(),
                        y * mapStruct.getTile_height(), null);
                x++;
            }
            x= 0; y++;
        }
    }

    /**
     * <p>Calcule la coordonnée Y du joueur relativement au coordonnées de l'écran dans la carte.</p>
     * @param player joueur
     * @param map la carte
     * @return la position traduite relativement à la position de l'écran.
     */
    private int playerScreenPositionY(IEntity player, IMap map) {
        int coef;
        int tile_height= map.getTile_height();
        coef= player.getY() / (tile_height * 20);
        return player.getY() - (coef * (tile_height * 20));
    }

    /**
     * <p>Affiche composant d'un niveau de type 3.</p>
     * @param structure à afficher
     */
    private void drawLevel3(IStructure structure) {
        if(structure.isRunning()) {
            drawScreenLevel3(structure);
            drawPlayerLevel3(structure);
        } else{
              BufferedImage image= ImageReader.lireImage(structure.getEndImageUrl());
              g.drawImage(image, 0, 0, null);
            }
    }

    /**
     * <p>Affiche les compostant de l'écran d'un niveau de type 3 (avec scrolling)</p>
     * @param structure à afficher
     */
    private void drawScreenLevel3(IStructure structure) {
        IMap mapStruct= structure.getMap();
        Rectangle screen= structure.getScreen();
        Tile tile;
        Tile[][] map= mapStruct.getMap();
        HashMap<Integer, BufferedImage> set= mapStruct.getTileSet();
        int tile_width= mapStruct.getTile_width();
        int tile_height= mapStruct.getTile_height();

        int row= screen.getBeginY() / tile_height;
        int col= screen.getBeginX() / tile_width;
        int rowMax= screen.getEndY() / tile_height;
        int colMax= screen.getEndX() / tile_width;
        int x, y;

        if(screen.getEndY() % mapStruct.getTile_height() != 0) rowMax ++;
        if(screen.getEndX() % mapStruct.getTile_width() != 0) colMax ++;
        for(int i= row; i<rowMax; i++) {
            for(int j= col; j<colMax; j++) {
                tile= map[i][j];
                y= tile.getY() - screen.getBeginY();
                x= tile.getX() - screen.getBeginX();
                g.drawImage(set.get(tile.getTile_num()), x,
                        y, null);
            }
        }
    }

    /**
     * <p>Calcule les coordonnées du joueur relativement à l'écran.</p>
     * @param structure à afficher
     */
    private void drawPlayerLevel3(IStructure structure) {
        IEntity player= structure.getPlayer();
        Rectangle screen= structure.getScreen();
        int screenPosY= playerScreenPositionY(player, screen);
        int screenPosX= playerScreenPositionX(player, screen);
        g.drawImage(player.getImage(), screenPosX, screenPosY, null);
    }

    /**
     * <p>Calcule coordonnée Y du joueur via l'écran.</p>
     * @param player le joueur
     * @param screen le rectangle qui représente les positions de l'écran sur la carte
     * @return la position Y du joueur traduit en position écran
     */
    private int playerScreenPositionY(IEntity player, Rectangle screen) {
        return player.getY() - screen.getBeginY();
    }

    /**
     * <p>Calcule coordonnées X.</p>
     * @see AfficheurLevel#playerScreenPositionY(IEntity, Rectangle)
     * @param player le joueur.
     * @param screen l'écran.
     * @return la position X du joueur sur l'écran.
     */
    private int playerScreenPositionX(IEntity player, Rectangle screen) {
        return player.getX() - screen.getBeginX();
    }
}
