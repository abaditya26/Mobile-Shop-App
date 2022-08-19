package ml.adityabodhankar.mobileapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ml.adityabodhankar.mobileapp.Models.CartModel;
import ml.adityabodhankar.mobileapp.R;

public class CheckoutCartAdapter extends RecyclerView.Adapter<CheckoutCartAdapter.ViewHolder> {
    private final Context context;
    private final List<CartModel> cartProducts;

    public CheckoutCartAdapter(Context context, List<CartModel> cartProducts) {
        this.context = context;
        this.cartProducts = cartProducts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.recycler_checkout_products, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(cartProducts.get(position));
    }

    @Override
    public int getItemCount() {
        return cartProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView name, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.product_image);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
        }

        @SuppressLint("SetTextI18n")
        public void setData(CartModel cartProduct) {
            name.setText(cartProduct.getProductName());
            final DecimalFormat df = new DecimalFormat("0.00");
            double total = cartProduct.getQuantity() * Double.parseDouble(cartProduct.getProductPrice());
            price.setText("Rs." + cartProduct.getProductPrice() + " X " + cartProduct.getQuantity() + " = Rs." + df.format(total));
        }
    }
}
