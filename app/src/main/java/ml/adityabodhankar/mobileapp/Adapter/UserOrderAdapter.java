package ml.adityabodhankar.mobileapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ml.adityabodhankar.mobileapp.Models.OrderModel;
import ml.adityabodhankar.mobileapp.OrderDetailsActivity;
import ml.adityabodhankar.mobileapp.R;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.ViewHolder> {
    private final Context context;
    private final List<OrderModel> orders;


    public UserOrderAdapter(Context context, List<OrderModel> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.recycler_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderName, orderStatus;
        CircleImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderName = itemView.findViewById(R.id.order_name);
            orderStatus = itemView.findViewById(R.id.order_status);
            image = itemView.findViewById(R.id.order_image);
        }

        public void setData(OrderModel orderData) {
            orderName.setText(orderData.getOrderTitle());
            orderStatus.setText(orderData.getOrderStatus());
            if (!orderData.getOrderImage().equalsIgnoreCase("default")){
                Glide.with(context).load(orderData.getOrderImage()).into(image);
            }
            itemView.setOnClickListener(view -> {
                Intent i = new Intent(context, OrderDetailsActivity.class);
                i.putExtra("id", orderData.getOrderId());
                context.startActivity(i);
            });
        }
    }
}
