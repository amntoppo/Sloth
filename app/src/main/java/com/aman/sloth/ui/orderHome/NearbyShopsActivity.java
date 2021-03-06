package com.aman.sloth.ui.orderHome;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aman.sloth.Common;
import com.aman.sloth.Model.ShopModel;
import com.aman.sloth.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NearbyShopsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;

    private FirebaseRecyclerAdapter<ShopModel, ShopModelHolder> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_shops);

        databaseReference = FirebaseDatabase.getInstance().getReference(Common.SHOP_INFO_REFERENCE);

        recyclerView = findViewById(R.id.recycler_view_neaby_shops);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = databaseReference;
        FirebaseRecyclerOptions<ShopModel> options = new FirebaseRecyclerOptions.Builder<ShopModel>()
                .setQuery(query, ShopModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<ShopModel, ShopModelHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ShopModelHolder shopModelHolder, int i, @NonNull ShopModel shopModel) {

                shopModelHolder.setShopName(shopModel.getShopName());
                Log.e("shop", shopModel.toMap().toString());
                shopModelHolder.setShopCategory(shopModel.getCategory());
                shopModelHolder.setShopDesc(shopModel.getDescription());
            }

            @NonNull
            @Override
            public ShopModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_nearby_shops, parent, false);
                return new ShopModelHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
