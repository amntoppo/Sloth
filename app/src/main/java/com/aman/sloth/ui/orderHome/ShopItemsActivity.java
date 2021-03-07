package com.aman.sloth.ui.orderHome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.aman.sloth.Common;
import com.aman.sloth.Model.ShopItemModel;
import com.aman.sloth.R;
import com.aman.sloth.ui.shop.ListViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShopItemsActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference shopDataReference;

    private ListView listView;
    private ArrayList<ShopItemModel> arrayList = new ArrayList<>();
    private ListViewAdapter adapter;

    private ShopItemModel shopItemModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_items);

        initialize();
        listViewInitialize();

    }

    private void initialize() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        shopDataReference = firebaseDatabase.getReference(Common.SHOP_DATA_REFERENCE);
    }

    private void listViewInitialize() {
        adapter = new ListViewAdapter(this, arrayList);
        listView = findViewById(R.id.list_view_shop_items);
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        String shopID = intent.getStringExtra("shop_id");
        String shopCity = intent.getStringExtra("shop_city");

        shopDataReference.child(shopCity)
                .child("shop_items")
                .child(shopID)
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
                        shopItemModel.setshopID(shopID);
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
                });
    }
}
