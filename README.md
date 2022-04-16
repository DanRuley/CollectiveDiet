# tcd-android

SDK - Android SDK
IDE - Android Studio

This app was written in Java using Android SDK and Android Studio. This app will only work in the Android Studio emulator and in Android devices 
with a minimum SKD of 21. 

**MainActivity**

***Walk through***
Upon starting the app, a view model will be created to keep track of user information and goals. The user can create/change their information, add food items to their daily diary, and create goals for weight and calories. The MainActivity will send the user to the "Today" fragment when first starting up.

***Navigation***
Navigation between fragments can be done by the bottom app bar or the drawer that can be opened by hitting the triple line icon found near the top
left. Two different sections can be found in the MainActivity class that handle the click listeners. The MainActivity will be in charge of allowing fragment switching (app navigation).

**CameraFragment**

***CameraX***
CameraX is used to handle the camera functions for the app. At this time, pictures taken are not saved so when the user takes a picture,
OnCaptureSuccess is used to get the ImageProxy. The ImageProxy is sent to the image classifier to predict the food and that result is stored
in SharedPreferences. Then the app switches to the ManualFoodFragment which checks to see if a prediction exists in SharedPreferences.

***Food Search***
Users can add food items manually by searching for them using text.

***Social***
Users can create posts with comments and images to be viewed by all in the Social view. 