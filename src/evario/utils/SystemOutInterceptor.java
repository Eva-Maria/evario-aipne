package evario.utils;

import evario.Config;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
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
            Files.write(Paths.get(Config.LOG_FILE_NAME), s.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Config.autoExit) {
            System.exit(0);
        }
    };

    public SystemOutInterceptor(OutputStream out) {
        super(out, true);
        buffer = new StringBuffer();
        finalBuffer = new StringBuffer();
    }

    @Override
    public void print(String s) {
        if (s.trim().startsWith(EXPORT)) {
            String info = s.replace(EXPORT, "");
            buffer.append(info).append(DELIMITER);
        } else if (s.trim().startsWith(FINAL_RESULT)) {
            hasGameFinished = true;
            new Thread(runnable).start();
        } else if (hasGameFinished && s.startsWith(Config.TEAM_NAME)) {
            finalBuffer.append(s).append(DELIMITER);
        }

        super.print(s);
    }
}
