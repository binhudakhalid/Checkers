/*
 * A class that represents a square of a game board.
 * The square can contain an object of type Piece.
 */

package gamer;

public class Square {

	private Piece piece;

	Square() {
	}

	Square(Piece piece) {
		
		setPiece(piece);
		
	}

	// put a Piece in the square. return success/failure indicator.
	public boolean setPiece(Piece piece) {
		this.piece = piece;
		return true;
	}

	// get Piece from the square
	public Piece getPiece() {
		return piece;
	}

	// check if Square contains a Piece
	public boolean isEmpty() {
		return piece == null;
	}

	// remove any Piece from the Square.
	public void empty() {
		piece = null;
	}

	// return the type of the Piece that is contained in the square.
	public int getPieceType() {
		return piece.getType();
	}

	// return the owner of the Piece that is contained in the square.
	public int getPieceOwner() {
		return piece.getOwner();
	}
}