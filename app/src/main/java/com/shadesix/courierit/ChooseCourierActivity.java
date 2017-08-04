package com.shadesix.courierit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shadesix.courierit.adapters.CourierAdapter;
import com.shadesix.courierit.adapters.MyOrderAdapter;
import com.shadesix.courierit.models.CourierCompanyModel;
import com.shadesix.courierit.models.CourierCompanyModel2;
import com.shadesix.courierit.models.RegistrationModel2;
import com.shadesix.courierit.utils.Constant;
import com.shadesix.courierit.utils.Utils;

import java.util.ArrayList;

public class ChooseCourierActivity extends AppCompatActivity {

    RecyclerView rv_courier;
    CourierAdapter courierAdapter;
    ArrayList<CourierCompanyModel> courierList = new ArrayList<>();
    String toaddress,fromaddress,wt,nopkg,dt,tm,dec,couriertype,priority;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_courier);

        rv_courier = (RecyclerView) findViewById(R.id.rv_courier);

        getAllCompanies();
    }

    public void getAllCompanies(){
        AsyncHttpClient client = new AsyncHttpClient();
        final int DEFAULT_TIMEOUT = 20 * 10000;
        RequestParams params = new RequestParams();
        client.setTimeout(DEFAULT_TIMEOUT);
        client.addHeader("auth-token",Utils.getFromUserDefaults(ChooseCourierActivity.this,Constant.PARAM_AUTHKEY));
        client.get(Constant.PARAM_BASE_URL+"companies", params, new ChooseCourierActivity.GetMyDealsResponsehandler());
    }

    public class GetMyDealsResponsehandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
            CourierCompanyModel2 deals = new Gson().fromJson(new String(responseBody), CourierCompanyModel2.class);
            if (deals.success == 1) {

                courierList.clear();

                for (CourierCompanyModel model : deals.companies) {
                    courierList.add(model);
                }

                courierAdapter = new CourierAdapter(courierList, ChooseCourierActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(ChooseCourierActivity.this, 1);
                rv_courier.setLayoutManager(mLayoutManager);
                rv_courier.setAdapter(courierAdapter);

            }
            else{
                Toast.makeText(ChooseCourierActivity.this,deals.message,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getBaseContext(),"Seems like your network connectivity is down or very slow", Toast.LENGTH_LONG).show();
        }
    }
}
