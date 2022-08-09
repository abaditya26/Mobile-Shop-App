package ml.adityabodhankar.mobileapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ml.adityabodhankar.mobileapp.Adapter.CartAdapter;
import ml.adityabodhankar.mobileapp.Models.CartModel;
import ml.adityabodhankar.mobileapp.R;

public class CartFragment extends Fragment {

    private List<CartModel> cartProducts;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private RecyclerView cartView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        cartProducts = new ArrayList<>();
        cartView = view.findViewById(R.id.cart_recycler_view);
        cartView.setLayoutManager(new LinearLayoutManager(requireContext()));

        db.collection("users").document(auth.getCurrentUser().getUid())
                .collection("cart").addSnapshotListener((value, error) -> {
                    if (value != null){
                        cartProducts.clear();
                        for (QueryDocumentSnapshot snap : value){
                            cartProducts.add(new CartModel(snap.getData()));
                        }
                        //set UI
                        setUI();
                    }else{
                        Toast.makeText(requireContext(), "Error to get data from DB", Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }

    private void setUI() {

        cartView.setAdapter(new CartAdapter(getContext(), cartProducts));
    }
}