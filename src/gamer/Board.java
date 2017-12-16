/*
 * A class that represents a game board. It consists of Squares that can contain
 * objects of type Piece. The game board is amde up of a linear array of Squares.
 */
package gamer;

public class Board {
    
    public static final int EMPTY_SQUARE = -1;
    public static final int INPUT_ERROR = -2;
    
    
    private int size;            
    private Square[] squareArray;
    
    // create board of given size
    public Board(int size)
    {   
        this.size = size;
        squareArray = new Square[size];
        
        for(int i=0; i   < size; i++){
            squareArray[i] = new Square();
        }
    }
    
    // return size of the board
    public int size() {
        return size;
    }
    
    // add the Piece at given position
    public boolean addPiece(int square, Piece piece) 
    {        
        if (square >= size || square < 0 || !squareArray[square].isEmpty()){
            return false;     
        }
        
        squareArray[square].setPiece(piece);
        
        return true;
    }
    
    // remove AND return the Piece at the given position.
    public Piece removePiece(int square) 
    {      
        if (square >= size || square < 0 || squareArray[square].isEmpty())
        {
            return null;  
        }
        
        Piece temp = squareArray[square].getPiece();
        squareArray[square].empty();        
        
        return temp;
    }
    
    // return the owner of the piece at the given position
    public int pieceOwnerAt(int square) {   
        
        // signal input error
        if (square >= size || square < 0){
            return INPUT_ERROR;  
        }
        
        // signal empty square error
        if (squareArray[square].isEmpty())
        {
            return EMPTY_SQUARE;  
        }
        
        return squareArray[square].getPieceOwner();        
    }
    
    // return the type of the piece at the given position
    public int pieceTypeAt(int square) 
    {    
        
        // signal empty square error
        if (squareArray[square].isEmpty()){
            return EMPTY_SQUARE;  
        }
        
        return squareArray[square].getPieceType();      
    }
    
}