package com.fyp1.assignment4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminMenu extends AppCompatActivity implements View.OnClickListener {

    private Button viewStockBtn, addItemBtn, updateStockBtn, viewPurchaseHistoryBtn, logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        viewStockBtn = findViewById(R.id.viewStockBtn);
        addItemBtn = findViewById(R.id.addItemBtn);
        updateStockBtn = findViewById(R.id.updateStockBtn);
        viewPurchaseHistoryBtn = findViewById(R.id.viewPurchaseHistoryBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        viewStockBtn.setOnClickListener(this);
        addItemBtn.setOnClickListener(this);
        updateStockBtn.setOnClickListener(this);
        viewPurchaseHistoryBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewStockBtn:
                startActivity(new Intent(this, ViewStock.class));
                break;
            case R.id.addItemBtn:
                startActivity(new Intent(this, AddItem.class));
                break;
            case R.id.updateStockBtn:
                // Add action to view customers
                break;
            case R.id.viewPurchaseHistoryBtn:
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
