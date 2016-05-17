import evario.Client;
import evario.Config;
import evario.utils.SystemOutInterceptor;
import lenz.htw.aipne.Server;

import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by eve on 4/11/16.
 */
public class Launcher {

    static final String SERVER_IS_ALREADY_RUNNING_DO_NOTHING = "Server is already running. Do nothing.";
    static final String MODE_AUTO = "auto";
    static final String MODE_FULLAUTO = "fullauto";

    static final Runnable serverLauncher = () -> {
        try {
            Server.main(Config.SERVER_ARGS);
        } catch (Exception e) {
            System.out.println(SERVER_IS_ALREADY_RUNNING_DO_NOTHING);
        }
    };

    public static void main(String... args) throws IOException {
        String hostName = Config.HOSTNAME;

        wrapSystemOut();

        if (args.length >= 1) {
            hostName = args[0];
        }

        if (args.length >= 2) {
            if (args[1].equals(MODE_AUTO)) {
                new Thread(serverLauncher).start();
                new Client(hostName);
                new Client(hostName);
                return;
            }

            if (args[1].equals(MODE_FULLAUTO)) {
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
