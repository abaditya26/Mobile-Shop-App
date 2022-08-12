package ml.adityabodhankar.mobileapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ml.adityabodhankar.mobileapp.Adapter.AdminOrdersAdapter;
import ml.adityabodhankar.mobileapp.Adapter.UserOrderAdapter;
import ml.adityabodhankar.mobileapp.Models.OrderModel;
import ml.adityabodhankar.mobileapp.R;

public class AdminOrdersFragment extends Fragment {

    private RecyclerView ordersRecyclerView;
    private FirebaseFirestore db;
    private List<OrderModel> orders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_orders, container, false);
        ordersRecyclerView = view.findViewById(R.id.admin_orders_recycler);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();

        orders = new ArrayList<>();

        db.collection("orders")
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        orders.clear();
                        for (QueryDocumentSnapshot snapshot : value){
                            try {
                                if ((boolean) snapshot.get("paid")) {
                                    orders.add(new OrderModel(snapshot.getData()));
                                }
                            }catch (Exception ignored){
                            }
                        }
                        //setUI
                        ordersRecyclerView.setAdapter(new AdminOrdersAdapter(getContext(), orders));
                    }else{
                        Toast.makeText(requireContext(), "Unable to get the data from cloud", Toast.LENGTH_SHORT).show();
                    }
                });
        return view;
    }
}