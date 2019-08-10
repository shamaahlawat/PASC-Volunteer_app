package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmailVerificationActivity extends AppCompatActivity {


    Button verifyButton;
    TextView loginLink;
    EditText userEmail;
    ProgressDialog progressDialog;

    public static final String EXTRA_TEXT = "EXTRA_TEXT";
    FirebaseAuth firebaseAuth;


    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        ActionBar actionBar =  getSupportActionBar();
        actionBar.hide();


        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            //start prof
            finish();
            startActivity(new Intent(EmailVerificationActivity.this, DashboardActivity.class));
        }


        verifyButton = (Button)findViewById(R.id.RegisterButton);
        userEmail = (EditText) findViewById(R.id.Email);
        loginLink = (TextView) findViewById(R.id.loginLink);
        progressDialog = new ProgressDialog(this);
        onRegisterButtonClicked();
        onLoginLinkClicked();



    }


    public void onRegisterButtonClicked() {

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = userEmail.getText().toString().trim();
                mRef = FirebaseDatabase.getInstance().getReference().child("AllUsers");


                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    userEmail.setError("Invalid email address");
                    userEmail.setFocusable(true);
                    return;
                    //no further execution
                }
                progressDialog.setMessage("Verifying user... ");
                progressDialog.show();

                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data : dataSnapshot.getChildren()){

                            if(data.child("EmailAddress").getValue().toString().equals(email)){
                                progressDialog.dismiss();
                                finish();
                                Intent intent = new Intent(EmailVerificationActivity.this, MainActivity.class);
                                intent.putExtra(EXTRA_TEXT, email);
                                startActivity(intent);
                            }
                            else{
                                progressDialog.dismiss();
                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EmailVerificationActivity.this);
                                mBuilder.setTitle("No user Found");
                                mBuilder.setMessage("Sorry! We could not find your email in our database. In Case of any problem, reach us at --email id / phn no--");
                                mBuilder.setCancelable(false);
                                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog a = mBuilder.create();
                                a.show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                            Toast.makeText(EmailVerificationActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void onLoginLinkClicked(){

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(EmailVerificationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
