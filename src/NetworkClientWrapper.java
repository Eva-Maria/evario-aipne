import lenz.htw.aipne.Move;
import lenz.htw.aipne.net.NetworkClient;

import java.awt.image.BufferedImage;

/**
 * Created by m on 4/26/16.
 */
public class NetworkClientWrapper {
    private final NetworkClient networkClient;

    NetworkClientWrapper() {
        // only for testing
        networkClient = null;
    }

    public NetworkClientWrapper(String hostName, String name, BufferedImage logo) {
        this.networkClient = new NetworkClient(hostName, name, logo);
    }

    int getTimeLimitInSeconds() {
        return this.networkClient.getTimeLimitInSeconds();
    }

    int getMyPlayerNumber() {
        return this.networkClient.getMyPlayerNumber();
    }

    Move receiveMove(int currentPlayer) {
        Move move = this.networkClient.receiveMove();
        return move;
    }

    void sendMove(Move move, int currentPlayer) {
        this.networkClient.sendMove(move);
    }
}
