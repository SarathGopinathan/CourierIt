package com.shadesix.courierit.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shadesix.courierit.R;

import java.util.Calendar;
import java.util.Date;

public class ShippingFragment extends Fragment {

    RelativeLayout month,day;
    TextView month_txt,day_txt;
    Button save;
    ViewPager viewPager;
    public ShippingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_shipping, container, false);
        month = (RelativeLayout) rootview.findViewById(R.id.rel_month);
        day = (RelativeLayout) rootview.findViewById(R.id.rel_day);
        month_txt = (TextView) rootview.findViewById(R.id.txt_month);
        day_txt = (TextView) rootview.findViewById(R.id.txt_day);
        save = (Button) rootview.findViewById(R.id.btn_save);
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager1);

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });

        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });

        return rootview;
    }

    public void selectDate(){
        final Calendar c = Calendar.getInstance();
        int year_calender = c.get(Calendar.YEAR);
        int month_calender = c.get(Calendar.MONTH);
        int day_calender = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        month_txt.setText(""+monthOfYear);
                        day_txt.setText(""+dayOfMonth);

                    }
                }, year_calender, month_calender, day_calender);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - 10000);
        datePickerDialog.show();
    }
}
