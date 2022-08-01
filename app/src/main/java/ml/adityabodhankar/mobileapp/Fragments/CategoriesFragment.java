package ml.adityabodhankar.mobileapp.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ml.adityabodhankar.mobileapp.Adapter.AdminCategoryAdapter;
import ml.adityabodhankar.mobileapp.Adapter.CategoryAdapter;
import ml.adityabodhankar.mobileapp.Models.CategoryModel;
import ml.adityabodhankar.mobileapp.R;

public class CategoriesFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView categoriesRecycler;
    private List<CategoryModel> categories;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_categories, container, false);
        db = FirebaseFirestore.getInstance();
        categoriesRecycler = view.findViewById(R.id.admin_categories_recycler);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        db.collection("categories").addSnapshotListener((snapshots, error) -> {
            if (snapshots != null) {
                categories = new ArrayList<>();
                for (DocumentSnapshot snapshot : snapshots) {
                    categories.add(
                            new CategoryModel(Objects.requireNonNull(snapshot.getData())));
                }
                categoriesRecycler.setAdapter(new AdminCategoryAdapter(getContext(), categories));
            }else{
                Toast.makeText(requireContext(), "Error:- "+ Objects.requireNonNull(error).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return  view;
    }
}