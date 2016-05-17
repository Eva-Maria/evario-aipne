package evario;

import evario.algorithms.Algorithm;
import evario.game.BoardManager;
import evario.utils.L;
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


    public static final String KICKED_OUT = "kicked out.";
    public static final String NO_PLAYER_FOUND = "no player found";

    private BufferedImage logo;
    private Thread thread;
    private String hostName;

    public Client(String hostName) throws IOException {
        this.hostName = hostName;
        logo = ImageIO.read(new File(Config.ASSET_IMG));

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        int random = (int) (Math.random() * 10);
        String name = Config.TEAM_NAME + random;

        NetworkClient networkClient = getNetworkClient(name);
        int myPlayerNumber = networkClient.getMyPlayerNumber();
        L.addPlayer(name, myPlayerNumber);

        BoardManager bm = new BoardManager(myPlayerNumber);
        Algorithm algorithm = Config.getAlgorithmForPlayer(myPlayerNumber, bm);

        try {
            interact(networkClient, bm, algorithm);
        } catch (Exception e) {
            if (e.getMessage() == null) {
                throw e;
            }
            if (e.getMessage().startsWith(INVALID_MOVE_EXCEPTION)) {
                L.d(myPlayerNumber, KICKED_OUT);
            } else if (e.getMessage().startsWith(BoardManager.EXCEPTION_NO_PLAYER)) {
                L.d(myPlayerNumber, NO_PLAYER_FOUND);
            } else {
                throw e;
            }
        }
    }

    private NetworkClient getNetworkClient(String name) {
        NetworkClient networkClient;
        try {
            networkClient = new NetworkClient(hostName, name, logo);
        } catch (Exception e) {
            throw new RuntimeException(SERVER_NOT_RUNNING_EXCEPTION);
        }
        return networkClient;
    }

    private void interact(NetworkClient networkClient, BoardManager bm, Algorithm algorithm) throws RuntimeException {
        while (true) {
            final long timeMillis = System.currentTimeMillis();
            Move move = networkClient.receiveMove();

            if (move == null) {
                final int timeLimitInSeconds = networkClient.getTimeLimitInSeconds() * 1000;
                move = algorithm.getNextMove(timeMillis, timeLimitInSeconds);
                networkClient.sendMove(move);
                continue;
            }

            bm.updateBoard(move);
        }
    }

}
