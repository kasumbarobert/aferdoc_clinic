package com.aferdoc.clinic;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * Created by robert on 2/17/18.
 */

public class Message  implements Serializable, Comparable<Message>{

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        DateFormat df = new SimpleDateFormat("yyyy-mm-mm hh:mm:ss");
        try {
            date_received = df.parse(this.createdAt);
        }
        catch (ParseException exception){
            exception.printStackTrace();
        }

    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage_subject() {
        return message_subject;
    }

    public void setMessage_subject(String message_subject) {
        this.message_subject = message_subject;
    }

    private Date date_received;

    private String message;

    public String getMessage_header() {
        if (message_subject != null){
            if(message_subject.length()>1){
                return message_subject;
            }
        }


            if(message.length()>200){
                return message.substring(0,199);
            }
            else {
                return message;
            }


    }

    public void setMessage_header(String message_header) {
        this.message_header = message_header;
    }

    private String message_header;
    private String createdAt;
    private String sender_id;
    private String name;

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    private String receiver_id;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    private  int state;

    public String getMessageSubject() {
        return message_subject;
    }

    public void setMessageSubject(String message_subject) {
        this.message_subject = message_subject;
    }

    private String message_subject;

    public String getImagePath() {
        return image_path;
    }

    private String image_path="";
    public int getMessageSource() {
        return message_source;
    }

    private int message_source;
    public  static final int MESSAGE_SOURCE_DOCTOR  =10001;
    public  static final int MESSAGE_SOURCE_PATIENT  =20002;
    public  static final int MESSAGE_TYPE_USSD =14526;
    public  static final int MESSAGE_TYPE_CHAT =148926;
    public  static final int MESSAGE_TYPE_DOC_TO_DOC =148726;
    public Message(String sender_id, String message, String sender_name, String timestamp,String image_path,int message_source ){
        this.message = message;
        this.sender_id =sender_id;
        this.name = sender_name;
        this.createdAt = timestamp;
        this.message_source = message_source;
        this.image_path = image_path;
    }

    public Message(String receiver_id){
        setReceiver_id(receiver_id);
    }

    public Message(JSONObject message,int message_source) throws JSONException {
        setMessage(message.getString("message"));
        setName(message.getString("sender_name"));
        setSender_id(message.getString("sender_id"));
        this.message_source = message_source;
        setCreatedAt(message.getString("date_sent"));
        setMessage_header(message.getString("message_subject"));
        setState(message.getInt("state"));
        setReceiver_id(message.getString("receiver_id"));
        this.image_path =message.getString("attached_image_url");
    }
    public String getMessage() {
        return message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getSenderId() {
        return sender_id;
    }

    public String getName(){
        return  name;
    }
    public Date getDate_received(){
        return date_received;
    }
    @Override
    public int compareTo(@NonNull Message o) {
        if(date_received.getTime()>o.getDate_received().getTime()){
            return 1;
        }
        else if(date_received.getTime()<o.getDate_received().getTime()){
            return -1;
        }
        else {
            return 0;
        }

    }
}
