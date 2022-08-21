package ml.adityabodhankar.mobileapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import ml.adityabodhankar.mobileapp.Models.CategoryModel;
import ml.adityabodhankar.mobileapp.Models.ProductModel;

public class AddProductActivity extends AppCompatActivity {

    private EditText productName, productPrice, productDescription;
    private RadioButton pickupYes, pickUpNo;
    private TextView addProductBtn;
    private ProgressBar progress;
    private CircleImageView productImage;
    private Button pickImageBtn;
    private Uri imageUri;
    private boolean isUpdate = false;
    private FirebaseFirestore db;
    private ProductModel productData;
    private boolean isPickup;
    private String category;
    private List<CategoryModel> categories;
    private Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Add Product");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(view -> finish());
        } catch (Exception ignored) {
        }

//        initialize the local variables
        productName = findViewById(R.id.product_name_inp);
        productPrice = findViewById(R.id.product_price_inp);
        productDescription = findViewById(R.id.product_desc_inp);
        progress = findViewById(R.id.add_product_loading);
        productImage = findViewById(R.id.product_image_view);
        pickUpNo = findViewById(R.id.product_pickup_no);
        pickupYes = findViewById(R.id.product_pickup_yes);
        addProductBtn = findViewById(R.id.add_product_btn);
        pickImageBtn = findViewById(R.id.product_pick_image_btn);
        dropdown = findViewById(R.id.dropdown_category);

        db = FirebaseFirestore.getInstance();

        pickImageBtn.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        });
        isPickup = false;

        pickupYes.setOnClickListener(view -> isPickup = true);
        pickUpNo.setOnClickListener(view -> isPickup = false);

        addProductBtn.setOnClickListener(view -> addProduct());

//        load all categories available

        showLoading(true);

        db.collection("categories").get()
                .addOnSuccessListener(snapshots -> {
                    categories = new ArrayList<>();
                    categories.add(new CategoryModel("null", "None", "default"));
                    for (QueryDocumentSnapshot s : snapshots) {
                        categories.add(new CategoryModel(s.getData()));
                    }
                    String[] categoryArray = new String[categories.size()];
                    for (int i = 0; i < categories.size(); i++) {
                        categoryArray[i] = categories.get(i).getTitle();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, categoryArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dropdown.setAdapter(adapter);
                    dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            category = categories.get(i).getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                    checkForIsUpdate();
                }).addOnFailureListener(e -> {
                });

    }

    private void checkForIsUpdate() {
        showLoading(false);
        Intent intent = getIntent();
        try {
            String id = intent.getStringExtra("productId");
            if (id != null) {
                isUpdate = true;
                showLoading(true);
                db.collection("products").document(id).get()
                        .addOnSuccessListener(snap -> {
                            showLoading(false);
                            if (snap.exists()) {
                                productData = new ProductModel(Objects.requireNonNull(snap.getData()));
                                if (productData.getImage().equalsIgnoreCase("default")) {
                                    Glide.with(this).load(productData.getImage()).into(productImage);
                                }
                                productName.setText(productData.getName());
                                productPrice.setText(productData.getPrice());
                                productDescription.setText(productData.getDescription());
                                for (int i = 0; i < categories.size(); i++) {
                                    if (categories.get(i).getId().equalsIgnoreCase(productData.getCategory())) {
                                        dropdown.setSelection(i);
                                    }
                                }
                                Objects.requireNonNull(getSupportActionBar()).setTitle("Update Product");
                                addProductBtn.setText("Update Product");
                            } else {
                                Toast.makeText(this, "Unable to load data", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, "Unable to load the data", Toast.LENGTH_SHORT).show();
                            finish();
                        });
            }
        } catch (Exception ignored) {
        }
    }

    private void addProduct() {
        if (!validateData()) {
            finish();
            return;
        }
        showLoading(true);
        String productName = this.productName.getText().toString();
        if (isUpdate) {
            uploadImage(productName);
        } else {
            db.collection("products").document(productName).get()
                    .addOnSuccessListener(snap -> {
                        if (snap.exists()) {
                            showLoading(false);
                            AlertDialog.Builder alert = new AlertDialog.Builder(this);
                            alert.setCancelable(false)
                                    .setTitle("Error")
                                    .setMessage("Product with same name/ID exists")
                                    .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                        } else {
                            uploadImage(productName);
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this,
                                "Unable to check data. Please try again later.", Toast.LENGTH_SHORT).show();
                        showLoading(false);
                    });
        }
    }


    private boolean validateData() {
        if (productName.getText().toString().equalsIgnoreCase("")) {
            productName.setError("Required Field");
            return false;
        }
        if (productPrice.getText().toString().equalsIgnoreCase("")) {
            productPrice.setError("Required Field");
            return false;
        }
        if (productDescription.getText().toString().equalsIgnoreCase("")) {
            productDescription.setError("Required Field");
            return false;
        }
        if (category.equalsIgnoreCase("")) {
            Toast.makeText(this, "Category Needed", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void uploadImage(String productName) {
        if (imageUri == null) {
            if (isUpdate) {
                //perform updating the name
                updateData(false, "");
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Warning!")
                        .setMessage("No image selected. Do you want to continue with default image?")
                        .setCancelable(false)
                        .setNegativeButton("NO", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            showLoading(false);
                        }).setPositiveButton("YES", (dialogInterface, i) -> {
                            //add category
                            addProductData("default");
                        });
                alert.show();
            }
        } else {
            //add image first
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("products/" + productName + ".png");
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Objects.requireNonNull(bmp).compress(Bitmap.CompressFormat.JPEG, 10, baos);
            byte[] data = baos.toByteArray();
            storageRef.putBytes(data).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> task = Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl();
                task.addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    if (isUpdate) {
                        updateData(true, imageUrl);
                    } else {
                        addProductData(imageUrl);
                    }
                }).addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(this, "Error => " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e -> {
                showLoading(false);
                Toast.makeText(this, "Error => " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void addProductData(String imageUrl) {
        ProductModel product = new ProductModel(productName.getText().toString(),
                productName.getText().toString(),
                imageUrl,
                productPrice.getText().toString(),
                productDescription.getText().toString(),
                category,
                isPickup);
        db.collection("products").document(product.getId()).set(product)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show();
                    showLoading(false);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Unable to create product", Toast.LENGTH_SHORT).show();
                    showLoading(false);
                });
    }

    private void updateData(boolean isImageUpdated, String imageUrl) {
        Map<String, Object> updateData = new HashMap<>();
        if (isImageUpdated) {
            updateData.put("image", imageUrl);
        }
        updateData.put("name", productName.getText().toString());
        updateData.put("price", productPrice.getText().toString());
        updateData.put("description", productDescription.getText().toString());
        updateData.put("category", category);
        updateData.put("pickup", isPickup);
        db.collection("products").document(productData.getId()).update(updateData)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Product Updated", Toast.LENGTH_SHORT).show();
                    showLoading(false);
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Fail to update Product", Toast.LENGTH_SHORT).show();
                    showLoading(false);
                });
    }


    private void showLoading(boolean status) {
        if (status) {
            //show loading
            progress.setVisibility(View.VISIBLE);
            addProductBtn.setVisibility(View.GONE);
        } else {
            //hide loading
            progress.setVisibility(View.GONE);
            addProductBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(productImage);
        }
    }
}