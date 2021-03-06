package com.aman.sloth.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import com.aman.sloth.Common;
import com.aman.sloth.Model.ShopGeoModel;
import com.aman.sloth.Model.ShopModel;
import com.aman.sloth.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private HomeViewModel homeViewModel;
    private SupportMapFragment mapFragment;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location previousLocation, currentLocation;
    private GeoQuery geoQuery;
    private GeoFire geoFire;

    private SlidingUpPanelLayout slidingUpPanelLayout;
    private TextView welcome_textview;
    private AutocompleteSupportFragment autocompleteSupportFragment;

    //private FirebaseShopInfoListener firebaseShopInfoListener;
    private DatabaseReference locationReference;
    private DatabaseReference shopInfoReference;

    private boolean firstTime = true;
    private double distance = 1000;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel =
        //        new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        init(

        );
        initViews(root);

        return root;
    }

    private void initViews(View root) {
        //layout
        slidingUpPanelLayout = root.findViewById(R.id.layout_maps);
        welcome_textview = root.findViewById(R.id.places_text);


    }

    private void init() {
        locationReference = FirebaseDatabase.getInstance().getReference(Common.SHOP_DATA_REFERENCE);
        shopInfoReference = FirebaseDatabase.getInstance().getReference(Common.SHOP_INFO_REFERENCE);
        //firebaseShopInfoListener = this;


        //initialize places
        Places.initialize(getContext(), getString(R.string.google_maps_key));
        autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteSupportFragment.setHint(getString(R.string.where_to_go));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Snackbar.make(getView(), "" + place.getLatLng(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Status status) {
                Snackbar.make(getView(), "" + status.getStatusMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });

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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 18f));

                if(firstTime) {
                    previousLocation = currentLocation = locationResult.getLastLocation();
                    firstTime = false;
                }
                else {
                    previousLocation = currentLocation;
                    currentLocation = locationResult.getLastLocation();
                }
                if(geoQuery == null)
                    loadShopsinMap();

            }
        };


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

    }

    private void loadShopsinMap() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if(Common.CURRENT_CITY != null) {
            Log.e("current", "loadshopsinmaps");
            geoFire = new GeoFire(locationReference.child(Common.CURRENT_CITY).child("shop_location"));
            geoQuery = geoFire.queryAtLocation(new GeoLocation(previousLocation.getLatitude(), previousLocation.getLongitude()),distance);
            geoQuery.removeAllListeners();
            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String key, GeoLocation location) {
                    Common.shopLocation.add(new ShopGeoModel(key, location));
                    Log.e("query", "geo");
                }

                @Override
                public void onKeyExited(String key) {

                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {

                }

                @Override
                public void onGeoQueryReady() {
                    addShopMarker();
                }

                @Override
                public void onGeoQueryError(DatabaseError error) {
                    Snackbar.make(getView(), error.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            });
        }

//        fusedLocationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//
//                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
//                List<Address> addressList;
//                try {
//                    addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//
//                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                    Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    private void addShopMarker() {
        if(Common.shopLocation.size() > 0 ) {
//            Observable.fromIterable(Common.shopLocation)
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(ShopGeoModel -> {
//                        findShopbyKey(ShopGeoModel);
//                    });
            for(ShopGeoModel shopGeoModel: Common.shopLocation) {
                Log.e("shoplocation", "" + Common.shopLocation.size());
                findShopbyKey(shopGeoModel);
            }
        }
        else {
            Snackbar.make(getView(), "Shops not found", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void findShopbyKey(ShopGeoModel shopGeoModel) {
        shopInfoReference.child(shopGeoModel.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()) {
                            Log.e("findshopbykey", snapshot.getValue().toString());
                            shopGeoModel.setShopModel(snapshot.getValue(ShopModel.class));
                            //onShopInfoLoadSuccess(shopGeoModel);
                            onMarkerSuccess(shopGeoModel);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void onMarkerSuccess(ShopGeoModel shopGeoModel) {
        Log.e("callback", "listens");
        if(!Common.shopMarkerList.containsKey(shopGeoModel.getKey())) {
            if(mMap != null) {
                Common.shopMarkerList.put(shopGeoModel.getKey(), mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(shopGeoModel.getGeoLocation().latitude, shopGeoModel.getGeoLocation().longitude))
                        .flat(true)
                        .title(shopGeoModel.getShopModel().getShopName())
                        .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.shop_icon))));
            }
            else {
                Log.e("map", "null");
            }
        }
        Log.e("shopmarkerlist", "" + Common.shopMarkerList.size() );
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //geoQuery.removeAllListeners();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        geoQuery = null;
        Common.shopLocation.clear();
        Common.shopMarkerList.clear();
        Log.e("Fragment", "Detach");
        //geoQuery.removeAllListeners();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.getUiSettings().setZoomControlsEnabled(true);

        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);

                        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                            @Override
                            public boolean onMyLocationButtonClick() {
                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return false;
                                }
                                fusedLocationProviderClient.getLastLocation()
                                        .addOnFailureListener(e -> Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show())
                                        .addOnSuccessListener(location -> {
                                            LatLng userLatlng = new LatLng(location.getLatitude(),location.getLongitude() );
                                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatlng, 18f));
                                        });
                                return true;
                            }
                        });

                        //location button
                        View locationButton = ((View)mapFragment.getView().findViewById(Integer.parseInt("1")).getParent())
                                .findViewById(Integer.parseInt("2"));
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                        params.setMargins(0, 0, 0, 50);
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
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
            mMap.setOnMarkerClickListener(this);
            if(!success)
                Snackbar.make(getView(),"Loading map failed", Snackbar.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }

    }


    public void onShopInfoLoadSuccess(ShopGeoModel shopGeoModel) {
        Log.e("callback", "listens");
        if(!Common.shopMarkerList.containsKey(shopGeoModel.getKey())) {
            if(mMap != null) {
                Common.shopMarkerList.put(shopGeoModel.getKey(), mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(shopGeoModel.getGeoLocation().latitude, shopGeoModel.getGeoLocation().longitude))
                        .flat(true)
                        .title(shopGeoModel.getShopModel().getShopName())
                        .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.shop_icon))));
            }
            else {
                Log.e("map", "null");
            }
        }
        Log.e("shopmarkerlist", "" + Common.shopMarkerList.size() );
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(Common.shopMarkerList.containsValue(marker)) {
            String markerID = getIDfromMarker(marker);
            shopInfoReference.child(markerID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                ShopModel shopModel = snapshot.getValue(ShopModel.class);
                                Log.e("shopmodel", shopModel.getShopName());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            return true;
        }
        return false;
    }

    private String getIDfromMarker(Marker marker) {
        HashMap<Marker, String> reversedHash = new HashMap<>();
        for(Map.Entry<String, Marker> entry : Common.shopMarkerList.entrySet()) {
            reversedHash.put(entry.getValue(), entry.getKey());
        }
        return reversedHash.get(marker);
    }
}