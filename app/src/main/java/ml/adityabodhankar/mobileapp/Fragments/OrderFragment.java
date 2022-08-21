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

import ml.adityabodhankar.mobileapp.Adapter.UserOrderAdapter;
import ml.adityabodhankar.mobileapp.Models.OrderModel;
import ml.adityabodhankar.mobileapp.R;

public class OrderFragment extends Fragment {

    private List<OrderModel> orders;
    private RecyclerView orderRecyclerView;
    private TextView noOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        orders = new ArrayList<>();
        orderRecyclerView = view.findViewById(R.id.orders_recycler);
        noOrders = view.findViewById(R.id.no_order_text);

        noOrders.setVisibility(View.GONE);
        orderRecyclerView.setVisibility(View.VISIBLE);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        db.collection("orders").whereEqualTo("uid",
                        Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        orders.clear();
                        for (QueryDocumentSnapshot snapshot : value) {
                            orders.add(new OrderModel(snapshot.getData()));
                        }
                        if (orders.size() == 0) {
                            noOrders.setVisibility(View.VISIBLE);
                            orderRecyclerView.setVisibility(View.GONE);
                        } else {
                            noOrders.setVisibility(View.GONE);
                            orderRecyclerView.setVisibility(View.VISIBLE);
                            //setUI
                            orderRecyclerView.setAdapter(new UserOrderAdapter(getContext(), orders));
                        }
                    } else {
                        Toast.makeText(requireContext(), "Unable to get the data from cloud", Toast.LENGTH_SHORT).show();
                    }
                });
        return view;
    }
}