package com.shadesix.courierit.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shadesix.courierit.ChooseCourierActivity;
import com.shadesix.courierit.R;
import com.shadesix.courierit.adapters.CourierAdapter;
import com.shadesix.courierit.models.CourierCompanyModel;
import com.shadesix.courierit.models.CourierCompanyModel2;
import com.shadesix.courierit.utils.Constant;
import com.shadesix.courierit.utils.Utils;


public class OverviewFragment extends Fragment {

    ImageView image;
    TextView courierType;
    EditText fromname,fromaddress,myregion,toname,toaddress,toregion,edt_username;
    Button save,fromedit,toedit;
    ViewPager viewPager;
    ProgressDialog progress;
    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_overview, container, false);
        image = (ImageView) rootview.findViewById(R.id.img_stand);
        courierType = (TextView) rootview.findViewById(R.id.txt_std);
        fromname = (EditText) rootview.findViewById(R.id.txt_name);
        fromaddress = (EditText) rootview.findViewById(R.id.my_addr);
        myregion = (EditText) rootview.findViewById(R.id.my_city_state);
        toname = (EditText) rootview.findViewById(R.id.txt_to_name);
        toaddress = (EditText) rootview.findViewById(R.id.my_to_addr);
        toregion = (EditText) rootview.findViewById(R.id.my_to_city_state);
        fromedit = (Button) rootview.findViewById(R.id.fromedit);
        toedit = (Button) rootview.findViewById(R.id.toedit);

        save = (Button) rootview.findViewById(R.id.btn_save);
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager1);

        courierType.setText(Utils.getFromUserDefaults(getActivity(), Constant.PARAM_PRIORITY));
        fromname.setText(Utils.getFromUserDefaults(getActivity(),Constant.PARAM_USERNAME));
        fromaddress.setText(Utils.getFromUserDefaults(getActivity(),Constant.PARAM_FROM_ADDRESS));
        myregion.setText(Utils.getFromUserDefaults(getActivity(),Constant.PARAM_CITY)+", "+
                Utils.getFromUserDefaults(getActivity(),Constant.PARAM_STATE) +", "+
                Utils.getFromUserDefaults(getActivity(),Constant.PARAM_ZIP_CODE));

        toaddress.setText(Utils.getFromUserDefaults(getActivity(),Constant.PARAM_TO_ADDRESS));

        if(Utils.getFromUserDefaults(getActivity(),Constant.PARAM_PRIORITY).equals("standard")){
            image.setImageResource(R.drawable.stand);
        }else {
            image.setImageResource(R.drawable.express);
            image.getDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCourier();
            }
        });

        fromedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglefromEdit();
            }
        });
        toedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleToEdit();
            }
        });

        fromname.setFocusable(false);
        fromaddress.setFocusable(false);
        myregion.setFocusable(false);
        toname.setFocusable(false);
        toaddress.setFocusable(false);
        toregion.setFocusable(false);



        return  rootview;
    }

    private void toggleToEdit() {
        toname.setFocusableInTouchMode(true);
        toname.setFocusable(true);
        toaddress.setFocusableInTouchMode(true);
        toaddress.setFocusable(true);
        toregion.setFocusableInTouchMode(true);
        toregion.setFocusable(true);
    }

    private void togglefromEdit() {

        fromname.setFocusableInTouchMode(true);
        fromname.setFocusable(true);
        fromaddress.setFocusableInTouchMode(true);
        fromaddress.setFocusable(true);
        myregion.setFocusableInTouchMode(true);
        myregion.setFocusable(true);
    }


    public void saveCourier(){
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Saving...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        AsyncHttpClient client = new AsyncHttpClient();
        final int DEFAULT_TIMEOUT = 20 * 10000;
        RequestParams params = new RequestParams();
        params.put("courier_type",Utils.getFromUserDefaults(getActivity(),Constant.PARAM_TYPE));
        params.put("courier_company",Utils.getFromUserDefaults(getActivity(),Constant.PARAM_COURIER_ID));
        params.put("to_address",Utils.getFromUserDefaults(getActivity(),Constant.PARAM_TO_ADDRESS));
        params.put("from_address",Utils.getFromUserDefaults(getActivity(),Constant.PARAM_FROM_ADDRESS));
        params.put("package_type",Utils.getFromUserDefaults(getActivity(),Constant.PARAM_COURIER_TYPE));
        params.put("priority",Utils.getFromUserDefaults(getActivity(),Constant.PARAM_PRIORITY));
        params.put("approx_weight",Utils.getFromUserDefaults(getActivity(),Constant.PARAM_WEIGHT));
        params.put("no_of_package",Utils.getFromUserDefaults(getActivity(),Constant.PARAM_NO_PACKAGE));
        params.put("pick_up_date",Utils.getFromUserDefaults(getActivity(),Constant.PARAM_DATE));
        params.put("pick_up_time",Utils.getFromUserDefaults(getActivity(),Constant.PARAM_TIME));
        params.put("declaration",Utils.getFromUserDefaults(getActivity(),Constant.PARAM_DECLARATION));
        client.setTimeout(DEFAULT_TIMEOUT);
        client.addHeader("auth-token",Utils.getFromUserDefaults(getActivity(),Constant.PARAM_AUTHKEY));
        client.post(Constant.PARAM_BASE_URL+"courier", params, new OverviewFragment.GetMyDealsResponsehandler());
    }

    public class GetMyDealsResponsehandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
            CourierCompanyModel2 deals = new Gson().fromJson(new String(responseBody), CourierCompanyModel2.class);
            if (deals.success == 1) {
                viewPager.setCurrentItem(1);
            }
            else{
                Toast.makeText(getActivity(),deals.message,Toast.LENGTH_SHORT).show();
            }
            progress.dismiss();
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getActivity(),"Seems like your network connectivity is down or very slow", Toast.LENGTH_LONG).show();
            progress.dismiss();
        }
    }
}
