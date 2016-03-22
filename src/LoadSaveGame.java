import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javafx.stage.Stage;

/**
 * The function of this module it to store current game state into a text file
 * as well as load current game-state from a game file
 * 
 * @author Pavithran Pathmarajah
 * @version A2
 */
public class LoadSaveGame {
	/**
	 * This constructor is called from the main menu to load current, state of
	 * the game into the Model
	 * 
	 * @param window
	 *            the application game window
	 */
	public LoadSaveGame(Stage window) {
		load(window);
	}

	/**
	 * This constructor is called from the game board to raise the save function
	 * to save the current game state into a file
	 * 
	 * @param window
	 *            The application run window
	 * @param playerTurn
	 *            The users turn
	 */
	public LoadSaveGame(Stage window, int playerTurn) {
		// save game
		save(playerTurn);
		// resetBoard state for a new Game
		Model.reset();
		// go back to main menu
		new Menu().start(window);
	}

	/**
	 * This function takes a data file and pushes it into the model to return
	 * the game state
	 * 
	 * @param window,
	 *            to extrapolate game data too and create an output file
	 */
	private void load(Stage window) {
		// if the data-file does not exist go back to main menu
		try {
			// data from file to be held
			String[] fileIn;
			// open file for reading
			File in = new File("SixMenSave.dat");
			FileReader read = new FileReader(in);
			BufferedReader buffRead = new BufferedReader(read);
			// get the line of data and split is like a csv
			fileIn = buffRead.readLine().split(",");
			// load current player info from file
			int player = Integer.parseInt(fileIn[0]);
			// load the sixteen board positions into the model
			for (int i = 0; i < 16; i++) {
				// offset by one since the first position is dedicated to player
				// turn
				Model.setValue(i, Integer.parseInt(fileIn[i + 1]));
			}
			// load the move history from the file into the model movehistory
			// list
			String inLine = buffRead.readLine();
			// while theirs data in the file
			while (inLine != null) {
				// split the triple by comma and add it to the empty movehistory
				// list via the adder function
				fileIn = inLine.split(",");
				int[] tmp = { Integer.parseInt(fileIn[0]), Integer.parseInt(fileIn[1]), Integer.parseInt(fileIn[2]) };
				Model.trackMoves(tmp);
				// read next line
				inLine = buffRead.readLine();
			}
			// close all file io
			buffRead.close();
			read.close();
			// begin game, from previously saved state
			new Game(window, player);
		} catch (Exception e) {
			new Menu().start(window);
		}
	}

	/**
	 * This function takes in the current window info and pushes the model data
	 * into a dat (data) file, for later game play
	 * 
	 * @param playerTurn
	 *            the current players turn in the game
	 */
	private void save(int playerTurn) {
		// try and catch used to supress warnings
		try {
			// the get the board data to be written
			int board[] = Model.getCurBoard();
			// load/create output file for data writing
			File out = new File("SixMenSave.dat");
			FileWriter write = new FileWriter(out);
			// first piece of data is the current players turn
			write.write(Integer.toString(playerTurn));
			// write the board data as a csv
			for (int i = 0; i < 16; i++) {
				write.write("," + board[i]);
			}
			// get move history from model class to store in the save data
			ArrayList<int[]> movesList = Model.getMoveTrackingList();
			// write out all move history as triples on seperate lines
			for (int i = 0; i < movesList.size(); i++) {
				write.write("\n");
				int[] tmp = movesList.get(i);
				// csv style triples
				write.write(Integer.toString(tmp[0]) + "," + Integer.toString(tmp[1]) + "," + Integer.toString(tmp[2]));
			}
			// close file io
			write.close();
		} catch (Exception e) {
		}
	}
}
