import lenz.htw.aipne.Move;
import lenz.htw.aipne.net.NetworkClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by eve on 4/11/16.
 */
public class Client implements Runnable {

    public static final String INVALID_MOVE_EXCEPTION = "You got kicked because your move was invalid!";
    public static final String SERVER_NOT_RUNNING_EXCEPTION = "Server seems not to be running.";

    public static final String TEAM_NAME = "EVARIO ";
    public static final String ASSET_IMG = "assets/zebra.png";

    private BufferedImage logo;
    private Thread thread;
    private String hostName;

    public Client(String hostName) throws IOException {
        this.hostName = hostName;
        logo = ImageIO.read(new File(ASSET_IMG));

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        int random = (int) (Math.random() * 10);
        String name = TEAM_NAME + random;

        NetworkClient networkClient;
        try {
            networkClient = new NetworkClient(hostName, name, logo);
        } catch (Exception e) {
            throw new RuntimeException(SERVER_NOT_RUNNING_EXCEPTION);
        }
        int myPlayerNumber = networkClient.getMyPlayerNumber();
        L.addPlayer(name, myPlayerNumber);

        BoardManager bm = new BoardManager(myPlayerNumber);
        Algorithm algorithm = null;
        switch (myPlayerNumber) {
            case Board.FIRST_PLAYER:
                algorithm = new RandomAlgorithm(bm);
                break;
            case Board.SECOND_PLAYER:
                algorithm = new RandomAlgorithm(bm);
                break;
            case Board.THIRD_PLAYER:
                algorithm = new AlphaBetaAlgorithm(bm, 4, 2, 2, 1);
        }

        try {
            interact(networkClient, bm, algorithm);
        } catch (Exception e) {
            if (e.getMessage() == null) {
                throw e;
            }
            if (e.getMessage().startsWith(INVALID_MOVE_EXCEPTION)) {
                L.d(myPlayerNumber, "kicked out.");
            } else if (e.getMessage().startsWith(BoardManager.EXCEPTION_NO_PLAYER)) {
                L.d(myPlayerNumber, "no player found");
            } else {
                throw e;
            }
        }
    }

    private void interact(NetworkClient networkClient, BoardManager bm, Algorithm algorithm) throws RuntimeException {
        while (true) {
            long timeMillis = System.currentTimeMillis();
            Move move = networkClient.receiveMove();

            if (move == null) {
                move = algorithm.getNextMove(timeMillis, networkClient.getTimeLimitInSeconds() * 1000);
                networkClient.sendMove(move);
                continue;
            }

            bm.updateBoard(move);
        }
    }

}
