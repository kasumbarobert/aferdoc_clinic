package com.aferdoc.clinic;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by robert on 2/19/18.
 */

public class USSDMessage extends Message {

    private String phone_number;

    public USSDMessage(String sender_id, String message,String phone_number, String sender_name, String timestamp,int message_source ){
        super(sender_id,message,phone_number,timestamp,"",message_source);
        this.phone_number = phone_number;
    }

    public USSDMessage(JSONObject message, int message_source) throws JSONException {
        super(message,message_source);
        this.phone_number=message.getString("sender_name");
    }
    public String getPhone_number() {
        return phone_number;
    }
}
