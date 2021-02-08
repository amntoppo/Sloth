package com.aman.sloth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aman.sloth.Model.CustomerInfoModel;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

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
    CustomerInfoModel model;




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
        customerDatabaseRef = firebaseDatabase.getReference(Common.CUSTOMER_INFO_REFERENCE);

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

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        View itemView = LayoutInflater.from(this).inflate(R.layout.layout_register, null);

        TextInputEditText edit_first_name = (TextInputEditText)itemView.findViewById(R.id.edit_first_name);
        TextInputEditText edit_last_name = (TextInputEditText)itemView.findViewById(R.id.edit_last_name);
        TextInputEditText edit_phone = (TextInputEditText)itemView.findViewById(R.id.edit_phone_number);
        Button button_continue = (Button)itemView.findViewById(R.id.button_register);

        //set phone number
        if(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null &&
                !TextUtils.isEmpty(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())) {
            edit_phone.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        }

        //set view
        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();
        button_continue.setOnClickListener(v -> {
            if(TextUtils.isEmpty(edit_first_name.getText().toString())) {
                Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(TextUtils.isEmpty(edit_last_name.getText().toString())) {
                Toast.makeText(this, "Please enter last name", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(TextUtils.isEmpty(edit_phone.getText().toString())) {
                Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                model = new CustomerInfoModel();
                model.setFirstname(edit_first_name.getText().toString());
                model.setLastname(edit_last_name.getText().toString());
                model.setPhoneNumber(edit_phone.getText().toString());
            }

            customerDatabaseRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(model)
                    .addOnFailureListener(e -> {
                        dialog.dismiss();
                        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
            .addOnSuccessListener(aVoid -> {
                dialog.dismiss();
                Toast.makeText(this, "Registration Success", Toast.LENGTH_SHORT).show();
            });

        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQUEST_CODE) {
            Intent intent;
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            }
            else {
                Toast.makeText(this, "Failed to sign in : " + response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}