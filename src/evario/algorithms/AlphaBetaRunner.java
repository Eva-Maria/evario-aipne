package evario.algorithms;

import evario.game.Board;
import evario.game.BoardManager;
import evario.utils.L;
import lenz.htw.aipne.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by eve on 5/16/16.
 */
public class AlphaBetaRunner implements Runnable {
    private final int depth;
    private final BoardManager bm;
    private final AlphaBetaAlgorithm algorithm;
    private final int id;

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
        final long timeStart = System.currentTimeMillis();

        final int rating = alphaBeta(depth, bm.myPlayerNumber, bm, bm, -10000, +10000);

        final long timeUsed = System.currentTimeMillis() - timeStart;
        L.d(bm.myPlayerNumber, "THREAD" + id + ">\ttime:" + timeUsed + "\t,depth:" + depth + "\t,move:" + bestMove + "\t,rating:" + rating + "\t,interrupted:" + isInterrupted);
        algorithm.setBestMove(depth, bestMove);
    }

    public int alphaBeta(final int currentDepth, final int player, BoardManager oldBm, final BoardManager bm, final int alpha, final int beta) {
        if (isInterrupted) {
            return Integer.MIN_VALUE;
        }

        if (currentDepth == 0) {
            return rateAllBoards(oldBm, bm, player);
        }

        final ArrayList<Move> allPossibleMoves = getAllMoves(bm, player);

        if (allPossibleMoves.size() == 0) {
            return rateAllBoards(oldBm, bm, player);
        }

        int bestMoveValue;
        final int nextPlayer = (player + 1) % 3;
        bestMoveValue = alpha;

        for (Move m : allPossibleMoves) {
            final Move translatedMove = Board.translateMoveForPlayer(m, player);

            final BoardManager bmClone = BoardManager.clone(bm);
            bmClone.updateBoard(translatedMove);

            final int value = -1 * alphaBeta(currentDepth - 1, nextPlayer, bm, bmClone, -1 * beta, -1 * bestMoveValue);
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

    void interrupt() {
        this.isInterrupted = true;
    }

    private static int rateAllBoards(BoardManager oldBm, BoardManager bm, int player) {
        final Board[] allBoards = bm.getAllBoards();
        final Board[] oldAllBoards = oldBm.getAllBoards();

        int rating = 0;
        //  rating += Rating.rateBoard(oldAllBoards[player], allBoards[player], player);

        for (int currentPlayer = 0; currentPlayer < allBoards.length; currentPlayer++) {
            final Board board = allBoards[currentPlayer];
            final Board oldBoard = oldAllBoards[currentPlayer];
            if (currentPlayer == player) {
                rating += Rating.rateBoard(oldBoard, board, currentPlayer) * 2;
            } else {
                rating -= Rating.rateBoard(oldBoard, board, currentPlayer);
            }
        }

        return rating;
    }

    private static ArrayList<Move> getAllMoves(BoardManager bm, int player) {
        final Board board = bm.getAllBoards()[player];

        final ArrayList<Move> moves = new ArrayList<>();
        int playerStones = 0;

        outerLoop:
        for (int fromY = 7; fromY >= 0; fromY--) {
            int fromRowLength = fromY * 2 + 1;
            for (int fromX = 0; fromX < fromRowLength; fromX++) {

                boolean isPlayerOnField = board.isPlayerOnField(fromX, fromY, player);
                if (!isPlayerOnField) {
                    continue;
                } else {
                    if (playerStones == board.playerStones) {
                        break outerLoop;
                    } else {
                        playerStones++;
                    }
                }

                if (fromX % 2 == 0) {
                    moves.add(new Move(fromX, fromY, fromX + 1, fromY));
                    moves.add(new Move(fromX, fromY, fromX - 1, fromY));
                } else {
                    moves.add(new Move(fromX, fromY, fromX - 1, fromY - 1));
                }
            }
        }

        if (moves.size() == 0) {
            return moves;
        }

        final int[][] fields = board.getFields();
        final Comparator<Move> COMPARATOR = (move1, move2) -> {
            int rate1 = Rating.rateMove(move1, fields, player);
            int rate2 = Rating.rateMove(move2, fields, player);

            if (rate1 > rate2) {
                return -1;
            } else if (rate1 < rate2) {
                return 1;
            } else {
                return 0;
            }
        };

        final ArrayList<Move> validMovesSorted = new ArrayList<Move>() {
            public boolean add(Move mt) {
                int index = Collections.binarySearch(this, mt, COMPARATOR);
                if (index < 0) {
                    index = ~index;
                }
                super.add(index, mt);
                return true;
            }
        };

        for (Move move : moves) {
            if (!Board.isMoveValid(move, player, fields)) {
                continue;
            }

            validMovesSorted.add(move);
        }

        return validMovesSorted;
    }
}
