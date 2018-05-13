package hu.ixwyow.minesweeper.events;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import hu.ixwyow.minesweeper.Application;

/**
 * Az ablakhoz tartozó eseménykezelőket tartalmazza 
 *
 */
public class AppWindowAdapter extends WindowAdapter {

	/**
	 * Az ablak bezárásához tartozó eseménykezelő
	 */
	@Override
	public void windowClosing(WindowEvent event) {
		Application.getInstance().saveGame();
	}
	
}
