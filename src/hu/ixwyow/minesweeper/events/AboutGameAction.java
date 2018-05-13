package hu.ixwyow.minesweeper.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import hu.ixwyow.minesweeper.Application;

/**
 * A program "About" ablakának megnyitásához tartozó eseményfigyelő
 *
 */
public class AboutGameAction implements ActionListener {

	/**
	 * A program "About" ablakának megnyitásához tartozó eseménykezelő
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		String[] arr = new String[] {
			"Minesweeper v1.0",
			"Készítette: Dániel Árvai",
			"Neptun-kód: IXWYOW",
			"2017"
		};
		JOptionPane.showMessageDialog(Application.getInstance().getFrame(), arr);
	}

}
