package Pieces;
import Data.*;

/**
 * @author Shiv Joshi
 * @author Vishnu Divakaruni
 */

/**
* This class represents the King piece.
*/
public class King extends Piece{
    public boolean moved = false;

    /**
     * This is a constructor for the King class.
     * 
     * @param white Boolean value which indicates the color of the piece.
     */
    public King(boolean white) {
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

        if (x+1<8 && (game[x+1][y] == null || game[x+1][y].getPiece().isWhite() != pieceColor )){
            potentialMoves.add( new Pair(x+1,y));

        }  if (x-1>=0 && (game[x-1][y] == null || game[x-1][y].getPiece().isWhite() != pieceColor )){
            potentialMoves.add( new Pair(x-1, y));

        }  if (y+1 < 8 && (game[x][y+1] == null || game[x][y+1].getPiece().isWhite() != pieceColor)){
            potentialMoves.add( new Pair(x, y+1));

        } if (y-1 >=0 && (game[x][y-1] == null || game[x][y-1].getPiece().isWhite() != pieceColor)){
            potentialMoves.add( new Pair(x, y-1));

        } if (y-1 >=0 && x-1>= 0 && (game[x-1][y-1] == null || game[x-1][y-1].getPiece().isWhite() != pieceColor)){
            potentialMoves.add( new Pair(x-1, y-1));

        } if (y+1 < 8 && x-1>= 0 && (game[x-1][y+1] == null || game[x-1][y+1].getPiece().isWhite() != pieceColor)){
            potentialMoves.add( new Pair(x-1, y+1));

        }  if (y+1 < 8 && x+1 < 8 && (game[x+1][y+1] == null || game[x+1][y+1].getPiece().isWhite() != pieceColor)){
            potentialMoves.add( new Pair(x+1, y+1));

        } if (y-1 >=0 && x+1< 8 && (game[x+1][y-1] == null || game[x+1][y-1].getPiece().isWhite() != pieceColor)){
            potentialMoves.add( new Pair(x+1, y-1));
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
     * Returns and prints a string that represents the King class.
     * 
     * @return String 
     */
    @Override
    public String toString() {
        if(isWhite()){
            return "wK";
        }else {
            return "bK";
        }
    } 
}
