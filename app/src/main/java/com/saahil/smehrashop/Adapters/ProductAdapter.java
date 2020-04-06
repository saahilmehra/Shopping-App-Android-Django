package com.saahil.smehrashop.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saahil.smehrashop.Models.Products;
import com.saahil.smehrashop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    ArrayList<Products> products;
    ItemClicked activity;

    public interface ItemClicked{
        void onItemClicked(int index);
    }

    public ProductAdapter(ItemClicked itemClicked, ArrayList<Products> list){
        products=list;
        activity=itemClicked;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvPrice;
        ImageView ivProductImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvName);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            ivProductImage=itemView.findViewById(R.id.ivProductImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onItemClicked(products.indexOf((Products) view.getTag()));
                }
            });
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
        holder.tvPrice.setText("$"+products.get(position).getPrice());
        Picasso.get().load(products.get(position).getImage()).placeholder(R.drawable.no_image).into(holder.ivProductImage);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
