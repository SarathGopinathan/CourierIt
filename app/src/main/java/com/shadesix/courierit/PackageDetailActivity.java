package com.shadesix.courierit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shadesix.courierit.models.UserAddressModel;
import com.shadesix.courierit.utils.Constant;
import com.shadesix.courierit.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PackageDetailActivity extends AppCompatActivity {

    RelativeLayout choosecourier,rel_date,rel_time,rl,rel_from;
    EditText to_address,from_address,weight,no_package,declare;
    TextView date,time;
    ImageView back_button;
    String from_address_final;
    String saddress,scity,sstate,szip,scountry;
    CheckBox box,envelope,std,express;
    String couriertype = "";
    String[] a = new String[100];
    String priority = "";
    String to_add_final,to_city_final;
    String from_add_final,from_city_final,from_state_final,from_country_final,from_zip_final;
    String address_alone,retval;
    ProgressDialog progress;
    ArrayList<String> ar;
    int i,j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail);

        choosecourier = (RelativeLayout) findViewById(R.id.rel_choose);
        rel_date = (RelativeLayout) findViewById(R.id.rel_date);
        rel_time = (RelativeLayout) findViewById(R.id.rel_time);
        rl = (RelativeLayout) findViewById(R.id.rl);
        rel_from = (RelativeLayout) findViewById(R.id.rel_from);
        back_button=(ImageView)findViewById(R.id.pd_back);
        to_address=(EditText)findViewById(R.id.et_to_address);
        from_address=(EditText)findViewById(R.id.et_from_address);
        weight=(EditText)findViewById(R.id.et_weight);
        no_package=(EditText)findViewById(R.id.edt_no_package);
        date=(TextView) findViewById(R.id.edt_date);
        time=(TextView) findViewById(R.id.edt_time);
        declare=(EditText)findViewById(R.id.edt_declare);
        box=(CheckBox)findViewById(R.id.chk_box);
        envelope=(CheckBox)findViewById(R.id.chk_envelope);
        std=(CheckBox)findViewById(R.id.chk_std);
        express=(CheckBox)findViewById(R.id.chk_express);
        i=0;
        j=0;
        address_alone="";
        to_add_final="";
        to_city_final="";
        from_address_final="";
     //   retval="";
    //    fad="";

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdateval();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gettimeval();
            }
        });

        to_address.setText(Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_TO_NAME)+" "+Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_TO_ADDRESS)+" "+Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_TO_CITY)
                +" "+Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_TO_STATE)+" "+Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_TO_COUNTRY)
                +" "+Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_TO_ZIP_CODE));

        to_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gettoaddressdetails();
            }
        });

        from_address.setText(Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_FROM_ADDRESS)+" "+Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_CITY)
                +" "+Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_STATE)+" "+Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_COUNTRY)
                +" "+Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_ZIP_CODE));


        from_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirm_from_address();
            }
        });


        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    couriertype="box";
                    envelope.setVisibility(View.INVISIBLE);
                }
                else {
                    couriertype = "";
                    envelope.setVisibility(View.VISIBLE);
                }
            }
        });

        envelope.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    couriertype="envelope";
                    box.setVisibility(View.INVISIBLE);
                }
                else {
                    couriertype = "";
                    box.setVisibility(View.VISIBLE);
                }
            }
        });

        std.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    priority="Standard";
                    express.setVisibility(View.INVISIBLE);
                }
                else {
                    priority = "";
                    express.setVisibility(View.VISIBLE);
                }
            }
        });

        express.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    priority="Express";
                    std.setVisibility(View.INVISIBLE);
                }
                else {
                    priority = "";
                    std.setVisibility(View.VISIBLE);
                }
            }
        });


        choosecourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()){

//                    Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_FROM_ADDRESS,Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_FROM_ADDRESS));
//                    Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_CITY,Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_CITY));
//                    Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_TO_ADDRESS,Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_TO_ADDRESS));
//                    Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_TO_CITY,Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_TO_CITY));
                    Utils.saveToUserDefaults(PackageDetailActivity.this, Constant.PARAM_WEIGHT,weight.getText().toString());
                    Utils.saveToUserDefaults(PackageDetailActivity.this, Constant.PARAM_NO_PACKAGE,no_package.getText().toString());
                    Utils.saveToUserDefaults(PackageDetailActivity.this, Constant.PARAM_DATE,date.getText().toString());
                    Utils.saveToUserDefaults(PackageDetailActivity.this, Constant.PARAM_TIME,time.getText().toString());
                    Utils.saveToUserDefaults(PackageDetailActivity.this, Constant.PARAM_DECLARATION,declare.getText().toString());
                    Utils.saveToUserDefaults(PackageDetailActivity.this, Constant.PARAM_COURIER_TYPE,couriertype);
                    Utils.saveToUserDefaults(PackageDetailActivity.this, Constant.PARAM_PRIORITY,priority);

                    startActivity(new Intent(PackageDetailActivity.this,ChooseCourierActivity.class));
                }
                else{
                    setAlert();
                }


            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PackageDetailActivity.this,DomesticInternationalActivity.class));
                finish();
            }
        });

    }

    private void confirm_from_address() {

        final Dialog dialog = new Dialog(PackageDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_from_address_alert_dialog);

        final ListView address_option=(ListView)dialog.findViewById(R.id.address_options);
        Button save = (Button) dialog.findViewById(R.id.btn_save);
        final EditText et_from_address = (EditText) dialog.findViewById(R.id.et_from_address);
        final EditText address = (EditText) dialog.findViewById(R.id.edt_address);
        final EditText city = (EditText) dialog.findViewById(R.id.edt_city);
        final EditText state = (EditText) dialog.findViewById(R.id.edt_state);
        final EditText zip = (EditText) dialog.findViewById(R.id.edt_zipcode);
        final EditText country = (EditText) dialog.findViewById(R.id.edt_country);

        progress = new ProgressDialog(this);
        progress.setMessage("Checking...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        AsyncHttpClient client = new AsyncHttpClient();
        final int DEFAULT_TIMEOUT = 20 * 10000;
        RequestParams params = new RequestParams();
        client.setTimeout(DEFAULT_TIMEOUT);
        client.addHeader("auth-token",Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_AUTHKEY));
        client.get(Constant.PARAM_BASE_URL+"address",params, new PackageDetailActivity.GetMyDealsResponsehandler(address_option));


        et_from_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address_option.setVisibility(View.VISIBLE);
            }
        });

        address_option.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                from_address_final=address_option.getItemAtPosition(i).toString();
                et_from_address.setText(from_address_final);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((et_from_address.getText().toString().isEmpty()) && (address.getText().toString().isEmpty() || city.getText().toString().isEmpty() || state.getText().toString().isEmpty()
                        || zip.getText().toString().isEmpty() || country.getText().toString().isEmpty())){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(PackageDetailActivity.this);
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
                    if(et_from_address.getText().toString().isEmpty()){

                        from_add_final=address.getText().toString();
                        from_city_final=city.getText().toString();
                        from_state_final=state.getText().toString();
                        from_country_final=country.getText().toString();
                        from_zip_final=zip.getText().toString();
                        Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_FROM_ADDRESS,address.getText().toString());
                        Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_CITY,city.getText().toString());
                        Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_STATE,state.getText().toString());
                        Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_COUNTRY,country.getText().toString());
                        Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_ZIP_CODE,zip.getText().toString());

                        progress.setMessage("Saving...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                        progress.setCancelable(false);
                        progress.show();
                        AsyncHttpClient client = new AsyncHttpClient();
                        final int DEFAULT_TIMEOUT = 20 * 10000;
                        RequestParams params = new RequestParams();
                        params.put("name",Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_USERNAME));

                        saddress=address.getText().toString();
                        scity=city.getText().toString();
                        sstate=state.getText().toString();
                        scountry=country.getText().toString();
                        szip=zip.getText().toString();

                        params.put("address",address.getText().toString());
                        params.put("city",city.getText().toString());
                        params.put("state",state.getText().toString());
                        params.put("country",country.getText().toString());
                        params.put("pincode",zip.getText().toString());
                        client.setTimeout(DEFAULT_TIMEOUT);
                        client.addHeader("auth-token",Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_AUTHKEY));
//                    client.post("url?name="+Utils.getFromUserDefaults(DomesticInternationalActivity.this,Constant.PARAM_USERNAME)
//                            +"&address="+address.getText().toString()+"&city="+city.getText().toString()+"&country="+country.getText().toString()
//                            +"&state="+state.getText().toString()+"&pincode="+zip.getText().toString(), params, new DomesticInternationalActivity.GetMyDealsResponsehandler());
                        //post_address_values();

                        client.post(Constant.PARAM_BASE_URL+"address",params,new PackageDetailActivity.GetMyDealsResponsehandler2());
                        dialog.dismiss();
                        startActivity(new Intent(PackageDetailActivity.this,PackageDetailActivity.class));
                        finish();


                    }
                    else if (!(et_from_address.getText().toString().isEmpty())&& ! (address.getText().toString().isEmpty() || city.getText().toString().isEmpty() || state.getText().toString().isEmpty()
                            || zip.getText().toString().isEmpty() || country.getText().toString().isEmpty())){
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(PackageDetailActivity.this);
                        builder1.setMessage("Choose any one!");
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
                    else if(!from_address_final.isEmpty()){
//                          save listview selected item to shared pref
                        for (String retval: from_address_final.split(",")) {
                            a[i]=retval;
                            i++;
                            j=i;
                        }
                        Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_COUNTRY,"");
                        from_country_final=a[j-2];
                        for(int x=j;x>=0;x--){
                              if(x==j-1){
                                  from_zip_final=a[x];
                                  Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_ZIP_CODE,a[x]);
                              }
                              else if(x==j-2){
                                  Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_COUNTRY,from_country_final);
                              }
                              else if(x==j-3){
                                  from_state_final=a[x];
                                  Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_STATE,a[x]);
                              }
                              else if(x==j-4){
                                  from_city_final=a[x];
                                  Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_CITY,a[x]);
                              }
                        }
                        for(int z=0;z<j;z++){
                            if(a[z].equals(from_city_final))
                                z=j+1;
                            else
                                address_alone=address_alone+a[z];
                        }
                     //   address_alone = address_alone.replace(city_final, "");
                        from_add_final=address_alone;
                        Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_FROM_ADDRESS,address_alone);
                    }
                    startActivity(new Intent(PackageDetailActivity.this,PackageDetailActivity.class));
                    dialog.cancel();
                }
            }
        });

        dialog.show();
    }
    public class GetMyDealsResponsehandler2 extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
            UserAddressModel deals = new Gson().fromJson(new String(responseBody), UserAddressModel.class);
            if (deals.success == 1) {

                Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_FROM_ADDRESS,saddress);
                Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_FROM_ADDRESS,scity);
                Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_FROM_ADDRESS,sstate);
                Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_FROM_ADDRESS,scountry);
                Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_FROM_ADDRESS,szip);

                Toast.makeText(PackageDetailActivity.this,deals.message,Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(PackageDetailActivity.this,deals.message,Toast.LENGTH_SHORT).show();
            }
            progress.dismiss();
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getBaseContext(),"Seems like your network connectivity is down or very slow", Toast.LENGTH_LONG).show();
            progress.dismiss();
        }
    }
    class GetMyDealsResponsehandler extends AsyncHttpResponseHandler {

        ListView listView;
        public GetMyDealsResponsehandler(ListView listView){
            this.listView = listView;
        }
        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
            final UserAddressModel deals = new Gson().fromJson(new String(responseBody), UserAddressModel.class);
            if (deals.success == 1) {
                ar=new ArrayList<String>();
                    for(int i=0;i<deals.addresses.size();i++){
                        ar.add(deals.addresses.get(i).street_name+","+deals.addresses.get(i).city+","+deals.addresses.get(i).state+","+deals.addresses.get(i).country+","+deals.addresses.get(i).pincode);
                    }
                ArrayAdapter adapter=new ArrayAdapter<String>(PackageDetailActivity.this,R.layout.my_text_view,ar);
                listView.setAdapter(adapter);
                Utils.setListViewHeightBasedOnChildren(listView);
                Toast.makeText(PackageDetailActivity.this,deals.message,Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(PackageDetailActivity.this,deals.message,Toast.LENGTH_SHORT).show();
            }
            progress.dismiss();
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getBaseContext(),"Seems like your network connectivity is down or very slow", Toast.LENGTH_LONG).show();
            progress.dismiss();
        }
    }

    private void gettoaddressdetails() {
        // custom dialog
        final Dialog dialog = new Dialog(PackageDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.to_address_alert_dialog);

        Button save = (Button) dialog.findViewById(R.id.btn_save);
        final EditText address = (EditText) dialog.findViewById(R.id.edt_address);
        final EditText edt_fname = (EditText) dialog.findViewById(R.id.edt_fname);
        final EditText city = (EditText) dialog.findViewById(R.id.edt_city);
        final EditText state = (EditText) dialog.findViewById(R.id.edt_state);
        final EditText zip = (EditText) dialog.findViewById(R.id.edt_zipcode);
        final EditText country = (EditText) dialog.findViewById(R.id.edt_country);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(address.getText().toString().isEmpty() || edt_fname.getText().toString().isEmpty() || city.getText().toString().isEmpty() || state.getText().toString().isEmpty()
                        || zip.getText().toString().isEmpty() || country.getText().toString().isEmpty()){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(PackageDetailActivity.this);
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

                    to_add_final=address.getText().toString();
                    to_city_final=city.getText().toString();

                    Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_TO_ADDRESS,address.getText().toString());
                    Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_TO_NAME,edt_fname.getText().toString());
                    Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_TO_CITY,city.getText().toString());
                    Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_TO_STATE,state.getText().toString());
                    Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_TO_COUNTRY,country.getText().toString());
                    Utils.saveToUserDefaults(PackageDetailActivity.this,Constant.PARAM_TO_ZIP_CODE,zip.getText().toString());

                    startActivity(new Intent(PackageDetailActivity.this,PackageDetailActivity.class));
                    dialog.cancel();
                }
            }
        });

        dialog.show();
    }

    private void setAlert() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(PackageDetailActivity.this);
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

    private boolean validate() {

        if(!to_address.getText().toString().isEmpty() && !from_address.getText().toString().isEmpty() &&
                !weight.getText().toString().isEmpty() && !no_package.getText().toString().isEmpty() &&
                !date.getText().toString().isEmpty() && !time.getText().toString().isEmpty() && !couriertype.isEmpty()
                && !priority.isEmpty()){
            return true;
        }
        else
        {
            return false;
        }

    }

    private void gettimeval() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timepickerdialog=new TimePickerDialog(PackageDetailActivity.this,R.style.DialogTheme,
                new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hr, int min) {
                        time.setText(String.format("%02d:%02d:%02d", hr, min,00));
                    }
                }, hour, minute,false);
       // TimePickerDialog.getDatePicker().setMinDate(new Time().getTime() - 10000);
        timepickerdialog.show();
        }

    private void getdateval() {
            final Calendar c = Calendar.getInstance();
            int year_calender = c.get(Calendar.YEAR);
            int month_calender = c.get(Calendar.MONTH);
            int day_calender = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(PackageDetailActivity.this, R.style.DialogTheme,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            date.setText(String.format("%04d-%02d-%02d", year, monthOfYear,dayOfMonth));

                        }
                    }, year_calender, month_calender, day_calender);
            datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - 10000);
            datePickerDialog.show();
        }

//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        selectedText = adapterView.getItemAtPosition(i).toString();
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }

}








