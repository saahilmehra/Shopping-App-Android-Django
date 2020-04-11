package com.saahil.smehrashop;

import com.saahil.smehrashop.Models.Order;
import com.saahil.smehrashop.Models.PaymentStatus;
import com.saahil.smehrashop.Models.Products;
import com.saahil.smehrashop.Models.Token;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("api/")
    Call<ArrayList<Products>> getProducts();

    @GET("api/{id}")
    Call<Products> getProduct(@Path("id") int id);

    @POST("orders/api/create/")
    Call<Order> placeOrder(@Body HashMap orderMap);

    @GET("payment/api/client_token/")
    Call<Token> getClientToken();

    @PUT("payment/api/process/{id}/")
    Call<PaymentStatus> paymentProcess(@Path("id") int id, @Body HashMap nonceMap);
}
