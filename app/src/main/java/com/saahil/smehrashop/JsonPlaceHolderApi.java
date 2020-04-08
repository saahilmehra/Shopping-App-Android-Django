package com.saahil.smehrashop;

import com.saahil.smehrashop.Models.Order;
import com.saahil.smehrashop.Models.Products;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("api/")
    Call<ArrayList<Products>> getProducts();

    @GET("api/{id}")
    Call<Products> getProduct(@Path("id") int id);

    @POST("orders/api/create/")
    Call<Order> placeOrder(@Body HashMap orderMap);
}
