package Pieces;

import Controller.Game;
import Data.*;

/**
 * @author Shiv Joshi
 * @author Vishnu Divakaruni
 */

/**
* This class represents the Queen piece.
*/

public class Queen extends Piece {
    public boolean moved = false;

    /**
     * This is a constructor for the Queen class.
     * 
     * @param white Boolean value which indicates the color of the piece.
     */

    public Queen(boolean white) {
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
        boolean pieceColor = current.getPiece().isWhite();
        
        //bottom left
        while(x-1 >= 0 && y-1 >= 0){
            Pair newPos = new Pair(x-1, y-1);
            if(game[x-1][y-1] == null){ // Moving to null space
                potentialMoves.add(newPos);
            } else if(pieceColor != game[x-1][y-1].getPiece().isWhite()){ //(White capture black or Black capture white)
                potentialMoves.add(newPos);
                break;
            } else { //Trying to capture your own piece
                break;
            }
            x--;
            y--;
        }

        //top right
        x = current.getX();
        y = current.getY();
        while(x+1 < 8 && y+1 < 8){
            Pair newPos = new Pair(x+1, y+1);
            if(game[x+1][y+1] == null){
                potentialMoves.add(newPos);
            } else if(pieceColor != game[x+1][y+1].getPiece().isWhite()){ //(White capture black or Black capture white)
                potentialMoves.add(newPos);
                break;
            } else {
                break;
            }
            x++;
            y++;
        }

        //top left
        x = current.getX();
        y = current.getY();
        while(x-1 >= 0 && y+1 < 8){
            Pair newPos = new Pair(x-1, y+1);
            if(game[x-1][y+1] == null){
                potentialMoves.add(newPos);
            } else if(pieceColor != game[x-1][y+1].getPiece().isWhite()){ //(White capture black or Black capture white)
                potentialMoves.add(newPos);
                break;
            } else {
                break;
            }
            x--;
            y++;
        }

        //bottom right
        x = current.getX();
        y = current.getY();
        while(x+1 < 8 && y-1 >= 0){
            Pair newPos = new Pair(x+1, y-1);
            if(game[x+1][y-1] == null){
                potentialMoves.add(newPos);
            } else if(pieceColor != game[x+1][y-1].getPiece().isWhite()){ //(White capture black or Black capture white)
                potentialMoves.add(newPos);
                break;
            } else {
                break;
            }
            x++;
            y--;
        }




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
     * Returns and prints a string that represents the Queen class.
     * 
     * @return String 
     */
    @Override
    public String toString() {
        if(isWhite()){
            return "wQ";
        }else {
            return "bQ";
        }
    }

   

    
    
}
