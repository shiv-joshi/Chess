package Data;
import Pieces.Piece;
//import Pieces.Pawn;

public class Space {
    private Piece piece;
    private int x1;
    private int y1;

    public Space(int x, int y, Piece piece){
        setPiece(piece);
        setX(x);
        setY(y);
    }

    public int getX(){
        return this.x1;
    }

    public void setX(int x){
        this.x1 = x;
    }

    public int getY(){
        return this.y1;
    }

    public void setY(int y){
        this.y1 = y;
    }

    public void setPiece(Piece piece){
        this.piece = piece;
    }

    public Piece getPiece(){
        return this.piece;
    }

}
