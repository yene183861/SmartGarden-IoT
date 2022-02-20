package vn.hust.soict.project.iotapp.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.hust.soict.project.iotapp.MyService;
import vn.hust.soict.project.iotapp.R;
import vn.hust.soict.project.iotapp.adapter.GardenListAdapter;
import vn.hust.soict.project.iotapp.datalocal.DataLocalManager;
import vn.hust.soict.project.iotapp.model.Garden;
import vn.hust.soict.project.iotapp.model.User;
import vn.hust.soict.project.iotapp.viewmodel.GardenListViewModel;

public class GardenManageActivity extends AppCompatActivity implements GardenListAdapter.ItemClickListener {
    private ImageView btnAddNewGarden;
    private TextView tvNoGardenList;
    private RecyclerView rcvGarden;
    private GardenListViewModel gardenListViewModel;
    private List<Garden> gardenList = new ArrayList<>();
    private GardenListAdapter adapter;
    private Garden gardenForEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_manage);
        getSupportActionBar().setTitle("Manage Garden");
        btnAddNewGarden = findViewById(R.id.btnAddNewGarden);
        rcvGarden = findViewById(R.id.rcvGarden);
        tvNoGardenList = findViewById(R.id.tvNoGardenList);
        Garden garden1 = new Garden("DataLocalManager.getClientId()", "Vuon Dau Tay", "Mê Linh", 4, 120);
        Garden garden2 = new Garden("DataLocalManager.getClientId()", "Vuon Nho", "Mê Linh", 4, 120);
        gardenList.add(garden1);
        gardenList.add(garden2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvGarden.setLayoutManager(linearLayoutManager);
        adapter = new GardenListAdapter(this, gardenList, this);
        rcvGarden.setAdapter(adapter);

        gardenListViewModel = new ViewModelProvider(this).get(GardenListViewModel.class);
        gardenListViewModel.getGardenListObserver().observe(this, new Observer<List<Garden>>() {
            @Override
            public void onChanged(List<Garden> gardens) {
                if (gardens == null) {
                    rcvGarden.setVisibility(View.GONE);
                    tvNoGardenList.setVisibility(View.VISIBLE);
                } else {
                    gardenList = gardens;
                    adapter.setGardenList(gardens);
                    //rcvGarden.setAdapter(adapter);
                    tvNoGardenList.setVisibility(View.GONE);
                    rcvGarden.setVisibility(View.VISIBLE);
                }

            }
        });
        // gardenListViewModel.getGardenList();
        btnAddNewGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog(false);
            }
        });
    }

    private void showAddDialog(boolean isEdit) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_garden, null);
        TextView addGardenTitleDialog = dialogView.findViewById(R.id.addTitleDialog);
        EditText enterNameGarden = dialogView.findViewById(R.id.enterName);
        EditText enterAddressGarden = dialogView.findViewById(R.id.enterAddress);
        EditText enterAcreageGarden = dialogView.findViewById(R.id.enterAcreage);
        EditText enterArea = dialogView.findViewById(R.id.enterArea);
        TextView btnCreate = dialogView.findViewById(R.id.btnCreate);
        TextView btnCancel = dialogView.findViewById(R.id.btnCancel);

        if (isEdit) {
            addGardenTitleDialog.setText("Update information garden");
            btnCreate.setText("Update");
            enterNameGarden.setText(gardenForEdit.getName());
            enterAddressGarden.setText(gardenForEdit.getAddress());
            enterAcreageGarden.setText(String.valueOf(gardenForEdit.getAcreage()));
            enterArea.setText(String.valueOf(gardenForEdit.getArea()));
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = enterNameGarden.getText().toString().trim();
                String addr = enterAddressGarden.getText().toString().trim();
                int area = Integer.parseInt(enterArea.getText().toString().trim());
                double acreage = Double.parseDouble(enterAcreageGarden.getText().toString().trim());

                //check fields empty

                if (isEdit) {
                    gardenForEdit.setName(name);
                    gardenForEdit.setAddress(addr);
                    gardenForEdit.setAcreage(acreage);
                    gardenForEdit.setArea(area);
                    gardenListViewModel.updateGarden(gardenForEdit.getId(), gardenForEdit);
                } else {
                    //call view model
                    Garden garden = new Garden("DataLocalManager.getClientId()", name, addr, area, acreage);
                    gardenList.add(garden);
                    adapter.setGardenList(gardenList);
                    gardenListViewModel.insertGarden(garden);
                }
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    @Override
    public void onGardenClick(Garden garden) {
        Intent intent = new Intent(GardenManageActivity.this, AreaManageActivity.class);
        //intent.putExtra("garden", garden);
        startActivity(intent);
    }

    @Override
    public void onGardenEditClick(Garden garden) {
        this.gardenForEdit = garden;
        showAddDialog(true);
    }

    @Override
    public void onGardenDeleteClick(Garden garden) {
        gardenList.remove(garden);
        gardenListViewModel.deleteGarden(garden);
    }
}


















