package com.saahil.smehrashop.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.saahil.smehrashop.Models.Products;
import com.saahil.smehrashop.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import io.paperdb.Paper;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    ArrayList<Products> cartProducts;
    ItemClicked activity;
    TextView tvTotalAmount;
    double totalAmount=0.00;

    public interface ItemClicked{
        void onDeleteClicked(int index);
        void onUpdateQuantityClicked(int index, int quantity);
    }

    public CartAdapter(ItemClicked itemClicked, ArrayList<Products> cartProducts, TextView tvTotalAmount){
        activity=itemClicked;
        this.cartProducts=cartProducts;
        this.tvTotalAmount=tvTotalAmount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProductImage, ivDeleteProduct;
        TextView tvName, tvPrice, tvSubTotal;
        ElegantNumberButton quantityNumberButton;
        Button btnUpdateQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName=itemView.findViewById(R.id.tvName);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvSubTotal=itemView.findViewById(R.id.tvSubTotal);
            ivProductImage=itemView.findViewById(R.id.ivProductImage);
            ivDeleteProduct=itemView.findViewById(R.id.ivDeleteProduct);
            quantityNumberButton=itemView.findViewById(R.id.quantityNumberButton);
            btnUpdateQuantity=itemView.findViewById(R.id.btnUpdateQuantity);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_product_items, parent, false);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(cartProducts.get(position));
        holder.tvName.setText(cartProducts.get(position).getName());
        holder.tvPrice.setText("$"+cartProducts.get(position).getPrice());
        Picasso.get().load(cartProducts.get(position).getImage()).placeholder(R.drawable.no_image).into(holder.ivProductImage);
        holder.quantityNumberButton.setNumber(""+cartProducts.get(position).getQuantity());

        double price=Double.parseDouble(cartProducts.get(position).getPrice());
        double subTotal=(cartProducts.get(position).getQuantity()) * price;
        totalAmount=totalAmount+subTotal;
        holder.tvSubTotal.setText("Sub-Total = $"+subTotal);
        tvTotalAmount.setText("Total Amount = $"+totalAmount);

        holder.ivDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalAmount=0.00;
                activity.onDeleteClicked(position);
            }
        });

        holder.btnUpdateQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalAmount=0.00;
                int quantity=Integer.parseInt(holder.quantityNumberButton.getNumber());
                activity.onUpdateQuantityClicked(position, quantity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartProducts.size();
    }
}
