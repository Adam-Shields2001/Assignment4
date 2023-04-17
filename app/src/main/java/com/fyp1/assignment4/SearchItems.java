package com.fyp1.assignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fyp1.assignment4.Adapter.NonAdminStockItemAdapter;
import com.fyp1.assignment4.POJOS.StockItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SearchItems extends AppCompatActivity implements Observer {

    private List<StockItem> stockItemList;
    private RecyclerView searchResultsRecyclerView;
    private DatabaseReference databaseReference;
    private NonAdminStockItemAdapter nonAdminStockItemAdapter;
    private List<Observer> observerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_items);

        // Initialize the RecyclerView and adapter
        searchResultsRecyclerView = findViewById(R.id.search_results_recycler_view);
        searchResultsRecyclerView.setHasFixedSize(true);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the list of stock items
        stockItemList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Stock");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stockItemList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StockItem stockItem = snapshot.getValue(StockItem.class);
                    stockItem.setId(snapshot.getKey());
                    stockItemList.add(stockItem);
                }

                nonAdminStockItemAdapter = new NonAdminStockItemAdapter(SearchItems.this, stockItemList);
                searchResultsRecyclerView.setAdapter(nonAdminStockItemAdapter);
                nonAdminStockItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchItems.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Get references to the search EditText and button
        EditText searchEditText = findViewById(R.id.search_edit_text);
        Button searchButton = findViewById(R.id.search_button);

        // Set a listener for the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the search query from the EditText
                String query = searchEditText.getText().toString().trim();

                // Filter the list of stock items by the search query
                List<StockItem> filteredList = filterStockItems(stockItemList, query);

                // Update the RecyclerView adapter with the filtered list
                nonAdminStockItemAdapter.setStockItemList(filteredList);
            }
        });
    }

    // Helper method to filter the list of stock items by a search query
    private List<StockItem> filterStockItems(List<StockItem> stockItems, String query) {
        List<StockItem> filteredList = new ArrayList<>();
        for (StockItem stockItem : stockItems) {
            if (stockItem.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    stockItem.getCategory().toLowerCase().contains(query.toLowerCase()) ||
                    stockItem.getManufacturer().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(stockItem);
            }
        }
        return filteredList;
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}