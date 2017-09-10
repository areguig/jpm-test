package io.areguig.jpmtest1.store;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;

import io.areguig.jpmtest1.to.Instruction;
import io.areguig.jpmtest1.to.InstructionType;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class InstructionStoreTest {

    @Test
    public void testStoreInstructions ()  throws Exception{
        Instruction inst1 = new Instruction();
        inst1.setEntity("foo");
        inst1.setType(InstructionType.B);
        inst1.setAgreedFx(0.5);
        inst1.setCurrency("SGP");
        inst1.setInstructionDate(LocalDate.of(2017,9,7));
        inst1.setSettlementDate(LocalDate.of(2017,9,8));
        inst1.setUnits(200);
        inst1.setPrice(100.25);

        Instruction inst2 = new Instruction();
        inst2.setEntity("bar");
        inst2.setType(InstructionType.S);
        inst2.setAgreedFx(0.22);
        inst2.setCurrency("AED");
        inst1.setInstructionDate(LocalDate.of(2017,9,7));
        inst1.setSettlementDate(LocalDate.of(2017,9,8));
        inst2.setUnits(450);
        inst2.setPrice(150.5);

        int prevSize=InstructionStore.findAll().size();
        InstructionStore.storeInstructions(Arrays.asList(inst1, inst2));

        assertEquals(2, InstructionStore.findAll().size()-prevSize);
    }

    @Test
    public void testDropAll(){
        Instruction inst1 = new Instruction();
        inst1.setEntity("foo");
        inst1.setType(InstructionType.B);
        inst1.setAgreedFx(0.5);
        inst1.setCurrency("SGP");
        inst1.setInstructionDate(LocalDate.of(2017,9,7));
        inst1.setSettlementDate(LocalDate.of(2017,9,8));
        inst1.setUnits(200);
        inst1.setPrice(100.25);

        InstructionStore.storeInstructions(Arrays.asList(inst1));
        InstructionStore.dropAll();
        assertTrue(InstructionStore.findAll().isEmpty());
    }
}
