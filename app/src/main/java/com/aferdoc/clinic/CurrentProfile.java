package com.aferdoc.clinic;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by robert on 2/17/18.
 */
public class CurrentProfile {
    private static final CurrentProfile ourInstance = new CurrentProfile();
    private ArrayList<USSDMessage> curUSSDChat;

    public Doctor getCurrentDoctor() {
        return currentDoctor;
    }

    Doctor currentDoctor;



    public void initializeProfile(HashMap user){
        currentDoctor = new Doctor(user);
    }

    public void initializeProfile (JSONObject user) throws JSONException

    {
        currentDoctor = new Doctor(user);
    }

    public static CurrentProfile getInstance(){
        return ourInstance;
    }


    public void setCurUSSDChat(ArrayList<USSDMessage> chatList) {
        this.curUSSDChat = chatList;
    }
    public ArrayList<USSDMessage> getCurrentUSSDChat(){

        return curUSSDChat;
    }
}
