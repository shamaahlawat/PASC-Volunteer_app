package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    GoogleSignInClient mGoogleSignInClient;


    ProgressDialog progressDialog;
    Button signInButton;
    SignInButton googleSignIn;
    TextView registerLink, forgotPasswordLink;
    EditText userEmail, userPassword;

    FirebaseAuth firebaseAuth;
    private final String TAG= "LoginAcitivity: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        signInButton = (Button)findViewById(R.id.SigninButton);
        googleSignIn = (SignInButton)findViewById(R.id.googleSignIn);
        userEmail = (EditText)findViewById(R.id.LoginEmail);
        userPassword = (EditText)findViewById(R.id.LoginPassword);
        registerLink = (TextView) findViewById(R.id.SignUpLink);
        forgotPasswordLink = (TextView) findViewById(R.id.ForgotPasswordLink);
        progressDialog = new ProgressDialog(this);



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

        firebaseAuth = FirebaseAuth.getInstance();

        onRegisterButtonClicked();
        onSignupLinkClicked();
        onForgotPasswordLinkClicked();
        onGoogleSignInClicked();

        if(firebaseAuth.getCurrentUser() != null){
            //start prof
            finish();
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
        }
    }

    public void onRegisterButtonClicked(){

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = userEmail.getText().toString().trim();
                String pwd = userPassword.getText().toString().trim();


                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())                {
                    userEmail.setError("Invalid email address");
                    userEmail.setFocusable(true);
                    return;
                    //no further execution
                }

                //if valid
                progressDialog.setMessage("logging in... ");
                progressDialog.show();

                //login user in firebase
                firebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                           //start prof activity
                            finish();
                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }


        });
    }

    public void onSignupLinkClicked(){

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(LoginActivity.this, EmailVerificationActivity.class));
            }
        });

    }

    public void onGoogleSignInClicked(){
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    public void onForgotPasswordLinkClicked(){

        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Password recovery");

                LinearLayout linLay = new LinearLayout(LoginActivity.this);

                final EditText emailId = new EditText(LoginActivity.this);
                emailId.setHint("Email");
                emailId.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                emailId.setMinEms(16);
                linLay.addView(emailId);
                linLay.setPadding(10,10,10,10);
                builder.setView(linLay);

                progressDialog.setMessage("Sending Recovery Email... ");
                progressDialog.show();

                builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //input email to recover password
                        String email = emailId.getText().toString().trim();

                        firebaseAuth.sendPasswordResetEmail(email).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Email sent.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();

            }
        });


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }


}
