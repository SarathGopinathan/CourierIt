package com.shadesix.courierit.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shade Six on 17-07-2017.
 */

public class LoginModel {

    @SerializedName("email")
    public String email;

    @SerializedName("phone")
    public String phone;

    @SerializedName("user_name")
    public String user_name;
}
