package com.example.ashutosh.chatupfinal;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.avi.contacts.ContactQuery;
import com.avi.contacts.ContactsFetchManager;
import com.avi.contacts.model.ContactInfo;
import com.avi.contacts.model.ContactQueryType;
import com.example.ashutosh.chatupfinal.adapters.ContactRecyclerAdapter;
import com.example.ashutosh.chatupfinal.interfaces.ContactItemClickListener;
import com.example.ashutosh.chatupfinal.interfaces.Updateable;
import com.example.ashutosh.chatupfinal.modal.ContactDetails;
import com.example.ashutosh.chatupfinal.modal.ContactObject;
import com.example.ashutosh.chatupfinal.modal.UserDetails;
import com.example.ashutosh.chatupfinal.preferences.MyPreferenceManager;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.*;
import java.util.concurrent.Semaphore;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.READ_CONTACTS;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static com.example.ashutosh.chatupfinal.BaseDashboardActivity.contactDetailList;

/**
 * Created by Ashutosh on 06-01-2018.
 */
public class ContactFragment extends Fragment implements ContactItemClickListener,Updateable
{
    View view;

    //@BindView(R.id.search_et)
    AutoCompleteTextView searchET;
    //@BindView(R.id.contact_recycler_view)
    RecyclerView contactRecyclerView;
    ContactRecyclerAdapter adapter;

    DatabaseReference mDatabaseRef;
    ProgressDialog pro;
    int round=0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("users");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_contact,container,false);
        searchET =(AutoCompleteTextView)view.findViewById(R.id.search_et);
        contactRecyclerView = (RecyclerView)view.findViewById(R.id.contact_recycler_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contactDetailList = MyPreferenceManager.getContactList(getActivity());
        if(contactDetailList!=null)
        {
            adapter = new ContactRecyclerAdapter(getActivity(),contactDetailList);
            contactRecyclerView.setAdapter(adapter);
            contactRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter.setClickListener(this);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view, int position) {
        //Toast.makeText(getActivity(), contactDetailsList.get(position)+" clicked", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getActivity(),ChatScreenActivity.class);
        Bundle b=new Bundle();
        b.putInt("position",position);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtras(b);
        startActivity(intent);

    }

    @Override
    public void update() {
        adapter.notifyData(contactDetailList);
    }

}
