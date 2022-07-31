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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
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
        progressLayout = view.findViewById(R.id.progress_products);
        mainLayout = view.findViewById(R.id.main_screen);
        progressLayout.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
        db.collection("categories").get()
                .addOnSuccessListener(snapshots -> {
                    categoriesLoaded = false;
                    setLoading();
                    categories = new ArrayList<>();
                    categories.add(new CategoryModel("0","All","default"));
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
        db.collection("products").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productsLoaded = false;
                    setLoading();
                    products = new ArrayList<>();
                    for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                        products.add(new ProductModel(Objects.requireNonNull(snapshot.getData())));
                    }
                    CommonData.products = products;
                    productsView.setAdapter(
                            new ProductAdapter(requireContext(), 0, products));
                    productsLoaded = true;
                    setLoading();
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                });
        return  view;
    }

    private void setLoading(){
        if(productsLoaded && categoriesLoaded){
            //view data
            progressLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        }else{
            //show loading
            progressLayout.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.GONE);
        }
    }
}