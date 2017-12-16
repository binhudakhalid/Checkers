/*
 * A checkers piece class, an implementation of class Piece.
 */
package bnew;
import gamer.Piece;

public class CheckersPiece implements Piece {
	public static final int Umer = 0;
	public static final int Khalid = 1;
	public static final int CHECKER = 0;
	private int owner, type;

	CheckersPiece() {
		owner = 0;
		type = 0;
	}
	// constructor that sets owner and type to non-default supplied values.
	CheckersPiece(int owner, int type) {
		setOwner(owner);
	}
	private void setOwner(int owner) {
		this.owner = owner;
	}

	public int getOwner() {
		return owner;
	}

	public int getType() {
		return type;
	}

}



