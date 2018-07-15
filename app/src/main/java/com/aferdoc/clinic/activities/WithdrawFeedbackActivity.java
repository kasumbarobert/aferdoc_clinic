package com.aferdoc.clinic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.aferdoc.clinic.CurrentProfile;
import com.aferdoc.clinic.R;

public class WithdrawFeedbackActivity extends AppCompatActivity {
    ConstraintLayout success_layout;
    TextView feedback_message,phone_number_tv,amount_tv,transaction_id_tv,reference_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        success_layout = findViewById(R.id.success_layout);
        feedback_message = findViewById(R.id.feedback_message);
        phone_number_tv = findViewById(R.id.phone_number_tv);
        transaction_id_tv = findViewById(R.id.transaction_id_tv);
        amount_tv = findViewById(R.id.amount_tv);
        reference_tv = findViewById(R.id.reference_tv);
        Intent intent = getIntent();

        if (intent.getStringExtra("result").equals("0")){
            success_layout.setVisibility(View.GONE);
            feedback_message.setText(intent.getStringExtra("message"));
            feedback_message.setTextColor(this.getResources().getColor(R.color.colorPrimary));
        }
        else {
            feedback_message.setText(intent.getStringExtra("message"));
            feedback_message.setTextColor(this.getResources().getColor(R.color.text_green));
            phone_number_tv.setText(CurrentProfile.getInstance().getCurrentDoctor().getAccountNumber());
            amount_tv.setText(intent.getStringExtra("amount"));
            transaction_id_tv.setText(intent.getStringExtra("transaction_id"));
            reference_tv.setText(intent.getStringExtra("receipt_id"));
        }

    }

}
