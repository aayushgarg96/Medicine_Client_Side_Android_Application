package com.example.ayush.medicine;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Ayush on 12/7/2016.
 */
public class Fragment_Update extends Fragment{

    Button btn1;
    EditText et1, et2;
    private String oldpassword, newpassword, password, mobile;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update, container, false);
        btn1 = (Button) view.findViewById(R.id.button5);
        et1 = (EditText) view.findViewById(R.id.old_password);
        et2 = (EditText) view.findViewById(R.id.new_password);
        password = homepage.password;
        mobile = homepage.mobile;
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldpassword = et1.getText().toString();
                newpassword = et2.getText().toString();

                if(oldpassword.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter your old password", Toast.LENGTH_SHORT).show();
                }
                else if(newpassword.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter your new password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(newpassword.length()<8 || newpassword.length()>15){
                        Toast.makeText(getActivity(),"Password should between 8-15 characters",Toast.LENGTH_SHORT).show();
                    }
                    else if(oldpassword.equals(password)==false){
                        Toast.makeText(getActivity(), "Old Password incorrect", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        UpdatePassword updatePassword = new UpdatePassword(getActivity());
                        updatePassword.execute(newpassword, mobile);
                        et1.setText("");
                        et2.setText("");
                    }
                }
            }
        });
        return view;
    }
}