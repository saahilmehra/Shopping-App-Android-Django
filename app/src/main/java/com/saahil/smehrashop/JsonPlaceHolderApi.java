package com.saahil.smehrashop;

import com.saahil.smehrashop.Model.Products;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("api/")
    Call<ArrayList<Products>> getProducts();

    @GET("api/{id}")
    Call<Products> getProduct(@Path("id") int id);
}
