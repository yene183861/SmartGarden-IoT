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
import vn.hust.soict.project.iotapp.model.Area;
import vn.hust.soict.project.iotapp.model.Garden;

public class AreaListAdapter extends RecyclerView.Adapter<AreaListAdapter.AreaViewHolder> {
private Context context;
private List<Area> areaList;
private ItemClickListener clickListener;

public AreaListAdapter(Context context, List<Area> areaList, ItemClickListener clickListener ){
    this.context = context;
    this.areaList = areaList;
    this.clickListener = clickListener;
}
public void setAreaList(List<Area> areaList){
    this.areaList = areaList;
    notifyDataSetChanged();
}

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);
        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvAreaName.setText(this.areaList.get(position).getName());
        holder.tvAreaPosition.setText(this.areaList.get(position).getPosition());
//        if(this.gardenList.get(position).getImage() == null){
            holder.imgArea.setImageResource(R.drawable.garden);
//        } else {
//            Glide.with(context)
//                    .load(this.gardenList.get(position).getImage())
//                    .apply(RequestOptions.centerCropTransform())
//                    .into(holder.imgGarden);
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onAreaClick(areaList.get(position));
            }
        });

        holder.imgEditArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onAreaEditClick(areaList.get(position));
            }
        });
        holder.imgDeleteArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onAreaDeleteClick(areaList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(this.areaList != null) {
            return this.areaList.size();
        }
        return 0;
    }

    public class AreaViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgArea, imgEditArea, imgDeleteArea;
        private TextView tvAreaName, tvAreaPosition;
        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgArea = itemView.findViewById(R.id.imgGarden);
            imgEditArea = itemView.findViewById(R.id.imgEditArea);
            imgDeleteArea = itemView.findViewById(R.id.imgDeleteArea);
            tvAreaName = itemView.findViewById(R.id.tvAreaName);
            tvAreaPosition = itemView.findViewById(R.id.tvAreaPosition);
        }
    }
    public interface ItemClickListener{
         void onAreaClick(Area area);
         void onAreaEditClick(Area area);
         void onAreaDeleteClick(Area area);
    }
}