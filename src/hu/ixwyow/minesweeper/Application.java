package hu.ixwyow.minesweeper;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;

import hu.ixwyow.minesweeper.events.AboutGameAction;
import hu.ixwyow.minesweeper.events.AppWindowAdapter;
import hu.ixwyow.minesweeper.events.ScoreBoardAction;
import hu.ixwyow.minesweeper.logic.Board;
import hu.ixwyow.minesweeper.logic.Board.Difficulty;
import hu.ixwyow.minesweeper.ui.BoardView;

/**
 *  Az alkalmazást reprezentáló osztály. Létrehozza a főablakot, összekapcsolja a Model és a View osztályokat
 */
public class Application {
	/**
	 * Pontszámokat tartalmazó adatfájl neve
	 */
	private static final String scoresFileName = "scores.dat";
	
	/**
	 * Mentett pályát tartalmazó adatfájl neve
	 */
	private static final String savesFileName = "minesweeper.dat";
	
	/**
	 * Singleton példány
	 */
	private static Application _instance;
	
	/**
	 * Példány lekérése
	 * @return Az osztály egyetlen példánya
	 */
	public static Application getInstance() {
		if (_instance == null) {
			_instance = new Application("Minesweeper");
		}
		return _instance;
	}
	
	/**
	 * A program belépési pontja
	 * @param args Parancssori argumentumok
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Application app = Application.getInstance();
			JFrame frame = app.getFrame(); 
			frame.pack();
			frame.setVisible(true);
		});
	}
	
	private JFrame frame;
	private Board gameBoard;
	private BoardView boardView;
	private Map<Difficulty, Integer> scores;
	
	/**
	 * Privát constructor, singleton minta
	 * @param title Az ablak címe
	 */
	private Application(String title) {
		frame = new JFrame(title);
		frame.addWindowListener(new AppWindowAdapter());
		scores = new EnumMap<Difficulty, Integer>(Difficulty.class);
		loadGame();
		initMenu();
		initComponents();	
	}

	/**
	 * Főablak lekérdezése
	 * @return Az alkalmazás fő ablak objektuma.
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * Játék tábla lekérdezése
	 * @return A játék tábla (Board) objektum
	 */
	public Board getGameBoard() {
		return gameBoard;
	}

	/**
	 * Ponttábla lekérdezése
	 * @return
	 */
	public Map<Difficulty, Integer> getScores() {
		return scores;
	}
	
	/**
	 * UI előkészítése
	 */
	public void initComponents() {
		boardView = new BoardView(gameBoard);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(boardView);
	}
	
	/**
	 * Menü előkészítése
	 */
	public void initMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		JMenu helpMenu = new JMenu("Help");
		JMenuItem newGame = new JMenuItem("New");
		JRadioButtonMenuItem beginner = new JRadioButtonMenuItem("Beginner", true);
		JRadioButtonMenuItem intermediate = new JRadioButtonMenuItem("Intermediate");
		JRadioButtonMenuItem expert = new JRadioButtonMenuItem("Expert");
		JMenuItem scoreBoard = new JMenuItem("Scoreboard");
		JMenuItem exitGame = new JMenuItem("Exit");
		JMenuItem about = new JMenuItem("About");
		
		newGame.addActionListener(e -> resetBoardView());
		beginner.addActionListener(e -> {
			gameBoard.setDifficulty(Board.Difficulty.BEGINNER);
			beginner.setSelected(true);
			resetBoardView();
		});
		intermediate.addActionListener(e -> {
			gameBoard.setDifficulty(Board.Difficulty.INTERMEDIATE);
			intermediate.setSelected(true);
			resetBoardView();
		});
		expert.addActionListener(e -> {
			gameBoard.setDifficulty(Board.Difficulty.EXPERT);
			expert.setSelected(true);
			resetBoardView();
		});
		scoreBoard.addActionListener(new ScoreBoardAction());
		exitGame.addActionListener(e -> {
			saveGame();
			frame.dispose();
		});
		about.addActionListener(new AboutGameAction());
		
		gameMenu.setMnemonic(KeyEvent.VK_G);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		newGame.setMnemonic(KeyEvent.VK_N);
		beginner.setMnemonic(KeyEvent.VK_B);
		intermediate.setMnemonic(KeyEvent.VK_I);
		expert.setMnemonic(KeyEvent.VK_E);
		scoreBoard.setMnemonic(KeyEvent.VK_S);
		exitGame.setMnemonic(KeyEvent.VK_X);
		about.setMnemonic(KeyEvent.VK_A);
		
		ButtonGroup group = new ButtonGroup();
		group.add(beginner);
		group.add(intermediate);
		group.add(expert);
		
		gameMenu.add(newGame);
		gameMenu.addSeparator();
		gameMenu.add(beginner);
		gameMenu.add(intermediate);
		gameMenu.add(expert);
		gameMenu.addSeparator();
		gameMenu.add(scoreBoard);
		gameMenu.addSeparator();
		gameMenu.add(exitGame);
		helpMenu.add(about);
		menuBar.add(gameMenu);
		menuBar.add(helpMenu);
		
		frame.setJMenuBar(menuBar);
	}
	
	/**
	 * Mentett játék és pontszámok betöltése
	 */
	public void loadGame() {
		try {
			File f = new File(savesFileName);
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			gameBoard = (Board) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			gameBoard = new Board(Board.Difficulty.BEGINNER);
		}
		try {
			File f = new File(scoresFileName);
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			for (Difficulty d : Difficulty.values()) {
				scores.put(d, (Integer)ois.readObject());
			}
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			scores = new EnumMap<Difficulty, Integer>(Difficulty.class);
			scores.put(Difficulty.BEGINNER, Board.maximumTime);
			scores.put(Difficulty.INTERMEDIATE, Board.maximumTime);
			scores.put(Difficulty.EXPERT, Board.maximumTime);
		}
	}
	
	/**
	 * Tábla nézet frissítése
	 */
	public void resetBoardView() {
		frame.remove(boardView);
		Difficulty d = gameBoard.getDifficulty();
		gameBoard = new Board(d);
		boardView = new BoardView(gameBoard);
		frame.add(boardView, BorderLayout.SOUTH);
		frame.revalidate();
		frame.repaint();
		frame.pack();
	}
	
	/**
	 * Játék és pontszámok mentése
	 */
	public void saveGame() {
		try {
			File f = new File(savesFileName);
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(Application.getInstance().getGameBoard());
			oos.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "Failed to save level.", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		try {
			File f = new File(scoresFileName);
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			for (Difficulty d : Difficulty.values()) {
				oos.writeObject(scores.get(d));
			}
			oos.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "Failed to save scoreboard.", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * Tábla beállítása
	 * @param gameBoard Az alkalmazáshoz hozzárendelendő tábla
	 */
	public void setGameBoard(Board gameBoard) {
		this.gameBoard = gameBoard;
	}

}
