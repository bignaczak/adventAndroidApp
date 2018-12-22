package com.example.bigna.adventofcode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DayOne extends AppCompatActivity {

    private String myTag = "BTI_debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_one);

        TextView headerText = (TextView) findViewById(R.id.day1Header);
        headerText.setText("Day One_Part2");


        Button day1CalculateButton = (Button) findViewById(R.id.day1Calculate);
        day1CalculateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                TextView solutionTV = (TextView) findViewById(R.id.day1Solution);
                solutionTV.setText("Calculating...");

                executeSecondTask();
            }
        });

        Button day1DoneButton = (Button) findViewById(R.id.day1Done);
        day1DoneButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    private void executeSecondTask(){
        String fileName = "/input_day1.csv";
        ArrayList<String> inputFileAsArray = new ArrayList<>();
        readFileIntoArray(fileName, inputFileAsArray);
        Log.d(myTag, "Lines Read: " + Integer.toString(inputFileAsArray.size()));

        TextView solutionTV = (TextView) findViewById(R.id.day1Solution);
        solutionTV.setText(Integer.toString(inputFileAsArray.size()));

        ArrayList<Integer> frequencyChanges = new ArrayList<>();
        for(String s: inputFileAsArray){
            frequencyChanges.add(Integer.parseInt(s.trim()));
        }
        Log.d(myTag, "Integers in Array: " + Integer.toString(frequencyChanges.size()));

        int duplicateFrequency = getFirstDuplicateFrequency(frequencyChanges);
        Log.d(myTag, "First Duplicate: " + Integer.toString(duplicateFrequency));
        solutionTV.setText(Integer.toString(duplicateFrequency));

    }

    private int getFirstDuplicateFrequency(ArrayList<Integer> frequencyChanges){
        int firstDuplicateFrequency = 0;
        Integer currentFrequency=0;
        boolean duplicateFound = false;
        int i = 0;
        int totalShifts = 0;
        Integer currentShift;
        ArrayList <Integer> frequencyLog = new ArrayList<>();


        while (!duplicateFound && totalShifts < 2000000) {
            currentShift = frequencyChanges.get(i);
            currentFrequency += currentShift;
            i++;
            totalShifts++;

            if (frequencyLog.size()==0){
                frequencyLog.add(currentFrequency);  //add it to the list of experienced frequecies
            }else if (frequencyLog.contains(currentFrequency)){
                //Duplicate frequency found
                duplicateFound = true;
                firstDuplicateFrequency = currentFrequency;
            }else{
                frequencyLog.add(currentFrequency);  //add it to the list of experienced frequecies
            }

            if(i > frequencyChanges.size()-1) i=0;  //Reset i to go through the loop again
            if (totalShifts==1000) Log.d(myTag, "Exceed 1000!");
            if ((totalShifts%10000)==0) Log.d(myTag, "Exceed " +
                    Integer.toString(totalShifts) + "!");
//            if (totalShifts==10000) Log.d(myTag, "Exceed 10,000!");
//            if (totalShifts==20000) Log.d(myTag, "Exceed 20,000!");
//            if (totalShifts==30000) Log.d(myTag, "Exceed 30,000!");
//            if (totalShifts==100000) Log.d(myTag, "Exceed 100,000!");
            if (totalShifts==1000000) Log.d(myTag, "Exceed 1 Million!");
        }

        Log.d(myTag, "Total Iterations: " + Integer.toString(totalShifts));
        if(totalShifts == 2000000) Log.d(myTag, "No answer Found");
        return firstDuplicateFrequency;
    }
    private void readFileIntoArray(String fileName, ArrayList<String> inputFileAsArray){
        if (MainActivity.isExternalStorageWritable() && MainActivity.isStoragePermissionGranted(this)) {
            try {
                File myDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File myFile = new File(myDir, fileName);
                BufferedReader readLine = new BufferedReader(new FileReader(myFile));
                MainActivity.readFileIntoArrayList(myTag, readLine, inputFileAsArray);
                readLine.close();
            } catch (FileNotFoundException e) {
                Log.d(myTag, "File Not Found..." + e.getMessage());
            } catch (IOException e) {
                Log.d(myTag, "IO Exception..." + e.getMessage());
            } catch (Exception e) {
                Log.d(myTag, "Other Exception" + e.getMessage());
            }
        }
    }

}
