package io.areguig.jpmtest1;

import java.util.Arrays;
import java.util.List;

public class AppConstants {
     static final String PROMPT = "JPMTest > ";

    // commands
    static final String CMD_REPORT = "report";
    static final String CMD_LOAD = "load";
    static final String CMD_SHOWALL = "showall";
    static final String CMD_HELP = "help";
    static final List<String> CMD_QUIT = Arrays.asList("quit", "exit", "q");
    // arguments

    static final String ARG_SAMPLE = "sample";

    // Results
    static final String RES_HELP = "Supported commands : \n" +
            "\thelp\t\t displays the current text.\n" +
            "\tload <file>.csv\t\tloads the data from a comma separated csv file with header (entity,type,agreedfx,currency,instructionDate,settlementDate,units,price) " +
            "\n\t\t\t(or just use sample as file name to load a sample data set)\n" +
            "\tshowall\t\tdisplays all the instructions.\n" +
            "\treport\t\tdisplays a report based on all the instructions.\n"+
            "\texit/quit/q\t\tcloses the application.";

    static final String RES_UNKNOWN = "Unknown command `%s`.\n" + RES_HELP;

    static final String RES_ERROR = "An error occurred : %s";

    static final String RES_LOAD_SAMPLE = "Sample data loaded, use showall command to display store content.";

    static final String RES_LOAD_FILE = "loaded %s instruction(s) from file %s";

    static final String RES_REPORT = "Daily outgoing amount(USB)\t%s\n"+
            "Daily incoming amount(USD)\t%s\n"+
            "Entities ranking based on incoming amount\t%s\n"+
            "Entities ranking based on outgoing amount\t%s";

    static final String RES_EXIT="Bye Bye :).";

}
