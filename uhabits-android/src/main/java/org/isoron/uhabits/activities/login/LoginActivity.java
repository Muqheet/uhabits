package org.isoron.uhabits.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.isoron.androidbase.activities.BaseActivity;
import org.isoron.uhabits.R;
import org.isoron.uhabits.activities.habits.list.ListHabitsActivity;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 123;
    FirebaseUser user;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            navigateToHabitsListActivity();
        } else
            signinTemplate();
    }

    private void signinTemplate() {
// ...

// Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );
/*
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build()
        );
*/

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    /**
     * Signin result will be here after validating user by firebase
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                //On success navigate user to habits list
                navigateToHabitsListActivity();
                // ...
            } else {

                Snackbar.make(findViewById(R.id.action_archive_habit),
                        "Signin failed, " + response.getError().getMessage() + ": " + response.getError().getErrorCode(),
                        Snackbar.LENGTH_LONG).show();
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private void navigateToHabitsListActivity() {
        startActivity(new Intent(this, ListHabitsActivity.class));
    }

    public void logout() {
/*
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    // ...
                    if (task.isSuccessful()) {

                        Toast.makeText(this, "Successfully Logged out.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, LoginActivity.class));
                    }

                });
*/
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        // App crashing because of below line on logout
        startActivity(new Intent(this, LoginActivity.class));

    }

}
