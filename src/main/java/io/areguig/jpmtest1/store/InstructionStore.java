package io.areguig.jpmtest1.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.areguig.jpmtest1.to.Instruction;

/**
 * This class represents an instruction store implemented
 * using a simple Map (because the application is executed in a single threaded environment).
 */

public class InstructionStore {


    private static  Map<Integer, Instruction> store = new HashMap<>();
    private static int id = 0;

    public static int storeInstructions(List<Instruction> instructions) {
        for(Instruction inst :instructions){
            id++;
            store.put(id,inst);
        }
        return instructions!=null?instructions.size():0;
    }

    public static List<Instruction> findAll(){
        return new ArrayList<>(store.values());
    }

    public static int dropAll(){
        int size = store.size();
        store.clear();
        return size;
    }
}
