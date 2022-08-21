package ml.adityabodhankar.mobileapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

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

    @SuppressLint("StaticFieldLeak")
    public static GridView productsView;
    private TextView noProducts;
    private List<CategoryModel> categories;
    private ArrayList<ProductModel> products;
    private RecyclerView categoriesRecycler;
    private boolean productsLoaded = false;
    private boolean categoriesLoaded = false;
    private LinearLayout progressLayout, mainLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        categoriesRecycler = view.findViewById(R.id.category_recycler);
        categoriesRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
                        false));
        productsView = view.findViewById(R.id.products_grid);
        noProducts = view.findViewById(R.id.no_products_found);
        progressLayout = view.findViewById(R.id.progress_products);
        mainLayout = view.findViewById(R.id.main_screen);
        progressLayout.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
        db.collection("categories").get()
                .addOnSuccessListener(snapshots -> {
                    categoriesLoaded = false;
                    setLoading();
                    categories = new ArrayList<>();
                    categories.add(new CategoryModel("0", "All", "default"));
                    for (DocumentSnapshot snapshot : snapshots) {
                        categories.add(
                                new CategoryModel(Objects.requireNonNull(snapshot.getData())));
                    }
                    categoriesRecycler.setAdapter(new CategoryAdapter(getContext(), categories));
                    categoriesLoaded = true;
                    setLoading();
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                });
        db.collection("products")
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        productsLoaded = false;
                        setLoading();
                        products = new ArrayList<>();
                        for (DocumentSnapshot snapshot : value) {
                            products.add(new ProductModel(Objects.requireNonNull(snapshot.getData())));
                        }
                        if (products.size() == 0){
                            noProducts.setVisibility(View.VISIBLE);
                            productsView.setVisibility(View.GONE);
                        }else {
                            noProducts.setVisibility(View.GONE);
                            productsView.setVisibility(View.VISIBLE);
                            CommonData.products = products;
                            productsView.setAdapter(
                                    new ProductAdapter(requireContext(), 0, products));
                        }
                        productsLoaded = true;
                        setLoading();
                    }else{
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });

        noProducts.setVisibility(View.GONE);
        productsView.setVisibility(View.VISIBLE);
        return view;
    }

    private void setLoading() {
        if (productsLoaded && categoriesLoaded) {
            //view data
            progressLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        } else {
            //show loading
            progressLayout.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.GONE);
        }
    }
}