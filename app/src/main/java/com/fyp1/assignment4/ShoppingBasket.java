package com.fyp1.assignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fyp1.assignment4.Adapter.BasketAdapter;
import com.fyp1.assignment4.POJOS.StockItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingBasket extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<StockItem> stockItemList;
    private DatabaseReference basketRef;
    private BasketAdapter adapter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_basket);

        // Get the current user's ID
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.basket_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        stockItemList = new ArrayList<>();
        adapter = new BasketAdapter(this, stockItemList);
        recyclerView.setAdapter(adapter);

        // Get a reference to the user's shopping basket in the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        basketRef = database.getReference("Basket").child(userId);

        basketRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stockItemList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StockItem stockItem = snapshot.getValue(StockItem.class);
                    stockItem.setId(snapshot.getKey());
                    stockItemList.add(stockItem);
                }

                adapter.setBasketItemList(stockItemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShoppingBasket.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Button emptyButton = findViewById(R.id.empty_button);
        emptyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear the user's shopping basket in the Firebase Realtime Database
                basketRef.setValue(null);

                // Clear the local basketItemList and update the adapter
                stockItemList.clear();
                adapter.notifyDataSetChanged();
            }
        });

        Button checkoutButton = findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Implement checkout functionality
            }
        });
    }
}
