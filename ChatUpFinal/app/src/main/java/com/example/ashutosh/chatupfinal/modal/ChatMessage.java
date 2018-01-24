package com.example.ashutosh.chatupfinal.modal;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ashutosh on 20-01-2018.
 */

public class ChatMessage implements Parcelable,Serializable
{
    @SerializedName("messageContent")
    @Expose
    private String messageContent;
    @SerializedName("timeOfMessage")
    @Expose
    private String timeOfMessage;
    @SerializedName("dateOfMessage")
    @Expose
    private String dateOfMessage;
    @SerializedName("messageStatus")
    @Expose
    private int messageStatus;
    @SerializedName("messageType")
    @Expose
    private int messageType;

    protected ChatMessage(Parcel in) {
        messageContent=in.readString();
        timeOfMessage=in.readString();
        dateOfMessage=in.readString();
        messageStatus=in.readInt();
        messageType=in.readInt();
    }

    public ChatMessage(String messageContent,String timeOfMessage,String dateOfMessage,int messageStatus,int messageType)
    {
        this.messageContent=messageContent;
        this.timeOfMessage=timeOfMessage;
        this.dateOfMessage=dateOfMessage;
        this.messageStatus=messageStatus;
        this.messageType=messageType;
    }

    public static final Parcelable.Creator<ChatMessage> CREATOR = new Parcelable.Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getTimeOfMessage() {
        return timeOfMessage;
    }

    public void setTimeOfMessage(String timeOfMessage) {
        this.timeOfMessage = timeOfMessage;
    }

    public String getDateOfMessage() {
        return dateOfMessage;
    }

    public void setDateOfMessage(String dateOfMessage) {
        this.dateOfMessage = dateOfMessage;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(messageContent);
        dest.writeString(timeOfMessage);
        dest.writeString(dateOfMessage);
        dest.writeInt(messageStatus);
        dest.writeInt(messageType);
    }
}
