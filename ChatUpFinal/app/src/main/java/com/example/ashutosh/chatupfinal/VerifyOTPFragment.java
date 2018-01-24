package com.example.ashutosh.chatupfinal;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.Manifest;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ashutosh.chatupfinal.modal.UserDetails;
import com.example.ashutosh.chatupfinal.preferences.MyPreferenceManager;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ashutosh on 06-01-2018.
 */

public class VerifyOTPFragment extends AppCompatActivity
{
    String TAG="VerifyOTPFragment";

    @BindView(R.id.custom_toolbar_fragment)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    @BindView(R.id.otp_sent_num)
    TextView otpSentNumberInfo;
    @BindView(R.id.tv_otp)
    EditText otpET;
    @BindView(R.id.tv_resend_otp)
    TextView resendOtpTV;
    @BindView(R.id.btn_verify_otp)
    Button verifyOtpBtn;
    String phoneNumberOriginal;
    UserDetails userDetails;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/KaushanScript-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        Bundle b=getIntent().getExtras();
        userDetails=b.getParcelable("userDetails");
        phoneNumberOriginal="+91"+userDetails.getPhoneNumber();

        setContentView(R.layout.fragment_otp_verification);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(VerifyOTPFragment.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        if (!validatePhoneNumber()) {
            return;
        }
        startPhoneNumberVerification(phoneNumberOriginal);


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();

                            final ProgressDialog progressDialog=new ProgressDialog(VerifyOTPFragment.this);
                            progressDialog.setMessage("Loading...");
                            progressDialog.show();
                            String url="https://chatupfinal.firebaseio.com/users.json";

                            StringRequest request=new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Firebase reference = new Firebase("https://chatupfinal.firebaseio.com/users");

                                    if(response.equals("null"))
                                    {
                                        reference.child(userDetails.getPhoneNumber()).child("fullName").setValue(userDetails.getFullName());
                                        reference.child(userDetails.getPhoneNumber()).child("userName").setValue(userDetails.getUserName());
                                        reference.child(userDetails.getPhoneNumber()).child("password").setValue(userDetails.getPassword());
                                        Toast.makeText(VerifyOTPFragment.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        MyPreferenceManager.setUserDetail(VerifyOTPFragment.this,userDetails);
                                        Intent intent=new Intent(VerifyOTPFragment.this,BaseDashboardActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();

                                    }
                                    else
                                    {
                                        try
                                        {
                                            JSONObject object=new JSONObject(response);

                                            if(!object.has(userDetails.getPhoneNumber()))
                                            {
                                                reference.child(userDetails.getPhoneNumber()).child("fullName").setValue(userDetails.getFullName());
                                                reference.child(userDetails.getPhoneNumber()).child("userName").setValue(userDetails.getUserName());
                                                reference.child(userDetails.getPhoneNumber()).child("password").setValue(userDetails.getPassword());
                                                Toast.makeText(VerifyOTPFragment.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                MyPreferenceManager.setUserDetail(VerifyOTPFragment.this,userDetails);
                                                Intent intent=new Intent(VerifyOTPFragment.this,BaseDashboardActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else
                                            {
                                                Toast.makeText(VerifyOTPFragment.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }

                                    progressDialog.dismiss();
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(VerifyOTPFragment.this, ""+error, Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });

                            RequestQueue rQueue= Volley.newRequestQueue(VerifyOTPFragment.this);
                            rQueue.add(request);


                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                otpET.setError("Invalid code.");
                            }
                        }
                    }
                });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private boolean validatePhoneNumber() {
        if (TextUtils.isEmpty(phoneNumberOriginal)) {
            Toast.makeText(this, "Phone number is not found", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Toast.makeText(this,"Test User",Toast.LENGTH_LONG).show();

        if (currentUser != null) {
            startActivity(new Intent(VerifyOTPFragment.this, BaseDashboardActivity.class));
            finish();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @OnClick(R.id.btn_verify_otp)
    public void onVerifyClicked()
    {
        String code = otpET.getText().toString();
        if (TextUtils.isEmpty(code)) {
            otpET.setError("Cannot be empty.");
            return;
        }

        verifyPhoneNumberWithCode(mVerificationId, code);

    }

    @OnClick(R.id.tv_resend_otp)
    public void onResendClicked()
    {
        resendVerificationCode(phoneNumberOriginal, mResendToken);
    }


}
