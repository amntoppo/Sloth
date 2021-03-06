package com.aman.sloth.ui.orderHome;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.aman.sloth.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShopModelHolder extends RecyclerView.ViewHolder {
    private View mView;
    private MaterialTextView shopNameTextView;
    private MaterialTextView shopDescTextView;
    private MaterialTextView shopCategoryTextView;
    private onItemClickedListener listener;
    private String shop_id;
    private String city;

    public ShopModelHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        shopNameTextView = mView.findViewById(R.id.material_textview_shop_name);
        shopDescTextView = mView.findViewById(R.id.material_textview_shop_description);
        shopCategoryTextView = mView.findViewById(R.id.material_textview_shop_category);

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClicked(mView, position);
                }
            }
        });

    }
    public void setShopName(String shopName) {
        shopNameTextView.setText(shopName);
    }
    public void setShopDesc(String shopDesc) {
        shopDescTextView.setText(shopDesc);
    }
    public void setShopCategory(String shopCategory) {
        shopCategoryTextView.setText(shopCategory);
    }
    public void setShopId(String id) {
        this.shop_id = id;
    }

    public interface onItemClickedListener {
        void onItemClicked(View view, int position);
    }
    public void setOnItemClickListener(ShopModelHolder.onItemClickedListener listener) {
        this.listener = listener;
    }
}