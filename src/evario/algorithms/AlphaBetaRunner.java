package evario.algorithms;

import evario.game.Board;
import evario.game.BoardManager;
import evario.utils.L;
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

        int rating = alphaBeta(depth, bm.myPlayerNumber, bm, bm, Integer.MIN_VALUE, Integer.MAX_VALUE);

        long timeUsed = System.currentTimeMillis() - timeStart;
        L.export(bm.myPlayerNumber, "THREAD" + id + ">\ttime:" + timeUsed + "\t,depth:" + depth + "\t,move:" + bestMove + "\t,rating:" + rating + "\t,interrupted:" + isInterrupted);
        algorithm.setBestMove(depth, bestMove);
    }

    public int alphaBeta(final int currentDepth, final int player, BoardManager oldBm, final BoardManager bm, final int alpha, final int beta) {
        if (isInterrupted) {
            return Integer.MIN_VALUE;
        }

        if (currentDepth == 0) {
            return algorithm.rateAllBoards(oldBm, bm, player);
        }

        ArrayList<Move> allPossibleMoves = algorithm.getAllMoves(bm, player);

        if (allPossibleMoves.size() == 0) {
            return algorithm.rateAllBoards(oldBm, bm, player);
        }

        int bestMoveValue;
        int nextPlayer = (player + 1) % 3;
        bestMoveValue = alpha;

        for (Move m : allPossibleMoves) {
            Move translatedMove = Board.translateMoveForPlayer(m, player);

            BoardManager bmClone = BoardManager.clone(bm);
            bmClone.updateBoard(translatedMove);

            int value = -1 * alphaBeta(currentDepth - 1, nextPlayer, bm, bmClone, -1 * beta, -1 * bestMoveValue);
//            L.d(bm.myPlayerNumber, "rating "+value+" for player "+ player + " with depth " + currentDepth + " with move " + translatedMove);

            if (bestMoveValue < value) {
                bestMoveValue = value;

                // Cutoff
                if (bestMoveValue >= beta) {
                    break;
                }

                if (depth == currentDepth) {
                    bestMove = translatedMove;
                }
            }
        }

        return bestMoveValue;
    }

    public void interrupt() {
        this.isInterrupted = true;
    }
}
