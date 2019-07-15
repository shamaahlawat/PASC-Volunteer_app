package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class adminActivity extends AppCompatActivity {

    Button searchButton, showButton;
    EditText seachBox;

    private static final String NAME = "name";
    private static final String YEAR = "year";
    private static final String DEPT = "dept";


    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    DocumentReference docRef ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        seachBox = (EditText)findViewById(R.id.searchBox);
        searchButton = (Button)findViewById(R.id.searchButton);
        showButton = (Button)findViewById(R.id.showButton);


        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //this will give info about current user
        //find code to get info about all / any user
        docRef = db.collection("User").document(firebaseAuth.getCurrentUser().getEmail());


        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(adminActivity.this, LoginActivity.class));
        }
    }

    public void onShowUsersClicked(){
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get entry from firestore
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String name = documentSnapshot.getString(NAME);
                            String year = documentSnapshot.getString(YEAR);
                            String dept = documentSnapshot.getString(DEPT);

                            //show the values in  textview or any suitable format

                        }else{
                            //error message
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //error message
                    }
                });


            }
        });

    }

    public void onSearchUserClicked(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


}

