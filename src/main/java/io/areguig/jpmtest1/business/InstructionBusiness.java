package io.areguig.jpmtest1.business;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.areguig.jpmtest1.mapper.InstructionMapper;
import io.areguig.jpmtest1.store.InstructionStore;
import io.areguig.jpmtest1.to.Instruction;
import io.areguig.jpmtest1.to.InstructionType;

import static io.areguig.jpmtest1.mapper.InstructionMapper.toInstruction;
import static io.areguig.jpmtest1.store.InstructionStore.storeInstructions;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.toMap;

/**
 * Contains the business logic and rules of the application.
 */

public class InstructionBusiness {

    /**
     * Gets all the instructions in the store.
     *
     * @return a list of instructions
     */
    public static List<Instruction> findAll() {
        return InstructionStore.findAll();
    }

    /**
     * Returns the ranking of instructions entities based on incoming amount
     *
     * @return a map of entities and incoming amount sorted by incoming amount desc
     */
    public static Map<String, Double> getIncomingEntitiesRanking() {
        return  // Filter the instruction list to keep only the Sell instructions
                InstructionStore.findAll().stream().filter(i -> i.getType().equals(InstructionType.S))
                        // Group by entity summing the instruction amount (Price per unit * Units * Agreed Fx)
                        .collect(groupingBy(Instruction::getEntity, summingDouble(i -> i.getPrice() * i.getUnits() * i.getAgreedFx())))
                        // Sort the entry set by value (sum of instructions amount)
                        .entrySet().stream().sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                        //Collect everything into a new Map
                        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (prevVal, newVal) -> newVal, LinkedHashMap::new));
    }

    /**
     * Returns the ranking of instructions entities based on outgoing amount
     *
     * @return a map of entities and outgoing amount sorted by outgoing amount desc
     */
    public static Map<String, Double> getOutgoingEntitiesRanking() {
        return // Filter the instruction list to keep only the Buy instructions
                InstructionStore.findAll().stream().filter(i -> i.getType().equals(InstructionType.B))
                        // Group by entity summing the instruction amount (Price per unit * Units * Agreed Fx)
                        .collect(groupingBy(Instruction::getEntity, summingDouble(i -> i.getPrice() * i.getUnits() * i.getAgreedFx())))
                        // Sort the entry set by value (sum of instructions amount)
                        .entrySet().stream().sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                        //Collect everything into a new Map
                        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (prevVal, newVal) -> newVal, LinkedHashMap::new));
    }

    /**
     * Returns a report on amount in USD settled incoming everyday.
     */
    public static Map<LocalDate, Double> getDailyIncomingReport() {
        return // Filter the instruction list to keep only the Sell instructions
                InstructionStore.findAll().stream().filter(i -> i.getType().equals(InstructionType.S))
                        // Group by settlement date summing the instruction amount (Price per unit * Units * Agreed Fx)
                        .collect(groupingBy(Instruction::getSettlementDate, summingDouble(i -> i.getPrice() * i.getUnits() * i.getAgreedFx())));
    }

    /**
     * Returns a report on amount in USD settled outgoing everyday
     */
    public static Map<LocalDate, Double> getDailyOutgoingReport() {
        return // Filter the instruction list to keep only the Buy instructions
                InstructionStore.findAll().stream().filter(i -> i.getType().equals(InstructionType.B))
                        // Group by settlement date summing the instruction amount (Price per unit * Units * Agreed Fx)
                        .collect(groupingBy(Instruction::getSettlementDate, summingDouble(i -> i.getPrice() * i.getUnits() * i.getAgreedFx())));
    }

    /**
     * Loads instructions from a file into the application store.
     *
     * @param filePath the csv file location.
     * @return the number of stored instructions.
     */
    public static int loadDataFromFile(String filePath) throws Exception {
        List<Instruction> instructions = InstructionMapper.instructionsFromFile(filePath);
        updateAndStoreInstructions(instructions);
        return instructions != null ? instructions.size() : 0;
    }

    /**
     * Loads a sample data set of instructions in the application store.
     */
    public static void loadSampleData() {
        updateAndStoreInstructions(
                Arrays.asList(toInstruction("foo,B,0.5,SGP,01 Jan 2016,02 Jan 2016,200,100.25"),
                        toInstruction("bar,S,0.22,AED,05 Jan 2016,07 Jan 2016,450,150.5")));
    }

    /**
     * update and stores a list of instructions.
     *
     * @param instructions the instruction list
     * @return the number of stored instructions.
     */
    public static int updateAndStoreInstructions(List<Instruction> instructions) {
        return storeInstructions(instructions.stream().map(InstructionBusiness::checkAndUpdateSettlementDate).collect(Collectors.toList()));
    }

    /**
     * A method that updates an instruction based on the following business rules : - A work week
     * starts Monday and ends Friday, unless the currency of the trade is AED or SAR, where the work
     * week starts Sunday and ends Thursday. No other holidays to be taken into account. - A trade
     * can only be settled on a working day. - If an instructed settlement date falls on a weekend,
     * then the settlement date should be changed to the next working day.
     *
     * @param instruction The instruction to process.
     * @return the updated instruction.
     */
    public static Instruction checkAndUpdateSettlementDate(Instruction instruction) {
        DayOfWeek settlementDayOfWeek = instruction.getSettlementDate().getDayOfWeek();
        if ("AED".equalsIgnoreCase(instruction.getCurrency()) || "SAR".equalsIgnoreCase(instruction.getCurrency())) {
            if (DayOfWeek.FRIDAY.equals(settlementDayOfWeek) || DayOfWeek.SATURDAY.equals(settlementDayOfWeek)) {
                instruction.setSettlementDate(instruction.getSettlementDate().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)));
            }
        } else {
            if (DayOfWeek.SATURDAY.equals(settlementDayOfWeek) || DayOfWeek.SUNDAY.equals(settlementDayOfWeek)) {
                instruction.setSettlementDate(instruction.getSettlementDate().with(TemporalAdjusters.next(DayOfWeek.MONDAY)));
            }
        }
        return instruction;
    }
}
