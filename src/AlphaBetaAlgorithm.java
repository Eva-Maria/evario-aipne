import lenz.htw.aipne.Move;

import java.util.ArrayList;

/**
 * Created by eve on 5/9/16.
 */
public class AlphaBetaAlgorithm implements Algorithm {

    private BoardManager bm;
    static int depth = 2;
    Move bestMove;
    int bestMoveValue;
    private int myStoneWeight;
    private int myDistanceWeight;
    private int opponentStoneWeigh;
    private int opponentDistanceWeight;

    public AlphaBetaAlgorithm(BoardManager bm, int myStoneWeight, int myDistanceWeight, int opponentStoneWeigh, int opponentDistanceWeight) {
        this.bm = bm;
        this.myStoneWeight = myStoneWeight;
        this.myDistanceWeight = myDistanceWeight;
        this.opponentStoneWeigh = opponentStoneWeigh;
        this.opponentDistanceWeight = opponentDistanceWeight;
    }

    @Override
    public Move getNextMove() {
        //reset best move
        bestMove = null;

        int rating = alphaBeta(depth, bm.myPlayerNumber, bm, Integer.MIN_VALUE, Integer.MAX_VALUE);

        if (bestMove == null) {
            L.d(bm.myPlayerNumber, "Leider keine Zuege mehr verfuegbar");
            return null;
        }

        L.d(bm.myPlayerNumber, "Rating: " + rating);

        return bestMove;
    }


    public int alphaBeta(final int depth, final int player, final BoardManager bm, final int alpha, final int beta) {
        ArrayList<Move> allPossibleMoves = getAllMoves(bm.getAllBoards()[player], player);

        L.d(bm.myPlayerNumber, "Player: " + player);

        if (depth == 0 || allPossibleMoves.size() == 0) {
            return rateBoard(bm, player);
        }

        int alphaBeta = 0;
        int nextPlayer = (player + 1) % 3;
        bestMoveValue = alpha;

        for (Move m : allPossibleMoves) {

            BoardManager bmClone = BoardManager.clone(bm);
            bmClone.updateBoard(m);

            int value = -1 * alphaBeta(depth - 1, nextPlayer, bmClone, -1 * beta, -1 * bestMoveValue);

            if (bestMove == null || bestMoveValue < value) {
                bestMove = m;
                bestMoveValue = value;
            }

            if (bestMoveValue >= beta) {
                break;
            }

            if (AlphaBetaAlgorithm.depth == depth) {
                bestMove = m;
                L.d(bm.myPlayerNumber, "Best move from : " + bestMove.fromX + "," + bestMove.fromY + " to " + bestMove.toX + "," + bestMove.toY);

            }
        }

        alphaBeta = bestMoveValue;

        return alphaBeta;
    }

    private int rateBoard(BoardManager bm, int player) {
        Board[] allBoards = bm.getAllBoards();

        int rating = 0;

        for (int currentPlayer = 0; currentPlayer < allBoards.length; currentPlayer++) {
            Board board = allBoards[currentPlayer];
            if (currentPlayer == player) {
                rating += board.rate(currentPlayer, myStoneWeight, myDistanceWeight);
            } else {

                rating -= board.rate(currentPlayer, opponentStoneWeigh, opponentDistanceWeight);
            }
        }

        return rating;
    }

    private static ArrayList<Move> getAllMoves(Board board, int myPlayerNumber) {
        int[][] fields = board.getFields();
        ArrayList<Move> moves = new ArrayList<>();

        for (int fromY = 7; fromY >= 0; fromY--) {
            int fromRowLength = fromY * 2 + 1;
            for (int fromX = 0; fromX < fromRowLength; fromX++) {

                for (int toY = fromY; toY >= fromY - 1; toY--) {
                    for (int toX = fromX - 1; toX <= fromX + 1; toX++) {
                        Move move = new Move(fromX, fromY, toX, toY);
                        boolean isValid = Board.isMoveValid(move, myPlayerNumber, fields);
                        if (isValid) {
                            moves.add(Board.translateMoveForPlayer(move, myPlayerNumber));
                        }
                    }
                }

            }
        }

        //  L.d(myPlayerNumber, "valid moved found: " + moves.size());
        return moves;
    }
}
