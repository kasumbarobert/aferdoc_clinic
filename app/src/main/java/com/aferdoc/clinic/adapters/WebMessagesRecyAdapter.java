package com.aferdoc.clinic.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aferdoc.clinic.CurrentProfile;
import com.aferdoc.clinic.Message;
import com.aferdoc.clinic.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 2/17/18.
 */

public class WebMessagesRecyAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Message> mMessageList;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public WebMessagesRecyAdapter(Context context, List<Message> messageList) {
        mContext = context;
        mMessageList = messageList;
    }


    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }


    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mMessageList.get(position);
        if (message.getSenderId().equals(CurrentProfile.getInstance().getCurrentDoctor().getUserId()) && message.getMessageSource()==Message.MESSAGE_SOURCE_DOCTOR) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }
    public void changeMessages(ArrayList<Message> messages) {
        mMessageList.clear();
        mMessageList.addAll(messages);
        notifyDataSetChanged();
    }
    public void addMessage(Message message) {
        mMessageList.add(message);
        notifyItemChanged(getItemCount());
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage,attached_image;
        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
            attached_image = itemView.findViewById(R.id.attached_image);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());
            // Format the stored timestamp into a readable String using method.
            //timeText.setText(DateUtils.formatDateTime(message.getCreatedAt()));
            nameText.setText(message.getName());
            timeText.setText(message.getCreatedAt());
            try {
                if (message.getImagePath().length() !=0 ){
                    Glide.with(mContext).load(message.getImagePath()).into(attached_image);
                    attached_image.setVisibility(View.VISIBLE);
                    Log.d("EMMA",message.getImagePath());
                }
            }catch (Exception e){

            }




            // Insert the profile image from the URL into the ImageView.
            //DateUtils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        ImageView attached_image;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            attached_image = itemView.findViewById(R.id.attached_image);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());
            timeText.setText(message.getCreatedAt());
            if (message.getImagePath().length() !=0 ){
                Glide.with(mContext).load(message.getImagePath()).into(attached_image);
                attached_image.setVisibility(View.VISIBLE);
                Log.d("EMMA",message.getImagePath());
            }
        }
    }
}