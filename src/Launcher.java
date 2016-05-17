import lenz.htw.aipne.Server;

import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by eve on 4/11/16.
 */
public class Launcher {

    static final Runnable serverLauncher = () -> {
        try {
            Server.main(Config.SERVER_ARGS);
        } catch (Exception e) {
            System.out.println("Server is already running. Do nothing.");
        }
    };

    public static void main(String... args) throws IOException {
        String hostName = "127.0.0.1";

        wrapSystemOut();

        if (args.length >= 1) {
            hostName = args[0];
        }

        if (args.length >= 2) {
            if (args[1].equals("auto")) {
                new Thread(serverLauncher).start();
                new Client(hostName);
                new Client(hostName);
                return;
            }

            if (args[1].equals("fullauto")) {
                new Thread(serverLauncher).start();
                new Client(hostName);
                new Client(hostName);
                new Client(hostName);
                return;
            }
        }

        new Client(hostName);
    }

    private static void wrapSystemOut() {
        PrintStream origOut = System.out;
        PrintStream interceptor = new SystemOutInterceptor(origOut);
        System.setOut(interceptor);
    }

}
