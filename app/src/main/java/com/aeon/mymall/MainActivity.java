package com.aeon.mymall;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aeon.mymall.ui.home.HomeFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.aeon.mymall.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.nio.charset.MalformedInputException;

import static com.aeon.mymall.RegisterActivity.setSignUpFragment;

public class MainActivity extends AppCompatActivity {

    private static final int HOME_FRAGMENT = 0;
    private static final int CART_FRAGMENT = 1;
    private static final int ORDERS_FRAGMENT = 2;
    private static final int WISHLIST_FRAGMENT = 3;
    private static final int REWARDS_FRAGMENT = 4;
    private static final int ACCOUNT_FRAGMENT = 5;
    public static Boolean showCart = false;

    private FrameLayout frameLayout;
    private ImageView actionBarLogo;
    private int currentFragment = -1;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavigationView navigationView;

    private Window window;
    private Dialog signInDialog;
    private FirebaseUser currentUser;

    public static DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        actionBarLogo = findViewById(R.id.actionbar_logo);

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        drawer = binding.drawerLayout;

        navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();*/
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_my_mall, R.id.nav_my_orders, R.id.nav_my_rewards, R.id.nav_my_cart, R.id.nav_my_wishlist, R.id.nav_my_account)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.getMenu().getItem(0).setChecked(true);
        //nav_host_fragment_content_main   register_framelayout     main_frame_layout

        frameLayout = findViewById(R.id.nav_host_fragment_content_main);
        // frameLayout.setVisibility(View.INVISIBLE);

        if (showCart) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//for drawer lock
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            gotoFragment("My Cart", new MyCartFragment(), -2);
        } else {
            ///////////////////////////////////
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, R.string.main_drawer_open, R.string.main_drawer_close);
            //   toggle.setDrawerIndicatorEnabled(true);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            ///////////////////////////////////
            setFragment(new HomeFragment(), HOME_FRAGMENT);
        }


        signInDialog = new Dialog(MainActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);
        final Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);

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


        //For using menus of the drawer THIS THIS ADD ADD BY ME ....................
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

                if (currentUser != null) {


                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            navController.navigate(R.id.nav_home);
                            break;

                        case R.id.nav_my_mall:
                            // actionBarLogo.setVisibility(View.VISIBLE);
                            // invalidateOptionMenu();
                            //setFragment (new HomeFragment(), HOME_FRAGMENT);
                            navController.navigate(R.id.nav_my_mall);
                            break;

                        case R.id.nav_my_orders:
                            gotoFragment("My Orders", new MyOrderFragment(), ORDERS_FRAGMENT);
                            navController.navigate(R.id.nav_my_orders);
                            break;

                        case R.id.nav_my_rewards:
                            gotoFragment("My Rewards", new MyRewardsFragment(), REWARDS_FRAGMENT);
                            navController.navigate(R.id.nav_my_rewards);
                            break;

                        case R.id.nav_my_cart:
                            gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                            navController.navigate(R.id.nav_my_cart);
                            break;

                        case R.id.nav_my_wishlist:
                            gotoFragment("My Wishlist", new MyWishlistFragment(), WISHLIST_FRAGMENT);
                            navController.navigate(R.id.nav_my_wishlist);
                            break;

                        case R.id.nav_my_account:
                            gotoFragment("My Account", new MyAccountFragment(), ACCOUNT_FRAGMENT);
                            navController.navigate(R.id.nav_my_account);
                            break;

                        case R.id.nav_sign_out:
                            FirebaseAuth.getInstance().signOut();
                            DBqueries.clearData();
                            Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                            startActivity(registerIntent);
                            finish();
                            break;

                    }

                    /////////////////////////////////////////
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                } else {
                    drawer.closeDrawer(GravityCompat.START);
                    signInDialog.show();
                    return false;
                }
                   /*
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                   */

                //drawer.closeDrawers();
                //return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
        } else {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragment == HOME_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.main_search_icon) {
            //todo: search
            return true;
        } else if (id == R.id.main_notification_icon) {
            Intent intent = new Intent(MainActivity.this, SplashActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.main_cart_icon) {

            if (currentUser == null) {
                signInDialog.show();
            } else {
                gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
            }
            return true;
        } else if (id == android.R.id.home) {
            if (showCart) {
                showCart = false;
                finish();
                return true;
            }
        }

        /////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////

        /*else if (id == R.id.nav_my_mall){
            actionBarLogo.setVisibility(View.VISIBLE);
            invalidateOptionsMenu();
            setFragment(new HomeFragment(), HOME_FRAGMENT);
            return true;
        }
        else if (id == R.id.nav_my_orders){
            gotoFragment("My Orders", new MyOrderFragment(), ORDERS_FRAGMENT);
            return true;
        }
        else if (id == R.id.nav_my_rewards){
            Intent intent = new Intent(MainActivity.this,SplashActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.nav_my_cart){
            gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
            return true;
        }
        else if (id == R.id.nav_my_wishlist){

            return true;
        }
        else if (id == R.id.nav_my_account){

            return true;
        }
        else if (id == R.id.nav_sign_out){

            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    private void gotoFragment(String title, Fragment fragment, int fragmentNo) {
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        actionBarLogo.setVisibility(View.GONE);
        invalidateOptionsMenu();
        setFragment(fragment, fragmentNo);
        if (fragmentNo == CART_FRAGMENT) {
            navigationView.getMenu().getItem(3).setChecked(true);
        }
    }


////////////////////////////////////////////////////////////////////
  /*  @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected (MenuItem item){
        int id = item.getItemId();

        if (id == R.id.nav_my_account){

        }else if (id == R.id.nav_sign_out){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }*/
////////////////////////////////////////////////////////////////////

    private void setFragment(Fragment fragment, int fragmentNo) {
        if (fragmentNo != currentFragment) {


           /* if (fragmentNo == REWARDS_FRAGMENT) {
                window.setStatusBarColor(Color.parseColor("#5B04B1"));
               *//* toolbar.setBackgroundColor(Color.parseColor("#5B04B1"));*//*
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.primary));
                *//* toolbar.setBackgroundColor(getResources().getColor(R.color.primary));*//*

            }*/


            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            // fragmentTransaction.replace(navController.getId(), fragment);
           /* val host = NavHostFragment.create(R.navigation.navigation_register_login);
            fragmentTransaction.replace(R.id.nav_host_fragment_content_main,host).setPrimaryNavigationFragment(host).commit();*/
            if (currentFragment != 0) {
                fragmentTransaction.replace(frameLayout.getId(), fragment);
            }
            frameLayout.setVisibility(View.VISIBLE);
            //  fragmentTransaction.remove(fragment); //important line
            fragmentTransaction.commit();
        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment == HOME_FRAGMENT) {
                currentFragment = -1;
                super.onBackPressed();
            } else {
                if (showCart) {
                    showCart = false;
                    finish();
                } else {
                    actionBarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                    // frameLayout.setVisibility(View.INVISIBLE);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }
    }
    /* @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment == HOME_FRAGMENT) {
                super.onBackPressed();
            } else {
                actionBarLogo.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
                setFragment(new HomeFragment(),HOME_FRAGMENT);
               // frameLayout.setVisibility(View.INVISIBLE);
                navigationView.getMenu().getItem(0).setChecked(true);
            }
        }
    }*/


    /* @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            // alertDialogBuilder.setTitle("Exit Application?");
            alertDialogBuilder
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    moveTaskToBack(true);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(1);
                                }
                            })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }*/


    /*@Override
    public void onBackPressed() {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
    }*/
}
