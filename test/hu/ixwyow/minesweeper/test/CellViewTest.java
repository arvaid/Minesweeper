package hu.ixwyow.minesweeper.test;


import org.junit.Before;
import org.junit.Test;

import hu.ixwyow.minesweeper.logic.Board;
import hu.ixwyow.minesweeper.logic.Board.Difficulty;
import hu.ixwyow.minesweeper.logic.Cell;
import hu.ixwyow.minesweeper.ui.CellView;

import org.junit.Assert;

public class CellViewTest {
	private Board board;
	private Cell cell;
	private CellView cv;

	@Before
	public void setUp() {
		board = new Board(Difficulty.BEGINNER);
		cell = board.getCell(0, 0);
		cv = new CellView(cell);
	}
	
	@Test
	public void flagCellViewTest() {
		cell.flag();
		Assert.assertSame("F", cv.getText());
	}
}
