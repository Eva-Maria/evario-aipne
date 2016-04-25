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

    private final BufferedImage logo;
    private final Thread thread;
    private String name;
    private String hostName;

    public Client(String hostName, String name) throws IOException {
        this.hostName = hostName;
        this.name = TEAM_NAME + name;
        logo = ImageIO.read(new File(ASSET_IMG));

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        NetworkClient networkClient = new NetworkClient(hostName, name, logo);
        Board board = new Board(networkClient.getMyPlayerNumber());
        while (!thread.isInterrupted()) {
            try {
                interact(networkClient, board);
                Thread.yield(); //TODO: remove before practise - because one system is hosting all threads.
            } catch (RuntimeException e) {
                if (e.getMessage().startsWith(INVALID_MOVE_EXCEPTION)) {
                    System.out.println(networkClient.getMyPlayerNumber() + " was kicked.");
                    thread.interrupt();
                } else {
                    throw e;
                }
            }
        }
    }

    private void interact(NetworkClient networkClient, Board board) throws RuntimeException {
        int currentPlayer = Board.FIRST_PLAYER;
        Move move = networkClient.receiveMove();
        while (true) {
            if (move == null) {
                move = generateMyMove();
                networkClient.sendMove(move);

            }
            board.makeMove(move, currentPlayer);
            System.out.println(board.toString());

            if (currentPlayer == Board.THIRD_PLAYER) {
                System.out.println("////////////////////////////////////");
                return;
            }
            currentPlayer++;
        }
    }

    private Move generateMyMove() {
        Move move = new Move(4, 7, 3, 7);
        return move;
    }
}
