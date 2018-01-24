package com.example.ashutosh.chatupfinal.modal;

/**
 * Created by Ashutosh on 14-01-2018.
 */

public class ContactObject
{
    private String phoneNumber;
    private String name;

    public ContactObject(String phoneNumber,String name)
    {
        this.phoneNumber=phoneNumber;
        this.name=name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
