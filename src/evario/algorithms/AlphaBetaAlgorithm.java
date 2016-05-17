package evario.algorithms;

import evario.Config;
import evario.game.Board;
import evario.game.BoardManager;
import evario.utils.L;
import lenz.htw.aipne.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by eve on 5/9/16.
 */
public class AlphaBetaAlgorithm implements Algorithm {

    final private ExecutorService threadPool;
    final private BoardManager bm;
    final private int[] depths;

    Move bestMove;
    int bestDepth;

    public AlphaBetaAlgorithm(BoardManager bm) {
        final int cpuCores = Config.ALPHA_BETA_ALGORITHM_USES_THREADS ? Runtime.getRuntime().availableProcessors() : 1;

        this.bm = bm;
        this.depths = Config.ALPHA_BETA_ALGORITHM_DEPTHS;
        this.threadPool = Executors.newFixedThreadPool(cpuCores);
    }

    @Override
    public Move getNextMove(long timeStartMillis, int timeLimitMillis) {
        bestMove = null;

        final int randomId = (int) (Math.random() * 1000);

        if (Config.ALPHA_BETA_ALGORITHM_USES_THREADS) {
            final ArrayList<AlphaBetaRunner> workerThreads = new ArrayList<>(depths.length);
            for (int depth : depths) {
                final BoardManager clonedBm = BoardManager.clone(bm);
                final AlphaBetaRunner workerThread = new AlphaBetaRunner(depth, clonedBm, this, randomId);
                workerThreads.add(workerThread);
            }

            for (AlphaBetaRunner workerThread : workerThreads) {
                threadPool.execute(workerThread);
            }

            try {
                threadPool.awaitTermination(timeLimitMillis - Config.ALPHA_BETA_ALGORITHM_THREAD_LATENCY, TimeUnit.MILLISECONDS);
                for (AlphaBetaRunner workerThread : workerThreads) {
                    workerThread.interrupt();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Thread.sleep(350);
            } catch (InterruptedException e) {
            }
            final BoardManager clonedBm = BoardManager.clone(bm);
            new AlphaBetaRunner(depths[0], clonedBm, this, randomId).run();
        }

        final long timeUsedMillis = System.currentTimeMillis() - timeStartMillis;
        L.export(bm.myPlayerNumber, "MASTER " + randomId + ">\ttime:" + timeUsedMillis + "\t,depth:" + bestDepth + "\t,move:" + bestMove);

        if (bestMove == null) {
            return new Move(0, 0, 0, 0);
        }
        return bestMove;

    }

    synchronized void setBestMove(int depth, Move move) {
        if (bestDepth < depth || bestMove == null) {
            bestMove = move;
            bestDepth = depth;
        }
    }

    int rateAllBoards(BoardManager oldBm, BoardManager bm, int player) {
        final Board[] allBoards = bm.getAllBoards();
        final Board[] oldAllBoards = oldBm.getAllBoards();

        int rating = 0;
        //  rating += Rating.rateBoard(oldAllBoards[player], allBoards[player], player);

        for (int currentPlayer = 0; currentPlayer < allBoards.length; currentPlayer++) {
            Board board = allBoards[currentPlayer];
            Board oldBoard = oldAllBoards[currentPlayer];
            if (currentPlayer == player) {
                rating += Rating.rateBoard(oldBoard, board, currentPlayer) * 2;
            } else {
                rating -= Rating.rateBoard(oldBoard, board, currentPlayer);
            }
        }

        return rating;
    }

    ArrayList<Move> getAllMoves(BoardManager bm, int player) {
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
