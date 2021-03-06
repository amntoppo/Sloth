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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.google.firebase.database.DataSnapshot;
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

    private FirebaseRecyclerAdapter<ShopItemModel, SearchItemHolder> adapter;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

    private void getCity() {
//                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
//        List<Address> addresseList;
//        try {
//            addresseList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            cityName = addresseList.get(0).getLocality();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Context context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresseList;
        try {
            addresseList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            cityName = addresseList.get(0).getLocality();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
//        DataSnapshot aa = //shopDatabaseReference.child(Common.CURRENT_CITY).child("shop_items");
//                shopDatabaseReference.child(Common.CURRENT_CITY).child("shop_items")
//                        .get().getResult();


        FirebaseRecyclerOptions<ShopItemModel> options = new FirebaseRecyclerOptions.Builder<ShopItemModel>()
                .setQuery(query, ShopItemModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<ShopItemModel, SearchItemHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SearchItemHolder searchItemHolder, int i, @NonNull ShopItemModel shopItemModel) {
                searchItemHolder.setShopItemTextView(shopItemModel.getItemName());
                searchItemHolder.setShopPriceTextView("Rs." + shopItemModel.getPrice());
//                if(shopItemModel.getItemName().isEmpty()) {
//
//                }
//                else {
//                    Log.e("shopitem", "exists");
//                }

            }

            @NonNull
            @Override
            public SearchItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_search_items, parent, false);
                searchItemHolder = new SearchItemHolder(view);
                return searchItemHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void initialize(View root) {
        editText = root.findViewById(R.id.search_edit_text);
        shopDatabaseReference = FirebaseDatabase.getInstance().getReference(Common.SHOP_DATA_REFERENCE);
    }

    private void searchForItems() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

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
