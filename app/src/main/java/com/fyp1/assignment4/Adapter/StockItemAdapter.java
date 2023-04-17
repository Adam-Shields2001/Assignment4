package com.fyp1.assignment4.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp1.assignment4.AddItem;
import com.fyp1.assignment4.POJOS.StockItem;
import com.fyp1.assignment4.R;
import com.fyp1.assignment4.ViewStock;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class StockItemAdapter extends RecyclerView.Adapter<StockItemAdapter.StockItemViewHolder> {

    private List<StockItem> stockItemList;
    private Context context;
    private DatabaseReference databaseReference;

    public StockItemAdapter(Context context, List<StockItem> stockItemList, DatabaseReference databaseReference) {
        this.context = context;
        this.stockItemList = stockItemList;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public StockItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_item_layout, parent, false);
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
        public ImageButton menuButton;

        public StockItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title);
            manufacturerTextView = itemView.findViewById(R.id.item_manufacturer);
            priceTextView = itemView.findViewById(R.id.item_price);
            categoryTextView = itemView.findViewById(R.id.item_category);
            imageView = itemView.findViewById(R.id.item_image);
            menuButton = itemView.findViewById(R.id.menu_button);

            // set the click listener for the menu button
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // create the popup menu
                    PopupMenu popupMenu = new PopupMenu(context, v);
                    popupMenu.getMenuInflater().inflate(R.menu.item_menu, popupMenu.getMenu());

                    // set the click listener for the popup menu
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION && position < stockItemList.size()) {
                                StockItem clickedItem = stockItemList.get(position);

                                switch (item.getItemId()) {
                                    case R.id.menu_edit:
                                        Intent intent = new Intent(context, AddItem.class);
                                        intent.putExtra("item_id", clickedItem.getId());
                                        intent.putExtra("stock_item", clickedItem);
                                        context.startActivity(intent);
                                        return true;
                                    case R.id.menu_delete:
                                        String itemId = clickedItem.getId();
                                        if (itemId != null) {
                                            databaseReference.child(itemId).removeValue(new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                    if (error == null) {
                                                        Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(context, "Error deleting item", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    default:
                                        return false;
                                }
                            }
                            return false;
                        }
                    });

                    // show the popup menu
                    popupMenu.show();
                }
            });
        }

        @Override
        public void onClick(View view) {
            // Handle item click
        }
    }


    private class ItemMenuClickListener implements PopupMenu.OnMenuItemClickListener {

        private StockItem clickedItem;

        public ItemMenuClickListener(StockItem clickedItem) {
            this.clickedItem = clickedItem;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_edit:
                    Intent intent = new Intent(context, AddItem.class);
                    intent.putExtra("stock_item", clickedItem);
                    context.startActivity(intent);
                    return true;

                case R.id.menu_delete:
                    String itemId = clickedItem.getId();
                    if (itemId != null) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Stock").child(itemId);
                        databaseReference.removeValue();
                        Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        return false;
                    }
                default:
                    return false;
            }
        }
    }
}


