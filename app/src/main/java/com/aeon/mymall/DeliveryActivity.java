package com.aeon.mymall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.aeon.mymall.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class DeliveryActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private RecyclerView deliveryRecyclerView;
    private Button changedOrAddAddressBtn;
    public static final int SELECT_ADDRESS = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

       /* binding = ActivityMainBinding.inflate(getLayoutInflater());
      //  setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");*/


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Delivery");


        deliveryRecyclerView = findViewById(R.id.delivery_recycler_view);
        changedOrAddAddressBtn = findViewById(R.id.change_or_add_address_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(layoutManager);

        List<CartItemModel> cartItemModelList = new ArrayList<>();
        cartItemModelList.add(new CartItemModel(0,R.drawable.mobile,"Apple Iphone XR",2,"Rs.49,999/-","Rs.59,999/-",1,0,0));
        cartItemModelList.add(new CartItemModel(0,R.drawable.mobile,"Apple Iphone XR",0,"Rs.49,999/-","Rs.59,999/-",1,1,0));
        cartItemModelList.add(new CartItemModel(0,R.drawable.mobile,"Apple Iphone XR",2,"Rs.49,999/-","Rs.59,999/-",1,2,0));
        cartItemModelList.add(new CartItemModel(1,"Price (3 items)","Rs.49,9999/-","Free","Rs.55,5999","Rs.4999/-"));

        CartAdapter cartAdapter = new CartAdapter(cartItemModelList);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changedOrAddAddressBtn.setVisibility(View.VISIBLE);
        changedOrAddAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
                intent.putExtra("MODE",SELECT_ADDRESS);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}