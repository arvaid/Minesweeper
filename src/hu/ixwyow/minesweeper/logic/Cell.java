package hu.ixwyow.minesweeper.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Egy játékban részt vevő mező, mely aknát tartalmazhat 
 *
 */
public class Cell extends Model implements Serializable {

	private static final long serialVersionUID = 7566541389268237216L;
	private int gridX;
	private int gridY;
	private boolean isOpened;
	private boolean isFlagged;
	private boolean isBomb;
	private boolean isDetonated;
	private Board board;
	
	/**
	 * Mező létrehozása
	 * @param board A tábla, amin a mező van.
	 * @param x Sor koordináta.
	 * @param y Oszlop koordináta.
	 */
	public Cell(Board board, int x, int y) {
		this.gridX = x;
		this.gridY = y;
		this.board = board;
		this.isBomb = false;
		this.isOpened = false;
		this.isFlagged = false;
		this.isDetonated = false;
		this.updateView();
	}
	
	/**
	 * A mező megjelölése zászlóval
	 */
	public void flag() {
		if (!board.isFinished()) {
			board.startGame();
			if (!isFlagged) {
				isFlagged = true;
				board.addFlag(this);
				if (board.checkWinCondition()) {
					board.finishGame(false);
				}
			} else {
				isFlagged = false;
				board.removeFlag(this);
			}
		}
		updateView();
	}
	
	/**
	 * Szomszédos mezők lekérdezése
	 * @return A szomszédos mezők listája
	 */
	public List<Cell> getNeighbors() {
		List<Cell> neighbors = new ArrayList<>();
		int xMin = Math.max(gridX - 1, 0);
		int yMin = Math.max(gridY - 1, 0);
		int xMax = Math.min(gridX + 1, board.getCols() - 1);
		int yMax = Math.min(gridY + 1, board.getRows() - 1);
		for (int x = xMin; x <= xMax; x++) {
			for (int y = yMin; y <= yMax; y++) {
				if (x != gridX || y != gridY)
					neighbors.add(board.getCell(x, y));
			}
		}
					
		return neighbors;
	}
	
	/**
	 * Szomszédos aknák számának lekérdezése
	 * @return A szomszédos aknák száma
	 */
	public int getNeighbourBombCount() {
		return (int)getNeighbors().stream().filter(x->x.isBomb).count();
	}

	/**
	 * Akna lekérdezése
	 * @return Aknát tartalmaz-e a mező
	 */
	public boolean isBomb() { 
		return isBomb;
	}
	
	/**
	 * Felrobbant-e ez a mező, vagyis, ha akna volt, ezt az aknát fedtük-e fel
	 * @return Felrobbant-e ez a mező
	 */
	public boolean isDetonated() {
		return isDetonated;
	}
	
	/**
	 * Zászló lekérdezése
	 * @return Van-e zászló a mezőn
	 */
	public boolean isFlagged() {
		return isFlagged;
	}
	
	/**
	 * Felfedettség lekérdezése
	 * @return Fel van-e fedve a mező
	 */
	public boolean isOpened() {
		return isOpened;
	}
	
	/**
	 * Mező felfedése
	 */
	public void open() {
		if (!board.isFinished()) {
			if (!board.isStarted()) {
				board.startGame();
				if (isBomb) {
					board.moveBomb(gridX, gridY);
				}
			}
			if (!isOpened && !isFlagged) {
				isOpened = true;
				if (!isBomb) {
					if (getNeighbourBombCount() == 0) {
						zeroFill();
					}
					if (board.checkWinCondition()) {
						board.finishGame(false);
					}
				} else {
					this.isDetonated = true;
					board.finishGame(true);
				}
			}
		}
		
		updateView();
	}

	/**
	 * Akna elhelyezése a mezőn
	 * @param isBomb Akna legyen-e a mezőn
	 */
	public void setBomb(boolean isBomb) {
		this.isBomb = isBomb; 
		updateView(); 
	}

	/**
	 * Felfedett állapot változtatása, a játék közbeni ellenőrzések nélkül
	 * @param isOpened Fel legyen-e ezután fedve a mező
	 */
	public void setOpened(boolean isOpened) {
		this.isOpened = isOpened;
		updateView();
	}
	
	/**
	 * Nulla akna szomszéddal rendelkező mezők rekurzív felfedése
	 */
	public void zeroFill() {
		for (Cell c : getNeighbors()) {
			if (c.gridX != gridX || c.gridY != gridY) {
				c.open();
			}
		}
	}
}
