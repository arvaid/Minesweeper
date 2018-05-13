package hu.ixwyow.minesweeper.ui;

import java.awt.*;

import javax.swing.JToggleButton;

import hu.ixwyow.minesweeper.Application;
import hu.ixwyow.minesweeper.events.CellViewMouseAdapter;
import hu.ixwyow.minesweeper.logic.Cell;

/**
 * Nézet egy mező megjelenítéséhez 
 *
 */
@SuppressWarnings("serial")
public class CellView extends JToggleButton implements View {
	/**
	 * Megjelenített méret pixelben (szélesség és magasság is)
	 */
	public static final int SIZE = 30;
	
	private static final Color[] colors = {
		Color.black,
		Color.BLUE,
		new Color(0, 128, 0),
		Color.RED,
		Color.MAGENTA,
		Color.CYAN,
		Color.ORANGE,
		Color.LIGHT_GRAY,
		Color.PINK
	};
	private Cell cell;
	
	/**
	 * Mező nézet létrehozása
	 * @param cell A megjelenítendő mező
	 */
	public CellView(Cell cell) {
		this.cell = cell;
		this.cell.setView(this);
		this.setMargin(new Insets(0, 0, 0, 0));
		this.addMouseListener(new CellViewMouseAdapter());
		this.setFont(new Font("Consolas", Font.BOLD, 16));
		this.setPreferredSize(new Dimension(CellView.SIZE, CellView.SIZE));
	}

	/**
	 * Bal gombos kattintás kezelése
	 */
	public void leftClick() {
		cell.open();
	}
	
	/**
	 * Jobb gombos kattintás kezelése
	 */
	public void rightClick() {
		cell.flag();
	}

	/**
	 * Mező nézet frissítése
	 */
	@Override
	public void update() {
		this.setSelected(!cell.isOpened());
		if (cell.isOpened()) {
			this.setBackground(Color.WHITE);
			int neighbors = cell.getNeighbourBombCount();
			if (cell.isBomb()) {
				this.setForeground(Color.BLACK);
				if (cell.isDetonated()) {
					this.setBackground(Color.RED);
					this.setForeground(Color.WHITE);
				}
				
				this.setText("X");
			}
			else if (neighbors > 0) {
				this.setForeground(colors[neighbors]);
				this.setText(Integer.toString(neighbors));
			}

		} else if (cell.isFlagged()) {
			this.setForeground(Color.BLACK);
			if (Application.getInstance().getGameBoard().isFinished()) {
				if (!cell.isBomb()) {
					this.setForeground(Color.RED);
				}
			}
			this.setText("F");
		} else {
			this.setText("");
		}
	}
}
