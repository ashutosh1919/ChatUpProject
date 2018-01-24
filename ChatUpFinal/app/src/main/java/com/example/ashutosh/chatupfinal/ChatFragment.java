package com.example.ashutosh.chatupfinal;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashutosh.chatupfinal.adapters.ChatRecyclerAdapter;
import com.example.ashutosh.chatupfinal.adapters.ContactRecyclerAdapter;
import com.example.ashutosh.chatupfinal.interfaces.ContactItemClickListener;
import com.example.ashutosh.chatupfinal.modal.ContactDetails;
import com.example.ashutosh.chatupfinal.preferences.MyPreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.*;

import static com.example.ashutosh.chatupfinal.BaseDashboardActivity.contactDetailList;

/**
 * Created by Ashutosh on 06-01-2018.
 */
public class ChatFragment extends Fragment implements ContactItemClickListener
{
    View view;
    //@BindView(R.id.no_chat_tv)
    TextView noChatTV;
    //@BindView(R.id.chat_list_rv)
    RecyclerView chatContactRecyclerView;

    List<ContactDetails> chatContactDetails;
    List<ContactDetails> mainContacts;
    ChatRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(chatContactDetails==null)
            chatContactDetails = new ArrayList<ContactDetails>();

        mainContacts = MyPreferenceManager.getContactList(getActivity());
        if(mainContacts==null)
            mainContacts = new ArrayList<ContactDetails>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_chat,container,false);
        chatContactRecyclerView = (RecyclerView)view.findViewById(R.id.chat_list_rv);
        noChatTV = (TextView)view.findViewById(R.id.no_chat_tv);

        if(mainContacts.size()==0)
        {
            noChatTV.setVisibility(View.VISIBLE);
            chatContactRecyclerView.setVisibility(View.GONE);
        }
        else
        {

            chatContactRecyclerView.setVisibility(View.VISIBLE);
            noChatTV.setVisibility(View.GONE);

            for(int i=0;i<mainContacts.size();i++)
            {
                if(mainContacts.get(i).getChatList()!=null)
                    chatContactDetails.add(mainContacts.get(i));

                //Toast.makeText(getActivity(), mainContacts.get(i).getLastMessage(), Toast.LENGTH_SHORT).show();
            }

            adapter = new ChatRecyclerAdapter(getActivity(),chatContactDetails);
            chatContactRecyclerView.setAdapter(adapter);
            chatContactRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter.setClickListener(this);

        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view, int position) {

        Toast.makeText(getActivity(), "Item is clicked", Toast.LENGTH_SHORT).show();

        ContactDetails contactDetails = chatContactDetails.get(position);

        for(int i=0;i<contactDetailList.size()-1;i++)
        {
            if(contactDetailList.get(i).getPhoneNumber().equals(contactDetails.getPhoneNumber()))
            {
                position = i;
                break;
            }
        }

        Intent intent=new Intent(getActivity(),ChatScreenActivity.class);
        Bundle b=new Bundle();
        b.putInt("position",position);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtras(b);
        startActivity(intent);
    }
}
