package com.fyp1.assignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fyp1.assignment4.POJOS.StockItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddItem extends AppCompatActivity {

    private EditText titleEditText, manufacturerEditText, priceEditText, categoryEditText, imageEditText;
    private Button addButton, updateButton;
    private StockItem stockItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Initialize views
        titleEditText = findViewById(R.id.stockTitleEditText);
        manufacturerEditText = findViewById(R.id.stockManufacturerEditText);
        priceEditText = findViewById(R.id.stockPriceEditText);
        categoryEditText = findViewById(R.id.stockCategoryEditText);
        imageEditText = findViewById(R.id.stockImageEditText);
        addButton = findViewById(R.id.addStockButton);
        updateButton = findViewById(R.id.updateStockButton);

        // Get the details of the clicked item from the Intent
        Intent intent = getIntent();
        stockItem = (StockItem) intent.getSerializableExtra("stock_item");


        // Check if the StockItem object is not null before accessing its properties
        if (stockItem != null) {
            stockItem.setId(getIntent().getStringExtra("item_id"));
            titleEditText.setText(stockItem.getTitle());
            manufacturerEditText.setText(stockItem.getManufacturer());
            priceEditText.setText(String.valueOf(stockItem.getPrice()));
            categoryEditText.setText(stockItem.getCategory());
            imageEditText.setText(stockItem.getImage());
        } else {
            Toast.makeText(this, "Error: Item not found", Toast.LENGTH_SHORT).show();
        }

        // Set click listener for update button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from EditText fields
                String title = titleEditText.getText().toString().trim();
                String manufacturer = manufacturerEditText.getText().toString().trim();
                String price = (priceEditText.getText().toString().trim());
                String category = categoryEditText.getText().toString().trim();
                String image = imageEditText.getText().toString().trim();

                // Perform validation checks on input fields
                if (TextUtils.isEmpty(title)) {
                    titleEditText.setError("Title is required");
                    titleEditText.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(manufacturer)) {
                    manufacturerEditText.setError("Manufacturer is required");
                    manufacturerEditText.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(price)) {
                    priceEditText.setError("Price is required");
                    priceEditText.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(category)) {
                    categoryEditText.setError("Category is required");
                    categoryEditText.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(image)) {
                    imageEditText.setError("Image URL is required");
                    imageEditText.requestFocus();
                    return;
                }

                // Save data to Firebase Realtime Database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Stock");
                String id = databaseReference.push().getKey();
                StockItem stockItem = new StockItem(title, manufacturer, price, category, image);
                databaseReference.child(id).setValue(stockItem);

                // Show success message and go back to admin menu
                Toast.makeText(AddItem.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddItem.this, AdminMenu.class));
            }
        });

        // Set the click listener for the update button
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItem();
            }
        });
    }

    private void updateItem() {
        String title = titleEditText.getText().toString().trim();
        String manufacturer = manufacturerEditText.getText().toString().trim();
        String price = (priceEditText.getText().toString().trim());
        String category = categoryEditText.getText().toString().trim();
        String image = imageEditText.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            titleEditText.setError("Title is required");
            titleEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(manufacturer)) {
            manufacturerEditText.setError("Manufacturer is required");
            manufacturerEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(price)) {
            priceEditText.setError("Price is required");
            priceEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(category)) {
            categoryEditText.setError("Category is required");
            categoryEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(image)) {
            imageEditText.setError("Image URL is required");
            imageEditText.requestFocus();
            return;
        }

        // Get the stock item object from the intent extra
        StockItem stockItem = (StockItem) getIntent().getSerializableExtra("stock_item");
        if (stockItem == null) {
            // Handle the case where the stock item object is null
            return;
        }

        // Check if the stock item has a non-null ID
        String itemId = stockItem.getId();
        if (itemId == null) {
            // Handle the case where the item ID is null
            Toast.makeText(this, "Unable to update stock item", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get a reference to the Firebase Realtime Database node for stock items
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Stock");

        // Retrieve the stock item from the database using its ID
        databaseReference.child(itemId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get the stock item object from the database snapshot
                StockItem stockItem = snapshot.getValue(StockItem.class);

                if (stockItem == null) {
                    return;
                }

                // Update the stock item object with the new values
                stockItem.setTitle(title);
                stockItem.setManufacturer(manufacturer);
                stockItem.setPrice(price);
                stockItem.setCategory(category);
                stockItem.setImage(image);

                // Update the stock item in the database
                databaseReference.child(itemId).setValue(stockItem);

                // Display a success message
                Toast.makeText(AddItem.this, "Stock item updated successfully", Toast.LENGTH_SHORT).show();

                // Navigate back to the MainActivity
                Intent intent = new Intent(AddItem.this, AdminMenu.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the case where the database retrieval is cancelled or fails
                Toast.makeText(AddItem.this, "Unable to retrieve stock item from database", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
