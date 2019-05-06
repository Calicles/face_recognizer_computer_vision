package com.antoine.contracts;

import com.antoine.events.LevelChangeEvent;

/**
 * <b>Représente une classe qui doit être précenu en cas de changement dans un niveau.</b>
 *
 * @author Antoine
 */
public interface LevelListener {

	/**
	 * <p>Se met à jour vis à vis du changement</p>
	 * @param lve l'évènement contenant des information pour guider la mise à jour.
	 */
	void update(LevelChangeEvent lve);
}
