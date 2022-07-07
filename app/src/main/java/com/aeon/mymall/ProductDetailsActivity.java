package com.aeon.mymall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aeon.mymall.databinding.ActivityMainBinding;
import com.aeon.mymall.databinding.SignInDialogBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.aeon.mymall.MainActivity.showCart;
import static com.aeon.mymall.RegisterActivity.setSignUpFragment;

public class ProductDetailsActivity extends AppCompatActivity {

    // single query fire for single time
    public static boolean running_wishlist_query = false;
    public static boolean running_rating_query = false;

    private ViewPager productImagesViewPager;
    private TextView productTitle;
    private TextView averageRatingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private TextView cuttedPrice;
    private ImageView codIndicator;
    private TextView tvCodIndicator;
    private TabLayout viewpagerIndicator;
    private LinearLayout coupanReedemptionLayout;
    private Button coupanRedeemBtn;

    private TextView rewardTitle;
    private TextView rewardBody;

    ///// product description
    private ConstraintLayout productDetailsOnlyContainer;
    private ConstraintLayout productDetailsTabsContainer;
    private ViewPager productDetailsViewPager;
    private TabLayout productDetailsTabLayout;
    private TextView productOnlyDescriptionBody;

    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();
    private String productDescription;
    private String productOtherDetails;
    ///// product description


    ///////////////////////// rating layout
    public static int initialRating;
    public static LinearLayout rateNowContainer;
    private TextView totalRatings;
    private LinearLayout ratingsNoContainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;
    private TextView averageRating;
    ///////////////////////// rating layout

    private Button buyNowBtn;
    private LinearLayout addToCartBtn;

    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static FloatingActionButton addToWishlistBtn;

    private FirebaseFirestore firebaseFirestore;

    ////////////////////////// coupan dialog
    public static TextView coupanTitle;
    public static TextView coupanExpiryDate;
    public static TextView coupanBody;
    private static RecyclerView coupansRecyclerView;
    private static LinearLayout selectedCoupan;
    ////////////////////////// coupan dialog

    private Dialog signInDialog;
    private Dialog loadingDialog;
    private FirebaseUser currentUser;
    public static String productID;

    private DocumentSnapshot documentSnapshot;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        /*binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewPager = findViewById(R.id.product_images_view_pager);
        viewpagerIndicator = findViewById(R.id.view_pager_indicator);
        addToWishlistBtn = findViewById(R.id.add_to_wishlist_btn);
        productDetailsViewPager = findViewById(R.id.product_details_view_pager);
        productDetailsTabLayout = findViewById(R.id.product_details_tab_layout);
        buyNowBtn = findViewById(R.id.buy_now_btn);
        coupanRedeemBtn = findViewById(R.id.coupan_redemption_btn);
        productTitle = findViewById(R.id.product_title);
        averageRatingMiniView = findViewById(R.id.tv_product_rating_miniview);
        totalRatingMiniView = findViewById(R.id.total_ratings_mini_view);
        productPrice = findViewById(R.id.product_price);
        cuttedPrice = findViewById(R.id.cutted_price);
        tvCodIndicator = findViewById(R.id.tv_cod_indicator);
        codIndicator = findViewById(R.id.cod_indicator_image_view);
        rewardTitle = findViewById(R.id.reward_title);
        rewardBody = findViewById(R.id.reward_body);
        productDetailsOnlyContainer = findViewById(R.id.product_details_container);
        productDetailsTabsContainer = findViewById(R.id.product_details_tabs_container);
        productOnlyDescriptionBody = findViewById(R.id.product_details_body);
        totalRatings = findViewById(R.id.total_ratings);
        ratingsNoContainer = findViewById(R.id.ratings_numbers_container);
        totalRatingsFigure = findViewById(R.id.total_ratings_figure);
        ratingsProgressBarContainer = findViewById(R.id.ratings_progressbar_container);
        averageRating = findViewById(R.id.average_rating);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);
        coupanReedemptionLayout = findViewById(R.id.coupan_redemption_layout);

        initialRating = -1;

        /////////////// loading dialog ///////////////////////
        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        /////////////// loading dialog ///////////////////////


        firebaseFirestore = FirebaseFirestore.getInstance();

        final List<String> productImages = new ArrayList<>();
        productID = getIntent().getStringExtra("PRODUCT_ID");
        firebaseFirestore.collection("PRODUCTS").document(productID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();

                    for (long x = 1; x < (long) documentSnapshot.get("no_of_product_images") + 1; x++) {
                        productImages.add(documentSnapshot.get("product_image_" + x).toString());
                    }
                    ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                    productImagesViewPager.setAdapter(productImagesAdapter);

                    productTitle.setText(documentSnapshot.get("product_title").toString());
                    averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                    totalRatingMiniView.setText("(" + (long) documentSnapshot.get("total_ratings") + ")ratings");
                    productPrice.setText("Rs." + documentSnapshot.get("product_price").toString() + "/-");
                    cuttedPrice.setText("Rs." + documentSnapshot.get("cutted_price").toString() + "/-");
                    if ((boolean) documentSnapshot.get("COD")) {
                        codIndicator.setVisibility(View.VISIBLE);
                        tvCodIndicator.setVisibility(View.VISIBLE);
                    } else {
                        codIndicator.setVisibility(View.INVISIBLE);
                        tvCodIndicator.setVisibility(View.INVISIBLE);
                    }
                    rewardTitle.setText((long) documentSnapshot.get("free_coupons") + documentSnapshot.get("free_coupon_title").toString());
                    rewardBody.setText(documentSnapshot.get("free_coupon_body").toString());

                    if ((boolean) documentSnapshot.get("use_tab_layout")) {
                        productDetailsTabsContainer.setVisibility(View.VISIBLE);
                        productDetailsOnlyContainer.setVisibility(View.GONE);
                        productDescription = documentSnapshot.get("product_description").toString();

                        productOtherDetails = documentSnapshot.get("product_other_details").toString();

                     /*   productDetailsTabsContainer.setVisibility(View.GONE);
                        productDetailsOnlyContainer.setVisibility(View.VISIBLE);
*/
                        for (long x = 1; x < (long) documentSnapshot.get("total_spec_titles") + 1; x++) {
                            productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("spec_title_" + x).toString()));
                            for (long y = 1; y < (long) documentSnapshot.get("spec_title_" + x + "_total_fields") + 1; y++) {
                                productSpecificationModelList.add(new ProductSpecificationModel(1, documentSnapshot.get("spec_title_" + x + "_field_" + y + "_name").toString(), documentSnapshot.get("spec_title_" + x + "_field_" + y + "_value").toString()));
                            }
                        }
                    } else {
                        productDetailsTabsContainer.setVisibility(View.GONE);
                        productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                        productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
                    }

                    totalRatings.setText((long) documentSnapshot.get("total_ratings") + " ratings");

                    for (int x = 0; x < 5; x++) {
                        TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
                        rating.setText(String.valueOf((long) documentSnapshot.get((5 - x) + "_star")));

                        ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                        int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));
                        progressBar.setMax(maxProgress);
                        progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));
                    }
                    totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings")));
                    averageRating.setText(documentSnapshot.get("average_rating").toString());
                    productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(), productDetailsTabLayout.getTabCount(), productDescription, productOtherDetails, productSpecificationModelList));

                    if (currentUser != null) {
                        if (DBqueries.myRating.size() == 0) {
                            DBqueries.loadRatingList(ProductDetailsActivity.this);
                        }
                        if (DBqueries.wishList.size() == 0) {
                            DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog, false);
                        } else {
                            loadingDialog.dismiss();
                        }
                    } else {
                        loadingDialog.dismiss();
                    }

                    ////check for wishlist product ///////
                    if (DBqueries.wishList.contains(productID)) {
                        ALREADY_ADDED_TO_WISHLIST = true;
                        addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.primary));
                    } else {
                        ALREADY_ADDED_TO_WISHLIST = false;
                    }

                } else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewpagerIndicator.setupWithViewPager(productImagesViewPager, true);

        addToWishlistBtn.setOnClickListener(view -> {
            if (currentUser == null) {
                signInDialog.show();
            } else {
                if (!running_wishlist_query) {
                    running_wishlist_query = true;
                    if (ALREADY_ADDED_TO_WISHLIST) {
                        int index = DBqueries.wishList.indexOf(productID);
                        DBqueries.removeFromWishlist(index, ProductDetailsActivity.this);
                        addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                    } else {
                        addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.primary));
                        Map<String, Object> addProduct = new HashMap<>();
                        addProduct.put("product_ID_" + String.valueOf(DBqueries.wishList.size()), productID);

                        firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                .update(addProduct).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                Map<String, Object> updateListSize = new HashMap<>();
                                updateListSize.put("list_size", (long) DBqueries.wishList.size() + 1);

                                firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                        .update(updateListSize).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {

                                        if (DBqueries.wishlistModelList.size() != 0) {
                                            DBqueries.wishlistModelList.add(new WishlistModel
                                                    (productID, documentSnapshot.get("product_image_1").toString(),
                                                            documentSnapshot.get("product_title").toString(),
                                                            (long) documentSnapshot.get("free_coupons"),
                                                            documentSnapshot.get("average_rating").toString(),
                                                            (long) documentSnapshot.get("total_ratings"),
                                                            documentSnapshot.get("product_price").toString(),
                                                            documentSnapshot.get("cutted_price").toString(),
                                                            (boolean) documentSnapshot.get("COD")));
                                        }
                                        ALREADY_ADDED_TO_WISHLIST = true;
                                        addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.primary));
                                        DBqueries.wishList.add(productID);
                                        Toast.makeText(ProductDetailsActivity.this, "Added to wishlist successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                        String error = task1.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    running_wishlist_query = false;
                                });

                            } else {
                                running_wishlist_query = false;
                                String error = task.getException().getMessage();
                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

        productDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTabLayout));
        productDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /////// rating layout
        rateNowContainer = findViewById(R.id.rate_now_container);
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentUser == null) {
                        signInDialog.show();
                    } else {
                        if (!running_rating_query) {
                            running_rating_query = true;

                            setRating(starPosition);
                            if (DBqueries.myRatedIds.contains(productID)) {

                            } else {

                                Map<String, Object> productRating = new HashMap<>();
                                productRating.put(starPosition + 1 + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                                productRating.put("average_rating", calculateAverageRating(starPosition + 1));
                                productRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);

                                firebaseFirestore.collection("PRODUCTS").document(productID)
                                        .update(productRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Map<String, Object> rating = new HashMap<>();
                                            rating.put("product_ID_" + DBqueries.myRatedIds.size(), productID);
                                            rating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPosition + 1);

                                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                    .update(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        DBqueries.myRatedIds.add(productID);
                                                        DBqueries.myRating.add((long)starPosition+1);

                                                        TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition + 1);
                                                        rating.setText(Integer.parseInt(rating.getText().toString()));

                                                        totalRatingMiniView.setText("(" + (long) documentSnapshot.get("total_ratings")+ 1 + ")ratings");
                                                        totalRatings.setText((long) documentSnapshot.get("total_ratings") + 1 + " ratings");
                                                        totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));

                                                        Toast.makeText(ProductDetailsActivity.this, "Thank you for rating..!!!", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        setRating(initialRating);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                    running_rating_query = false;
                                                }
                                            });


                                        } else {
                                            running_rating_query = false;
                                            setRating(initialRating);
                                            String error = task.getException().getMessage();
                                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        }
                    }
                }
            });

        }
        /////// rating layout

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    Intent intent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                    startActivity(intent);
                }
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    //////// todo: add to cart
                }
            }
        });

        //////////////////////////////// coupan dialog

        Dialog checkCoupanPriceDialog = new Dialog(ProductDetailsActivity.this);
        checkCoupanPriceDialog.setContentView(R.layout.coupan_redeem_dialog);
        checkCoupanPriceDialog.setCancelable(true);
        checkCoupanPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toggleRecyclerView = checkCoupanPriceDialog.findViewById(R.id.toggle_recycler_view);
        coupansRecyclerView = checkCoupanPriceDialog.findViewById(R.id.coupans_recycler_view);
        selectedCoupan = checkCoupanPriceDialog.findViewById(R.id.selected_coupan);
        coupanTitle = checkCoupanPriceDialog.findViewById(R.id.coupan_title);
        coupanExpiryDate = checkCoupanPriceDialog.findViewById(R.id.coupan_validity);
        coupanBody = checkCoupanPriceDialog.findViewById(R.id.coupan_body);


        TextView originalPrice = checkCoupanPriceDialog.findViewById(R.id.original_price);
        TextView discountedPrice = checkCoupanPriceDialog.findViewById(R.id.discounted_price);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        coupansRecyclerView.setLayoutManager(layoutManager);


        List<RewardModel> rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel("Cashback", "till 2nd,Aug 2022", "GET 20% CASHBACK on any product above Rs.200/- and below Rs.1000/-"));
        rewardModelList.add(new RewardModel("Discount", "till 2nd,Aug 2022", "GET 20% CASHBACK on any product above Rs.200/- and below Rs.1000/-"));
        rewardModelList.add(new RewardModel("Buy 1 get 1 free", "till 2nd,Aug 2022", "GET 20% CASHBACK on any product above Rs.200/- and below Rs.1000/-"));
        rewardModelList.add(new RewardModel("Cashback", "till 2nd,Aug 2022", "GET 20% CASHBACK on any product above Rs.200/- and below Rs.1000/-"));
        rewardModelList.add(new RewardModel("Discount", "till 2nd,Aug 2022", "GET 20% CASHBACK on any product above Rs.200/- and below Rs.1000/-"));
        rewardModelList.add(new RewardModel("Buy 1 get 1 free", "till 2nd,Aug 2022", "GET 20% CASHBACK on any product above Rs.200/- and below Rs.1000/-"));
        rewardModelList.add(new RewardModel("Cashback", "till 2nd,Aug 2022", "GET 20% CASHBACK on any product above Rs.200/- and below Rs.1000/-"));
        rewardModelList.add(new RewardModel("Discount", "till 2nd,Aug 2022", "GET 20% CASHBACK on any product above Rs.200/- and below Rs.1000/-"));
        rewardModelList.add(new RewardModel("Buy 1 get 1 free", "till 2nd,Aug 2022", "GET 20% CASHBACK on any product above Rs.200/- and below Rs.1000/-"));

        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardModelList, false);
        coupansRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();

        toggleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogRecyclerView();
            }
        });
        //////////////////////////////// coupan dialog

        coupanRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCoupanPriceDialog.show();
            }
        });


        ////////////////// Sign In Dialog /////////////////

        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);
        final Intent registerIntent = new Intent(ProductDetailsActivity.this, RegisterActivity.class);

        dialogSignInBtn.setOnClickListener(view -> {
            SignInFragment.disableCloseBtn = true;
            SignUpFragment.disableCloseBtn = true;
            signInDialog.dismiss();
            setSignUpFragment = false;
            startActivity(registerIntent);
        });

        dialogSignUpBtn.setOnClickListener(view -> {
            SignInFragment.disableCloseBtn = true;
            SignUpFragment.disableCloseBtn = true;
            signInDialog.dismiss();
            setSignUpFragment = true;
            startActivity(registerIntent);
        });

        ////////////////// Sign In Dialog /////////////////


    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            coupanReedemptionLayout.setVisibility(View.GONE);
        } else {
            coupanReedemptionLayout.setVisibility(View.VISIBLE);
        }

        if (currentUser != null) {
            if (DBqueries.myRating.size() == 0) {
                DBqueries.loadRatingList(ProductDetailsActivity.this);
            }
            if (DBqueries.wishList.size() == 0) {
                DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog, false);
            } else {
                loadingDialog.dismiss();
            }
        } else {
            loadingDialog.dismiss();
        }

        ////check for wishlist product ///////
        if (DBqueries.wishList.contains(productID)) {
            ALREADY_ADDED_TO_WISHLIST = true;
            addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.primary));
        } else {
            addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
            ALREADY_ADDED_TO_WISHLIST = false;
        }

    }

    public static void showDialogRecyclerView() {
        if (coupansRecyclerView.getVisibility() == View.GONE) {
            coupansRecyclerView.setVisibility(View.VISIBLE);
            selectedCoupan.setVisibility(View.GONE);
        } else {
            coupansRecyclerView.setVisibility(View.GONE);
            selectedCoupan.setVisibility(View.VISIBLE);
        }
    }

    public static void setRating(int starPosition) {
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPosition) {
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }

    private long calculateAverageRating(long currentUserRating) {
        long totalStars = 0;
        for (int x = 1; x < 6; x++) {
            totalStars = totalStars + (long) documentSnapshot.get(x + "_star");
        }
        totalStars = totalStars + currentUserRating;
        return totalStars / ((long) documentSnapshot.get("total_ratings") + 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_item, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.main_search_icon) {
            //todo: search
            return true;
        } else if (id == R.id.main_cart_icon) {
            if (currentUser == null) {
                signInDialog.show();
            } else {
                Intent intent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                showCart = true;
                startActivity(intent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

}