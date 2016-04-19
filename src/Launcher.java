import lenz.htw.aipne.Server;

import java.io.IOException;

/**
 * Created by eve on 4/11/16.
 */
public class Launcher {

    static final Runnable serverLauncher = () -> {
        String[] serverArgs = {"800", "600", "5", "noanim"};
        Server.main(serverArgs);
    };

    public static void main(String... args) throws IOException {
        new Thread(serverLauncher).start();

        String hostName = null;
        if (args.length >= 1) {
            hostName = args[0];
        }
        for (int i = 1; i <= 3; i++) {
            new Client(hostName, "Gamer" + i);
        }
    }
}
