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
    public static final String TEAM_NAME = "Evario_";
    public static final String ASSET_IMG = "assets/zebra.png";

    private BufferedImage logo;
    private Thread thread;
    private String name;
    private String hostName;

    public Client() {
        //only for testing
        name = "Test";
        thread = new Thread(this);
    }

    public Client(String hostName) throws IOException {
        this.hostName = hostName;
        int random = (int) (Math.random() * 10);
        this.name = TEAM_NAME + random;
        logo = ImageIO.read(new File(ASSET_IMG));

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        NetworkClient networkClient = getNetworkClient();
        int myPlayerNumber = networkClient.getMyPlayerNumber();
        BoardManager bm = new BoardManager(myPlayerNumber);
        Algorithm algorithm;

        algorithm = new AlphaBetaAlgorithm(bm, 4, 2, 2, 1);

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

    NetworkClient getNetworkClient() {
        try {
            return new NetworkClient(hostName, name, logo);
        } catch (Exception e) {
            throw new RuntimeException("Server seems not to be running.");
        }
    }

    private void interact(NetworkClient networkClient, BoardManager bm, Algorithm algorithm) throws RuntimeException {
        while (true) {
            long timeMillis = System.currentTimeMillis();
            Move move = networkClient.receiveMove();

            if (move == null) {
                move = algorithm.getNextMove(timeMillis);
                networkClient.sendMove(move);
                continue;
            }

            bm.updateBoard(move);
        }
    }

}
