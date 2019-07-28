package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText post_title;
    private EditText post_description;
    private Button save_post;

    String Title,Description,Date,Time;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        post_title = findViewById(R.id.nameET);
        post_description = findViewById(R.id.DescET);
        save_post = findViewById(R.id.Save);

        Button post_date = findViewById(R.id.DatePicker);
        post_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new com.example.myapplication.DatePicker();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        Button post_time = findViewById(R.id.TimePicker);
        post_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePicker();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });

        db = FirebaseFirestore.getInstance();

        onSavePostClicked();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        Date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);
        Time = DateFormat.getTimeInstance().format(c.getTime());
    }

    public void onSavePostClicked(){
        save_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Date = post_date.getText().toString().trim();
                Title = post_title.getText().toString().trim();
                Description = post_description.getText().toString().trim();

                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                //add values to the resp fields
                Map<String, Object> map = new HashMap<>();
                map.put("Title", Title);
                map.put("Description",Description);
                map.put("Date",Date);
                map.put("Time",Time);
                map.put("Timestamp", FieldValue.serverTimestamp());
                map.put("userId",email);

                // add entry to firestore
                db.collection("Post").document()
                        .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddPostActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPostActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
                finish();
                startActivity(new Intent(AddPostActivity.this, DashboardActivity.class));
            }
        });

    }


}
