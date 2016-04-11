import lenz.htw.aipne.Server;

/**
 * Created by eve on 4/11/16.
 */
public class Launcher {

    static final Runnable serverLauncher = () -> {
        String[] serverArgs = {"800", "600", "5"};
        Server.main(serverArgs);
    };

    public static void main(String[] args) {
        new Thread(serverLauncher).start();

        System.out.println("Welcome to Evario-Land v.0.1");
    }
}
