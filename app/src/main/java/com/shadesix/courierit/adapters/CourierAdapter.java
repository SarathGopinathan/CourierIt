package com.shadesix.courierit.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shadesix.courierit.ProceedPaymentActivity;
import com.shadesix.courierit.R;
import com.shadesix.courierit.models.CourierCompanyModel;
import com.shadesix.courierit.utils.Constant;
import com.shadesix.courierit.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Shade Six on 07-07-2017.
 */

public class CourierAdapter extends RecyclerView.Adapter<CourierAdapter.MyViewHolder> {

    private static final String TAG = MyOrderAdapter.class.getSimpleName();
    private Context context;
    public ArrayList<CourierCompanyModel> data;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView compname,compcost;
        ImageView logo,one,two,three,four,five;
        Button courierit;
        RelativeLayout rel;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.compname = (TextView) itemView.findViewById(R.id.txt_name);
            this.compcost = (TextView) itemView.findViewById(R.id.txt_cost);
            this.logo = (ImageView) itemView.findViewById(R.id.img_logo);
            this.one = (ImageView) itemView.findViewById(R.id.img_one);
            this.two = (ImageView) itemView.findViewById(R.id.img_two);
            this.three = (ImageView) itemView.findViewById(R.id.img_three);
            this.four = (ImageView) itemView.findViewById(R.id.img_four);
            this.five = (ImageView) itemView.findViewById(R.id.img_five);
            this.courierit = (Button) itemView.findViewById(R.id.btn_cit);
            this.rel = (RelativeLayout) itemView.findViewById(R.id.rel);

        }
    }

    public CourierAdapter(ArrayList<CourierCompanyModel> data, Context cont) {
        this.data = data;
        this.context = cont;
    }

    @Override
    public CourierAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courier_list, parent, false);

        CourierAdapter.MyViewHolder myViewHolder = new CourierAdapter.MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final CourierAdapter.MyViewHolder holder, final int listPosition) {

        holder.compname.setText(data.get(listPosition).company);
        holder.courierit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.saveToUserDefaults(context, Constant.PARAM_COURIER_ID,data.get(listPosition).id);
                Intent intent = new Intent(context, ProceedPaymentActivity.class);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }




}
