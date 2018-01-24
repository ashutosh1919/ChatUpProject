package com.example.ashutosh.chatupfinal;

import android.content.Context;
import java.util.*;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ashutosh.chatupfinal.adapters.ChatScreenAdapter;
import com.example.ashutosh.chatupfinal.modal.ChatMessage;
import com.example.ashutosh.chatupfinal.modal.ContactDetails;
import com.example.ashutosh.chatupfinal.preferences.MyPreferenceManager;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Ashutosh on 19-01-2018.
 */

public class ChatScreenActivity extends AppCompatActivity
{
    @BindView(R.id.chat_activity_toolbar)
    Toolbar chatToolbar;
    @BindView(R.id.back_btn_iv)
    ImageView backChatScreenBtn;
    @BindView(R.id.profile_image_civ)
    CircleImageView profilePictureCIV;
    @BindView(R.id.name_of_contact_tv)
    TextView nameOfContactTV;
    @BindView(R.id.last_seen_tv)
    TextView lastSeenTV;
    @BindView(R.id.option_menu_iv)
    ImageView optionMenuBtnIV;
    @BindView(R.id.call_contact_iv)
    ImageView callContactBtnIV;
    @BindView(R.id.send_file_iv)
    ImageView sendFileBtnIV;
    @BindView(R.id.send_message_iv)
    ImageView sendMessageBtnIV;
    @BindView(R.id.imoge_icon_iv)
    ImageView imogeBtnIV;
    @BindView(R.id.message_box_et)
    EditText messageET;
    @BindView(R.id.rl_message_area)
    RelativeLayout messageRL;
    @BindView(R.id.chat_recycler_view)
    RecyclerView chatConversionListRecyclerView;


    String friendPhoneNumber;
    String myPhoneNumber;
    String messageText;
    int mType;
    Firebase myReference,friendReference,receivedRef;
    ChatScreenAdapter chatListAdapter;
    List<ContactDetails> contactDetailsList;
    ContactDetails contactDetails;

    List<ChatMessage> chatMessageList;
    int position;
    String lastMessage="";
    Firebase databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat_Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_chat_screen);
        ButterKnife.bind(this);
        setSupportActionBar(chatToolbar);
        getWindow().setBackgroundDrawableResource(R.drawable.chat_background);
        messageRL.bringToFront();

        Firebase.setAndroidContext(this);
        position = getIntent().getIntExtra("position",0);

        contactDetailsList = MyPreferenceManager.getContactList(this);

        contactDetails = contactDetailsList.get(position);

        chatMessageList = contactDetails.getChatList();
        if(chatMessageList==null)
            chatMessageList = new ArrayList<ChatMessage>();

        nameOfContactTV.setText(contactDetails.getNameToShow());
        if(contactDetails.getProfileUrl().equals("null"))
            profilePictureCIV.setImageDrawable(getResources().getDrawable(R.drawable.default_profile_image));

        friendPhoneNumber = contactDetails.getPhoneNumber();
        myPhoneNumber = MyPreferenceManager.getUserDetail(this).getPhoneNumber();

        chatListAdapter = new ChatScreenAdapter(this,chatMessageList);
        chatConversionListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatConversionListRecyclerView.setAdapter(chatListAdapter);
        //chatConversionListRecyclerView.smoothScrollToPosition(chatMessageList.size()-1);

        //databaseReference = FirebaseDatabase.getInstance("https://chatupfinal.firebaseio.com/users/"+friendPhoneNumber+"/chats/"+myPhoneNumber).getReference();

        myReference = new Firebase("https://chatupfinal.firebaseio.com/users/"+myPhoneNumber+"/chats/"+friendPhoneNumber);
        friendReference = new Firebase("https://chatupfinal.firebaseio.com/users/"+friendPhoneNumber+"/chats/"+myPhoneNumber);

        friendReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map=dataSnapshot.getValue(Map.class);

                String message=map.get("message").toString();
                String user=map.get("user").toString();
                String isRead = map.get("isRead").toString();
                int mStatus = Integer.parseInt(map.get("messageStatus").toString());

                if(user.equals(friendPhoneNumber) && isRead.equals("0"))
                {
                    Calendar c = Calendar.getInstance();
                    int minutes = c.get(Calendar.MINUTE);
                    int hour = c.get(Calendar.HOUR);
                    String time = hour + ":" + minutes;

                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int month = c.get(Calendar.MONTH);
                    int year = c.get(Calendar.YEAR);
                    String date = day + "/" + month + "/" + year;
                    mType=0;

                    chatMessageList.add(new ChatMessage(message,time,date,mStatus,mType));
                    chatListAdapter.notifyData(chatMessageList);

                    databaseReference=dataSnapshot.child("isRead").getRef();
                    databaseReference.setValue("1");

                    Map<String,String> map1=new HashMap<String,String>();
                    map1.put("message",message);
                    map1.put("user",user);
                    map1.put("isRead","1");
                    map1.put("messageStatus",String.valueOf(mStatus));
                    map1.put("type",String.valueOf(mType));
                    lastMessage = message;
                    contactDetails.setLastMessage(lastMessage);
                    myReference.push().setValue(map1);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick(R.id.message_box_et)
    public void onMessageETClicked()
    {
        messageET.requestFocus();

        messageET.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                InputMethodManager keyboard = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(messageET, 0);
            }
        },200);
    }

    @OnClick(R.id.send_message_iv)
    public void sendMessageClicked()
    {
        messageText = messageET.getText().toString().trim();
        if(!messageText.isEmpty())
        {
            Calendar c = Calendar.getInstance();
            int minutes = c.get(Calendar.MINUTE);
            int hour = c.get(Calendar.HOUR);
            String time = hour + ":" + minutes;

            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);
            String date = day + "/" + month + "/" + year;

            int mStatus=0;
            mType = 1;

            chatMessageList.add(new ChatMessage(messageText,time,date,mStatus,mType));
            chatListAdapter.notifyData(chatMessageList);

            Map<String,String> map=new HashMap<String,String>();
            map.put("message",messageText);
            map.put("isRead","0");
            map.put("user",myPhoneNumber);
            map.put("messageStatus",String.valueOf(mStatus));
            map.put("type",String.valueOf(mType));
            lastMessage = messageText;
            contactDetails.setLastMessage(lastMessage);
            myReference.push().setValue(map);
            messageET.setText("");
        }
    }

    @Override
    public void onBackPressed() {
        contactDetailsList.get(position).setChatList(chatMessageList);
        contactDetailsList.get(position).setLastMessage(lastMessage);
        MyPreferenceManager.setContactList(this,contactDetailsList);
        Intent intent=new Intent(ChatScreenActivity.this,BaseDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("isFromChat",true);
        startActivity(intent);
    }

    @OnClick(R.id.back_btn_iv)
    public void onBackIconPressed()
    {
        onBackPressed();
        //finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //contactDetails.setChatList(chatMessageList);
        contactDetailsList.get(position).setChatList(chatMessageList);
        contactDetailsList.get(position).setLastMessage(lastMessage);
        MyPreferenceManager.setContactList(this,contactDetailsList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contactDetailsList.get(position).setChatList(chatMessageList);
        contactDetailsList.get(position).setLastMessage(lastMessage);
        MyPreferenceManager.setContactList(this,contactDetailsList);
    }
}
