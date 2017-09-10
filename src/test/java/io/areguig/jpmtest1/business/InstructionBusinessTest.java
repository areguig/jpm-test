package io.areguig.jpmtest1.business;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

import io.areguig.jpmtest1.store.InstructionStore;
import io.areguig.jpmtest1.to.Instruction;
import io.areguig.jpmtest1.to.InstructionType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class InstructionBusinessTest {

    @Test
    public void testLoadDataFromFile() throws Exception {
        int prevSize = InstructionBusiness.findAll().size();
        int inserted = InstructionBusiness.loadDataFromFile("src/test/resources/sample_data_test.csv");
        assertEquals(prevSize + inserted, InstructionBusiness.findAll().size());
    }

    @Test
    public void testLoadSampleData() throws Exception {
        int prevSize = InstructionBusiness.findAll().size();
        InstructionBusiness.loadSampleData();
        assertTrue(InstructionBusiness.findAll().size() > prevSize);
    }

    @Test
    public void testFindAll() throws Exception {
        assertNotNull(InstructionBusiness.findAll());
    }

    @Test
    public void testCheckAndUpdateSettlementDate() throws Exception {
        Instruction inst = new Instruction();
        LocalDate sdate = LocalDate.of(2017, 9, 3);
                
        inst.setEntity("foo");
        inst.setType(InstructionType.B);
        inst.setAgreedFx(0.5);
        inst.setCurrency("SGP");
        inst.setInstructionDate(LocalDate.of(2017,9,2));
        inst.setSettlementDate(sdate);
        inst.setUnits(200);
        inst.setPrice(100.25);

        //if the settlement date in on sunday
        assertEquals(inst.getSettlementDate().getDayOfWeek(), DayOfWeek.SUNDAY);
        InstructionBusiness.checkAndUpdateSettlementDate(inst);

        // The settlement date is updated to the next monday.
        assertNotEquals(inst.getSettlementDate().getDayOfWeek(), DayOfWeek.SATURDAY);
        assertNotEquals(inst.getSettlementDate().getDayOfWeek(), DayOfWeek.SUNDAY);
        assertEquals(inst.getSettlementDate(),sdate.with(TemporalAdjusters.next(DayOfWeek.MONDAY)));

        // if the currency is AED or SAR the week starts on SUNDAY and ends on Thursday.
        inst.setCurrency("AED");
        sdate = LocalDate.of(2017, 9, 8);
        inst.setSettlementDate(sdate);
        assertEquals(inst.getSettlementDate().getDayOfWeek(), DayOfWeek.FRIDAY);
        InstructionBusiness.checkAndUpdateSettlementDate(inst);
        assertNotEquals(inst.getSettlementDate().getDayOfWeek(), DayOfWeek.FRIDAY);
        assertEquals(inst.getSettlementDate(),sdate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)));

        inst.setCurrency("SAR");
        sdate = LocalDate.of(2017, 9, 2);
        inst.setSettlementDate(sdate);
        assertEquals(inst.getSettlementDate().getDayOfWeek(), DayOfWeek.SATURDAY);
        InstructionBusiness.checkAndUpdateSettlementDate(inst);
        assertNotEquals(inst.getSettlementDate().getDayOfWeek(), DayOfWeek.SATURDAY);
        assertEquals(inst.getSettlementDate(),sdate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)));

        // If the settlement date is during a week day, it is not updated.
        inst.setCurrency("SAR");
        sdate = LocalDate.of(2017, 9, 4);
        inst.setSettlementDate(sdate);
        assertEquals(inst.getSettlementDate().getDayOfWeek(), DayOfWeek.MONDAY);
        InstructionBusiness.checkAndUpdateSettlementDate(inst);
        assertEquals(inst.getSettlementDate().getDayOfWeek(), DayOfWeek.MONDAY);
        assertEquals(inst.getSettlementDate(),sdate);
    }

    @Test
    public void testGetIncomingEntitiesRanking() throws Exception {
        // First clean the store.
        InstructionStore.dropAll();
        assertTrue(InstructionBusiness.findAll().isEmpty());

        assertTrue(InstructionBusiness.getOutgoingEntitiesRanking().isEmpty());
        // Load the file containing the report test data.
        InstructionBusiness.loadDataFromFile("src/test/resources/sample_data_report_test.csv");

        Map<String, Double> result = InstructionBusiness.getIncomingEntitiesRanking();
        assertFalse(result.isEmpty());
        assertTrue(result.containsKey("first_S"));
        assertTrue(result.containsKey("second_S"));
        assertFalse(result.containsKey("first_B"));
        assertFalse(result.containsKey("second_B"));
        // TODO test the map is sorted
    }

    @Test
    public void testGetOutgoingEntitiesRanking() throws Exception {
        // First clean the store.
        InstructionStore.dropAll();
        assertTrue(InstructionBusiness.findAll().isEmpty());

        assertTrue(InstructionBusiness.getOutgoingEntitiesRanking().isEmpty());
        // Load the file containing the report test data.
        InstructionBusiness.loadDataFromFile("src/test/resources/sample_data_report_test.csv");

        Map<String, Double> result = InstructionBusiness.getOutgoingEntitiesRanking();
        assertFalse(result.isEmpty());
        assertTrue(result.containsKey("first_B"));
        assertTrue(result.containsKey("second_B"));
        assertFalse(result.containsKey("first_S"));
        assertFalse(result.containsKey("second_S"));
        // TODO test the map is sorted
    }

    @Test
    public void testGetDailyIncomingReport()throws Exception{
        // First clean the store.
        InstructionStore.dropAll();
        assertTrue(InstructionBusiness.findAll().isEmpty());

        assertTrue(InstructionBusiness.getDailyIncomingReport().isEmpty());
        // Load the file containing the report test data.
        InstructionBusiness.loadDataFromFile("src/test/resources/sample_data_report_test.csv");

        Map<LocalDate, Double> result = InstructionBusiness.getDailyIncomingReport();
        assertFalse(result.isEmpty());
        assertEquals(5,result.size());// The file contains 5 distinct settlement dates (Incoming).
    }

    @Test
    public void testGetDailyOutgoingReport()throws Exception{
        // First clean the store.
        InstructionStore.dropAll();
        assertTrue(InstructionBusiness.findAll().isEmpty());

        assertTrue(InstructionBusiness.getDailyOutgoingReport().isEmpty());
        // Load the file containing the report test data.
        InstructionBusiness.loadDataFromFile("src/test/resources/sample_data_report_test.csv");

        Map<LocalDate, Double> result = InstructionBusiness.getDailyOutgoingReport();
        assertFalse(result.isEmpty());
        assertEquals(6,result.size());// The file contains 6 distinct settlement dates (Outgoing).
    }
}