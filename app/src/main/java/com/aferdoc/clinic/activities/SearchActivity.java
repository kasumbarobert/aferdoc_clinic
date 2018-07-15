package com.aferdoc.clinic.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.aferdoc.clinic.CurrentProfile;
import com.aferdoc.clinic.Doctor;
import com.aferdoc.clinic.R;
import com.aferdoc.clinic.adapters.DoctorsListRecyAdapter;
import com.aferdoc.clinic.singletons.MyVolleySingleton;
import com.aferdoc.clinic.singletons.Server;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnSuggestionListener{
    SearchView searchView;
    RecyclerView doctors_recycler_view;
    ArrayList <Doctor> all_doctors = new ArrayList<>();
DoctorsListRecyAdapter doctorsListRecyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        doctors_recycler_view = findViewById(R.id.doctors_recycler_view);
        doctorsListRecyAdapter = new DoctorsListRecyAdapter(this,new ArrayList<Doctor>());
        doctors_recycler_view.setAdapter(doctorsListRecyAdapter);
        doctors_recycler_view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        loadDoctors();
    }



    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                SearchActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchView = (SearchView) myActionMenuItem.getActionView();
                        searchView.setMaxWidth(Integer.MAX_VALUE);
                        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                        searchView.setOnQueryTextListener(SearchActivity.this);
                        searchView.setIconified(false);

                    }
                });
            }
        };
        new Thread(runnable).start();
        return true;
    }
    public void loadDoctors(){
            Map<String, String> jsonParams = new HashMap<String, String>();
            jsonParams.put("doctor_id", CurrentProfile.getInstance().getCurrentDoctor().getUserId());
            jsonParams.put("action", "get_practioners");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Server.CONTROLLER_PATH, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray practioners = response.getJSONArray("practitioners");
                        for (int i=0; i<practioners.length(); i++){
                            all_doctors.add(new Doctor(practioners.getJSONObject(i)));
                        }
                        doctorsListRecyAdapter.changeDoctors(all_doctors);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ERROR PROFILE", error.toString());
                }
            }) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("User-agent", "My useragent");
                    return headers;
                }
            };

            MyVolleySingleton.getInstance(SearchActivity.this).addToRequestQueue(jsonObjectRequest);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
       ArrayList <Doctor> results = new ArrayList<>();
       query = query.toLowerCase();
       for(Doctor doctor: all_doctors){
           if (doctor.getName().toLowerCase().contains(query) || doctor.getSpeciality().toLowerCase().contains(query)
                   ||doctor.getWorkPlace().toLowerCase().contains(query) || doctor.getExperience().toLowerCase().contains(query))
           {
               results.add(doctor);
           }
       }

       doctorsListRecyAdapter.changeDoctors(results);
        doctors_recycler_view.scrollToPosition(0);
        return  true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList <Doctor> results = new ArrayList<>();

        for(Doctor doctor: all_doctors){
            if (doctor.getName().contains(newText) || doctor.getSpeciality().contains(newText)||doctor.getWorkPlace().contains(newText))
            {
                results.add(doctor);
            }
        }
        doctorsListRecyAdapter.changeDoctors(results);
        //doctors_recycler_view.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        return false;
    }
}
