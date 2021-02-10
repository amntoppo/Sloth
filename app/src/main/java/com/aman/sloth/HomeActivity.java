package com.aman.sloth;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.aman.sloth.ui.gallery.GalleryFragment;
import com.aman.sloth.ui.home.HomeFragment;

import com.wwdablu.soumya.lottiebottomnav.FontBuilder;
import com.wwdablu.soumya.lottiebottomnav.FontItem;
import com.wwdablu.soumya.lottiebottomnav.ILottieBottomNavCallback;
import com.wwdablu.soumya.lottiebottomnav.LottieBottomNav;
import com.wwdablu.soumya.lottiebottomnav.MenuItem;
import com.wwdablu.soumya.lottiebottomnav.MenuItemBuilder;

import java.util.ArrayList;


import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;


public class HomeActivity extends AppCompatActivity implements ILottieBottomNavCallback {

    LottieBottomNav lottieNav;
    ArrayList<MenuItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//
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