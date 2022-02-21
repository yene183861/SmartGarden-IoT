package vn.hust.soict.project.iotapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import vn.hust.soict.project.iotapp.R;
import vn.hust.soict.project.iotapp.model.Garden;

public class GardenListAdapter extends RecyclerView.Adapter<GardenListAdapter.GardenViewHolder> {
  private Context context;
  private List<Garden> gardenList;
  private ItemClickListener clickListener;

  public GardenListAdapter(Context context, List<Garden> gardenList, ItemClickListener clickListener ){
    this.context = context;
    this.gardenList = gardenList;
    this.clickListener = clickListener;
  }
  public void setGardenList(List<Garden> gardenList){
    this.gardenList = gardenList;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public GardenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_garden, parent, false);
    return new GardenViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull GardenViewHolder holder, @SuppressLint("RecyclerView") int position) {
    holder.tvGardenName.setText(this.gardenList.get(position).getName());
    holder.tvGardenAddress.setText(this.gardenList.get(position).getAddress());
    if(position % 2 == 0){
      holder.imgGarden.setImageResource(R.drawable.garden);
    } else {
      holder.imgGarden.setImageResource(R.drawable.garden2);
    }

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        clickListener.onGardenClick(gardenList.get(position));
      }
    });

    holder.imgEditGarden.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        clickListener.onGardenEditClick(gardenList.get(position));
      }
    });
    holder.imgDeleteGarden.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        clickListener.onGardenDeleteClick(gardenList.get(position));
      }
    });
  }

  @Override
  public int getItemCount() {
    if(this.gardenList != null) {
      return this.gardenList.size();
    }
    return 0;
  }

  public class GardenViewHolder extends RecyclerView.ViewHolder {
    private ImageView imgGarden, imgEditGarden, imgDeleteGarden;
    private TextView tvGardenName, tvGardenAddress;
    public GardenViewHolder(@NonNull View itemView) {
      super(itemView);
      imgGarden = itemView.findViewById(R.id.imgGarden);
      imgEditGarden = itemView.findViewById(R.id.imgEditGarden);
      imgDeleteGarden = itemView.findViewById(R.id.imgDeleteGarden);
      tvGardenName = itemView.findViewById(R.id.tvGardenName);
      tvGardenAddress = itemView.findViewById(R.id.tvGardenAddress);
    }
  }
  public interface ItemClickListener{
    void onGardenClick(Garden garden);
    void onGardenEditClick(Garden garden);
    void onGardenDeleteClick(Garden garden);
  }
}