package ml.adityabodhankar.mobileapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ml.adityabodhankar.mobileapp.Adapter.AdminCategoryAdapter;
import ml.adityabodhankar.mobileapp.AddCategoryActivity;
import ml.adityabodhankar.mobileapp.Models.CategoryModel;
import ml.adityabodhankar.mobileapp.R;

public class CategoriesFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView categoriesRecycler;
    private TextView noCategories;
    private List<CategoryModel> categories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        db = FirebaseFirestore.getInstance();
        categoriesRecycler = view.findViewById(R.id.admin_categories_recycler);
        noCategories =view.findViewById(R.id.no_categories_text);

        categoriesRecycler.setVisibility(View.VISIBLE);
        noCategories.setVisibility(View.GONE);

        categoriesRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        db.collection("categories").addSnapshotListener((snapshots, error) -> {
            if (snapshots != null) {
                categories = new ArrayList<>();
                for (DocumentSnapshot snapshot : snapshots) {
                    categories.add(
                            new CategoryModel(Objects.requireNonNull(snapshot.getData())));
                }
                if (categories.size() == 0){
                    categoriesRecycler.setVisibility(View.GONE);
                    noCategories.setVisibility(View.VISIBLE);
                }else{
                    categoriesRecycler.setVisibility(View.VISIBLE);
                    noCategories.setVisibility(View.GONE);
                    categoriesRecycler.setAdapter(new AdminCategoryAdapter(getContext(), categories));
                }
            } else {
                Toast.makeText(requireContext(), "Error:- " + Objects.requireNonNull(error).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_add_category);
        fab.setOnClickListener(view1 -> {
            startActivity(new Intent(requireContext(), AddCategoryActivity.class));
        });
        return view;
    }
}