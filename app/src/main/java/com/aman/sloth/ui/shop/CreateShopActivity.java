package com.aman.sloth.ui.shop;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.aman.sloth.R;
import com.google.gson.internal.bind.ArrayTypeAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreateShopActivity extends AppCompatActivity {

    Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_shop_register);
        spinnerInitialize();
    }

    private void spinnerInitialize() {
        spinner = findViewById(R.id.order_home_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateShopActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.itemselect));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
