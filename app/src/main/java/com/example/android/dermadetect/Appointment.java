package com.example.android.dermadetect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Appointment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        TextView name = (TextView)findViewById(R.id.firstname);
        final String firstname = name.getText().toString();

        TextView email = (TextView)findViewById(R.id.email);
        final String emailid = email.getText().toString();

        Button request = (Button)findViewById(R.id.request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, ""+firstname);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Appointment Letter");
                intent.putExtra(Intent.EXTRA_TEXT, "hi \n" +"I want to book an appointment");
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });
    }
}