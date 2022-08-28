package ml.adityabodhankar.mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import ml.adityabodhankar.mobileapp.Models.PickupProductModel;
import ml.adityabodhankar.mobileapp.Models.ProductModel;

public class PickupGetDetailsActivity extends AppCompatActivity {

    private TextView productName;
    private EditText model, description, repair, suggestion;
    private ImageView image;

    private ConstraintLayout loading;
    private ScrollView mainLayout;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_get_details);
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Pickup Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(view -> finish());
        } catch (Exception ignored) {
        }
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        productName = findViewById(R.id.product_name);
        model = findViewById(R.id.repair_device_model);
        description = findViewById(R.id.repair_description);
        repair = findViewById(R.id.repair_need_to_make);
        suggestion = findViewById(R.id.repair_suggestion);
        image = findViewById(R.id.product_image);
        loading = findViewById(R.id.loading);
        mainLayout = findViewById(R.id.main_view);

        db = FirebaseFirestore.getInstance();

//        get product details
        setLoading(true);

        db.collection("products").document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    ProductModel product = new ProductModel(Objects.requireNonNull(documentSnapshot.getData()));
                    productName.setText(product.getName());
                    if (!product.getImage().equalsIgnoreCase("default")){
                        Glide.with(this).load(product.getImage()).into(image);
                    }
                    setLoading(false);
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Error :- "+e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        (findViewById(R.id.proceed_btn)).setOnClickListener(view -> {
            if(!validateData()){
                return;
            }
            Intent i = new Intent(this, PickupCheckoutActivity.class);
            i.putExtra("id", id);
            i.putExtra("name", productName.getText().toString());
            i.putExtra("model", model.getText().toString());
            i.putExtra("description", description.getText().toString());
            i.putExtra("repair", repair.getText().toString());
            i.putExtra("suggestion", suggestion.getText().toString());
            startActivity(i);
        });

    }

    private boolean validateData() {
        if (model.getText().toString().equalsIgnoreCase("")){
            model.setError("Model Required");
            return false;
        }
        if (description.getText().toString().equalsIgnoreCase("")){
            description.setError("Description required");
            return false;
        }
        if (repair.getText().toString().equalsIgnoreCase("")){
            repair.setError("Please add repair to be made");
            return false;
        }
        return true;
    }

    private void setLoading(boolean flag) {
        if (flag){
            loading.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.GONE);
        }else{
            loading.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        }
    }
}