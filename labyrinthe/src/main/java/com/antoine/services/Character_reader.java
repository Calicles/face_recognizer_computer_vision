package com.antoine.services;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * <b>Classe de service, lit le fichier de configuration d'un personnage (sprites d'animation).</b>
 * Fournit le tableau association entre un entier (représente une des 4 directions) et un tableau d'image
 * (représente l'animation dans une direction).
 *
 * Le nombre de direction contenu dans le fichier et le nombre d'image d'animation par direction
 * est dans le header du ficher.
 *
 * @author Antoine
 */
public class Character_reader {
	
	public static HashMap<Integer, BufferedImage[]> readCharactereAnimation(String url){
		HashMap<Integer, BufferedImage[]> map= new HashMap<>();
		
		try(BufferedReader reader= new BufferedReader(
				new InputStreamReader(Character_reader.class.getResourceAsStream(url)))) {
					
			String[] bounds= reader.readLine().split(" ");
			String line;
			int directionNumber= Integer.parseInt(bounds[0]);
			int imagePerDirection= Integer.parseInt(bounds[1]);
			BufferedImage[] tab;
			
			for(int i= 0; i < directionNumber; i++) {
				
				tab= new BufferedImage[imagePerDirection];
				
				for(int j= 0; j < imagePerDirection; j++) {
					line= reader.readLine();
					tab[j]= ImageReader.lireImage(line);
				}
				
				map.put(i, tab);
			}

			
		}catch(Throwable t) {
			t.printStackTrace();
			throw new RuntimeException("Erreur de Lecture d'image animation");
		}

		return map;
	}

}
