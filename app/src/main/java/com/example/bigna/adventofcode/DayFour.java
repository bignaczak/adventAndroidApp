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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DayFour extends AppCompatActivity {
    private String myTag = "BTI_debug";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_four);

        TextView headerText = (TextView) findViewById(R.id.day4Header);
        headerText.setText("Day Four ;o)");


        Button firstTaskButton = (Button) findViewById(R.id.day4Compute);
        firstTaskButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                TextView solutionTV = (TextView) findViewById(R.id.day4Answer);
                solutionTV.setText("Calculating...");

                solveFirstTask();
            }
        });

        Button day4DoneButton = (Button) findViewById(R.id.day4Done);
        day4DoneButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    public void solveFirstTask(){
        String fileName = "/input_day4";
        BufferedReader readLine = prepareInputFileConversion(fileName);
        ArrayList<String> inputFileAsArray = new ArrayList<>();
        if (readLine!= null) {
            MainActivity.readFileIntoArrayList(myTag, readLine, inputFileAsArray);
            Log.d(myTag, Integer.toString(inputFileAsArray.size()) + " Records imported");
        }else{
            Log.d(myTag, "No BufferedReader Proided");
        }

        try{
            readLine.close();
        }catch (IOException e) {
            Log.d(myTag, "readLine not able to close" + e.getMessage());
        }
        int i = 0;
        String currentLine = inputFileAsArray.get(i);
        Log.d(myTag, "Processing: " + currentLine);
        ArrayList<GuardLogEvent> allEvents = createGuardLogEvents(currentLine);
        Log.d(myTag, Integer.toString(allEvents.size()) + " events added to log");




    }

    private ArrayList<GuardLogEvent> createGuardLogEvents(String line){
        ArrayList<GuardLogEvent> allEvents = new ArrayList<>();
        int dateEnd = line.indexOf("]")+1;
        int eventStart = dateEnd + 1;
        String datePart = line.substring(0,dateEnd);
        String eventPart = line.substring(eventStart, line.length());
        Log.d(myTag, datePart);
        Date timeStamp = convertDateString(datePart);
        Log.d(myTag, timeStamp.toString());
        GuardLogEvent event = new GuardLogEvent(timeStamp, eventPart);
        allEvents.add(event);

        return allEvents;
    }
    private BufferedReader prepareInputFileConversion(String fileName) {
        BufferedReader readLine = null;
        if (MainActivity.isExternalStorageWritable() && MainActivity.isStoragePermissionGranted(this)) {
            try {
                File myDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File myFile = new File(myDir, fileName);
                readLine = new BufferedReader(new FileReader(myFile));
            } catch (FileNotFoundException e) {
                Log.d(myTag, "File Not Found..." + e.getMessage());
            } catch (Exception e) {
                Log.d(myTag, "Other Exception" + e.getMessage());
            }
        }
        return readLine;
    }

    private Date convertDateString(String inputString){
        Date returnDate = null;
        SimpleDateFormat parser = new SimpleDateFormat("'['yyyy-MM-dd HH:mm']'");
        try{
            returnDate = parser.parse(inputString);
        }catch (ParseException e){
            Log.d(myTag, "Date Parser failed..." + e.getMessage());
        }
        return returnDate;
    }

    public class GuardLogEvent{
        private Date timeStamp;
        private String eventDescription;

        public GuardLogEvent(Date timeStamp, String eventDescription){
            this.timeStamp = timeStamp;
            this.eventDescription = eventDescription;
        }
    }
}
