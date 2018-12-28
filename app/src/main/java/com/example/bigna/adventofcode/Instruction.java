package com.example.bigna.adventofcode;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Instruction implements Comparable<Instruction> {
    private static List<Instruction> instances = new ArrayList<>();
    private static Integer count=0;
    private Integer id;
    private Task objective;
    private List<Task> predecessors = new ArrayList<>();
    private List<Task> pendingPredecessors= new ArrayList<>();

    //CONSTRUCTORS
    //This constructor is private to force existence check of tasks
    private Instruction(Task objective, Task predecessor){
        count++;
        this.id = count;
        this.objective = objective;
        this.predecessors.add(predecessor);
        instances.add(this);
    }

    //GETTERS
    public static List<Instruction> getInstances(){ return instances; }

    //OVERRIDES
    @Override
    public int compareTo(Instruction otherInstruction){
        int result = this.id.compareTo(otherInstruction.id);
        return result;
    }

    //STATIC METHODS
    public static boolean generateInstruction(Character objective, Character predecessor){
        boolean wasCreated = false;

        //Make sure the 2 tasks exist;
        Task taskObjective = Task.createIfNew(objective);
        Task taskPredecessor = Task.createIfNew(predecessor);

        if (taskObjective!=null && taskPredecessor!=null){
            Log.d("BTI_debug", "Tasks returned successfully");
            //Get handle on existing instruction if objective already exists
            Instruction instruction = Instruction.getInstructionIfObjectiveExists(taskObjective);
            if(instruction !=null){
                instruction.addPredecessor(taskPredecessor);
                Log.d("BTI_debug", "Predecessor added");
            }else {
                instruction = new Instruction(taskObjective, taskPredecessor);
                Log.d("BTI_debug", "Instruction generated");
            }
            wasCreated = true;
        } else{
            Log.d("BTI_debug", "Tasks not returned");
        }
        return wasCreated;
    }

    private static Instruction getInstructionIfObjectiveExists(Task objective){
        Instruction returnInstruction = null;
        for (Instruction i:instances){
            if (objective == i.objective){
                returnInstruction = i;
            }
        }
        return returnInstruction;
    }

    public static Integer countUnfinishedPredecessors(Task task){
        Integer unfinishedPredecessors = 0;
        Instruction instruction = Instruction.getInstructionIfObjectiveExists(task);
        if (instruction == null) {
            unfinishedPredecessors = 0;
        }else {
            for (Task predecessor : instruction.predecessors) {
                if (!predecessor.isComplete()) unfinishedPredecessors++;
            }
        }
        return unfinishedPredecessors;
    }


    private void addPredecessor(Task predecessor){
        this.predecessors.add(predecessor);
    }





}
