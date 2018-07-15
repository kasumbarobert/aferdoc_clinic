package com.aferdoc.clinic.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aferdoc.clinic.CurrentProfile;
import com.aferdoc.clinic.Message;
import com.aferdoc.clinic.R;
import com.aferdoc.clinic.adapters.WebMessagesRecyAdapter;
import com.aferdoc.clinic.singletons.MyVolleySingleton;
import com.aferdoc.clinic.singletons.Server;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ConsultsChatActivity extends AppCompatActivity {

    RecyclerView reyclerview_message_list;
    WebMessagesRecyAdapter consultMessagesRecyAdapter;
    LinearLayoutManager linearLayoutManager;
    EditText edittext_chatbox;
    Button button_chatbox_send;
    ImageView attach_image;
    String cur_user_id, subject, attached_image;
    TextView attached_image_path;
    int RESULT_LOAD_IMAGE=979;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consults_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        reyclerview_message_list = findViewById(R.id.reyclerview_message_list);
        edittext_chatbox = findViewById(R.id.edittext_chatbox);
        button_chatbox_send = findViewById(R.id.button_chatbox_send);
        consultMessagesRecyAdapter = new WebMessagesRecyAdapter(this, new ArrayList<Message>());
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reyclerview_message_list.setLayoutManager(linearLayoutManager);
        reyclerview_message_list.setAdapter(consultMessagesRecyAdapter);
        attach_image = findViewById(R.id.attach_image);
        attached_image_path = findViewById(R.id.attached_image_path);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Message message = (Message) getIntent().getSerializableExtra("chat_title");
        getSupportActionBar().setTitle(message.getName());
        loadMessages(message);
        dialog = new ProgressDialog(this);
        cur_user_id = message.getReceiver_id();
        subject = message.getMessage_subject();
        if (cur_user_id.equals(CurrentProfile.getInstance().getCurrentDoctor().getUserId())) {
            cur_user_id = message.getSender_id();
        }

        button_chatbox_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edittext_chatbox.getText().toString();
                sendMessage(text);
            }
        });

        attach_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),RESULT_LOAD_IMAGE);

            }
        });
    }

    public void loadMessages(Message m) {
        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("doctor_id", CurrentProfile.getInstance().getCurrentDoctor().getUserId());
        jsonParams.put("practitioner_id", m.getReceiver_id());
        jsonParams.put("action", "load_consults_chat");
        Log.d("CHAT", m.getSender_id() + " " + m.getReceiver_id());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Server.CONTROLLER_PATH, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("RESULT", response.toString());

                    JSONArray songs = response.getJSONArray("consults");
                    ArrayList<Message> messages = new ArrayList<>();
                    for (int i = 0; i < songs.length(); i++) {
                        JSONObject song_details = songs.getJSONObject(i);
                        if (song_details.getString("sender_id").equals(CurrentProfile.getInstance().getCurrentDoctor().getUserId())) {
                            messages.add(new Message(song_details, Message.MESSAGE_SOURCE_DOCTOR));
                        } else {
                            messages.add(new Message(song_details, Message.MESSAGE_SOURCE_PATIENT));
                        }
                    }
                    consultMessagesRecyAdapter.changeMessages(messages);

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
        MyVolleySingleton.getInstance(ConsultsChatActivity.this).addToRequestQueue(jsonObjectRequest);


    }

    public void sendMessage(final String text) {
        JSONObject json_message = new JSONObject();
        try {
            json_message.put("from_hcp_id", CurrentProfile.getInstance().getCurrentDoctor().getUserId());
            json_message.put("to_hcp_id", cur_user_id);
            json_message.put("message", text);
            json_message.put("subject", subject);
            if(attached_image != null){
                json_message.put("attached_image",attached_image);
            }
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("action", "write_consult");
            jsonParams.put("message", json_message);
            jsonParams.put("doctor_id", CurrentProfile.getInstance().getCurrentDoctor().getUserId());

            dialog.setTitle("Sending ...");
            dialog.show();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Server.CONTROLLER_PATH, jsonParams, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    try {
                        JSONObject send_response = response.getJSONObject("send_response");
                        if (send_response.getInt("result") == 1) {
                            java.util.Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-mm hh:mm:ss");
                            String formattedDate = df.format(c);
                            consultMessagesRecyAdapter.addMessage(new Message(CurrentProfile.getInstance().getCurrentDoctor().getUserId(),
                                    text, CurrentProfile.getInstance().getCurrentDoctor().getName(), formattedDate,send_response.getString("image_path") ,Message.MESSAGE_SOURCE_DOCTOR));

                            reyclerview_message_list.scrollToPosition(consultMessagesRecyAdapter.getItemCount() - 1);
                            edittext_chatbox.setText("");
                            attached_image=null;
                            attached_image_path.setText("");
                        } else {
                            Toast.makeText(ConsultsChatActivity.this, "Message not sent! send again", Toast.LENGTH_LONG).show();
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
            MyVolleySingleton.getInstance(ConsultsChatActivity.this).addToRequestQueue(jsonObjectRequest);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream); //compress to which format you want.
                byte[] byte_arr = stream.toByteArray();
                attached_image = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                attached_image_path.setText("Image attached");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }


        }


    }
}

