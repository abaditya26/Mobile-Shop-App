package ml.adityabodhankar.mobileapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ml.adityabodhankar.mobileapp.Models.TrackingModel;
import ml.adityabodhankar.mobileapp.R;

public class TrackingAdapter extends RecyclerView.Adapter<TrackingAdapter.ViewHolder> {

    private final List<TrackingModel> trackingDetails;
    private Context context;

    public TrackingAdapter(Context context, List<TrackingModel> trackingDetails) {
        this.context = context;
        this.trackingDetails = trackingDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(layoutInflater.inflate(R.layout.recycler_status, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(trackingDetails.get(position));
    }

    @Override
    public int getItemCount() {
        return trackingDetails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final private TextView date, time, title, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.tracking_date);
            time = itemView.findViewById(R.id.tracking_time);
            title = itemView.findViewById(R.id.tracking_title);
            description = itemView.findViewById(R.id.tracking_description);
        }

        public void setData(TrackingModel trackingModel) {
            String date = trackingModel.getDateTime().split(" ")[0];
            String time = trackingModel.getDateTime().split(" ")[1].split(":")[0] + ":"
                    + trackingModel.getDateTime().split(" ")[1].split(":")[1];
            this.date.setText(date);
            this.time.setText(time);
            title.setText(trackingModel.getStatus());
            description.setText(trackingModel.getDescription());
        }
    }
}
