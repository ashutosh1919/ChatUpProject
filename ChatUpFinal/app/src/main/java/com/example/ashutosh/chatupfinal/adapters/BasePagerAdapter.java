package com.example.ashutosh.chatupfinal.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;

import com.example.ashutosh.chatupfinal.BaseDashboardActivity;
import com.example.ashutosh.chatupfinal.CallFragment;
import com.example.ashutosh.chatupfinal.ChatFragment;
import com.example.ashutosh.chatupfinal.ContactFragment;
import com.example.ashutosh.chatupfinal.R;

/**
 * Created by Ashutosh on 06-01-2018.
 */

public class BasePagerAdapter extends FragmentStatePagerAdapter {

    private FragmentActivity mActivity;
    private String[] pageTitle;
    //integer to count number of tabs
    private int tabCount;


    public BasePagerAdapter(BaseDashboardActivity activity, FragmentManager fm, int tabCount) {
        super(fm);
        this.mActivity = activity;
        this.tabCount = tabCount;

        pageTitle = new String[3];
        String chatView = "CHATS";
        String contactView = "CONTACTS";
        String callView = "CALLS";
        pageTitle[0] = chatView;
        pageTitle[1] = contactView;
        pageTitle[2]= callView;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle[position];
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ChatFragment chatFragment=new ChatFragment();
                return chatFragment;
            case 1:
                ContactFragment contactFragment=new ContactFragment();
                return contactFragment;
            case 2:
                CallFragment callFragment=new CallFragment();
                return callFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public View getTabView(int position) {
        View v;

        if(position==0) {
            v = LayoutInflater.from(mActivity).inflate(R.layout.fragment_chat, null);
        }
        else if(position==1)
        {
            v = LayoutInflater.from(mActivity).inflate(R.layout.fragment_contact, null);
        }
        else
        {
            v = LayoutInflater.from(mActivity).inflate(R.layout.fragment_call, null);
        }
        return v;
    }

}
