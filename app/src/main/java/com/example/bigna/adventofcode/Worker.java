package com.example.bigna.adventofcode;

import com.example.bigna.adventofcode.Task;

import java.util.ArrayList;
import java.util.List;

public class Worker {
    private static int count=0;
    private static List<Worker> allWorkers = new ArrayList<>();
    private int id;
    private Task currentTask;
    private int tasksCompleted;
    private int startTime;
    private int endTime;

    public Worker(){
        count++;
        this.id = count;
        this.currentTask = null;
        this.startTime = 0;
        this.endTime = 0;
        this.tasksCompleted = 0;
        allWorkers.add(this);
    }

    //Getters
    public static List<Worker> getAllWorkers(){return allWorkers;}
    public Task getCurrentTask(){return currentTask;}
    public int getEndTime(){return endTime;}


    //Setters
    public void setCurrentTask(Task task, int startTime){
        this.currentTask = task;
        this.startTime = startTime;
        this.endTime = startTime + task.getCompletionTime();
        task.setIsAssigned(true);
    }


    public void completeCurrentTask(){
        //now mark task as complete
        this.currentTask.setIsComplete(true);

        //reset worker task state
        this.currentTask = null;
        this.tasksCompleted++;
        this.startTime=0;
        this.endTime=0;
    }
    public static Worker findById(int id){
        Worker worker = null;
        for(Worker w: allWorkers){
            if(w.id == id){
                worker = w;
            }
        }
        return worker;
    }
}
