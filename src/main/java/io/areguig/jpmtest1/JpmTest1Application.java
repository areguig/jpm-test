package io.areguig.jpmtest1;

import org.jline.builtins.Completers;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

import static io.areguig.jpmtest1.AppConstants.CMD_QUIT;
import static io.areguig.jpmtest1.AppConstants.PROMPT;
import static io.areguig.jpmtest1.AppConstants.RES_EXIT;
import static io.areguig.jpmtest1.CommandHandler.help;

/**
 * Application's main class
 */

public class JpmTest1Application {

    public static void main(String[] args) throws IOException {

        LineReader reader = LineReaderBuilder.builder()
                .terminal(TerminalBuilder.terminal()).completer(new Completers.FileNameCompleter())
                .parser(new DefaultParser()).build();

        print(help());
        while (true) {
            String line;
            try {
                line = reader.readLine(PROMPT, null);
            } catch (UserInterruptException e) {
                break;
            }
            if (line == null) {
                continue;
            }
            line = line.trim();
            if (CMD_QUIT.contains(line)) {
                break;
            }
            print(CommandHandler.handleCommand(reader.getParser().parse(line, 0)));
        }
        print(RES_EXIT);
    }

    static void print(String toPrint) {
        System.out.println(toPrint);
    }
}
