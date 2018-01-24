package com.example.ashutosh.chatupfinal.modal;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Ashutosh on 12-01-2018.
 */

public class ContactDetails implements Parcelable,Serializable
{
    @SerializedName("nameToShow")
    @Expose
    private String nameToShow;
    @SerializedName("profileUrl")
    @Expose
    private String profileUrl;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("lastMessage")
    @Expose
    private String lastMessage="";
    @SerializedName("chatList")
    @Expose
    private List<ChatMessage> chatList=null;

    protected ContactDetails(Parcel in) {
        nameToShow=in.readString();
        profileUrl=in.readString();
        fullName=in.readString();
        userName=in.readString();
        phoneNumber=in.readString();
        in.readList(chatList,null);
    }

    public ContactDetails(String nameToShow,String fullName,String userName,String phoneNumber,String profileUrl)
    {
        this.nameToShow=nameToShow;
        this.fullName=fullName;
        this.userName=userName;
        this.phoneNumber=phoneNumber;
        this.profileUrl=profileUrl;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<ChatMessage> getChatList() {
        return chatList;
    }

    public void setChatList(List<ChatMessage> chatList) {
        this.chatList = chatList;
    }

    public String getNameToShow() {
        return nameToShow;
    }

    public void setNameToShow(String nameToShow) {
        this.nameToShow = nameToShow;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nameToShow);
        dest.writeString(profileUrl);
        dest.writeString(fullName);
        dest.writeString(userName);
        dest.writeString(phoneNumber);
        dest.writeList(chatList);
    }

    public static final Parcelable.Creator<ContactDetails> CREATOR = new Parcelable.Creator<ContactDetails>() {
        @Override
        public ContactDetails createFromParcel(Parcel in) {
            return new ContactDetails(in);
        }

        @Override
        public ContactDetails[] newArray(int size) {
            return new ContactDetails[size];
        }
    };
}
