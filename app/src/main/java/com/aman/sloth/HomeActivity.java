package com.aman.sloth;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;


import com.aman.sloth.ui.gallery.GalleryFragment;
import com.aman.sloth.ui.home.HomeFragment;
import com.aman.sloth.ui.slideshow.SlideshowFragment;
import com.google.android.material.navigation.NavigationView;
import com.wwdablu.soumya.lottiebottomnav.FontBuilder;
import com.wwdablu.soumya.lottiebottomnav.FontItem;
import com.wwdablu.soumya.lottiebottomnav.ILottieBottomNavCallback;
import com.wwdablu.soumya.lottiebottomnav.LottieBottomNav;
import com.wwdablu.soumya.lottiebottomnav.MenuItem;
import com.wwdablu.soumya.lottiebottomnav.MenuItemBuilder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity implements ILottieBottomNavCallback {

    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    LottieBottomNav lottieNav;
    ArrayList<MenuItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        navigationView = (NavigationView)findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);


        //setupNavigationVIew();
        lottieNavigation();
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

//    private void setupNavigationVIew() {
//        Log.e("nav", "works");
//        navigationView.setNavigationItemSelectedListener(item -> {
//            Log.e("menu", "found");
//            Log.e("meny", item.getTitle().toString());
//            switch (item.getItemId()) {
//
//                case R.id.nav_home:
//                    Log.e("menu", "home");
//                    break;
//                case R.id.nav_gallery:
//                    Log.e("menu", "gallery");;
//                    break;
//                case R.id.nav_slideshow:
//                    Toast.makeText(HomeActivity.this, "SLide", Toast.LENGTH_SHORT).show();
//                    break;
//
//            }
//            if (item.isChecked()) {
//                item.setChecked(false);
//            } else {
//                item.setChecked(true);
//            }
//            item.setChecked(true);
//
//            return true;
//        });
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        //getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
//        return true;
//    }
//
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

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
                selectedFragment = new SlideshowFragment();
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