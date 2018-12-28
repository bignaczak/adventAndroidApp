package com.example.bigna.adventofcode;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Task implements Comparable<Task> {
    private static List<Task> instances = new ArrayList<>();
    private static String myTag = "BTI_debug";
    private Character taskId;
    private boolean isComplete;
    private boolean isAssigned;
    private Integer completionTime;

    //Constructor made private to enforce existence check first
    private Task(Character taskId) {
        this.taskId = taskId;
        this.isComplete = false;
        this.isAssigned = false;
        instances.add(this);
    }

    //GETTERS
    public static List<Task> getInstances() { return instances; }

    public boolean isComplete() { return isComplete; }
    public int getCompletionTime() { return completionTime; }

    //SETTERS
    public void setIsComplete(boolean value) {this.isComplete = value;}
    public void setIsAssigned(boolean value) {this.isAssigned = value;}

    @Override
    public int compareTo(Task otherTask) {
        int result = this.taskId.compareTo(otherTask.taskId);
        return result;
    }

    @Override
    public String toString() {
        return Character.toString(taskId);
    }

    public static boolean taskExists(Character taskId) {
        boolean taskExists = false;
        for (Task t : instances) {
            if (taskId == t.taskId) taskExists = true;
        }
        return taskExists;
    }

    public static List<Task> getTasksReadyForCompletion(){
        List<Task> tasksReadyForCompletion = new ArrayList<>();
        for(Task task:instances){
            if(Instruction.countUnfinishedPredecessors(task)==0 && !task.isComplete && !task.isAssigned){
                tasksReadyForCompletion.add(task);
            }
        }

        return tasksReadyForCompletion;
    }


    //Overload method if sending task instead of character
    public static boolean taskExists(Task task) {
        return taskExists(task.taskId);
    }

    public static Task findById(Character taskId){
        for(Task t: instances){
            if (taskId == t.taskId) return t;
        }
        return null;
    }

    public static Task createIfNew(Character taskId){
        Task returnTask;
        if(!taskExists(taskId)) {
            returnTask = new Task(taskId);
        }else{
            returnTask = findById(taskId);
        }
        return returnTask;
    }

    public static void calculateTaskCompletionTimes(){
        int standardTime=60;
        int additionalTimeOffset = (int) 'A' - 1;  //Subtract this from the Char value
        for(Task t:instances){
            t.completionTime = standardTime + ((int) (t.taskId) - additionalTimeOffset);
            //Log.d(myTag, t.toString() + " duration: " + Integer.toString(t.completionTime) + " s");
        }
    }

    public static void resetCompletionFlags(){
        for(Task t:instances){
            t.isComplete = false;
        }
    }
}
