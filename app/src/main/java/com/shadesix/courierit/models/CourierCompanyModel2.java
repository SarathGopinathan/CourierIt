package com.shadesix.courierit.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shade Six on 19-07-2017.
 */

public class CourierCompanyModel2 {
    @SerializedName("message")
    public String message;

    @SerializedName("success")
    public int success;

    @SerializedName("companies")
    public ArrayList<CourierCompanyModel> companies;
}
