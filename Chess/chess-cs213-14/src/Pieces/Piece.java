package Pieces;
import Data.*;
import java.util.*;

/**
 * @author Shiv Joshi
 * @author Vishnu Divakaruni
 */
/**
* This class represents all the Pieces and extends onto every other piece class.
*/

public abstract class Piece {
    /*
    - we use the keyword abstract in front of the methods so that we dont need to
    add any implementation(body) in the method
    - every subclass that extends Piece will have to also implement these methods
    and add a body for them because each piece(rook, pawn...) will behave differently
    */
    private boolean white;
    private boolean killed;

    //list of all the moves the piece can make
    public ArrayList<Pair> potentialMoves = new ArrayList<>(); 
    /**
     * This is a constructor for the Piece class.
     * 
     * @param white Boolean value which indicates the color of the piece.
     */

    public Piece(boolean white){
        this.setWhite(white);
    }

    public boolean isWhite(){
        return this.white;
    }

    public boolean isKilled(){
        return this.killed;
    }

    public void setWhite(boolean white){
        this.white = white;
    }


    /**
     * This method sets all the valid moves the piece can make into the potentialMoves array
     * based on the current state of the game.
     * 
     * @param game The current game board.
     * @param current The current space on the board where the piece is located.
     */
    //set valid moves for piece
    public abstract void setValidMoves(Space[][] game, Space current);

     /**
     * This method checks if the move that the user wants to make is valid or not.
     * 
     * @param game The current game board.
     * @param start The starting space/position of the piece.
     * @param end The end space/position of the piece.
     * @return boolean Returns whether the move is valid or not.
     */
    //check if valid move
    public abstract boolean isValidMove(Space[][] game, Space start, Space end);

/**
 * This class checks if the Piece is on the board
 * 
 * @param testGame current game board
 * @param newPos new position of the position
 */

    //check if it is in the board
    public void checkEndSpace(Space[][] testGame, Pair newPos){
        if((newPos.x >= 0 && newPos.y >= 0 && newPos.x <= 7 && newPos.y <= 7) && (testGame[newPos.x][newPos.y] == null || testGame[newPos.x][newPos.y].getPiece().isWhite()!= this.isWhite())){
            potentialMoves.add(newPos);
        }
    }

    /**
     * Returns and prints a string that represents the Piece class.
     * 
     * @return String 
     */

    //toString method
    @Override
    public abstract String toString();

}
