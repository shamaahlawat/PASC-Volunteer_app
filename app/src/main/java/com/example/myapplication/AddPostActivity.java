package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    private EditText post_title;
    private EditText post_description;
    private EditText post_date;
    private Button save_post;

    String Title,Description,Date;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        post_title = findViewById(R.id.nameET);
        post_description = findViewById(R.id.DescET);
        post_date = findViewById(R.id.DateET);
        save_post = findViewById(R.id.Save);

        db = FirebaseFirestore.getInstance();

        onSavePostClicked();

    }

    public void onSavePostClicked(){
        save_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date = post_date.getText().toString().trim();
                Title = post_title.getText().toString().trim();
                Description = post_description.getText().toString().trim();

                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                //add values to the resp fields
                Map<String, Object> map = new HashMap<>();
                map.put("Title", Title);
                map.put("Description",Description);
                map.put("Date",Date);
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
