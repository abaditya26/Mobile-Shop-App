package ml.adityabodhankar.mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import ml.adityabodhankar.mobileapp.Models.CartModel;
import ml.adityabodhankar.mobileapp.Models.ProductModel;

public class ProductDescriptionActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView addToCartBtn, buyBtn;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitle(getString(R.string.app_name));
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

        addToCartBtn = findViewById(R.id.add_to_cart_btn);
        loading = findViewById(R.id.progress_add_to_cart);
        buyBtn = findViewById(R.id.buy_now_btn);
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

        addToCartBtn.setOnClickListener(view -> addToCart(product, false));
        buyBtn.setOnClickListener(view -> addToCart(product, true));
        showLoading(false);
    }

    private void addToCart(ProductModel product, boolean isBuy) {
//        check if the product is already in cart
//        if yes then increment the quantity
//        if no add as new product to cart
        showLoading(true);
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        CartModel cartProduct = new CartModel(product.getId(), product.getName(),
                product.getImage(), product.getPrice(), 1);
        db.collection("users").document(uid).collection("cart").document(product.getId())
                .get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        //product already exists in cart
                        try {
                            cartProduct.setQuantity((int) ((long) documentSnapshot.get("quantity") + 1));
                        } catch (Exception e) {
                            System.out.println("error" + e);
                            cartProduct.setQuantity(1);
                        }
                    }
                    db.collection("users").document(uid).collection("cart").document(product.getId())
                            .set(cartProduct).addOnSuccessListener(unused -> {
                                if (isBuy) {
                                    startActivity(new Intent(this, CheckoutActivity.class));
                                    finish();
                                    return;
                                }
                                Toast.makeText(this, "Product Added to cart", Toast.LENGTH_SHORT).show();
                                showLoading(false);
                            }).addOnFailureListener(e -> {
                                Toast.makeText(this, "Unable to add product to cart", Toast.LENGTH_SHORT).show();
                                showLoading(false);
                            });
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    showLoading(false);
                });
    }

    private void showLoading(boolean flag) {
        if (flag) {
            //show loading
            addToCartBtn.setVisibility(View.GONE);
            buyBtn.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
        } else {
            //hide loading
            addToCartBtn.setVisibility(View.VISIBLE);
            buyBtn.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
        }
    }
}