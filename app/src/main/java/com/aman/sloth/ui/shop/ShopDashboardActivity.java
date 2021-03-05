package com.aman.sloth.ui.shop;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.aman.sloth.Common;
import com.aman.sloth.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShopDashboardActivity extends AppCompatActivity implements ItemDialog.AdditemDialogListener{

    private FloatingActionButton floatingActionButton;
    private ListView listView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference shopDataReference;

    private ArrayList<String> arrayList = new ArrayList<>();
    HashMap<String, String> hashSnap = new HashMap<>();
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shop_dashboard);
        initialize();
        listViewInitialize();


    }

    private void listViewInitialize() {
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_shop_items, arrayList);
        listView = findViewById(R.id.item_list_view);
        listView.setAdapter(adapter);

        shopDataReference.child(Common.shopModel.getCity())
                .child("shop_items")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //update View
                        //HashMap<String, String> hash = snapshot.getValue(HashMap.class);
                        //DataSnapshot hashSnap = (DataSnapshot) snapshot.getValue();

                        //Log.e("item", hashSnap.getKey().toString());
                        Log.e("item", snapshot.getValue().toString());
                        //arrayList.add(hashSnap.getValue().toString());
                        adapter.notifyDataSetChanged();
                        for(DataSnapshot data: snapshot.getChildren()) {
                            hashSnap.put(data.getKey(), data.getValue().toString());
                            arrayList.add(data.getValue().toString());
                            Log.e("hash", hashSnap.toString());
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });                ;
//        shopDataReference.child(Common.shopModel.getCity())
//                .child("ge")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Log.e("item", snapshot.toString());
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
    }

    private void initialize() {




        firebaseDatabase = FirebaseDatabase.getInstance();
        shopDataReference = firebaseDatabase.getReference(Common.SHOP_DATA_REFERENCE);


        floatingActionButton = findViewById(R.id.fab_add_item_to_shop);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
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
        arrayList.clear();
        HashMap<String, Object> values = new HashMap<>();
        values.put(itemName, price);
        shopDataReference.child(Common.shopModel.getCity())
                .child("shop_items")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .updateChildren(values);

    }

}
