package com.example.ashutosh.chatupfinal.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.ashutosh.chatupfinal.modal.ContactDetails;
import com.example.ashutosh.chatupfinal.modal.UserDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.*;

/**
 * Created by Ashutosh on 06-01-2018.
 */

public class MyPreferenceManager {

    public static  void setContactList(Context context, List<ContactDetails> list)
    {
        if(context!=null)
        {
            Gson gson = new Gson();
            String objectString = gson.toJson(list);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("contactList",objectString);
            editor.commit();
            Log.d("contactList : ",objectString);
        }
    }

    public static ArrayList<ContactDetails> getContactList(Context context)
    {
        if (context != null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();

            String contactListString = preferences.getString("contactList", "");
            if (!contactListString.isEmpty()) {
                ArrayList<ContactDetails> contactList = gson.fromJson(contactListString,new TypeToken<ArrayList<ContactDetails>>(){}.getType());
                return contactList;
            } else {
                //return  new UserDetail(null);
                return null;
            }
        } else
            return null;
    }

    public static void setUserDetail(Context context, UserDetails userDetails) {

        if (context != null) {
            Gson gson = new Gson();
            String objectString = gson.toJson(userDetails);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("userDetails", objectString);
            editor.commit();
        }
    }

    public static UserDetails getUserDetail(Context context) {

        if (context != null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();

            String userDetailString = preferences.getString("userDetails", "");
            if (!userDetailString.isEmpty()) {
                UserDetails userDetails = gson.fromJson(userDetailString, UserDetails.class);
                return userDetails;
            } else {
                //return  new UserDetail(null);
                return null;
            }
        } else
            return null;
    }

    public static void setOtp(Context context, String otp) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("otp", otp);
        editor.commit();
    }

    public static String getOtp(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String otp = preferences.getString("otp", "");
        return otp;
    }


    // default value of printBoolean="0"
    public static void setPrint(Context context, String printBoolean) {  // This is for active jobs variable for check all cancel without print or not.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("printBoolean", printBoolean);
        editor.commit();
    }

    public static String getPrint(Context context) {
        if (context != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String printBoolean = preferences.getString("printBoolean", "0");
            return printBoolean;
        } else {
            return "0";
        }
    }


    // if tutorial 1 then not display..
    public static void setTutorial(Context context, String tuto) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("tutorial", tuto);
        editor.commit();
    }

    public static String getTutorial(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String otp = preferences.getString("tutorial", "");
        return otp;
    }
}