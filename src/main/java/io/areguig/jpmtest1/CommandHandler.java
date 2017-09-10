package io.areguig.jpmtest1;

import org.jline.reader.ParsedLine;

import java.util.stream.Collectors;

import static io.areguig.jpmtest1.AppConstants.ARG_SAMPLE;
import static io.areguig.jpmtest1.AppConstants.CMD_HELP;
import static io.areguig.jpmtest1.AppConstants.CMD_LOAD;
import static io.areguig.jpmtest1.AppConstants.CMD_REPORT;
import static io.areguig.jpmtest1.AppConstants.CMD_SHOWALL;
import static io.areguig.jpmtest1.AppConstants.RES_ERROR;
import static io.areguig.jpmtest1.AppConstants.RES_HELP;
import static io.areguig.jpmtest1.AppConstants.RES_LOAD_FILE;
import static io.areguig.jpmtest1.AppConstants.RES_LOAD_SAMPLE;
import static io.areguig.jpmtest1.AppConstants.RES_REPORT;
import static io.areguig.jpmtest1.AppConstants.RES_UNKNOWN;
import static io.areguig.jpmtest1.business.InstructionBusiness.findAll;
import static io.areguig.jpmtest1.business.InstructionBusiness.getDailyIncomingReport;
import static io.areguig.jpmtest1.business.InstructionBusiness.getDailyOutgoingReport;
import static io.areguig.jpmtest1.business.InstructionBusiness.getIncomingEntitiesRanking;
import static io.areguig.jpmtest1.business.InstructionBusiness.getOutgoingEntitiesRanking;
import static io.areguig.jpmtest1.business.InstructionBusiness.loadDataFromFile;
import static io.areguig.jpmtest1.business.InstructionBusiness.loadSampleData;

/**
 * This class handle the commands and formats the command's result.
 */
public class CommandHandler {

    static String help() {
        return RES_HELP;
    }

    static String handleCommand(ParsedLine parsedLine) {
        String result;
        String command = parsedLine.word();
        try {
            if (CMD_HELP.equalsIgnoreCase(command)) {
                result = RES_HELP;
            } else if (CMD_LOAD.equalsIgnoreCase(command)) {
                String arg = parsedLine.words().get(1);
                if (ARG_SAMPLE.equalsIgnoreCase(arg)) {
                    loadSampleData();
                    result = RES_LOAD_SAMPLE;
                } else {
                    result = String.format(RES_LOAD_FILE, loadDataFromFile(arg), arg);
                }
            } else if (CMD_SHOWALL.equalsIgnoreCase(command)) {
                result = findAll().stream().map(Object::toString).collect(Collectors.joining(",\n"));
            } else if (CMD_REPORT.equalsIgnoreCase(command)) {
                result = String.format(RES_REPORT, getDailyOutgoingReport(), getDailyIncomingReport(),
                        getOutgoingEntitiesRanking(), getIncomingEntitiesRanking());
            } else {
                result = String.format(RES_UNKNOWN, command);
            }
        }catch (Exception e){ // catch any Exception an return an error result containing the exception message.
            result = String.format(RES_ERROR,e.getMessage());
        }
        return result;
    }
}
