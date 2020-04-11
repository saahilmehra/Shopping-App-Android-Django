package com.saahil.smehrashop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.saahil.smehrashop.Models.Order;
import com.saahil.smehrashop.Models.PaymentStatus;
import com.saahil.smehrashop.Models.Products;
import com.saahil.smehrashop.Models.Token;

import java.util.ArrayList;
import java.util.HashMap;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckoutActivity extends AppCompatActivity {
    TextView tvMessage, tvCheckout;
    EditText etFirstName, etLastName, etEmail, etAddress, etCity, etPostalCode;
    Button btnPlaceOrder;
    String firstName, lastName, email, address, city, postalCode;
    JsonPlaceHolderApi jsonPlaceHolderApi;
    ArrayList<Object> orderedProducts;
    int REQUEST_CODE=5;
    String client_token="", nonce="";
    int order_id;

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

        //sendPaymentData();

        orderedProducts=new ArrayList<>();

        tvMessage=findViewById(R.id.tvMessage);
        tvCheckout=findViewById(R.id.tvCheckout);
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
                order_id=Integer.parseInt(order.getId());
                Log.e("order_id", ""+order.getId());
                Paper.book().delete("cart");
                Toast.makeText(CheckoutActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                getClientToken();
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });
    }

    private void getClientToken() {
        Call<Token> call=jsonPlaceHolderApi.getClientToken();
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(CheckoutActivity.this, "Code: "+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Token token=response.body();
                client_token=token.getClient_token();
                paymentProcess();
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });
    }

    private void paymentProcess() {
        DropInRequest dropInRequest = new DropInRequest().clientToken(client_token);
        startActivityForResult(dropInRequest.getIntent(CheckoutActivity.this), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Log.e("success", "yeah");
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                // use the result to update your UI and send the payment method nonce to your server
                nonce=result.getPaymentMethodNonce().getNonce();
                Log.e("nonce", nonce);

                sendPaymentData();
            }

            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Payment Cancelled!!!", Toast.LENGTH_SHORT).show();
            }

            else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        }
    }

    private void sendPaymentData() {
        HashMap<String, Object> nonceMap=new HashMap<>();
        nonceMap.put("nonce", nonce);

        Call<PaymentStatus> call=jsonPlaceHolderApi.paymentProcess(order_id, nonceMap);
        call.enqueue(new Callback<PaymentStatus>() {
            @Override
            public void onResponse(Call<PaymentStatus> call, Response<PaymentStatus> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(CheckoutActivity.this, "Error code = "+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                PaymentStatus status=response.body();

                paymentStatus(status.getPayment_status());
            }

            @Override
            public void onFailure(Call<PaymentStatus> call, Throwable t) {
            }
        });
    }

    private void paymentStatus(String payment_status) {
        tvMessage.setVisibility(View.VISIBLE);
        tvCheckout.setVisibility(View.GONE);
        etFirstName.setVisibility(View.GONE);
        etLastName.setVisibility(View.GONE);
        etEmail.setVisibility(View.GONE);
        etAddress.setVisibility(View.GONE);
        etPostalCode.setVisibility(View.GONE);
        etCity.setVisibility(View.GONE);
        btnPlaceOrder.setVisibility(View.GONE);

        if(payment_status.equals("success")){
            tvMessage.setText("Payment Successfull. Your order has been placed and paid. Click here to continue shopping");
        }
        else if(payment_status.equals("failed")){
            tvMessage.setText("Payment Failed. Your order has been placed but not paid. Click here to continue shopping");
        }
    }
}
