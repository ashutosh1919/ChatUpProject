package com.example.ashutosh.chatupfinal.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ashutosh.chatupfinal.R;
import com.example.ashutosh.chatupfinal.interfaces.ContactItemClickListener;
import com.example.ashutosh.chatupfinal.modal.ContactDetails;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ashutosh on 22-01-2018.
 */

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder>
{
    Context context;
    List<ContactDetails> data;
    ContactItemClickListener clickListener;
    int size=0;

    public  ChatRecyclerAdapter(Context context, List<ContactDetails> data){
        this.context=context;
        this.data=data;
        size = data.size();
    }

    @Override
    public ChatRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_row, null);
        ChatRecyclerAdapter.ViewHolder viewHolder = new ChatRecyclerAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ChatRecyclerAdapter.ViewHolder holder, int position) {
        //if(data.get(position).getProfileUrl().equals("null"))
        holder.profileIV.setImageDrawable(context.getResources().getDrawable(R.drawable.default_profile_image));
        holder.nameOfContactTV.setText(data.get(position).getNameToShow());
        holder.lastMessageTV.setText(data.get(position).getChatList().get(data.get(position).getChatList().size()-1).getMessageContent());
        holder.lastTimeMessage.setText(data.get(position).getChatList().get(data.get(position).getChatList().size()-1).getTimeOfMessage());
        // TODO: holder for message count is remaining
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        CardView parent;
        CircleImageView profileIV;
        TextView nameOfContactTV;
        TextView lastMessageTV;
        TextView lastTimeMessage;
        TextView newMessageCount;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            parent = (CardView)itemView.findViewById(R.id.item_parent);
            profileIV =(CircleImageView)itemView.findViewById(R.id.profile_iv);
            nameOfContactTV = (TextView)itemView.findViewById(R.id.nameOfContact);
            lastMessageTV = (TextView)itemView.findViewById(R.id.lastMessageTV);
            lastTimeMessage = (TextView) itemView.findViewById(R.id.message_time_tv);
            newMessageCount = (TextView)itemView.findViewById(R.id.number_of_messages_received);
        }

        @Override
        public void onClick(View v)
        {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }

    public void setClickListener(ContactItemClickListener itemClickListener)
    {
        this.clickListener = itemClickListener;
    }

    public void notifyData(List<ContactDetails> myList) {
        Log.d("notifyData ", myList.size() + "");
        if(size!=myList.size()) {
            this.data = myList;
            size=data.size();
            notifyDataSetChanged();
        }
    }

}

