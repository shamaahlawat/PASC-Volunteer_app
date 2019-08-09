package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.fragments.HomeFragment;
import com.example.myapplication.fragments.ProfileFragment;
import com.example.myapplication.fragments.UsersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ActionBar actionBar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        firebaseAuth = FirebaseAuth.getInstance();
        actionBar = getSupportActionBar();
        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);
        navigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);




        //defalut
        actionBar.setTitle("Home");
        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content, fragment1, "");
        ft1.commit();


    }

    BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            //handle item clicks
            switch (menuItem.getItemId()){
                case R.id.nav_home:{
                    actionBar.setTitle("Home");
                    HomeFragment fragment1 = new HomeFragment();
                    FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                    ft1.replace(R.id.content, fragment1, "fragment_profile");
                    ft1.commit();
                    return true;
                }
                case R.id.nav_profile:{
                    actionBar.setTitle("Profile");
                    ProfileFragment fragment2 = new ProfileFragment();
                    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                    ft2.replace(R.id.content, fragment2, "");
                    ft2.commit();
                    return true;
                }
                case R.id.nav_users:{
                    actionBar.setTitle("Explore");
                    UsersFragment fragment3 = new UsersFragment();
                    FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                    ft3.replace(R.id.content, fragment3, "");
                    ft3.commit();
                    return true;
                }

            }

            return false;
        }
    };



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
