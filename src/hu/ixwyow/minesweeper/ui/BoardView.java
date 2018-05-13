package hu.ixwyow.minesweeper.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import hu.ixwyow.minesweeper.Application;
import hu.ixwyow.minesweeper.logic.Board;

/**
 * Nézet a tábla megjelenítéséhez 
 *
 */
@SuppressWarnings("serial")
public class BoardView extends JPanel implements View {

	final Color greenColor = new Color(0, 150, 136);
	Board board;
	JPanel infoPanel, gridPanel;
	JLabel mines, time;
	JButton button;
	
	/**
	 * Tábla nézet létrehozása
	 * @param board A megjelenítendő tábla
	 */
	public BoardView(Board board) {
		mines = new JLabel();
		mines.setFont(new Font(mines.getFont().getName(), Font.PLAIN, 24));
		
		time = new JLabel();
		time.setFont(new Font(time.getFont().getName(), Font.PLAIN, 24));
		
		button = new JButton(":)");
		button.setBackground(Color.WHITE);
		button.addActionListener(e -> Application.getInstance().resetBoardView());
		
		infoPanel = new JPanel();
		infoPanel.add(mines);
		infoPanel.add(button);
		infoPanel.add(time);
		
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(board.getCols(), board.getRows()));
		
		
		this.board = board;
		this.board.setView(this);
		for (int x = 0; x < board.getCols(); x++) {
			for (int y = 0; y < board.getRows(); y++) {
				gridPanel.add(new CellView(board.getCell(x, y)));
			}
		}
		
		this.setLayout(new BorderLayout());
		this.add(infoPanel, BorderLayout.NORTH);
		this.add(gridPanel, BorderLayout.CENTER);
	}
	
	
	/**
	 * Tábla nézet frissítése
	 */
	@Override
	public void update() {
		boolean lose = (board.isFinished() && !board.isPlayerVictory());
		infoPanel.setBackground(lose ? Color.RED : greenColor);
		mines.setText(Integer.toString(board.getBombsRemaining()));
		time.setText(String.format("%03d", board.getTime()));
		if (!board.isFinished()) {
			button.setText(board.isSmileyScreamingWithFear() ? ":O" : ":)");
		} else if (board.isPlayerVictory()) {
			button.setText("B)");
		} else {
			button.setText(":(");
		}
	}

}
