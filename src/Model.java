
/**
 * Write what the purpose of this class is
 * 
 * @author Emily Ashworth, Pavithran Pathmarajah
 * @version A1
 */
public class Model {

	private static int boardPositions[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private static int error = 0;

	/**
	 * This function returns the most recent error that was raised
	 * 
	 * @return the integer value corresponding to the last error that was raised as 
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
	 * This method is used to find the value at any given position in the board
	 * 
	 * @param pos the location in the array to be checked
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

	/* The isPossiblePlace method calculates if it is possible for the player
	* to place a piece in a location.
	* 
	* @param int[] a
	* 				the array with postitions of the board.
	* @param int p
	* 				The location the user wants to put the piece in.
	* @return
	* 				a boolean stating if a piece can be placed in a specified position.
	*/
	public static boolean isPossiblePlace(int[] a, int p) { 
		if (a[p] == 1 || a[p] == 2) { // Checks if that location is already
										// occupied.
			// System.out.println("Error: You cannot place a piece on top of
			// another piece."); //Then prints an error message and returns
			// false if the space is occupied.
			return false;
		}
		return true; // Otherwise the space is free, and the player can place
						// their piece in that location so true is returned.
	}

	/* The isPossibleBoard checks if a board is legal and playable or not.
	 * 
	 * @return
	 * 		A boolean stating if a board is playable.
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

		if (isBlocked(boardPositions, 1, redCounter)) { // This uses the
														// isBlocked function
			// to check if the Red player is
			// blocked, if so
			error = 3; //Error is created
			return false; // False returned.
		} else if (isBlocked(boardPositions, 2, blueCounter)) { // Same as
																// above.
			error = 4;
			return false;
		}

		return true; // Otherwise board is legal and playable, true is returned.
	}

	/* The isBlocked function checks if a player is blocked, that is they have
	* no pieces capable of legal moves.
	* 
	* @param int[] boardPositions
	* 							This is the array with the board positions.
	* @param int player
	* 					Indicates Player 1 or Player 2
	* @param int numberPieces
	* 					Gives the number of pieces indicated player has on the board.
	* @return 
	* 			A boolean stating whether a player is blocked or not.
	*/
	public static boolean isBlocked(int[] boardPositions, int player, int numberPieces) { // It
		// takes
		// in
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
		int numberBlockedPieces = 0; // A counter is set for number of pieces.

		// For each position where the player has a piece the neighbouring
		// locations are checked for pieces. If all surrounding locations are
		// filled that piece is blocked and the counter is increased by 1.
		if (boardPositions[0] == player) {
			if (boardPositions[1] != 0 && boardPositions[6] != 0) {
				numberBlockedPieces++;
			}
		}
		if (boardPositions[1] == player) {
			if (boardPositions[0] != 0 && boardPositions[2] != 0 && boardPositions[4] != 0) {
				numberBlockedPieces++;
			}
		}
		if (boardPositions[2] == player) {
			if (boardPositions[1] != 0 && boardPositions[9] != 0) {
				numberBlockedPieces++;
			}
		}
		if (boardPositions[3] == player) {
			if (boardPositions[4] != 0 && boardPositions[7] != 0) {
				numberBlockedPieces++;
			}
		}
		if (boardPositions[4] == player) {
			if (boardPositions[1] != 0 && boardPositions[3] != 0 && boardPositions[5] != 0) {
				numberBlockedPieces++;
			}
		}
		if (boardPositions[5] == player) {
			if (boardPositions[4] != 0 && boardPositions[8] != 0) {
				numberBlockedPieces++;
			}
		}
		if (boardPositions[6] == player) {
			if (boardPositions[0] != 0 && boardPositions[7] != 0 && boardPositions[13] != 0) {
				numberBlockedPieces++;
			}
		}
		if (boardPositions[7] == player) {
			if (boardPositions[3] != 0 && boardPositions[6] != 0 && boardPositions[10] != 0) {
				numberBlockedPieces++;
			}
		}
		if (boardPositions[8] == player) {
			if (boardPositions[5] != 0 && boardPositions[9] != 0 && boardPositions[12] != 0) {
				numberBlockedPieces++;
			}
		}
		if (boardPositions[9] == player) {
			if (boardPositions[2] != 0 && boardPositions[8] != 0 && boardPositions[15] != 0) {
				numberBlockedPieces++;
			}
		}
		if (boardPositions[10] == player) {
			if (boardPositions[7] != 0 && boardPositions[11] != 0) {
				numberBlockedPieces++;
			}
		}
		if (boardPositions[11] == player) {
			if (boardPositions[10] != 0 && boardPositions[12] != 0 && boardPositions[14] != 0) {
				numberBlockedPieces++;
			}
		}
		if (boardPositions[12] == player) {
			if (boardPositions[8] != 0 && boardPositions[11] != 0) {
				numberBlockedPieces++;
			}
		}
		if (boardPositions[13] == player) {
			if (boardPositions[6] != 0 && boardPositions[14] != 0) {
				numberBlockedPieces++;
			}
		}
		if (boardPositions[14] == player) {
			if (boardPositions[11] != 0 && boardPositions[13] != 0 && boardPositions[15] != 0) {
				numberBlockedPieces++;
			}
		}
		if (boardPositions[15] == player) {
			if (boardPositions[9] != 0 && boardPositions[14] != 0) {
				numberBlockedPieces++;
			}
		}

		if (numberBlockedPieces == numberPieces) { // If all the players pieces
													// are blocked, the player
													// is blocked, and true is
													// returned.
			return true;
		}
		return false; // Otherwise player is not blocked and false is returned.

	}
}
