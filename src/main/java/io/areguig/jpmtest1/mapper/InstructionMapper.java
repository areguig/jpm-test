package io.areguig.jpmtest1.mapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.stream.Collectors;

import io.areguig.jpmtest1.to.Instruction;
import io.areguig.jpmtest1.to.InstructionType;

/**
 * Utility methods for instruction mapping from file (coming from external systems).
 */
public class InstructionMapper {

    private static final String COMMA = ",";

    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive()
            .appendPattern("dd MMM yyyy")
            .toFormatter();

    /**
     * Maps a well formed csv file to an Instruction list
     *
     * @param filePath the file location path.
     * @return the list of the mapped Instructions from the file.
     */
    public static List<Instruction> instructionsFromFile(String filePath) throws Exception {

        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        List<Instruction> instructions = br.lines().skip(1).map(InstructionMapper::toInstruction).collect(Collectors.toList());
        br.close();
        return instructions;
    }

    /**
     * Map a comma separated line from a csv file to an Instruction.
     *
     * @param line the line
     * @return the mapped Instruction from the line.
     */
    public static Instruction toInstruction(String line) {
        String[] splittedLine = line.split(COMMA);
        Instruction instruction = new Instruction();
        instruction.setEntity(splittedLine[0]);
        instruction.setType(InstructionType.valueOf(splittedLine[1]));
        instruction.setAgreedFx(Double.valueOf(splittedLine[2]));
        instruction.setCurrency(splittedLine[3]);
        instruction.setInstructionDate(LocalDate.parse(splittedLine[4], formatter));
        instruction.setSettlementDate(LocalDate.parse(splittedLine[5], formatter));
        instruction.setUnits(Double.valueOf(splittedLine[6]));
        instruction.setPrice(Double.valueOf(splittedLine[7]));
        return instruction;
    }
}
