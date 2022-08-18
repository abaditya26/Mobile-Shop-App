package ml.adityabodhankar.mobileapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import ml.adityabodhankar.mobileapp.Adapter.CheckoutCartAdapter;
import ml.adityabodhankar.mobileapp.Adapter.TrackingAdapter;
import ml.adityabodhankar.mobileapp.Models.CartModel;
import ml.adityabodhankar.mobileapp.Models.OrderModel;
import ml.adityabodhankar.mobileapp.Models.TrackingModel;

public class UpdateStatusActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView orderId, orderName, orderTotal, cName, updateStatusBtn;
    private String status, id;
    private EditText orderStatus, orderDescription;
    private ProgressBar progress;
    private List<TrackingModel> trackingDetails;
    private RecyclerView oldOrdersRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status);


        Toolbar toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Update Order Status");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(view -> finish());
        } catch (Exception ignored) {}

        Intent intent = getIntent();
        if (intent.getStringExtra("id") == null){
            Toast.makeText(this, "Invalid navigation to screen",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        db = FirebaseFirestore.getInstance();

        orderId = findViewById(R.id.order_id);
        orderName = findViewById(R.id.order_name);
        orderTotal = findViewById(R.id.order_total);
        cName = findViewById(R.id.c_name);

        orderStatus = findViewById(R.id.order_status_input);
        orderDescription = findViewById(R.id.order_description_input);

        updateStatusBtn = findViewById(R.id.update_status_btn);
        progress = findViewById(R.id.status_progress);

        oldOrdersRecycler = findViewById(R.id.order_old_status);
        oldOrdersRecycler.setLayoutManager(new LinearLayoutManager(this));

        trackingDetails = new ArrayList<>();

        id = intent.getStringExtra("id");

        setLoading(true);

        db.collection("orders").document(id)
                .addSnapshotListener((value, error) -> {
                    if (value != null){
                        OrderModel orderData =
                                new OrderModel(Objects.requireNonNull(value.getData()));
                        setData(orderData);
                    }else{
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

        db.collection("orders").document(id).collection("tracking")
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (snapshots != null) {
                        trackingDetails.clear();
                        for (QueryDocumentSnapshot snapshot : snapshots){
                            trackingDetails.add(new TrackingModel(snapshot.getData()));
                        }

                        //update UI
                        setLoading(false);
                        oldOrdersRecycler.setAdapter(new TrackingAdapter(this, trackingDetails));
                    }else{
                        Toast.makeText(this, "Unable to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });

        String[] statusArray = {
                "Order Placed",
                "Tracking Update",
                "Pickup",
                "Cancelled",
                "Order Complete",
                "Other"
        };

        Spinner spinnerLanguages=findViewById(R.id.status_dropdown);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statusArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguages.setAdapter(adapter);
        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                status = statusArray[i];
                orderStatus.setEnabled(false);
                orderStatus.setText(status);
                if (status.equalsIgnoreCase("other")){
                    orderStatus.setEnabled(true);
                    orderStatus.setText("");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        updateStatusBtn.setOnClickListener(view -> validateStatus(orderStatus.getText().toString(),
                orderDescription.getText().toString()));
    }

    private void validateStatus(String status, String description) {
        if (status.equalsIgnoreCase("")) {
            orderStatus.setError("Required");
            return;
        }
        setLoading(true);

        if (description.equalsIgnoreCase("")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Warning")
                    .setMessage("Do you want to continue without description?")
                    .setCancelable(false)
                    .setPositiveButton("YES", (dialogInterface, i) -> {
                        updateStatus(status, description);
                        dialogInterface.dismiss();
                    }).setNegativeButton("NO", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }).show();
        } else {
            updateStatus(status, description);
        }
    }

    private void updateStatus(String status, String description) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter =
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String currentDate = formatter.format(date);
        TrackingModel tracking = new TrackingModel(id,status,description,currentDate);
        db.collection("orders").document(id)
                .collection("tracking").document(tracking.getDateTime()).set(tracking)
                .addOnSuccessListener(documentReference -> {
                    orderDescription.setText("");
                    Toast.makeText(this, "Tracking Added", Toast.LENGTH_SHORT).show();
                    setLoading(false);
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    setLoading(false);
                });
    }

    private void setLoading(boolean status){
        if (status){
            progress.setVisibility(View.VISIBLE);
            updateStatusBtn.setVisibility(View.GONE);
        }else{
            progress.setVisibility(View.GONE);
            updateStatusBtn.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setData(OrderModel orderData) {
        orderId.setText(orderData.getOrderId());
        orderName.setText(orderData.getOrderTitle());
        orderTotal.setText(""+orderData.getOrderTotal());
        cName.setText(orderData.getName());
    }
}