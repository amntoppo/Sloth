package com.aman.sloth.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import com.aman.sloth.Common;
import com.aman.sloth.R;
import com.aman.sloth.ui.search.searchFragment;
import com.aman.sloth.ui.shop.CreateShopActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tomer.fadingtextview.FadingTextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class GalleryFragment extends Fragment implements View.OnClickListener{

    private GalleryViewModel galleryViewModel;
    private TextView fadingText;

    //private TextView nameText;
    int count = 0;

    private RelativeLayout relativeLayout;
    private ConstraintLayout layoutHeader;

    private LinearLayout layouyOrderHome;
    private  LinearLayout layoutDeliverAnything;
    private LinearLayout layoutDeliverYourself;
    private LinearLayout layoutCreateShop;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        initializeButtons(root);

        //customer_first_name = Common.currentCustomer.getFirstname();


        fadingText = root.findViewById(R.id.hello_text);
        //nameText = root.findViewById(R.id.text_name);
        fadingText.setText("Hi there, \n " + Common.currentCustomer.getFirstname());

        Observable.interval(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tick -> {
                    fadingText.setText(Common.text[count]);
                    if (fadingText.getText() == "") {
                        fadingText.setText("Hi there, \n " + Common.currentCustomer.getFirstname());

                    }
                    count++;
                    if (count > 3)
                        count = 0;
                });


        relativeLayout = (RelativeLayout) root.findViewById(R.id.searchbar_layout);
        layoutHeader = root.findViewById(R.id.layout_header);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animate = new TranslateAnimation(0, 0, 0, -650);
                animate.setDuration(500);
                animate.setFillAfter(true);
                relativeLayout.startAnimation(animate);
                layoutHeader.startAnimation(animate);
                animate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new searchFragment()).addToBackStack(null).commit();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
        return root;
    }


    private void initializeButtons(View root) {
        layouyOrderHome = root.findViewById(R.id.layout_order_home);
        layoutDeliverAnything = root.findViewById(R.id.layout_deliver_anything);
        layoutDeliverYourself = root.findViewById(R.id.layout_get_yourself_delivered);
        layoutCreateShop = root.findViewById(R.id.layout_create_shop);

        layouyOrderHome.setOnClickListener(this);
        layoutDeliverAnything.setOnClickListener(this);
        layoutDeliverYourself.setOnClickListener(this);
        layoutCreateShop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_order_home:
                Log.e("button", "order home");
                break;
            case R.id.layout_deliver_anything:
                Log.e("button", "order anything");
                break;
            case R.id.layout_get_yourself_delivered:
                break;
            case R.id.layout_create_shop:
                Intent intent = new Intent(getActivity(), CreateShopActivity.class);
                startActivity(intent);
                break;

        }
    }
}