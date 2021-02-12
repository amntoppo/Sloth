package com.aman.sloth.Utils;

import android.content.Context;
import android.widget.Toast;
import android.widget.Toolbar;

import com.aman.sloth.Common;
import com.aman.sloth.Model.TokenModel;
import com.aman.sloth.Services.FirebaseMessagingService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;

public class UserUtils {

    public static void updateToken(Context context, String token) {
        TokenModel tokenModel = new TokenModel(token);
        FirebaseDatabase.getInstance()
                .getReference(Common.TOKEN_REFERENCE)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(tokenModel)
                .addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(aVoid -> {

                });
    }

}
