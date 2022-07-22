package ml.adityabodhankar.mobileapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import ml.adityabodhankar.mobileapp.Models.ProductModel;
import ml.adityabodhankar.mobileapp.R;

public class ProductAdapter extends ArrayAdapter<ProductModel> {
    ArrayList<ProductModel> products;
    public ProductAdapter(@NonNull Context context, int resource, ArrayList<ProductModel> products) {
        super(context, resource, products);
        this.products = products;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.recycler_product, parent, false);
        }
        ProductModel product = products.get(position);
        listitemView.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Click", Toast.LENGTH_SHORT).show();
        });
        return listitemView;
    }
}
