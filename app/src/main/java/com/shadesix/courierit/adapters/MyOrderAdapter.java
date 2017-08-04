package com.shadesix.courierit.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shadesix.courierit.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Shade Six on 06-07-2017.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {

    private static final String TAG = MyOrderAdapter.class.getSimpleName();
    private Context context;
    public ArrayList<String> data;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView orderedtext,shippedtext,packagedtext,deliveredtext,price,qty;
        ImageView logo,type;
        RelativeLayout rel;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.orderedtext = (TextView) itemView.findViewById(R.id.txt_ordered);
            this.shippedtext = (TextView) itemView.findViewById(R.id.txt_shipped);
            this.packagedtext = (TextView) itemView.findViewById(R.id.txt_packaged);
            this.deliveredtext = (TextView) itemView.findViewById(R.id.txt_delivered);
            this.price = (TextView) itemView.findViewById(R.id.price_val);
            this.qty = (TextView) itemView.findViewById(R.id.qty_val);
            this.logo = (ImageView) itemView.findViewById(R.id.img_cmpny);
            this.type = (ImageView) itemView.findViewById(R.id.img_type);
            this.rel = (RelativeLayout) itemView.findViewById(R.id.rel);

        }
    }

    public MyOrderAdapter(ArrayList<String> data, Context cont) {
        this.data = data;
        this.context = cont;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_list, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return 5;
    }




}
