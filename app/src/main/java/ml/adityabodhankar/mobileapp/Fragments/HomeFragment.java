package ml.adityabodhankar.mobileapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ml.adityabodhankar.mobileapp.Adapter.CategoryAdapter;
import ml.adityabodhankar.mobileapp.Adapter.ProductAdapter;
import ml.adityabodhankar.mobileapp.CommonData;
import ml.adityabodhankar.mobileapp.Models.CategoryModel;
import ml.adityabodhankar.mobileapp.Models.ProductModel;
import ml.adityabodhankar.mobileapp.R;

public class HomeFragment extends Fragment {

    private List<CategoryModel> categories;
    private ArrayList<ProductModel> products;
    private RecyclerView categoriesRecycler;
    @SuppressLint("StaticFieldLeak")
    public static GridView productsView;
    private FirebaseFirestore db;
    private boolean productsLoaded = false;
    private boolean categoriesLoaded = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();
        categoriesRecycler = view.findViewById(R.id.category_recycler);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        productsView = view.findViewById(R.id.products_grid);

        db.collection("categories").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    categories = new ArrayList<>();
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                });
        db.collection("products").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    products = new ArrayList<>();
                }).addOnFailureListener(e -> {
                });

        categories = new ArrayList<>();
        categories.add(new CategoryModel("0","All","default"));
        categories.add(new CategoryModel("1","Category 1","default"));
        categories.add(new CategoryModel("2","Category 2","default"));
        categories.add(new CategoryModel("3","Category 3","default"));
        categories.add(new CategoryModel("4","Category 4","default"));
        categories.add(new CategoryModel("5","Category 5","default"));
        categories.add(new CategoryModel("6","Category 6","default"));

        categoriesRecycler.setAdapter(new CategoryAdapter(getContext(), categories));

//        products section
        products = new ArrayList<>();
        products.add(new ProductModel("1","Product 1","default","100","Product 1 desc","1"));
        products.add(new ProductModel("2","Product 2","default","230","Product 2 desc","1"));
        products.add(new ProductModel("3","Product 3","default","130","Product 3 desc","1"));
        products.add(new ProductModel("4","Product 4","default","120","Product 4 desc","1"));
        products.add(new ProductModel("5","Product 5","default","150","Product 5 desc","2"));
        products.add(new ProductModel("5","Product 6","default","150","Product 6 desc","2"));
        products.add(new ProductModel("5","Product 7","default","150","Product 7 desc","2"));
        products.add(new ProductModel("5","Product 8","default","150","Product 8 desc","3"));
        products.add(new ProductModel("5","Product 9","default","150","Product 9 desc","4"));
        products.add(new ProductModel("5","Product 10","default","150","Product 10 desc","5"));
        products.add(new ProductModel("5","Product 11","default","150","Product 11 desc","5"));
        CommonData.products = products;

        productsView.setAdapter(new ProductAdapter(requireContext(), 0, products));
        return  view;
    }
}