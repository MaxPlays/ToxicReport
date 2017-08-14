package me.MaxPlays.ToxicReport.util;

import java.util.Random;

/**
 * Created by Max_Plays on 12.08.2017.
 */
public class IDGenerator {

    private static final char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static String getRandomID(int length){
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for(int i = 1; i <= length; i++){
            String c = String.valueOf(chars[r.nextInt(chars.length)]);
            if(Math.random() > 0.5)
                c = c.toLowerCase();
            sb.append(c);
        }
        return sb.toString();
    }

}
