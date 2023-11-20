package chess;

import java.util.*;
import Controller.*;

public class Chess {
    public static void main(String[] args) {
        Game newGame = new Game();
        Scanner scanner = new Scanner(System.in);
        System.out.println(newGame);
        System.out.println();

        while(!newGame.gameFinished) {
            if(newGame.numberOfMoves % 2 == 0) {
                System.out.print("White's Move: ");
            }else{
                System.out.print("Black's Move: ");
            }
            String input = scanner.nextLine();
            System.out.println();
            newGame.moveStringConverter(input);
        }

        scanner.close();
    }
}
