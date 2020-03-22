package com.saahil.smehrashop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saahil.smehrashop.Model.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    ArrayList<Products> products;

    public ProductAdapter(Context context, ArrayList<Products> list){
        products=list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvPrice, tvDescription;
        ImageView ivProductImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvName);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            ivProductImage=itemView.findViewById(R.id.ivProductImage);
        }
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(products.get(position));
        holder.tvName.setText(products.get(position).getName());
        holder.tvDescription.setText(products.get(position).getDescription());
        holder.tvPrice.setText("$"+products.get(position).getPrice());
        Picasso.get().load(products.get(position).getImage()).placeholder(R.drawable.no_image).into(holder.ivProductImage);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
