package com.aman.sloth.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.aman.sloth.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class searchFragment extends Fragment {

    private EditText editText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        editText = root.findViewById(R.id.search_edit_text);

        showKeyboard();
        return root;
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
