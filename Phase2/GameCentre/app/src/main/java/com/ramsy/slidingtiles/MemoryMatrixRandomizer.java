package com.ramsy.slidingtiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MemoryMatrixRandomizer {
    int size;
    public MemoryMatrixRandomizer(int size){
        this.size = size;
    }

    public Set<Integer> randomizer(){
        Set<Integer> values = new HashSet<>();
        Random random = new Random();
        for(int i = 0; i<Math.ceil(size*0.25);i++){
            Integer id = random.nextInt(size);
            if (values.contains(id)){
                i--;
            }
            else{
                values.add(id);
            }
        }
        return values;
    }
}
