package com.aeon.mymall;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyWishlistFragment extends Fragment {


    public MyWishlistFragment() {
        // Required empty public constructor
    }


    private RecyclerView wishlistRecyclerView;
    private Dialog loadingDialog;
    public static WishlistAdapter wishlistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_wishlist, container, false);

        /////////////// loading dialog ///////////////////////
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        /////////////// loading dialog ///////////////////////


        wishlistRecyclerView = view.findViewById(R.id.my_wishlist_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        wishlistRecyclerView.setLayoutManager(linearLayoutManager);


       /* wishlistModelList.add(new WishlistModel(R.drawable.mobile_1,"Pixel 2", 1,"3", 145,"Rs.49999/-","Rs.79999/-","Cash on delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.mobile_1,"Pixel 2", 0,"3", 145,"Rs.49999/-","Rs.79999/-","Cash on delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.mobile_1,"Pixel 2", 1,"3", 145,"Rs.49999/-","Rs.79999/-","Cash on delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.mobile_1,"Pixel 2", 2,"3", 145,"Rs.49999/-","Rs.79999/-","Cash on delivery"));
        wishlistModelList.add(new WishlistModel(R.drawable.mobile_1,"Pixel 2", 4,"3", 145,"Rs.49999/-","Rs.79999/-","Cash on delivery"));
*/
        if (DBqueries.wishlistModelList.size() == 0) {
            DBqueries.wishList.clear();
            DBqueries.loadWishList(getContext(), loadingDialog, true);
        } else {
            loadingDialog.dismiss();
        }

        wishlistAdapter = new WishlistAdapter(DBqueries.wishlistModelList,true);
        wishlistRecyclerView.setAdapter(wishlistAdapter);
        wishlistAdapter.notifyDataSetChanged();

        return view;

    }
}