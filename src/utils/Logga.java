package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by andrea on 18/10/16.
 */
public class Logga {
    public static void print(String message){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println(sdf.format(cal.getTime())+" "+message);
    }
}
