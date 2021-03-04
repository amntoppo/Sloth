package com.aman.sloth.ui.shop;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.aman.sloth.Common;
import com.aman.sloth.Model.CustomerInfoModel;
import com.aman.sloth.Model.ShopModel;
import com.aman.sloth.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.internal.bind.ArrayTypeAdapter;
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
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CreateShopActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mmap;
    private SupportMapFragment mapFragment;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    private Button continueButton;

    private ShopModel shopModel;
    private LatLng shopLocation;
    private String cityname;

    private GeoFire geoFire;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_shop_register);
        mapInitialize();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Common.SHOP_INFO_REFERENCE);
        buttonHandling();
    }

    private void buttonHandling() {
        continueButton = findViewById(R.id.button_create_shop_continue);
        TextInputEditText edit_shop_name = (TextInputEditText) findViewById(R.id.edit_shop_name);
        TextInputEditText edit_shop_description = (TextInputEditText) findViewById(R.id.edit_description);
        TextInputEditText edit_category = (TextInputEditText) findViewById(R.id.edit_category);


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_shop_name.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please enter first name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(edit_shop_description.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please enter last name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(edit_category.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addresseList;
                    try {
                        addresseList = geocoder.getFromLocation(shopLocation.latitude, shopLocation.longitude, 1);
                        cityname = addresseList.get(0).getLocality();
                        Log.e("city", cityname);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    shopModel = new ShopModel(edit_shop_name.getText().toString(), edit_shop_description.getText().toString() ,edit_category.getText().toString());
                }
                databaseReference = databaseReference.child(cityname);
                geoFire = new GeoFire(databaseReference);
                geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        new GeoLocation(shopLocation.latitude, shopLocation.longitude),
                        (key, error) -> {
                            if (error != null)
                                Log.e("geofire", error.getMessage());
                        });
                Map<String, Object> values = shopModel.toMap();
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .updateChildren(values);

            }
        });
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void mapInitialize() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_create_shop);
        mapFragment.getMapAsync((OnMapReadyCallback) this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mmap = googleMap;


        locationRequest = new LocationRequest();
        locationRequest.setSmallestDisplacement(10f);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LatLng newPosition = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());

                mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 18f));
                mmap.clear();
                Marker shopLocationMarker = mmap.addMarker(new MarkerOptions().icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.shop_location)).position(newPosition).title("shop").snippet("Location of shop").draggable(true));
                shopLocation = shopLocationMarker.getPosition();
                mmap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {
                        shopLocation = marker.getPosition();
                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {

                    }
                });
            }
        };
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        //mmap.setMyLocationEnabled(true);
                        //mmap.getUiSettings().setMyLocationButtonEnabled(true);

//                        mmap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//                            @Override
//                            public boolean onMyLocationButtonClick() {
//                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                    return false;
//                                }
//                                fusedLocationProviderClient.getLastLocation()
//                                        .addOnFailureListener(e -> Snackbar.make(findViewById(android.R.id.content), e.getMessage(), Snackbar.LENGTH_SHORT).show())
//                                        .addOnSuccessListener(location -> {
//                                            LatLng userLatlng = new LatLng(location.getLatitude(),location.getLongitude() );
//                                            mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatlng, 18f));
//                                        });
//                                return true;
//                            }
//                        });
//
//                        //location button
//                        View locationButton = ((View)mapFragment.getView().findViewById(Integer.parseInt("1")).getParent())
//                                .findViewById(Integer.parseInt("2"));
//                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
//                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//                        params.setMargins(0, 0, 0, 50);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                })
                .check();
        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.map_style));
            if(!success)
                Snackbar.make(findViewById(android.R.id.content),"Loading map failed", Snackbar.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Snackbar.make(findViewById(android.R.id.content), e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }


}
