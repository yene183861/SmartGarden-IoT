package vn.hust.soict.project.iotapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.hust.soict.project.iotapp.R;
import vn.hust.soict.project.iotapp.adapter.GardenListAdapter;
import vn.hust.soict.project.iotapp.model.Garden;
import vn.hust.soict.project.iotapp.viewmodel.GardenListViewModel;

public class AddGarden extends AppCompatActivity {
    private GardenListViewModel gardenListViewModel;
    private List<Garden> gardenList;
    private GardenListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_garden);

        EditText enterNameGarden = findViewById(R.id.enterNameGarden);
        EditText enterAddressGarden = findViewById(R.id.enterAddressGarden);
        EditText enterAcreageGarden = findViewById(R.id.enterAcreageGarden);
        EditText enterArea = findViewById(R.id.enterArea);
        EditText enterImage = findViewById(R.id.enterImage);
        TextView btnCreate = findViewById(R.id.btnCreate);
        TextView btnCancel = findViewById(R.id.btnCancel);
        ImageView chooseGardenImage = findViewById(R.id.chooseGardenImage);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(AddGarden.this, GardenManageActivity.class);
//                startActivity(intent);
//                finishAffinity();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = enterNameGarden.getText().toString().trim();
                String addr = enterAddressGarden.getText().toString().trim();
                int acreage = Integer.parseInt(enterAcreageGarden.getText().toString().trim());
                int area = Integer.parseInt(enterArea.getText().toString().trim());
                String image = enterImage.getText().toString().trim();

                //check fields empty

//                    Garden garden = new Garden("id",name, addr, acreage, area);
//                    Intent intent = new Intent(AddGarden.this, GardenManageActivity.class);
//                intent.putExtra("add_garden", garden);
//                startActivity(intent);
                }
        });

    }

}