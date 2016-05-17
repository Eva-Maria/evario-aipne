package evario;

import evario.algorithms.Algorithm;
import evario.algorithms.AlphaBetaAlgorithm;
import evario.algorithms.RandomAlgorithm;
import evario.game.Board;
import evario.game.BoardManager;

/**
 * Created by m on 5/17/16.
 */
public class Config {

    public static final String HOSTNAME = "127.0.0.1";
    public static final String[] SERVER_ARGS = {"800", "600", "5", "noanim"};
    public static final boolean autoExit = false;

    public static final String LOG_FILE_NAME = "results.csv";
    public static final String TEAM_NAME = "EVARIO ";
    public static final String ASSET_IMG = "assets/zebra.png";

    public static final boolean RANDOM_ALGORITHM_SIMPLY_TAKES_FIRST_MOVE = false;

    public static final boolean ALPHA_BETA_ALGORITHM_USES_THREADS = true;
    public static final int ALPHA_BETA_ALGORITHM_WEIGHT_CORNER = 2;
    public static final int ALPHA_BETA_ALGORITHM_WEIGHT_BEAT_OPPONENT = 2;
    public static final int ALPHA_BETA_ALGORITHM_WEIGHT_MOVE_FORWARD_TO_CENTER = 1;

    public static Algorithm getAlgorithmForPlayer(final int myPlayerNumber, BoardManager bm) {
        switch (myPlayerNumber) {
            case Board.FIRST_PLAYER:
                return new RandomAlgorithm(bm);
            case Board.SECOND_PLAYER:
                return new RandomAlgorithm(bm);
            case Board.THIRD_PLAYER:
                return new AlphaBetaAlgorithm(bm);
        }
        return null;
    }
}
