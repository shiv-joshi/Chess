package Pieces;

import Data.*;

/**
 * @author Shiv Joshi
 * @author Vishnu Divakaruni
 */

/**
* This class represents the Pawn piece.
*/


public class Pawn extends Piece{

    public boolean twoMove = false;
     /**
     * This is a constructor for the Pawn class.
     * 
     * @param white Boolean value which indicates the color of the piece.
     */

    public Pawn(boolean white) {
        super(white);
    }
 /**
     * This method sets all the valid moves the piece can make into the potentialMoves array
     * based on the current state of the game.
     * 
     * @param testGame The current game board.
     * @param current The current space on the board where the piece is located.
     */
    
    @Override
    public void setValidMoves(Space[][] testGame, Space current) {
        potentialMoves.clear();

        int x = current.getX();
        int y = current.getY();

        
        if(isWhite()==true){
            //two move
            if(y == 1 && testGame[x][2] == null && testGame[x][3] == null){
                potentialMoves.add(new Pair(x, 3));
               
            }

            //one space down
            if(y+1 <8){
                if( testGame[x][y+1] == null) potentialMoves.add(new Pair(x, y+1));
            }

            //diagonal kills
            if (x+1 < 8 && y+1 < 8) {
                if (testGame[x+1][y+1]!=null && testGame[x+1][y+1].getPiece().isWhite()!=isWhite()){
                    potentialMoves.add(new Pair(x+1, y+1));
                }
            }

            if (x-1 >= 0 && y+1 < 8) {
                if (testGame[x-1][y+1]!=null && testGame[x-1][y+1].getPiece().isWhite()!=isWhite()){
                    potentialMoves.add(new Pair(x-1, y+1));
                }
            }
        }else{
            //two move
            if(y==6 && testGame[x][5]==null && testGame[x][4]==null){
                potentialMoves.add(new Pair(x, 4));
                
            } 

            //one space down
            if(y-1 >=0){
                if( testGame[x][y-1] == null) potentialMoves.add(new Pair(x, y-1));
            }
            
            //diagonal kills
            if (x+1 < 8 && y-1 >= 0) {
                if (testGame[x+1][y-1]!=null && testGame[x+1][y-1].getPiece().isWhite()!=isWhite()){
                    potentialMoves.add(new Pair(x+1, y-1));
                }
            }

            if (x-1 >= 0 && y-1 >= 0) {
                if (testGame[x-1][y-1]!=null && testGame[x-1][y-1].getPiece().isWhite()!=isWhite()){
                    potentialMoves.add(new Pair(x-1, y-1));
                }
            }
        }
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
     * Returns and prints a string that represents the Pawn class.
     * 
     * @return String 
     */
    @Override
    public String toString() {
        if(isWhite()){
            return "wP";
        }else {
            return "bP";
        }
    }
    
    
}
