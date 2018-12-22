package com.example.bigna.adventofcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DayThree extends AppCompatActivity {

    private String myTag = "BTI_debug";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_three);

        TextView headerText = (TextView) findViewById(R.id.headerText);
        headerText.setText("Day Three");


        Button firstTaskButton = (Button) findViewById(R.id.firstTask);
        firstTaskButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                TextView solutionTV = (TextView) findViewById(R.id.day3Solution);
                solutionTV.setText("Calculating...");

                executeFirstTask();
            }
        });

        Button day3DoneButton = (Button) findViewById(R.id.day3Done);
        day3DoneButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }
    private void executeFirstTask() {
        ArrayList<String> inputFileAsArray = new ArrayList<>();
        String fileName = "/day3_Input";
        readFileIntoArray(fileName, inputFileAsArray);
        Log.d(myTag, "First Line: " + inputFileAsArray.get(0));
        Log.d(myTag, "26th Line: " + inputFileAsArray.get(25));
        ArrayList<CutClaim> allCutClaims = parseInputArrayIntoCuts(inputFileAsArray);
        int[][] heatMap = createHeatMapOfCutClaims(allCutClaims);
        int multipleCutClaims = getMultipleCutClaims(heatMap);

        //write solution
        TextView solutionTV = (TextView) findViewById(R.id.day3Solution);
        solutionTV.setText(Integer.toString(multipleCutClaims));

        int uniqueClaimID = getUniqueClaimID(allCutClaims, heatMap);
        TextView uniqueIdTV = (TextView) findViewById(R.id.uniqueId);
        uniqueIdTV.setText(Integer.toString(uniqueClaimID));


    }

    private int getUniqueClaimID(ArrayList<CutClaim> allCutClaims, int[][] heatMap){
        int uniqueID=0;
        for(CutClaim cc: allCutClaims){
            boolean isClaimUnique = true;
            for (int x=cc.leftX; x<cc.getMaxX(); x++){
                for(int y=cc.topY; y<cc.getMaxY(); y++){
                    if(heatMap[x][y] != 1) {
                        isClaimUnique=false;
                    }
                    if (!isClaimUnique) break;
                }
                if (!isClaimUnique) break;
            }
            if (isClaimUnique){
                Log.d(myTag, "Unique ID: " + Integer.toString(cc.id));
                return cc.id;
            }
        }
        Log.d(myTag, "All Claimed reviewed, none unique");
        return uniqueID;
    }

    private int[][] createHeatMapOfCutClaims(ArrayList<CutClaim> allCutClaims){
        //Find max width and height for heat map
        //Create the array with all zeros
        //Apply each cut to the heat map by incrementing affected area by 1 for each cut

        int[] heatMapSize = findMaxSize(allCutClaims);  //returns X and Y of required heat map width
        int[][] heatMap = new int[heatMapSize[0]][heatMapSize[1]];
        //CutClaim cutClaim=allCutClaims.get(0);

        Log.d(myTag, "Calculated Dimensions: " + Integer.toString(heatMapSize[0]) +
                ", " + Integer.toString(heatMapSize[1]));
        Log.d(myTag, "Queried Size: " + Integer.toString(heatMap.length) +
                ", " + Integer.toString(heatMap[0].length));

        for (CutClaim cutClaim: allCutClaims){
            applyCutClaim(cutClaim, heatMap);
        }
        exportHeatMap(heatMap);


        //Log.d(myTag, cutClaim.toString());
        //Log.d(myTag, Arrays.toString(heatMap));
        return heatMap;
    }

    private int getMultipleCutClaims(int[][] heatMap){
        int multipleCount = 0;
        for(int x=0; x<heatMap.length; x++){
            for(int y=0; y<heatMap[1].length;y++) {
                int currentCell = heatMap[x][y];
                if (currentCell>1) multipleCount++;
            }
        }
        Log.d(myTag, "Multiples: " + Integer.toString(multipleCount));
        return multipleCount;
    }

    private void applyCutClaim(CutClaim cutClaim, int[][] heatMap){
        //Nested for loop to cover X and Y dimensions of cutClaim
        //Increment the heatMap cell by 1 for covered area
        try {
            for (int x = cutClaim.leftX; x < cutClaim.getMaxX(); x++) {  //shift left by 1 caused -1 error
                for (int y = cutClaim.topY; y < cutClaim.getMaxY(); y++) {  //shift y by 1 to account for 0 index start
                    heatMap[x][y] = heatMap[x][y] + 1;
                    //Log.d(myTag, "(" + Integer.toString(x) + ", " + Integer.toString(y) + ") = " +
                    //Integer.toString(heatMap[x][y]));
                }
            }
        }catch (Exception e) {
            Log.d(myTag, "Error during cut application" + e.getMessage());
        }
    }

    private void exportHeatMap(int[][] heatMap){
        try {
            BufferedWriter outputWriter = null;
            File myDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File myFile = new File(myDir, "/day3_heatMap.csv");
            outputWriter = new BufferedWriter(new FileWriter(myFile));
            for(int y=0; y<heatMap[0].length;y++){
                for(int x=0; x<heatMap.length;x++){
                    outputWriter.write(Integer.toString(heatMap[x][y]));
                    if(x<(heatMap.length-1)) outputWriter.write(",");
                }
                outputWriter.newLine();
            }
            outputWriter.flush();
            outputWriter.close();


        }catch (IOException e){
            Log.d(myTag, "Error writing matrix " + e.getMessage());
        }
    }

    private int[] findMaxSize(ArrayList<CutClaim> allCutClaims){
        int[] maxDimensions = new int[]{0,0};
        for(CutClaim cc:allCutClaims){
            if (cc.getMaxX()>maxDimensions[0]) maxDimensions[0] = cc.getMaxX();
            if (cc.getMaxY()>maxDimensions[1]) maxDimensions[1] = cc.getMaxY();
        }

        //Now need to add 1 because both left and top dims start at 0
        for(int i=0; i<maxDimensions.length; i++) {
            maxDimensions[i] = maxDimensions[i] +1;
        }

        return maxDimensions;
    }

    private ArrayList<CutClaim> parseInputArrayIntoCuts(ArrayList<String> inputFileAsArray){
        //Log.d(myTag, "Returned Array==>" + Arrays.toString(cutClaimParameters));
        //Log.d(myTag, "From Instance==>" + cutClaim.toString());
        ArrayList<CutClaim> allCutClaims = new ArrayList<>();

        for (String line:inputFileAsArray){
            int[] cutClaimParameters = getCutClaimParameters(line);
            CutClaim cutClaim = new CutClaim(cutClaimParameters);
            allCutClaims.add(cutClaim);
        }
        Log.d(myTag, Integer.toString(allCutClaims.size()) + " cut claims added to array");
        return allCutClaims;
    }

    private int[] getCutClaimParameters(String line) {
        int cutClaimParameters[] = new int[5];
        int idStart=1;
        int idEnd = line.indexOf("@");
        int xStart = idEnd+1;
        int xEnd = line.indexOf(",");
        int yStart = xEnd+1;
        int yEnd = line.indexOf(":");
        int wStart = yEnd+1;
        int wEnd = line.lastIndexOf("x");
        int hStart = wEnd+1;
        int hEnd = line.length();

        String idString = line.substring(idStart, idEnd);
        String xString = line.substring(xStart, xEnd);
        String yString = line.substring(yStart, yEnd);
        String wString = line.substring(wStart, wEnd);
        String hString = line.substring(hStart, hEnd);
//        Log.d(myTag, line);
//        Log.d(myTag, "x: "+ xString +
//                          ", y: " + yString +
//                          ", w: " + wString +
//                          ", h: " + hString);
        cutClaimParameters[0] = Integer.parseInt(idString.trim());
        cutClaimParameters[1] = Integer.parseInt(xString.trim());
        cutClaimParameters[2] = Integer.parseInt(yString.trim());
        cutClaimParameters[3] = Integer.parseInt(wString.trim());
        cutClaimParameters[4] = Integer.parseInt(hString.trim());
        return cutClaimParameters;
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

    public class CutClaim{
        private int id;
        private int leftX;
        private int topY;
        private int width;
        private int height;

        public CutClaim(){
            this.leftX = 0;
            this.topY = 0;
            this.width = 0;
            this.height = 0;
        }

        public CutClaim(int [] dims){
            if (dims.length<5){
                new CutClaim();
            }else {
                this.id = dims[0];
                this.leftX = dims[1];
                this.topY = dims[2];
                this.width = dims[3];
                this.height = dims[4];
            }
        }

        public String toString(){
            return ("X: " + Integer.toString(leftX)+
            ", Y: " + Integer.toString(topY)+
            ", w: " + Integer.toString(width) +
            ", h: " + Integer.toString(height));
        }

        public int getMaxX(){
            return (leftX+width);
        }

        public int getMaxY(){
            return (topY + height);
        }

    }
}
