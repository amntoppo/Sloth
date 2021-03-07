package com.aman.sloth.ui.shop;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.aman.sloth.Common;
import com.aman.sloth.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ItemDialog extends AppCompatDialogFragment {

    private TextInputEditText itemNameEditText;
    private TextInputEditText itemPriceEditText;
    private AdditemDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AdditemDialogListener) context;
        } catch (ClassCastException e) {
            Log.e("dialog", e.getMessage());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_add_items_dialog, null);
        builder.setView(view)
                .setTitle("Add item")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("dialog", "Cancel");
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        listener.itemData(itemNameEditText.getText().toString(), Integer.parseInt(itemPriceEditText.getText().toString()), FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

                    }
                });
        itemNameEditText = view.findViewById(R.id.edit_item_name);
        itemPriceEditText = view.findViewById(R.id.edit_item_price);

        return builder.create();
    }
    public interface AdditemDialogListener{
        void itemData(String itemName, int price, String shopID);
    }

}
