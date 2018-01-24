package com.example.ashutosh.chatupfinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ashutosh.chatupfinal.modal.UserDetails;
import com.example.ashutosh.chatupfinal.preferences.MyPreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Ashutosh on 05-01-2018.
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.custom_toolbar_login)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.parent_login)
    RelativeLayout loginParent;
    @BindView(R.id.tv_phone_number)
    EditText phoneNumberET;
    @BindView(R.id.tv_forgot_password)
    TextView forgotPasswordTV;
    @BindView(R.id.tv_password)
    EditText passwordET;
    @BindView(R.id.tv_sign_up)
    TextView signupTV;
    @BindView(R.id.btn_login)
    Button loginBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/KaushanScript-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick(R.id.tv_sign_up)
    public void onSignUpClicked()
    {
        startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
    }

    @OnClick(R.id.btn_login)
    public void onLoginBtnClicked()
    {
        final String phoneNumber=phoneNumberET.getText().toString();
        final String password=passwordET.getText().toString();

        if(!phoneNumber.isEmpty() && !password.isEmpty())
        {

            String url="https://chatupfinal.firebaseio.com/users.json";
            final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Loading...");
            pd.show();

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    if(response.equals("null"))
                    {
                        phoneNumberET.setText("Given user is not Registered! Please Register first.");
                        phoneNumberET.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        try
                        {
                            JSONObject loginObject=new JSONObject(response);

                            if(!loginObject.has(phoneNumber))
                            {
                                phoneNumberET.setText("Given user is not Registered! Please Register first.");
                                phoneNumberET.setVisibility(View.VISIBLE);
                            }
                            else if(loginObject.getJSONObject(phoneNumber).getString("password").equals(password))
                            {
                                String password=loginObject.getJSONObject(phoneNumber).getString("password");
                                String userName=loginObject.getJSONObject(phoneNumber).getString("userName");
                                String fullName=loginObject.getJSONObject(phoneNumber).getString("fullName");
                                UserDetails userDetails = new UserDetails(fullName,userName,phoneNumber,password);
                                MyPreferenceManager.setUserDetail(LoginActivity.this,userDetails);
                                Intent intent = new Intent(LoginActivity.this,BaseDashboardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else
                            {
                                phoneNumberET.setText("Incorrect Password!!");
                                phoneNumberET.setVisibility(View.VISIBLE);
                            }
                            pd.dismiss();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });

            RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
            rQueue.add(request);



            /*DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference userNameRef = rootRef.child("users");

            userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(phoneNumber).exists())
                    {
                        DatabaseReference phoneRef=userNameRef.child(phoneNumber).getRef();
                        phoneRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child("password").getValue().equals(password))
                                {
                                    startActivity(new Intent(LoginActivity.this,BaseDashboardActivity.class));
                                    //Toast.makeText(LoginActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    passwordET.setError("Password is Wrong!");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else
                    {
                        phoneNumberET.setError("Phone Number is not Registered!");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/
        }
        else
        {
            if(phoneNumber.isEmpty())
                phoneNumberET.setError("Phone Number Field is Empty.");
            else
                passwordET.setError("Password Field is Empty.");
        }

    }

}
