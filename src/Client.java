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
    private final Board board = new Board();

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
        while (!thread.isInterrupted()) {
            try {
                interact(networkClient);
            } catch (RuntimeException e) {
                if (e.getMessage().startsWith(INVALID_MOVE_EXCEPTION)) {
                    thread.interrupt();
                } else {
                    throw e;
                }
            }
        }
    }

    private void interact(NetworkClient networkClient) throws RuntimeException {
        Move move;
        while ((move = networkClient.receiveMove()) != null) {
            // update internal model
        }

        move = new Move(4, 7, 3, 7);
        networkClient.sendMove(move);
    }
}
