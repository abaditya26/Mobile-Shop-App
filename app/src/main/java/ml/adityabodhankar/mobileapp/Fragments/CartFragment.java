package ml.adityabodhankar.mobileapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ml.adityabodhankar.mobileapp.Adapter.CartAdapter;
import ml.adityabodhankar.mobileapp.CheckoutActivity;
import ml.adityabodhankar.mobileapp.Models.CartModel;
import ml.adityabodhankar.mobileapp.R;

public class CartFragment extends Fragment {

    private List<CartModel> cartProducts;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private RecyclerView cartView;
    private TextView total;
    double cartTotal = 0.0;
    private LinearLayout noProductView;
    private RelativeLayout mainCartView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        cartProducts = new ArrayList<>();
        total = view.findViewById(R.id.cart_total);
        noProductView = view.findViewById(R.id.cart_no_products);
        mainCartView = view.findViewById(R.id.main_cart_layout);
        noProductView.setVisibility(View.GONE);
        mainCartView.setVisibility(View.VISIBLE);
        cartView = view.findViewById(R.id.cart_recycler_view);
        cartView.setLayoutManager(new LinearLayoutManager(requireContext()));

        db.collection("users").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("cart").addSnapshotListener((value, error) -> {
                    if (value != null){
                        cartProducts.clear();
                        cartTotal = 0;
                        mainCartView.setVisibility(View.VISIBLE);
                        noProductView.setVisibility(View.GONE);
                        for (QueryDocumentSnapshot snap : value){
                            cartTotal += ((long)snap.get("quantity") *
                                    Double.parseDouble((String) Objects.requireNonNull(snap.get("productPrice"))));
                            cartProducts.add(new CartModel(snap.getData()));
                        }
                        //set UI
                        setUI();
                    }else{
                        Toast.makeText(requireContext(), "Error to get data from DB", Toast.LENGTH_SHORT).show();
                    }
                });
        view.findViewById(R.id.checkout_btn).setOnClickListener(view1 ->
                startActivity(new Intent(requireContext(), CheckoutActivity.class)));
        return view;
    }

    private void setUI() {
        if (cartProducts.size() == 0){
            mainCartView.setVisibility(View.GONE);
            noProductView.setVisibility(View.VISIBLE);
        }
        final DecimalFormat df = new DecimalFormat("0.00");
        total.setText("Rs."+df.format(cartTotal));
        cartView.setAdapter(new CartAdapter(getContext(), cartProducts));
    }
}