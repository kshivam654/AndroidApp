package com.lookentertainment.androidapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dev.android.oneupi.OneUPIPayment;
import dev.android.oneupi.listener.PaymentStatusListener;
import dev.android.oneupi.model.PaymentApp;
import dev.android.oneupi.model.TransactionDetails;

public class MainActivity extends AppCompatActivity implements PaymentStatusListener {

    String transactionId;
    private OneUPIPayment easyUpiPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button = findViewById(R.id.button);

        button.setOnClickListener(view -> {
            payWithUpi();
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void payWithUpi() {
        transactionId = "TID" + System.currentTimeMillis();

        PaymentApp paymentApp = PaymentApp.ALL;

        // START PAYMENT INITIALIZATION
        OneUPIPayment.Builder builder = new OneUPIPayment.Builder(this)
                .with(paymentApp)
                .setPayeeVpa("oneupiexample@ybl")
                .setPayeeName("Elon Musk")
                .setTransactionId(transactionId)
                .setTransactionRefId(transactionId)
                .setPayeeMerchantCode("fc458352-768f-423f-9015-75336c94c5f6")
                .setDescription("planName")
                .setAmount("1.00");
        // END INITIALIZATION

        try {
            // Build instance
            easyUpiPayment = builder.build();

            // Register Listener for Events
            easyUpiPayment.setPaymentStatusListener(this);

            // Start payment / transaction
            easyUpiPayment.startPayment();
        } catch (Exception exception) {
            exception.printStackTrace();
            toast("Error: " + exception.getMessage());
        }
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        // Transaction Completed
        Log.d("TransactionDetails", transactionDetails.toString());

        switch (transactionDetails.getTransactionStatus()) {
            case SUCCESS:
                onTransactionSuccess();
                break;
            case FAILURE:
                onTransactionFailed();
                break;
            case SUBMITTED:
                onTransactionSubmitted();
                break;
        }
    }

    @Override
    public void onTransactionCancelled() {
        // Payment Cancelled by User
        toast("Cancelled by user");

    }

    private void onTransactionSuccess() {
        // Payment Success
        toast("Success");
        Log.d("UPI", "responseStr: " + transactionId);
//  new Transaction(SelectPlanActivity.this).purchasedItem(planId, transactionId, "UPI");
    }

    private void onTransactionSubmitted() {
        // Payment Pending
        toast("Pending | Submitted");

    }

    private void onTransactionFailed() {
        // Payment Failed
        toast("Failed");

    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}