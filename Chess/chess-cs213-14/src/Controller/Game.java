package Controller;

import Pieces.*;

import Data.*;
import java.util.*;

public class Game {
    // game grid
    public Space[][] game = new Space[8][8];

    public int numberOfMoves = 0;
    public boolean gameFinished = false;
    private boolean drawCondition = false;

    // checks if piece is at end piece and can promoted
    private boolean promoted = false;

    // checks if it is the consecutive turn after a pawn twomove
    private boolean nextTurn = false;
    private Pair lastPawn;

    // constructor
    public Game() {
        this.initializeGame();
    }

    public void initializeGame() {
        // white pieces
        game[0][0] = new Space(0, 0, new Rook(true));
        game[1][0] = new Space(1, 0, new Knight(true));
        game[2][0] = new Space(2, 0, new Bishop(true));
        game[3][0] = new Space(3, 0, new Queen(true));
        game[4][0] = new Space(4, 0, new King(true));
        game[5][0] = new Space(5, 0, new Bishop(true));
        game[6][0] = new Space(6, 0, new Knight(true));
        game[7][0] = new Space(7, 0, new Rook(true));

        game[0][1] = new Space(0, 1, new Pawn(true));
        game[1][1] = new Space(1, 1, new Pawn(true));
        game[2][1] = new Space(2, 1, new Pawn(true));
        game[3][1] = new Space(3, 1, new Pawn(true));
        game[4][1] = new Space(4, 1, new Pawn(true));
        game[5][1] = new Space(5, 1, new Pawn(true));
        game[6][1] = new Space(6, 1, new Pawn(true));
        game[7][1] = new Space(7, 1, new Pawn(true));

        // black pieces
        game[0][7] = new Space(0, 7, new Rook(false));
        game[1][7] = new Space(1, 7, new Knight(false));
        game[2][7] = new Space(2, 7, new Bishop(false));
        game[3][7] = new Space(3, 7, new Queen(false));
        game[4][7] = new Space(4, 7, new King(false));
        game[5][7] = new Space(5, 7, new Bishop(false));
        game[6][7] = new Space(6, 7, new Knight(false));
        game[7][7] = new Space(7, 7, new Rook(false));

        game[0][6] = new Space(0, 6, new Pawn(false));
        game[1][6] = new Space(1, 6, new Pawn(false));
        game[2][6] = new Space(2, 6, new Pawn(false));
        game[3][6] = new Space(3, 6, new Pawn(false));
        game[4][6] = new Space(4, 6, new Pawn(false));
        game[5][6] = new Space(5, 6, new Pawn(false));
        game[6][6] = new Space(6, 6, new Pawn(false));
        game[7][6] = new Space(7, 6, new Pawn(false));

        // sets valid moves for every piece on the board
        for (Space[] boxes : game) {
            for (Space box : boxes) {
                if (box != null) {
                    box.getPiece().setValidMoves(game, box);
                }
            }
        }
    }

    /**
     * This checks if the format for the input of the moves is correct and splits
     * the input to make it
     * easy for the program to read
     * 
     * @param move input of the terminal. Reads the startposition and endposition of
     *             the piece
     * @return returns if the input is not correct
     */
    public void moveStringConverter(String move) {
        String moves[] = move.split(" ");

        // faulty format
        if (moves.length < 1 || moves.length > 3) {
            System.out.println("Not correct format");
            return;
        }

        // resign or draw
        if (moves.length == 1) {
            if (moves[0].equals("resign")) {
                numberOfMoves++;
                if (numberOfMoves % 2 == 0) {
                    System.out.println("White Wins");
                    gameFinished = true;
                } else {
                    System.out.println("Black Wins");
                    gameFinished = true;
                }
                return;
            }

            if (moves[0].equals("draw")) {
                if (drawCondition) {
                    gameFinished = true;
                    return;
                } else {
                    System.out.println("Draw has not been asked");
                }
            }
        }

        if (drawCondition) {
            drawCondition = false;
        }

        // converting input
        int startX = moves[0].charAt(0) - 'a';
        int startY = moves[0].charAt(1) - '1';
        int endX = moves[1].charAt(0) - 'a';
        int endY = moves[1].charAt(1) - '1';

        // out of bounds error
        if (startX > 7 || startX < 0 || startY > 7 || startY < 0 || endX > 7 || endX < 0 || endY > 7 || endY < 0) {
            System.out.println("Input out of bound");
            return;
        }

        Pair startPos = new Pair(startX, startY);
        Pair endPos = new Pair(endX, endY);

        // draw and promotion are asked for after the move: "e2 e4 draw" or "h7 h8 Q"
        if (moves.length > 2) {
            if (moves[2].equals("draw?")) {
                drawCondition = true;
            } else if (moves[2].length() == 1) {
                promotion(startPos, endPos, moves[2]);
                System.out.println(this);
                return;
            }
        }

        move(startPos, endPos);
        if (!gameFinished) {
            System.out.println(this);
        }
        System.out.println();
    }

    /**
     * This method essentially checks if the move is possible and if it's possible
     * it will move the piece on the board
     * Also contains all the logic for the game
     * 
     * @param startPos start position of the piece
     * @param endPos   end position of the piece
     */
    public void move(Pair startPos, Pair endPos) {
        // Create a new test Board to b for invalid moves so that you don't mess with
        // the real board while checking
        Space[][] testGame = dupeBoard(game);

        // get color of player
        boolean color;
        if (numberOfMoves % 2 == 0) {
            color = true;
        } else {
            color = false;
        }

        if (game[startPos.x][startPos.y] == null) {
            // Checks for player trying to move null space
            System.out.println("No piece at " + startPos);
        } else if (color != game[startPos.x][startPos.y].getPiece().isWhite()) {
            // Checks for player trying to move opponents piece
            System.out.println("Cannot move opponent's piece at " + startPos);
        } else {

            // if it is a valid move
            if (game[startPos.x][startPos.y].getPiece().potentialMoves.contains(endPos)) {
                // doesnt work
                boolean enPassent = pawnCapture(startPos, endPos);

                if (enPassent && testGame[endPos.x][endPos.y] == null) {
                    if (testGame[startPos.x][startPos.y].getPiece().isWhite() == true) {
                        testGame[endPos.x][endPos.y + 1] = null;
                    } else {
                        testGame[endPos.x][endPos.y - 1] = null;
                    }

                    // update the position
                    testGame[startPos.x][startPos.y].setX(endPos.x);
                    testGame[startPos.x][startPos.y].setY(endPos.y);

                    // move the piece
                    testGame[endPos.x][endPos.y] = testGame[startPos.x][startPos.y];
                    testGame[startPos.x][startPos.y] = null;
                    setValidPieceMoves(testGame);
                } else {
                    if (startPos.equals(new Pair(4, 7)) && endPos.equals(new Pair(2, 7))) {
                        // castle to the left side of white

                        // update the position
                        testGame[startPos.x][startPos.y].setX(endPos.x);
                        testGame[startPos.x][startPos.y].setY(endPos.y);
                        testGame[0][7].setX(3);
                        testGame[0][7].setY(7);

                        // move pieces
                        testGame[endPos.x][endPos.y] = testGame[startPos.x][startPos.y];
                        testGame[3][7] = testGame[0][7];
                        testGame[startPos.x][startPos.y] = null;
                        testGame[0][7] = null;

                        setValidPieceMoves(testGame);
                    } else if (startPos.equals(new Pair(4, 7)) && endPos.equals(new Pair(6, 7))) {
                        // castle to the right side of white

                        // update the position
                        testGame[startPos.x][startPos.y].setX(endPos.x);
                        testGame[startPos.x][startPos.y].setY(endPos.y);
                        testGame[7][7].setX(5);
                        testGame[7][7].setY(7);

                        // move pieces
                        testGame[endPos.x][endPos.y] = testGame[startPos.x][startPos.y];
                        testGame[5][7] = testGame[7][7];
                        testGame[startPos.x][startPos.y] = null;
                        testGame[7][7] = null;

                        setValidPieceMoves(testGame);
                    } else if (startPos.equals(new Pair(4, 0)) && endPos.equals(new Pair(2, 0))) {
                        // castle to the left side of black

                        // update the position
                        testGame[startPos.x][startPos.y].setX(endPos.x);
                        testGame[startPos.x][startPos.y].setY(endPos.y);
                        testGame[0][0].setX(3);
                        testGame[0][0].setY(0);

                        // move pieces
                        testGame[endPos.x][endPos.y] = testGame[startPos.x][startPos.y];
                        testGame[3][0] = testGame[0][0];
                        testGame[startPos.x][startPos.y] = null;
                        testGame[0][0] = null;

                        setValidPieceMoves(testGame);
                    } else if (startPos.equals(new Pair(4, 0)) && endPos.equals(new Pair(6, 0))) {
                        // castle to the right side of black

                        // update the position
                        testGame[startPos.x][startPos.y].setX(endPos.x);
                        testGame[startPos.x][startPos.y].setY(endPos.y);
                        testGame[7][0].setX(5);
                        testGame[7][0].setY(0);

                        // move pieces
                        testGame[endPos.x][endPos.y] = testGame[startPos.x][startPos.y];
                        testGame[5][0] = testGame[7][0];
                        testGame[startPos.x][startPos.y] = null;
                        testGame[7][0] = null;

                        setValidPieceMoves(testGame);
                    } else {

                        // check if its enpassant
                        if (testGame[startPos.x][startPos.y].getPiece() instanceof Pawn
                                && (testGame[startPos.x][startPos.y].getPiece().isWhite() == false)
                                && (endPos.x == startPos.x - 1) && (endPos.y == startPos.y - 1)
                                && nextTurn == true
                                && lastPawn.equals(new Pair(startPos.x - 1, startPos.y))) { // black pawn
                            // bottom left
                            if ((testGame[startPos.x - 1][startPos.y] != null)
                                    && testGame[startPos.x - 1][startPos.y].getPiece() instanceof Pawn
                                    && testGame[startPos.x - 1][startPos.y].getPiece().isWhite() == true) {
                                testGame[startPos.x - 1][startPos.y] = null;

                                // update the position
                                testGame[startPos.x][startPos.y].setX(endPos.x);
                                testGame[startPos.x][startPos.y].setY(endPos.y);

                                // move piece
                                testGame[endPos.x][endPos.y] = testGame[startPos.x][startPos.y];
                                testGame[startPos.x][startPos.y] = null;

                                setValidPieceMoves(testGame);
                            }
                        } else if (testGame[startPos.x][startPos.y].getPiece() instanceof Pawn
                                && (testGame[startPos.x][startPos.y].getPiece().isWhite() == false)
                                && (endPos.x == startPos.x + 1) && (endPos.y == startPos.y - 1)
                                && nextTurn == true
                                && lastPawn.equals(new Pair(startPos.x + 1, startPos.y))) { // black pawn
                            // bottom right
                            if ((testGame[startPos.x + 1][startPos.y] != null)
                                    && testGame[startPos.x + 1][startPos.y].getPiece() instanceof Pawn
                                    && testGame[startPos.x + 1][startPos.y].getPiece().isWhite() == true) {
                                testGame[startPos.x + 1][startPos.y] = null;

                                // update the position
                                testGame[startPos.x][startPos.y].setX(endPos.x);
                                testGame[startPos.x][startPos.y].setY(endPos.y);

                                // move piece
                                testGame[endPos.x][endPos.y] = testGame[startPos.x][startPos.y];
                                testGame[startPos.x][startPos.y] = null;

                                setValidPieceMoves(testGame);
                            }
                        } else if (testGame[startPos.x][startPos.y].getPiece() instanceof Pawn
                                && (testGame[startPos.x][startPos.y].getPiece().isWhite() == true)
                                && (endPos.x == startPos.x - 1) && (endPos.y == startPos.y + 1)
                                && nextTurn == true
                                && lastPawn.equals(new Pair(startPos.x - 1, startPos.y))) { // white pawn
                            // top left
                            if ((testGame[startPos.x - 1][startPos.y] != null)
                                    && testGame[startPos.x - 1][startPos.y].getPiece() instanceof Pawn
                                    && testGame[startPos.x - 1][startPos.y].getPiece().isWhite() == false) {
                                testGame[startPos.x - 1][startPos.y] = null;

                                // update the position
                                testGame[startPos.x][startPos.y].setX(endPos.x);
                                testGame[startPos.x][startPos.y].setY(endPos.y);

                                // move piece
                                testGame[endPos.x][endPos.y] = testGame[startPos.x][startPos.y];
                                testGame[startPos.x][startPos.y] = null;

                                setValidPieceMoves(testGame);
                            }
                        } else if (testGame[startPos.x][startPos.y].getPiece() instanceof Pawn
                                && (testGame[startPos.x][startPos.y].getPiece().isWhite() == true)
                                && (endPos.x == startPos.x + 1) && (endPos.y == startPos.y + 1)
                                && nextTurn == true
                                && lastPawn.equals(new Pair(startPos.x + 1, startPos.y))) { // white pawn
                            // top right
                            if ((testGame[startPos.x + 1][startPos.y] != null)
                                    && testGame[startPos.x + 1][startPos.y].getPiece() instanceof Pawn
                                    && testGame[startPos.x + 1][startPos.y].getPiece().isWhite() == false) {
                                testGame[startPos.x + 1][startPos.y] = null;

                                // update the position
                                testGame[startPos.x][startPos.y].setX(endPos.x);
                                testGame[startPos.x][startPos.y].setY(endPos.y);

                                // move piece
                                testGame[endPos.x][endPos.y] = testGame[startPos.x][startPos.y];
                                testGame[startPos.x][startPos.y] = null;

                                setValidPieceMoves(testGame);
                            }
                        } else {
                            nextTurn = false;

                            // update the position
                            testGame[startPos.x][startPos.y].setX(endPos.x);
                            testGame[startPos.x][startPos.y].setY(endPos.y);

                            // move piece
                            testGame[endPos.x][endPos.y] = testGame[startPos.x][startPos.y];
                            testGame[startPos.x][startPos.y] = null;

                            setValidPieceMoves(testGame);

                            // check if its a two move
                            if ((testGame[endPos.x][endPos.y].getPiece() instanceof Pawn)) {
                                int numberOfMoves = Math.abs(endPos.y - startPos.y);
                                if (numberOfMoves == 2) {
                                    Pawn temp = (Pawn) testGame[endPos.x][endPos.y].getPiece();
                                    temp.twoMove = true;
                                    nextTurn = true;

                                    testGame[endPos.x][endPos.y] = new Space(endPos.x, endPos.y, temp);
                                    setValidPieceMoves(testGame);
                                }
                            }
                        }
                    }

                    if (isCheck(testGame)) {
                        // king is being put into check with this move
                        System.out.println("Putting yourself in check if you move from " + startPos + " to " + endPos);
                    } else if (isOpponentCheck(testGame)) {
                        // opponent is being put into check with this move
                        game = testGame;
                        if (isCheckmate(testGame[endPos.x][endPos.y])) {
                            if (numberOfMoves % 2 == 0) {
                                System.out.println("White wins");
                                gameFinished = true;
                                return;
                            } else {
                                System.out.println("Black wins");
                                gameFinished = true;
                                return;
                            }
                        }

                        numberOfMoves++;
                        System.out.println("Check");
                        if ((game[startPos.x][startPos.y] != null)
                                && game[startPos.x][startPos.y].getPiece() instanceof King) {
                            King temp = (King) testGame[startPos.x][startPos.y].getPiece();
                            temp.moved = true;
                            testGame[startPos.x][startPos.y] = new Space(startPos.x, startPos.y, temp);
                            setValidPieceMoves(testGame);
                        }

                        if ((game[startPos.x][startPos.y] != null)
                                && game[startPos.x][startPos.y].getPiece() instanceof Rook) {
                            Rook temp = (Rook) testGame[startPos.x][startPos.y].getPiece();
                            temp.moved = true;
                            testGame[startPos.x][startPos.y] = new Space(startPos.x, startPos.y, temp);
                            setValidPieceMoves(testGame);
                        }
                    } else {
                        game = testGame;

                        numberOfMoves++;
                        if ((game[startPos.x][startPos.y] != null)
                                && game[startPos.x][startPos.y].getPiece() instanceof King) {
                            King temp = (King) testGame[startPos.x][startPos.y].getPiece();
                            temp.moved = true;
                            testGame[startPos.x][startPos.y] = new Space(startPos.x, startPos.y, temp);
                            ;
                            setValidPieceMoves(testGame);
                        }

                        if ((game[startPos.x][startPos.y] != null)
                                && game[startPos.x][startPos.y].getPiece() instanceof Rook) {
                            Rook temp = (Rook) testGame[startPos.x][startPos.y].getPiece();
                            temp.moved = true;
                            testGame[startPos.x][startPos.y] = new Space(startPos.x, startPos.y, temp);
                            setValidPieceMoves(testGame);
                        }
                    }
                }
            } else {
                System.out.println("Illegal move, try again");
                System.out.println();
            }
        }
    }

    /**
     * This method promotes a pawn to another piece if it reaches the end of the
     * board
     * 
     * @param startPos  startposition of the piece
     * @param endPos    end position of the piece
     * @param promotion user types in what piece that want to promote
     * @return returns if it cannot be promoted
     */
    public void promotion(Pair startPos, Pair endPos, String promotion) {
        promoted = true;
        move(startPos, endPos);
        promoted = false;
        Piece current = game[endPos.x][endPos.y].getPiece();
        Piece promotedPiece = null;

        // check if piece is a pawn
        if (!(game[endPos.x][endPos.y].getPiece() instanceof Pawn)) {
            System.out.println("Cannot promote piece because it is not a pawn.");
            return;
        }

        // promote pawn to said piece
        if (promotion.equals("R")) {
            // set color
            promotedPiece = new Rook(current.isWhite());
            // set position
            game[endPos.x][endPos.y] = new Space(endPos.x, endPos.y, promotedPiece);
        } else if (promotion.equals("N")) {
            // set color
            promotedPiece = new Knight(current.isWhite());
            // set position
            game[endPos.x][endPos.y] = new Space(endPos.x, endPos.y, promotedPiece);
        } else if (promotion.equals("B")) {
            // set color
            promotedPiece = new Bishop(current.isWhite());
            // set position
            game[endPos.x][endPos.y] = new Space(endPos.x, endPos.y, promotedPiece);
        } else if (promotion.equals("Q")) {
            // set color
            promotedPiece = new Queen(current.isWhite());
            // set position
            game[endPos.x][endPos.y] = new Space(endPos.x, endPos.y, promotedPiece);
        } else {
            System.out.println("Cannot promote pawn because the input is invalid.");
        }

        // create a duplicate board to test
        Space[][] testGame = dupeBoard(game);

        testGame[endPos.x][endPos.y] = new Space(endPos.x, endPos.y, promotedPiece);

        setValidPieceMoves(testGame);
        game = testGame;
        if (isOpponentCheck(testGame)) {
            if (isCheckmate(testGame[endPos.x][endPos.y])) {
                if (numberOfMoves % 2 == 0) {
                    System.out.println("White wins");
                    gameFinished = true;
                    return;
                } else {
                    System.out.println("Black wins");
                    gameFinished = true;
                    return;
                }
            }
            System.out.println("Check");
        }
    }

    int wk = 0;
    int bk = 0;
    int wrr = 0;
    int wlr = 0;
    int brr = 0;
    int blr = 0;

    /**
     * This method essentially sets all the moves that a player can make on a board
     * 
     * @param testGame the gameBoard
     */
    public void setValidPieceMoves(Space[][] testGame) {

        for (Space[] pieces : testGame) {
            for (Space piece : pieces) {
                if (piece != null) {
                    piece.getPiece().setValidMoves(game, piece);
                }
            }
        }

        // en passant
        for (int i = 0; i < testGame.length - 1; i++) {
            if (testGame[i][3] != null && testGame[i][3].getPiece() instanceof Pawn
                    && testGame[i][3].getPiece().isWhite() == false
                    && testGame[i + 1][3] != null && testGame[i + 1][3].getPiece() instanceof Pawn
                    && testGame[i + 1][3].getPiece().isWhite() == true && testGame[i + 1][2] == null
                    && nextTurn == true
                    && lastPawn.equals(new Pair(i + 1, 3))) {
                Pawn temp = (Pawn) testGame[i][3].getPiece();
                if (temp.twoMove == true) {
                    System.out.println(i);
                    temp.potentialMoves.add(new Pair(i + 1, 2));

                }
            }
            if (testGame[i + 1][3] != null && testGame[i + 1][3].getPiece() instanceof Pawn
                    && testGame[i + 1][3].getPiece().isWhite() == false
                    && testGame[i][3] != null && testGame[i][3].getPiece() instanceof Pawn
                    && testGame[i][3].getPiece().isWhite() == true && testGame[i][2] == null
                    && nextTurn == true
                    && lastPawn.equals(new Pair(i, 3))) {
                Pawn temp = (Pawn) testGame[i + 1][3].getPiece();
                if (temp.twoMove == true) {
                    System.out.println(i);
                    temp.potentialMoves.add(new Pair(i, 2));

                }
            }
            if (testGame[i][4] != null && testGame[i][4].getPiece() instanceof Pawn
                    && testGame[i][4].getPiece().isWhite() == true
                    && testGame[i + 1][4] != null && testGame[i + 1][4].getPiece() instanceof Pawn
                    && testGame[i + 1][4].getPiece().isWhite() == false && testGame[i + 1][5] == null
                    && nextTurn == true
                    && lastPawn.equals(new Pair(i + 1, 4))) {
                Pawn temp = (Pawn) testGame[i][4].getPiece();
                if (temp.twoMove == true) {
                    System.out.println(i);
                    temp.potentialMoves.add(new Pair(i + 1, 5));

                }
            }
            if (game[i + 1][4] != null && game[i + 1][4].getPiece() instanceof Pawn
                    && game[i + 1][4].getPiece().isWhite() == true
                    && game[i][4] != null && game[i][4].getPiece() instanceof Pawn
                    && game[i][4].getPiece().isWhite() == false && game[i][5] == null
                    && nextTurn == true
                    && lastPawn.equals(new Pair(i, 4))) {
                Pawn temp = (Pawn) game[i + 1][4].getPiece();
                if (temp.twoMove == true) {
                    System.out.println(i);
                    temp.potentialMoves.add(new Pair(i, 5));

                }
            }
        }

        // castling
        if (game[4][0] == null) {
            wk++;
        }
        if (game[4][7] == null) {
            bk++;
        }
        if (game[7][7] == null || !(game[7][7].getPiece() instanceof Rook)) {
            brr++;
        }
        if (game[0][7] == null || !(game[0][7].getPiece() instanceof Rook)) {
            blr++;
        }
        if (game[7][0] == null || !(game[7][0].getPiece() instanceof Rook)) {
            wlr++;
        }
        if (game[0][0] == null || !(game[7][7].getPiece() instanceof Rook)) {
            wrr++;
        }

        if (game[4][7] != null && game[4][7].getPiece() instanceof King) { // White left Castle
            King k = (King) game[4][7].getPiece();
            if (bk != 0) {
                k.moved = true;
            }
            if (k.moved == false) {

                if ((game[0][7] != null) && game[0][7].getPiece() instanceof Rook && game[1][7] == null
                        && game[2][7] == null && game[3][7] == null && (!attacked(new Pair(1, 7)))
                        && (!attacked(new Pair(2, 7))) && (!attacked(new Pair(3, 7)))) {
                    Rook r = (Rook) game[0][7].getPiece();
                    if (blr != 0) {
                        r.moved = true;
                    }
                    if (r.moved == false) {
                        k.potentialMoves.add(new Pair(2, 7));
                        // r.potentialMoves.add(new Pair(3,7)); Shouldn't be in Rook?
                    }
                }
                if ((game[7][7] != null) && game[7][7].getPiece() instanceof Rook && game[5][7] == null
                        && game[6][7] == null && (!attacked(new Pair(5, 7))) && (!attacked(new Pair(6, 7)))) { // White
                                                                                                               // Right
                                                                                                               // Castle
                    Rook r1 = (Rook) game[7][7].getPiece();
                    if (brr != 0) {
                        r1.moved = true;
                    }
                    if (r1.moved == false) {
                        k.potentialMoves.add(new Pair(6, 7));
                        // r1.potentialMoves.add(new Pair(5,7)); Shouldn't be in Rook?
                    }
                }
            }
        }
        if (game[4][0] != null && game[4][0].getPiece() instanceof King) { // white left Castle
            King k = (King) game[4][0].getPiece();
            if (wk != 0) {
                k.moved = true;
            }
            if (k.moved == false) {
                if ((game[0][0] != null) && game[0][0].getPiece() instanceof Rook && game[1][0] == null
                        && game[2][0] == null && game[3][0] == null && (!attacked(new Pair(1, 0)))
                        && (!attacked(new Pair(2, 0))) && (!attacked(new Pair(3, 0)))) {
                    Rook r = (Rook) game[0][0].getPiece();
                    if (wrr != 0) {
                        r.moved = true;
                    }
                    if (r.moved == false) {
                        k.potentialMoves.add(new Pair(2, 0));
                        r.potentialMoves.add(new Pair(3, 0));
                    }
                }
                if ((game[7][0] != null) && game[7][0].getPiece() instanceof Rook && game[5][0] == null
                        && game[6][0] == null && (!attacked(new Pair(5, 0))) && (!attacked(new Pair(6, 0)))) { // Black
                                                                                                               // Right
                                                                                                               // Castle
                    Rook r1 = (Rook) game[7][0].getPiece();
                    if (wlr != 0) {
                        r1.moved = true;
                    }
                    if (r1.moved == false) {
                        k.potentialMoves.add(new Pair(6, 0));
                        r1.potentialMoves.add(new Pair(5, 0));
                    }
                }
            }
        }
    }

    /**
     * This method just returns the duplicate version of the gameboard
     * 
     * @param testGame The current gameboard
     * @return returns the duplicated game board
     */
    public Space[][] dupeBoard(Space[][] testGame) {
        Space[][] newGame = new Space[8][8];
        for (int i = 0; i < testGame.length; i++) {
            for (int j = 0; j < testGame[i].length; j++) {
                if (testGame[i][j] == null) {
                    newGame[i][j] = null;
                } else if (testGame[i][j].getPiece() instanceof Pawn) {
                    // copy piece
                    Pawn p = (Pawn) testGame[i][j].getPiece();
                    Pawn newP = new Pawn(p.isWhite());
                    testGame[i][j] = new Space(testGame[i][j].getX(), testGame[i][j].getY(), newP);

                    // fix twoMove
                    newP.twoMove = p.twoMove;
                    newP.potentialMoves = new ArrayList<>(p.potentialMoves);

                    // put it in duplicate board
                    newGame[i][j] = new Space(testGame[i][j].getX(), testGame[i][j].getY(), newP);
                } else if (testGame[i][j].getPiece() instanceof Knight) {
                    // copy piece
                    Knight p = (Knight) testGame[i][j].getPiece();
                    Knight newP = new Knight(p.isWhite());
                    newP.potentialMoves = new ArrayList<>(p.potentialMoves);

                    // put it in duplicate board
                    newGame[i][j] = new Space(testGame[i][j].getX(), testGame[i][j].getY(), newP);
                } else if (testGame[i][j].getPiece() instanceof Rook) {
                    // copy piece
                    Rook p = (Rook) testGame[i][j].getPiece();
                    Rook newP = new Rook(p.isWhite());

                    // fix moved function
                    newP.moved = p.moved;
                    newP.potentialMoves = new ArrayList<>(p.potentialMoves);

                    // put it in duplicate board
                    newGame[i][j] = new Space(testGame[i][j].getX(), testGame[i][j].getY(), newP);
                } else if (testGame[i][j].getPiece() instanceof Bishop) {
                    // copy piece
                    Bishop p = (Bishop) testGame[i][j].getPiece();
                    Bishop newP = new Bishop(p.isWhite());
                    newP.potentialMoves = new ArrayList<>(p.potentialMoves);

                    // put it in duplicate board
                    newGame[i][j] = new Space(testGame[i][j].getX(), testGame[i][j].getY(), newP);
                } else if (testGame[i][j].getPiece() instanceof Queen) {
                    // copy piece
                    Queen p = (Queen) testGame[i][j].getPiece();
                    Queen newP = new Queen(p.isWhite());
                    newP.potentialMoves = new ArrayList<>(p.potentialMoves);

                    // put it in duplicate board
                    newGame[i][j] = new Space(testGame[i][j].getX(), testGame[i][j].getY(), newP);
                } else if (testGame[i][j].getPiece() instanceof King) {
                    // copy piece
                    King p = (King) testGame[i][j].getPiece();
                    King newP = new King(p.isWhite());
                    newP.potentialMoves = new ArrayList<>(p.potentialMoves);

                    // fix moved function
                    newP.moved = p.moved;

                    // put it in duplicate board
                    newGame[i][j] = new Space(testGame[i][j].getX(), testGame[i][j].getY(), newP);
                }
            }
        }
        setValidPieceMoves(newGame);
        return newGame;
    }

    /**
     * This method checks if there is a checkmate and returns a boolean.
     * 
     * @param checkingPiece the piece thats being checked
     *
     * @return if checkmate is possible or not
     */
    public boolean isCheckmate(Space checkingPiece) {
        Space[][] testGame = dupeBoard(game);

        // get checking path
        Pair[] checkingPath = new Pair[checkingPiece.getPiece().potentialMoves.size()];
        checkingPiece.getPiece().potentialMoves.toArray(checkingPath);

        for (int i = 0; i < game.length; i++) {
            for (int j = 0; j < game[i].length; j++) {
                if (game[i][j] != null) {
                    // get color of current player
                    boolean color;
                    if (numberOfMoves % 2 == 0) {
                        color = true;
                    } else {
                        color = false;
                    }

                    // find all of opponents pieces
                    if (color != game[i][j].getPiece().isWhite()) {
                        Pair[] valid = new Pair[game[i][j].getPiece().potentialMoves.size()];
                        game[i][j].getPiece().potentialMoves.toArray(valid);

                        System.out.println(game[i][j].getPiece());
                        // checks every potential move of piece
                        for (Pair pair : valid) {
                            System.out.println(pair);

                            // can any opponent piece kill checking piece
                            if (pair.x == checkingPiece.getX() && pair.y == checkingPiece.getY()) {
                                return false;
                            }

                            // find opponents pieces
                            if (game[i][j].getPiece().isWhite() != checkingPiece.getPiece().isWhite()) {
                                if (game[i][j].getPiece() instanceof King) { // can king move to a safe space
                                    // setValidPieceMoves(game);
                                    System.out.println("king: " + pair);
                                    if (attacked(pair) == false)
                                        return false;
                                }
                            }
                        }
                    }

                }
            }
        }

        return true;
    }

    /**
     * This method essentially checks if the king is under check or is being
     * attacked in any way shape or form
     *
     * @param p sees what pieces are attacked the king
     * @return returns if its being attacked or not
     */
    public boolean attacked(Pair p) {
        for (int i = 0; i < game.length; i++) {
            for (int j = 0; j < game[i].length; j++) {
                if (game[i][j] != null) {
                    // get color of current player
                    boolean color;
                    if (numberOfMoves % 2 == 0) {
                        color = true;
                    } else {
                        color = false;
                    }

                    if (color != game[i][j].getPiece().isWhite()) { // Finds all opponent's pieces not king
                        if (!(game[i][j].getPiece() instanceof King)) {
                            Pair[] valid = new Pair[game[i][j].getPiece().potentialMoves.size()];
                            game[i][j].getPiece().potentialMoves.toArray(valid);
                            for (Pair pair : valid) {
                                if (pair.equals(p)) {
                                    return true;
                                }
                            }
                        }

                    }
                }
            }
        }
        return false;
    }

    /**
     * This method checks to see if the king is in check
     *
     * @param testGame This is the current gameboard
     * @return return if its in check or not
     */
    public boolean isCheck(Space[][] testGame) {
        Pair allyKingPos = null;
        boolean allyKingCol = false;
        for (int i = 0; i < testGame.length; i++) {
            for (int j = 0; j < testGame[i].length; j++) {
                // get color of current player
                boolean color;
                if (numberOfMoves % 2 == 0) {
                    color = true;
                } else {
                    color = false;
                }

                // finds your king
                if ((testGame[i][j] != null) && (testGame[i][j].getPiece() instanceof King)
                        && color == testGame[i][j].getPiece().isWhite()) {
                    allyKingPos = new Pair(i, j);
                    allyKingCol = color;
                    break;
                }
            }
        }

        // checks if any opponents piece can attack your king
        for (Space[] pieces : testGame) {
            for (Space piece : pieces) {

                if (piece != null) {
                    // System.out.println(piece.getPiece() + " " +
                    // piece.getPiece().potentialMoves.contains(allyKingPos) );
                }
                if (piece != null) {
                    if ((piece.getPiece().isWhite() != allyKingCol)
                            && piece.getPiece().potentialMoves.contains(allyKingPos)) {
                        // System.out.println(piece.getPiece());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This method sees if the opponents king is in check or not
     *
     * @param testGame this is the current gameboard
     * @return returns if the opponents king is in check or not
     */
    public boolean isOpponentCheck(Space[][] testGame) {
        Pair opponentKingPos = null;
        for (int i = 0; i < testGame.length; i++) {
            for (int j = 0; j < testGame[i].length; j++) {
                // get color of current player
                boolean color;
                if (numberOfMoves % 2 == 0) {
                    color = true;
                } else {
                    color = false;
                }

                // find opponents king
                if ((testGame[i][j] != null) && (testGame[i][j].getPiece() instanceof King)
                        && (color != testGame[i][j].getPiece().isWhite())) {
                    opponentKingPos = new Pair(i, j);
                    break;
                }
            }
        }
        for (Space[] pieces : testGame) {
            for (Space piece : pieces) {
                if (piece != null) {
                    if (piece.getPiece().potentialMoves.contains(opponentKingPos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This method checks to see if the pawn is killing another piece
     *
     * @param start start position of pawn
     * @param end   end position of pawn
     * @return returns if pawncapture is possible
     */
    public boolean pawnCapture(Pair start, Pair end) {
        Space[][] testGame = dupeBoard(game);
        int startx = start.x;
        int starty = start.y;
        int endx = end.x;
        int endy = end.y;
        if (testGame[startx][starty] == null) {
            return false;
        }
        if (!(testGame[startx][starty].getPiece() instanceof Pawn)) {
            return false;
        }

        if (testGame[startx][starty].getPiece().isWhite() == true) {
            if (endy == starty - 1 && (endx == startx + 1 || endx == startx - 1)) {
                if (testGame[endx][endy] == null && (testGame[endx][endy + 1].getPiece() instanceof Pawn)
                        && testGame[endx][endy + 1].getPiece().isWhite() == false) {
                    Pawn temp = (Pawn) testGame[endx][endy + 1].getPiece();
                    if (temp.twoMove) {
                        game[startx][starty].getPiece().potentialMoves.add(new Pair(endx, endy));
                        return true;
                    }
                } else {
                    return false;
                }
            }

        } else {
            if (endy == starty + 1 && (endx == startx + 1 || endx == startx - 1)) {
                if (testGame[endx][endy] == null && (testGame[endx][endy - 1].getPiece() instanceof Pawn)
                        && testGame[endx][endy - 1].getPiece().isWhite() == true) {
                    Pawn temp = (Pawn) testGame[endx][endy - 1].getPiece();
                    if (temp.twoMove) {
                        game[startx][starty].getPiece().potentialMoves.add(new Pair(endx, endy));
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    /**
     * This method resets the Pawn's twoMove variable to false, which is used to
     * figure out if en passant is possible.
     *
     * @param testGame Takes in the board.
     */
    public void pawnTwoMoveReset(Space[][] testGame) {
        for (int i = 0; i < testGame.length; i++) {
            if ((testGame[i][3] != null) && testGame[i][3].getPiece() instanceof Pawn) {
                Pawn temp = (Pawn) testGame[i][3].getPiece();
                temp.twoMove = false;

                testGame[i][3] = new Space(i, 3, temp);
            }
            if ((testGame[i][4] != null) && testGame[i][4].getPiece() instanceof Pawn) {
                Pawn temp = (Pawn) testGame[i][4].getPiece();
                temp.twoMove = false;

                testGame[i][4] = new Space(i, 3, temp);
            }
        }
        setValidPieceMoves(testGame);
    }

    /**
     * this method takes in the character being used in the command line and
     * converts
     * it to an int
     *
     * @param c character being used
     * @return returns an integer
     */
    public int letterToInt(char c) {
        int number = -1;

        switch (c) {
            case 'a':
                number = 0;
                break;
            case 'b':
                number = 1;
                break;
            case 'c':
                number = 2;
                break;
            case 'd':
                number = 3;
                break;
            case 'e':
                number = 4;
                break;
            case 'f':
                number = 5;
                break;
            case 'g':
                number = 6;
                break;
            case 'h':
                number = 7;
                break;
            default:
                break;
        }

        return number;
    }

    // printing
    public String toString() {
        String board = "";
        int row = 8;

        for (int x = 7; x >= 0; x--) {
            for (int y = 0; y < 8; y++) {
                if (game[y][x] == null) {
                    if (x % 2 == 1 && y % 2 == 0 || x % 2 == 0 && y % 2 == 1) {
                        board += "## ";
                    } else {
                        board += "   ";
                    }
                } else {
                    board += game[y][x].getPiece() + " ";
                }
            }
            board += row + "\n";
            row--;
        }

        board += " a  b  c  d  e  f  g  h  ";
        return board;
    }

}