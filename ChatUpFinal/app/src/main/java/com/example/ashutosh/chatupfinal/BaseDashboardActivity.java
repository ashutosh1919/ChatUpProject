package com.example.ashutosh.chatupfinal;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashutosh.chatupfinal.adapters.BasePagerAdapter;
import com.example.ashutosh.chatupfinal.modal.ContactDetails;
import com.example.ashutosh.chatupfinal.preferences.MyPreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import java.util.*;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * Created by Ashutosh on 06-01-2018.
 */

public class BaseDashboardActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener
{
    @BindView(R.id.custom_toolbar_dashboard)
    Toolbar toolbar;
    @BindView(R.id.img_btn_profile)
    ImageView imgProfileBtn;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.frag_chat)
    RelativeLayout fragChat;
    @BindView(R.id.linlaHeaderProgress)
    LinearLayout linlaHeaderProgress;
    //@BindView(R.id.container)
    //FrameLayout containerLayout;
    DatabaseReference mDatabaseRef;

    public static List<ContactDetails> contactDetailList;
    private static final int REQUEST_READ_CONTACTS = 444;

    Animation rotate_forward,rotate_backward;


    boolean isBackPressed=false;
    static final int PICK_CONTACT=1;
    public String nameToAdd="";
    public String phoneNumberToAdd="";

    EditText nameET;
    EditText phoneNumberET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat_Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users");

        setSupportActionBar(toolbar);

        tabLayout.addTab(tabLayout.newTab().setText("CHATS"));
        tabLayout.addTab(tabLayout.newTab().setText("CONTACTS"));
        tabLayout.addTab(tabLayout.newTab().setText("CALLS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        BasePagerAdapter adapter = new BasePagerAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        contactDetailList = MyPreferenceManager.getContactList(this);

        boolean ex=getIntent().getBooleanExtra("isFromChat",false);
        if(ex)
        {
            viewPager.setCurrentItem(2,true);
            viewPager.setCurrentItem(0,true);
        }

        String ans=getIntent().getStringExtra("fromContact");
        if(ans!=null) {
            if (ans.equals("yes"))
                viewPager.setCurrentItem(1, true);
            linlaHeaderProgress.setVisibility(View.GONE);
        }


        ImageView icon=new ImageView(this);
        icon.setImageResource(R.drawable.add_icon);

        ImageView clearIcon = new ImageView(this);
        clearIcon.setImageResource(R.drawable.cross_floating_icon);

        final FloatingActionButton fab=new FloatingActionButton.Builder(this).setContentView(icon).build();
        fab.setBackgroundDrawable(getResources().getDrawable(R.drawable.floating_action_background));

        SubActionButton.Builder builder=new SubActionButton.Builder(this);

        ImageView contactsIcon=new ImageView(this);
        contactsIcon.setImageResource(R.drawable.show_contacts_icon);
        SubActionButton contactsBtn=builder.setContentView(contactsIcon).build();
        contactsBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.floating_contact_background));

        ImageView addFriendIcon=new ImageView(this);
        addFriendIcon.setImageResource(R.drawable.add_friend_icon);
        SubActionButton addFriendBtn=builder.setContentView(addFriendIcon).build();
        addFriendBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.floating_friend_background));

        ImageView addGroupIcon=new ImageView(this);
        addGroupIcon.setImageResource(R.drawable.add_group_icon);
        SubActionButton addGroupBtn=builder.setContentView(addGroupIcon).build();
        addGroupBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.floating_group_background));

        final FloatingActionMenu fam=new FloatingActionMenu.Builder(this)
                .addSubActionView(contactsBtn)
                .addSubActionView(addFriendBtn)
                .addSubActionView(addGroupBtn)
                .attachTo(fab)
                .build();

        fam.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
                fab.startAnimation(rotate_forward);
            }

            @Override
            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
                fab.startAnimation(rotate_backward);
            }
        });

        contactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BaseDashboardActivity.this, "Contacts Clicked", Toast.LENGTH_SHORT).show();
                fam.close(true);
                viewPager.setCurrentItem(1,true);
            }
        });
        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(BaseDashboardActivity.this, "Add Friends Clicked", Toast.LENGTH_SHORT).show();
                fam.close(true);

                final Dialog dialog = new Dialog(BaseDashboardActivity.this);
                // Include dialog.xml file
                dialog.setContentView(R.layout.add_contact_dialog);

                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                // Set dialog title
                // set values for custom dialog components - text, image and button

                dialog.show();

                nameET  = (EditText)dialog.findViewById(R.id.name_et);
                phoneNumberET = (EditText)dialog.findViewById(R.id.phone_et);

                TextView addFromContactTV = (TextView)dialog.findViewById(R.id.add_from_contact_tv);
                addFromContactTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!mayRequestContacts())
                        {
                            //Toast.makeText(BaseDashboardActivity.this, ".", Toast.LENGTH_SHORT).show();
                        }

                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(intent, PICK_CONTACT);
                    }
                });

                TextView addTv = (TextView)dialog.findViewById(R.id.add_contact_tv);
                addTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(BaseDashboardActivity.this, "Added", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        contactDetailList = MyPreferenceManager.getContactList(BaseDashboardActivity.this);
                        if(contactDetailList == null)
                        {
                            contactDetailList = new ArrayList<ContactDetails>();
                        }
                        if(!nameET.getText().toString().trim().isEmpty() && !phoneNumberET.getText().toString().trim().isEmpty())
                        {
                            int flag=0;
                            for(int i=0;i<contactDetailList.size();i++)
                                if(phoneNumberET.getText().toString().trim().equals(contactDetailList.get(i).getPhoneNumber()))
                                {
                                    flag=1;
                                    break;
                                }
                            if(flag!=1)
                            {
                                mDatabaseRef.child(phoneNumberET.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists())
                                        {
                                            String fullName = dataSnapshot.child("fullName").getValue().toString();
                                            String userName = dataSnapshot.child("userName").getValue().toString();
                                            //String profileUrl =null;
                                            //profileUrl = dataSnapshot.child("profile_url").getValue().toString();

                                            contactDetailList.add(new ContactDetails(nameET.getText().toString().trim(),fullName,userName,phoneNumberET.getText().toString().trim(),"null"));
                                            //ContactFragment.adapter.notifyData(contactDetailList);
                                            Log.d("Info Add ",fullName+" "+userName+" "+phoneNumberET.getText().toString());
                                            Toast.makeText(BaseDashboardActivity.this, "Contact is Added!", Toast.LENGTH_SHORT).show();
                                            //MyPreferenceManager.setContactList(BaseDashboardActivity.this,contactDetailList);
                                            //dialog.dismiss();
                                            /*Intent i = new Intent(BaseDashboardActivity.this,BaseDashboardActivity.class);
                                            i.putExtra("fromContact","yes");
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i);*/

                                            linlaHeaderProgress.setVisibility(View.VISIBLE);
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    MyPreferenceManager.setContactList(BaseDashboardActivity.this,contactDetailList);
                                                    Intent i = new Intent(BaseDashboardActivity.this,BaseDashboardActivity.class);
                                                    i.putExtra("fromContact","yes");
                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);
                                                }
                                            }).start();

                                        }
                                        else
                                        {
                                            Toast.makeText(BaseDashboardActivity.this, "Given Number is not Registered for this Page.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(BaseDashboardActivity.this, "Contact is Already there in Contact List.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                TextView cancelTv = (TextView)dialog.findViewById(R.id.cancel_tv);
                cancelTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });
        addGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BaseDashboardActivity.this, "Add Group Clicked", Toast.LENGTH_SHORT).show();
                fam.close(true);
            }
        });


    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c =  managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {


                        String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                                    null, null);
                            phones.moveToFirst();
                            phoneNumberToAdd = phones.getString(phones.getColumnIndex("data1"));
                            phoneNumberToAdd= phoneNumberToAdd.replaceAll("[^0-9]", "");

                            if(phoneNumberToAdd.length()>10)
                                phoneNumberToAdd=phoneNumberToAdd.substring(phoneNumberToAdd.length()-10);

                            //System.out.println("number is:"+cNumber);
                        }
                        nameToAdd = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                    }

                    if(!nameToAdd.isEmpty() && !phoneNumberToAdd.isEmpty())
                    {
                        nameET.setText(nameToAdd);
                        phoneNumberET.setText(phoneNumberToAdd);
                    }
                    else
                    {
                        Toast.makeText(BaseDashboardActivity.this, "Fate hue, number dal", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.img_btn_profile)
    public void profileBtnClicked()
    {
        //fragChat.setVisibility(View.GONE);
        //containerLayout.setVisibility(View.VISIBLE);
        startActivity(new Intent(BaseDashboardActivity.this,ProfileActivity.class));
        /*FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.profile_fragment_container, new ProfileFragment(), "ProfileFragment");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack("ProfileFragment");
        fragmentTransaction.commit();*/
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        tab.select();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        inflateCustomLayout(tab.getPosition());
    }

    private void inflateCustomLayout(int selectedPosition) {

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        FragmentManager fManager = getSupportFragmentManager();
        int count = fManager.getBackStackEntryCount();

        if(getSupportFragmentManager().findFragmentByTag("ProfileFragment") != null)
        {
            getSupportFragmentManager().popBackStackImmediate("ProfileFragment",0);
            /*fragChat.setVisibility(View.VISIBLE);
            FragmentManager.BackStackEntry bs1 = fManager.getBackStackEntryAt(count - 1);
            bs1.getName();
            Log.e("Log", "Fragment Name : " + bs1.getName());

            FragmentManager.BackStackEntry bs = fManager.getBackStackEntryAt(count - 1);
            fManager.popBackStack(bs.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);*/
        }
        else
        {
            if(!isBackPressed)
            {
                isBackPressed=true;
                Toast.makeText(this, "Press again to exit the app.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                finish();
            }

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isBackPressed = false;
                }
            }, 2000);

        }
    }
}
