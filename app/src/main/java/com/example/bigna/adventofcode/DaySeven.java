package com.example.bigna.adventofcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DaySeven extends AppCompatActivity {
    private String myTag = "BTI_debug";
    private static int tasksCompleted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_seven);

        TextView headerText = (TextView) findViewById(R.id.day6Header);
        headerText.setText(getResources().getString(R.string.dayHeader) + "7 ;o)");

        Button firstTaskButton = (Button) findViewById(R.id.day7Compute);
        firstTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setAnswerFields("Calculating...", "Calculating...");
                solveTasks();
            }
        });

        Button dayDoneButton = (Button) findViewById(R.id.dayDone);
        dayDoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }

    public void setAnswerFields(String answer, String answer2){
        TextView solutionTV = (TextView) findViewById(R.id.day7Answer);
        solutionTV.setText(answer);
        TextView solutionTV2 = (TextView) findViewById(R.id.day7Answer2);
        solutionTV2.setText(answer2);
    }

    private void solveTasks() {
        String inputFileName = "/input_day7";
        ArrayList<String> inputFileAsArray = MainActivity.getInputFileAsArray(myTag, inputFileName, this);

        generateInstructionObjects(inputFileAsArray);
        Log.d(myTag, "Instructions Generated--> " + Integer.toString(Instruction.getInstances().size()));
        Log.d(myTag, "Total Number of Tasks =  " + Integer.toString(Task.getInstances().size()));

        List<Task> taskCompletionOrder = new ArrayList<>();

        List<Task> tasksReadyForCompletion = Task.getTasksReadyForCompletion();
        while (tasksReadyForCompletion.size()>0) {
            if (tasksReadyForCompletion.size() == 0) {
                Log.d(myTag, "No tasks ready");
            } else {
                Collections.sort(tasksReadyForCompletion);
                ListIterator<Task> listIterator = tasksReadyForCompletion.listIterator();
                Task toDoTask = listIterator.next();
                toDoTask.setIsComplete(true);
                taskCompletionOrder.add(toDoTask);
                Log.d(myTag, toDoTask.toString() + " remove____");
                listIterator.remove();
                //for (Task t : tasksReadyForCompletion) Log.d(myTag, t.toString() + " is Ready!!");
            }
            tasksReadyForCompletion = Task.getTasksReadyForCompletion();
        }
        Log.d(myTag, Integer.toString(taskCompletionOrder.size()) + " items completed in this order...");
        StringBuilder builder = new StringBuilder();
        for (Task t: taskCompletionOrder){
            builder.append(t.toString());
        }
        Log.d(myTag, builder.toString());
        setAnswerFields(builder.toString(), "");


        //////////////////////////////////////////////////
        //SECOND TASK
        //////////////////////////////////////////////////

        Task.calculateTaskCompletionTimes();
        Task.resetCompletionFlags();
        List<Worker> myWorkers = generateCrew(5);
        List<Task> taskCompletionOrderPartTwo = new ArrayList<>();

        //At a given timestep, see if there are tasks and workers available
        int currentTime = 0;
        tasksReadyForCompletion = Task.getTasksReadyForCompletion();
        Collections.sort(tasksReadyForCompletion);

        while(tasksCompleted < Task.getInstances().size()) {
            //Generate list of tasks ready for completion
            List<Worker> idleWorkers = getIdleWorkers(myWorkers);
            Log.d(myTag, "t"+Integer.toString(currentTime) + ": " +
                    Integer.toString(tasksReadyForCompletion.size()) + " tasks ready and " +
                    Integer.toString(idleWorkers.size()) + " workers available");

            //While both are available, assign tasks
            assignTasks(currentTime, tasksReadyForCompletion, idleWorkers);

            //Figure out when the next task will be completed,
            // which will either free resources or relieve dependencies
            currentTime = findNextTaskCompletion(myWorkers);
            completeTasks(currentTime, myWorkers, taskCompletionOrderPartTwo);

            tasksReadyForCompletion = Task.getTasksReadyForCompletion();
            Collections.sort(tasksReadyForCompletion);

        }

        StringBuilder builder2 = new StringBuilder();
        for (Task t: taskCompletionOrderPartTwo){
            builder2.append(t.toString());
        }
        Log.d(myTag, builder2.toString());
        setAnswerFields(builder.toString(), Integer.toString(currentTime));

    }

    private void completeTasks(int currentTime, List<Worker> myWorkers, List<Task> taskCompletionOrderPartTwo){

        for(Worker w:myWorkers){
            if (w.getEndTime() == currentTime){
                taskCompletionOrderPartTwo.add(w.getCurrentTask());
                tasksCompleted++;
                Log.d(myTag, "Just Completed task " + w.getCurrentTask().toString() +
                    ", " + Integer.toString(tasksCompleted) + " tasks completed total");
                w.completeCurrentTask();
            }
        }
    }

    private int findNextTaskCompletion(List<Worker> myWorkers){
        boolean firstPass = true;
        int firstCompletion = -1;
        for(Worker w:myWorkers){
            if(w.getEndTime() == 0){
                //do nothing, worker is idle
            }else if(w.getEndTime() < firstCompletion | firstPass){
                firstCompletion = w.getEndTime();
                firstPass = false;
            }
        }

        return firstCompletion;
    }
    private void assignTasks(int currentTime, List<Task> tasksReadyForCompletion, List<Worker> idleWorkers) {

        while (idleWorkers.size()>0 && tasksReadyForCompletion.size()>0) {
            ListIterator<Task> taskIterator = tasksReadyForCompletion.listIterator();
            Task toDoTask = taskIterator.next();

            ListIterator<Worker> workerIterator = idleWorkers.listIterator();
            Worker assignee = workerIterator.next();

            assignee.setCurrentTask(toDoTask, currentTime);

            taskIterator.remove();
            workerIterator.remove();
        }

    }
    private List<Worker> getIdleWorkers(List<Worker> myWorkers){
        List<Worker> idleWorkers = new ArrayList<>();
        for (Worker w:myWorkers){
            if(w.getCurrentTask() == null) idleWorkers.add(w);
        }
        return idleWorkers;
    }

    private List<Worker> generateCrew(int crewSize){
        for (int i=0; i<crewSize; i++) {
            Worker worker = new Worker();
        }
        return Worker.getAllWorkers();
    }
    private Character[][] parseTaskAndPredecessor(ArrayList<String> inputFileAsArray){
        int listLength = inputFileAsArray.size();
        Character[][] taskAndPredecessorList = new Character[listLength][2];
        int i = 0;
        int taskIndex = 7;
        int predecessorIndex = 1;
        for(String line:inputFileAsArray){
            String[] lineArray = line.split(" ");
            taskAndPredecessorList[i][0] = (lineArray[taskIndex]).charAt(0);
            taskAndPredecessorList[i][1] = (lineArray[predecessorIndex]).charAt(0);
            i++;
        }
        return taskAndPredecessorList;
    }

    private void generateInstructionObjects(ArrayList<String> inputFileAsArray){
        //Now create array of [TASK, PREDECESSOR]
        Character[][] inputTaskPredecessor = parseTaskAndPredecessor(inputFileAsArray);
        Log.d(myTag, "Instructions to be processed: " + Integer.toString(inputTaskPredecessor.length));
        //Generate Instruction objects
        for (int j=0; j<inputTaskPredecessor.length; j++){
            Character objective = inputTaskPredecessor[j][0];
            Character predecessor = inputTaskPredecessor[j][1];
            Log.d(myTag, Integer.toString(j) + ") About to generate for o--p " + objective + "--" + predecessor);
            boolean wasCreated = Instruction.generateInstruction(objective, predecessor);
            Log.d(myTag, Integer.toString(j) + ") back in main loop");

            if (!wasCreated) {
                Log.d(myTag, "Failed to Generate instruction for " + objective +
                        ", " + predecessor);
            }else{
                Log.d(myTag, "Created Successfully");
            }
        }

    }
}

