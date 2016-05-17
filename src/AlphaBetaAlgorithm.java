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

    private final ExecutorService threadPool;

    private static final boolean USES_THREADS = false;

    private BoardManager bm;
    int[] depths;
    Move bestMove;
    int bestDepth;

    public AlphaBetaAlgorithm(BoardManager bm) {
        this.bm = bm;

        int cpuCores = 1;
        if (USES_THREADS) {
            cpuCores = Runtime.getRuntime().availableProcessors();
            depths = new int[]{11, 13, 15, 8};
        } else {
            depths = new int[]{1};
        }

        threadPool = Executors.newFixedThreadPool(cpuCores);

    }

    @Override
    public Move getNextMove(long timeStartMillis, int timeLimitMillis) {
        bestMove = null;

        int randomId = (int) (Math.random() * 1000);

        if (USES_THREADS) {

            ArrayList<AlphaBetaRunner> workerThreads = new ArrayList<>(depths.length);
            for (int depth : depths) {
                BoardManager clonedBm = BoardManager.clone(bm);
                final AlphaBetaRunner workerThread = new AlphaBetaRunner(depth, clonedBm, this, randomId);
                workerThreads.add(workerThread);
            }

            for (AlphaBetaRunner workerThread : workerThreads) {
                threadPool.execute(workerThread);
            }

            try {
                threadPool.awaitTermination(timeLimitMillis - 300, TimeUnit.MILLISECONDS);
                for (AlphaBetaRunner workerThread : workerThreads) {
                    workerThread.interrupt();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            BoardManager clonedBm = BoardManager.clone(bm);
            new AlphaBetaRunner(depths[0], clonedBm, this, randomId).run();
        }

        long timeUsedMillis = System.currentTimeMillis() - timeStartMillis;
        L.d(bm.myPlayerNumber, "CLIENT " + randomId + "> time: " + timeUsedMillis + " ms" + " best Depth: " + bestDepth + ", best move : " + bestMove);
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

    int rateBoard(BoardManager oldBm, BoardManager bm, int player) {
        Board[] allBoards = bm.getAllBoards();

        int rating = 0;

        for (int currentPlayer = 0; currentPlayer < allBoards.length; currentPlayer++) {
            Board board = allBoards[currentPlayer];
            if (currentPlayer == player) {
                rating += board.rate(oldBm, currentPlayer);
            } else {

                rating -= board.rate(oldBm, currentPlayer) / 2;
            }
        }

        return rating;
    }

    ArrayList<Move> getAllMoves(BoardManager bm, int player) {
        Board board = bm.getAllBoards()[player];

        ArrayList<Move> moves = new ArrayList<>();
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

        int[][] fields = board.getFields();
        final Comparator<Move> COMPARATOR = (move1, move2) -> {
            int rate1 = rate(move1, fields, player);
            int rate2 = rate(move2, fields, player);

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

        // L.d(player, "Valid moves: " + validMovesSorted.size());

        return validMovesSorted;
    }

    public static int rate(Move move, int[][] fields, int player) {
        int rating = 0;
        int rowLength = move.fromY * 2 + 1;
        if (move.toY == 0 && move.toX == 0) {
            rating += 2;
        } else {
            rating += 1;
        }

        if (move.fromX % 2 == 0) {
            if (move.fromX != 0) {
                int neighbour = fields[move.fromY][move.fromX - 1];
                if (neighbour != Board.EMPTY_FIELD && neighbour != player) {
                    rating += 3;
                }
            } else if (move.fromX < rowLength - 1) {
                int neighbour = fields[move.fromY][move.fromX + 1];
                if (neighbour != Board.EMPTY_FIELD && neighbour != player) {
                    rating += 3;
                }
            }
        }
        return rating;
    }
}
