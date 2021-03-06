package com.aman.sloth.ui.search;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aman.sloth.Common;
import com.aman.sloth.Model.ShopItemModel;
import com.aman.sloth.Model.ShopModel;
import com.aman.sloth.R;
import com.firebase.geofire.GeoFire;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class searchFragment extends Fragment {

    private EditText editText;
    private String cityName;

    private RecyclerView recyclerView;
    private DatabaseReference shopDatabaseReference;

    //private FirebaseRecyclerAdapter<ShopItemModel, SearchItemHolder> adapter;
    private SearchAdapter adapter;
    private ArrayList<ShopItemModel> itemList = new ArrayList<>();
    private ArrayList<ShopItemModel> searchList = new ArrayList<>();

    private SearchItemHolder searchItemHolder;

    private GeoFire geoFire;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        initialize(root);
        showKeyboard();
        searchForItems();
        askPermission();
        //getCity();
        recyclerView = root.findViewById(R.id.recycler_view_search);
        recyclerViewSetup();



        return root;
    }

    private void recyclerViewSetup() {
        shopDatabaseReference.child(Common.CURRENT_CITY).child("shop_items").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot data: snapshot.getChildren()) {
                    Log.e("key", data.getKey());
                    //Log.e("data", data.getValue(String.class));
                    ShopItemModel shopItemModel;
                    shopItemModel = data.getValue(ShopItemModel.class);
                    itemList.add(shopItemModel);
                    Log.e("data", itemList.get(0).getItemName());
                }
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


    private void askPermission() {
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(getContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
                        searchFragment.super.onDestroy();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                })
                .check();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("CURRENTCITY", Common.CURRENT_CITY);
        Query query = shopDatabaseReference.child(Common.CURRENT_CITY).child("shop_items").orderByChild("2w6RkGhNzlajfPnwlRbF0oUp6pE2");

    }

    @Override
    public void onStop() {
        super.onStop();
        //adapter.stopListening();
    }

    private void initialize(View root) {
        editText = root.findViewById(R.id.search_edit_text);
        shopDatabaseReference = FirebaseDatabase.getInstance().getReference(Common.SHOP_DATA_REFERENCE);

    }

    private void searchForItems() {
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                for(ShopItemModel model: itemList) {
//                    if(model.getItemName().contains(s)) {
//                        //do
//                        searchList.add(model);
//                        adapter = new SearchAdapter(searchList);
//                        recyclerView.setHasFixedSize(true);
//                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                        recyclerView.setAdapter(adapter);
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    searchList.clear();
                    Log.e("emter", "pressed");
                    for(ShopItemModel model: itemList) {
                        if(model.getItemName().contains(editText.getText())) {
                            //do

                            searchList.add(model);
                            adapter = new SearchAdapter(searchList);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }
                return false;

            }
        });
    }

    private void showKeyboard() {
        editText.setText("");
        editText.requestFocus();
        editText.postDelayed(() -> {

            InputMethodManager keyboard = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(editText, 0);
        },200);
    }
}
