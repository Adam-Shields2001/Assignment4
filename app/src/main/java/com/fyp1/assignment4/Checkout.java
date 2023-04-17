package com.fyp1.assignment4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp1.assignment4.Interfaces.PaymentStrategy;
import com.fyp1.assignment4.POJOS.StockItem;

import java.util.List;
import java.util.Locale;

public class Checkout extends AppCompatActivity {

    private TextView totalAmountTextView;
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        totalAmountTextView = findViewById(R.id.total_amount);

        // Calculate total amount and display it
        double totalAmount = calculateTotalAmount();
        String totalAmountString = String.format(Locale.getDefault(), "$%.2f", totalAmount);
        totalAmountTextView.setText(totalAmountString);

        // Set up place order button
        Button placeOrderButton = findViewById(R.id.place_order_button);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pay using selected payment strategy
                paymentStrategy.pay(totalAmount);
                Toast.makeText(Checkout.this, "Purchase Successful", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up cancel order button
        Button cancelOrderButton = findViewById(R.id.cancel_order_button);
        cancelOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Checkout.this, ShoppingBasket.class));
            }
        });
    }

    private double calculateTotalAmount() {
        List<StockItem> stockItemList = (List<StockItem>) getIntent().getSerializableExtra("stockItemList");
        double totalAmount = 0.0;

        for (StockItem stockItem : stockItemList) {
            String price = stockItem.getPrice();
            double priceValue = Double.parseDouble(price);
            totalAmount += priceValue;
        }

        return totalAmount;
    }

}
