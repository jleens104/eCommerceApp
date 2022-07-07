package com.aeon.mymall;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.Viewholder> {

    private List<RewardModel> rewardModelList;
    private Boolean useMiniLayout = false;

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (useMiniLayout) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.mini_rewards_item_layout, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.rewards_item_layout, viewGroup, false);
        }
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position)   {
        String title = rewardModelList.get(position).getTitle();
        String date = rewardModelList.get(position).getExpiryDate();
        String body = rewardModelList.get(position).getCoupanBody();

        viewholder.setData(title,date,body);
    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        private TextView coupanTitle;
        private TextView coupanExpiryDate;
        private TextView coupanBody;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            coupanTitle = itemView.findViewById(R.id.coupan_title) ;
            coupanExpiryDate = itemView.findViewById(R.id.coupan_validity);
            coupanBody = itemView.findViewById(R.id.coupan_body);
        }

        private void setData(String title, String date, String body) {
            coupanTitle.setText(title);
            coupanExpiryDate.setText(date);
            coupanBody.setText(body);

            if (useMiniLayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProductDetailsActivity.coupanTitle.setText(title);
                        ProductDetailsActivity.coupanExpiryDate.setText(date);
                        ProductDetailsActivity.coupanBody.setText(body);
                        ProductDetailsActivity.showDialogRecyclerView();
                    }
                });
            }
        }
    }
}
