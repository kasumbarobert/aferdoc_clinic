package com.aferdoc.clinic.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aferdoc.clinic.CurrentProfile;
import com.aferdoc.clinic.Doctor;
import com.aferdoc.clinic.R;
import com.aferdoc.clinic.singletons.MyVolleySingleton;
import com.aferdoc.clinic.singletons.Server;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    ImageView profile_image;
    Button save_changes_btn,upload_img_btn;
    TextView account_number_value_tv,error_tv;
    EditText consultation_fee_value_tv,experience_value_tv,availability_value_tv,workplace_value_tv;
    String attached_image;
    final int RESULT_LOAD_IMAGE=979;
    EditText withdraw_AT_amount,withdraw_MM_amount;
    Button withdraw_MM_btn,withdraw_AT_btn;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        workplace_value_tv = findViewById(R.id.workplace_value_tv);
        account_number_value_tv = findViewById(R.id.account_number_value_tv);
        consultation_fee_value_tv = findViewById(R.id.consultation_fee_value_tv);
        experience_value_tv = findViewById(R.id.experience_value_tv);
        availability_value_tv = findViewById(R.id.availability_value_tv);
        profile_image = findViewById(R.id.profile_image_view);
        save_changes_btn = findViewById(R.id.save_changes_btn);
        upload_img_btn = findViewById(R.id.upload_img_btn);
        error_tv = findViewById(R.id.error_tv);
        dialog = new ProgressDialog(this);
        withdraw_MM_btn = findViewById(R.id.withdraw_MM_btn);
        withdraw_AT_btn = findViewById(R.id.withdraw_AT_btn);
        withdraw_AT_amount = findViewById(R.id.withdraw_AT_amount);
        withdraw_MM_amount = findViewById(R.id.withdraw_MM_amount);
        intitializeProfileItems();
    }

    public void intitializeProfileItems(){
        workplace_value_tv.setText(CurrentProfile.getInstance().getCurrentDoctor().getWorkPlace());
        account_number_value_tv.setText(CurrentProfile.getInstance().getCurrentDoctor().getAccountNumber());
        consultation_fee_value_tv.setText(CurrentProfile.getInstance().getCurrentDoctor().getConsultationFee());
        experience_value_tv.setText(CurrentProfile.getInstance().getCurrentDoctor().getExperience());
        availability_value_tv.setText(CurrentProfile.getInstance().getCurrentDoctor().getAvailabilty());
        // Glide.with(this).load(CurrentProfile.getInstance().getProfileImagePath());
        Glide.with(this).load(CurrentProfile.getInstance().getCurrentDoctor().getProfileImagePath()).into(profile_image);
        upload_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),RESULT_LOAD_IMAGE);
            }
        });
        save_changes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_tv.setVisibility(View.INVISIBLE);
                try {
                    int changes =0;
                    JSONObject profile = new JSONObject();
                    if (!workplace_value_tv.getText().toString().equals(CurrentProfile.getInstance().getCurrentDoctor().getWorkPlace())) {
                        profile.put("workplace",workplace_value_tv.getText().toString());
                        changes++;
                    }

                    if (!consultation_fee_value_tv.getText().toString().equals(CurrentProfile.getInstance().getCurrentDoctor().getConsultationFee())) {
                        profile.put("consultation_fee",consultation_fee_value_tv.getText().toString());
                        changes++;
                    }

                    if (!experience_value_tv.getText().toString().equals(CurrentProfile.getInstance().getCurrentDoctor().getExperience())) {
                        profile.put("experience",experience_value_tv.getText().toString());
                        changes++;
                    }

                    if (!availability_value_tv.getText().toString().equals(CurrentProfile.getInstance().getCurrentDoctor().getAvailabilty())) {
                        profile.put("availability",availability_value_tv.getText().toString());
                        changes++;
                    }

                    if (attached_image != null){
                        profile.put("profile_image",attached_image);
                        changes++;
                    }

                    if (changes>0){
                        final JSONObject update_profile = new JSONObject();
                        update_profile.put("action", "update_profile");
                        update_profile.put("doctor_id", CurrentProfile.getInstance().getCurrentDoctor().getUserId());
                        update_profile.put("profile",profile);
                        dialog.setTitle("Updating profile");
                        dialog.show();
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Server.CONTROLLER_PATH, update_profile, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("RESPONSE",response.toString());
                                dialog.dismiss();
                                try {
                                    JSONObject result = response.getJSONObject("result");
                                    if (result.getJSONObject("update_profile").getInt("success")==1){
                                        CurrentProfile.getInstance().getCurrentDoctor().changeProfile(result.getJSONObject("profile"));
                                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                        EditProfileActivity.this.startActivity(intent);
                                        finish();
                                        attached_image=null;
                                    }
                                    else {
                                        error_tv.setText("Profile not updated . "+result.getJSONObject("update_profile").getString("error_message"));
                                        error_tv.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dialog.dismiss();
                                error_tv.setText("Profile not updated . A problem occurred. Try Later");
                                error_tv.setVisibility(View.VISIBLE);

                            }
                        });
                        MyVolleySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(jsonObjectRequest);
                    }

                }catch (JSONException e){

                }

            }
        });

        withdraw_MM_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    float amount = Float.parseFloat(withdraw_MM_amount.getText().toString());
                    if (amount>0){
                        withdraw("mobile_money",amount);
                    }
                }catch (Exception e){

                }
            }
        });

        withdraw_AT_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    float amount = Float.parseFloat(withdraw_AT_amount.getText().toString());
                    if (amount>0){
                        withdraw("airtime",amount);
                    }
                }catch (Exception e){

                }
            }
        });
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
                Glide.with(this).load(bitmap).into(profile_image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }


    }

    public void withdraw(String type, final float amount){
        JSONObject jsonParams = new JSONObject();
        try {

            jsonParams.put("action", "withdraw_money");
            jsonParams.put("doctor_id", CurrentProfile.getInstance().getCurrentDoctor().getUserId());
            JSONObject prescription = new JSONObject();
            jsonParams.put("amount",amount);
            jsonParams.put("type",type);
            Log.d("Request",jsonParams.toString());
            dialog.setTitle("Please wait...");
            dialog.show();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Server.CONTROLLER_PATH, jsonParams, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    Intent intent = new Intent(EditProfileActivity.this, WithdrawFeedbackActivity.class);
                    JSONObject result = null;
                    Log.d("Response",response.toString());
                    try {
                        result = response.getJSONObject("withdraw_result");
                        intent.putExtra("result",result.getString("success"));
                        intent.putExtra("message",result.getString("message"));
                        if (result.getInt("success")==1){
                            intent.putExtra("transaction_id",result.getString("transaction_id"));
                            intent.putExtra("receipt_id",result.getString("receipt_id"));
                            intent.putExtra("amount",amount);
                        }

                        EditProfileActivity.this.startActivity(intent);
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

            MyVolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
