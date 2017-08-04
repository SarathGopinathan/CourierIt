package com.shadesix.courierit.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shade Six on 17-07-2017.
 */

public class LoginModel {
    @SerializedName("success")
    public int success;

    @SerializedName("message")
    public String message;

    @SerializedName("auth_key")
    public String authkey;

}
