package ml.adityabodhankar.mobileapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ml.adityabodhankar.mobileapp.databinding.ActivityHomeBinding;


public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

//        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_cart, R.id.navigation_orders,
                R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout_btn_menu_item) {
            CommonData.signOut(getApplicationContext());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                    .child("users/" + Objects.requireNonNull(auth.getCurrentUser()).getUid() + ".png");
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Objects.requireNonNull(bmp).compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data1 = baos.toByteArray();
            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Loading");
            pd.setMessage("Uploading Image Please Wait");
            pd.setCancelable(false);
            pd.show();
            storageRef.putBytes(data1).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> task = Objects.requireNonNull(
                        Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl();
                task.addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("profile", imageUrl);
                    db.collection("users").document(auth.getCurrentUser().getUid())
                                    .update(updateData)
                                    .addOnSuccessListener(unused -> {
                                        pd.dismiss();
                                        Toast.makeText(this, "Profile Image Updated",
                                                Toast.LENGTH_SHORT).show();
                                    }).addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to update image",
                                                Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    });
                }).addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(this, "Error => " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e -> {
                pd.dismiss();
                Toast.makeText(this, "Error => " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}