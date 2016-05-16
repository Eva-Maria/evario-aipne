import lenz.htw.aipne.Server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
        PrintStream interceptor = new Interceptor(origOut);
        System.setOut(interceptor);
    }

    static class Interceptor extends PrintStream {

        static final String FINAL_RESULT = "Final result:";
        static final String EXPORT = "EXP>";
        static final char DELIMITER = ';';
        static final String FILE_NAME = "results.csv";
        static final String NEW_LINE = "\n";

        boolean hasGameFinished = false;
        StringBuffer buffer;
        StringBuffer finalBuffer;

        final Runnable runnable = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            try {
                buffer.append(NEW_LINE);
                String s = finalBuffer.toString() + buffer.toString();
                Files.write(Paths.get(FILE_NAME), s.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.exit(0);
        };

        Interceptor(OutputStream out) {
            super(out, true);
            buffer = new StringBuffer();
            finalBuffer = new StringBuffer();
        }

        @Override
        public void print(String s) {
            boolean startsWithExport = s.trim().startsWith(EXPORT);
            if (s.trim().startsWith(FINAL_RESULT)) {
                hasGameFinished = true;
                new Thread(runnable).start();
            } else if (startsWithExport) {
                String info = s.replace(EXPORT, "");
                buffer.append(info).append(DELIMITER);
            } else if (hasGameFinished && s.startsWith(Client.TEAM_NAME)) {
                finalBuffer.append(s).append(DELIMITER);
            }

            if (!startsWithExport) {
                super.print(s);
            }
        }
    }
}
