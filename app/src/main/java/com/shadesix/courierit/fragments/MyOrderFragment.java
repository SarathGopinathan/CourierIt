package com.shadesix.courierit.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.shadesix.courierit.R;
import com.shadesix.courierit.adapters.MyOrderAdapter;

import java.util.ArrayList;

public class MyOrderFragment extends Fragment {
    RecyclerView rv_one,rv_two,rv_three;
    Button btn_one,btn_two,btn_three;
    LinearLayout ll_one,ll_two,ll_three;
    MyOrderAdapter orderAdapter1,orderAdapter2,orderAdapter3;
    ArrayList<String> orderlist = new ArrayList<>();

    public MyOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_order, container, false);

        rv_one = (RecyclerView) rootView.findViewById(R.id.rv_order_one);
        rv_two = (RecyclerView) rootView.findViewById(R.id.rv_order_two);
        rv_three = (RecyclerView) rootView.findViewById(R.id.rv_order_three);
        btn_one = (Button) rootView.findViewById(R.id.btn_order_one);
        btn_two = (Button) rootView.findViewById(R.id.btn_order_two);
        btn_three = (Button) rootView.findViewById(R.id.btn_order_three);
        ll_one = (LinearLayout) rootView.findViewById(R.id.ll_order_one);
        ll_two = (LinearLayout) rootView.findViewById(R.id.ll_order_two);
        ll_three = (LinearLayout) rootView.findViewById(R.id.ll_order_three);

        btn_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_one.setBackgroundColor(Color.parseColor("#3c4648"));
                ll_two.setBackgroundColor(Color.parseColor("#f05045"));
                ll_three.setBackgroundColor(Color.parseColor("#f05045"));


                rv_one.setVisibility(View.VISIBLE);
                rv_two.setVisibility(View.GONE);
                rv_three.setVisibility(View.GONE);
            }
        });

        btn_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_one.setBackgroundColor(Color.parseColor("#f05045"));
                ll_two.setBackgroundColor(Color.parseColor("#3c4648"));
                ll_three.setBackgroundColor(Color.parseColor("#f05045"));


                rv_one.setVisibility(View.GONE);
                rv_two.setVisibility(View.VISIBLE);
                rv_three.setVisibility(View.GONE);
            }
        });

        btn_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_one.setBackgroundColor(Color.parseColor("#f05045"));
                ll_two.setBackgroundColor(Color.parseColor("#f05045"));
                ll_three.setBackgroundColor(Color.parseColor("#3c4648"));

                rv_one.setVisibility(View.GONE);
                rv_two.setVisibility(View.GONE);
                rv_three.setVisibility(View.VISIBLE);
            }
        });

        orderAdapter1 = new MyOrderAdapter(orderlist, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        rv_one.setLayoutManager(mLayoutManager);
        rv_one.setAdapter(orderAdapter1);

        orderAdapter2 = new MyOrderAdapter(orderlist, getActivity());
        RecyclerView.LayoutManager mLayoutManager2 = new GridLayoutManager(getActivity(), 1);
        rv_two.setLayoutManager(mLayoutManager2);
        rv_two.setAdapter(orderAdapter2);

        orderAdapter3 = new MyOrderAdapter(orderlist, getActivity());
        RecyclerView.LayoutManager mLayoutManager3 = new GridLayoutManager(getActivity(), 1);
        rv_three.setLayoutManager(mLayoutManager3);
        rv_three.setAdapter(orderAdapter3);


        return rootView;
    }
}
