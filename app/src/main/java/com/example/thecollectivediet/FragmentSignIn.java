package com.example.thecollectivediet;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.thecollectivediet.API_Utilities.User_API_Controller;
import com.example.thecollectivediet.API_Utilities.VolleyResponseListener;
import com.example.thecollectivediet.JSON_Marshall_Objects.User;
import com.example.thecollectivediet.Me_Fragment_Components.MeTabLayoutFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

public class FragmentSignIn extends Fragment implements View.OnClickListener {

    private ActivityResultLauncher<Intent> signInActivityResultLauncher;
    private GoogleSignInClient mGoogleSignInClient;

    //buttons
    SignInButton signInButton;

    //views
    View v;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_sign_in, container, false);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        //hook elements
        signInButton = v.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        signInActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(@NonNull ActivityResult result) {

                        Task<GoogleSignInAccount> task1 = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            handleSignInResult(task);
                        }
                    }

                });
        return v;
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.sign_in_button: {

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();

                signInActivityResultLauncher.launch(signInIntent);
                break;
            }
        }
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> task) {
        String msg = "Hello, " + task.getResult().getDisplayName();
        GoogleSignInAccount account = task.getResult();

        User_API_Controller.handleNewSignIn(account, getActivity(), new VolleyResponseListener<User>() {
            @Override
            public void onResponse(User user) {
               // MainActivity.setCurrentUser(user);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });

        String name = account.getDisplayName();
        String email = account.getEmail();
        String id = account.getId();

        SharedPreferences prefs;
        SharedPreferences.Editor editor;

        //Any class in this app can use this
        prefs = getActivity().getSharedPreferences("TheCollectiveDiet", Context.MODE_PRIVATE);

        editor = prefs.edit();

        editor.putString("user", name);
        editor.putString("id", id);
        editor.putString("user_email", email);

        editor.commit();

        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        getActivity().setResult(RESULT_OK);

        TextView login1 = getActivity().findViewById(R.id.toolbar_login);
        String username = prefs.getString("user", "null");
        login1.setText(username);

        MainActivity.commitFragmentTransaction(getActivity(), R.id.fragmentHolder, new MeTabLayoutFragment());
    }
}