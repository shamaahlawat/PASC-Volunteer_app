package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    Button registerButton;
    TextView loginLink,userEmail;
    EditText  userPassword, userPassword2;
    TextInputLayout t1,t2;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar =  getSupportActionBar();
        actionBar.hide();



        registerButton = (Button)findViewById(R.id.SigninButton);
        userEmail = (TextView)findViewById(R.id.userEmail);
        userPassword = (EditText)findViewById(R.id.userPassword);
        userPassword2 = (EditText)findViewById(R.id.userPassword2);
        loginLink = (TextView) findViewById(R.id.loginLink);
        progressDialog = new ProgressDialog(this);
        t1 = findViewById(R.id.pwdTil);
        t2 = findViewById(R.id.pwd2Til);



        Intent intent = getIntent();
        userEmail.setText("Email ID: " + intent.getStringExtra(EmailVerificationActivity.EXTRA_TEXT));

        onRegisterButtonClicked();
        onLoginLinkClicked();

    }


    public void onRegisterButtonClicked(){

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String pwd2 = userPassword2.getText().toString().trim();
                final String pwd = userPassword.getText().toString().trim();


                if(pwd.length() < 8){
                    t1.setError("Password length at least 8 charachters");
                    t1.setFocusable(true);
                    return;
                    //no further execution
                }

                if(!pwd.equals(pwd2)){
                    t2.setError("Password length at least 8 charachters");
                    t2.setFocusable(true);
                    return;
                    //no further execution
                }
                //if valid
                progressDialog.setMessage("Registering user... ");

                progressDialog.show();

                //create user entry in firebase

                firebaseAuth.createUserWithEmailAndPassword(userEmail.getText().toString().trim(), pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Registration successful...", Toast.LENGTH_SHORT).show();
                                    firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString().trim(),pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            progressDialog.dismiss();
                                            if (task.isSuccessful()) {
                                                //start prof activity
                                                finish();
                                                startActivity(new Intent(MainActivity.this, RegistrationFormActivity.class));
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
