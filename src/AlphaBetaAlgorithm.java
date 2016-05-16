import lenz.htw.aipne.Move;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by eve on 5/9/16.
 */
public class AlphaBetaAlgorithm implements Algorithm {

    private final ExecutorService threadPool;
    private BoardManager bm;
    int[] depths;
    Move bestMove;
    int bestDepth;

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


        int cpuCores = Runtime.getRuntime().availableProcessors();
        threadPool = Executors.newFixedThreadPool(cpuCores);
        depths = new int[]{11, 13, 15, 8};
    }

    @Override
    public Move getNextMove(long timeStartMillis, int timeLimitMillis) {
        bestMove = null;

        int randomId = (int) (Math.random() * 1000);
        int numberOfWorkers = 5;

        ArrayList<AlphaBetaRunner> workerThreads = new ArrayList<>(numberOfWorkers);
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

    int rateBoard(BoardManager bm, int player) {
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

    ArrayList<Move> getAllMoves(Board board, int myPlayerNumber) {
        int[][] fields = board.getFields();
        ArrayList<Move> moves = new ArrayList<>();
        int playerStones = 0;

        for (int fromY = 7; fromY >= 0; fromY--) {
            int fromRowLength = fromY * 2 + 1;
            for (int fromX = 0; fromX < fromRowLength; fromX++) {
//                boolean isPlayerOnField = board.isPlayerOnField(fromX, fromY, myPlayerNumber);
//                if(!isPlayerOnField){
//                    continue;
//                }else{
//                    playerStones++;
//                }
                for (int toY = fromY; toY >= fromY - 1; toY--) {
                    for (int toX = fromX - 1; toX <= fromX + 1; toX++) {
                        Move move = new Move(fromX, fromY, toX, toY);

                        boolean isValid = Board.isMoveValid(move, myPlayerNumber, fields);
                        if (isValid) {
                            moves.add(Board.translateMoveForPlayer(move, myPlayerNumber));
//                            if(playerStones == Board.getPlayerStonesCount(myPlayerNumber)){
//                                return moves;
//                            }
                        }

                    }
                }

            }
        }

        // L.d(myPlayerNumber, "valid moved found: " + moves.size());
        return moves;
    }
}
