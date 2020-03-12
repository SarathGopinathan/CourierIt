package com.shadesix.courierit.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shadesix.courierit.R;
import com.shadesix.courierit.models.UserDetailsModel;
import com.shadesix.courierit.utils.Constant;
import com.shadesix.courierit.utils.Utils;

import cz.msebera.android.httpclient.Header;


public class ProfileFragment extends Fragment {
    EditText fname,lname,phone,mail,address,city,state,zipcode,country;
    String name,firstname;
   // ArrayList<String> ar;
    String[] a = new String[100];
    int i=0,j=0;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_profile, container, false);

        fname=(EditText)rootview.findViewById(R.id.edt_fname);
        lname=(EditText)rootview.findViewById(R.id.edt_lname);
        phone=(EditText)rootview.findViewById(R.id.edt_phone);
        mail=(EditText)rootview.findViewById(R.id.edt_mail);
        address=(EditText)rootview.findViewById(R.id.edt_address);
        city=(EditText)rootview.findViewById(R.id.edt_city);
        state=(EditText)rootview.findViewById(R.id.edt_state);
        zipcode=(EditText)rootview.findViewById(R.id.edt_zipcode);
        country=(EditText)rootview.findViewById(R.id.edt_country);
        firstname="";

        AsyncHttpClient client=new AsyncHttpClient();
        final int DEFAULT_TIMEOUT=20 * 1000;
        RequestParams params=new RequestParams();
        client.setTimeout(DEFAULT_TIMEOUT);
        client.addHeader("auth-token", Utils.getFromUserDefaults(getActivity(), Constant.PARAM_AUTHKEY));
        Log.e("fragment",Utils.getFromUserDefaults(getActivity(), Constant.PARAM_AUTHKEY));
        client.get(Constant.PARAM_BASE_URL+"user",params,new ProfileFragment.GetMyDealsResponsehandler());

//        mail.setText(em);
//        phone.setText(ph);
//        address.setText(ad);
//        city.setText(c);
//        state.setText(st);
//        country.setText(ctry);
//        zipcode.setText(zip);

        return(rootview);
    }

    public class GetMyDealsResponsehandler extends AsyncHttpResponseHandler{

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            UserDetailsModel deals = new Gson().fromJson(new String(responseBody), UserDetailsModel.class);

            if (deals.success == 1) {
                name=deals.user.get(0).user_name;

                for (String retval: name.split(" ")) {
                    a[i]=retval;
                    i++;
                    j=i;
                }

                lname.setText(a[j-1]);

                for(int x=0;x<j;x++){
                    if(a[x].equals(a[j-1]))
                        x=j+1;
                    else
                        firstname=firstname+a[x];
                }

                fname.setText(firstname);

                mail.setText(deals.user.get(0).email);
                phone.setText(deals.user.get(0).phone_no);
                address.setText(deals.user.get(0).addresses.get(0).street_name);
                city.setText(deals.user.get(0).addresses.get(0).city);
                state.setText(deals.user.get(0).addresses.get(0).state);
                country.setText(deals.user.get(0).addresses.get(0).country);
                zipcode.setText(deals.user.get(0).addresses.get(0).pincode);

            }
            else
                Toast.makeText(getActivity(),"done fr life", Toast.LENGTH_LONG).show();
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getActivity(),"Seems like your network connectivity is down or very slow", Toast.LENGTH_LONG).show();
        }
    }
}
