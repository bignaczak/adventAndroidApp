package com.example.bigna.adventofcode;

import java.util.ArrayList;

public class Guard {
    private DayFour dayFour;
    private int id;
    private int shiftsObserved;
    private int[] sleepLog;  //Heat map of observed sleep behavior
    private int totalSleep;

    public Guard(DayFour dayFour, int id){
        this.dayFour = dayFour;
        this.id = id;
        this.shiftsObserved++;
        this.sleepLog = new int[60];
        this.totalSleep=0;
    }

    //GETTERS
    public int getGuardId(){return id;}
    public int[] getSleepLog(){return sleepLog;}
    public int getTotalSleep(){return totalSleep;}

    @Override
    public String toString(){
        return Integer.toString(id);
    }

    public void addShiftObserved(){this.shiftsObserved++;}

    public void applySleepEvent(int minStart, int minEnd){
        int sleepTime = minEnd - minStart;
        totalSleep+=sleepTime;
        for(int minute=minStart; minute<minEnd;minute++){
            this.sleepLog[minute] = this.sleepLog[minute]+1;
        }
    }

    public static Guard findById(int id, ArrayList<Guard> allGuards){
        for(Guard g: allGuards){
            if(g.id == id){
                return g;
            }
        }
        return null;
    }

    public int getCumulativeMinutesAsleepFromHeatMap(){
        int minutesAsleep = 0;
        for(int i=0; i<sleepLog.length; i++){
            if(sleepLog[i] != 0){
                minutesAsleep++;
            }
        }
        return minutesAsleep;
    }

    public int[] getMinuteWithHighestSleepOccurrence() {
        //return matrix is [MINUTE, OCCURRENCE]
        int[] minuteWithHighestSleepOccurrence = new int[]{0,0};
        int currentMax = minuteWithHighestSleepOccurrence[1];
        for (int i = 0; i < sleepLog.length; i++) {
            int currentOccurrence = sleepLog[i];
            if (currentOccurrence > currentMax) {
                currentMax = currentOccurrence;
                minuteWithHighestSleepOccurrence[0] = i;
                minuteWithHighestSleepOccurrence[1] = currentOccurrence;
            }
        }
        return minuteWithHighestSleepOccurrence;
    }

    public int getSleepiestMinute(){
        int sleepiestMinute = 0;
        for(int i=0; i<sleepLog.length; i++){
            if(sleepLog[i] > sleepiestMinute){
                sleepiestMinute = i;
            }
        }
        return sleepiestMinute;
    }

}
