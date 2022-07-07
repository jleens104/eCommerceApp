package com.aeon.mymall.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.aeon.mymall.CategoryAdapter;
import com.aeon.mymall.CategoryModal;
import com.aeon.mymall.GridProductLayoutAdapter;
import com.aeon.mymall.HomePageAdapter;
import com.aeon.mymall.HomePageModel;
import com.aeon.mymall.HorizontalProductScrollAdapter;
import com.aeon.mymall.HorizontalProductScrollModel;
import com.aeon.mymall.MainActivity;
import com.aeon.mymall.R;
import com.aeon.mymall.SliderAdapter;
import com.aeon.mymall.SliderModal;
import com.aeon.mymall.WishlistModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED;
import static com.aeon.mymall.DBqueries.categoryModalList;
import static com.aeon.mymall.DBqueries.firebaseFirestore;
import static com.aeon.mymall.DBqueries.lists;
import static com.aeon.mymall.DBqueries.loadCategories;
import static com.aeon.mymall.DBqueries.loadFragmentData;
import static com.aeon.mymall.DBqueries.loadedCategoriesNames;

public class HomeFragment extends Fragment {

    public HomeFragment() {

    }

    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    public static SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView categoryRecyclerView;
    private List<CategoryModal> categoryModalFakeList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    private HomePageAdapter adapter;
    private ImageView noInternetConnection;
    private Button retryBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        noInternetConnection = view.findViewById(R.id.no_internet_connection);
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
        homePageRecyclerView = view.findViewById(R.id.home_page_recycler_view);
        retryBtn = view.findViewById(R.id.retry_btn);

        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.primary), getContext().getResources().getColor(R.color.primary), getContext().getResources().getColor(R.color.primary));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerView.setLayoutManager(testingLayoutManager);

        /////// Categories fake list
        categoryModalFakeList.add(new CategoryModal("null", ""));
        categoryModalFakeList.add(new CategoryModal("", ""));
        categoryModalFakeList.add(new CategoryModal("", ""));
        categoryModalFakeList.add(new CategoryModal("", ""));
        categoryModalFakeList.add(new CategoryModal("", ""));
        categoryModalFakeList.add(new CategoryModal("", ""));
        categoryModalFakeList.add(new CategoryModal("", ""));
        categoryModalFakeList.add(new CategoryModal("", ""));
        categoryModalFakeList.add(new CategoryModal("", ""));
        categoryModalFakeList.add(new CategoryModal("", ""));
        /////// Categories fake list

        /////// Home page fake list
        List<SliderModal> sliderModalFakeList = new ArrayList<>();
        sliderModalFakeList.add(new SliderModal("null", "#dfdfdf"));
        sliderModalFakeList.add(new SliderModal("null", "#dfdfdf"));
        sliderModalFakeList.add(new SliderModal("null", "#dfdfdf"));
        sliderModalFakeList.add(new SliderModal("null", "#dfdfdf"));
        sliderModalFakeList.add(new SliderModal("null", "#dfdfdf"));

        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList = new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));

        homePageModelFakeList.add(new HomePageModel(0, sliderModalFakeList));
        homePageModelFakeList.add(new HomePageModel(1, "", "#dfdfdf"));
        homePageModelFakeList.add(new HomePageModel(2, "", "#dfdfdf", horizontalProductScrollModelFakeList, new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(3, "", "#dfdfdf", horizontalProductScrollModelFakeList));
        /////// Home page fake list

        categoryAdapter = new CategoryAdapter(categoryModalFakeList);
        adapter = new HomePageAdapter(homePageModelFakeList);

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
//            MainActivity.drawer.setDrawerLockMode(LOCK_MODE_UNLOCKED);
            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);

            if (categoryModalList.size() == 0) {
                loadCategories(categoryRecyclerView, getContext());
            } else {
                categoryAdapter = new CategoryAdapter(categoryModalList);
                categoryAdapter.notifyDataSetChanged();
            }
            categoryRecyclerView.setAdapter(categoryAdapter);
            ///////////horizontal product layout/////////////
    /*    List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.mobile, "Iphone XR", "M1 Max Processor", "Rs.59999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.icon, "Iphone XR", "M1 Max Processor", "Rs.59999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.inbox_red, "Iphone XR", "M1 Max Processor", "Rs.59999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.inbox_green, "Iphone XR", "M1 Max Processor", "Rs.59999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.error_icon, "Iphone XR", "M1 Max Processor", "Rs.59999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.icons8_user, "Iphone XR", "M1 Max Processor", "Rs.59999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.icons8_favorite_cart_white, "Iphone XR", "M1 Max Processor", "Rs.59999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.icons8_favorite_cart, "Iphone XR", "M1 Max Processor", "Rs.59999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.inbox_green, "Iphone XR", "M1 Max Processor", "Rs.59999/-"));
*/
            ///////////horizontal product layout/////////////


            //////////////////////////////////////////////////

            if (lists.size() == 0) {
                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<HomePageModel>());
                loadFragmentData(homePageRecyclerView, getContext(), 0, "Home");
            } else {
                adapter = new HomePageAdapter(lists.get(0));
                adapter.notifyDataSetChanged();
            }
            homePageRecyclerView.setAdapter(adapter);

        } else {
  //          MainActivity.drawer.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED);
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.no_internet).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
        }

        ////////////////////////Refresh Layout //////////////////////////
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            reloadPage();
        });
        ////////////////////////Refresh Layout //////////////////////////
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadPage();
            }
        });
        return view;
    }

    private void reloadPage() {
        networkInfo = connectivityManager.getActiveNetworkInfo();
        categoryModalList.clear();
        lists.clear();
        loadedCategoriesNames.clear();
        if (networkInfo != null && networkInfo.isConnected() == true) {
      //      MainActivity.drawer.setDrawerLockMode(LOCK_MODE_UNLOCKED);
            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);

            categoryAdapter = new CategoryAdapter(categoryModalFakeList);
            adapter = new HomePageAdapter(homePageModelFakeList);
            categoryRecyclerView.setAdapter(categoryAdapter);
            homePageRecyclerView.setAdapter(adapter);

            loadCategories(categoryRecyclerView, getContext());

            loadedCategoriesNames.add("HOME");
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(homePageRecyclerView, getContext(), 0, "Home");


        } else {
         //   MainActivity.drawer.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED);
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(getContext()).load(R.drawable.no_internet).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }

    }

}