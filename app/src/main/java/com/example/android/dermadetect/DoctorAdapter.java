package com.example.android.dermadetect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.dermadetect.R;

import java.util.ArrayList;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    ArrayList<Drinfo> drinfos;
    Context context;

    public DoctorAdapter(Context context,ArrayList<Drinfo> drinfos){
        this.drinfos=drinfos;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.imageView.setImageResource(drinfos.get(position).getimage());
        holder.textViewname.setText(drinfos.get(position).getname());
        holder.textViewdegree.setText(drinfos.get(position).getdegree());
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = drinfos.get(position).getnumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number) );
                context.startActivity(intent);
            }
        });

        holder.website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = drinfos.get(position).geturl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(number) );
                context.startActivity(intent);
            }
        });

        holder.appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Appointment.class);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return drinfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewdegree;
        TextView textViewname;
        Button call;
        Button website;
        Button appointment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView= itemView.findViewById(R.id.imagedr);
            textViewdegree= itemView.findViewById(R.id.drdegree);
            textViewname= itemView.findViewById(R.id.drname);
            call= itemView.findViewById(R.id.call);
            website= itemView.findViewById(R.id.website);
            appointment=itemView.findViewById(R.id.appointment);
        }
    }
}
