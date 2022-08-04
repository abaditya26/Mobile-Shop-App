package ml.adityabodhankar.mobileapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ml.adityabodhankar.mobileapp.Models.ProductModel;
import ml.adityabodhankar.mobileapp.R;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder> {
    Context context;
    List<ProductModel> products;

    public AdminProductAdapter(Context context, List<ProductModel> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.recycler_admin_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(products, position);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CircleImageView image;
        TextView title;
        ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.admin_product_image);
            title = itemView.findViewById(R.id.admin_product_title);
            deleteBtn = itemView.findViewById(R.id.admin_product_delete);
        }

        public void setData(List<ProductModel> products, int position) {
            title.setText(products.get(position).getName());
            if (!products.get(position).getImage().equalsIgnoreCase("default")) {
                Glide.with(context).load(products.get(position).getImage()).into(image);
            }
            deleteBtn.setOnClickListener(view -> db.collection("products").document(products.get(position).getId()).delete()
                    .addOnSuccessListener(unused -> Toast.makeText(context, "Product Deleted.", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(context, "Product delete fail.", Toast.LENGTH_SHORT).show()));
            itemView.setOnClickListener(view -> {
//                Intent intent = new Intent(context, AddProductActivity.class);
//                intent.putExtra("categoryId", products.get(position).getId());
//                context.startActivity(intent);
            });
        }
    }
}
