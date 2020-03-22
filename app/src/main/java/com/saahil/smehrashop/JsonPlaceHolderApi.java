package com.saahil.smehrashop;

import com.saahil.smehrashop.Model.Products;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("api/")
    Call<ArrayList<Products>>getProducts();
}
