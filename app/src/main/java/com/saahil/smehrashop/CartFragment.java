package com.saahil.smehrashop;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.saahil.smehrashop.Adapters.CartAdapter;
import com.saahil.smehrashop.Models.Products;

import java.util.ArrayList;

import io.paperdb.Paper;

public class CartFragment extends Fragment implements CartAdapter.ItemClicked {
    View view;
    RecyclerView rvProductList;
    RecyclerView.Adapter cartAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Products> cartProducts;
    TextView tvTotalAmount;
    Button btnCheckout;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Paper.init(this.getActivity());

        view=inflater.inflate(R.layout.fragment_cart, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvTotalAmount=view.findViewById(R.id.tvTotalAmount);
        btnCheckout=view.findViewById(R.id.btnCheckout);

        rvProductList=view.findViewById(R.id.rvCartProducts);
        rvProductList.setHasFixedSize(true);
        cartProducts=new ArrayList<>();

        layoutManager=new LinearLayoutManager(this.getActivity());
        rvProductList.setLayoutManager(layoutManager);

        getCartProducts();

        cartAdapter=new CartAdapter(CartFragment.this, cartProducts, tvTotalAmount);
        rvProductList.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
    }

    private void getCartProducts() {
        if(Paper.book().read("cart") != null) {
            cartProducts= Paper.book().read("cart");
        }
    }

    @Override
    public void onDeleteClicked(int index) {
        cartProducts.remove(index);
        Paper.book().delete("cart");
        Paper.book().write("cart", cartProducts);
        cartAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateQuantityClicked(int index, int quantity) {
        cartProducts.get(index).setQuantity(quantity);
        Paper.book().delete("cart");
        Paper.book().write("cart", cartProducts);
        cartAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Quantity updated successfully", Toast.LENGTH_SHORT).show();
    }
}
