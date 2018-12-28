package com.example.bigna.adventofcode;

import android.util.Log;

import java.util.List;

public class Coordinate implements Comparable<Coordinate> {
    private Place claimedBy;
    private Integer x;
    private Integer y;
    private Integer totalDistanceToAllPlaces;

    public Coordinate(int x, int y, List<Place> places){
        this.x = x;
        this.y = y;
        this.claimedBy = getClosestPlace(x,y,places);
    }

    //Getters
    public Place getClaimedBy() {return claimedBy;}
    public int getTotalDistanceToAllPlaces() {return totalDistanceToAllPlaces;}
    public int getX() {return x;}
    public int getY() {return y;}


    //Setters
    public void setTotalDistanceToAllPlaces(int distance){
        this.totalDistanceToAllPlaces = distance;
    }
    @Override
    public int compareTo(Coordinate otherCoordinate){
        int result = this.totalDistanceToAllPlaces.compareTo(otherCoordinate.totalDistanceToAllPlaces);
        //if(result==0) result=((Integer) (this.x+this.y)).compareTo(((Integer) (otherCoordinate.x+otherCoordinate.y)));
        return result;
    }

    @Override
    public String toString(){
        return ("(" + Integer.toString(x) + ", " + Integer.toString(y) + ") total dist-->" +
          Integer.toString(totalDistanceToAllPlaces));
    }

    private Place getClosestPlace(int xCoord,int yCoord, List<Place> places){
        Place closestPlace = null;
        int minDistance = -1;
        boolean firstPass = true;
        for(Place p:places){
            int currentDistance = Math.abs(xCoord - p.getX()) + Math.abs(yCoord - p.getY());
            if (firstPass | currentDistance < minDistance) {
                minDistance = currentDistance;
                closestPlace = p;
                firstPass = false;
            } else if (currentDistance == minDistance){
                closestPlace = null;
            }
        }
        return closestPlace;
    }
}
