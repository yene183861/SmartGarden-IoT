//package vn.hust.soict.project.iotapp.ui;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.os.Message;
//import android.os.Messenger;
//import android.os.RemoteException;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import java.util.List;
//
//import vn.hust.soict.project.iotapp.MyService;
//import vn.hust.soict.project.iotapp.R;
//import vn.hust.soict.project.iotapp.adapter.GardenListAdapter;
//import vn.hust.soict.project.iotapp.model.Garden;
//import vn.hust.soict.project.iotapp.model.User;
//import vn.hust.soict.project.iotapp.viewmodel.GardenListViewModel;
//
//public class GardenManageActivity extends AppCompatActivity implements GardenListAdapter.ItemClickListener{
//    private ImageView btnAddNewGarden;
//    private TextView tvNoGardenList;
//    private RecyclerView rcvGarden;
//    private GardenListViewModel gardenListViewModel;
//    private List<Garden> gardenList;
//    private GardenListAdapter adapter;
//    private Garden gardenForEdit;
//    private Messenger messenger;
//    private boolean isServiceConnected;
//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            messenger = new Messenger(service);
//            isServiceConnected = true;
//            //send message connect mqtt
//            sendMessageConnectMQTT();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            messenger = null;
//            isServiceConnected = false;
//        }
//    };
//
//    private void sendMessageConnectMQTT() {
//        Message message = Message.obtain(null, MyService.MSG_CONNECT_MQTT, 0, 0);
//        try {
//            messenger.send(message);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
//
//    User user;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_garden_manage);
//
//        user = (User) getIntent().getSerializableExtra("user");
//        //bound service
//        Intent intent = new Intent(this, MyService.class);
//        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
//
//        btnAddNewGarden = findViewById(R.id.btnAddNewGarden);
//        rcvGarden = findViewById(R.id.rcvGarden);
//        tvNoGardenList = findViewById(R.id.tvNoGardenList);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        rcvGarden.setLayoutManager(linearLayoutManager);
//        adapter = new GardenListAdapter(this ,gardenList, this);
//        rcvGarden.setAdapter(adapter);
//
//        gardenListViewModel = new ViewModelProvider(this).get(GardenListViewModel.class);
//        gardenListViewModel.getGardenListObserver().observe(this, new Observer<List<Garden>>() {
//            @Override
//            public void onChanged(List<Garden> gardens) {
//                if(gardens == null){
//                    rcvGarden.setVisibility(View.GONE);
//                    tvNoGardenList.setVisibility(View.VISIBLE);
//                } else {
//                    gardenList = gardens;
//                    adapter.setGardenList(gardens);
//                    //rcvGarden.setAdapter(adapter);
//                    tvNoGardenList.setVisibility(View.GONE);
//                    rcvGarden.setVisibility(View.VISIBLE);
//                }
//
//            }
//        });
//        gardenListViewModel.getGardenList();
//        btnAddNewGarden.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showAddDialog(false);
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        if(isServiceConnected){
//            unbindService(serviceConnection);
//            isServiceConnected = false;
//        }
//        super.onDestroy();
//    }
//
//    private void showAddDialog(boolean isEdit){
//        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
//        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_garden, null);
//        TextView addGardenTitleDialog = dialogView.findViewById(R.id.addGardenTitleDialog);
//        EditText enterNameGarden = dialogView.findViewById(R.id.enterNameGarden);
//        EditText enterAddressGarden = dialogView.findViewById(R.id.enterAddressGarden);
//        EditText enterAcreageGarden = dialogView.findViewById(R.id.enterAcreageGarden);
//        EditText enterArea = dialogView.findViewById(R.id.enterArea);
//        TextView btnCreate = dialogView.findViewById(R.id.btnCreate);
//        TextView btnCancel = dialogView.findViewById(R.id.btnCancel);
////        ImageView chooseGardenImage = dialogView.findViewById(R.id.chooseGardenImage);
////        imageHome = dialogView.findViewById(R.id.imageHome);
//    //    LinearLayout layout = dialogView.findViewById(R.id.layoutImgGarden);
//        //layout.setVisibility(View.GONE);
//
//        if(isEdit){
//            addGardenTitleDialog.setText("Update information garden");
//            btnCreate.setText("Update");
//            enterNameGarden.setText(gardenForEdit.getName());
//            enterAddressGarden.setText(gardenForEdit.getAddress());
//            enterAcreageGarden.setText(String.valueOf(gardenForEdit.getAcreage()));
//            enterArea.setText(String.valueOf(gardenForEdit.getArea()));
//        }
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogBuilder.dismiss();
//            }
//        });
//        btnCreate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name = enterNameGarden.getText().toString().trim();
//                String addr = enterAddressGarden.getText().toString().trim();
//                int area = Integer.parseInt(enterArea.getText().toString().trim());
//                int acreage = Integer.parseInt(enterAcreageGarden.getText().toString().trim());
//
//                //check fields empty
//
//                if(isEdit){
//                    gardenForEdit.setName(name);
//                    gardenForEdit.setAddress(addr);
//                    gardenForEdit.setAcreage(acreage);
//                    gardenForEdit.setArea(acreage);
//                    gardenListViewModel.updateGarden(gardenForEdit.getId(), gardenForEdit);
//                } else {
//                    //call view model
//                    Garden garden = new Garden(user.getId(), name, addr, area, acreage);
//                    gardenListViewModel.insertGarden(garden);
//                }
//                dialogBuilder.dismiss();
//            }
//        });
//        dialogBuilder.setView(dialogView);
//        dialogBuilder.show();
//    }
//    @Override
//    public void onGardenClick(Garden garden) {
//        Intent intent = new Intent(GardenManageActivity.this, DeviceManageActivity.class);
//        intent.putExtra("garden", garden);
//        startActivity(intent);
//    }
//
//    @Override
//    public void onGardenEditClick(Garden garden) {
//        this.gardenForEdit = garden;
//        showAddDialog(true);
//    }
//
//    @Override
//    public void onGardenDeleteClick(Garden garden) {
//        gardenListViewModel.deleteGarden(garden.getId());
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
