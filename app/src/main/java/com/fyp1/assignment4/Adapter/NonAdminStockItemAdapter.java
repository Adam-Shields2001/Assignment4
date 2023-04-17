package com.fyp1.assignment4.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp1.assignment4.POJOS.StockItem;
import com.fyp1.assignment4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class NonAdminStockItemAdapter extends RecyclerView.Adapter<NonAdminStockItemAdapter.StockItemViewHolder> implements Observer{

    private List<StockItem> stockItemList;
    private Context context;
    private Observable observable;

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
            holder.manufacturerTextView.setText("Manufacturer: " + stockItem.getManufacturer());
            holder.priceTextView.setText("Price: €" + String.valueOf(stockItem.getPrice()));
            holder.categoryTextView.setText("Category: " + stockItem.getCategory());
            holder.stockTextView.setText("Stock: " + String.valueOf(stockItem.getStock()));
            // Set image to ImageView using a library such as Glide or Picasso
        }

        holder.addToBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String userId = mAuth.getCurrentUser().getUid(); // get the current user's ID

                int position = holder.getAdapterPosition(); // get the selected item's position

                StockItem selectedItem = stockItemList.get(position); // get the selected item's data

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference basketRef = database.getReference("Basket");

                // create a new child node for the current user's basket with a unique ID
                DatabaseReference userBasketRef = basketRef.child(userId).push();

                // add the selected item's data to the new child node
                userBasketRef.child("title").setValue(selectedItem.getTitle());
                userBasketRef.child("manufacturer").setValue(selectedItem.getManufacturer());
                userBasketRef.child("price").setValue(selectedItem.getPrice());
                userBasketRef.child("category").setValue(selectedItem.getCategory());
                userBasketRef.child("stock").setValue(selectedItem.getStock());
                // add the image URL if necessary

                // show a toast to indicate that the item has been added to the basket
                Toast.makeText(context, "Item added to basket", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return stockItemList.size();
    }

    public void setStockItemList(List<StockItem> stockItemList) {
        this.stockItemList = stockItemList;
        notifyDataSetChanged();
    }

    @Override
    public void update(Observable observable, Object arg) {
        // Check if the updated observable is the same as the one we are observing
        if (observable == this.observable) {
            // Cast the argument to a List of StockItems and set it in the adapter
            List<StockItem> stockItemList = (List<StockItem>) arg;
            setStockItemList(stockItemList);
        }
    }

    public void setObservable(Observable observable) {
        this.observable = observable;
    }

    public class StockItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView titleTextView, manufacturerTextView, priceTextView, categoryTextView, stockTextView;
        public ImageView imageView;
        public Button addToBasketButton;

        public StockItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title);
            manufacturerTextView = itemView.findViewById(R.id.item_manufacturer);
            priceTextView = itemView.findViewById(R.id.item_price);
            categoryTextView = itemView.findViewById(R.id.item_category);
            imageView = itemView.findViewById(R.id.item_image);
            addToBasketButton = itemView.findViewById(R.id.addToBasketButton);
            stockTextView = itemView.findViewById(R.id.item_stock_left);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Handle item click
        }
    }
}

