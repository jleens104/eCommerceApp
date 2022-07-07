package com.aeon.mymall;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.slider.BaseOnChangeListener;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private List<WishlistModel> wishlistModelList;
    private Boolean wishlist;
    private int lastPosition = -1;

    public WishlistAdapter(List<WishlistModel> wishlistModelList, Boolean wishlist) {
        this.wishlistModelList = wishlistModelList;
        this.wishlist = wishlist;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlist_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String productId = wishlistModelList.get(position)
                .getProductId();
        String resource = wishlistModelList.get(position)
                .getProductImage();
        String title = wishlistModelList.get(position)
                .getProductTitle();
        long freeCoupans = wishlistModelList.get(position)
                .getFreeCoupans();
        String rating = wishlistModelList.get(position)
                .getRating();
        long totalRatings = wishlistModelList.get(position)
                .getTotalRatings();
        String productPrice = wishlistModelList.get(position)
                .getProductPrice();
        String cuttedPrice = wishlistModelList.get(position)
                .getCuttedPrice();
        boolean paymentMethod = wishlistModelList.get(position)
                .isCod();

        viewHolder.setData(productId, resource, title, freeCoupans, rating, totalRatings, productPrice, cuttedPrice, paymentMethod, position);


        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return wishlistModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle;
        private TextView freeCoupans;
        private ImageView coupanIcon;
        private TextView rating;
        private TextView totalRatings;
        private View PriceCut;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView paymentMethod;
        private ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupans = itemView.findViewById(R.id.free_coupans);
            coupanIcon = itemView.findViewById(R.id.coupan_icon);
            rating = itemView.findViewById(R.id.tv_product_rating_miniview);
            totalRatings = itemView.findViewById(R.id.total_ratings);
       //     PriceCut = itemView.findViewById(R.id.price_cut);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            paymentMethod = itemView.findViewById(R.id.payment_method);
            deleteBtn = itemView.findViewById(R.id.delete_btn);

        }

        private void setData(String productId, String resource, String title, long freeCoupansNo, String averageRate, long totalRatingsNo, String price, String cuttedPriceValue, boolean cod, int index ) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.icon_placeholder_product)).into(productImage);
            productTitle.setText(title);
            if (freeCoupansNo != 0) {
                coupanIcon.setVisibility(View.VISIBLE);
                if (freeCoupansNo == 1) {
                    freeCoupans.setText("free " + freeCoupansNo + " coupon");
                } else {
                    freeCoupans.setText("free " + freeCoupansNo + " coupons");
                }
            } else {
                coupanIcon.setVisibility(View.INVISIBLE);
                freeCoupans.setVisibility(View.INVISIBLE);
            }
            rating.setText(averageRate);
            totalRatings.setText("("+totalRatingsNo+")ratings");
            productPrice.setText("Rs."+price+"/-");
            cuttedPrice.setText("Rs."+cuttedPriceValue+"/-");
            if (cod){
                paymentMethod.setVisibility(View.VISIBLE);
            } else {
                paymentMethod.setVisibility(View.INVISIBLE);
            }

            if (wishlist){
                deleteBtn.setVisibility(View.VISIBLE);
            } else {
                deleteBtn.setVisibility(View.GONE);
            }

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ProductDetailsActivity.running_wishlist_query){
                        ProductDetailsActivity.running_wishlist_query = true;
                        DBqueries.removeFromWishlist(index, itemView.getContext());
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    intent.putExtra("PRODUCT_ID", productId);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
