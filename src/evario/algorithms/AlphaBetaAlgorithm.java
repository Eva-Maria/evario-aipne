package evario.algorithms;

import evario.Config;
import evario.game.BoardManager;
import evario.utils.L;
import lenz.htw.aipne.Move;

import java.util.ArrayList;
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

    private Move bestMove;
    private int bestDepth;

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
            startMutliThread(timeLimitMillis, randomId);
        } else {
            startSingleTask(randomId);
        }

        final long timeUsedMillis = System.currentTimeMillis() - timeStartMillis;
        L.d(bm.myPlayerNumber, "MASTER " + randomId + ">\ttime:" + timeUsedMillis + "\t,depth:" + bestDepth + "\t,move:" + bestMove);

        if (bestMove == null) {
            return new Move(0, 0, 0, 0);
        }
        return bestMove;

    }

    private void startMutliThread(int timeLimitMillis, int randomId) {
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
    }

    private void startSingleTask(int randomId) {
        try {
            Thread.sleep(350);
        } catch (InterruptedException e) {
        }
        final BoardManager clonedBm = BoardManager.clone(bm);
        new AlphaBetaRunner(depths[0], clonedBm, this, randomId).run();
    }

    synchronized void setBestMove(int depth, Move move) {
        if (bestDepth < depth || bestMove == null) {
            bestMove = move;
            bestDepth = depth;
        }
    }
}
