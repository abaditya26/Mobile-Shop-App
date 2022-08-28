package ml.adityabodhankar.mobileapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

import ml.adityabodhankar.mobileapp.Models.CartModel;
import ml.adityabodhankar.mobileapp.Models.OrderModel;

public class PickupCheckoutActivity extends AppCompatActivity {

    int count = 0;
    OrderModel order;
    private EditText houseNumberInp, streetInp, landmarkInp, cityInp, pinCodeInp, nameInp, phoneInp;
    private TextView totalPrice, payNowBtn;
    private RecyclerView orderProductsView;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private double total = 0.0;
    private ProgressBar progress;
    private boolean isError = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Checkout");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(view -> finish());
        } catch (Exception ignored) {
        }

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nameInp = findViewById(R.id.checkout_address_name);
        phoneInp = findViewById(R.id.checkout_phone_number);
        houseNumberInp = findViewById(R.id.checkout_address_house_no);
        streetInp = findViewById(R.id.checkout_address_street);
        landmarkInp = findViewById(R.id.checkout_address_landmark);
        cityInp = findViewById(R.id.checkout_address_city);
        pinCodeInp = findViewById(R.id.checkout_address_pin_code);
        totalPrice = findViewById(R.id.total_price_checkout);
        payNowBtn = findViewById(R.id.pay_now_btn);
        progress = findViewById(R.id.progress);
        orderProductsView = findViewById(R.id.checkout_product_list);
        orderProductsView.setLayoutManager(new LinearLayoutManager(this));

    }
}
