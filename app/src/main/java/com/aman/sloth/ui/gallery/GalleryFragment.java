package com.aman.sloth.ui.gallery;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aman.sloth.R;
import com.aman.sloth.ui.search.searchFragment;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private RelativeLayout relativeLayout;
    private ConstraintLayout layoutHeader;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
//        final TextView textView = root.findViewById(R.id.text_gallery);
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
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
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new searchFragment()).commit();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
        return root;
    }
}