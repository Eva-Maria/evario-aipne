import lenz.htw.aipne.Move;

import java.util.ArrayList;

/**
 * Created by eve on 5/16/16.
 */
public class AlphaBetaRunner implements Runnable {
    private final int depth;
    private final BoardManager bm;
    private final AlphaBetaAlgorithm algorithm;
    private int id;

    private Move bestMove;
    private boolean isInterrupted;

    public AlphaBetaRunner(int depth, BoardManager bm, AlphaBetaAlgorithm algorithm, int id) {
        this.depth = depth;
        this.bm = bm;
        this.algorithm = algorithm;
        this.id = id;
    }

    @Override
    public void run() {
        long timeStart = System.currentTimeMillis();

        int rating = alphaBeta(depth, bm.myPlayerNumber, bm, Integer.MIN_VALUE, Integer.MAX_VALUE);

        long timeUsed = System.currentTimeMillis() - timeStart;
        L.d(bm.myPlayerNumber, "THREAD " + id + "> time: " + timeUsed + " ms" + " depth: " + depth + ", best move : " + bestMove + ", rating: " + rating + ", interrupted: " + isInterrupted);

        algorithm.setBestMove(depth, bestMove);
    }

    public int alphaBeta(final int currentDepth, final int player, final BoardManager bm, final int alpha, final int beta) {
        if (isInterrupted) {
            return Integer.MIN_VALUE;
        }

        ArrayList<Move> allPossibleMoves = algorithm.getAllMoves(bm.getAllBoards()[player], player);

        // L.d(bm.myPlayerNumber, "Player: " + player);

        if (currentDepth == 0 || allPossibleMoves.size() == 0) {
            return algorithm.rateBoard(bm, player);
        }
        //Todo: moves vorsortieren

        int bestMoveValue;
        int nextPlayer = (player + 1) % 3;
        bestMoveValue = alpha;

        for (Move m : allPossibleMoves) {

            BoardManager bmClone = BoardManager.clone(bm);
            bmClone.updateBoard(m);

            int value = -1 * alphaBeta(currentDepth - 1, nextPlayer, bmClone, -1 * beta, -1 * bestMoveValue);

            if (bestMoveValue < value) {
                bestMoveValue = value;
            }

            // Cutoff
            if (bestMoveValue >= beta) {
                break;
            }

            if (depth == currentDepth) {
                bestMove = m;
            }

        }

        return bestMoveValue;
    }

    public void interrupt() {
        this.isInterrupted = true;
    }
}
