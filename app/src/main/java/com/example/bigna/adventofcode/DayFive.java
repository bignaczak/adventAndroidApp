package com.example.bigna.adventofcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class DayFive extends AppCompatActivity {
    private String myTag = "BTI_debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_five);

        TextView headerText = (TextView) findViewById(R.id.day5Header);
        headerText.setText("Day Five ;o)");


        Button firstTaskButton = (Button) findViewById(R.id.day5Compute);
        firstTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setAnswerFields("Calculating...", "Calculating...");
                //TextView solutionTV = (TextView) findViewById(R.id.day4Answer);
                //solutionTV.setText("Calculating...");
                solveTasks();
            }
        });

        Button day5DoneButton = (Button) findViewById(R.id.day5Done);
        day5DoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }

    public void setAnswerFields(String answer, String answer2){
        TextView solutionTV = (TextView) findViewById(R.id.day5Answer);
        solutionTV.setText(answer);
        TextView solutionTV2 = (TextView) findViewById(R.id.day5Answer2);
        solutionTV2.setText(answer2);
    }

    private void solveTasks(){
        String inputFileName = "/input_day5";
        ArrayList<String> inputFileAsArray = MainActivity.getInputFileAsArray(myTag, inputFileName, this);
        ArrayList<Character> charArray = getCharArray(inputFileAsArray);
        int cancellationCount = 0;
        while (findCancelingCharacters(charArray)){
            cancellationCount++;
        }


        Log.d(myTag, "Found " + Integer.toString(cancellationCount)+ "Cancellations");

        String resultantString = buildString(charArray);
        Log.d(myTag, "New String-->" + resultantString);
        String answer = Integer.toString(resultantString.length());
        setAnswerFields(answer, "awaiting...");

        //////////////////////////////////////
        //Second Task
        //////////////////////////////////////
        //65->A
        //97->a
        int upperCaseOrigin = (int) 'A';
        int lowerCaseOrigin = (int) 'a';
        int smallestSize = resultantString.length();
        int[] mostEfficientCharactersToRemove = new int[2];
        for (int i=0; i<26; i++){
            Log.d(myTag, "Starting letter: " + Character.toString((char) (upperCaseOrigin+i)));
            int[] charsToRemove = new int[]{upperCaseOrigin+i, lowerCaseOrigin+i};
            List<Character> whatIf = removeCharacters(charsToRemove, charArray);
            int currentSize = whatIf.size();
            Log.d(myTag, "Letter " + Character.toString((char) upperCaseOrigin) +
                    " new length==>" + Integer.toString(currentSize));
            Log.d(myTag, "Starting reduction process...");
            while (findCancelingCharacters(whatIf)){
                cancellationCount++;
            }
            int fullyReducedSize = whatIf.size();
            Log.d(myTag, "Fully Reduced Size-->" + Integer.toString(fullyReducedSize));
            if (fullyReducedSize < smallestSize){
                mostEfficientCharactersToRemove = charsToRemove;
                smallestSize = fullyReducedSize;
            }
        }

        Log.d(myTag, "Remove " + Character.toString((char) mostEfficientCharactersToRemove[0]) +
        " and " + Character.toString((char) mostEfficientCharactersToRemove[1]) + " for length" +
        Integer.toString(smallestSize));
    }

    private List<Character> removeCharacters(int[] charsToRemove, ArrayList<Character> charArray){
        List<Character> whatIf = new ArrayList<>(charArray);  //copy the list
        ListIterator<Character> listIterator = whatIf.listIterator();
        while (listIterator.hasNext()){
            Character current = listIterator.next();
            if ((int) current == charsToRemove[0] | (int) current == charsToRemove[1]){
                listIterator.remove();
            }
        }
        return whatIf;
    }
    private String buildString(ArrayList<Character> charArray){
        StringBuilder stringBuilder = new StringBuilder(charArray.size());
        for(Character c:charArray){
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }
    private ArrayList<Character> getCharArray(ArrayList<String> inputFileAsArray){
        ArrayList<Character> charArray = new ArrayList<>();
        String line = inputFileAsArray.get(0);
        for(int i = 0; i<line.length();i++){
            charArray.add(line.charAt(i));
        }
        return charArray;
    }

    private boolean findCancelingCharacters(ArrayList<Character> charArray){
        //returns true if the end of the file was reached with no removed characters
        String current = null;
        String previous = null;
        boolean reachedEnd = false;
        for(int i=0; i<charArray.size(); i++){
            boolean sameCharacter = false;
            boolean differentCase = false;
            current = charArray.get(i).toString();
            if (current.equalsIgnoreCase(previous) && !current.equals(previous)){
                //Log.d(myTag, "Found " + current + " and " + previous + " at "
                //    + Integer.toString(i-1) + ", " + Integer.toString(i));
                charArray.remove(i);
                charArray.remove(i-1);
                break;  //break out of for loop
            }
            previous = current;
            if(i == (charArray.size() - 1)) reachedEnd = true;
        }
        return !reachedEnd;
    }

    private boolean findCancelingCharacters(List<Character> charArray){
        //returns true if the end of the file was reached with no removed characters
        String current = null;
        String previous = null;
        boolean reachedEnd = false;
        for(int i=0; i<charArray.size(); i++){
            boolean sameCharacter = false;
            boolean differentCase = false;
            current = charArray.get(i).toString();
            if (current.equalsIgnoreCase(previous) && !current.equals(previous)){
                //Log.d(myTag, "Found " + current + " and " + previous + " at "
                //    + Integer.toString(i-1) + ", " + Integer.toString(i));
                charArray.remove(i);
                charArray.remove(i-1);
                break;  //break out of for loop
            }
            previous = current;
            if(i == (charArray.size() - 1)) reachedEnd = true;
        }
        return !reachedEnd;
    }

}
