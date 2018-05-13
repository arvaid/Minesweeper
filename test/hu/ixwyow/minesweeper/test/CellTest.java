package hu.ixwyow.minesweeper.test;

import org.junit.Before;
import org.junit.Test;

import hu.ixwyow.minesweeper.logic.Board;
import hu.ixwyow.minesweeper.logic.Board.Difficulty;
import hu.ixwyow.minesweeper.logic.Cell;
import org.junit.Assert;

public class CellTest {
	private Board board;
	
	@Before
	public void setUp() {
		board = new Board(Difficulty.BEGINNER);
	}
	
	
	@Test
	public void neighborTest() {
		Cell cell = board.getCell(1, 1);
		Assert.assertNotNull(cell.getNeighbors());
		Assert.assertEquals(8, cell.getNeighbors().size());
		cell = board.getCell(0, 0);
		Assert.assertNotNull(cell.getNeighbors());
		Assert.assertEquals(3, cell.getNeighbors().size());
		cell = board.getCell(1, 0);
		Assert.assertNotNull(cell.getNeighbors());
		Assert.assertEquals(5, cell.getNeighbors().size());
	}
	
	@Test
	public void openCellTest() {
		Cell cell = board.getCell(0, 0);
		cell.open();
		if (cell.isBomb()) {
			
		} else {
			
		}
	}
	
		
}
