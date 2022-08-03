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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import ml.adityabodhankar.mobileapp.Models.CategoryModel;
import ml.adityabodhankar.mobileapp.R;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.ViewHolder> {
    private Context context;
    List<CategoryModel> categories;

    public AdminCategoryAdapter(Context context, List<CategoryModel> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.recycler_admin_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(categories, position);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView title;
        ImageButton deleteBtn;
        private FirebaseFirestore db = FirebaseFirestore.getInstance();
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.admin_category_image);
            title = itemView.findViewById(R.id.admin_category_title);
            deleteBtn = itemView.findViewById(R.id.admin_category_delete);
        }

        public void setData(List<CategoryModel> categories, int position) {
            title.setText(categories.get(position).getTitle());
            if (!categories.get(position).getImage().equalsIgnoreCase("default")){
                Glide.with(context).load(categories.get(position).getImage()).into(image);
            }
            deleteBtn.setOnClickListener(view -> {
                db.collection("categories").document(categories.get(position).getId()).delete()
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(context, "Category Deleted", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(context, "Error to delete category.", Toast.LENGTH_SHORT).show();
                        });
            });
        }
    }
}
