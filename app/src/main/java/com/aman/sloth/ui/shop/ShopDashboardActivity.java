package com.aman.sloth.ui.shop;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aman.sloth.Common;
import com.aman.sloth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShopDashboardActivity extends AppCompatActivity implements ItemDialog.AdditemDialogListener{
    private Button addItemButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference shopDataReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shop_dashboard);
        initialize();


    }

    private void initialize() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        shopDataReference = firebaseDatabase.getReference(Common.SHOP_DATA_REFERENCE);

        addItemButton = findViewById(R.id.button_add_item_to_shop);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    private void openDialog() {
        ItemDialog itemDialog = new ItemDialog();
        itemDialog.show(getSupportFragmentManager(), "Add items");
    }

    @Override
    public void itemData(String itemName, int price) {
        Log.e("itemdata", itemName + price);
        //shopDataReference.child()

    }
}