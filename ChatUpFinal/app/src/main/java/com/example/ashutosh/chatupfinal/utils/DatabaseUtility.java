package com.example.ashutosh.chatupfinal.utils;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Ashutosh on 10-01-2018.
 */

public class DatabaseUtility
{

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase()
    {
        if(mDatabase==null)
        {
            mDatabase=FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }

        return mDatabase;
    }

}
