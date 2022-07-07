package com.aeon.mymall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import static com.aeon.mymall.DeliveryActivity.SELECT_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {

    private RecyclerView myAddressesRecyclerView;
    private Button deliverHereBtn;
    private static AddressesAdapter addressesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Addresses");

        myAddressesRecyclerView = findViewById(R.id.addresses_recycler_view);
        deliverHereBtn = findViewById(R.id.deliver_here_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myAddressesRecyclerView.setLayoutManager(layoutManager);

        List<AddressesModel> addressesModelList = new ArrayList<>();
        addressesModelList.add(new AddressesModel("Abc Xyz", "Viman Nagar, Pune", "422112",true));
        addressesModelList.add(new AddressesModel("Job Xyz", "Janak Nagar, Pune", "412112", false));
        addressesModelList.add(new AddressesModel("Tom Xyz", "Mk Nagar, Pune", "400112", false));
        addressesModelList.add(new AddressesModel("Som Xyz", "Abc Nagar, Pune", "2323232", false));
        addressesModelList.add(new AddressesModel("Git Xyz", "OM Nagar, Pune", "421112", false));

        int mode = getIntent().getIntExtra("MODE",-1);
        if (mode == SELECT_ADDRESS){
            deliverHereBtn.setVisibility(View.VISIBLE);
        } else {
            deliverHereBtn.setVisibility(View.GONE);
        }

        addressesAdapter = new AddressesAdapter(addressesModelList, mode);
        myAddressesRecyclerView.setAdapter(addressesAdapter);
        ((SimpleItemAnimator)myAddressesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        addressesAdapter.notifyDataSetChanged();

    }

    public static void refreshItem(int deselect,int select){
        addressesAdapter.notifyItemChanged(deselect);
        addressesAdapter.notifyItemChanged(select);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}