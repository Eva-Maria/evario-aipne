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
        Board board = new Board(networkClient.getMyPlayerNumber());
        boolean isInterrupted = false;

        while (!thread.isInterrupted() && !isInterrupted) {
            try {
                interact(networkClient, board);
                Thread.yield(); //TODO: remove before practise - because one system is hosting all threads.
            } catch (RuntimeException e) {
                if (e.getMessage().startsWith(INVALID_MOVE_EXCEPTION)) {
                    System.out.println(networkClient.getMyPlayerNumber() + " was kicked.");
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
        while (true) {
            int myPlayerNumber = networkClient.getMyPlayerNumber();
            Move move = networkClient.receiveMove(currentPlayer);

            if (move == null) {
                move = generateMyMove(currentPlayer);
                networkClient.sendMove(move, currentPlayer);
                move = networkClient.receiveMove(currentPlayer);
                System.out.println("I am " + name + " with number " + myPlayerNumber + ", it is my turn: " + move);

            } else {
                System.out.println("I am " + name + " with number " + myPlayerNumber + ", it is player " + currentPlayer + "'s turn: " + move);
            }

            board.makeMove(move, currentPlayer);
            System.out.println(board.toString());

            if (currentPlayer == Board.THIRD_PLAYER) {
                System.out.println("/// ROUND DONE ///////////////////////////////////");
                return;
            }

            currentPlayer++;
            System.out.println("I am " + name + " with number " + myPlayerNumber + ", next player will be " + currentPlayer);
        }
    }

    private Move generateMyMove(int myPlayerNumber) {
        switch (myPlayerNumber) {
            case Board.FIRST_PLAYER:
                return new Move(4, 7, 3, 7);
//            case Board.SECOND_PLAYER:
//                return new Move(0, 5, 1, 6);
//            case Board.THIRD_PLAYER:
//                return new Move(6, 4, 5, 4);
        }
        return null;
    }
}
