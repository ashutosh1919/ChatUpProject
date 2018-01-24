package com.example.ashutosh.chatupfinal.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.*;

import com.example.ashutosh.chatupfinal.R;
import com.example.ashutosh.chatupfinal.modal.ChatMessage;

/**
 * Created by Ashutosh on 20-01-2018.
 */

public class ChatScreenAdapter extends RecyclerView.Adapter
{
    private static final int MESSAGE_TYPE_SENT=1;
    private static final int MESSAGE_TYPE_RECEIVED=0;

    private Context mContext;
    private List<ChatMessage> mMessageList;

    public ChatScreenAdapter(Context context,List<ChatMessage> mMessageList)
    {
        this.mContext=context;
        this.mMessageList=mMessageList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == MESSAGE_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_my_bubble, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == MESSAGE_TYPE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_opposite_bubble, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = (ChatMessage) mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case MESSAGE_TYPE_SENT:
                ((SentMessageHolder) holder).bind(chatMessage);
                break;
            case MESSAGE_TYPE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(chatMessage);
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = (ChatMessage) mMessageList.get(position);

        if (chatMessage.getMessageType()==MESSAGE_TYPE_SENT) {
            // If the current user is the sender of the message
            return MESSAGE_TYPE_SENT;
        } else {
            // If some other user sent the message
            return MESSAGE_TYPE_RECEIVED;
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeTV;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.sent_message_tv);
            timeTV = (TextView)itemView.findViewById(R.id.message_time_tv);

        }

        void bind(ChatMessage chatMessage) {
            messageText.setText(chatMessage.getMessageContent());
            timeTV.setText(chatMessage.getTimeOfMessage());
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeTV;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.received_message_tv);
            timeTV = (TextView)itemView.findViewById(R.id.message_time_tv);
        }

        void bind(ChatMessage chatMessage) {
            messageText.setText(chatMessage.getMessageContent());
            timeTV.setText(chatMessage.getTimeOfMessage());
        }
    }

    public void notifyData(List<ChatMessage> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.mMessageList = myList;
        notifyDataSetChanged();
    }

}
