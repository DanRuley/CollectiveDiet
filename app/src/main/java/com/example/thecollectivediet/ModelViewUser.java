package com.example.thecollectivediet;

import static com.example.thecollectivediet.MainActivity.commitFragmentTransaction;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.thecollectivediet.API_Utilities.User_API_Controller;
import com.example.thecollectivediet.API_Utilities.VolleyResponseListener;
import com.example.thecollectivediet.JSON_Marshall_Objects.User;
import com.example.thecollectivediet.Me_Fragment_Components.MeTabLayoutFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class ModelViewUser extends AndroidViewModel {

    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInAccount googleSignInAccount;
    private Application application;

    private final MutableLiveData<User> userData = new MutableLiveData<User>();
    private User user; //This user will have its data passed around the app via userData

    private Context ctx;

    public ModelViewUser(@NonNull Application application) {
        super(application);

        this.application = application;

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(application, googleSignInOptions);
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(application);

        user = new User();
//        User_API_Controller.handleNewSignIn(googleSignInAccount, application, new VolleyResponseListener<User>() {
//            @Override
//            public void onResponse() {
//                MainActivity.commitFragmentTransaction(application, R.id.fragmentHolder, new TodayFragment());
//            }
//
//            @Override
//            public void onError(String error) {
//                Toast.makeText(application, error, Toast.LENGTH_SHORT).show();
//            }
//        });
        //user =
    }


    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(application) != null && !GoogleSignIn.getLastSignedInAccount(application).isExpired();
    }

    /**
     * The MutableLiveData of the user will be passed to other classes/fragments
     * @return userData
     */
    public MutableLiveData<User> getUserData(){
        return userData;
    }

    /**
     * Returns the user object of ModelViewUser. Likely to be used with
     * static API calls.
     * @return
     */
    public User getUser(){
        return user;
    }

    /**
     * Sets the user in ModelViewUser.
     *
     */
    public void setUser(User user){
        this.user = user;
        userData.setValue(this.user);

    }

    public void pullUserData(MainActivity mainActivity){
        User_API_Controller.handleNewSignIn(googleSignInAccount, mainActivity, new VolleyResponseListener<User>() {
            @Override
            public void onResponse(User user) {
                setUser(user);
               // commitFragmentTransaction(MainActivity.this, R.id.fragmentHolder, new MeTabLayoutFragment());
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ctx, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public GoogleSignInAccount getAccount() {
        return googleSignInAccount;
    }
}
