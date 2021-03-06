package com.aman.sloth.ui.orderHome;

import android.view.View;

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

    public ShopModelHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        shopNameTextView = mView.findViewById(R.id.material_textview_shop_name);
        shopDescTextView = mView.findViewById(R.id.material_textview_shop_description);
        shopCategoryTextView = mView.findViewById(R.id.material_textview_shop_category);
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
}
