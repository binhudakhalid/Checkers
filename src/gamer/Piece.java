/*
 * Interface for Piece. Used as a general class by Square and Board.
 */
package gamer;

public interface Piece {
	// returns the owner of the piece
	public int getOwner();

	// returns the type of the piece
	public int getType();
}
