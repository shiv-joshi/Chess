package Pieces;

import Controller.Game;
import Data.*;
/**
 * @author Shiv Joshi
 * @author Vishnu Divakaruni
 */

/**
* This class represents the Rook piece.
*/
public class Rook extends Piece{
    public boolean moved = false;

     /**
     * This is a constructor for the Rook class.
     * 
     * @param white Boolean value which indicates the color of the piece.
     */

    public Rook(boolean white){
        super(white);
    }
    /**
     * This method checks if the move that the user wants to make is valid or not.
     * 
     * @param game The current game board.
     * @param start The starting space/position of the piece.
     * @param end The end space/position of the piece.
     * @return boolean Returns whether the move is valid or not.
     */

   
    //rook can move in all 4 directions for any amount of spaces
    @Override
    public boolean isValidMove(Space[][] game, Space start, Space end) {
        //grabs the coordinates for the end space
        Pair endSpace = new Pair(end.getX(), end.getY());
        return potentialMoves.contains(endSpace);
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
        //clear all previous valid moves
        potentialMoves.clear();
        int x = current.getX();
        int y = current.getY();

        //current piece color
        boolean pieceColor = current.getPiece().isWhite();

        //right
        x = current.getX();
        y = current.getY();
        while( x+1 < 8 ){
            Pair newPos = new Pair(x+1, y);
            if(game[x+1][y] == null){ // Moving to null space
                potentialMoves.add(newPos);
            } else if(pieceColor != game[x+1][y].getPiece().isWhite()){ //(White capture black or Black capture white)
                potentialMoves.add(newPos);
                break;
            } else { //Trying to capture your own piece
                break;
            }
            x++;
        }

        //left   
        x = current.getX();
        y = current.getY();     
        while(x-1 >= 0){
            Pair newPos = new Pair(x-1, y);
            if(game[x-1][y] == null){ // Moving to null space
                potentialMoves.add(newPos);
            } else if(pieceColor != game[x-1][y].getPiece().isWhite()){ //(White capture black or Black capture white)
                potentialMoves.add(newPos);
                break;
            } else { //Trying to capture your own piece
                break;
            }
            x--;
        }

        //up
        x = current.getX();
        y = current.getY();
        while(y+1 < 8){
            Pair newPos = new Pair(x, y+1);
            if(game[x][y+1] == null){
                potentialMoves.add(newPos);
            } else if(pieceColor != game[x][y+1].getPiece().isWhite()){ //(White capture black or Black capture white)
                potentialMoves.add(newPos);
                break;
            } else {
                break;
            }
            y++;
        }
        
        //down
        x = current.getX();
        y = current.getY();
        while(y-1 >= 0){
            Pair newPos = new Pair(x, y-1);
            if(game[x][y-1] == null){
                potentialMoves.add(newPos);
            } else if(pieceColor != game[x][y-1].getPiece().isWhite()){ //(White capture black or Black capture white)
                potentialMoves.add(newPos);
                break;
            } else {
                break;
            }
            y--;
        }
    }

    /**
     * Returns and prints a string that represents the Rook class.
     * 
     * @return String 
     */

    @Override
    public String toString() {
        if(isWhite()){
            return "wR";
        }else {
            return "bR";
        }
    }

}
