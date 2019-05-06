package com.antoine.afficheur;

import com.antoine.contracts.IEntity;
import com.antoine.contracts.IMap;
import com.antoine.contracts.IStructure;
import com.antoine.contracts.IAfficheur;
import com.antoine.geometry.Rectangle;
import com.antoine.geometry.Tile;
import com.antoine.modele.level.Level;

import java.awt.*;

/**
 * <b>Classe qui organise l'affichage d'un niveau sous forme d'une mini-carte</b>
 *
 * @author Antoine
 */
public class AfficheurMiniMap extends AbstractAfficheur implements IAfficheur {

    /**L'echelle utilisée pour les dimensions de la mini-carte*/
	public static final int SCALE= 10;

	/**Utilisé pour ralentir l'effet de clignotant de la sortie sur la mini-map*/
	private int tempo = 0;


    /**
     * @see IAfficheur#visit(IStructure)
     * @param structure dont l'affichage doit être organisé.
     */
    @Override
    public void visit(IStructure structure) {
        if (!(structure instanceof Level))
            drawMiniMap(structure);
    }

    /**
     * <p>Organise l'affichage de l'objet Graphics.</p>
     * L'affichage de l'ensemble des composants est mis à l'echelle de la valeur de SCALE.
     * @param structure à afficher.
     */
    private void drawMiniMap(IStructure structure)
    {
        int x, y;

        //Affiche le fond unifié
        this.drawBackground(structure);

        //Récupère les tiles
        IMap mapStruct= structure.getMap();
        Tile[][] map= mapStruct.getMap();

        //Récupère l'ancienne couleur, pour lui redonner par la suite
        Color old= g.getColor();


        //récupère les dimenstions des tiles à l'echelle
        int width= mapStruct.getTile_width() / SCALE;
        int height= mapStruct.getTile_height() / SCALE;

        //Choisi la couleur
        g.setColor(Color.ORANGE);

        //Affiche chaque tile sous forme de carré de couleur à l'echelle
        for(int i=0; i<map.length; i++) {
            for(int j=0;j<map[0].length;j++) {
                x= map[i][j].getX() / SCALE;
                y= map[i][j].getY() / SCALE;

                //N'affiche que les tiles solides, le reste est symbolisé par le fond unifié
                if(map[i][j].getTile_num() > Tile.getSolidNum()) {
                    g.fillRect(x, y, width, height);

                }else if (map[i][j].isExit()){
                    drawExit(map[i][j]);
                }
            }
        }
        //Redonne l'ancienne couleur
        g.setColor(old);

        //Affiche le reste
        this.drawScreen(structure);
        this.drawEntity(structure.getPlayer(), Color.RED);
        this.drawEntity(structure.getBoss(), Color.BLACK);
    }

    /**
     * <p>Dessine la sortie sur la mini-carte.</p>
     * @param tile la tuile de sortie, pour coordonnées et taille.
     */
    private void drawExit(Tile tile) {
        //Le tempo sert à faire clignoter la sortie sur la mini-carte
        if (tempo % 20 != 0)
        {
            Color old = g.getColor();
            g.setColor(Color.BLUE);

            int width = (Tile.getWidth() / SCALE) * 2;
            int height = (Tile.getHeight() / SCALE) * 2;

            g.fillRect(tile.getX() / SCALE, tile.getY() / SCALE, width, height);

            g.setColor(old);
        }
        tempo++;

        //Evite les dépassements mémoires avec un nombre immense
        if (tempo > 100)
            tempo = 0;
    }

    /**
     * <p>Affiche une rectangle sur la mini-map qui simule les dimensions de l'écran.</p>
     * @param structure à afficher
     */
    private void drawScreen(IStructure structure) {
        int x, y, width, height;

        Rectangle screen= structure.getScreen();

        //Gestion de la couleur
        Color old= g.getColor();
        g.setColor(Color.YELLOW);

        //Gestion de la mise à l'echelle
        x= screen.getBeginX() / SCALE;
        y= screen.getBeginY() / SCALE;
        width= screen.getWidth() / SCALE;
        height= screen.getHeight() / SCALE;

        g.drawRect(x, y, width, height);
        g.setColor(old);
    }

    /**
     * <p>Dessine un rectangle sur la mini-map pour simuler un personnage.</p>
     * @param entity personnage à afficher.
     * @param color la couleur à appliquer.
     */
    private void drawEntity(IEntity entity, Color color) {
        if(entity != null) {
            int x, y;

            Color old = g.getColor();

            x = entity.getX() / SCALE;
            y = entity.getY() / SCALE;

            g.setColor(color);
            g.fillRect(x, y, 5, 5);
            g.setColor(old);
        }
    }

    /**
     * <p>Dessine un fond unifié pour simuler le sol du niveau.</p>
     * @param structure à afficher
     */
    private void drawBackground(IStructure structure) {
        int mapWidth, mapHeight;

        Color old= g.getColor();

        mapWidth= structure.getMapWidth() / SCALE;
        mapHeight= structure.getMapHeight() / SCALE;

        g.setColor(Color.GREEN);
        g.fillRect(0, 0, mapWidth, mapHeight);
        g.setColor(old);
    }

}
