package ml.adityabodhankar.mobileapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.List;

import ml.adityabodhankar.mobileapp.Models.CartModel;
import ml.adityabodhankar.mobileapp.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    private final Context context;
    private final List<CartModel> cartProducts;
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    public CartAdapter(Context context, List<CartModel> cartProducts) {
        this.context = context;
        this.cartProducts = cartProducts;
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.recycler_cart_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(cartProducts, position);
    }

    @Override
    public int getItemCount() {
        return cartProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView productName, productPrice, totalPrice, totalQuantity;
        private final ImageView productImage, deleteProductBtn, reduceQuantity, increaseQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.cart_product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            totalPrice = itemView.findViewById(R.id.product_price_total);
            totalQuantity = itemView.findViewById(R.id.product_qty);
            productImage = itemView.findViewById(R.id.cart_image);
            deleteProductBtn = itemView.findViewById(R.id.delete_product_cart);
            reduceQuantity = itemView.findViewById(R.id.remove_qty);
            increaseQuantity = itemView.findViewById(R.id.add_qty);
        }

        public void setData(List<CartModel> cartProducts, int position) {
            productName.setText(cartProducts.get(position).getProductName());
            productPrice.setText("Rs."+cartProducts.get(position).getProductPrice());
            totalQuantity.setText(""+cartProducts.get(position).getQuantity());
            final DecimalFormat df = new DecimalFormat("0.00");
            double total = cartProducts.get(position).getQuantity() * Double.parseDouble(cartProducts.get(position).getProductPrice());
            totalPrice.setText("Rs."+df.format(total));
            if (!cartProducts.get(position).getProductImage().equalsIgnoreCase("default")){
                Glide.with(context).load(cartProducts.get(position).getProductImage()).into(productImage);
            }
            deleteProductBtn.setOnClickListener(view -> db.collection("users").document(auth.getCurrentUser().getUid())
                    .collection("cart").document(cartProducts.get(position).getProductId())
                    .delete().addOnSuccessListener(unused -> Toast.makeText(context, "Product Removed From Cart", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(context, "Unable to remove from cart.", Toast.LENGTH_SHORT).show()));
            reduceQuantity.setOnClickListener(view -> {
                if (cartProducts.get(position).getQuantity() > 1) {
                    cartProducts.get(position).setQuantity(cartProducts.get(position).getQuantity() - 1);
                    db.collection("users").document(auth.getCurrentUser().getUid())
                            .collection("cart").document(cartProducts.get(position).getProductId())
                            .set(cartProducts.get(position)).addOnSuccessListener(unused -> Toast.makeText(context, "Quantity Reduced", Toast.LENGTH_SHORT).show()).
                            addOnFailureListener(e -> Toast.makeText(context, "Unable to reduce quantity", Toast.LENGTH_SHORT).show());
                }else{
                    db.collection("users").document(auth.getCurrentUser().getUid())
                            .collection("cart").document(cartProducts.get(position).getProductId())
                            .delete().addOnSuccessListener(unused -> Toast.makeText(context, "Product Removed From Cart", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(context, "Unable to remove from cart.", Toast.LENGTH_SHORT).show());
                }
            });
            increaseQuantity.setOnClickListener(view -> {
                cartProducts.get(position).setQuantity(cartProducts.get(position).getQuantity() + 1);
                db.collection("users").document(auth.getCurrentUser().getUid())
                        .collection("cart").document(cartProducts.get(position).getProductId())
                        .set(cartProducts.get(position)).addOnSuccessListener(unused -> Toast.makeText(context, "Quantity Increased", Toast.LENGTH_SHORT).show()).
                        addOnFailureListener(e -> Toast.makeText(context, "Unable to increase quantity", Toast.LENGTH_SHORT).show());
            });
        }
    }
}
