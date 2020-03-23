package com.saahil.smehrashop;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.saahil.smehrashop.Model.Products;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetailFragment extends Fragment {
    View view;
    TextView tvDetailName, tvDetailPrice, tvDetailDescription;
    ImageView ivProductDetailImage;
    Button btnAddToCart;
    JsonPlaceHolderApi jsonPlaceHolderApi;
    int id;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_product_detail, container, false);

        id=getArguments().getInt("id", -1);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.29.214:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);

        tvDetailName=view.findViewById(R.id.tvDetailName);
        tvDetailPrice=view.findViewById(R.id.tvDetailPrice);
        tvDetailDescription=view.findViewById(R.id.tvDetailDescription);
        ivProductDetailImage=view.findViewById(R.id.ivProductDetailImage);
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
        Toast.makeText(getContext(), "Adding to cart", Toast.LENGTH_SHORT).show();
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

                Products product=response.body();
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
