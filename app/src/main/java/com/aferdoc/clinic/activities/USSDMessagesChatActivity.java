package com.aferdoc.clinic.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aferdoc.clinic.CurrentProfile;
import com.aferdoc.clinic.Message;
import com.aferdoc.clinic.R;
import com.aferdoc.clinic.USSDMessage;
import com.aferdoc.clinic.adapters.USSDChatRecyAdapter;
import com.aferdoc.clinic.singletons.MyVolleySingleton;
import com.aferdoc.clinic.singletons.Server;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class USSDMessagesChatActivity extends AppCompatActivity {
    RecyclerView reyclerview_message_list;
    USSDChatRecyAdapter webMessagesRecyAdapter;
    LinearLayoutManager linearLayoutManager;
    EditText edittext_chatbox;
    Button button_chatbox_send;
    String current_phone_number;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ussdmessageschat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reyclerview_message_list = findViewById(R.id.reyclerview_message_list);
        edittext_chatbox = findViewById(R.id.edittext_chatbox);
        button_chatbox_send = findViewById(R.id.button_chatbox_send);
        webMessagesRecyAdapter = new USSDChatRecyAdapter(this, new ArrayList<Message>());
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        reyclerview_message_list.setLayoutManager(linearLayoutManager);
        reyclerview_message_list.setAdapter(webMessagesRecyAdapter);
        getSupportActionBar().setTitle(getIntent().getStringExtra("chat_title"));
        current_phone_number=getIntent().getStringExtra("chat_title");

        loadMessages(CurrentProfile.getInstance().getCurrentUSSDChat());
        dialog = new ProgressDialog(this);
        button_chatbox_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edittext_chatbox.getText().toString();
                if (text.length()>160){
                    Toast.makeText(USSDMessagesChatActivity.this,"The message is too long",Toast.LENGTH_LONG).show();
                }
                else if (text.length()>0)
                {
                    sendMessage(text);
                }
            }
        });

    }

    public void loadMessages(ArrayList<USSDMessage> ussdMessages){


        webMessagesRecyAdapter.changeMessages(ussdMessages);

    }


    public void sendMessage(final String text){

        try {
            final JSONObject jsonParams = new JSONObject();
            jsonParams.put("action", "send_sms");
            jsonParams.put("message",text);
            jsonParams.put("doctor_id", CurrentProfile.getInstance().getCurrentDoctor().getUserId());
            jsonParams.put("phone_number", current_phone_number);
            dialog.setTitle("Sending ...");
            dialog.show();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Server.CONTROLLER_PATH, jsonParams, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    try {
                        Log.d("RESPONSE", response.toString());
                        Log.d("Sent", jsonParams.toString());
                        JSONObject send_response = response.getJSONObject("sms_response");
                        if (send_response.getInt("success")==1){
                            java.util.Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-mm hh:mm:ss");
                            String formattedDate = df.format(c);
                            JSONObject message = new JSONObject();
                            webMessagesRecyAdapter.addMessage(new USSDMessage(
                                    CurrentProfile.getInstance().getCurrentDoctor().getUserId(),current_phone_number,
                                    text,CurrentProfile.getInstance().getCurrentDoctor().getName(),formattedDate
                                    ,Message.MESSAGE_SOURCE_DOCTOR));
                            reyclerview_message_list.scrollToPosition(webMessagesRecyAdapter.getItemCount()-1);
                            edittext_chatbox.setText("");
                        }
                        else{
                            Toast.makeText(USSDMessagesChatActivity.this,"Message not sent! send again",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Log.d("ERROR PROFILE", error.toString());
                }
            });
            MyVolleySingleton.getInstance(USSDMessagesChatActivity.this).addToRequestQueue(jsonObjectRequest);




        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
