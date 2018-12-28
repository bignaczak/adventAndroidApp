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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class DayFour extends AppCompatActivity {
    private String myTag = "BTI_debug";
    private ArrayList<com.example.bigna.adventofcode.Guard> allGuards = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_four);

        TextView headerText = (TextView) findViewById(R.id.day4Header);
        headerText.setText("Day Four ;o)");


        Button firstTaskButton = (Button) findViewById(R.id.day4Compute);
        firstTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setAnswerField("Calculating...", "Calculating...");
                //TextView solutionTV = (TextView) findViewById(R.id.day4Answer);
                //solutionTV.setText("Calculating...");
                solveFirstTask();
            }
        });

        Button day4DoneButton = (Button) findViewById(R.id.day4Done);
        day4DoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    public void setAnswerField(String answer, String answer2){
        TextView solutionTV = (TextView) findViewById(R.id.day4Answer);
        solutionTV.setText(answer);
        TextView solutionTV2 = (TextView) findViewById(R.id.day4Answer2);
        solutionTV2.setText(answer2);

    }

    public void solveFirstTask() {
        String fileName = "/input_day4";
        BufferedReader readLine = prepareInputFileConversion(fileName);
        ArrayList<String> inputFileAsArray = new ArrayList<>();
        if (readLine != null) {
            MainActivity.readFileIntoArrayList(myTag, readLine, inputFileAsArray);
            Log.d(myTag, Integer.toString(inputFileAsArray.size()) + " Records imported");
        } else {
            Log.d(myTag, "No BufferedReader Provided");
        }

        try {
            readLine.close();
        } catch (IOException e) {
            Log.d(myTag, "readLine not able to close" + e.getMessage());
        }
        ArrayList<GuardLogEvent> allEvents = createGuardLogEvents(inputFileAsArray);
        ArrayList<GuardLogEvent> shiftStartEntries = getAllShiftStarts(allEvents);
        createArrayOfGuardIds(shiftStartEntries);  //creates array allGuards
        transferLogsIntoHeatMap(allEvents);
        Guard sleepiestGuard = getSleepiestGuard(allGuards);
        Log.d(myTag, "Sleepiest Guard: " + sleepiestGuard.toString());
        int sleepiestMinute = sleepiestGuard.getSleepiestMinute();
        Log.d(myTag, "Sleepiest minute for guard: " + sleepiestGuard.toString()
            + " is minute-->" + Integer.toString(sleepiestMinute));
        int answer = sleepiestGuard.getGuardId() * sleepiestMinute;
        setAnswerField(Integer.toString(answer), "Calculating...");

        int answer2 = calculateAnswer2();
        setAnswerField(Integer.toString(answer), Integer.toString(answer2));
        //Log.d(myTag, "First sleep log: " + Arrays.toString(allGuards.get(0).getSleepLog()));

        //Log.d(myTag, Integer.toString(allEvents.size()) + " events added to log");
        //Log.d(myTag, Integer.toString(shiftStartEntries.size()) + " Shift Start events identified");
        //Log.d(myTag, Integer.toString(allGuards.size()) + " Unique Guards");


    }

    private int calculateAnswer2(){

        int peakOccurrence = 0;
        int minuteAtPeakOccurrence = 0;
        Guard returnGuard = null;
        for(Guard g:allGuards){
            int[] currentMinuteWithOccurrence = g.getMinuteWithHighestSleepOccurrence();
            int currentOccurrence = currentMinuteWithOccurrence[1];
            int currentMinute = currentMinuteWithOccurrence[0];
            if(currentOccurrence>peakOccurrence){
                returnGuard = g;
                peakOccurrence = currentOccurrence;
                minuteAtPeakOccurrence = currentMinute;
            }
        }
        int answer2 = minuteAtPeakOccurrence * returnGuard.getGuardId();
        return answer2;
    }
    private Guard getSleepiestGuard(ArrayList<Guard> allGuards){
        //Loop through all to find one who sleeps most
        Guard sleepiestGuard = null;
        int mostMinutesAsleep=0;
        for(Guard g: allGuards){
            int currentSleepMinutes = g.getTotalSleep();
            if(currentSleepMinutes > mostMinutesAsleep){
                mostMinutesAsleep = currentSleepMinutes;
                sleepiestGuard = g;
            }
        }
        return sleepiestGuard;
    }

    private void transferLogsIntoHeatMap(ArrayList<GuardLogEvent> allEvents){
        int sleepStart=0;
        int sleepEnd;
        int guardId;
        Guard currentGuard=null;
        for(GuardLogEvent e: allEvents){
            if(e.eventDescription.contains("begins shift")){
                guardId=getGuardId(e);
                currentGuard=Guard.findById(guardId, allGuards);
            } else if(e.eventDescription.contains("falls asleep")){
                sleepStart = e.getEventMinute();
            } else if(e.eventDescription.contains("wakes up")){
                sleepEnd = e.getEventMinute();
                if (currentGuard != null){
                    currentGuard.applySleepEvent(sleepStart, sleepEnd);
                }
            } else{
                Log.d(myTag, "Uncaptured Event: " + e.toString());
            }
        }

    }
    private ArrayList<GuardLogEvent> createGuardLogEvents(ArrayList<String> inputFileAsArray) {
        ArrayList<GuardLogEvent> allEvents = new ArrayList<>();

        for(int i = 0; i<inputFileAsArray.size(); i++) {
            String currentLine = inputFileAsArray.get(i);
            //Log.d(myTag, "Processing: " + currentLine);

            int dateEnd = currentLine.indexOf("]") + 1;
            int eventStart = dateEnd + 1;
            int id=i;
            String datePart = currentLine.substring(0, dateEnd);
            String eventPart = currentLine.substring(eventStart, currentLine.length());
            //Log.d(myTag, datePart);
            Date timeStamp = convertDateString(datePart);
            //Log.d(myTag, timeStamp.toString());
            GuardLogEvent event = new GuardLogEvent(id, timeStamp, eventPart);
            allEvents.add(event);
        }

        Collections.sort(allEvents);  //sort by timestamp
        for(int i=0; i<30; i++){
            Log.d(myTag, "Event " + Integer.toString(i) + ":" + allEvents.get(i).toString());
        }



        //For parsing date parts
        /*
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeStamp);
        int month = calendar.get(Calendar.MONTH);  //Note: January is 0
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        Log.d(myTag, timeStamp.toString() + " the month is: " + Integer.toString(month));
        Log.d(myTag, "And the day is: " + Integer.toString(day));
        */
        return allEvents;
    }

    private ArrayList<GuardLogEvent> getAllShiftStarts(ArrayList<GuardLogEvent> allEvents){

        ArrayList<GuardLogEvent> shiftStartEntries = new ArrayList<>();
        for (GuardLogEvent e: allEvents){
            if(e.eventDescription.contains("begins shift")){
                shiftStartEntries.add(e);
            }
        }
        return shiftStartEntries;
    }

    private void createArrayOfGuardIds(ArrayList<GuardLogEvent> shiftStartEntries){

        for (GuardLogEvent e:shiftStartEntries) {
            int id = getGuardId(e);
            addGuardIfNeeded(id);
            //Log.d(myTag, "From " + eventDescription + "...ID->" + Integer.toString(id));
        }
    }

    private int getGuardId(GuardLogEvent e) {
        int idStart = e.eventDescription.indexOf("#") + 1;
        int idEnd = e.eventDescription.indexOf("begins") - 1;
        return Integer.parseInt(e.eventDescription.substring(idStart, idEnd));
    }

    private void addGuardIfNeeded(int id){
        boolean guardExists = false;
        for (com.example.bigna.adventofcode.Guard g: allGuards){
            if(g.getGuardId() == id) guardExists = true;
            break;
        }
        if (!guardExists) {
            Guard guard = new Guard(this, id);
            allGuards.add(guard);
        }
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

    private Date convertDateString(String inputString) {
        Date returnDate = null;
        SimpleDateFormat parser = new SimpleDateFormat("'['yyyy-MM-dd HH:mm']'");
        try {
            returnDate = parser.parse(inputString);
        } catch (ParseException e) {
            Log.d(myTag, "Date Parser failed..." + e.getMessage());
        }
        return returnDate;
    }

    public class GuardLogEvent implements Comparable<GuardLogEvent>{
        private int id;
        private Date timeStamp;
        private String eventDescription;
        private int eventMinute;
        private int shiftDate;
        private int shiftMonth;

        public GuardLogEvent(int id, Date timeStamp, String eventDescription) {
            this.id=id;
            this.timeStamp = timeStamp;
            this.eventDescription = eventDescription;

            //Figure out the shift date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(timeStamp);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            //Log.d(myTag, timeStamp.toString() + " the hour is: " + Integer.toString(hour));
            if (hour>22) {
                //bump to next day
                calendar.add(Calendar.DATE, 1);
            }

            int month = calendar.get(Calendar.MONTH);  //Note: January is 0
            int day = calendar.get(Calendar.DATE);
            this.shiftDate = day;
            this.shiftMonth = month;
            this.eventMinute = calendar.get(Calendar.MINUTE);

            //Log.d(myTag, timeStamp.toString() + " the month is: " + Integer.toString(month));
            //Log.d(myTag, "And the day is: " + Integer.toString(day));


        }
        //THE GETTERS
        public int getEventMinute(){return eventMinute;}


        @Override
        public String toString() {
            return timeStamp.toString() + "::" +"ShiftDate" +
                    Integer.toString(shiftMonth+1) + "/" + Integer.toString(shiftDate) +
                    " Min: " + Integer.toString(eventMinute)
                    + "::" + eventDescription;
        }

        @Override
        public int compareTo(GuardLogEvent otherEvent){
            int result = this.timeStamp.compareTo(otherEvent.timeStamp);
            return result;
        }

    }

}
