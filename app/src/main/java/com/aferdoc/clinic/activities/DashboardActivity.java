package com.aferdoc.clinic.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aferdoc.clinic.CurrentProfile;
import com.aferdoc.clinic.R;
import com.bumptech.glide.Glide;

public class DashboardActivity extends AppCompatActivity {
    ImageView profile_image_view;
    CardView profile_card,web_messages_card,ussd_card,prescription_card,doc_to_doc_card,settings_card;
    TextView doctor_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeDashBoard();
    }
    public void initializeDashBoard(){
        profile_card = findViewById(R.id.profile_card);
        web_messages_card = findViewById(R.id.web_messages_card);
        ussd_card = findViewById(R.id.ussd_card);
        prescription_card = findViewById(R.id.prescription_card);
        doc_to_doc_card = findViewById(R.id.doc_to_doc_card);
        settings_card = findViewById(R.id.settings_card);
        profile_image_view = findViewById(R.id.profile_image_view);
        doctor_name = findViewById(R.id.doctor_name);
        try {
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth(); // ((display.getWidth()*20)/100)
            int height = (int) (display.getHeight() * 0.4);// ((display.getHeight()*30)/100)
            ConstraintLayout.LayoutParams parms = new ConstraintLayout.LayoutParams(width,height);
            profile_image_view.setLayoutParams(parms);
            int card_width = (int)(0.95*height)/3;
          //  ussd_card.setLayoutParams(new GridLayout.LayoutParams());
        }catch (Exception e){
            int width = getScreenWidth(); // ((display.getWidth()*20)/100)
            int height = (int) (getScreenHeight() * 0.5);// ((display.getHeight()*30)/100)
            ConstraintLayout.LayoutParams parms = new ConstraintLayout.LayoutParams(width,height);
            profile_image_view.setLayoutParams(parms);
        }

        profile_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                DashboardActivity.this.startActivity(intent);
            }
        });
        web_messages_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, WebMessagesActivity.class);
                DashboardActivity.this.startActivity(intent);
            }
        });
        ussd_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, USSDMessagesActivity.class);
                DashboardActivity.this.startActivity(intent);
            }
        });
        settings_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, EditProfileActivity.class);
                DashboardActivity.this.startActivity(intent);
            }
        });
        doc_to_doc_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, DoctorsListActivity.class);
                DashboardActivity.this.startActivity(intent);
            }
        });
        prescription_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PrescriptionActivity.class);
                DashboardActivity.this.startActivity(intent);
            }
        });
        Glide.with(this).load(CurrentProfile.getInstance().getCurrentDoctor().getProfileImagePath()).into(profile_image_view);
        doctor_name.setText(CurrentProfile.getInstance().getCurrentDoctor().getName());
    }


    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

}
