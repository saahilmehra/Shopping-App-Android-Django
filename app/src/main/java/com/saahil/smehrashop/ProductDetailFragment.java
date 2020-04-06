package com.saahil.smehrashop;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.saahil.smehrashop.Models.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetailFragment extends Fragment {
    View view;
    TextView tvDetailName, tvDetailPrice, tvDetailDescription;
    ImageView ivProductDetailImage;
    ElegantNumberButton elegantQuantityButton;
    Button btnAddToCart;
    JsonPlaceHolderApi jsonPlaceHolderApi;
    int id;
    Products product;
    ArrayList<Products> cart;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Paper.init(this.getActivity());

        view=inflater.inflate(R.layout.fragment_product_detail, container, false);

        id=getArguments().getInt("id", -1);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cart=new ArrayList<>();

        if(Paper.book().read("cart") != null) {
            cart= Paper.book().read("cart");
        }
        Toast.makeText(getContext(), "size is " + cart.size(), Toast.LENGTH_SHORT).show();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.29.214:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);

        tvDetailName=view.findViewById(R.id.tvDetailName);
        tvDetailPrice=view.findViewById(R.id.tvDetailPrice);
        tvDetailDescription=view.findViewById(R.id.tvDetailDescription);
        ivProductDetailImage=view.findViewById(R.id.ivProductDetailImage);
        elegantQuantityButton=view.findViewById(R.id.elegantQuantityButton);
        btnAddToCart=view.findViewById(R.id.btnAddToCart);

        getPost();

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAddToCart();
            }
        });

    }

    private void btnAddToCart() {
        boolean checkInCart=false;
        int quantity=Integer.parseInt(elegantQuantityButton.getNumber());

        for(Products productInCart : cart){
            if(product.getId() == productInCart.getId()){
                checkInCart=true;
                productInCart.setQuantity(productInCart.getQuantity()+quantity);
                break;
            }
        }
        if(!checkInCart){
            Products cartProduct=new Products();
            cartProduct.setId(product.getId());
            cartProduct.setName(product.getName());
            cartProduct.setImage(product.getImage());
            cartProduct.setPrice(product.getPrice());
            cartProduct.setQuantity(quantity);
            cart.add(cartProduct);
        }
        Paper.book().write("cart", cart);
        Toast.makeText(getContext(), "Added to cart...", Toast.LENGTH_SHORT).show();
    }

    private void getPost() {
        Call<Products> call=jsonPlaceHolderApi.getProduct(id);
        call.enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Code: "+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                product=response.body();
                tvDetailName.setText(product.getName());
                tvDetailPrice.setText("$"+product.getPrice());
                tvDetailDescription.setText(product.getDescription());
                Picasso.get().load(product.getImage()).placeholder(R.drawable.no_image).into(ivProductDetailImage);
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
