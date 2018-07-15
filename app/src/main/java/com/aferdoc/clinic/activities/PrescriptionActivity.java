package com.aferdoc.clinic.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aferdoc.clinic.CurrentProfile;
import com.aferdoc.clinic.R;
import com.aferdoc.clinic.singletons.MyVolleySingleton;
import com.aferdoc.clinic.singletons.Server;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PrescriptionActivity extends AppCompatActivity {

    EditText phone_number_edit_text, prescription_edit_text,diagnosis_edit_text;
    Button send_prescription_btn,completed_btn;
    TextView success_message, prescription_message;
    ConstraintLayout default_layout, success_layout;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        diagnosis_edit_text = findViewById(R.id.diagnosis_edit_text);
        prescription_edit_text = findViewById(R.id.prescription_edit_text);
        phone_number_edit_text = findViewById(R.id.phone_number_edit_text);
        default_layout = findViewById(R.id.default_layout);
        success_layout = findViewById(R.id.success_layout);
        success_message = findViewById(R.id.success_message);
        completed_btn = findViewById(R.id.completed_btn);
        prescription_message = findViewById(R.id.prescription_message);
        send_prescription_btn = findViewById(R.id.send_prescription_btn);
        dialog = new ProgressDialog(this);
        send_prescription_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPrescriptiom();
            }
        });

        completed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public boolean sendPrescriptiom(){

       if (phone_number_edit_text.getText().length()<9){
            phone_number_edit_text.requestFocus();
            return false;
        }
        else if(diagnosis_edit_text.getText().length()==0){
           diagnosis_edit_text.requestFocus();
            return false;
        }
        else if (prescription_edit_text.getText().length()==0){
            prescription_edit_text.requestFocus();
            return false;
        }
        if(phone_number_edit_text.getText().toString().startsWith("07")){
            phone_number_edit_text.setText("+256"+phone_number_edit_text.getText().toString().substring(1,phone_number_edit_text.getText().toString().length()));
        }
        else if(phone_number_edit_text.getText().toString().startsWith("7")) {
            phone_number_edit_text.setText("+256"+phone_number_edit_text.getText().toString());
        }
        JSONObject jsonParams = new JSONObject();
        try {

            jsonParams.put("action", "send_prescription");
            jsonParams.put("doctor_id", CurrentProfile.getInstance().getCurrentDoctor().getUserId());
            JSONObject prescription = new JSONObject();
            prescription.put("phone_number",phone_number_edit_text.getText().toString().substring(1,phone_number_edit_text.getText().toString().length()));
            prescription.put("diagnosis",diagnosis_edit_text.getText().toString());
            prescription.put("prescription",diagnosis_edit_text.getText().toString());
            jsonParams.put("prescription",prescription);
            dialog.setTitle("Sending");
            dialog.show();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Server.CONTROLLER_PATH, jsonParams, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    try {
                        Log.d("RESULT",response.toString());
                        JSONObject result = response.getJSONObject("prescription_response");
                        if (result.getInt("success")==1){
                            success_message.setText(result.getString("message"));
                            prescription_message.setText(Html.fromHtml(result.getString("sent_prescription")));
                            default_layout.setVisibility(View.GONE);
                            success_layout.setVisibility(View.VISIBLE);
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

            MyVolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return true;
    }
    }


