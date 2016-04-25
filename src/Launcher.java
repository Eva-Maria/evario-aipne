import lenz.htw.aipne.Server;

import java.io.IOException;

/**
 * Created by eve on 4/11/16.
 */
public class Launcher {

    static final Runnable serverLauncher = () -> {
        try {
            String[] serverArgs = {"800", "600", "5", "noanim"};
            Server.main(serverArgs);
        } catch (Exception e) {
            System.out.println("Server is already running. Do nothing.");
        }
    };

    public static void main(String... args) throws IOException {

        String hostName = "127.0.0.1";

        if (args.length >= 1) {
            hostName = args[0];
        }

        if (args.length >= 2) {
            if (args[1].equals("auto")) {
                new Thread(serverLauncher).start();
                new Client(hostName, 2 + "");
                new Client(hostName, 3 + "");
                return;
            }

            if (args[1].equals("fullauto")) {
                new Thread(serverLauncher).start();
                new Client(hostName, 1 + "");
                new Client(hostName, 2 + "");
                new Client(hostName, 3 + "");
                return;
            }
        }

        new Client(hostName, 1 + "");
    }
}
