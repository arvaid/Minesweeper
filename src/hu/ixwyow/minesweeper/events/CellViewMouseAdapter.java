package hu.ixwyow.minesweeper.events;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import hu.ixwyow.minesweeper.Application;
import hu.ixwyow.minesweeper.logic.Board;
import hu.ixwyow.minesweeper.ui.CellView;

/**
 * A mezőkre való kattintásokat lekezelő eseménykezelőket tartalmazza 
 *
 */
public class CellViewMouseAdapter extends MouseAdapter {

	/**
	 * A mező fölötti, bal egérgomb lenyomásához tartozó eseménykezelő
	 */
	@Override
	public void mousePressed(MouseEvent event) {
		if (SwingUtilities.isLeftMouseButton(event)) {
			Board board = Application.getInstance().getGameBoard();
			board.setSmileyScreamingWithFear(true);
		}
	}
	
	/**
	 * A mező fölötti, jobb egérgomb lenyomásához tartozó eseménykezelő
	 */
	@Override
    public void mouseReleased(MouseEvent event) {
    	CellView c = (CellView)event.getSource();
    	if (SwingUtilities.isLeftMouseButton(event)) {
    		Board board = Application.getInstance().getGameBoard();
    		board.setSmileyScreamingWithFear(false);
			c.leftClick();
		} else {
			c.rightClick();
		}
    }
}
