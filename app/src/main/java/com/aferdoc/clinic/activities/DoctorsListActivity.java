package com.aferdoc.clinic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aferdoc.clinic.CurrentProfile;
import com.aferdoc.clinic.Message;
import com.aferdoc.clinic.R;
import com.aferdoc.clinic.USSDMessage;
import com.aferdoc.clinic.adapters.ChatsListRecyclerAdapter;
import com.aferdoc.clinic.singletons.MyVolleySingleton;
import com.aferdoc.clinic.singletons.Server;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DoctorsListActivity extends AppCompatActivity {
    Button search_doctors_btn;
    RecyclerView doctors_recy_view;
    ChatsListRecyclerAdapter chatsListRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        doctors_recy_view = findViewById(R.id.doctors_recy_view);
        search_doctors_btn = findViewById(R.id.search_doctors_btn);
        chatsListRecyclerAdapter = new ChatsListRecyclerAdapter(this,
                new ArrayList<Message>(),
                Message.MESSAGE_TYPE_DOC_TO_DOC);
        doctors_recy_view.setAdapter(chatsListRecyclerAdapter);
        doctors_recy_view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        loadMessages();

        search_doctors_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(DoctorsListActivity.this,SearchActivity.class);
                    startActivity(intent);
            }
        });

    }

    public void loadMessages(){
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("doctor_id", CurrentProfile.getInstance().getCurrentDoctor().getUserId());
        jsonParams.put("action", "get_consult_messages");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Server.CONTROLLER_PATH, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("RESULT",response.toString());

                    JSONArray songs = response.getJSONArray("messages");
                    ArrayList<Message> messages = new ArrayList<>();
                    for(int i=0; i< songs.length(); i++){
                        JSONObject song_details = songs.getJSONObject(i);
                        messages.add(new Message(song_details, Message.MESSAGE_SOURCE_PATIENT));
                    }
                    chatsListRecyclerAdapter.changeChats(messages);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR PROFILE", error.toString());
            }
        });
        MyVolleySingleton.getInstance(DoctorsListActivity.this).addToRequestQueue(jsonObjectRequest);


    }

}
