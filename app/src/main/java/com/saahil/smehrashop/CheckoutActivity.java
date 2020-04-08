package com.saahil.smehrashop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.saahil.smehrashop.Models.Order;
import com.saahil.smehrashop.Models.Products;

import java.util.ArrayList;
import java.util.HashMap;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckoutActivity extends AppCompatActivity {
    EditText etFirstName, etLastName, etEmail, etAddress, etCity, etPostalCode;
    Button btnPlaceOrder;
    String firstName, lastName, email, address, city, postalCode;
    JsonPlaceHolderApi jsonPlaceHolderApi;
    ArrayList<Object> orderedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Paper.init(this);

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.29.214:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);

        orderedProducts=new ArrayList<>();
        
        etFirstName=findViewById(R.id.etFirstName);
        etLastName=findViewById(R.id.etLastName);
        etEmail=findViewById(R.id.etEmail);
        etAddress=findViewById(R.id.etAddress);
        etPostalCode=findViewById(R.id.etPostalCode);
        etCity=findViewById(R.id.etCity);
        btnPlaceOrder=findViewById(R.id.btnPlaceOrder);
        
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidations();
            }
        });
    }

    private void checkValidations() {
        firstName=etFirstName.getText().toString().toLowerCase();
        lastName=etLastName.getText().toString().toLowerCase();
        email=etEmail.getText().toString().toLowerCase();
        address=etAddress.getText().toString().toLowerCase();
        city=etCity.getText().toString().toLowerCase();
        postalCode=etPostalCode.getText().toString().toLowerCase();
        
        if(TextUtils.isEmpty(firstName)){
            etFirstName.setError("Required field!");
            return;
        }
        if(TextUtils.isEmpty(lastName)){
            etLastName.setError("Required field!");
            return;
        }
        if(TextUtils.isEmpty(email)){
            etEmail.setError("Required field!");
            return;
        }
        if(TextUtils.isEmpty(address)){
            etAddress.setError("Required field!");
            return;
        }
        if(TextUtils.isEmpty(postalCode)){
            etPostalCode.setError("Required field!");
            return;
        }
        if(TextUtils.isEmpty(city)){
            etCity.setError("Required field!");
            return;
        }
        
        getproducts();
    }

    private void getproducts() {
        ArrayList<Products> cartProducts=new ArrayList<>();
        if(Paper.book().read("cart") != null) {
            cartProducts= Paper.book().read("cart");
        }

        for(Products productInCart : cartProducts){
            HashMap<String, Object> orderedProductsMap=new HashMap<>();
            orderedProductsMap.put("product", productInCart.getId());
            orderedProductsMap.put("price", productInCart.getPrice());
            orderedProductsMap.put("quantity", productInCart.getQuantity());

            orderedProducts.add(orderedProductsMap);
        }

        placeOrder();
    }

    private void placeOrder() {
        HashMap<String, Object> orderMap=new HashMap<>();
        orderMap.put("first_name", firstName);
        orderMap.put("last_name", lastName);
        orderMap.put("email", email);
        orderMap.put("address", address);
        orderMap.put("postal_code", postalCode);
        orderMap.put("city", city);
        orderMap.put("items", orderedProducts);

        Call<Order> call=jsonPlaceHolderApi.placeOrder(orderMap);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if(!response.isSuccessful()){
                    Log.e("error-response code", ""+response.code());
                    return;
                }

                Order order=response.body();
                Log.e("order_id", ""+order.getId());
                Paper.book().delete("cart");
                Toast.makeText(CheckoutActivity.this, "Order successfull", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CheckoutActivity.this, ProductListActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });
    }
}
