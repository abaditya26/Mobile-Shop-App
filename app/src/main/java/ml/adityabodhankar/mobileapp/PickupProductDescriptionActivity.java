package ml.adityabodhankar.mobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import ml.adityabodhankar.mobileapp.Models.ProductModel;

public class PickupProductDescriptionActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ProgressBar loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_product_description);
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Product Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(view -> finish());
        } catch (Exception ignored) {
        }
//        Local var initialization
        db = FirebaseFirestore.getInstance();

        loading = findViewById(R.id.progress_add_to_cart);
//        Getting the data
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        showLoading(true);
        db.collection("products").document(id).addSnapshotListener((value, error) -> {
            if (value != null) {
                if (value.exists()) {
                    ProductModel product = new ProductModel(Objects.requireNonNull(value.getData()));
                    setUi(product);
                } else {
                    Toast.makeText(this, "Error: Product not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUi(ProductModel product) {
        TextView price = findViewById(R.id.product_price);
        TextView title = findViewById(R.id.product_name);
        TextView description = findViewById(R.id.product_description);
        ImageView image = findViewById(R.id.product_image);
        if (product.getPrice().equalsIgnoreCase("0")){
            price.setText("Dynamic Pricing Applicable");
        }else {
            price.setText(product.getPrice());
        }
        title.setText(product.getName());
        description.setText(product.getDescription());
        if (!product.getImage().equalsIgnoreCase("default")) {
            Glide.with(this).load(product.getImage()).into(image);
        }
        showLoading(false);
    }


    private void showLoading(boolean flag) {
        if (flag) {
            //show loading
            loading.setVisibility(View.VISIBLE);
        } else {
            //hide loading
            loading.setVisibility(View.GONE);
        }
    }
}