package com.fyp1.assignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.fyp1.assignment4.Adapter.NonAdminStockItemAdapter;
import com.fyp1.assignment4.Adapter.StockItemAdapter;
import com.fyp1.assignment4.POJOS.StockItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewItems extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<StockItem> stockItemList;
    private DatabaseReference databaseReference;
    private NonAdminStockItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        recyclerView = findViewById(R.id.view_items_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

                adapter = new NonAdminStockItemAdapter(ViewItems.this, stockItemList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewItems.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}