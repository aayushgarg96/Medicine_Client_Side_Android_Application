package com.example.ayush.medicine;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by sankalp on 24-08-2016.
 */
public class Fragment_Home extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView =  inflater.inflate(R.layout.fragment_home, container, false);
        ImageView iv = (ImageView)rootView.findViewById(R.id.imageView10);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), GetImage.class);
                startActivity(intent1);
            }
        });
        return rootView;
    }
}
