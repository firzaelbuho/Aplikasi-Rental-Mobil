package com.mobileapps.uas;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobileapps.uas.model.Car;
import com.mobileapps.uas.model.Order;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Util {

    public static boolean isFirst = true ;
    public static boolean isAdmin = false;
    public static Order order = null;

    public static long toUnixTime(String dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            long unixTime = sdf.parse(dateStr).getTime()/1000;
            return unixTime;
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String unixTimeToString(long unixTime){
        Date date = new Date();
        date.setTime(unixTime * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String str = sdf.format(date);
        return str;
    }

    public  static  String getCurrentDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return (formatter.format(date));
    }

    public static String generateId(){

        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String datetime = ft.format(dNow);
        String id = "ID"+datetime;
        return id;
    }

    public static void logout(){

        isAdmin = false;
        FirebaseAuth.getInstance().signOut();

    }
    public static List<Car> getListCar(){
        ArrayList<Car> myList = new ArrayList<Car>();

        for(int i = 0 ; i<10;i++){
            myList.add(new Car("","Avanza",2011,50000,"url",true ));
        }
        return myList;
    }



}
