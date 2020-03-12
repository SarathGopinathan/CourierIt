package com.shadesix.courierit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shadesix.courierit.models.LoginModel2;
import com.shadesix.courierit.utils.Constant;
import com.shadesix.courierit.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    Button signin,signup;
    ProgressDialog progress;
    EditText username,password;
    TextView forgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.edt_username);
        password = (EditText) findViewById(R.id.edt_pass);
        signin = (Button) findViewById(R.id.btn_sign_in);
        signup = (Button) findViewById(R.id.btn_sign_up);
        forgot = (TextView) findViewById(R.id.txt_forgot);

        if(!Utils.getFromUserDefaults(LoginActivity.this,Constant.PARAM_AUTHKEY).isEmpty()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = validate();
                if(result.equals("validated")) {
                    checkUser();
                }else {
                    showAlertDialog(result);
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                finish();
            }
        });
    }

    public String validate(){
        if(username.getText().toString().isEmpty() && password.getText().toString().isEmpty()){
            return "Enter all fields";
        }else {
            return "validated";
        }
    }
    public void checkUser(){
        progress = new ProgressDialog(this);
        progress.setMessage("Checking...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        AsyncHttpClient client = new AsyncHttpClient();
        final int DEFAULT_TIMEOUT = 20 * 10000;
        RequestParams params = new RequestParams();
        params.put("username",username.getText().toString());
        params.put("password",password.getText().toString());
        client.setTimeout(DEFAULT_TIMEOUT);
        client.addHeader("auth-token",Utils.getFromUserDefaults(LoginActivity.this,Constant.PARAM_AUTHKEY));
        client.post(Constant.PARAM_BASE_URL+"login", params, new LoginActivity.GetMyDealsResponsehandler());
    }
    public void showAlertDialog(String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
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
    public class GetMyDealsResponsehandler extends AsyncHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
            LoginModel2 deals = new Gson().fromJson(new String(responseBody), LoginModel2.class);
            if (deals.success == 1) {

                Log.e("Login","Authkey "+deals.authkey);
                Utils.saveToUserDefaults(LoginActivity.this,Constant.PARAM_AUTHKEY,deals.authkey);
                Utils.saveToUserDefaults(LoginActivity.this,Constant.PARAM_USERNAME,deals.userdetail.user_name);
                Utils.saveToUserDefaults(LoginActivity.this,Constant.PARAM_EMAIL,deals.userdetail.email);
                Utils.saveToUserDefaults(LoginActivity.this,Constant.PARAM_PHONE,deals.userdetail.phone);

                Toast.makeText(LoginActivity.this,deals.message,Toast.LENGTH_SHORT).show();

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
            else{
                Toast.makeText(LoginActivity.this,deals.message,Toast.LENGTH_SHORT).show();
            }
            progress.dismiss();
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getBaseContext(),"Seems like your network connectivity is down or very slow", Toast.LENGTH_LONG).show();
            progress.dismiss();
        }
    }

}
