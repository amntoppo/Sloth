package com.aman.sloth.ui.shop;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.aman.sloth.Common;
import com.aman.sloth.Model.ShopItemModel;
import com.aman.sloth.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShopDashboardActivity extends AppCompatActivity implements ItemDialog.AdditemDialogListener{

    private FloatingActionButton floatingActionButton;
    private ListView listView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference shopDataReference;

    private ArrayList<ShopItemModel> arrayList = new ArrayList<>();
    HashMap<String, String> hashSnap = new HashMap<>();
    private ListViewAdapter adapter;

    private ShopItemModel shopItemModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shop_dashboard);
        initialize();
        listViewInitialize();


    }

    private void listViewInitialize() {
        adapter = new ListViewAdapter(this, arrayList);
        listView = findViewById(R.id.item_list_view);
        listView.setAdapter(adapter);

        shopDataReference.child(Common.shopModel.getCity())
                .child("shop_items")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        //Log.e("item", snapshot.toString());
//                        for(DataSnapshot data: snapshot.getChildren()) {
//                            shopItemModel = new ShopItemModel(data.getValue("itemName"),)
//                        }
                        shopItemModel = new ShopItemModel();
                        shopItemModel.setItemName(snapshot.getValue(ShopItemModel.class).getItemName());
                        shopItemModel.setPrice(snapshot.getValue(ShopItemModel.class).getPrice());
                        arrayList.add(shopItemModel);
                        Log.e("array", arrayList.get(0).getItemName());
                        adapter.notifyDataSetChanged();

                        //Log.e("itemChild", snapshot.getChildren().toString());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

        //HashMap<String, Object> values = new HashMap<>();
        //values.put(itemName, price);
        shopItemModel = new ShopItemModel(itemName, price);
        shopDataReference.child(Common.shopModel.getCity())
                .child("shop_items")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FirebaseDatabase.getInstance().getReference().push().getKey())
                .setValue(shopItemModel);

    }

}
