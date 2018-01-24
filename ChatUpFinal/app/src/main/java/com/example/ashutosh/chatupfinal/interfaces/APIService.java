package com.example.ashutosh.chatupfinal.interfaces;

import com.example.ashutosh.chatupfinal.modal.UserDetails;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Ashutosh on 06-01-2018.
 */

public interface APIService
{
    @POST("/user.json")
    public Call<UserDetails> completeUserRegistration(@Path("new") String s1, @Body UserDetails userDetails);

}
