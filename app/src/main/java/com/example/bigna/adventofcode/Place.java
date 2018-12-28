package com.example.bigna.adventofcode;

import android.util.Log;

import java.util.List;

public class Place implements Comparable<Place>{
    private static int count=0;
    private Integer id;
    private int x;
    private int y;
    private Integer claimArea;

    public Place(int x, int y){

        count++;
        this.id = count;
        this.x = x;
        this.y = y;
        this.claimArea = 0;

    }

    public int getId() {return this.id;}
    public int getX() {return this.x;}
    public int getY() {return this.y;}
    public int getClaimArea() {return this.claimArea;}

    @Override
    public String toString(){
        return ("(" + Integer.toString(x) + ", " + Integer.toString(y)
                + "): Claim Size-->" + claimArea);
    }

    @Override
    public int compareTo(Place otherPlace){
        int result = this.claimArea.compareTo(otherPlace.claimArea);
        if (result==0) result = this.id.compareTo(otherPlace.id);  //Use ID for tie breaker
        return result;
    }
    public boolean isSurrounded(List<Place> allPlaces){
        boolean isSurrounded = false;
        boolean hasLeftNeighbor = false;
        boolean hasRightNeighbor = false;
        boolean hasAboveNeighbor = false;
        boolean hasBelowNeighbor = false;
        for(Place p: allPlaces){
            if(p.x<this.x) hasLeftNeighbor=true;
            if(p.x>this.x) hasRightNeighbor = true;
            if(p.y<this.y) hasAboveNeighbor = true;
            if(p.y>this.y) hasBelowNeighbor = true;
        }

        if(hasLeftNeighbor && hasRightNeighbor && hasAboveNeighbor && hasBelowNeighbor){
            isSurrounded = true;
        }

        return isSurrounded;
    }

    public void calculateClaimArea(List<Coordinate> coordinates){
        int claimArea = 0;
        for (Coordinate c:coordinates){
            Place coordClaimedBy = c.getClaimedBy();
            if (c.getClaimedBy() == null){
                //Log.d("BTI_debug", "One null coordinate processed");
            }else if(coordClaimedBy.getId() == this.id){
                claimArea++;
            }
        }
        this.claimArea = claimArea;
    }


}
