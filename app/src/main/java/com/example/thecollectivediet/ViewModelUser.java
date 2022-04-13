package com.example.thecollectivediet;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.thecollectivediet.API_Utilities.FoodLog_API_Controller;
import com.example.thecollectivediet.API_Utilities.User_API_Controller;
import com.example.thecollectivediet.API_Utilities.VolleyResponseListener;
import com.example.thecollectivediet.JSON_Marshall_Objects.FoodLogItemView;
import com.example.thecollectivediet.JSON_Marshall_Objects.User;
import com.example.thecollectivediet.Me_Fragment_Components.Food_Logging_Editing.Converter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.jjoe64.graphview.series.DataPoint;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ViewModelUser extends AndroidViewModel {

    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInAccount googleSignInAccount;
    private Application application;

    MutableLiveData<HashMap<String,List<FoodLogItemView>>> list = new MutableLiveData<>();

    private final MutableLiveData<User> userData = new MutableLiveData<User>();
    private final MutableLiveData<DataPoint[]> weights = new MutableLiveData<>();
    private final MutableLiveData<Float> calorieForToday = new MutableLiveData<Float>();

    private User user; //This user will have its data passed around the app via userData
    private String chosenDate;
    private int updateFlag;

    tcdandroid tcd;

    private Context ctx;

    public ViewModelUser(@NonNull Application application) {
        super(application);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(application));

        this.application = application;


        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(application, googleSignInOptions);
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(application);

        user = new User();
        this.chosenDate = getTodayString();
        updateFlag = 0;

    }


    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(application) != null && !GoogleSignIn.getLastSignedInAccount(application).isExpired();
    }

    public void getWeighIns(){

        User_API_Controller.getWeighIns(ctx, user, new VolleyResponseListener<DataPoint[]>() {
            @Override
            public void onResponse(DataPoint[] response) {
                weights.setValue(response);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public MutableLiveData<DataPoint[]> getWeights(){
        return weights;
    }
    /**
     * The MutableLiveData of the user will be passed to other classes/fragments
     * @return userData
     */
    public MutableLiveData<User> getUserData(){
        return userData;
    }

    /**
     * Returns the user object of ViewModelUser. Likely to be used with
     * static API calls.
     * @return
     */
    public User getUser(){
        return user;
    }

    /**
     * Sets the user in ViewModelUser.
     *
     */
    public void setUser(User user){
        this.user = user;
        userData.setValue(this.user);
    }

    /**
     * Pull user data from database
     * @param mainActivity
     */
    public void pullUserData(MainActivity mainActivity){
        User_API_Controller.handleNewSignIn(googleSignInAccount, mainActivity, new VolleyResponseListener<User>() {
            @Override
            public void onResponse(User user) {
                setUser(user);
                getWeighIns();
                pullMealsData(mainActivity, user);
               // commitFragmentTransaction(MainActivity.this, R.id.fragmentHolder, new MeTabLayoutFragment());
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ctx, error, Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void pullMealsData(MainActivity mainActivity, User user){
        FoodLog_API_Controller.getFoodLogEntries(mainActivity, user, chosenDate, new VolleyResponseListener<HashMap<String, List<FoodLogItemView>>>() {
            @Override
            public void onResponse(@NonNull HashMap<String, List<FoodLogItemView>> response) {

                // populateRecyclerItems(response);// erase later after viewmodel implementation
                setList(response);
            }

            @Override
            public void onError(String error) {
                Log.d("error", error);
                Toast.makeText(mainActivity, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public GoogleSignInAccount getAccount() {
        return googleSignInAccount;
    }

    public void updateUserProfile(User currentUser, Context context) {
        User_API_Controller.updateUserProfile(currentUser,context);
        setUser(currentUser);
    }

    public void signOut(){
        this.user = null;
        userData.setValue(null);
    }

    /**
     * The MutableLiveData of the user will be passed to other classes/fragments
     * @return userData
     */
    public MutableLiveData<HashMap<String, List<FoodLogItemView>>> getList(){
        return list;
    }

    /**
     * set the MutableLiveData to be passed around
     * @param list
     */
    public void setList(HashMap<String,List<FoodLogItemView>> list){

        int cals = 0;
        this.list.setValue(list);

        for(HashMap.Entry<String, List<FoodLogItemView>> entry : list.entrySet()){

            for(FoodLogItemView foodLogItemView : entry.getValue()){
                //cals += foodLogItemView.getEnergy_kcal_100g();
                cals += Converter.calculateCalories(foodLogItemView.getEnergy_kcal_100g(), foodLogItemView.getPortion_unit(), foodLogItemView.getPortion_size());
            }

        }

        setCalories(cals);
    }

    /**
     * Get today's date in yyyy-MM-dd HH:mm:ss format
     * @return sdf.format(dts)
     */
    @NonNull
    public String getTodayString() {
        java.util.Date dts = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(dts);
    }

    public void setDate(String date){
        this.chosenDate = date;
    }

    public String getDate(){
        return chosenDate;
    }

    public void setCalories(float cals){
        this.calorieForToday.setValue(cals);
    }

    public MutableLiveData<Float> getCals(){
        return calorieForToday;
    }

    public int getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(int updateFlag) {
        this.updateFlag = updateFlag;
    }
}
