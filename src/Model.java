import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class acts as the model module in the MVC architecture
 * 
 * @author Emily Ashworth, Pavithran Pathmarajah, Ziyi Jin
 * @version A2
 */
public class Model {

	// keep track of disk status on board 0 = none, 1 =red, 2 = blue
	private static int boardPositions[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	// takes in arrays of 3 to keep track of previous moved, to check for cycles
	// and thus draws
	private static ArrayList<int[]> moveHistory = new ArrayList();
	private static int error = 0;
	
	/****************************************************************************************
	 * *****************                        AI                          *************** *
	 ***************************************************************************************/
	//keep track of what positions are connected which positions
	private static int positionAdjacent[][]={{1,6},{0,2,4},{1,9},{4,7},{1,3,5},{4,8},{0,7,13},{3,6,10},{5,9,12},{2,8,15},{7,11},{10,12,14},{8,11},{6,14},{11,13,15},{9,14}};
		
	//to map each position to a winpath
	private static Map<Integer,Map<Integer,Integer>> winPath = new HashMap<Integer,Map<Integer,Integer>>();

	/**
	 * This function returns the most recent error that was raised
	 * 
	 * @return the integer value corresponding to the last error that was raised
	 *         as
	 */
	public static int getError() {
		return error;
	}

	/**
	 * This function returns the entirety of the board positions as requested
	 * 
	 * @return the boardPositions array that holds all the position values
	 */
	public static int[] getCurBoard() {
		return boardPositions;
	}

	/**
	 * This function returns the entirety of the board positions as requested
	 * Also restes the move history back to blank
	 * 
	 * @return the boardPositions array that holds all the position values
	 */
	public static void reset() {
		for (int i = 0; i < 16; i++) {
			boardPositions[i] = 0;
		}
		moveHistory = new ArrayList();
	}

	/**
	 * Resets the move history list to a fresh one, called on disk removal since
	 * anything prior will not be considered in a cycle check for a draw
	 */
	public static void moveTrackReset() {
		moveHistory = new ArrayList();
	}

	/**
	 * getter for the history list so that, the save game can keep track of
	 * possible cyclic patterns
	 * 
	 * @return the moveHistory list
	 */
	public static ArrayList<int[]> getMoveTrackingList() {
		return moveHistory;
	}

	/**
	 * This method is used to find the value at any given position in the board
	 * 
	 * @param pos
	 *            the location in the array to be checked
	 * @return the value at that position on the board
	 */
	public static int valueAt(int pos) {
		return boardPositions[pos];
	}

	/**
	 * This method is used when placing pieces to check if pieces will be
	 * overlapped
	 * 
	 * @param pos
	 *            the location in the array to be checked
	 * @return a boolean stating if a piece is there or not
	 */
	public static boolean hasValue(int pos) {
		return !(boardPositions[pos] == 0);
	}

	/**
	 * This method is to allow other classes to change values of the display
	 * board
	 * 
	 * @param pos
	 *            the location in the array to be changed
	 * @param val
	 *            This is the value to change said location on the board too
	 */
	public static void setValue(int pos, int val) {
		boardPositions[pos] = val;
	}

	/**
	 * this function takes in a triple and pushes it to the list of move history
	 * 
	 * @param move the move that was just made as a triple {player, old position, new position}
	 */
	public static void trackMoves(int[] move) {
		moveHistory.add(move);
	}

	/**
	 * The isPossiblePlace method calculates if it is possible for the player to
	 * place a piece in a location.
	 * 
	 * @param int[]
	 *            a the array with postitions of the board.
	 * @param int
	 *            p The location the user wants to put the piece in.
	 * @return a boolean stating if a piece can be placed in a specified
	 *         position.
	 */
	public static boolean isPossiblePlace(int[] a, int p) {
		// Checks if that location is already occupied.
		// Then prints an error message and returns
		// false if the space is occupied.

		if (a[p] == 1 || a[p] == 2) {
			return false;
		}
		return true; // Otherwise the space is free, and the player can place
						// their piece in that location so true is returned.
	}

	/**
	 * The isPossibleBoard checks if a board is legal and playable or not.
	 * 
	 * @return A boolean stating if a board is playable.
	 */
	public static boolean isPossibleBoard() {
		int redCounter = 0; // A counter is set for the number of red pieces.
		int blueCounter = 0; // And a counter for the number of blue pieces.
		error = 0;

		for (int i = 0; i < boardPositions.length; i++) { // Then this for loop
															// counts both
			// the red and blue pieces.
			if (boardPositions[i] == 1) {
				redCounter++;
			} else if (boardPositions[i] == 2) {
				blueCounter++;
			}
		}

		if (redCounter <= 2 || blueCounter <= 2) { // The game requires each
													// player has at least 3
													// pieces on the board, so
													// if there isn't an error
													// is created and
													// false is returned.
			error = 1;
			return false;
		} else if (redCounter > 6 || blueCounter > 6) { // On the other hand the
														// maximum is 6 pieces
														// per player, if more
														// pieces are placed, an
														// error is created
														// and false
														// is returned.
			error = 2;
			return false;
		}

		if (isBlocked(boardPositions, 1)) { // This uses the
											// isBlocked function
			// to check if the Red player is
			// blocked, if so
			error = 3; // Error is created
			return false; // False returned.
		} else if (isBlocked(boardPositions, 2)) { // Same as
													// above.
			error = 4;
			return false;
		}

		return true; // Otherwise board is legal and playable, true is returned.
	}

	/**
	 * The isBlocked function checks if a player is blocked, that is they have
	 * no pieces capable of legal moves.
	 * 
	 * @param int[]
	 *            boardPositions This is the array with the board positions.
	 * @param int
	 *            player Indicates Player 1 or Player 2
	 * @return A boolean stating whether a player is blocked or not.
	 */
	public static boolean isBlocked(int[] boardPositions, int player) { // It
		// takes in
		// the
		// array,
		// player
		// number,
		// and
		// the
		// number
		// of
		// pieces
		// the
		// player
		// has
		// as
		// input.

		// For each position where the player has a piece, the neighbouring
		// locations are checked for pieces. If any piece's surrounding
		// locations are not
		// filled.That player is not blocked.Return false.
		if (boardPositions[0] == player) {
			if (boardPositions[1] == 0 || boardPositions[6] == 0)
				return false;
		}
		if (boardPositions[1] == player) {
			if (boardPositions[0] == 0 || boardPositions[2] == 0 || boardPositions[4] == 0)
				return false;
		}
		if (boardPositions[2] == player) {
			if (boardPositions[1] == 0 || boardPositions[9] == 0)
				return false;
		}
		if (boardPositions[3] == player) {
			if (boardPositions[4] == 0 || boardPositions[7] == 0)
				return false;
		}
		if (boardPositions[4] == player) {
			if (boardPositions[1] == 0 || boardPositions[3] == 0 || boardPositions[5] == 0)
				return false;
		}
		if (boardPositions[5] == player) {
			if (boardPositions[4] == 0 || boardPositions[8] == 0)
				return false;
		}
		if (boardPositions[6] == player) {
			if (boardPositions[0] == 0 || boardPositions[7] == 0 || boardPositions[13] == 0)
				return false;
		}
		if (boardPositions[7] == player) {
			if (boardPositions[3] == 0 || boardPositions[6] == 0 || boardPositions[10] == 0)
				return false;
		}
		if (boardPositions[8] == player) {
			if (boardPositions[5] == 0 || boardPositions[9] == 0 && boardPositions[12] == 0)
				return false;
		}
		if (boardPositions[9] == player) {
			if (boardPositions[2] == 0 || boardPositions[8] == 0 || boardPositions[15] == 0)
				return false;
		}
		if (boardPositions[10] == player) {
			if (boardPositions[7] == 0 || boardPositions[11] == 0)
				return false;
		}
		if (boardPositions[11] == player) {
			if (boardPositions[10] == 0 || boardPositions[12] == 0 || boardPositions[14] == 0)
				return false;
		}
		if (boardPositions[12] == player) {
			if (boardPositions[8] == 0 || boardPositions[11] == 0)
				return false;
		}
		if (boardPositions[13] == player) {
			if (boardPositions[6] == 0 || boardPositions[14] == 0)
				return false;
		}
		if (boardPositions[14] == player) {
			if (boardPositions[11] == 0 || boardPositions[13] == 0 || boardPositions[15] == 0)
				return false;
		}
		if (boardPositions[15] == player) {
			if (boardPositions[9] == 0 || boardPositions[14] == 0)
				return false;
		}

		return true; // Otherwise player is blocked and true is returned.

	}

	/**
	 * check if a player win the game.
	 * 
	 * @return return an int value representing who wins. 0 for no one wins,1
	 *         for red wins,2 for blue wins
	 */

	// we only need to check who wins the game after placing all the dices.
	public static int checkWin(int mover) {

		// check dices numbers
		int numberOfRed = 0; // number of red on board after
		int numberOfBlue = 0; // number of blue on board
		for (int i = 0; i < boardPositions.length; i++) {
			if (boardPositions[i] == 1)
				numberOfRed++;
			else if (boardPositions[i] == 2)
				numberOfBlue++;
		}
		if (numberOfRed <= 2)
			return 2;// blue wins
		if (numberOfBlue <= 2)
			return 1;// red wins

		// check if a player is blocked
		if (isBlocked(boardPositions, 1) && mover == 1)
			return 2; // red is blocked, blue wins,return 2.
		if (isBlocked(boardPositions, 2) && mover == 2)
			return 1; // blue is blocked, red wins,return 1.

		// check if there is a draw
		if (checkDraw())
			return 3;

		return 0; // no one wins
	}

	/**
	 * Six men-morris a draw is established if cyclical game play occurs, with a cycle size of 3
	 */
	private static boolean checkDraw() {
		/*
		 * the method finds a pattern by looking through all moves since the start of the game or since the last mill,
		 * a pattern is identified if a point in the moveHistory list (i) and a point that is a specfic distance (sampleSize) away all match up.
		 * Proving that a cycle occured over that (sample size) the length of that cycle being of (sampleSize)
		 */
		//the sample size, since a cycle must occur 3 times, the max smaple size is 1/3 of the history of moves made
		for (int sampleSize = 2; sampleSize <= (moveHistory.size() / 3); sampleSize += 2) {
			//since list uses fifo, we move forward intime looking for cycles
			for (int startPos = 0; (moveHistory.size() - startPos) >= 3 * sampleSize; startPos++) {
				//a draw is considered to occur until proven that this smaple size dos not yield 3 cycles
				boolean draw = true;
				// look through this sample area for three cycles
				for (int i = startPos; ((i + 2 * sampleSize) < moveHistory.size()) && (i < startPos + sampleSize)  ; i++) {
					//cycle one info triple (current most recent info)
					int[] curCycle = moveHistory.get(i+ 2 * sampleSize);
					//cycle two info triple
					int[] pastCycle1 = moveHistory.get(i + sampleSize);
					//cycle three info triple
					int[] pastCycle2 = moveHistory.get(i);
					//check if all three pieces of info {move, oldpos ,new pos} line up across all three cycles, if so the cycle is not a cycle
					if (curCycle[0] != pastCycle1[0] || curCycle[1] != pastCycle1[1] || curCycle[2] != pastCycle1[2]
							|| curCycle[0] != pastCycle2[0] || curCycle[1] != pastCycle2[1]
							|| curCycle[2] != pastCycle2[2])
						draw = false;
				}
				//if a draw is found return true a draw is found
				if (draw) {
					return true;
				}
			}
		}
		//after an exhastive search no draw was found
		return false;
	}

	/**
	 * This function checks if the current player can perform the eat step.
	 * 
	 * @param currentmove
	 *            the player that is making the current move
	 * @param position
	 *            the position of the dice selected by the current player moves
	 *            to
	 * @return return true if the current user can eat the opponet's dice
	 */
	// board position start form 0 to 15
	public static boolean canEat(int currentmove, int position) {
		if (position == 0) {
			if (boardPositions[1] == currentmove && boardPositions[2] == currentmove)
				return true;
			else if (boardPositions[6] == currentmove && boardPositions[13] == currentmove)
				return true;
		}
		if (position == 1) {
			if (boardPositions[0] == currentmove && boardPositions[2] == currentmove)
				return true;
		}
		if (position == 2) {
			if (boardPositions[1] == currentmove && boardPositions[0] == currentmove)
				return true;
			else if (boardPositions[9] == currentmove && boardPositions[15] == currentmove)
				return true;
		}
		if (position == 3) {
			if (boardPositions[4] == currentmove && boardPositions[5] == currentmove)
				return true;
			else if (boardPositions[7] == currentmove && boardPositions[10] == currentmove)
				return true;
		}
		if (position == 4) {
			if (boardPositions[3] == currentmove && boardPositions[5] == currentmove)
				return true;
		}
		if (position == 5) {
			if (boardPositions[3] == currentmove && boardPositions[4] == currentmove)
				return true;
			else if (boardPositions[8] == currentmove && boardPositions[12] == currentmove)
				return true;
		}
		if (position == 6) {
			if (boardPositions[0] == currentmove && boardPositions[13] == currentmove)
				return true;

		}
		if (position == 7) {
			if (boardPositions[3] == currentmove && boardPositions[10] == currentmove)
				return true;

		}
		if (position == 8) {
			if (boardPositions[5] == currentmove && boardPositions[12] == currentmove)
				return true;

		}
		if (position == 9) {
			if (boardPositions[2] == currentmove && boardPositions[15] == currentmove)
				return true;

		}
		if (position == 10) {
			if (boardPositions[3] == currentmove && boardPositions[7] == currentmove)
				return true;
			else if (boardPositions[11] == currentmove && boardPositions[12] == currentmove)
				return true;

		}
		if (position == 11) {
			if (boardPositions[10] == currentmove && boardPositions[12] == currentmove)
				return true;

		}

		if (position == 12) {
			if (boardPositions[10] == currentmove && boardPositions[11] == currentmove)
				return true;
			else if (boardPositions[5] == currentmove && boardPositions[8] == currentmove)
				return true;
		}

		if (position == 13) {
			if (boardPositions[0] == currentmove && boardPositions[6] == currentmove)
				return true;
			else if (boardPositions[14] == currentmove && boardPositions[15] == currentmove)
				return true;
		}
		if (position == 14) {
			if (boardPositions[13] == currentmove && boardPositions[15] == currentmove)
				return true;

		}
		if (position == 15) {
			if (boardPositions[13] == currentmove && boardPositions[14] == currentmove)
				return true;
			else if (boardPositions[2] == currentmove && boardPositions[9] == currentmove)
				return true;
		}

		return false;
	}

	/**
	 * This function checks if the a dice on the specific board position can be
	 * ate
	 * 
	 * @param position
	 *            the position of the dice that the current player want to eat
	 * @param DiceCoulor
	 *            the color of the dice that the current player want to eat
	 * @return return true if the selected dice can be ate and false otherwise
	 */
	public static boolean canBeAte(int position, int DiceCoulor) {
		if (position == 0) {
			if (boardPositions[1] == DiceCoulor && boardPositions[2] == DiceCoulor)
				return false;
			else if (boardPositions[6] == DiceCoulor && boardPositions[13] == DiceCoulor)
				return false;
		}
		if (position == 1) {
			if (boardPositions[0] == DiceCoulor && boardPositions[2] == DiceCoulor)
				return false;
		}
		if (position == 2) {
			if (boardPositions[1] == DiceCoulor && boardPositions[0] == DiceCoulor)
				return false;
			else if (boardPositions[9] == DiceCoulor && boardPositions[15] == DiceCoulor)
				return false;
		}
		if (position == 3) {
			if (boardPositions[4] == DiceCoulor && boardPositions[5] == DiceCoulor)
				return true;
			else if (boardPositions[7] == DiceCoulor && boardPositions[10] == DiceCoulor)
				return false;
		}
		if (position == 4) {
			if (boardPositions[3] == DiceCoulor && boardPositions[5] == DiceCoulor)
				return false;
		}
		if (position == 5) {
			if (boardPositions[3] == DiceCoulor && boardPositions[4] == DiceCoulor)
				return false;
			else if (boardPositions[8] == DiceCoulor && boardPositions[12] == DiceCoulor)
				return false;
		}
		if (position == 6) {
			if (boardPositions[0] == DiceCoulor && boardPositions[13] == DiceCoulor)
				return false;

		}
		if (position == 7) {
			if (boardPositions[3] == DiceCoulor && boardPositions[10] == DiceCoulor)
				return false;

		}
		if (position == 8) {
			if (boardPositions[5] == DiceCoulor && boardPositions[12] == DiceCoulor)
				return false;

		}
		if (position == 9) {
			if (boardPositions[2] == DiceCoulor && boardPositions[15] == DiceCoulor)
				return false;

		}
		if (position == 10) {
			if (boardPositions[3] == DiceCoulor && boardPositions[7] == DiceCoulor)
				return false;
			else if (boardPositions[11] == DiceCoulor && boardPositions[12] == DiceCoulor)
				return false;

		}
		if (position == 11) {
			if (boardPositions[10] == DiceCoulor && boardPositions[12] == DiceCoulor)
				return false;

		}

		if (position == 12) {
			if (boardPositions[10] == DiceCoulor && boardPositions[11] == DiceCoulor)
				return false;
			else if (boardPositions[5] == DiceCoulor && boardPositions[8] == DiceCoulor)
				return false;
		}

		if (position == 13) {
			if (boardPositions[0] == DiceCoulor && boardPositions[6] == DiceCoulor)
				return false;
			else if (boardPositions[14] == DiceCoulor && boardPositions[15] == DiceCoulor)
				return false;
		}
		if (position == 14) {
			if (boardPositions[13] == DiceCoulor && boardPositions[15] == DiceCoulor)
				return false;

		}
		if (position == 15) {
			if (boardPositions[13] == DiceCoulor && boardPositions[14] == DiceCoulor)
				return false;
			else if (boardPositions[2] == DiceCoulor && boardPositions[9] == DiceCoulor)
				return false;
		}

		return true;
	}

	/**
	 * check if the move is valid
	 * 
	 * @param from
	 *            the start position of the dice be selected
	 * @param to
	 *            the position the selected dice wants to move to
	 * @return return ture if the dice can be moved to the selected position
	 */
	public static boolean canMoveTo(int from, int to) {
		if (from == 0) {
			if (to == 1 || to == 6)
				return true;
		}
		if (from == 1) {
			if (to == 0 || to == 2 || to == 4)
				return true;
		}
		if (from == 2) {
			if (to == 9 || to == 1)
				return true;
		}
		if (from == 3) {
			if (to == 4 || to == 7)
				return true;
		}
		if (from == 4) {
			if (to == 3 || to == 5 || to == 1)
				return true;
		}
		if (from == 5) {
			if (to == 4 || to == 8)
				return true;
		}
		if (from == 6) {
			if (to == 0 || to == 13 || to == 7)
				return true;
		}
		if (from == 7) {
			if (to == 3 || to == 6 || to == 10)
				return true;
		}
		if (from == 8) {
			if (to == 5 || to == 12 || to == 9)
				return true;
		}
		if (from == 9) {
			if (to == 8 || to == 2 || to == 15)
				return true;
		}
		if (from == 10) {
			if (to == 11 || to == 7)
				return true;
		}
		if (from == 11) {
			if (to == 10 || to == 12 || to == 14)
				return true;
		}
		if (from == 12) {
			if (to == 11 || to == 8)
				return true;
		}
		if (from == 13) {
			if (to == 6 || to == 14)
				return true;
		}
		if (from == 14) {
			if (to == 13 || to == 11 || to == 15)
				return true;
		}
		if (from == 15) {
			if (to == 14 || to == 9)
				return true;
		}
		return false;
	}
	
	/****************************************************************************************
	 * *****************                        AI                          *************** *
	 ***************************************************************************************/
	/**
	 * This method initializes the winPath map, to list all possible pathways that lead form a single psoiton to form a mill
	 */
	public static void initalize(){
		//list all paths that can be used to make a mill
		int winPaths[][]={{0,1,2},{3,4,5},{10,11,12},{13,14,15},{0,6,13},{2,9,15},{3,7,10},{5,8,12}};
		//go through list of path ways to make a mill
		for (int i=0;i<winPaths.length;i++){
			//load up secondary hashmap if it exists if it does not form it now. This hashmap is waht the first key/psotion maps too
			Map<Integer,Integer> map = new HashMap<Integer,Integer>();
			if (winPath.containsKey(winPaths[i][0]))
				map = winPath.get(winPaths[i][0]);

			//for each of the other two psotions add them into the map, with both ocmbinations
			map.put(winPaths[i][1], winPaths[i][2]);
			map.put(winPaths[i][2], winPaths[i][1]);

			//add the path's to the map for the positon
			winPath.put(winPaths[i][0],map);

			//repeat for the other combinations of  the mill
			map = new HashMap<Integer,Integer>();
			if (winPath.containsKey(winPaths[i][1]))
				map = winPath.get(winPaths[i][1]);

			map.put(winPaths[i][0], winPaths[i][2]);
			map.put(winPaths[i][2], winPaths[i][0]);

			winPath.put(winPaths[i][1],map);

			map = new HashMap<Integer,Integer>();
			if (winPath.containsKey(winPaths[i][2]))
				map = winPath.get(winPaths[i][2]);

			map.put(winPaths[i][1], winPaths[i][0]);
			map.put(winPaths[i][0], winPaths[i][1]);

			winPath.put(winPaths[i][2],map);	
		}

	}

	/**
	 * This method takes in a position on the board adn returns all adjacent ones
	 * 
	 * @param pos - the position in question
	 * @return - an array of integers representing all adjacent positions
	 */
	public static int[] getAdj(int pos){
		return positionAdjacent[pos];
	}

	/**
	 * This method takes in a position on the board and returns the mill it belongs too
	 * 
	 * @param pos - the position in question
	 * @return  - a amp of the rest of the mill
	 */
	public static Map<Integer,Integer> getPath(int pos){
		// if mill exists return it
		if (winPath.containsKey(pos))
			return winPath.get(pos);
		return null;

	}
}
