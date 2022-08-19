package ml.adityabodhankar.mobileapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ml.adityabodhankar.mobileapp.Adapter.CheckoutCartAdapter;
import ml.adityabodhankar.mobileapp.Adapter.TrackingAdapter;
import ml.adityabodhankar.mobileapp.Models.CartModel;
import ml.adityabodhankar.mobileapp.Models.OrderModel;
import ml.adityabodhankar.mobileapp.Models.TrackingModel;

public class OrderDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView orderId, orderName, orderTotal, cName, cPhoneNo, cAddress, cCity, cPinCode,
            totalProductsView, totalQuantityView, orderStatus, paymentId;
    private RecyclerView adminOrdersProductsRecycler;
    private int totalQuantity;
    private RecyclerView oldOrdersRecycler;
    private ProgressBar progress;
    private List<TrackingModel> trackingDetails;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Toolbar toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Order Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(view -> finish());
        } catch (Exception ignored) {
        }

        Intent intent = getIntent();
        if (intent.getStringExtra("id") == null) {
            Toast.makeText(this, "Invalid navigation to screen", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();

        orderId = findViewById(R.id.order_id);
        orderName = findViewById(R.id.order_name);
        orderTotal = findViewById(R.id.order_total);
        cName = findViewById(R.id.c_name);
        cPhoneNo = findViewById(R.id.c_phone_no);
        cAddress = findViewById(R.id.c_address);
        cCity = findViewById(R.id.c_city);
        cPinCode = findViewById(R.id.c_pin_code);
        totalProductsView = findViewById(R.id.total_products);
        orderStatus = findViewById(R.id.order_status);
        paymentId = findViewById(R.id.payment_id);
        adminOrdersProductsRecycler = findViewById(R.id.admin_order_products);
        totalQuantityView = findViewById(R.id.total_quantity);
        adminOrdersProductsRecycler.setLayoutManager(new LinearLayoutManager(this));


        oldOrdersRecycler = findViewById(R.id.order_old_status);
        oldOrdersRecycler.setLayoutManager(new LinearLayoutManager(this));

        trackingDetails = new ArrayList<>();

        id = intent.getStringExtra("id");


        db.collection("orders").document(intent.getStringExtra("id"))
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        OrderModel orderData = new OrderModel(Objects.requireNonNull(value.getData()));
                        List<CartModel> orderProducts = new ArrayList<>();
                        db.collection("orders").document(intent.getStringExtra("id"))
                                .collection("products")
                                .addSnapshotListener((value1, error1) -> {
                                    if (value1 != null) {
                                        orderProducts.clear();
                                        totalQuantity = 0;
                                        for (QueryDocumentSnapshot s : value1) {
                                            orderProducts.add(new CartModel(s.getData()));
                                            totalQuantity += (int) ((long) (s.get("quantity")));
                                        }
                                        setData(orderData, orderProducts);
                                    } else {
                                        Toast.makeText(this, "Error",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
        setLoading(true);

        db.collection("orders").document(id).collection("tracking")
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (snapshots != null) {
                        trackingDetails.clear();
                        for (QueryDocumentSnapshot snapshot : snapshots) {
                            trackingDetails.add(new TrackingModel(snapshot.getData()));
                        }

                        //update UI
                        setLoading(false);
                        oldOrdersRecycler.setAdapter(new TrackingAdapter(this, trackingDetails));
                    } else {
                        Toast.makeText(this, "Unable to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setLoading(boolean status) {
        if (status) {
        } else {
        }
    }

    @SuppressLint("SetTextI18n")
    private void setData(OrderModel orderData, List<CartModel> orderProducts) {
        orderId.setText(orderData.getOrderId());
        orderName.setText(orderData.getOrderTitle());
        orderTotal.setText("" + orderData.getOrderTotal());
        cName.setText(orderData.getName());
        cPhoneNo.setText(orderData.getPhone());
        cAddress.setText(orderData.getAddressHouseNo() + ", " +
                orderData.getAddressLandmark() + ", " + orderData.getAddressStreet());
        cCity.setText(orderData.getAddressCity());
        cPinCode.setText(orderData.getAddressPinCode());
        adminOrdersProductsRecycler.setAdapter(new CheckoutCartAdapter(this, orderProducts));
        totalQuantityView.setText("" + totalQuantity);
        totalProductsView.setText(orderProducts.size() + "");
        orderStatus.setText(orderData.getOrderStatus());
        paymentId.setText(orderData.getPaymentId());
    }
}