package com.ptit.ptittoeiclearning;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FacebookLoginActivity extends AppCompatActivity {


    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextView FacebookDataTextView;
    private TextView fb_usernameView;
    private TextView fb_emailView;
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";
    AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);

        FacebookSdk.sdkInitialize(FacebookLoginActivity.this);
        // Assign TextView ID.
        FacebookDataTextView = (TextView) findViewById(R.id.TextView1);
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE));

        // Checking the Access Token.
        if (AccessToken.getCurrentAccessToken() != null) {

            GraphLoginRequest(AccessToken.getCurrentAccessToken());

            // If already login in then show the Toast.
            Toast.makeText(FacebookLoginActivity.this, "Already logged in", Toast.LENGTH_SHORT).show();

        } else {

            // If not login in then show the Toast.
            Toast.makeText(FacebookLoginActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(FacebookLoginActivity.this, "Successful", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(FacebookLoginActivity.this, "Cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(FacebookLoginActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });

        // Detect user is login or not. If logout then clear the TextView and delete all the user info from TextView.
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {

                    // Clear the TextView after logout.
                    FacebookDataTextView.setText("");

                }
            }
        };
    }

    // Method to access Facebook User Data.
    protected void GraphLoginRequest(AccessToken accessToken) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                        try {

                            // Adding all user info one by one into TextView.
                            FacebookDataTextView.setText("ID: " + jsonObject.getString("id"));

                            FacebookDataTextView.setText(FacebookDataTextView.getText() + "\nName : " + jsonObject.getString("name"));

                            FacebookDataTextView.setText(FacebookDataTextView.getText() + "\nFirst name : " + jsonObject.getString("first_name"));

                            FacebookDataTextView.setText(FacebookDataTextView.getText() + "\nLast name : " + jsonObject.getString("last_name"));

                            FacebookDataTextView.setText(FacebookDataTextView.getText() + "\nEmail : " + jsonObject.getString("email"));

                            FacebookDataTextView.setText(FacebookDataTextView.getText() + "\nGender : " + jsonObject.getString("gender"));

                            FacebookDataTextView.setText(FacebookDataTextView.getText() + "\nLink : " + jsonObject.getString("link"));

                            FacebookDataTextView.setText(FacebookDataTextView.getText() + "\nTime zone : " + jsonObject.getString("timezone"));

                            FacebookDataTextView.setText(FacebookDataTextView.getText() + "\nLocale : " + jsonObject.getString("locale"));

                            FacebookDataTextView.setText(FacebookDataTextView.getText() + "\nUpdated time : " + jsonObject.getString("updated_time"));

                            FacebookDataTextView.setText(FacebookDataTextView.getText() + "\nVerified : " + jsonObject.getString("verified"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle bundle = new Bundle();
        bundle.putString(
                "fields",
                "id,name,link,email,gender,last_name,first_name,locale,timezone,updated_time,verified"
        );
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(FacebookLoginActivity.this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(FacebookLoginActivity.this);

    }


}
