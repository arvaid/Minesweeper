package hu.ixwyow.minesweeper.logic;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A játékhoz felhasznált időzítő osztály 
 *
 */
public class GameTimer extends TimerTask {
	
	private Timer timer;
	private Board gameBoard;
	
	/**
	 * Időzítő létrehozása
	 * @param gameBoard
	 */
	public GameTimer(Board gameBoard) {
		this.gameBoard = gameBoard;
		this.timer = new Timer();
		timer.scheduleAtFixedRate(this, 0, 1000);
	}
	
	/**
	 * Minden megadott időközben végrehajtja a megadott utasítást
	 */
	@Override
	public void run() {
		if (gameBoard.isStarted() && !gameBoard.isFinished()) {
			gameBoard.tick();
		}
	}
}
