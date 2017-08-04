package com.shadesix.courierit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shadesix.courierit.utils.Constant;
import com.shadesix.courierit.utils.Utils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RelativeLayout send,track;
    //ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        send = (RelativeLayout) findViewById(R.id.rel_send);
        track = (RelativeLayout) findViewById(R.id.rel_track);

       // logo = (ImageView) findViewById(R.id.imageView3);

//        logo.getDrawable().setColorFilter(getResources().getColor(R.color.colorBlue), PorterDuff.Mode.SRC_ATOP);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DomesticInternationalActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        LinearLayout profilell = (LinearLayout) header.findViewById(R.id.ll_profile);
        LinearLayout signout = (LinearLayout) header.findViewById(R.id.ll_settings);

        TextView txt_name = (TextView) header.findViewById(R.id.txt_name);
        TextView txt_email = (TextView) header.findViewById(R.id.txt_email);

        if(Constant.PARAM_USERNAME.toString().isEmpty()){

        }
        else{
            txt_name.setText(Utils.getFromUserDefaults(MainActivity.this,Constant.PARAM_USERNAME.toString()));
        }
        if(Constant.PARAM_EMAIL.toString().isEmpty()){

        }
        else{
            txt_email.setText(Utils.getFromUserDefaults(MainActivity.this,Constant.PARAM_EMAIL.toString()));
        }

        final TextView profile = (TextView) header.findViewById(R.id.txt_profile);
        final ImageView profileimg = (ImageView) header.findViewById(R.id.img_profile);

        profilell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                profileimg.getDrawable().setColorFilter(getResources().getColor(R.color.selectedColor), PorterDuff.Mode.SRC_ATOP);
//                profile.setTextColor(Color.parseColor("#ffffff"));
                startActivity(new Intent(MainActivity.this,UserDetailActivity.class));
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert();
            }
        });
    }

    private void alert() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("Are you sure you want to logout?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        logoutuser();
                        dialog.cancel();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
                    }
                });
        builder1.setNegativeButton(
                "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    private void logoutuser() {

        Utils.saveToUserDefaults(MainActivity.this, Constant.PARAM_TO_ADDRESS,"");
        Utils.saveToUserDefaults(MainActivity.this, Constant.PARAM_FROM_ADDRESS,"");
        Utils.saveToUserDefaults(MainActivity.this, Constant.PARAM_WEIGHT,"");
        Utils.saveToUserDefaults(MainActivity.this, Constant.PARAM_NO_PACKAGE,"");
        Utils.saveToUserDefaults(MainActivity.this, Constant.PARAM_DATE,"");
        Utils.saveToUserDefaults(MainActivity.this, Constant.PARAM_TIME,"");
        Utils.saveToUserDefaults(MainActivity.this, Constant.PARAM_DECLARATION,"");
        Utils.saveToUserDefaults(MainActivity.this, Constant.PARAM_COURIER_TYPE,"");
        Utils.saveToUserDefaults(MainActivity.this, Constant.PARAM_PRIORITY,"");
        Utils.saveToUserDefaults(MainActivity.this, Constant.PARAM_USERNAME,"");
        Utils.saveToUserDefaults(MainActivity.this, Constant.PARAM_EMAIL,"");
        Utils.saveToUserDefaults(MainActivity.this, Constant.PARAM_PHONE,"");
        Utils.saveToUserDefaults(MainActivity.this, Constant.PARAM_AUTHKEY,"");
        Utils.saveToUserDefaults(MainActivity.this, Constant.PARAM_STATE,"");
        Utils.saveToUserDefaults(MainActivity.this, Constant.PARAM_CITY,"");
        Utils.saveToUserDefaults(MainActivity.this, Constant.PARAM_COUNTRY,"");
        Utils.saveToUserDefaults(MainActivity.this, Constant.PARAM_ZIP_CODE,"");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
