package com.example.ashutosh.chatupfinal.modal;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Ashutosh on 06-01-2018.
 */

public class UserDetails implements Parcelable,Serializable
{
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("password")
    @Expose
    private String password;
    /*@SerializedName("contactList")
    @Expose
    private List<ContactDetails> contactList=null;*/


    protected UserDetails(Parcel in) {
        fullName = in.readString();
        userName = in.readString();
        phoneNumber = in.readString();
        password = in.readString();
        //in.readList(contactList,null);
    }

    public UserDetails(String fullName,String userName,String phoneNumber,String password)
    {
        this.fullName=fullName;
        this.userName=userName;
        this.phoneNumber=phoneNumber;
        this.password=password;
    }

    /*public List<ContactDetails> getContactList() {
        return contactList;
    }*/

    /*public void setContactList(List<ContactDetails> contactList) {
        this.contactList = contactList;
    }*/

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(userName);
        dest.writeString(phoneNumber);
        dest.writeString(password);
        //dest.writeList(contactList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<UserDetails> CREATOR = new Parcelable.Creator<UserDetails>() {
        @Override
        public UserDetails createFromParcel(Parcel in) {
            return new UserDetails(in);
        }

        @Override
        public UserDetails[] newArray(int size) {
            return new UserDetails[size];
        }
    };
}
