package ml.adityabodhankar.mobileapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import ml.adityabodhankar.mobileapp.Models.CategoryModel;

public class AddCategoryActivity extends AppCompatActivity {

    boolean isUpdate = false;
    private FirebaseFirestore db;
    private EditText categoryNameInp;
    private CircleImageView categoryImage;
    private Uri imageUri;
    private ProgressBar progress;
    private TextView addCategoryBtn;
    private CategoryModel categoryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Add Category");
        } catch (Exception ignored) {
        }

        addCategoryBtn = findViewById(R.id.add_category_btn);
        categoryNameInp = findViewById(R.id.category_name_inp);
        categoryImage = findViewById(R.id.category_image_view);
        Button pickImageBtn = findViewById(R.id.pick_image_btn);
        progress = findViewById(R.id.add_category_loading);

        db = FirebaseFirestore.getInstance();

        pickImageBtn.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        });

        addCategoryBtn.setOnClickListener(view -> addCategory());

        showLoading(false);
        Intent intent = getIntent();
        try {
            String id = intent.getStringExtra("categoryId");
            if (id != null) {
                isUpdate = true;
                showLoading(true);
                db.collection("categories").document(id).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            showLoading(false);
                            if (documentSnapshot.exists()) {
                                categoryData = new CategoryModel((String) documentSnapshot.get("id"), (String) documentSnapshot.get("title"), (String) documentSnapshot.get("image"));
                                if (!categoryData.getImage().equalsIgnoreCase("default")) {
                                    Glide.with(this).load(categoryData.getImage()).into(categoryImage);
                                }
                                categoryNameInp.setText(categoryData.getTitle());
                            } else {
                                Toast.makeText(this, "Unable to load data", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, "Unable to load data", Toast.LENGTH_SHORT).show();
                            finish();
                        });
            }
        } catch (Exception ignored) {
        }
    }

    private void addCategory() {
        if (categoryNameInp.getText().toString().equalsIgnoreCase("")) {
            categoryNameInp.setError("Enter Category");
            return;
        }

        showLoading(true);
        String categoryName = categoryNameInp.getText().toString();
        if (isUpdate) {
            //perform updating data
            uploadImage(categoryName);
        } else {
            db.collection("categories").document(categoryName).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            showLoading(false);
                            AlertDialog.Builder alert = new AlertDialog.Builder(this);
                            alert.setCancelable(false)
                                    .setTitle("Error")
                                    .setMessage("Category with same name/ID exists")
                                    .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                        } else {
                            uploadImage(categoryName);
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(AddCategoryActivity.this,
                                "Unable to check data. Please try again later.", Toast.LENGTH_SHORT).show();
                        showLoading(false);
                    });
        }
    }

    private void uploadImage(String categoryName) {
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
                            addCategoryData("default");
                        });
                alert.show();
            }
        } else {
            //add image first
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("categories/" + categoryName + ".png");
            storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> task = Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl();
                task.addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    if (isUpdate) {
                        updateData(true, imageUrl);
                    } else {
                        addCategoryData(imageUrl);
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

    private void updateData(boolean isImageUpdated, String imageUrl) {
        Toast.makeText(this, "TODO:update data", Toast.LENGTH_SHORT).show();
    }

    private void addCategoryData(String imageUrl) {
        CategoryModel category = new CategoryModel(
                categoryNameInp.getText().toString(),
                categoryNameInp.getText().toString(),
                imageUrl);
        db.collection("categories").document(category.getId()).set(category)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Category Added", Toast.LENGTH_SHORT).show();
                    showLoading(false);
                    finish();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Unable to create category", Toast.LENGTH_SHORT).show();
                    showLoading(false);
                });
    }

    private void showLoading(boolean status) {
        if (status) {
            //show loading
            progress.setVisibility(View.VISIBLE);
            addCategoryBtn.setVisibility(View.GONE);
        } else {
            //hide loading
            progress.setVisibility(View.GONE);
            addCategoryBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(categoryImage);
        }
    }
}