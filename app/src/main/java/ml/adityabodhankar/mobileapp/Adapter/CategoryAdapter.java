package ml.adityabodhankar.mobileapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ml.adityabodhankar.mobileapp.CommonData;
import ml.adityabodhankar.mobileapp.Fragments.HomeFragment;
import ml.adityabodhankar.mobileapp.Models.CategoryModel;
import ml.adityabodhankar.mobileapp.Models.ProductModel;
import ml.adityabodhankar.mobileapp.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    List<CategoryModel> categories;

    public CategoryAdapter(Context context, List<CategoryModel> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.recycler_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.setData(categories, position);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.category_image);
            title = itemView.findViewById(R.id.category_title);
        }

        public void setData(List<CategoryModel> categories, int position) {
            title.setText(categories.get(position).getTitle());
            if(!categories.get(position).getImage().equalsIgnoreCase("default")){
                Glide.with(context).load(categories.get(position).getImage()).into(image);
            }
            itemView.setOnClickListener(view -> {
                ArrayList<ProductModel> products = new ArrayList<>();
                for (ProductModel p : CommonData.products) {
                    if(p.getCategory().equalsIgnoreCase(categories.get(position).getId())){
                        products.add(p);
                    }
                }
                if(categories.get(position).getId().equalsIgnoreCase("0")){
                    products = CommonData.products;
                }
                HomeFragment.productsView.setAdapter(
                        new ProductAdapter(context, 0, products));
            });
        }
    }
}
