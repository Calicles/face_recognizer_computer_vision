package com.antoine.entity;

import com.antoine.services.ImageReader;

import java.awt.image.BufferedImage;

/**
 * Classe gérant une image 2D et ses dimensions.
 * @author antoine
 */
public abstract class AbstractImage {

	/**
	 * L'image pour affichage
	 */
	protected BufferedImage image;

	protected AbstractImage(){}

	public void setImage(String imageUrl) {
		image= ImageReader.lireImage(imageUrl);
	}

	public int getWidth() {return image.getWidth();}
	public int getHeight() {return image.getHeight();}
	public BufferedImage getImage() {return image;}

}
