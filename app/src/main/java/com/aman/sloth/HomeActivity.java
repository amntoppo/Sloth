package com.aman.sloth;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.aman.sloth.ui.gallery.GalleryFragment;
import com.aman.sloth.ui.home.HomeFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wwdablu.soumya.lottiebottomnav.FontBuilder;
import com.wwdablu.soumya.lottiebottomnav.FontItem;
import com.wwdablu.soumya.lottiebottomnav.ILottieBottomNavCallback;
import com.wwdablu.soumya.lottiebottomnav.LottieBottomNav;
import com.wwdablu.soumya.lottiebottomnav.MenuItem;
import com.wwdablu.soumya.lottiebottomnav.MenuItemBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


public class HomeActivity extends AppCompatActivity implements ILottieBottomNavCallback {

    LottieBottomNav lottieNav;
    ArrayList<MenuItem> list;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        lottieNavigation();
        setCity();
    }

    private void setCity() {
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Toast.makeText(HomeActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(HomeActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresseList;
                try {
                    addresseList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    String cityName = addresseList.get(0).getLocality();
                    Common.CURRENT_CITY = cityName;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void lottieNavigation() {
        lottieNav = findViewById(R.id.lottie_bottom_nav);
        FontItem fontItem = FontBuilder.create("Map")
                .selectedTextColor(Color.BLACK)
                .unSelectedTextColor(Color.GRAY)
                .selectedTextSize(0)
                .unSelectedTextColor(0)
                .build();
        MenuItem menuItem1 = MenuItemBuilder.create("map_icon.json", com.wwdablu.soumya.lottiebottomnav.MenuItem.Source.Assets, fontItem, "upload")
                .pausedProgress(1f)
                .loop(true)
                .build();

        fontItem = FontBuilder.create(fontItem).setTitle("Home").build();
        MenuItem menuItem2 = MenuItemBuilder.createFrom(menuItem1, fontItem)
                .selectedLottieName("home_icon.json")
                .unSelectedLottieName("home_icon.json")
                .build();

        fontItem = FontBuilder.create(fontItem).setTitle("Settings").build();
        MenuItem menuItem3 = MenuItemBuilder.createFrom(menuItem1, fontItem)
                .selectedLottieName("settings_icon.json")
                .unSelectedLottieName("settings_icon.json")
                .build();
        list = new ArrayList<>(3);
        list.add(menuItem1);
        Log.e("menu", "created");
        list.add(menuItem2);
        list.add(menuItem3);

        lottieNav.setCallback(this);
        lottieNav.setMenuItemList(list);
        lottieNav.setSelectedIndex(1);
    }

    @Override
    public void onMenuSelected(int oldIndex, int newIndex, MenuItem menuItem) {
        Fragment selectedFragment = null;
        switch (newIndex) {
            case 0:
                selectedFragment = new HomeFragment();
                break;
            case 1:
                selectedFragment = new GalleryFragment();
                break;
            case 2:
                //selectedFragment = new SlideshowFragment();
                break;
            default:
                selectedFragment = new HomeFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }

    @Override
    public void onAnimationStart(int index, MenuItem menuItem) {

    }

    @Override
    public void onAnimationEnd(int index, MenuItem menuItem) {

    }

    @Override
    public void onAnimationCancel(int index, MenuItem menuItem) {

    }


}