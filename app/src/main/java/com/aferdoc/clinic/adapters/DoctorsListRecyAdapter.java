package com.aferdoc.clinic.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aferdoc.clinic.Doctor;
import com.aferdoc.clinic.R;
import com.aferdoc.clinic.activities.DoctorProfileActivity;
import com.aferdoc.clinic.activities.WebMessageChatActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 2/26/18.
 */

public class DoctorsListRecyAdapter  extends RecyclerView.Adapter<DoctorsListRecyAdapter.DoctorViewHolder> {
    private Context mContext;
    private List<Doctor> doctorList;
    public DoctorsListRecyAdapter(Context context, List<Doctor> messageList) {
        mContext = context;
        doctorList = messageList;
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public DoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.single_doctor_card, parent, false);
        return new DoctorViewHolder(view);
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(DoctorViewHolder holder, int position) {
        Doctor doctor =  doctorList.get(position);

        holder.doc_work_place_tv.setText(doctor.getWorkPlace());
        holder.doctor_name_tv.setText(doctor.getName());
        holder.experience_tv.setText(doctor.getExperience());
        holder.speciality_tv.setText(doctor.getSpeciality());

        Glide.with(mContext).load(doctor.getProfileImagePath()).into( holder.doctor_image).onLoadStarted(mContext.getResources().getDrawable(R.drawable.doctor_profile_icon));

    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public void changeDoctors(ArrayList<Doctor> doctor) {
        doctorList.clear();
        doctorList.addAll(doctor);
        notifyDataSetChanged();
    }
    public void addDoctor(Doctor doctor) {
        doctorList.add(doctor);
        notifyItemChanged(getItemCount());
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView doctor_name_tv, doc_work_place_tv, speciality_tv,experience_tv;
        ImageView doctor_image;
        DoctorViewHolder(View itemView) {
            super(itemView);
            doctor_name_tv =  itemView.findViewById(R.id.doctor_name_tv);
            doc_work_place_tv =  itemView.findViewById(R.id.doc_work_place_tv);
            speciality_tv =  itemView.findViewById(R.id.speciality_tv);
            experience_tv = itemView.findViewById(R.id.experience_tv);
            doctor_image =  itemView.findViewById(R.id.doctor_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext,DoctorProfileActivity.class);
            intent.putExtra("practitioner",doctorList.get(getAdapterPosition()));
            mContext.startActivity(intent);
        }
    }

}
