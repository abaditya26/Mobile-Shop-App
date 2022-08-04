package ml.adityabodhankar.mobileapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

import ml.adityabodhankar.mobileapp.Adapter.AdminProductAdapter;
import ml.adityabodhankar.mobileapp.AddProductActivity;
import ml.adityabodhankar.mobileapp.Models.ProductModel;
import ml.adityabodhankar.mobileapp.R;

public class ProductsFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView productsRecycler;
    private ArrayList<ProductModel> products;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        db = FirebaseFirestore.getInstance();
        productsRecycler = view.findViewById(R.id.admin_product_recycler);
        productsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        db.collection("products").addSnapshotListener((snapshots, error) -> {
            if (snapshots != null) {
                products = new ArrayList<>();
                for (DocumentSnapshot snapshot : snapshots) {
                    products.add(
                            new ProductModel(Objects.requireNonNull(snapshot.getData())));
                }
                productsRecycler.setAdapter(new AdminProductAdapter(requireContext(), products));
            } else {
                Toast.makeText(requireContext(), "Error:- " + Objects.requireNonNull(error).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_add_product);
        fab.setOnClickListener(view1 -> {
            startActivity(new Intent(requireContext(), AddProductActivity.class));
        });
        return view;
    }
}