package com.aeon.mymall;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.api.Distribution;

import java.util.ArrayList;
import java.util.List;


public class MyCartFragment extends Fragment {

    public MyCartFragment() {
        // Required empty public constructor
    }

    private RecyclerView cartItemRecyclerView;
    private Button continueBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        cartItemRecyclerView = view.findViewById(R.id.cart_items_recycler_view);
        continueBtn = view.findViewById(R.id.cart_continue_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemRecyclerView.setLayoutManager(layoutManager);

        List<CartItemModel> cartItemModelList = new ArrayList<>();
        cartItemModelList.add(new CartItemModel(0,R.drawable.mobile,"Apple Iphone XR",2,"Rs.49,999/-","Rs.59,999/-",1,0,0));
        cartItemModelList.add(new CartItemModel(0,R.drawable.mobile,"Apple Iphone XR",0,"Rs.49,999/-","Rs.59,999/-",1,1,0));
        cartItemModelList.add(new CartItemModel(0,R.drawable.mobile,"Apple Iphone XR",2,"Rs.49,999/-","Rs.59,999/-",1,2,0));
        cartItemModelList.add(new CartItemModel(1,"Price (3 items)","Rs.49,9999/-","Free","Rs.55,5999","Rs.4999/-"));


        CartAdapter cartAdapter = new CartAdapter(cartItemModelList);
        cartItemRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();



        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddAddressActivity.class);
                getContext().startActivity(intent);
            }
        });
        return view;
    }
}