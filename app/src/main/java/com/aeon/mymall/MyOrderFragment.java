package com.aeon.mymall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrderFragment extends Fragment {

    public MyOrderFragment() {
        // Required empty public constructor
    }

    private RecyclerView myOrdersRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);

        myOrdersRecyclerView = view.findViewById(R.id.my_orders_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrdersRecyclerView.setLayoutManager(layoutManager);

        List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.mobile,"Apple XR1",3,"Delivered on Mon, 15th Aug 2022"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.mobile_1,"Iphone 3",2,"Delivered on Mon, 25th Aug 2022"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.mobile_2,"Xiomi A5",1,"Cancelled"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.mobile_3,"Samsung M33 5G",4,"Delivered on Mon, 10th Aug 2022"));

        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(myOrderItemModelList);
        myOrdersRecyclerView.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();

        return view;
    }
}