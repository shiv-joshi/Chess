package Pieces;
import Data.*;

/**
 * @author Shiv Joshi
 * @author Vishnu Divakaruni
 */

/**
* This class represents the Knight piece.
*/
public class Knight extends Piece{
    public boolean moved = false;

    /**
     * This is a constructor for the Knight class.
     * 
     * @param white Boolean value which indicates the color of the piece.
     */
    public Knight(boolean white) {
        super(white);
    }

    /**
     * This method sets all the valid moves the piece can make into the potentialMoves array
     * based on the current state of the game.
     * 
     * @param game The current game board.
     * @param current The current space on the board where the piece is located.
     */
    @Override
    public void setValidMoves(Space[][] game, Space current) {
        potentialMoves.clear();
        int x = current.getX();
        int y = current.getY();

        checkEndSpace(game, new Pair(x - 1, y - 2));
        checkEndSpace(game, new Pair(x - 1, y + 2));
        checkEndSpace(game, new Pair(x + 1, y - 2));
        checkEndSpace(game, new Pair(x + 1, y + 2));
        checkEndSpace(game, new Pair(x - 2, y - 1));
        checkEndSpace(game, new Pair(x - 2, y + 1));
        checkEndSpace(game, new Pair(x + 2, y - 1));
        checkEndSpace(game, new Pair(x + 2, y + 1));
    }

    /**
     * This method checks if the move that the user wants to make is valid or not.
     * 
     * @param game The current game board.
     * @param start The starting space/position of the piece.
     * @param end The end space/position of the piece.
     * @return boolean Returns whether the move is valid or not.
     */
    @Override
    public boolean isValidMove(Space[][] game, Space start, Space end) {
        Pair endSpace = new Pair(end.getX(), end.getY());
        return potentialMoves.contains(endSpace);
    }

    /**
     * Returns and prints a string that represents the knight class.
     * 
     * @return String 
     */
    @Override
    public String toString() {
        if(isWhite()){
            return "wN";
        }else {
            return "bN";
        }
    }
}
