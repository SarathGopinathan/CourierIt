package com.shadesix.courierit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class OverviewActivity extends AppCompatActivity {

    ImageView img_stand;
    EditText txt_name,my_addr,my_city_state,txt_to_name,my_to_addr,my_to_city_state,edt_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_2);


    }

}
