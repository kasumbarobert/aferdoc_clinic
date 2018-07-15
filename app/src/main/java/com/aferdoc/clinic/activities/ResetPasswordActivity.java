package com.aferdoc.clinic.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class ResetPasswordActivity extends AppCompatActivity {

    EditText aferdoc_id_edit_text, phone_number_edit_text, new_password_edit_text,confirm_passsword_edit_text;
    Button reset_button;
    TextView error_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        aferdoc_id_edit_text = findViewById(R.id.aferdoc_id_edit_text);
        phone_number_edit_text = findViewById(R.id.phone_number_edit_text);
        new_password_edit_text = findViewById(R.id.diagnosis_edit_text);
        confirm_passsword_edit_text = findViewById(R.id.confirm_passsword_edit_text);
        reset_button = findViewById(R.id.send_prescription_btn);
        error_tv = findViewById(R.id.error_tv);
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_tv.setVisibility(View.INVISIBLE);
                submitPasswordChangeRequest();
            }
        });

    }



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
                if (code[0].length()==0){
                    code[0]="0";
                }
                int received_code = Integer.parseInt(code[0]);
                received_code = (received_code%999)*(received_code-1232);
                if(recv_code.toString().equals(received_code+"")){

                    try {
                        JSONObject jsonParams = new JSONObject();
                        jsonParams.put("aferdoc_id",aferdoc_id_edit_text.getText());
                        jsonParams.put("new_password",confirm_passsword_edit_text.getText());
                        jsonParams.put("phone_number",phone_number_edit_text.getText().toString().substring(1,phone_number_edit_text.getText().toString().length()));
                        jsonParams.put("action", "reset_password");
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Server.CONTROLLER_PATH, jsonParams, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject password_change_response = response.getJSONObject("result");
                                    if(password_change_response.getInt("success")==1){
                                        //sign up is successful
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                        builder.setTitle("Success");
                                        final TextView textView = new TextView(ResetPasswordActivity.this);
                                        textView.setText("Password change successfully. Login to access your account");
                                        builder.setView(textView);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ResetPasswordActivity.this.finish();
                                            }
                                        });
                                        builder.setCancelable(false);
                                        builder.show();
                                    }
                                    else {
                                        error_tv.setText("Password Change has failed.");
                                        error_tv.setVisibility(View.VISIBLE);
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
                        MyVolleySingleton.getInstance(ResetPasswordActivity.this).addToRequestQueue(jsonObjectRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }
                else {
                    error_tv.setText("Incorrect request code! Try again, a new code will be sent to you");
                    error_tv.setVisibility(View.VISIBLE);
                }



            }
        });


        builder.show();
    }

    public Boolean submitPasswordChangeRequest() {
        if (aferdoc_id_edit_text.getText().length()==0){
            aferdoc_id_edit_text.requestFocus();
            return false;
        }

        else if (phone_number_edit_text.getText().length()<9){
            phone_number_edit_text.requestFocus();
            return false;
        }
        else if(new_password_edit_text.getText().length()==0){
            new_password_edit_text.requestFocus();
            return false;
        }
        else if (confirm_passsword_edit_text.getText().length()==0){
            confirm_passsword_edit_text.requestFocus();
            return false;
        }

        if (!confirm_passsword_edit_text.getText().toString().equals(new_password_edit_text.getText().toString())){
            confirm_passsword_edit_text.requestFocus();
            return false;
        }

        if(phone_number_edit_text.getText().toString().startsWith("07")){
            phone_number_edit_text.setText("+256"+phone_number_edit_text.getText().toString().substring(1,phone_number_edit_text.getText().toString().length()));
        }
        else if(phone_number_edit_text.getText().toString().startsWith("7")) {
            phone_number_edit_text.setText("+256"+phone_number_edit_text.getText().toString());
        }
            final Map<String, String> jsonParams = new HashMap<String, String>();
            jsonParams.put("phone_number",phone_number_edit_text.getText().toString().substring(1,phone_number_edit_text.getText().toString().length()));
            jsonParams.put("action", "verify_phone_number_pwd_change");
            jsonParams.put("aferdoc_id",aferdoc_id_edit_text.getText().toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Server.CONTROLLER_PATH, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d("RESULT",response.toString());

                        JSONObject result = response.getJSONObject("result");
                        if (result.getInt("verified")==1){
                            requestCode(result.getString("code"));
                        }
                        else {
                            phone_number_edit_text.requestFocus();
                            Log.d("MAP",jsonParams.toString());
                            error_tv.setText("Aferdoc ID or Phone number couldnt be verified");
                            error_tv.setVisibility(View.VISIBLE);
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

            MyVolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);





        return true;
    }

}
