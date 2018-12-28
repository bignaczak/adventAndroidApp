package com.example.bigna.adventofcode;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    String myTag = "BTI_DEBUG";
    public Context myContext;

    public Context getMyContext(){return myContext;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.day2_text);
        myContext = this;
        tv.setText("jojo");
        Button day2Button = (Button) findViewById(R.id.day2Button);
        day2Button.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                day2();
                //setContentView(R.layout.activity_main);
            }
        });

        Button gotoDay1Button = findViewById(R.id.gotoDay1);
        gotoDay1Button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(view.getContext(), DayOne.class);
                startActivityForResult(myIntent,0);
            }
        });


        Button gotoDay3Button = findViewById(R.id.gotoDay3);
        gotoDay3Button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(view.getContext(), DayThree.class);
                startActivityForResult(myIntent,0);
            }
        });

        Button gotoDay4Button = findViewById(R.id.gotoDay4);
        gotoDay4Button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(view.getContext(), DayFour.class);
                startActivityForResult(myIntent,0);
            }
        });

        Button gotoDay5Button = findViewById(R.id.gotoDay5);
        gotoDay5Button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(view.getContext(), DayFive.class);
                startActivityForResult(myIntent,0);
            }
        });
        Button gotoDay6Button = findViewById(R.id.gotoDay6);
        gotoDay6Button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(view.getContext(), DaySix.class);
                startActivityForResult(myIntent,0);
            }
        });
        Button gotoDay7Button = findViewById(R.id.gotoDay7);
        gotoDay7Button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(view.getContext(), DaySeven.class);
                startActivityForResult(myIntent,0);
            }
        });


    }

    public void day2() {
        Log.d(myTag, "Hello World");
        BufferedReader readLine = null;
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.day2_text);
        textView.setText("Sending...");
        ArrayList<String> inputFileAsArray = new ArrayList<>();
        if (isExternalStorageWritable()) {
            if (isStoragePermissionGranted(this)) {
                try {
                    File myDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                    File myFile = new File(myDir, "/day2_input");
                    readLine = new BufferedReader(new FileReader(myFile));
                    readFileIntoArrayList(myTag,readLine, inputFileAsArray);
                    readLine.close();

                    //int checkSum = calculateCheckSum(inputFileAsArray);
                    //textView.setText(Integer.toString(checkSum));



                } catch (FileNotFoundException e) {
                    Log.d(myTag, "File Not Found..." + e.getMessage());
                } catch (IOException e) {
                    Log.d(myTag, "IO Exception..." + e.getMessage());
                } catch (Exception e) {
                    Log.d(myTag, "Other Exception" + e.getMessage());
                }
            }
        }

        //Take the first line and count the intersection with every other line
        //Find which line matches all but 1 character
        int[] similarButOneChar = new int[2];
        similarButOneChar = findSimilarLines(inputFileAsArray);
        if(similarButOneChar[0] == 0 && similarButOneChar[1] == 0) {
            Log.d(myTag, "No matching IDs were found");
        } else{
            Log.d(myTag, "Matching IDs found!!!!");
            String line1 = inputFileAsArray.get(similarButOneChar[0]);
            String line2 = inputFileAsArray.get(similarButOneChar[1]);
            Log.d(myTag, "First One: " + line1);
            Log.d(myTag, "Second One: " + line2);
            String matchingCharacters = findIntersection(line1, line2);
            textView.setText(matchingCharacters);
            Log.d(myTag, matchingCharacters);
        }
    }

    private int[] findSimilarLines(ArrayList<String> inputFileAsArray){
        //this starts with the first line and compares it to all other lines
        for (int i = 0; i<inputFileAsArray.size()-1; i++){
            char[] baseCompare = inputFileAsArray.get(i).toCharArray();
            //Start the inner loop at the line after the base line
            for(int j = i+1; j<inputFileAsArray.size(); j++) {
                char[] compareTo = inputFileAsArray.get(j).toCharArray();
                //Skip the comparison if they aren't the same length
                if (baseCompare.length != compareTo.length) continue;
                int similarCharCount = countSimilarChars(baseCompare, compareTo);
                //If all but 1 character is the same
                if (similarCharCount == (baseCompare.length-1)){
                    return new int[]{i,j};  //return the similar row indexes
                }
            }
        }
        //If the similar lines weren't found, return 0,0
        return new int[]{0,0};
    }

    private static String findIntersection(String a, String b){
        char[] matchingChars = new char[a.length()-1];
        char[] aArray = a.toCharArray();
        char[] bArray = b.toCharArray();
        int j=0;
        for(int i=0; i<aArray.length; i++){
            char currentChar = aArray[i];
            if(currentChar == bArray[i]){
                matchingChars[j] = currentChar;
                j++;
            }
        }
        return String.valueOf(matchingChars);
    }
    private int countSimilarChars(char[] base, char[] compareTo){
        int similarChars = 0;
        for(int i=0; i<base.length; i++){
            if(base[i] == compareTo[i]) similarChars++;
        }
        return similarChars;
    }

    public static BufferedReader prepareInputFileConversion(String logTag,String fileName, AppCompatActivity activity) {
        BufferedReader readLine = null;
        if (MainActivity.isExternalStorageWritable() && MainActivity.isStoragePermissionGranted(activity)) {
            try {
                File myDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File myFile = new File(myDir, fileName);
                readLine = new BufferedReader(new FileReader(myFile));
            } catch (FileNotFoundException e) {
                Log.d(logTag, "File Not Found..." + e.getMessage());
            } catch (Exception e) {
                Log.d(logTag, "Other Exception" + e.getMessage());
            }
        }
        return readLine;
    }


    public static void readFileIntoArrayList(String logTag, BufferedReader readLine, ArrayList<String> inputFileAsArray) {
        try {
            int i = 0;
            for (String line = readLine.readLine(); line != null; line = readLine.readLine()) {
                inputFileAsArray.add(line);
                //Log.d(logTag, "Line #" + Integer.toString(i));
                i++;
            }
        } catch (IOException e) {
            Log.d(logTag, e.getMessage());
        }
        Log.d(logTag, Integer.toString(inputFileAsArray.size()) + " lines read into array");
    }

    public static ArrayList<String> getInputFileAsArray(String logTag, String fileName, AppCompatActivity activity){
        ArrayList<String> inputFileAsArray = new ArrayList<>();
        BufferedReader readLine = MainActivity.prepareInputFileConversion(logTag, fileName, activity);

        if (readLine != null) {
            MainActivity.readFileIntoArrayList(logTag, readLine, inputFileAsArray);
            Log.d(logTag, Integer.toString(inputFileAsArray.size()) + " Records imported");
        } else {
            Log.d(logTag, "No BufferedReader Provided");
        }

        try {
            readLine.close();
        } catch (IOException e) {
            Log.d(logTag, "readLine not able to close" + e.getMessage());
        }

        return inputFileAsArray;
    }


    private int calculateCheckSum(ArrayList<String> inputFileAsArray) {
        int doubleCount=0;
        int tripleCount =0;

        for (int i = 0; i < inputFileAsArray.size(); i++) {
            String line = inputFileAsArray.get(i);
            ArrayList<LetterOccurrence> lettersSummary = new ArrayList<>();

            for (int j = 0; j < line.length(); j++) {
                String s = line.substring(j, j + 1);
                char c = s.toCharArray()[0];
                //Log.d (myTag, s);
                if (!checkIfExists(c, lettersSummary)) {
                    lettersSummary.add(new LetterOccurrence(c));
                }
            }
            //Now report out all the occurrences for 2 and 3 times
            for (LetterOccurrence letter : lettersSummary) {
                if (letter.count > 1 && letter.count < 4) {
                    Log.d(myTag, letter.toString());
                } else {
                    Log.d(myTag, "No Letters repeated!");
                }
            }

            if (didLetterOccurXTimes(2, lettersSummary)) {
                doubleCount++;
            }
            if (didLetterOccurXTimes(3, lettersSummary)) {
                tripleCount++;
            }

        }
        Log.d(myTag,
                "Doubles: " + Integer.toString(doubleCount) +
                        ", Triples: " + Integer.toString(tripleCount));

        return (doubleCount * tripleCount);
    }

    private static boolean checkIfExists(char letter, ArrayList<LetterOccurrence> existingLetters) {
        //this routine looks to see if the letter already exists
        //If so, it will bump the number
        //Otherwise, it will create a new item and add it to the existing Letters list

        for (int i = 0; i < existingLetters.size(); i++) {
            char currentLetter = existingLetters.get(i).letter;
            if (letter == currentLetter) {
                existingLetters.get(i).count++;
                return true;
            }
        }
        return false;
    }

    private boolean didLetterOccurXTimes(int x, ArrayList<LetterOccurrence> letters){
        for(LetterOccurrence letter:letters){
            if(letter.count == x){
                return true;
            }
        }
        return false;
    }


        public class LetterOccurrence{
        private char letter;
        private int count;

        //getters
        public char getLetter() {
            return letter;
        }

        public int getCount() {
            return count;
        }
        //setters
        public void setLetter(char letter) {
            this.letter = letter;
        }

        public void setCount(int count) {
            this.count = count;
        }

        //CONSTRUCTORS
        public LetterOccurrence(char letter){
            this.letter = letter;
            this.count=1;

        }

        //OVERRIDES
        @Override
        public String toString(){
            return (Character.toString(this.letter) + " occurred " + Integer.toString(this.count) + " times");
        }




    }

    public static boolean isStoragePermissionGranted(AppCompatActivity mainActivity) {
        String TAG = "Helper";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mainActivity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

        public static boolean isExternalStorageWritable() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                return true;
            }
            return false;
        }

    }
