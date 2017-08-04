package com.shadesix.courierit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.shadesix.courierit.utils.Constant;
import com.shadesix.courierit.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

public class PackageDetailActivity extends AppCompatActivity {

    RelativeLayout choosecourier,rel_date,rel_time;
    EditText to_address,from_address,weight,no_package,declare;
    TextView date,time;
    CheckBox box,envelope,std,express;
    String couriertype = "";
    String priority = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail);

        choosecourier = (RelativeLayout) findViewById(R.id.rel_choose);
        rel_date = (RelativeLayout) findViewById(R.id.rel_date);
        rel_time = (RelativeLayout) findViewById(R.id.rel_time);
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

        from_address.setText(Utils.getFromUserDefaults(PackageDetailActivity.this,Constant.PARAM_FROM_ADDRESS));

        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    couriertype="box";
                }
                else {
                    couriertype = "";
                }
            }
        });

        envelope.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    couriertype="envelope";
                }
                else {
                    couriertype = "";
                }
            }
        });

        std.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    priority="Standard";
                }
                else {
                    priority = "";
                }
            }
        });

        express.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    priority="Express";
                }
                else {
                    priority = "";
                }
            }
        });


        choosecourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()){
                    Utils.saveToUserDefaults(PackageDetailActivity.this, Constant.PARAM_TO_ADDRESS,to_address.getText().toString());
                    Utils.saveToUserDefaults(PackageDetailActivity.this, Constant.PARAM_FROM_ADDRESS,from_address.getText().toString());
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
}
