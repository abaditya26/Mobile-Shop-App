package ml.adityabodhankar.mobileapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ml.adityabodhankar.mobileapp.Models.ProductModel;
import ml.adityabodhankar.mobileapp.R;

public class ProductAdapter extends ArrayAdapter<ProductModel> {
    ArrayList<ProductModel> products;
    ImageView image;
    TextView text;
    public ProductAdapter(@NonNull Context context, int resource, ArrayList<ProductModel> products) {
        super(context, resource, products);
        this.products = products;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.recycler_product, parent, false);
        }
        image = itemView.findViewById(R.id.category_image);
        text = itemView.findViewById(R.id.category_title);
        ProductModel product = products.get(position);
        text.setText(product.getName());
        if(!product.getImage().equalsIgnoreCase("default")){
            Glide.with(getContext()).load(product.getImage()).into(image);
        }
        itemView.setOnClickListener(view -> {
        });
        return itemView;
    }
}
