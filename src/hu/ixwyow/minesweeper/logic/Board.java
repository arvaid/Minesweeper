package hu.ixwyow.minesweeper.logic;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import hu.ixwyow.minesweeper.Application;

/**
 * A tábla mely a mezőket tartalmazza, egyben a játék állapota
 *
 */
public class Board extends Model implements Serializable {
	/**
	 * A nehézségi szinteket tartalmazó felsorolás 
	 *
	 */
	public static enum Difficulty {
		BEGINNER (8, 8, 10),
		INTERMEDIATE (16, 16, 40),
		EXPERT(16, 30, 99);
		
		private final int cols, rows, bombs;
		Difficulty(int cols, int rows, int bombs) {
			this.cols = cols;
			this.rows = rows;
			this.bombs = bombs;
		}
		public int bombs() { return bombs; }
		public int cols() { return cols; }
		public int rows() { return rows; }
		
	}
	
	/**
	 * Maximális idő. (másodperc) Ha letelik, a játékos automatikusan veszít
	 */
	public static final Integer maximumTime = 999;
	static final long serialVersionUID = 906505627072910693L;
	Difficulty difficulty;
	boolean isStarted;
	boolean isFinished;
	boolean isPlayerVictory;
	Cell[][] cells;
	Cell[] bombs;
	Set<Cell> flags;
	int time;
	boolean isSmileyScreamingWithFear;
	transient GameTimer timer;
	
	/**
	 * Tábla létrehozása
	 * @param difficulty A választott nehézségi szint
	 */
	public Board(Difficulty difficulty) {
		int cols = difficulty.cols;
		int rows = difficulty.rows;
		int bombs = difficulty.bombs;
		
		this.difficulty = difficulty;
		this.timer = new GameTimer(this);
		this.bombs = new Cell[bombs];
		this.cells = new Cell[cols][rows];
		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				cells[x][y] = new Cell(this, x, y);
			}
		}
		this.time = 0;
		this.flags = new HashSet<Cell>();
		this.isStarted = false;
		this.isFinished = false;
		this.isPlayerVictory = false;
		
		Random rn = new Random();
		for (int i = 0; i < bombs; i++) {
			int x, y;
			do {
				x = rn.nextInt(getCols());
				y = rn.nextInt(getRows());
			} while (cells[x][y].isBomb());
			if (!cells[x][y].isBomb())
				cells[x][y].setBomb(true);
			this.bombs[i] = cells[x][y];
			
		}
		updateView();
	}
	
	
	/**
	 * Zászló hozzáadása
	 * @param cell A mező, amire zászlót teszünk
	 */
	void addFlag(Cell cell) {
		flags.add(cell);
		updateView();
	}

	/**
	 * 
	 * @return Nyert-e a játékos a legutóbbi lépésével
	 */
	public boolean checkWinCondition() {
		int allCells = getCols() * getRows();
		int unOpened = getOpenedCellCount();
		boolean win = (unOpened == (allCells - bombs.length));
		
		if (win) {
			for (Cell c : bombs) {
				if (!c.isFlagged()) {
					c.flag();
				}
			}
		}
		
		return win;
	}
	
	/**
	 * Játék befejezése
	 * @param hasPlayerLost A játékos veszített-e
	 */
	public void finishGame(boolean hasPlayerLost) {
		isFinished = true;
		this.isPlayerVictory = !hasPlayerLost;
		if (hasPlayerLost) {
			for (Cell c : bombs) {
				if (!c.isFlagged()) {
					c.setOpened(true);
				}
			}
			for (Cell c : flags) {
				c.updateView();
			}
		} else {
			Map<Difficulty, Integer> scores = Application.getInstance().getScores();
			if (!scores.containsKey(difficulty) || scores.get(difficulty) > time) {
				scores.put(difficulty, time);
			}
		}
		updateView();
	}

	/**
	 * Aknák számának lekérdezése
	 * @return Az aknák száma
	 */
	public int getBombCount() { 
		return bombs.length;
	}
	
	/**
	 * Felfedetlen aknák számának lekérdezése
	 * @return A felfedetlen aknák száma
	 */
	public int getBombsRemaining() { 
		return (getBombCount() - getFlagCount());
	}
	
	/**
	 * Mező lekérdezése
	 * @param x Oszlop koordináta
	 * @param y Sor koordináta
	 * @return Az (x,y) koordinátájú mező
	 */
	public Cell getCell(int x, int y) { 
		return cells[x][y];
	}
	
	/**
	 * Oszlopszám lekérdezése
	 * @return Oszlopok száma
	 */
	public int getCols() { 
		return cells.length;
	}
	
	/**
	 * Nehézség lekérdezése
	 * @return A nehézségi szintet reprezentáló enum érték
	 */
	public Difficulty getDifficulty() {
		return difficulty;
	}
	
	/**
	 * Zászlók számának lekérdezése
	 * @return Zászlók száma
	 */
	public int getFlagCount() { 
		return flags.size();
	}
	
	/**
	 * Felfedetlen mezők számának lekérdezése
	 * @return Felfedetlen mezők száma
	 */
	public int getOpenedCellCount() {
		int count = 0;
		for (int x = 0; x < getCols(); x++) {
			for (int y = 0; y < getRows(); y++) {
				if (cells[x][y].isOpened())
					count++;
			}
		}
		return count;
	}
	
	/**
	 * Sorok számának lekérdezése
	 * @return Sorok száma
	 */
	public int getRows() {
		return cells[0].length;
	}
	
	/**
	 * Eltelt idő lekérdezése
	 * @return Az eltelt idő
	 */
	public int getTime() { 
		return time;
	}
	
	/**
	 * Játék végállapotának lekérdezése
	 * @return Véget ért-e a játék
	 */
	public boolean isFinished() {
		return isFinished;
	}
	
	/**
	 * 
	 * @return Nyert-e a játékos
	 */
	public boolean isPlayerVictory() {
		return isPlayerVictory;
	}

	/**
	 *  A síkító smiley-t kell-e kirajzolni
	 * @return A síkító smiley-t kell-e kirajzolni
	 */
	// TODO: if possible, move this method to BoardView, as it's unrelated to the model
	public boolean isSmileyScreamingWithFear() {
		return isSmileyScreamingWithFear;
	}
	
	/**
	 * Elkezdődött-e a játék
	 * @return elkezdődött-e a játék
	 */
	public boolean isStarted() {
		return isStarted;
	}
		
	/**
	 * Akna áthelyezése
	 * @param x Az akna eredeti oszlopa
	 * @param y Az akna eredeti sora
	 */
	void moveBomb(int x, int y) {
		boolean ready = false;
		for (int xx = 0; xx < getCols() && !ready; xx++) {
			for (int yy = 0; yy < getRows() && !ready; yy++) {
				Cell cell = cells[xx][yy];
				if (!cell.isBomb() && (xx != x) || (yy != y)) {
					cell.setBomb(true);
					ready = true;
				}
			}
		}
		cells[x][y].setBomb(false);
	}
	
	/**
	 * Deszerializáció esetén is inicializálni kell a transient tagokat 
	 * @param in A beolvasott objektum forrása
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		timer = new GameTimer(this);
	}
	
	/**
	 * Zászló levétele
	 * @param cell A mező, ahonnan elvesszük a zászlót
	 */
	void removeFlag(Cell cell) {
		flags.remove(cell);
		updateView(); 
	}
	
	/**
	 * Nehézség beállítása
	 * @param difficulty Az új nehézség
	 */
	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	/**
	 * Sikító smiley beállítása
	 * @param isSmileyScreamingWithFear Sikít-e a smiley
	 */
	public void setSmileyScreamingWithFear(boolean isSmileyScreamingWithFear) {
		this.isSmileyScreamingWithFear = isSmileyScreamingWithFear;
		updateView();
	}

	/**
	 * Játék elindítása
	 */
	public void startGame() {
		if (!isStarted) {
			isStarted = true;
		}
		updateView();
	}
	
	/**
	 * Idő léptetése
	 */
	public void tick() {
		if (time < maximumTime) {
			time++;
		} else {
			finishGame(true);
		}
		updateView(); 
	}
	
}
