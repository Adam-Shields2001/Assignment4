package com.fyp1.assignment4.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp1.assignment4.POJOS.StockItem;
import com.fyp1.assignment4.R;

import java.util.List;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.BasketViewHolder> {

    private List<StockItem> stockItemList;
    private Context context;

    public BasketAdapter(Context context, List<StockItem> stockItemList) {
        this.context = context;
        this.stockItemList = stockItemList;
    }

    @NonNull
    @Override
    public BasketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item, parent, false);
        return new BasketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketViewHolder holder, int position) {
        StockItem basketItem = stockItemList.get(position);
        if (basketItem != null) {
            holder.titleTextView.setText(basketItem.getTitle());
            holder.manufacturerTextView.setText("Manufacturer: " + basketItem.getManufacturer());
            holder.priceTextView.setText("Price: €" + String.valueOf(basketItem.getPrice()));
            holder.categoryTextView.setText("Category: " + basketItem.getCategory());
            // Set image to ImageView using a library such as Glide or Picasso
        }
    }

    @Override
    public int getItemCount() {
        return stockItemList.size();
    }

    public void setBasketItemList(List<StockItem> stockItemList) {
        this.stockItemList = stockItemList;
        notifyDataSetChanged();
    }

    public void clearBasket() {
        this.stockItemList.clear();
        notifyDataSetChanged();
    }

    public class BasketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView titleTextView, manufacturerTextView, priceTextView, categoryTextView;
        public ImageView imageView;

        public BasketViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.basket_item_title);
            manufacturerTextView = itemView.findViewById(R.id.basket_item_manufacturer);
            priceTextView = itemView.findViewById(R.id.basket_item_price);
            categoryTextView = itemView.findViewById(R.id.basket_item_category);
            imageView = itemView.findViewById(R.id.basket_item_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Handle item click
        }
    }
}

