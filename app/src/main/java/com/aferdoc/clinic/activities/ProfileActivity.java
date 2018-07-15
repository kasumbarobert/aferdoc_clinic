package com.aferdoc.clinic.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aferdoc.clinic.CurrentProfile;
import com.aferdoc.clinic.R;
import com.aferdoc.clinic.singletons.MyVolleySingleton;
import com.aferdoc.clinic.singletons.Server;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    ImageView profile_image;
    Button edit_profile;
    TextView account_number_value_tv,doc_name_tv,consultation_fee_value_tv,
            experience_value_tv,availability_value_tv,workplace_value_tv,balance_value_tv;
    EditText withdraw_AT_amount,withdraw_MM_amount;
    Button withdraw_MM_btn,withdraw_AT_btn;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        doc_name_tv = findViewById(R.id.doc_name_tv);

        getSupportActionBar().setTitle(CurrentProfile.getInstance().getCurrentDoctor().getName());
        workplace_value_tv = findViewById(R.id.workplace_value_tv);
        account_number_value_tv = findViewById(R.id.account_number_value_tv);
        consultation_fee_value_tv = findViewById(R.id.consultation_fee_value_tv);
        experience_value_tv = findViewById(R.id.experience_value_tv);
        availability_value_tv = findViewById(R.id.availability_value_tv);
        balance_value_tv = findViewById(R.id.balance_value_tv);
        profile_image = findViewById(R.id.profile_image);
        edit_profile = findViewById(R.id.edit_profile);
        withdraw_MM_btn = findViewById(R.id.withdraw_MM_btn);
        withdraw_AT_btn = findViewById(R.id.withdraw_AT_btn);
        withdraw_AT_amount = findViewById(R.id.withdraw_AT_amount);
        withdraw_MM_amount = findViewById(R.id.withdraw_MM_amount);
        dialog = new ProgressDialog(this);
        intitializeProfileItems();



    }

    public void intitializeProfileItems(){
        workplace_value_tv.setText(CurrentProfile.getInstance().getCurrentDoctor().getWorkPlace());
        account_number_value_tv.setText(CurrentProfile.getInstance().getCurrentDoctor().getAccountNumber());
        consultation_fee_value_tv.setText(CurrentProfile.getInstance().getCurrentDoctor().getConsultationFee());
        experience_value_tv.setText(CurrentProfile.getInstance().getCurrentDoctor().getExperience());
        availability_value_tv.setText(CurrentProfile.getInstance().getCurrentDoctor().getAvailabilty());
        doc_name_tv.setText(CurrentProfile.getInstance().getCurrentDoctor().getName());
        balance_value_tv.setText(CurrentProfile.getInstance().getCurrentDoctor().getBalance());
       Glide.with(this).load(CurrentProfile.getInstance().getCurrentDoctor().getProfileImagePath()).into(profile_image);

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                ProfileActivity.this.startActivity(intent);
                finish();
            }
        });
    }



}
