import lenz.htw.aipne.Move;

import java.util.ArrayList;

/**
 * Created by m on 5/8/16.
 */
public class RandomAlgorithm implements Algorithm {

    public static final String RANDOM_VALID_MOVES_FOUND = "random valid moves found: ";
    private BoardManager bm;

    public RandomAlgorithm(BoardManager bm) {
        this.bm = bm;
    }

    @Override
    public Move getNextMove(long timeMillis, int timeLimitInSeconds) {
        Board myBoard = bm.getMyBoard();

        Move move = findMove(myBoard.getFields(), bm.myPlayerNumber);
        move = Board.translateMoveForPlayer(move, bm.myPlayerNumber);

        return move;
    }

    private static Move findMove(int[][] fields, int myPlayerNumber) {
        ArrayList<Move> moves = new ArrayList<>();
        for (int fromY = 7; fromY >= 0; fromY--) {
            int fromRowLength = fromY * 2 + 1;
            for (int fromX = 0; fromX < fromRowLength; fromX++) {

                for (int toY = fromY; toY >= fromY - 1; toY--) {
                    for (int toX = fromX - 1; toX <= fromX + 1; toX++) {
                        Move move = new Move(fromX, fromY, toX, toY);
                        boolean isValid = Board.isMoveValid(move, myPlayerNumber, fields);
                        if (isValid) {
                            moves.add(move);
                        }
                    }
                }

            }
        }

        if (moves.size() == 0) {
            return new Move(0, 0, 0, 0);
        }

        if (Config.RANDOM_ALGORITHM_SIMPLY_TAKES_FIRST_MOVE) {
            return moves.get(0);
        }

        int index = (int) (Math.random() * moves.size());
        L.d(myPlayerNumber, RANDOM_VALID_MOVES_FOUND + moves.size());
        return moves.get(index);
    }
}
