/*
 * This class handles the logic of a game of checkers. Uses Piece, CheckersPiece
 * , Square and Board.
 * 
 * The board is represented as a linear array of Squares. A linear array was
 * chosen over a 2d array to simplify the application of game rules. 
 * Below is a diagram of what the board would look like if the linear array of 
 * squares was stretched over it:
 * 
 * [ 0] [ 1] [ 2] [ 3] [ 4] [ 5] [ 6] [ 7]
 * [ 8] [ 9] [10] [11] [12] [13] [14] [15]
 * [16] [17] [18] [19] [20] [21] [22] [23]
 * [24] [25] [26] [27] [28] [29] [30] [31]
 * [32] [33] [34] [35] [36] [37] [38] [39]
 * [40] [41] [42] [43] [44] [45] [46] [47]
 * [48] [49] [50] [51] [52] [53] [54] [55]
 * [56] [57] [58] [59] [60] [61] [62] [63]
 * 
 * Under this setup, a valid move consists of addition or subtraction of 9 or 7.
 * A valid capturing move consists of the addition or subtraction of 14 or 18.
 * For example, to check if a move from 44 to 50 is valid, add 9 to it, check
 * the result, do the same with 7:
 * 50 -  7 = 43, 43 != 44
 * 50 - 9 = 41, 41 != 44
 * Therefore the move is invalid.
 */
package bnew;

import gamer.Board;
import gamer.Piece;

public class CheckersGame {

	public static final boolean[] VALID_SQUARE = { false, true, false, true,false, true, false, true, true, false, true, false, true, false,
			true, false, false, true, false, true, false, true, false, true,true, false, true, false, true, false, true, false, false, true,
			false, true, false, true, false, true, true, false, true, false,true, false, true, false, false, true, false, true, false, true,
			false, true, true, false, true, false, true, false, true, false };

	private Board checkersBoard;

	// set up the game board
	CheckersGame() 
	{
		
		checkersBoard = new Board(64);

		Piece tempChecker;
		// Setting Piece on Board
		for (int i = 0; i < checkersBoard.size(); i++) {
			if (i < 24 && VALID_SQUARE[i] == true) 
			{
				tempChecker = new CheckersPiece(CheckersPiece.Khalid,CheckersPiece.CHECKER);
				checkersBoard.addPiece(i, tempChecker);
				continue;
			}

			if (i > 39 && VALID_SQUARE[i] == true) {
				tempChecker = new CheckersPiece(CheckersPiece.Umer,
						CheckersPiece.CHECKER);
				checkersBoard.addPiece(i, tempChecker);
				continue;
			}
		}
	}

	/*
	 * moves a piece on the board owned by the given player. return false if
	 * move is invalid
	 */
	public boolean movePiece(int sourceSquare, int destSquare, int player) {

		if (!isValid(sourceSquare, destSquare, player))
		{
			return false;
		}

		CheckersPiece movedPiece = (CheckersPiece) checkersBoard
				.removePiece(sourceSquare);

		checkersBoard.addPiece(destSquare, movedPiece);

		// check if move was a capturing move
		int difference = Math.abs(sourceSquare - destSquare);

		// for non-capturing moves, difference will be less than 10
		if (difference < 10) {
		//	consecutiveAttack = "";
			return true;
		}

		// capturing move happened - remove captured piece
		checkersBoard.removePiece((sourceSquare + destSquare) / 2);


		return true;
	}

	// lists all valid moves available to the player
	public String listAllMoves(int player) {
		
		String legalAttacks = listCaptures(player);
		if (legalAttacks.length() != 0) 
		{
			return legalAttacks;
		}
		String legalMoves = listMoves(player);
		if (legalMoves.length() != 0) {
			return legalMoves;
		}
		return "";
	}

	// checks if a move is valid
	public boolean isValid(int source, int dest, int player) {

		String move = " " + source + " " + dest + " ";
		String legalMoves = listAllMoves(player);

		if (legalMoves.length() != 0) {
			return (" " + legalMoves).contains(move);
		}

		return false;
	}

	/*
	 * lists all captures available to the given player Attacks are listed in
	 * the form: <source> + " " + <destination> + " "
	 */
	public String listCaptures(int player) {

		String attackList = "";

		for (int i = 0; i < checkersBoard.size(); i++) {
			attackList += listCaptures(i, player);
		}

		return attackList;
	}

	/*
	 * List all captures available to the given player from the given position
	 * on the board. Captures are listed in the form: <source> + " " +
	 * <destination> + " "
	 */
	public String listCaptures(int square, int player) {
		String attackList = "";

		// try 4 possible moves each time, add to list if valid
		
		int posAttack[] = { square + 18, square - 18, square + 14, square - 14 };

		for (int j = 0; j < posAttack.length; j++) {
			if (validCapture(square, posAttack[j], player)) {
				attackList += square + " " + posAttack[j] + " ";
			}
		}
		return attackList;
	}

	// list all non-capturing moves available for the given player
	public String listMoves(int player) {
		String moveList = "";

		// try 4 possible moves each time, add to list if valid
		for (int i = 0; i < checkersBoard.size(); i++)
		{
			int posMove[] = { i + 9, i - 9, i + 7, i - 7 };

			for (int j = 0; j < posMove.length; j++) {
				if (validMove(i, posMove[j], player)) {
					moveList += i + " " + posMove[j] + " ";// ",";
				}
			}
		}
		return moveList;
	}

	// checks if the given capture is valid
	private boolean validCapture(int source, int dest, int player) {
		int tempOwner = checkersBoard.pieceOwnerAt(source);
		int tempType = checkersBoard.pieceTypeAt(source);

		// check owner of the piece and desination square
		if (tempOwner != player
				|| checkersBoard.pieceOwnerAt(dest) != Board.EMPTY_SQUARE) {
			return false;
		}

		// player 1 - starts bottom, moves upwards
		if (tempType == CheckersPiece.CHECKER
				&& player == CheckersPiece.Umer) {
			if (source - dest > 10) {
				return validAttackUp(source, dest, player);
			} else {
				return false;
			}
		}

		// player 2 - starts top, moves downwards
		if (tempType == CheckersPiece.CHECKER
				&& player == CheckersPiece.Khalid) {
			if (dest - source > 10) {
				return validAttackDown(source, dest, player);
			} else {
				return false;
			}
		}
		return false;
	}

	// checks if the given non-capturing move is valid
	private boolean validMove(int source, int dest, int player) {
		int tempOwner = checkersBoard.pieceOwnerAt(source);
		int tempType = checkersBoard.pieceTypeAt(source);

		// check owner of the piece and destination square
		if (tempOwner != player
				|| checkersBoard.pieceOwnerAt(dest) != Board.EMPTY_SQUARE) {
			return false;
		}

		// player 1 - starts bottom, moves upwards
		if (tempType == CheckersPiece.CHECKER
				&& player == CheckersPiece.Umer) {
			if (source - dest < 10) {
				return validMoveUp(source, dest);
			} else {
				return false;
			}
		}

		// computer - starts top, moves downwards
		if (tempType == CheckersPiece.CHECKER
				&& player == CheckersPiece.Khalid) {
			// test for a move range
			if (dest - source < 10) {
				return validMoveDown(source, dest);
			} else {
				return false;
			}
		}

		return false;
	}

	// checks if the given move upwards on the board is valid
	private boolean validMoveUp(int sourceSquare, int destSquare) {
		int difference = sourceSquare - destSquare;

		// the left-border case
		if (sourceSquare % 8 == 0) {
			return difference == 7;
		}

		// right-border case
		if (sourceSquare % 8 == 7) {
			return difference == 9;
		}

		return difference == 7 || difference == 9;
	}

	// checks if the given move downwards on the baord is valid
	private boolean validMoveDown(int sourceSquare, int destSquare) {
		int difference = destSquare - sourceSquare;

		// the left-border case
		if (sourceSquare % 8 == 0) {
			return difference == 9;
		}

		// right-border case
		if (sourceSquare % 8 == 7) {
			return difference == 7;
		}

		return difference == 7 || difference == 9;
	}

	// checks if the capturing move upwards is valid
	private boolean validAttackUp(int sourceSquare, int destSquare, int player) {
		int difference = sourceSquare - destSquare;
		int midSquare = (sourceSquare + destSquare) / 2;

		if (checkersBoard.pieceOwnerAt(midSquare) == player
				|| checkersBoard.pieceOwnerAt(midSquare) == Board.EMPTY_SQUARE) {
			return false;
		}

		// left border case
		if (sourceSquare % 8 == 0 || sourceSquare % 8 == 1) {
			return difference == 14;
		}

		// right border case
		if (sourceSquare % 8 == 7 || sourceSquare % 8 == 6) {
			return difference == 18;
		}

		return difference == 14 || difference == 18;
	}

	// checks if the capturing move downwards is valid
	private boolean validAttackDown(int sourceSquare, int destSquare, int player) {
		int difference = destSquare - sourceSquare;
		int midSquare = (sourceSquare + destSquare) / 2;

		if (checkersBoard.pieceOwnerAt(midSquare) == player
				|| checkersBoard.pieceOwnerAt(midSquare) == Board.EMPTY_SQUARE) {
			return false;
		}

		// left border case
		if (sourceSquare % 8 == 0 || sourceSquare % 8 == 1) {
			return difference == 18;
		}

		// right border case
		if (sourceSquare % 8 == 7 || sourceSquare % 8 == 6) {
			return difference == 14;
		}

		return difference == 14 || difference == 18;
	}

	// returns type of piece at given location
	public int getTypeAt(int square) {
		return checkersBoard.pieceTypeAt(square);
	}

	// returns owner of piece at given location
	public int getOwnerAt(int square) {
		return checkersBoard.pieceOwnerAt(square);
	}

	// add a piece to the board at given location
	public void addPieceAt(int square, CheckersPiece piece) {
		checkersBoard.addPiece(square, piece);
	}


}
