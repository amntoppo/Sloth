package com.aman.sloth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SplashScreenActivity extends AppCompatActivity {

    private final static int LOGIN_REQUEST_CODE = 1337;
    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference customerDatabaseRef;


    private ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        init();

    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        if(firebaseAuth != null && listener != null)
            firebaseAuth.removeAuthStateListener(listener);
        super.onStop();

    }

    private void init() {
        progressBar = findViewById(R.id.progress_bar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        customerDatabaseRef = firebaseDatabase.getReference(Common.CUSTOMER_INFO_REFERENCE)

        providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );
        firebaseAuth = FirebaseAuth.getInstance();
        listener = myFirebaseAuth -> {
            FirebaseUser firebaseUser = myFirebaseAuth.getCurrentUser();
            if(firebaseUser != null) {
                checkFirebaseUser();
            }
            else {
                showLoginLayout();
            }
        };

    }

    private void checkFirebaseUser() {
        customerDatabaseRef.child(firebaseAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            Toast.makeText(SplashScreenActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            showRegisterLayout();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SplashScreenActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void showRegisterLayout() {


    }

    private void showLoginLayout() {
        AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout
                .Builder(R.layout.layout_signin)
                .setPhoneButtonId(R.id.button_phone_signin)
                .setGoogleButtonId(R.id.button_email_signin)
                .build();
        startActivityForResult(AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAuthMethodPickerLayout(authMethodPickerLayout)
        .setIsSmartLockEnabled(false)
                .setTheme(R.style.LoginTheme)
        .setAvailableProviders(providers)
        .build(), LOGIN_REQUEST_CODE);
    }

    private void displaySplashScreen() {
        progressBar.setVisibility(View.VISIBLE);


        Completable.timer(5, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(() -> {

                });
    }

}