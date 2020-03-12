package com.shadesix.courierit;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shadesix.courierit.helpers.GPSTracker;
import com.shadesix.courierit.models.UserAddressModel;
import com.shadesix.courierit.utils.Constant;
import com.shadesix.courierit.utils.Utils;

import java.util.List;
import java.util.Locale;

public class DomesticInternationalActivity extends AppCompatActivity {

    ImageView backArrow;
    String saddress,scity,sstate,szip,scountry;
    Geocoder geocoder;
    List<Address> addresses;
    ProgressDialog progress;
    static final String TAG = DomesticInternationalActivity.class.getSimpleName();
    public static double lat_reg,long_reg;
    GPSTracker gps;
    RelativeLayout domestic,international;
    private static final int LOCATION_PERMISSION_CONSTANT = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domestic_international);

        backArrow = (ImageView) findViewById(R.id.img_back);
        domestic = (RelativeLayout) findViewById(R.id.rel_domestic);
        international = (RelativeLayout) findViewById(R.id.rel_international);
        progress = new ProgressDialog(this);

        locationpermission();

        domestic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Utils.getFromUserDefaults(DomesticInternationalActivity.this,Constant.PARAM_FROM_ADDRESS).isEmpty()){
                    getAddressDetails();

                }else {
                    Utils.saveToUserDefaults(DomesticInternationalActivity.this, Constant.PARAM_TYPE, "Domestic");
                    Intent intent = new Intent(DomesticInternationalActivity.this, PackageDetailActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        international.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.getFromUserDefaults(DomesticInternationalActivity.this,Constant.PARAM_FROM_ADDRESS).isEmpty()){
                    getAddressDetails();
                }else {
                    Utils.saveToUserDefaults(DomesticInternationalActivity.this, Constant.PARAM_TYPE, "International");
                    Intent intent = new Intent(DomesticInternationalActivity.this, PackageDetailActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DomesticInternationalActivity.this,MainActivity.class);
                startActivity(intent);
//                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                finish();
            }
        });
    }

    private void getcurrentlocation() {

    }

    protected void getAddressDetails() {
        // custom dialog
        final Dialog dialog = new Dialog(DomesticInternationalActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.address_alert_dialog);

        Button current = (Button) dialog.findViewById(R.id.btn_location);
        Button save = (Button) dialog.findViewById(R.id.btn_save);
        final EditText address = (EditText) dialog.findViewById(R.id.edt_address);
        final EditText city = (EditText) dialog.findViewById(R.id.edt_city);
        final EditText state = (EditText) dialog.findViewById(R.id.edt_state);
        final EditText zip = (EditText) dialog.findViewById(R.id.edt_zipcode);
        final EditText country = (EditText) dialog.findViewById(R.id.edt_country);
        RelativeLayout relLocation = (RelativeLayout) dialog.findViewById(R.id.rel_current);

        if (ActivityCompat.checkSelfPermission(DomesticInternationalActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(DomesticInternationalActivity.this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            relLocation.setVisibility(View.GONE);
        }else {
            relLocation.setVisibility(View.VISIBLE);
        }

        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gps = new GPSTracker(DomesticInternationalActivity.this);

                if(gps.canGetLocation()){

                    lat_reg = gps.getLatitude();
                    long_reg = gps.getLongitude();
                    geocoder = new Geocoder(DomesticInternationalActivity.this, Locale.getDefault());

                    try {

                        addresses = geocoder.getFromLocation(lat_reg, long_reg, 1);

                        address.setText(addresses.get(0).getAddressLine(0));
                        city.setText(addresses.get(0).getLocality());
                        state.setText(addresses.get(0).getAdminArea());
                        zip.setText(addresses.get(0).getPostalCode());
                        country.setText(addresses.get(0).getCountryName());
                    } catch (Exception e) {
                        Log.e(TAG,"error:"+e);
                    }


                }else{
                    gps.showSettingsAlert();
//           turnGPSOn();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(address.getText().toString().isEmpty() || city.getText().toString().isEmpty() || state.getText().toString().isEmpty()
                        || zip.getText().toString().isEmpty() || country.getText().toString().isEmpty()){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(DomesticInternationalActivity.this);
                    builder1.setMessage("Enter all fields!");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else{

                    saddress=address.getText().toString();
                    scity=city.getText().toString();
                    sstate=state.getText().toString();
                    scountry=country.getText().toString();
                    szip=zip.getText().toString();
                    Utils.saveToUserDefaults(DomesticInternationalActivity.this,Constant.PARAM_FROM_ADDRESS,address.getText().toString());
                    Utils.saveToUserDefaults(DomesticInternationalActivity.this,Constant.PARAM_CITY,city.getText().toString());
                    Utils.saveToUserDefaults(DomesticInternationalActivity.this,Constant.PARAM_STATE,state.getText().toString());
                    Utils.saveToUserDefaults(DomesticInternationalActivity.this,Constant.PARAM_COUNTRY,country.getText().toString());
                    Utils.saveToUserDefaults(DomesticInternationalActivity.this,Constant.PARAM_ZIP_CODE,zip.getText().toString());

                    progress.setMessage("Saving...");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(true);
                    progress.setCancelable(false);
                    progress.show();
                    AsyncHttpClient client = new AsyncHttpClient();
                    final int DEFAULT_TIMEOUT = 20 * 10000;
                    RequestParams params = new RequestParams();
                    params.put("name",Utils.getFromUserDefaults(DomesticInternationalActivity.this,Constant.PARAM_USERNAME));
                    Log.e("DomInter","Authkey"+Utils.getFromUserDefaults(DomesticInternationalActivity.this,Constant.PARAM_AUTHKEY));

                    params.put("address",address.getText().toString());
                    params.put("city",city.getText().toString());
                    params.put("state",state.getText().toString());
                    params.put("country",country.getText().toString());
                    params.put("pincode",zip.getText().toString());
                    client.setTimeout(DEFAULT_TIMEOUT);
                    client.addHeader("auth-token",Utils.getFromUserDefaults(DomesticInternationalActivity.this,Constant.PARAM_AUTHKEY));
//                    client.post("url?name="+Utils.getFromUserDefaults(DomesticInternationalActivity.this,Constant.PARAM_USERNAME)
//                            +"&address="+address.getText().toString()+"&city="+city.getText().toString()+"&country="+country.getText().toString()
//                            +"&state="+state.getText().toString()+"&pincode="+zip.getText().toString(), params, new DomesticInternationalActivity.GetMyDealsResponsehandler());
                    //post_address_values();

                    client.post(Constant.PARAM_BASE_URL+"address",params,new DomesticInternationalActivity.GetMyDealsResponsehandler());
                    dialog.dismiss();
                    startActivity(new Intent(DomesticInternationalActivity.this,PackageDetailActivity.class));
                    finish();
                }
            }
        });

        dialog.show();
    }

    public class GetMyDealsResponsehandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
            UserAddressModel deals = new Gson().fromJson(new String(responseBody), UserAddressModel.class);
            if (deals.success == 1) {

//                Utils.saveToUserDefaults(DomesticInternationalActivity.this,Constant.PARAM_FROM_ADDRESS,saddress);
//                Utils.saveToUserDefaults(DomesticInternationalActivity.this,Constant.PARAM_CITY,scity);
//                Utils.saveToUserDefaults(DomesticInternationalActivity.this,Constant.PARAM_STATE,sstate);
//                Utils.saveToUserDefaults(DomesticInternationalActivity.this,Constant.PARAM_COUNTRY,scountry);
//                Utils.saveToUserDefaults(DomesticInternationalActivity.this,Constant.PARAM_ZIP_CODE,szip);

                Toast.makeText(DomesticInternationalActivity.this,deals.message,Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(DomesticInternationalActivity.this,deals.message,Toast.LENGTH_SHORT).show();
            }
            progress.dismiss();
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getBaseContext(),"Seems like your network connectivity is down or very slow", Toast.LENGTH_LONG).show();
            progress.dismiss();
        }
    }

    private void locationpermission() {

        if (ActivityCompat.checkSelfPermission(DomesticInternationalActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(DomesticInternationalActivity.this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DomesticInternationalActivity.this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DomesticInternationalActivity.this);
                builder.setTitle("Need Location Permission");
                builder.setMessage("Courier It needs Location Permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(DomesticInternationalActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                LOCATION_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            } else {
                //just request the permission
                ActivityCompat.requestPermissions(DomesticInternationalActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CONSTANT);
            }
        } else {
            //You already have the permission, just go ahead.
            turnGPS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                turnGPS();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(DomesticInternationalActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    //Show Information about why you need the permission

                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DomesticInternationalActivity.this);
                    builder.setTitle("Need Location Permission");
                    builder.setMessage("Courier IT needs Location Permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(DomesticInternationalActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CONSTANT);

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(DomesticInternationalActivity.this,MainActivity.class));
        finish();
    }

    public void turnGPS(){
        GPSTracker gps = new GPSTracker(DomesticInternationalActivity.this);

        if(!gps.canGetLocation()){
            gps.showSettingsAlert();
        }
    }
}
