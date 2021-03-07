package com.aman.sloth.ui.search;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aman.sloth.Common;
import com.aman.sloth.Model.ShopItemModel;
import com.aman.sloth.Model.ShopModel;
import com.aman.sloth.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchItemHolder>{
    private ArrayList<ShopItemModel> arrayList;
    private DatabaseReference shopInfoReference;

    public SearchAdapter(ArrayList<ShopItemModel> arrayList) {
        Log.e("adapter", "constructor works");
        shopInfoReference = FirebaseDatabase.getInstance().getReference(Common.SHOP_INFO_REFERENCE);
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public SearchItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //View view = inflater.inflate(R.layout.listview_search_items, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_search_items, parent, false);
        Log.e("adapter", "onCreateHolder works");
        return new SearchItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemHolder holder, int position) {
        shopInfoReference.child(arrayList.get(position).getSHopID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.setShopNameTextView(snapshot.getValue(ShopModel.class).getShopName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.setShopItemTextView(arrayList.get(position).getItemName());

        holder.setShopPriceTextView("Rs." + arrayList.get(position).getPrice());
        Log.e("dataaaaa", arrayList.get(0).getItemName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
