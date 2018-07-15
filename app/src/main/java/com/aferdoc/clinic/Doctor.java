package com.aferdoc.clinic;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by robert on 2/25/18.
 */

public class Doctor implements Serializable{

    String user_id;
    private String name ="Doctor";
    private String workPlace;
    private String accountNumber;
    private String consultationFee;
    private String experience;
    private String availabilty;

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    private String speciality;
    private String profileImagePath;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    private String balance;

    public Doctor(HashMap user){
        setUserId(user.get("user_id").toString());
        setName(user.get("name").toString());
        setAccountNumber(user.get("account_number").toString());
        setWorkPlace(user.get("work_place").toString());
        setAvailabilty(user.get("availability").toString());
        setExperience(user.get("experience").toString());
        setProfileImagePath(user.get("image_path").toString());
        setBalance(user.get("balance").toString());
    }

    public Doctor(JSONObject user) throws JSONException {
        setUserId(user.getString("user_id"));
        setName(user.getString("name"));
        setAccountNumber(user.getString("account_number"));
        setWorkPlace(user.getString("work_place"));
        setAvailabilty(user.getString("availability"));
        setExperience(user.getString("experience"));
        setConsultationFee(user.getString("consultation_fee"));
        setProfileImagePath(user.getString("image_path"));
        setBalance(user.getString("balance"));
        setSpeciality(user.getString("specialty"));
    }
    public void changeProfile(JSONObject user) throws JSONException {
        setUserId(user.getString("user_id"));
        setName(user.getString("name"));
        setAccountNumber(user.getString("account_number"));
        setWorkPlace(user.getString("work_place"));
        setAvailabilty(user.getString("availability"));
        setExperience(user.getString("experience"));
        setConsultationFee(user.getString("consultation_fee"));
        setProfileImagePath(user.getString("image_path"));
        setBalance(user.getString("balance"));
        setSpeciality(user.getString("specialty"));

    }
    public void setUserId(String  userId) {
        this.user_id = userId;
    }
    public String getUserId() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getConsultationFee() {
        return consultationFee;
    }

    public String getExperience() {
        return experience;
    }

    public String getAvailabilty() {
        return availabilty;
    }

    public void setAvailabilty(String availabilty) {
        this.availabilty = availabilty;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setConsultationFee(String consultationFee) {
        this.consultationFee = consultationFee;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }
}
