package test.com.antoine.services;

import com.antoine.services.Map_reader;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import static org.junit.Assert.*;

public class Map_readerTest {

    String mapPath= "./ressources/maps/levelApple/map.txt";
    String tileSetPath= "./ressources/maps/tileSet.txt";


    /**
     * test de non nullité pour map
     */
    @Test
    public void readMap() {
        int[][] map= Map_reader.readMap(mapPath);
        assertTrue(( map != null ));
    }


    /**
     * test de non nullité pour tileSet
     */
    @Test
    public void readTileSet() {
       HashMap<Integer, BufferedImage> set= Map_reader.readTileSet(tileSetPath);
       assertTrue(( set != null ));
    }


    /**
     * test sur nombre colonne
     */
    @Test
    public void readMap2() {
        int[][] map= Map_reader.readMap(mapPath);
        assertTrue(( map.length == 20 ));
    }


    /**
     * test sur nombre de lignes
     */
    @Test
    public void readMap3() {
        int[][] map= Map_reader.readMap(mapPath);
        assertTrue(( map[0].length == 20 ));
    }


    /**
     * test de valeur d'une cellule
     */
    @Test
    public void readMap4() {
        int[][] map= Map_reader.readMap(mapPath);
        assertTrue(( map[1][0] == 7 ));
    }
}