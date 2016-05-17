package evario.utils;

import evario.Config;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by m on 5/17/16.
 */
public class SystemOutInterceptor extends PrintStream {

    static final String FINAL_RESULT = "Final result:";
    static final String EXPORT = "EXP>";
    static final char DELIMITER = ';';
    static final String NEW_LINE = "\n";

    boolean hasGameFinished = false;

    final Path path;
    final StringBuffer buffer;
    final Runnable stopGame = () -> {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        System.exit(0);
    };

    public SystemOutInterceptor(OutputStream out) {
        super(out, true);
        buffer = new StringBuffer();
        path = Paths.get(Config.LOG_FILE_NAME);
    }

    @Override
    public void print(final String s) {
        if (s.trim().startsWith(EXPORT)) {
            String info = s.replace(EXPORT, "");
            buffer.append(info).append(DELIMITER);
            writeToFile(info);
        } else if (s.trim().startsWith(FINAL_RESULT)) {
            hasGameFinished = true;
            if (Config.autoExit) {
                new Thread(stopGame).start();
            }
        } else if (hasGameFinished && s.startsWith(Config.TEAM_NAME)) {
            buffer.append(s).append(DELIMITER);
        }

        super.print(s);
    }

    void writeToFile(final String s) {
        try {
            buffer.append(NEW_LINE);
            Files.write(path, s.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
