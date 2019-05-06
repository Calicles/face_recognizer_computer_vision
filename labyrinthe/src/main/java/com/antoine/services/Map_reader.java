package com.antoine.services;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * <b>Classe de service, gère les lectures des fichiers pour initialiser une carte d'un niveau.</b>
 * Une carte dans un fichier est sous une forme de Matrice avec un header donnant le nombre de lignes
 * et le nombre de colonnes.
 *
 * @author antoine
 */
public class Map_reader {


	/**
	 * <p>lit la matrice du fichier .txt</p>
	 * @param fileUrl path du fichier
	 * @return la matrice sous forme d'int.
	 */
	public static int[][] readMap(String fileUrl){
		int[][] map= null;
		
		try(BufferedReader reader= createReader(fileUrl)){
			
			String[] line;
			String[] bounds= reader.readLine().split(" ");
			int iMax= Integer.parseInt(bounds[0]);
			int jMax= Integer.parseInt(bounds[1]);
			map= new int[iMax][jMax];
			
			for(int i= 0; i < iMax; i++) {
				
				line= reader.readLine().split(" ");
				for(int j= 0; j < jMax; j++) {
					map[i][j]= Integer.parseInt(line[j]);
				}
			}

		}catch(Throwable t) {t.printStackTrace();}
		
		return map;
	}


	/**
	 * <p>lit l'association numéro/image à partir du fichier .txt</p>
	 * Retourne le "tileSet, association nombre entier / image.
	 * @param fileUrl path du fichier contenant l'association.
	 * @return un tableau associatif de l'association
	 */
	public static HashMap<Integer, BufferedImage> readTileSet(String fileUrl){
		HashMap<Integer, BufferedImage> tileSet= new HashMap<>();
		
		try(BufferedReader reader= createReader(fileUrl)){
			
			BufferedImage image;
			String[] line;
			
			int bounds=  Integer.parseInt(reader.readLine());
			int number;
			
			for(int i= 0; i < bounds; i++) {
				
				line= reader.readLine().split(" ");
					number= Integer.parseInt(line[0]);
					image= ImageReader.lireImage(line[1]);
					tileSet.put(number, image);
					
			}

		}catch(Throwable t) {t.printStackTrace();}
		
		return tileSet;
	}

	/**
	 * <p>Créer un objet BufferedReader.</p>
	 * @param path le path du fichier à lire.
	 * @return le Reader prêt à l'emploi.
	 */
	private static BufferedReader createReader(String path){
		return new BufferedReader(new InputStreamReader(Map_reader.class.getResourceAsStream(path)));
	}

}
