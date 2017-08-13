package me.MaxPlays.ToxicReport.util;

/**
 * Created by Max_Plays on 12.08.2017.
 */
public class TimeParser {

    long seconds;

    public TimeParser(long seconds){
        this.seconds = seconds;
    }

    public String parse(){
        StringBuilder s = new StringBuilder();

        int minutes = 0, hours = 0, days = 0;

        while(seconds >= 60){
            seconds -= 60;
            minutes++;
        }
        while(minutes >= 60){
            minutes -= 60;
            hours++;
        }
        while(hours >= 24){
            hours -= 24;
            days++;
        }

        if(days > 0){
            s.append(days + (days > 1 ? " Tage " : " Tag "));
        }
        if(hours > 0){
            s.append(hours + (hours > 1 ? " Stunden " : " Stunde "));
        }
        if(minutes > 0){
            s.append(minutes + (minutes > 1 ? " Minuten " : " Minute "));
        }
        if(seconds > 0){
            s.append(seconds + (seconds > 1 ? " Sekunden" : " Sekunde"));
        }else if(days == 0 && hours == 0 && minutes == 0){
            s.append("0 seconds");
        }
        return s.toString();
    }

}
