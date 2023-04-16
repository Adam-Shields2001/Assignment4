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

public class NonAdminStockItemAdapter extends RecyclerView.Adapter<NonAdminStockItemAdapter.StockItemViewHolder> {

    private List<StockItem> stockItemList;
    private Context context;

    public NonAdminStockItemAdapter(Context context, List<StockItem> stockItemList) {
        this.context = context;
        this.stockItemList = stockItemList;
    }

    @NonNull
    @Override
    public StockItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_customer, parent, false);
        return new StockItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockItemViewHolder holder, int position) {
        StockItem stockItem = stockItemList.get(position);
        if (stockItem != null) {
            holder.titleTextView.setText(stockItem.getTitle());
            holder.manufacturerTextView.setText(stockItem.getManufacturer());
            holder.priceTextView.setText(String.valueOf(stockItem.getPrice()));
            holder.categoryTextView.setText(stockItem.getCategory());
            // Set image to ImageView using a library such as Glide or Picasso
        }
    }

    @Override
    public int getItemCount() {
        return stockItemList.size();
    }

    public void setStockItemList(List<StockItem> stockItemList) {
        this.stockItemList = stockItemList;
        notifyDataSetChanged();
    }

    public class StockItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView titleTextView;
        public TextView manufacturerTextView;
        public TextView priceTextView;
        public TextView categoryTextView;
        public ImageView imageView;

        public StockItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title);
            manufacturerTextView = itemView.findViewById(R.id.item_manufacturer);
            priceTextView = itemView.findViewById(R.id.item_price);
            categoryTextView = itemView.findViewById(R.id.item_category);
            imageView = itemView.findViewById(R.id.item_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Handle item click
        }
    }
}

