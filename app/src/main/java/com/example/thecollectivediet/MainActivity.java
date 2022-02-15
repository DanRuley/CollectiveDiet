package com.example.thecollectivediet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.thecollectivediet.Camera_Fragment_Components.CameraFragment;
import com.example.thecollectivediet.Intro.IntroActivity;
import com.example.thecollectivediet.Me_Fragment_Components.MeTabLayoutFragment;
import com.example.thecollectivediet.Profile_Fragment_Components.ProfileFragment;
import com.example.thecollectivediet.Us_Fragment_Components.UsFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityResultLauncher ARL;

    GoogleSignInClient mGoogleSignInClient;

    //elements
    Toolbar toolbar;
    DrawerLayout drawer;



    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = this.getSharedPreferences("TheCollectiveDiet", Context.MODE_PRIVATE);
        editor = prefs.edit();

        TextView login = findViewById(R.id.toolbar_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(!isSignedIn()){
                FragmentSignIn frag = new FragmentSignIn();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentHolder, frag);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            }
        });

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        TextView login1 = findViewById(R.id.toolbar_login);
                        String username = prefs.getString("user", "null");
                        login1.setText(username);

                    } else {
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail()
                                .build();
                         mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);
                        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);

                        if (account != null && !account.isExpired()) {
                            TextView login1 = findViewById(R.id.toolbar_login);
                            String username = prefs.getString("user", "null");
                            login1.setText(username);
                        }
                    }
                });

        //If this is user's first time on app
        String firstTime = prefs.getString("firstTime", "null");

        if(firstTime.equals("null")) {
            editor.putString("firstTime", "false");
            editor.commit();

            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
        }
//        else {
//            Intent intent = new Intent(this, IntroActivity.class);
//            someActivityResultLauncher.launch(intent);
//        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail()
                                .build();
                        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);
                        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);

                        if (account != null && !account.isExpired()) {

                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            MeTabLayoutFragment fragment = new MeTabLayoutFragment();
                            transaction.replace(R.id.fragmentHolder, fragment);

                            //Ask Android to remember which menu options the user has chosen
                            transaction.addToBackStack(null);

                            //Implement the change
                            transaction.commit();

                            TextView login1 = findViewById(R.id.toolbar_login);
                            String username = prefs.getString("user", "null");
                            login1.setText(username);
                            //todo
                            //get user metrics

                        }
                        else{
                            FragmentSignIn frag = new FragmentSignIn();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentHolder, frag);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }

        //Setup button, views, etc in the activity_main layout
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Bottom navigation tool bar on the bottom of the app screen will be used for
        //navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_toolbar);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener(){

            //When icon in bottom app is selected, switch to appropriate fragment
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                int id = item.getItemId();
                //Create a transaction
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if(id == R.id.bottom_nav_camera){
                    //Create a new fragment of the appropriate type
                    CameraFragment fragment = new CameraFragment();
                    transaction.replace(R.id.fragmentHolder, fragment);
                }

                if(id == R.id.bottom_nav_profile){

                    MeTabLayoutFragment frag = new MeTabLayoutFragment(2);
                    transaction.replace(R.id.fragmentHolder, frag);

                }

                if(id == R.id.bottom_nav_us){
                    UsFragment fragment = new UsFragment();
                    transaction.replace(R.id.fragmentHolder, fragment);
                }

                if(id == R.id.bottom_nav_me){
                    MeTabLayoutFragment fragment = new MeTabLayoutFragment();
                    transaction.replace(R.id.fragmentHolder, fragment);
                }

                //Ask Android to remember which menu options the user has chosen
                transaction.addToBackStack(null);

                //Implement the change
                transaction.commit();
                return true;
            }
        });


        //For now, the app will open straight to the food fragment.
        //This will be the first screen the user will see
        //Create a transaction
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                 MeTabLayoutFragment fragment = new MeTabLayoutFragment();
//            transaction.replace(R.id.fragmentHolder, fragment);
//
//        //Ask Android to remember which menu options the user has chosen
//        transaction.addToBackStack(null);
//
//        //Implement the change
//        transaction.commit();

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
            //super.onBackPressed();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            MeTabLayoutFragment fragment = new MeTabLayoutFragment();
            transaction.replace(R.id.fragmentHolder, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    //Navigation via the drawer
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
            CameraFragment fragment = new CameraFragment();
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
            MeTabLayoutFragment fragment = new MeTabLayoutFragment();
            transaction.replace(R.id.fragmentHolder, fragment);
        }

        if(id == R.id.nav_sign_in){
            if(!isSignedIn()) {
                FragmentSignIn fragment = new FragmentSignIn();
                transaction.replace(R.id.fragmentHolder, fragment);
            }
        }

        if(id == R.id.nav_sign_out){
            signOut();
            FragmentSignIn frag = new FragmentSignIn();
            transaction.replace(R.id.fragmentHolder, frag);

            TextView login = findViewById(R.id.toolbar_login);
            login.setText("sign in");
        }

        //Ask Android to remember which menu options the user has chosen
        transaction.addToBackStack(null);

        //Implement the change
        transaction.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        hideKeyboard(this);
        return super.getSupportFragmentManager();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

    private void signOut(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


            }
        });
    }
//    // Register the permissions callback, which handles the user's response to the
//// system permissions dialog. Save the return value, an instance of
//// ActivityResultLauncher, as an instance variable.
//    private ActivityResultLauncher requestPermissionLauncher =
//            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//                if (isGranted) {
//                    // Permission is granted. Continue the action or workflow in your
//                    // app.
//                } else {
//                    // Explain to the user that the feature is unavailable because the
//                    // features requires a permission that the user has denied. At the
//                    // same time, respect the user's decision. Don't link to system
//                    // settings in an effort to convince the user to change their
//                    // decision.
//                }
//            });
//
//
//    private ActivityResultLauncher registerForActivityResult(ActivityResultLauncher requestPermissionLauncher) {
//    }
}
