import lenz.htw.aipne.Move;

/**
 * Created by m on 5/8/16.
 */
public class Algorithm {
    public static Move getNextMove(BoardManager bm) {
        Board myBoard = bm.getMyBoard();

        Move move = findMove(myBoard.getFields(), bm.myPlayerNumber);
        move = Board.translateMoveForPlayer(move, bm.myPlayerNumber);

        return move;
    }

    private static Move findMove(int[][] fields, int myPlayerNumber) {
        for (int fromY = 7; fromY >= 0; fromY--) {
            int fromRowLength = fromY * 2 + 1;
            for (int fromX = 0; fromX < fromRowLength; fromX++) {

                for (int toY = fromY; toY >= fromY - 1; toY--) {
                    for (int toX = fromX - 1; toX < fromX + 1; toX++) {
                        Move move = new Move(fromX, fromY, toX, toY);
                        boolean isValid = Board.isMoveValid(move, myPlayerNumber, fields);
                        if (isValid) {
                            return move;
                        }
                    }
                }

            }
        }
        return null;
    }
}
