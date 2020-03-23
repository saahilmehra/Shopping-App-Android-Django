package com.saahil.smehrashop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saahil.smehrashop.Model.Products;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements ProductAdapter.ItemClicked {
    View root;
    RecyclerView rvProductList;
    RecyclerView.Adapter productAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Products> productList;
    JsonPlaceHolderApi jsonPlaceHolderApi;

    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.29.214:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);

        rvProductList=root.findViewById(R.id.rvProductList);
        rvProductList.setHasFixedSize(true);
        productList=new ArrayList<>();

        layoutManager=new LinearLayoutManager(this.getActivity());
        rvProductList.setLayoutManager(layoutManager);
        
        getProducts();
    }

    private void getProducts() {
        Call<ArrayList<Products>> call=jsonPlaceHolderApi.getProducts();
        call.enqueue(new Callback<ArrayList<Products>>() {
            @Override
            public void onResponse(Call<ArrayList<Products>> call, Response<ArrayList<Products>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getActivity(), "Code: "+ response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                productList=response.body();
                productAdapter=new ProductAdapter(HomeFragment.this, productList);
                rvProductList.setAdapter(productAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Products>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClicked(int index) {
        ProductDetailFragment detailFragment=new ProductDetailFragment();

        Bundle args=new Bundle();
        args.putInt("id", productList.get(index).getId());
        detailFragment.setArguments(args);

        this.getParentFragmentManager().beginTransaction()
                .add(R.id.nav_host_fragment, detailFragment)
                .hide(this)
                .addToBackStack(null)
                .commit();
    }
}
