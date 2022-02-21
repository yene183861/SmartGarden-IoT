package vn.hust.soict.project.iotapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.hust.soict.project.iotapp.R;
import vn.hust.soict.project.iotapp.adapter.AreaListAdapter;
import vn.hust.soict.project.iotapp.model.Area;
import vn.hust.soict.project.iotapp.model.Garden;
import vn.hust.soict.project.iotapp.viewmodel.AreaListViewModel;

public class AreaManageActivity extends AppCompatActivity implements AreaListAdapter.ItemClickListener {
    private ImageView btnAddNewArea;
    private TextView tvNoAreaList, txtGardenName, txtGardenAddress, txtArea, txtGardenAcreage;
    private RecyclerView rcvArea;
    private AreaListViewModel areaListViewModel;
    private List<Area> areaList = new ArrayList<>();
    private AreaListAdapter adapter;
    private Area areaForEdit;
    private Garden garden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_manage);

        garden = (Garden) getIntent().getSerializableExtra("garden");
        getSupportActionBar().setTitle("Manage Area");

        initUi();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvArea.setLayoutManager(linearLayoutManager);
        adapter = new AreaListAdapter(this, areaList, this);
        rcvArea.setAdapter(adapter);
        areaListViewModel = new ViewModelProvider(this).get(AreaListViewModel.class);
        areaList = areaListViewModel.getAreaList(garden.getId());
        areaListViewModel.getAreaListObserver().observe(this, new Observer<List<Area>>() {
            @Override
            public void onChanged(List<Area> areas) {
                if (areas == null) {
                    rcvArea.setVisibility(View.GONE);
                    tvNoAreaList.setVisibility(View.VISIBLE);
                } else {
                    areaList = areas;
                    adapter.setAreaList(areas);
                    tvNoAreaList.setVisibility(View.GONE);
                    rcvArea.setVisibility(View.VISIBLE);
                }

            }
        });
        btnAddNewArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog(false);
            }
        });
    }
    private void initUi(){
        btnAddNewArea = findViewById(R.id.btnAddNewArea);
        rcvArea = findViewById(R.id.rcvArea);
        tvNoAreaList = findViewById(R.id.tvNoAreaList);
        txtGardenName = findViewById(R.id.txtGardenName);
        txtGardenAddress = findViewById(R.id.txtGardenAddress);
        txtGardenAcreage = findViewById(R.id.txtGardenAcreage);
        txtArea = findViewById(R.id.txtArea);
        txtGardenName.setText("Name garden: " + garden.getName());
        txtGardenAddress.setText("Address: " + garden.getAddress());
        //txtArea.setText("Area: " + String.valueOf(garden.getArea()));
        txtGardenAcreage.setText("Acreage:" + String.valueOf(garden.getAcreage()) + " (m2)");
    }


    private void showAddDialog(boolean isEdit) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_garden, null);
        TextView addAreaTitleDialog = dialogView.findViewById(R.id.addTitleDialog);
        EditText enterNameArea = dialogView.findViewById(R.id.enterName);
        EditText enterPositionArea = dialogView.findViewById(R.id.enterAddress);
        EditText enterAcreage = dialogView.findViewById(R.id.enterAcreage);
        TextView btnCreateArea = dialogView.findViewById(R.id.btnCreate);
        TextView btnCancelArea = dialogView.findViewById(R.id.btnCancel);
        TextView enterArea = dialogView.findViewById(R.id.enterArea);
        enterArea.setVisibility(View.GONE);


        if (isEdit) {
            addAreaTitleDialog.setText("Update information area");
            btnCreateArea.setText("Update");
            enterNameArea.setText(areaForEdit.getName());
            enterPositionArea.setText(areaForEdit.getPosition());
            enterAcreage.setText(String.valueOf(areaForEdit.getAcreage()));
        }

        btnCancelArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        btnCreateArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = enterNameArea.getText().toString().trim();
                String position = enterPositionArea.getText().toString().trim();
                double acreage = Double.parseDouble(enterAcreage.getText().toString().trim());

                //check fields empty

                //call view model
                if (isEdit) {
                    areaForEdit.setName(name);
                    areaForEdit.setPosition(position);
                    areaForEdit.setAcreage(acreage);
                    areaListViewModel.updateArea(areaForEdit);
                } else {
                    //call view model
//                    Area area = new Area(garden.getId(), name, position);
//                    areaListViewModel.insertArea(area);
                }
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    @Override
    public void onAreaClick(Area area) {
        Intent intent = new Intent(AreaManageActivity.this, DeviceManageActivity.class);
        intent.putExtra("area", area);
        startActivity(intent);
    }

    @Override
    public void onAreaEditClick(Area area) {
        this.areaForEdit = area;
        showAddDialog(true);
    }

    @Override
    public void onAreaDeleteClick(Area area) {
        areaListViewModel.deleteArea(area);
    }
}

















