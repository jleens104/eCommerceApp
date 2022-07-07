package com.aeon.mymall;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.time.temporal.Temporal;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;

    public CartAdapter(List<CartItemModel> cartItemModelList) {
        this.cartItemModelList = cartItemModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()){
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType){
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout, viewGroup, false);
                return new CartItemViewHolder(cartItemView);
            case CartItemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amount_layout, viewGroup, false);
                return new CartTotatAmountViewHolder(cartTotalView);
            default:
                return null;
        }    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewholder, int position) {
        switch (cartItemModelList.get(position).getType()){
            case CartItemModel.CART_ITEM:
                int resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                int freeCoupans = cartItemModelList.get(position).getFreeCoupans();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                int offerApplied = cartItemModelList.get(position).getOffersApplied();

                ((CartItemViewHolder)viewholder).setItemDetails(resource,title,freeCoupans, productPrice, cuttedPrice, offerApplied);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                String totalItems = cartItemModelList.get(position).getTotalItems();
                String totalItemPrice = cartItemModelList.get(position).getTotalItemPrice();
                String deliveryPrice = cartItemModelList.get(position).getDeliveryPrice();
                String totalAmount = cartItemModelList.get(position).getTotalAmount();
                String savedAmount = cartItemModelList.get(position).getSavedAmount();

                ((CartTotatAmountViewHolder)viewholder).setTotalAmount(totalItems,totalItemPrice,deliveryPrice,totalAmount,savedAmount);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private ImageView freeCoupanIcon;
        private TextView productTitle;
        private TextView freeCoupans;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView offersApplied;
        private TextView coupansApplied;
        private TextView productQuantity;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupans = itemView.findViewById(R.id.tv_free_coupan);
            freeCoupanIcon = itemView.findViewById(R.id.free_coupan_icon);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            coupansApplied = itemView.findViewById(R.id.coupans_applied);
            productQuantity = itemView.findViewById(R.id.product_quantity);
        }
        private void setItemDetails(int resource, String title, int freeCoupansNo, String productPriceText, String cuttedPriceText, int offersAppliedNo ) {
            productImage.setImageResource(resource);
            productTitle.setText(title);
            if (freeCoupansNo > 0) {
                freeCoupanIcon.setVisibility(View.VISIBLE);
                freeCoupans.setVisibility(View.VISIBLE);
                if (freeCoupansNo == 1) {
                    freeCoupans.setText("free " + freeCoupansNo + " coupan");
                } else {
                    freeCoupans.setText("free " + freeCoupansNo + " coupans");
                }
            } else {
                freeCoupanIcon.setVisibility(View.INVISIBLE);
                freeCoupans.setVisibility(View.INVISIBLE);
            }
            productPrice.setText(productPriceText);
            productPrice.setText(cuttedPriceText);
            if(offersAppliedNo > 0){
                offersApplied.setVisibility(View.VISIBLE);
                offersApplied.setText(offersAppliedNo+" Offers Applied");
            } else{
                offersApplied.setVisibility(View.INVISIBLE);
            }
        }
    }

    class CartTotatAmountViewHolder extends RecyclerView.ViewHolder{

        private TextView totalItems;
        private TextView totalItemPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;
        private TextView savedAmount;

        public CartTotatAmountViewHolder(@NonNull View itemView) {
            super(itemView);

            totalItems = itemView.findViewById(R.id.total_items);
            totalItemPrice = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            savedAmount = itemView.findViewById(R.id.saved_amount);
        }

        private void setTotalAmount(String totalItemText, String totalItemPriceText, String deliveryPriceText, String totalAmountText, String savedAmountText ){
            totalItems.setText(totalItemText);
            totalItemPrice.setText(totalItemPriceText);
            deliveryPrice.setText(deliveryPriceText);
            totalAmount.setText(totalAmountText);
            savedAmount.setText(savedAmountText);
        }
    }


}
