package ml.adityabodhankar.mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ml.adityabodhankar.mobileapp.Adapter.CheckoutCartAdapter;
import ml.adityabodhankar.mobileapp.Models.CartModel;
import ml.adityabodhankar.mobileapp.Models.OrderModel;

public class CheckoutActivity extends AppCompatActivity {

    private EditText houseNumberInp, streetInp, landmarkInp, cityInp, pinCodeInp, nameInp, phoneInp;
    private TextView totalPrice, payNowBtn;
    private RecyclerView orderProductsView;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private List<CartModel> cartProducts;
    private double total = 0.0;
    private ProgressBar progress;
    private boolean isError = false;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        cartProducts = new ArrayList<>();

        showLoading(true);
        db.collection("users").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("cart").get()
                .addOnSuccessListener(snapshots -> {
                    cartProducts.clear();
                    total = 0;
                    for (DocumentSnapshot snapshot : snapshots){
                        total += ((long)snapshot.get("quantity") * Double.parseDouble((String) Objects.requireNonNull(snapshot.get("productPrice"))));
                        cartProducts.add(new CartModel(Objects.requireNonNull(snapshot.getData())));
                    }
                    //got products in cart
                    setUI();
                    showLoading(false);
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Unable to get required data", Toast.LENGTH_SHORT).show();
                    finish();
                    showLoading(false);
                });

        payNowBtn.setOnClickListener(view -> payNow());
    }

    private void payNow() {
        if (!validateInput()){
            return;
        }
        showLoading(true);
        OrderModel order = new OrderModel("", Objects.requireNonNull(auth.getCurrentUser()).getUid(),
                cartProducts.get(0).getProductName() + " + " +(cartProducts.size()-1 ==  0 ?
                        "" : cartProducts.size()-1), cartProducts.get(0).getProductImage(),
                "Order Received", nameInp.getText().toString(), phoneInp.getText().toString(),
                houseNumberInp.getText().toString(), streetInp.getText().toString(), landmarkInp.getText().toString(),
                cityInp.getText().toString(), pinCodeInp.getText().toString(), total);
        db.collection("orders").add(order)
                .addOnSuccessListener(documentReference -> {
                    order.setOrderId(documentReference.getId());
                    db.collection("orders").document(documentReference.getId()).set(order)
                            .addOnSuccessListener(unused -> {
                                count = 0;
                                for (CartModel product : cartProducts){
                                    db.collection("orders").document(documentReference.getId())
                                            .collection("products").document(product.getProductId())
                                            .set(product).addOnSuccessListener(unused1 -> {
                                                count++;
                                                updateUI(documentReference.getId());
                                            }).addOnFailureListener(e -> {
                                                //remove all the order details added
                                                isError = true;
                                                count++;
                                                updateUI(documentReference.getId());
                                            });
                                }
                            }).addOnFailureListener(e1 -> {
                                Toast.makeText(this, "Unable to save the order.", Toast.LENGTH_SHORT).show();
                                showLoading(false);
                            });
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Unable to save the order.", Toast.LENGTH_SHORT).show();
                    showLoading(false);
                });
    }

    private void updateUI(String productId) {
        if (count != cartProducts.size()){
            return;
        }
        if (isError){
            db.collection("orders").document(productId).delete();
            Toast.makeText(this, "Unable to save product", Toast.LENGTH_SHORT).show();
        }else{
            db.collection("users").document(auth.getCurrentUser().getUid()).collection("cart").get()
                            .addOnSuccessListener(snapshots -> {
                                for (DocumentSnapshot s : snapshots){
                                    db.collection("users").document(auth.getCurrentUser().getUid()).collection("cart")
                                            .document(s.getId()).delete();
                                }
                            });
            Toast.makeText(this, "Order Saved. Proceed Checkout.", Toast.LENGTH_SHORT).show();
        }
        showLoading(false);
    }

    private boolean validateInput() {
        if (nameInp.getText().toString().equalsIgnoreCase("")){
            nameInp.setError("Name Required");
            return false;
        }
        if (phoneInp.getText().toString().equalsIgnoreCase("")){
            phoneInp.setError("Phone Number Required");
            return false;
        }
        if (houseNumberInp.getText().toString().equalsIgnoreCase("")){
            houseNumberInp.setError("House Number Required");
            return false;
        }
        if (streetInp.getText().toString().equalsIgnoreCase("")){
            streetInp.setError("Street Number required");
            return  false;
        }
        if (cityInp.getText().toString().equalsIgnoreCase("")){
            cityInp.setError("City Required");
            return false;
        }
        if (pinCodeInp.getText().toString().equalsIgnoreCase("")){
            pinCodeInp.setError("Pin code required");
            return false;
        }
        return true;
    }

    private void setUI() {
        totalPrice.setText("Rs."+total);
        orderProductsView.setAdapter(new CheckoutCartAdapter(this, cartProducts));
    }

    private void showLoading(boolean flag){
        if (flag){
            progress.setVisibility(View.VISIBLE);
            payNowBtn.setVisibility(View.GONE);
        }else{
            progress.setVisibility(View.GONE);
            payNowBtn.setVisibility(View.VISIBLE);
        }
    }
}