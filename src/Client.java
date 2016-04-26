import lenz.htw.aipne.Move;

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

    private Brain brain = new Brain();

    public Client() {
        //only for testing
        name = "Test";
        thread = new Thread(this);
    }

    public Client(String hostName, String name) throws IOException {
        this.hostName = hostName;
        this.name = TEAM_NAME + name;
        logo = ImageIO.read(new File(ASSET_IMG));

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        NetworkClientWrapper networkClient = getNetworkClient();
        int myPlayerNumber = networkClient.getMyPlayerNumber();
        Board board = new Board(myPlayerNumber);
        boolean isInterrupted = false;

        while (!thread.isInterrupted() && !isInterrupted) {
            try {
                interact(networkClient, board);
            } catch (RuntimeException e) {
                if (e.getMessage().startsWith(INVALID_MOVE_EXCEPTION)) {
                    L.d(myPlayerNumber, "kicked out.");
                    thread.interrupt();
                    isInterrupted = true;
                } else {
                    throw e;
                }
            }
        }
    }

    NetworkClientWrapper getNetworkClient() {
        try {
            return new NetworkClientWrapper(hostName, name, logo);
        } catch (Exception e) {
            throw new RuntimeException("Server seems not to be running.");
        }
    }

    private void interact(NetworkClientWrapper networkClient, Board board) throws RuntimeException {
        int currentPlayer = Board.FIRST_PLAYER;
        int myPlayerNumber = networkClient.getMyPlayerNumber();

        while (true) {
            Move move = networkClient.receiveMove(currentPlayer);
            L.d(myPlayerNumber, "NC: move received for player " + currentPlayer + ": " + move);

            if (move == null) {
                move = brain.generateMyMove(myPlayerNumber);
                L.d(myPlayerNumber, "NC: move sending for player " + currentPlayer + ": " + move);
                networkClient.sendMove(move, myPlayerNumber);
                currentPlayer = myPlayerNumber;
            }

            if (currentPlayer == Board.THIRD_PLAYER) {
                L.d(myPlayerNumber, "// ROUND DONE //////");
                return;
            } else {
                currentPlayer++;
            }
        }
    }

}
