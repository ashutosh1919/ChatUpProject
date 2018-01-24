package com.example.ashutosh.chatupfinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ashutosh.chatupfinal.interfaces.APIService;
import com.example.ashutosh.chatupfinal.modal.UserDetails;
import com.example.ashutosh.chatupfinal.preferences.MyPreferenceManager;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Ashutosh on 06-01-2018.
 */

public class SignUpActivity extends AppCompatActivity
{

    @BindView(R.id.custom_toolbar_login)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.parent_login)
    RelativeLayout loginParent;
    @BindView(R.id.tv_full_name)
    EditText fullNameET;
    @BindView(R.id.tv_username)
    EditText userNameET;
    @BindView(R.id.tv_phone_number)
    EditText phoneNumberET;
    @BindView(R.id.tv_password)
    EditText passwordET;
    @BindView(R.id.tv_confirm_password)
    EditText confirmPasswordET;
    @BindView(R.id.btn_signup)
    Button signupBtn;

    FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/KaushanScript-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

         //mDatabase = FirebaseDatabase.getInstance();

        Firebase.setAndroidContext(this);

        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        MultiDex.install(this);
    }

    @OnClick(R.id.btn_signup)
    public void onSignupBtnClciked(){
        final String fullName=fullNameET.getText().toString();
        final String userName=userNameET.getText().toString();
        final String phoneNumber=phoneNumberET.getText().toString();
        final String password=passwordET.getText().toString();
        String confirmPassword=confirmPasswordET.getText().toString();


        if(!fullName.isEmpty() && !userName.isEmpty() && !phoneNumber.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty())
        {
            if(confirmPassword.equals(password))
            {
                UserDetails userDetails=new UserDetails(fullName,userName,phoneNumber,password);

                Bundle b=new Bundle();
                b.putParcelable("userDetails",userDetails);

                Intent intent=new Intent(SignUpActivity.this,VerifyOTPFragment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
            else
            {
                confirmPasswordET.setError("Password Does not match!");
            }
        }
        else
        {
            if(fullName.isEmpty())
                fullNameET.setError("Full Name Field is Empty.");
            else if(userName.isEmpty())
                userNameET.setError("Username Field is Empty.");
            else if(phoneNumber.isEmpty())
                phoneNumberET.setError("Phone Number Field is Empty.");
            else if(password.isEmpty())
                passwordET.setError("Password Field is Empty.");
            else if(confirmPassword.isEmpty())
                confirmPasswordET.setError("Confirm Password Field is Empty.");
        }
    }

}
