package com.example.thecollectivediet;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.navigation.NavigationView;


import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       /* //camera button
        Button liveCamera = findViewById(R.id.take_pic);

        liveCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainActivity.this, LiveCam.class));
            }
        });*/



        //Bottom navigation tool bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_toolbar);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                int id = item.getItemId();
                //Create a transaction
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if(id == R.id.bottom_nav_food){
                    //Create a new fragment of the appropriate type
                    FoodVisionFragment fragment = new FoodVisionFragment();
                    transaction.replace(R.id.fragmentHolder, fragment);
                }

                if(id == R.id.bottom_nav_profile){
                    ProfileFragment fragment = new ProfileFragment();
                    transaction.replace(R.id.fragmentHolder, fragment);
                }

                if(id == R.id.bottom_nav_us){
                    UsFragment fragment = new UsFragment();
                    transaction.replace(R.id.fragmentHolder, fragment);
                }

                if(id == R.id.bottom_nav_me){
                    MeFragment fragment = new MeFragment();
                    transaction.replace(R.id.fragmentHolder, fragment);
                }

                //Ask Android to remember which menu options the user has chosen
                transaction.addToBackStack(null);

                //Implement the change
                transaction.commit();
                return true;
            }
        });


        //For now, the app will open straight to the food fragment. When this
        //temp code is erased, the app will open to content_main.xml
        //////////////////////////////////////////////////////////////temp code
        //Create a transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                 FoodVisionFragment fragment = new FoodVisionFragment();
            transaction.replace(R.id.fragmentHolder, fragment);

        //Ask Android to remember which menu options the user has chosen
        transaction.addToBackStack(null);

        //Implement the change
        transaction.commit();

         drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        ///////////////////////////////////////////////////////////////end temp code
    }

    @Override
    public void onBackPressed(){
        //If the drawer is open, close it
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        //else, if the drawer is closed, rely on super class default behavior
        else{
            super.onBackPressed();
        }
    }

    //Handle navigation view item clicks here
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        //Create a transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int id = item.getItemId();

        //Create a new fragment of the appropriate type depending
        //on click item
        if(id == R.id.nav_food){
            //Create a new fragment of the appropriate type
            FoodVisionFragment fragment = new FoodVisionFragment();
            transaction.replace(R.id.fragmentHolder, fragment);
        }

        if(id == R.id.nav_profile){
            ProfileFragment fragment = new ProfileFragment();
            transaction.replace(R.id.fragmentHolder, fragment);
        }

        if(id == R.id.nav_us){
            UsFragment fragment = new UsFragment();
            transaction.replace(R.id.fragmentHolder, fragment);
        }

        if(id == R.id.nav_me){
            MeFragment fragment = new MeFragment();
            transaction.replace(R.id.fragmentHolder, fragment);
        }

        //Ask Android to remember which menu options the user has chosen
        transaction.addToBackStack(null);

        //Implement the change
        transaction.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


}
