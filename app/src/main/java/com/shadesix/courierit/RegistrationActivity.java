package com.shadesix.courierit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shadesix.courierit.models.RegistrationModel2;
import com.shadesix.courierit.utils.Constant;
import com.shadesix.courierit.utils.Utils;

public class RegistrationActivity extends AppCompatActivity {

    EditText fname,lname,email,phone,password,rpassword;
    Button register;
    CheckBox tc;
    boolean termsCondn = false;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        fname = (EditText) findViewById(R.id.edt_fname);
        lname = (EditText) findViewById(R.id.edt_lname);
        email = (EditText) findViewById(R.id.edt_mail);
        phone = (EditText) findViewById(R.id.edt_phone);
        password = (EditText) findViewById(R.id.edt_pass);
        rpassword = (EditText) findViewById(R.id.edt_rpass);
        tc = (CheckBox) findViewById(R.id.chk_terms);
        register = (Button) findViewById(R.id.btn_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String result = validate();
                if(result.equals("validated")) {
                    registerUser();
                }else {
                    showAlertDialog(result);
                }
            }
        });

        tc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    termsCondn = true;
                }else {
                    termsCondn = false;
                }
            }
        });
    }

    public void showAlertDialog(String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(RegistrationActivity.this);
        builder1.setMessage(message);
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

    public String validate(){
        if(fname.getText().toString().isEmpty() && email.getText().toString().isEmpty() && phone.getText().toString().isEmpty()
                && password.getText().toString().isEmpty() && rpassword.getText().toString().isEmpty()
                && lname.getText().toString().isEmpty()){
            return "Enter all fields";
        }else if(phone.getText().toString().length() != 10){
            return "Enter a valid phone number";
        }else if(password.getText().toString().length() < 6){
            return "Password is too short";
        }else if(!password.getText().toString().equals(rpassword.getText().toString())){
            return "Password incorrect";
        }else if(!validateEmail(email.getText().toString())){
            return "Enter valid email ID";
        }else if(!termsCondn){
            return "Terms and Conditions is not accepted";
        }else {
            return "validated";
        }
    }

    public boolean validateEmail(String mail){
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(mail);
        return m.matches();
    }

    public void registerUser(){
        progress = new ProgressDialog(this);
        progress.setMessage("Saving...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        AsyncHttpClient client = new AsyncHttpClient();
        final int DEFAULT_TIMEOUT = 20 * 10000;
        RequestParams params = new RequestParams();
        params.put("username",fname.getText().toString()+" "+lname.getText().toString());
        params.put("phoneno",phone.getText().toString());
        params.put("email",email.getText().toString());
        params.put("password",password.getText().toString());
        params.put("confirmpassword",rpassword.getText().toString());
        client.setTimeout(DEFAULT_TIMEOUT);
        client.post(Constant.PARAM_BASE_URL+"register", params, new RegistrationActivity.GetMyDealsResponsehandler());
    }

    public class GetMyDealsResponsehandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
            RegistrationModel2 deals = new Gson().fromJson(new String(responseBody), RegistrationModel2.class);
            if (deals.success == 1) {

                Utils.saveToUserDefaults(RegistrationActivity.this,Constant.PARAM_USERNAME,fname.getText().toString()+" "+lname.getText().toString());
                Utils.saveToUserDefaults(RegistrationActivity.this,Constant.PARAM_EMAIL,email.getText().toString());
                Utils.saveToUserDefaults(RegistrationActivity.this,Constant.PARAM_PHONE,phone.getText().toString());
                Utils.saveToUserDefaults(RegistrationActivity.this,Constant.PARAM_AUTHKEY,phone.getText().toString());

                Toast.makeText(RegistrationActivity.this,deals.message,Toast.LENGTH_SHORT).show();

                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
            else{
                Toast.makeText(RegistrationActivity.this,deals.message,Toast.LENGTH_SHORT).show();
            }
            progress.dismiss();
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
            Toast.makeText(getBaseContext(),"Seems like your network connectivity is down or very slow", Toast.LENGTH_LONG).show();
            progress.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
        finish();
    }
}
