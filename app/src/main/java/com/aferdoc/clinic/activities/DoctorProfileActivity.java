package com.aferdoc.clinic.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aferdoc.clinic.CurrentProfile;
import com.aferdoc.clinic.Doctor;
import com.aferdoc.clinic.Message;
import com.aferdoc.clinic.R;
import com.aferdoc.clinic.singletons.MyVolleySingleton;
import com.aferdoc.clinic.singletons.Server;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class DoctorProfileActivity extends AppCompatActivity {

    ImageView profile_image;
    Button chat_btn;
    TextView account_number_value_tv,doc_name_tv,consultation_fee_value_tv,experience_value_tv,availability_value_tv,workplace_value_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        doc_name_tv = findViewById(R.id.doc_name_tv);
        workplace_value_tv = findViewById(R.id.workplace_value_tv);
        account_number_value_tv = findViewById(R.id.account_number_value_tv);
        consultation_fee_value_tv = findViewById(R.id.consultation_fee_value_tv);
        experience_value_tv = findViewById(R.id.experience_value_tv);
        availability_value_tv = findViewById(R.id.availability_value_tv);
        profile_image = findViewById(R.id.profile_image);
        chat_btn = findViewById(R.id.edit_profile);

        try {
            Doctor doctor =(Doctor) getIntent().getSerializableExtra("practitioner");
            getSupportActionBar().setTitle(doctor.getName());
            intitializeProfileItems(doctor);
        }
        catch (Exception e){
            finish();
        }


    }
    public void intitializeProfileItems(final Doctor doctor){
        workplace_value_tv.setText(doctor.getWorkPlace());
        account_number_value_tv.setText(doctor.getAccountNumber());
        consultation_fee_value_tv.setText(doctor.getConsultationFee());
        experience_value_tv.setText(doctor.getExperience());
        availability_value_tv.setText(doctor.getAvailabilty());
        doc_name_tv.setText(doctor.getName());
        // Glide.with(this).load(CurrentProfile.getInstance().getProfileImagePath());
        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message m = new Message(doctor.getUserId());
                m.setName(doctor.getName());
                Intent intent = new Intent(DoctorProfileActivity.this,ConsultsChatActivity.class);
                intent.putExtra("chat_title",m);
                DoctorProfileActivity.this.startActivity(intent);
            }
        });
    }



}
