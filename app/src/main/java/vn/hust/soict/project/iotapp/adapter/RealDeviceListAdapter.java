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

import java.util.List;

import vn.hust.soict.project.iotapp.R;
import vn.hust.soict.project.iotapp.model.Area;
import vn.hust.soict.project.iotapp.model.Device;

public class RealDeviceListAdapter extends RecyclerView.Adapter<RealDeviceListAdapter.RealDeviceViewHolder> {
private Context context;
private List<Device> list;
private ItemClickListener clickListener;

public RealDeviceListAdapter(Context context, List<Device> list, ItemClickListener clickListener ){
    this.context = context;
    this.list = list;
    this.clickListener = clickListener;
}
public void setList(List<Device> list){
    this.list = list;
    notifyDataSetChanged();
}

    @NonNull
    @Override
    public RealDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_garden, parent, false);
        return new RealDeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RealDeviceViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvName.setText("Real device: " + this.list.get(position).getName());
//        holder.tvAreaPosition.setText(this.list.get(position).getArea());
//      if(position % 3 == 0){
//          holder.imgArea.setImageResource(R.drawable.area1);
//      } else if(position % 3 == 1){
//          holder.imgArea.setImageResource(R.drawable.area2);
//      } else {
//          holder.imgArea.setImageResource(R.drawable.area3);
//      }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onRealClick(list.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        if(this.list != null) {
            return this.list.size();
        }
        return 0;
    }

    public class RealDeviceViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgArea, imgEditArea, imgDeleteArea;
        private TextView tvName, tvAreaPosition;
        public RealDeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            imgArea = itemView.findViewById(R.id.imgGarden);
            imgEditArea = itemView.findViewById(R.id.imgEditGarden);
            imgDeleteArea = itemView.findViewById(R.id.imgDeleteGarden);
            tvName = itemView.findViewById(R.id.tvGardenName);
            tvAreaPosition = itemView.findViewById(R.id.tvGardenAddress);
            imgEditArea.setVisibility(View.GONE);
            imgDeleteArea.setVisibility(View.GONE);
            tvAreaPosition.setVisibility(View.GONE);
            imgArea.setVisibility(View.GONE);
        }
    }
    public interface ItemClickListener{
         void onRealClick(Device device);
    }
}
