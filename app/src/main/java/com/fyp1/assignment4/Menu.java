package com.fyp1.assignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fyp1.assignment4.POJOS.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    private Button viewStockButton, searchButton, shoppingCartButton, accountButton;
    private TextView headerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        String userId = user.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user != null) {
                    String displayName = user.getName();
                    headerText.setText("Welcome " + displayName);
                } else {
                    headerText.setText("Welcome");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        headerText = findViewById(R.id.headerText);
        viewStockButton = findViewById(R.id.viewStockButton);
        searchButton = findViewById(R.id.searchButton);
        shoppingCartButton = findViewById(R.id.shoppingCartButton);
        accountButton = findViewById(R.id.accountButton);

        viewStockButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        shoppingCartButton.setOnClickListener(this);
        accountButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewStockButton:
                startActivity(new Intent(this, ViewStock.class));
                break;
            case R.id.searchButton:
                startActivity(new Intent(this, AddItem.class));
                break;
            case R.id.shoppingCartButton:
                // Add action to view customers
                break;
            case R.id.accountButton:
                // Add action to view purchase history
                break;
            case R.id.logoutBtn:
                startActivity(new Intent(this, Login.class));
                break;
            default:
                break;
        }
    }
}