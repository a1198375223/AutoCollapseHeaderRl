package com.example.autocollaspseheader.auto;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.autocollaspseheader.R;

public class CommonFragment extends Fragment {
    private TextView mTextView;
    private String mText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);

        mTextView = (TextView) view.findViewById(R.id.text);
        mTextView.setText(mText);

        return view;
    }

    public void setText(String text) {
        this.mText = text;
    }
}
