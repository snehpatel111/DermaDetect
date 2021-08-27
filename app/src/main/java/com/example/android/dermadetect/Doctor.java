package com.example.android.dermadetect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Doctor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        ArrayList<Drinfo> drinfo = new ArrayList<>();

        drinfo.add(new Drinfo("Dr Ramji Gupta ","MD", R.drawable.dr_ramaji,"https://www.vaidam.com/hospitals/blk-hospital-new-delhi","9910555533"));
        drinfo.add(new Drinfo("Dr. Gopi Krishna Maddali","MD",R.drawable.dr_gopi,"https://www.vaidam.com/hospitals/blk-hospital-new-delhi","9803742732"));
        drinfo.add(new Drinfo("Dr. Ravichandran G ","MD", R.drawable.dr_ravichandran_g_dermatologist,"https://www.vaidam.com/hospitals/apollo-hospitals-greams-road-chennai","9910555533"));
        drinfo.add(new Drinfo("Dr. Anil K. Agarwal  ","MD", R.drawable.dr_anil_agarwal,"https://www.vaidam.com/hospitals/w-pratiksha-hospital-gurgaon","1244131091"));
        drinfo.add(new Drinfo("Dr. Anuj Pall","MD", R.drawable.dr_anuj_pall,"https://www.vaidam.com/hospitals/la-skinnovita-gurgaon","9871217300"));
        drinfo.add(new Drinfo("Dr S C Bharija  ","MD", R.drawable.dr_s_c_bharija,"https://www.vaidam.com/hospitals/moolchand-hospital-new-delhi","99589 97292 "));





        RecyclerView recycleview = (RecyclerView) findViewById(R.id.recycleview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycleview.setLayoutManager(layoutManager);
        recycleview.setItemAnimator(new DefaultItemAnimator());

        DoctorAdapter itemview = new DoctorAdapter(this,drinfo);

        recycleview.setAdapter(itemview);
    }
}