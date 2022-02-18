package vn.hust.soict.project.iotapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.hust.soict.project.iotapp.R;
import vn.hust.soict.project.iotapp.model.Device;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder> {
    private Context context;
    private List<Device> deviceList;
    private ItemClickListener clickListener;

    public DeviceListAdapter(Context context, List<Device> gardenList, ItemClickListener clickListener) {
        this.context = context;
        this.deviceList = deviceList;
        this.clickListener = clickListener;
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvName.setText("Name: " + this.deviceList.get(position).getName());
        holder.value.setText(""+this.deviceList.get(position).getValue());
        holder.tvPosition.setText("Position: " + this.deviceList.get(position).getPosition());
        int type = this.deviceList.get(position).getType();
        int imgCode = R.drawable.temper;
        boolean status = this.deviceList.get(position).isStatus();
        if (status) {
            holder.tvStatus.setText("Status: On");
        } else
            holder.tvStatus.setText("Status: Off");
        //holder.control.setChecked(status);
        switch (type) {
            case 1:
                imgCode = R.drawable.temper;
//                holder.control.setVisibility(View.GONE);
                break;
            case 2: //2 đo độ ẩm đất
                imgCode = R.drawable.soil_moisture;
//                if (status) {
//                    imgCode = R.drawable.wartering;
//                } else {
//                    imgCode = R.drawable.not_watering;
//                }
                break;
            case 3:
                imgCode = R.drawable.air_humidity;
                //holder.control.setVisibility(View.GONE);
                break;
            default: //4 la den
                imgCode = R.drawable.brightness;
//                if (status) {
//                    imgCode = R.drawable.lamp_on;
//                } else {
//                    imgCode = R.drawable.lamp_off;
//                }
//                break;
        }
        holder.img.setImageResource(imgCode);

//        holder.control.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                clickListener.onControlClick(deviceList.get(position), isChecked);
//            }
//        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onEditClick(deviceList.get(position));
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onDeleteClick(deviceList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.deviceList != null) {
            return this.deviceList.size();
        }
        return 0;
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {
        private ImageView img, btnEdit, btnDelete;
        private TextView tvName, tvPosition, tvStatus, value;
        private Switch control;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            tvName = itemView.findViewById(R.id.tvName);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            value = itemView.findViewById(R.id.value);
            //control = itemView.findViewById(R.id.control);
        }
    }

    public interface ItemClickListener {
        void onControlClick(Device device, boolean isChecked);

        void onEditClick(Device device);

        void onDeleteClick(Device device);
    }
}