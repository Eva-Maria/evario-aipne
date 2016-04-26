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
        NetworkClientWrapper networkClient = getNetworkClient();
        int myPlayerNumber = networkClient.getMyPlayerNumber();
        Board board = new Board(myPlayerNumber);

        try {
            interact(networkClient, board);
        } catch (RuntimeException e) {
            if (e.getMessage().startsWith(INVALID_MOVE_EXCEPTION)) {
                L.d(myPlayerNumber, "kicked out.");
            } else {
                throw e;
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

        boolean[] isStillPlaying = {true, true, true};

        while (true) {
            if (!isStillPlaying[currentPlayer]) {
                currentPlayer = ++currentPlayer % 3;
                continue;
            }

            Move move = networkClient.receiveMove(0);

            if (move == null) {
                move = brain.generateMyMove(myPlayerNumber);
                networkClient.sendMove(move, 0);
                move = networkClient.receiveMove(0);
                currentPlayer = myPlayerNumber;
            }

            L.d(myPlayerNumber, "currentPlayer " + currentPlayer + " with move " + move);

            boolean isValidMove = board.makeMoveIfValid(move, currentPlayer);

            if (!isValidMove) {
                isStillPlaying[currentPlayer] = false;
            }

            currentPlayer = ++currentPlayer % 3;
        }
    }

}
