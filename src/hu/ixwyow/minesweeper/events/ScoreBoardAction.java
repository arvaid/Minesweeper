package hu.ixwyow.minesweeper.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JOptionPane;

import hu.ixwyow.minesweeper.Application;
import hu.ixwyow.minesweeper.logic.Board.Difficulty;

/**
 * A ponttábla megnyitásához tartozó eseményfigyelő
 *
 */
public class ScoreBoardAction implements ActionListener {

	/**
	 *  A ponttábla megnyitásához tartozó eseménykezelő
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Map<Difficulty,Integer> scores = Application.getInstance().getScores();
		String[] strScores = new String[] {
				"Beginner: " + scores.get(Difficulty.BEGINNER),
				"Intermediate: " + scores.get(Difficulty.INTERMEDIATE),
				"Expert: " + scores.get(Difficulty.EXPERT)
		};
		JOptionPane.showMessageDialog(Application.getInstance().getFrame(), strScores, "Scores", JOptionPane.INFORMATION_MESSAGE);
	}

}
