import lenz.htw.aipne.Move;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by m on 4/26/16.
 */
public class Brain {

    private ArrayList<Move> movesPlayerOne;
    private ArrayList<Move> movesPlayerTwo;
    private ArrayList<Move> movesPlayerThree;

    public Brain() {
        Move[] movesOne = {
//                    new Move(5, 7, 5, 6),
                new Move(4, 7, 3, 7),
        };
        Move[] movesTwo = {
                new Move(0, 5, 1, 6),
//                    new Move(0, 2, 0, 1),
        };
        Move[] movesThree = {
                new Move(6, 4, 5, 4),
//                    new Move(9, 5, 8, 5),
        };

        movesPlayerOne = new ArrayList<>(Arrays.asList(movesOne));
        movesPlayerTwo = new ArrayList<>(Arrays.asList(movesTwo));
        movesPlayerThree = new ArrayList<>(Arrays.asList(movesThree));
    }

    Move generateMyMove(int myPlayerNumber) {
        Move move = null;
        switch (myPlayerNumber) {
            case Board.FIRST_PLAYER:
                move = movesPlayerOne.get(0);
                movesPlayerOne.remove(0);
                break;
            case Board.SECOND_PLAYER:
                move = movesPlayerTwo.get(0);
                movesPlayerTwo.remove(0);
                break;
            case Board.THIRD_PLAYER:
                move = movesPlayerThree.get(0);
                movesPlayerThree.remove(0);
                break;
        }
        return move;
    }
}
