package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    Button registerButton;
    TextView loginLink;
    EditText userEmail, userPassword;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        registerButton = (Button)findViewById(R.id.SigninButton);
        userEmail = (EditText)findViewById(R.id.userEmail);
        userPassword = (EditText)findViewById(R.id.userPassword);
        loginLink = (TextView) findViewById(R.id.loginLink);
        progressDialog = new ProgressDialog(this);


        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            //start prof
            finish();
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
        }

        onRegisterButtonClicked();
        onLoginLinkClicked();



    }


    public void onRegisterButtonClicked(){

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = userEmail.getText().toString().trim();
                final String pwd = userPassword.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())                {
                    userEmail.setError("Invalid email address");
                    userEmail.setFocusable(true);
                    return;
                    //no further execution
                }
                if(pwd.length() < 8){
                    userEmail.setError("Password length at least 8 charachters");
                    userEmail.setFocusable(true);
                    return;
                    //no further execution
                }

                //if valid
                progressDialog.setMessage("Registering user... ");

                progressDialog.show();

                //create user entry in firebase

                firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Registration successful...", Toast.LENGTH_SHORT).show();
                                    firebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            progressDialog.dismiss();
                                            if (task.isSuccessful()) {
                                                //start prof activity
                                                finish();
                                                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                                            }
                                            else{
                                                Toast.makeText(MainActivity.this, "Login in to your account", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(MainActivity.this, "Registration unsuccessful...", Toast.LENGTH_SHORT).show();
                                }
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
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }

}
