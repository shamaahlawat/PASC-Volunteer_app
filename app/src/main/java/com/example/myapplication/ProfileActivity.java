package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    TextView userName;
    Button logoutButton, editProfButton;
    EditText userid, dept, year;

    // key fields for map
    // fields of user class
    private static final String NAME = "name";
    private static final String YEAR = "year";
    private static final String DEPT = "dept";


    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");


        userName = (TextView)findViewById(R.id.userName);
        logoutButton = (Button)findViewById(R.id.logoutButton);

        editProfButton = (Button)findViewById(R.id.editProfilebutton);
        userid = (EditText) findViewById(R.id.userId);
        dept = (EditText) findViewById(R.id.department);
        year = (EditText) findViewById(R.id.year);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userid.setEnabled(false);
        year.setEnabled(false);
        dept.setEnabled(false);
        year.setAllCaps(true);
        dept.setAllCaps(true);

        //checkUserStatus();
        onLogOutCLicked();
        onSetProfileButtonClicked();

        //if no user is logged in on this device, go to login activity

        //set welcome text
        String email = firebaseAuth.getCurrentUser().getEmail().toString();
        userName.setText("Welcome " + email);

    }


    public void onLogOutCLicked(){
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            }
        });
    }

    public void onSetProfileButtonClicked(){
        editProfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to make the profile editable, add enable and disable feature
                //to display in same activity
                if(!userid.isEnabled()){
                    editProfButton.setText("Done");
                    userid.setEnabled(true);
                    year.setEnabled(true);
                    dept.setEnabled(true);
                }else{
                    editProfButton.setText("Edit profile");
                    userid.setEnabled(false);
                    year.setEnabled(false);
                    dept.setEnabled(false);
                }

                //add values to the resp fields
                  Map<String, Object>map = new HashMap<>();
                  map.put(NAME, userid.getText().toString());
                  map.put(YEAR,year.getText().toString().toUpperCase());
                  map.put(DEPT,dept.getText().toString().toUpperCase());

                  // add entry to firestore
                db.collection("User").document(firebaseAuth.getCurrentUser().getEmail().toString())
                        .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }




    //inflate menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //handle option clicks

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id){
            case R.id.action_logout:{
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
