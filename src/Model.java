import java.util.ArrayList;

/**
 * This class acts as the model module in the MVC architecture
 * 
 * @author Emily Ashworth, Pavithran Pathmarajah, Ziyi Jin
 * @version A2
 */
public class Model {

	private static int boardPositions[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private static ArrayList<int[]> moveHistory = new ArrayList();
	private static int error = 0;

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

		// if there is a draw
		if (checkDraw(mover))
			return 3;

		return 0; // no one wins
	}

	/**
	 * Six men-morris a draw is established if cyclical game play occurs
	 * 
	 * @param mover
	 *            - the current person turns
	 */
	private static boolean checkDraw(int mover) {
		System.out.println("------------------------------------------");
		System.out.println("hi" + moveHistory.size());
		/*for (int i=0; i<moveHistory.size();i++ ){
			System.out.print(i+" |");
			int [] tmp = moveHistory.get(i);
			for (int j=0;j<tmp.length;j++){
				System.out.print(" "+tmp[j]);
			}
			System.out.println();
		}*/
		// look for a pattern, minimum size is 3
		for (int sampleSize = 2; sampleSize <= (moveHistory.size() / 2); sampleSize+=2) {

			boolean draw = true;
			System.out.println("-----------sampleSize-------------" + sampleSize);
			for (int i = 0; (i+ sampleSize) < moveHistory.size(); i++) {
				int[] curCycle = moveHistory.get(i);
				int[] pastCycle = moveHistory.get(i + sampleSize);
				if (curCycle[0]!=pastCycle[0] || curCycle[1]!=pastCycle[1] ||  curCycle[2]!=pastCycle[2])
					draw=false;
				for (int j = 0; j < curCycle.length; j++) {
					System.out.print(" " + curCycle[j]);
				}
				System.out.print(" |");
				for (int j = 0; j < pastCycle.length; j++) {
					System.out.print(" " + pastCycle[j]);
				}
				System.out.println();
			}
			if (draw){
				System.out.println("yo");
				return true;
			}
		}
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

}
