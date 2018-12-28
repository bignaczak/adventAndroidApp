package com.example.bigna.adventofcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DaySix extends AppCompatActivity {
    private String myTag = "BTI_debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_six);

        TextView headerText = (TextView) findViewById(R.id.day6Header);
        headerText.setText(getResources().getString(R.string.dayHeader) + " ;o)");

        Button firstTaskButton = (Button) findViewById(R.id.day6Compute);
        firstTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setAnswerFields("Calculating...", "Calculating...");
                //TextView solutionTV = (TextView) findViewById(R.id.day4Answer);
                //solutionTV.setText("Calculating...");
                solveTasks();
            }
        });

        Button day6DoneButton = (Button) findViewById(R.id.day6Done);
        day6DoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }

    public void setAnswerFields(String answer, String answer2){
        TextView solutionTV = (TextView) findViewById(R.id.day6Answer);
        solutionTV.setText(answer);
        TextView solutionTV2 = (TextView) findViewById(R.id.day6Answer2);
        solutionTV2.setText(answer2);
    }

    private void solveTasks() {
        String inputFileName = "/input_day6";
        ArrayList<String> inputFileAsArray = MainActivity.getInputFileAsArray(myTag, inputFileName, this);

        List<Place> places = getPlacesFromString(inputFileAsArray);
        List<Place> finitePlaces = getFinitePlaces(places);
        int[] gridDimensions = getGridDimensions(places);
        List<Coordinate> coordinates = generateCoordinates(gridDimensions, places);
        generateClaimAreas(coordinates, places);
        Collections.sort(finitePlaces);
        for (Place fp:finitePlaces) Log.d(myTag, fp.toString());
        int maxArea = findMaxArea(finitePlaces);
        setAnswerFields(Integer.toString(maxArea), "");

        calculateCoordinateDistancesToAllPlaces(coordinates, places);
        Log.d(myTag, "Completed Calculation of total Distance");
        int regionLimit = 10000;
        List<Coordinate> coordinatesWithinRegion = getCoordinatesWithinRegion(coordinates, regionLimit);
        Log.d(myTag, "Completed screening for total Distances within region");
        int regionSize = coordinatesWithinRegion.size();
        Log.d(myTag, "Coordinates within region: " + Integer.toString(regionSize));
        //Collections.sort(coordinatesWithinRegion);
        //Collections.reverse(coordinatesWithinRegion);
        for (int i=0;i<30;i++){
            coordinates.get(i).toString();
        }
        setAnswerFields(Integer.toString(maxArea), Integer.toString(regionSize));
    }

    private List<Coordinate> getCoordinatesWithinRegion(List<Coordinate> coordinates, int regionLimit){
        List<Coordinate> coordinatesWithinRegion = new ArrayList<>();
        int coordCount=0;
        for(Coordinate c: coordinates){
            if(c.getTotalDistanceToAllPlaces() < regionLimit){
                coordinatesWithinRegion.add(c);
            }
            coordCount++;
            if (coordCount%10000==0) {
                Log.d(myTag, Integer.toString(coordCount) + " total distances screened");
            }
        }
        return coordinatesWithinRegion;
    }

    private void calculateCoordinateDistancesToAllPlaces(List<Coordinate> coordinates, List<Place> places){
        int coordinateX ;
        int coordinateY ;
        int placeX;
        int placeY;
        int coordinateCount=0;

        for(Coordinate c: coordinates){
            int totalDistance = 0;
            coordinateX = c.getX();
            coordinateY = c.getY();
            for (Place p: places){
                placeX = p.getX();
                placeY = p.getY();
                totalDistance += Math.abs(coordinateX-placeX) + Math.abs(coordinateY - placeY);
            }
            coordinateCount++;
            if(coordinateCount%10000==0) Log.d(myTag, Integer.toString(coordinateCount) + " coordinates computed");
            c.setTotalDistanceToAllPlaces(totalDistance);
        }

    }
    private void generateClaimAreas(List<Coordinate> coordinates, List<Place> places){
        for (Place p: places){
            p.calculateClaimArea(coordinates);
        }
    }

    private int findMaxArea(List<Place> finitePlaces){
        int maxArea = 0;
        for (Place p: finitePlaces){
            if (p.getClaimArea() > maxArea){
                maxArea = p.getClaimArea();
            }
        }
        Log.d(myTag, "Max Area: " + Integer.toString(maxArea));
        return maxArea;
    }

    private List<Coordinate> generateCoordinates(int[] gridDimensions, List<Place> places){
        List<Coordinate> coordinates = new ArrayList<>();
        int gridWidth = gridDimensions[0];
        int gridHeight = gridDimensions[1];
        int startX = gridDimensions[2];
        int startY = gridDimensions[3];

        for(int x=startX; x<startX + gridWidth; x++){
            for (int y=startY; y<startY + gridHeight; y++){
                Coordinate newCoord = new Coordinate(x,y,places);
                coordinates.add(newCoord);
            }
        }

        return coordinates;
    }

    private int[] getGridDimensions(List<Place> places){
        int gridWidth = 0;
        int gridHeight = 0;
        int minX = -1;
        int maxX = 0;
        int minY = -1;
        int maxY = 0;
        boolean firstPass = true;
        int[] gridDimensions = new int[4];
        for(Place p:places){
            if (firstPass) {
                minX=p.getX();
                maxX = p.getX();
                minY = p.getY();
                maxY = p.getY();
                firstPass = false;
            }else {
                if (p.getX() > maxX) maxX = p.getX();
                if (p.getX() < minX) minX = p.getX();
                if (p.getY() < minY) minY = p.getY();
                if (p.getY() > maxY) maxY = p.getY();
            }
        }
        gridWidth = maxX - minX;
        gridHeight = maxY - minY;
        gridDimensions[0] = gridWidth;
        gridDimensions[1] = gridHeight;
        gridDimensions[2] = minX;
        gridDimensions[3] = minY;
        Log.d(myTag, "Grid Dimensions: (" + Integer.toString(gridWidth) +
            ", " + Integer.toString(gridHeight) + ")" + Integer.toString(minX) +
            "-->" + Integer.toString(maxX) + ", " + Integer.toString(minY) +
            "-->" + Integer.toString(maxY));
        return gridDimensions;
    }

    private List<Place> getFinitePlaces(List<Place> places){
        List<Place> finitePlaces = new ArrayList<Place>();
        int infinitePlaceCount = 0;
        for (Place p: places){
            if (p.isSurrounded(places)){
                finitePlaces.add(p);
            }else{
                infinitePlaceCount++;
            }
        }

        Log.d(myTag, "Finite places: " + Integer.toString(finitePlaces.size()) +
                ", Infinite places: " + Integer.toString(infinitePlaceCount));

        return finitePlaces;
    }

    private List<Place> getPlacesFromString(ArrayList<String> inputFileAsArray){
        List<Place> places = new ArrayList<>();
        for(String line: inputFileAsArray){
            int comma = line.indexOf(',');
            try {
                int newX = Integer.parseInt(line.substring(0, comma));
                int newY = Integer.parseInt((line.substring(comma + 1, line.length())).trim());
                Place place = new Place(newX, newY);
                places.add(place);
            } catch(Exception e){
                Log.d(myTag,"Issue parsing int..." + e.getMessage());
            }
        }
        Log.d(myTag, Integer.toString(places.size()) + " places added!");

        return places;
    }
    }
