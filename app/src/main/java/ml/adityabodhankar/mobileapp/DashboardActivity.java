package ml.adityabodhankar.mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import ml.adityabodhankar.mobileapp.Adapter.CategoryAdapter;
import ml.adityabodhankar.mobileapp.Models.CategoryModel;

public class DashboardActivity extends AppCompatActivity {

    private List<CategoryModel> categories;

    private RecyclerView categoriesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        categories = new ArrayList<>();
        categories.add(new CategoryModel("1","Product 1","default"));
        categories.add(new CategoryModel("2","Product 2","default"));
        categories.add(new CategoryModel("3","Product 3","default"));
        categories.add(new CategoryModel("4","Product 4","default"));
        categories.add(new CategoryModel("5","Product 5","default"));
        categories.add(new CategoryModel("6","Product 6","default"));


        categoriesRecycler = findViewById(R.id.category_recycler);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.setAdapter(new CategoryAdapter(this, categories));
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
}