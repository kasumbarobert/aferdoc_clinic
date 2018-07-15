package com.aferdoc.clinic.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aferdoc.clinic.CurrentProfile;
import com.aferdoc.clinic.Message;
import com.aferdoc.clinic.R;
import com.aferdoc.clinic.USSDMessage;
import com.aferdoc.clinic.activities.ConsultsChatActivity;
import com.aferdoc.clinic.activities.USSDMessagesChatActivity;
import com.aferdoc.clinic.activities.WebMessageChatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by robert on 2/17/18.
 */

public class ChatsListRecyclerAdapter extends Adapter {
    List<Message> chatHeads;
    List<Message> not_displayed;
    LayoutInflater layoutInflater;
    Context mContext;
    int messageType;
    public ChatsListRecyclerAdapter(Context context, List<Message> chatList, int messageType){
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
        this.chatHeads = chatList;
        this.messageType = messageType;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ChatHeaderViewHolder chatHeaderViewHolder = new ChatHeaderViewHolder(layoutInflater.inflate(R.layout.chat_header_message_card,parent,false));
        return chatHeaderViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = chatHeads.get(position);
        ((ChatHeaderViewHolder)holder).bind(message);
    }

    @Override
    public int getItemCount() {
        return chatHeads.size();
    }

    public void changeChats(ArrayList<Message> messages) {
        chatHeads.clear();
        not_displayed =messages;
        Map<String, Message> distinct = new HashMap();
        Collections.sort(messages);
        for(Message m : messages) {
            if (!distinct.containsKey(m.getSender_id())) {
                distinct.put(m.getSender_id(), m);
                chatHeads.add(m);
            }

        }

        notifyDataSetChanged();
    }

    private class ChatHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView profile_image;
        TextView senders_name_tv,message_time_stamp,message_tv;

        public ChatHeaderViewHolder(View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            senders_name_tv = itemView.findViewById(R.id.senders_name_tv);
            message_time_stamp = itemView.findViewById(R.id.message_time_stamp);
            message_tv = itemView.findViewById(R.id.message_tv);
            itemView.setOnClickListener(this);
        }

        public void bind(Message message){

            senders_name_tv.setText(message.getName());
            message_tv.setText(message.getMessage_header());
            message_time_stamp.setText(message.getCreatedAt());
        }

        @Override
        public void onClick(View v) {

            if (messageType==Message.MESSAGE_TYPE_CHAT){
              Intent intent = new Intent(mContext,WebMessageChatActivity.class);
                intent.putExtra("chat_title",chatHeads.get(getAdapterPosition()));
                mContext.startActivity(intent);
            }

            else if (messageType==Message.MESSAGE_TYPE_DOC_TO_DOC){
                Intent intent = new Intent(mContext,ConsultsChatActivity.class);
                intent.putExtra("chat_title",chatHeads.get(getAdapterPosition()));
                mContext.startActivity(intent);

            }
            else if (messageType==Message.MESSAGE_TYPE_USSD){
                Intent intent = new Intent(mContext,USSDMessagesChatActivity.class);
                USSDMessage m = (USSDMessage)chatHeads.get(getAdapterPosition());
                if(m.getReceiver_id().equals(CurrentProfile.getInstance().getCurrentDoctor().getUserId())){
                    CurrentProfile.getInstance().setCurUSSDChat(getChatList(m.getSender_id()));
                    intent.putExtra("chat_title",m.getPhone_number());
                    mContext.startActivity(intent);
                }
                else if (m.getSenderId().equals(CurrentProfile.getInstance().getCurrentDoctor().getUserId())){
                    CurrentProfile.getInstance().setCurUSSDChat(getChatList(m.getReceiver_id()));
                    intent.putExtra("chat_title",m.getPhone_number());
                    mContext.startActivity(intent);
                }



            }

        }
    }

    private ArrayList<USSDMessage> getChatList(String phonenumber){
        ArrayList<USSDMessage> related_chat = new ArrayList<>();

        for(Message m : not_displayed){
            if (m.getSenderId().equals(phonenumber) || m.getReceiver_id().equals(phonenumber)){
                related_chat.add((USSDMessage)m);
            }
        }

        return related_chat;
    }
}
