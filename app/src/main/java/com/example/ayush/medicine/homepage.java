package com.example.ayush.medicine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String email, mobile, name, password;
    private NavigationView navigationView;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //getSupportActionBar().setTitle(item.getTitle());
        Fragment_Home hf = new Fragment_Home();
        android.app.FragmentTransaction ft = homepage.this.getFragmentManager().beginTransaction();
        ft.replace(R.id.frame, hf);
        ft.commit();
       // setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ss = sp.getString("sp_status", "No Data");
        name = sp.getString("sp_name","");
        email = sp.getString("sp_email","");
        mobile = sp.getString("sp_mobile","");
        password = sp.getString("sp_password","");
        SharedPreferences.Editor ET = sp.edit();
        ET.putString("sp_status", ss);
        ET.commit();

        View header = navigationView.getHeaderView(0);
        TextView text = (TextView) header.findViewById(R.id.textView);
        text.setText(ss);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                getSupportActionBar().setTitle(item.getTitle());
                Fragment_Home hf = new Fragment_Home();
                android.app.FragmentTransaction ft = homepage.this.getFragmentManager().beginTransaction();
                ft.replace(R.id.frame, hf);
                ft.commit();
                Toast.makeText(homepage.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_orders:
                getSupportActionBar().setTitle(item.getTitle());
                Fragment_Previous_Order pf = new Fragment_Previous_Order();
                android.app.FragmentTransaction ft2 = homepage.this.getFragmentManager().beginTransaction();
                ft2.replace(R.id.frame, pf);
                ft2.commit();
                Toast.makeText(homepage.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_update:
                getSupportActionBar().setTitle(item.getTitle());
                Fragment_Update uf = new Fragment_Update();
                android.app.FragmentTransaction ft3 = homepage.this.getFragmentManager().beginTransaction();
                ft3.replace(R.id.frame, uf);
                ft3.commit();
                Toast.makeText(homepage.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_about_us:
                getSupportActionBar().setTitle(item.getTitle());
                return true;

            case R.id.nav_logout:
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor ET = sp.edit();
                ET.putString("sp_status", "No Data");
                ET.putString("sp_name","");
                ET.putString("sp_mobile","");
                ET.putString("sp_email","");
                ET.putString("sp_password","");
                ET.commit();

                Intent back = new Intent(getApplicationContext(),FirstPage.class);
                startActivity(back);
                finish();

                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();

        }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
