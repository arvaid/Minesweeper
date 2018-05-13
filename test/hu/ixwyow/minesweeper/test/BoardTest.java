package hu.ixwyow.minesweeper.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import hu.ixwyow.minesweeper.logic.Board;
import hu.ixwyow.minesweeper.logic.Cell;
import hu.ixwyow.minesweeper.logic.Board.Difficulty;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;

@RunWith(Parameterized.class)
public class BoardTest {

	private Difficulty d;
	private Board board;
	
	@Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {     
        	{Difficulty.BEGINNER}, {Difficulty.INTERMEDIATE}, {Difficulty.EXPERT}
        });
    }
	
	public BoardTest(Difficulty d) {
		this.d = d;
		this.board = new Board(d);
	}
	
	@Test
	public void createBoardTest() {
		Assert.assertEquals(d.cols(), board.getCols());
		Assert.assertEquals(d.rows(), board.getRows());
		Assert.assertEquals(d.bombs(), board.getBombCount());
		Assert.assertEquals(d.bombs(), board.getBombsRemaining());
		Assert.assertFalse(board.isStarted());
		Assert.assertFalse(board.isFinished());
		Assert.assertFalse(board.isPlayerVictory());
	}
	
	@Test
	public void winGameTest() {
		board.finishGame(false);
		Assert.assertTrue(board.isFinished());
		Assert.assertTrue(board.isPlayerVictory());
	}
	
	@Test
	public void loseGameTest() {
		board.finishGame(true);
		Assert.assertTrue(board.isFinished());
		Assert.assertFalse(board.isPlayerVictory());
	}
	
	@Test
	public void flagCellsTest() {
		board.getCell(4, 5).flag();
		Assert.assertTrue(board.getCell(4, 5).isFlagged());
		Assert.assertEquals(d.bombs() - 1, board.getBombsRemaining());
		Assert.assertEquals(1, board.getFlagCount());
	}
	
	@Test
	public void detonateBombTest() {
		Cell cell = board.getCell(0, 0);
		cell.open();
		if (cell.isBomb()) {
			Assert.assertTrue(board.isFinished());
			Assert.assertFalse(board.isPlayerVictory());
		} else {
			Assert.assertFalse(board.isFinished());
		}
	}
	
}
