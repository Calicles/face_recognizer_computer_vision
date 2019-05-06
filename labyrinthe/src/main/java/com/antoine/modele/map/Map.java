package com.antoine.modele.map;

import com.antoine.contracts.IMap;
import com.antoine.geometry.Coordinates;
import com.antoine.geometry.Rectangle;
import com.antoine.geometry.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <b>Représente une carte standard d'un niveau.</b>
 */
public class Map extends AbstractTileMap implements IMap {
	

	public Map(){
		super();
	}

	/**
	 * @see IMap#getSubMap(Rectangle)
	 * <p>Collecte une sous-partie de la coarte sous forme de liste</p>
	 * @param surface partie à découper
	 * @return la liste des tuiles contenu dans surface
	 */
	@Override
	public List<Tile> getSubMap(Rectangle surface) {

		Predicate<Tile> predicate = t -> {

			boolean inXBounds = ((t.getX() > surface.getBeginX()) && (t.getX() < surface.getEndX())) ||
					((t.getX() + tile_width > surface.getBeginX()) && (t.getX() + tile_width < surface.getEndX()));

			boolean inYBounds = ((t.getY() > surface.getBeginY()) && (t.getY() < surface.getEndY())) ||
					((t.getY() + tile_height > surface.getBeginY()) && (t.getY() + tile_height < surface.getEndY()));

			return inXBounds && inYBounds;
		};

		List<Tile> tilesList= new ArrayList<>();

		for (Tile[] tab: map){

			List<Tile> buffer = Arrays.stream(tab)

					.filter(t-> predicate.test(t))

					.collect(Collectors.toList());

			tilesList.addAll(buffer);
		}

		return tilesList;
	}

	/**
	 * <p>Clone une partie de la carte.</p>
	 * @see IMap#getsubMapInArray(Rectangle)
	 * @param surface sous partie à cloner
	 * @throws IllegalArgumentException si la surface ne convient pas à la taille de la carte
	 */
	@Override
	public Tile[][] getsubMapInArray(Rectangle surface) {


		if (surface.getBeginX() < 0 || surface.getBeginY() < 0 ||
				surface.getEndY() > map.length || surface.getEndX() > map[0].length) {
			throw  new IllegalArgumentException("surface inadaptée à la taille de la map" + surface.getBeginX() +", "+ surface.getEndX()+
					", "+ surface.getBeginY()+", "+ surface.getEndY());
		}

		//Adapte la taille du tableau à la surface voulu
		Tile[][] cloneSubMap = new Tile
				[surface.getHeight()]
				[surface.getWidth()];

		int boundX = surface.getEndX();
		int boundY = surface.getEndY();
		int x = surface.getBeginX();
		int y = surface.getBeginY();

		for (int i = surface.getBeginY(); i < boundY; i++) {

			for (int j = surface.getBeginX(); j < boundX; j++) {

				cloneSubMap[i - y][j - x] = (Tile) map[i][j].clone();
			}
		}

		return cloneSubMap;
	}

	@Override
	public Coordinates getCoorinatesInTile(Coordinates coordinates)
	{
		return new Coordinates(
				coordinates.getX() / tile_width,
				coordinates.getY() / tile_height);
	}

	@Override
	public boolean isSolideTile(int j, int i)
	{
		if (i < 0 || j < 0 || i >= map.length || j >= map[0].length)
			throw new IllegalArgumentException("coordonnées hors map"+" i: "+ i+", j: "+j);

		return map[i][j].isSolid();
	}

}
