package com.aferdoc.clinic.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aferdoc.clinic.CurrentProfile;
import com.aferdoc.clinic.Message;
import com.aferdoc.clinic.R;
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

public class ApplicationActivity extends AppCompatActivity {

    Spinner countries_spinner,codes_spinner;
    TextView terms_and_conditions_tv,first_name_edit_text,last_name_edit_text,speciality_edit_text,workplace_edit_text,phone_edit_text,
    district_edit_text,consultation_fee_edit_text,availability_edit_text,qualification_edit_text,experience_edit_text;
    Button apply_btn,instructions_btn;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        countries_spinner = findViewById(R.id.countries_spinner);
        codes_spinner = findViewById(R.id.codes_spinner);
        terms_and_conditions_tv = findViewById(R.id.terms_and_conditions_tv);
        first_name_edit_text = findViewById(R.id.first_name_edit_text);
        last_name_edit_text = findViewById(R.id.last_name_edit_text);
        speciality_edit_text = findViewById(R.id.speciality_edit_text);
        workplace_edit_text = findViewById(R.id.workplace_edit_text);
        phone_edit_text = findViewById(R.id.phone_edit_text);
        district_edit_text = findViewById(R.id.district_edit_text);
        consultation_fee_edit_text = findViewById(R.id.consultation_fee_edit_text);
        availability_edit_text = findViewById(R.id.availability_edit_text);
        qualification_edit_text = findViewById(R.id.qualification_edit_text);
        experience_edit_text = findViewById(R.id.experience_edit_text);
        instructions_btn = findViewById(R.id.instructions_btn);
        apply_btn = findViewById(R.id.apply_btn);
        dialog = new ProgressDialog(this);
        terms_and_conditions_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplicationActivity.this,TermsAndConditionsActivity.class);
                ApplicationActivity.this.startActivity(intent);
            }
        });

        instructions_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ApplicationActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.instructions_layout);
                dialog.setCancelable(true);
                dialog.show();
            }
        });
        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitApplication();
            }
        });
        setUpSpinners();


    }

    void setUpSpinners(){
        ArrayAdapter<CharSequence> codesAdapter = ArrayAdapter
                .createFromResource(this, R.array.codes,
                        android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        codesAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        codes_spinner.setAdapter(codesAdapter);

        ArrayAdapter<CharSequence> countriesAdapter = ArrayAdapter
                .createFromResource(this, R.array.countries,
                        android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        countriesAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countries_spinner.setAdapter(countriesAdapter);
    }



    public Boolean submitApplication() {
        if (first_name_edit_text.getText().length()<3){
            Log.d("HONE",first_name_edit_text.getText().toString());
            first_name_edit_text.requestFocus();
            return false;
        }
        else if (last_name_edit_text.getText().length()<3){
            Log.d("HONE",last_name_edit_text.getText().toString());
            last_name_edit_text.requestFocus();
            return false;
        }
        else if(speciality_edit_text.getText().length()==0){
            Log.d("HONE",speciality_edit_text.getText().toString());
            speciality_edit_text.requestFocus();
            return false;
        }
        else if (workplace_edit_text.getText().length() ==0){
            Log.d("HONE",workplace_edit_text.getText().toString());
            workplace_edit_text.requestFocus();
            return false;
        }
        else if (phone_edit_text.getText().length()<9){
            Log.d("HONE",phone_edit_text.getText().toString());
            phone_edit_text.requestFocus();
            return false;
        }
        else if (!(phone_edit_text.getText().toString().startsWith("07") || phone_edit_text.getText().toString().startsWith("7")) ){
            Log.d("HONE",phone_edit_text.getText().toString());
            phone_edit_text.requestFocus();
            return false;
        }
        else if(district_edit_text.getText().length()==0){
            Log.d("HONE",district_edit_text.getText().toString());
            district_edit_text.requestFocus();
            return false;
        }
        else if (consultation_fee_edit_text.getText().length()==0){
            Log.d("HONE",consultation_fee_edit_text.getText().toString());
            consultation_fee_edit_text.requestFocus();
            return false;
        }
        else if( availability_edit_text.getText().length()==0){
            availability_edit_text.requestFocus();
            return false;
        }
        else if ( qualification_edit_text.getText().length()==0){
            qualification_edit_text.requestFocus();
            return false;
        }
        else if( experience_edit_text.getText().length()==0){
            experience_edit_text.requestFocus();
            return false;
        }

        if(phone_edit_text.getText().toString().startsWith("07")){
            phone_edit_text.setText(phone_edit_text.getText().subSequence(1,phone_edit_text.getText().length()));
        }

        String phone_number = codes_spinner.getSelectedItem().toString().subSequence(1,codes_spinner.getSelectedItem().toString().length())
                +phone_edit_text.getText().toString();
        Log.d("PHONE",phone_number);

        final Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("phone_number",phone_number);
        jsonParams.put("action", "verify_phone_number");
        dialog.setTitle("Please wait ....");
        dialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Server.CONTROLLER_PATH, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                try {
                    Log.d("RESULT",response.toString());

                    JSONObject result = response.getJSONObject("result");
                    if (result.getInt("verified")==1){
                        requestCode(result.getString("code"));
                    }
                    else {
                        phone_edit_text.requestFocus();
                        Log.d("Phone number",jsonParams.get("phone_number")+" is FAKE");
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

        return true;
    }

    //TO DO 
    public void requestCode(final String recv_code){
        Log.d("CODE",recv_code);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter the code you have received");
        final String[] code = new String[1];

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setCancelable(false);
// Set up the buttons
        builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                code[0] = input.getText().toString();
                int received_code = Integer.parseInt(code[0]);
                Log.d("CODE ENTERED",received_code+"");
                received_code = (received_code%999)*(received_code-1232);
                if(recv_code.toString().equals(received_code+"")){
                    JSONObject new_application = new JSONObject();
                    try {
                        new_application.put("first_name",first_name_edit_text.getText());
                        new_application.put("last_name",last_name_edit_text.getText());
                        new_application.put("speciality",speciality_edit_text.getText());
                        new_application.put("workplace",workplace_edit_text.getText());
                        new_application.put("country",countries_spinner.getSelectedItem().toString());
                        new_application.put("country_code",codes_spinner.getSelectedItem().toString());
                        new_application.put("phone_number",phone_edit_text.getText());
                        new_application.put("district",district_edit_text.getText());
                        new_application.put("consultation_fee",consultation_fee_edit_text.getText());
                        new_application.put("availability",availability_edit_text.getText());
                        new_application.put("qualification",qualification_edit_text.getText());
                        new_application.put("experience",experience_edit_text.getText());
                        JSONObject jsonParams = new JSONObject();
                        jsonParams.put("details", new_application);
                        jsonParams.put("action", "sign_up");

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Server.CONTROLLER_PATH, jsonParams, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject sign_up_response = response.getJSONObject("sign_up_response");
                                    if(sign_up_response.getInt("result")==1){
                                        //sign up is successful
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(ApplicationActivity.this);
                                        builder.setTitle("Success");
                                        final TextView textView = new TextView(ApplicationActivity.this);
                                        textView.setText(sign_up_response.getString("message"));
                                        builder.setView(textView);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                        ApplicationActivity.this.finish();
                                            }
                                        });
                                        builder.setCancelable(false);
                                        builder.show();
                                    }
                                    else {
                                    }

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
                        MyVolleySingleton.getInstance(ApplicationActivity.this).addToRequestQueue(jsonObjectRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }
                else {
                    Log.d("CODE",recv_code+" - "+received_code);
                    Toast.makeText(ApplicationActivity.this,"Incorrect code, apply again to receive another code",Toast.LENGTH_LONG).show();
                }



            }
        });
       

        builder.show();
    }
}
