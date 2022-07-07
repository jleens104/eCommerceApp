package com.aeon.mymall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.aeon.mymall.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import static com.aeon.mymall.DBqueries.lists;
import static com.aeon.mymall.DBqueries.loadFragmentData;
import static com.aeon.mymall.DBqueries.loadedCategoriesNames;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    private HomePageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /////// Home page fake list
        List<SliderModal> sliderModalFakeList = new ArrayList<>();
        sliderModalFakeList.add(new SliderModal("null","#ffffff"));
        sliderModalFakeList.add(new SliderModal("null","#ffffff"));
        sliderModalFakeList.add(new SliderModal("null","#ffffff"));
        sliderModalFakeList.add(new SliderModal("null","#ffffff"));
        sliderModalFakeList.add(new SliderModal("null","#ffffff"));

        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList = new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModalFakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#ffffff"));
        homePageModelFakeList.add(new HomePageModel(2,"","#ffffff",horizontalProductScrollModelFakeList, new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#ffffff",horizontalProductScrollModelFakeList));
        /////// Home page fake list

        categoryRecyclerView = findViewById(R.id.category_recycler_view);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(testingLayoutManager);

        adapter = new HomePageAdapter(homePageModelFakeList);

        int listPosition = 0;
        for (int x = 0; x < loadedCategoriesNames.size(); x++){
            if (loadedCategoriesNames.get(x).equals(title.toUpperCase())){
                listPosition = x;
            }
        }
        if (listPosition == 0){
            // for new data access
            loadedCategoriesNames.add(title.toUpperCase());
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(categoryRecyclerView, this,loadedCategoriesNames.size() - 1, title);
        } else {
            // if data already available
            adapter = new HomePageAdapter(lists.get(listPosition));
        }
        categoryRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.main_search_icon){
            //todo: search
            return true;
        } else if (id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}