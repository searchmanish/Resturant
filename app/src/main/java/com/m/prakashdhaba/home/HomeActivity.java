package com.m.prakashdhaba.home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.m.prakashdhaba.MainActivity;
import com.m.prakashdhaba.R;
import com.m.prakashdhaba.bean_response.GetbannerModel;
import com.m.prakashdhaba.bean_response.NewProdResponse;
import com.m.prakashdhaba.cart.CartDetails;
import com.m.prakashdhaba.cart.getCartDetails;
import com.m.prakashdhaba.user_login.SigninActivity;
import com.m.prakashdhaba.utility.AppUtilits;
import com.m.prakashdhaba.utility.Constant;
import com.m.prakashdhaba.utility.NetworkUtility;
import com.m.prakashdhaba.utility.SharePreferenceUtils;
import com.m.prakashdhaba.web_service.ServiceWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BannerSlider bannerSlider;
    private String TAG = "HomeActivity";
    private RecyclerView recycler_bestSelling;
    private ArrayList<BestSelling_Model> bestSellingModelArrayList = new ArrayList<BestSelling_Model>();
    private BestSelling_Adapter bestSellingAdapter;

    private RecyclerView recycler_trending;
    private ArrayList<BestSelling_Model> trendingArrayList = new ArrayList<BestSelling_Model>();
    private BestSelling_Adapter trendingAdapter;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Menu mainmenu;


    private List<Banner> remoteBanners = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //setting username from shared pref to nav header

        View header = navigationView.getHeaderView(0);
        TextView nav_username = header.findViewById(R.id.nav_userrname);
        String name = SharePreferenceUtils.getInstance().getString(Constant.USER_name);
        if (name.isEmpty()) {
            nav_username.setText("Guest User");
        } else {
            nav_username.setText(name);
        }




        bannerSlider = (BannerSlider) findViewById(R.id.banner_slider1);


        /// for best selling
        recycler_bestSelling = (RecyclerView) findViewById(R.id.recycler_bestselling);
        LinearLayoutManager mLayoutManger = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_bestSelling.setLayoutManager(mLayoutManger);
        recycler_bestSelling.setItemAnimator(new DefaultItemAnimator());

        bestSellingAdapter = new BestSelling_Adapter(this, bestSellingModelArrayList, GetScreenWidth());
        recycler_bestSelling.setAdapter(bestSellingAdapter);


        // for trending product
        recycler_trending = (RecyclerView) findViewById(R.id.recycler_trend);
        LinearLayoutManager mLayoutManger1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_trending.setLayoutManager(mLayoutManger1);
        recycler_trending.setItemAnimator(new DefaultItemAnimator());

        trendingAdapter = new BestSelling_Adapter(this, trendingArrayList, GetScreenWidth());
        recycler_trending.setAdapter(trendingAdapter);

        getbannerimg();
        getBestSelling();
        getTrendingProd();


    }

    public void getbannerimg() {
        if (!NetworkUtility.isNetworkConnected(HomeActivity.this)) {
            AppUtilits.displayMessage(HomeActivity.this, getString(R.string.network_not_connected));

        } else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<GetbannerModel> call = service.getbannerModelCall("1234");
            call.enqueue(new Callback<GetbannerModel>() {
                @Override
                public void onResponse(Call<GetbannerModel> call, Response<GetbannerModel> response) {
                    Log.e(TAG, " banner response is " + response.body().getInformation().toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            if (response.body().getInformation().size() > 0) {

                                for (int i = 0; i < response.body().getInformation().size(); i++) {
                                    remoteBanners.add(new RemoteBanner(response.body().getInformation().get(i).getImgurl()));

                                }


                            } else {

                                remoteBanners.add(new RemoteBanner("http://beliefitsolution.com/buyonline/downloads/preview.jpg"
                                ));
                                remoteBanners.add(new RemoteBanner("http://beliefitsolution.com/buyonline/downloads/preview.jpg"
                                ));
                            }

                            bannerSlider.setBanners(remoteBanners);
                        } else {
                            remoteBanners.add(new RemoteBanner("http://beliefitsolution.com/buyonline/downloads/preview.jpg"
                            ));
                            remoteBanners.add(new RemoteBanner("http://beliefitsolution.com/buyonline/downloads/preview.jpg"
                            ));
                            bannerSlider.setBanners(remoteBanners);
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetbannerModel> call, Throwable t) {
                    Log.e(TAG, "fail banner ads " + t.toString());

                }
            });


        }

    }

    public void getBestSelling() {
        if (!NetworkUtility.isNetworkConnected(HomeActivity.this)) {
            AppUtilits.displayMessage(HomeActivity.this, getString(R.string.network_not_connected));

        } else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<NewProdResponse> call = service.getBestselling("1234");
            call.enqueue(new Callback<NewProdResponse>() {
                @Override
                public void onResponse(Call<NewProdResponse> call, Response<NewProdResponse> response) {
                    Log.e(TAG, " response is " + response.body().getInformation().toString());

                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            if (response.body().getInformation().size() > 0) {
                                bestSellingModelArrayList.clear();
                                for (int i = 0; i < response.body().getInformation().size(); i++) {

                                    bestSellingModelArrayList.add(new BestSelling_Model(response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getName(),
                                            response.body().getInformation().get(i).getImgUrl(), response.body().getInformation().get(i).getMrp(),
                                            response.body().getInformation().get(i).getPrice(), response.body().getInformation().get(i).getRating()));


                                }
                                bestSellingAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.e(TAG, "failed to get rnew prod " + response.body().getMsg());
                            // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                        }
                    } else {
                        Log.e(TAG, "failed to get rnew prod " + response.body().getMsg());
                        // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<NewProdResponse> call, Throwable t) {
                    Log.e(TAG, " fail best sell " + t.toString());
                }
            });


        }

    }


    public void getTrendingProd() {
        if (!NetworkUtility.isNetworkConnected(HomeActivity.this)) {
            AppUtilits.displayMessage(HomeActivity.this, getString(R.string.network_not_connected));

        } else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<NewProdResponse> call = service.getTrendingProd("1234");
            call.enqueue(new Callback<NewProdResponse>() {
                @Override
                public void onResponse(Call<NewProdResponse> call, Response<NewProdResponse> response) {
                    Log.e(TAG, " response is " + response.body().getInformation().toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            if (response.body().getInformation().size() > 0) {
                                if (response.body().getInformation().size() > 0) {
                                    trendingArrayList.clear();
                                    for (int i = 0; i < response.body().getInformation().size(); i++) {

                                        trendingArrayList.add(new BestSelling_Model(response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getName(),
                                                response.body().getInformation().get(i).getImgUrl(), response.body().getInformation().get(i).getMrp(),
                                                response.body().getInformation().get(i).getPrice(), response.body().getInformation().get(i).getRating()));


                                    }
                                    trendingAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            Log.e(TAG, "failed to get rnew prod " + response.body().getMsg());
                            // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                        }
                    } else {
                        Log.e(TAG, "failed to get rnew prod " + response.body().getMsg());
                        // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<NewProdResponse> call, Throwable t) {
                    Log.e(TAG, " fail best sell " + t.toString());
                }
            });


        }

    }


    public int GetScreenWidth() {
        int width = 100;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        return width;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.myaccount_toolbar_menu, menu);
        mainmenu = menu;
        if (mainmenu!=null){
            AppUtilits.UpdateCartCount(HomeActivity.this, mainmenu);
            getUserCartDetails();
            Log.e(TAG , "  option menu create" );
        }
        return true;
    }


// used to achieve search for items (Optional not necessary in this app)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Log.e(TAG, "  option click "+ item.getTitle() );
        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {

            // Associate searchable configuration with the SearchView
            SearchManager searchManager =  (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =  (SearchView) mainmenu.findItem(R.id.search).getActionView();
            final EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

            searchEditText.setHint(getString(R.string.search_name));

            searchEditText.setHintTextColor(getResources().getColor(R.color.white));
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));

            searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                    //  Log.e("onClick: ", "-- " + searchEditText.getText().toString().trim());
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        //run query to the server
                        Log.e("onClick: ", "-- " + searchEditText.getText().toString().trim());
                        if ( null!=searchEditText.getText().toString().trim() && !searchEditText.getText().toString().trim().equals("")){

                        }
                        //  AppUtils.GetSearchResult( HomeActivity.this, TAG, searchEditText.getText().toString());
                    }
                    return false;
                }
            });
            return true;
        }else if (id==R.id.cart){
            Intent intent = new Intent(this, CartDetails.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.e(TAG, "navi option " + item.getTitle());


        if (id == R.id.nav_home) {


        } else if (id == R.id.nav_new_prod) {


        } else if (id == R.id.nav_myaccount) {
            // Intent intent = new Intent(this, myaccount.class);
            //startActivity(intent);

        } else if (id == R.id.nav_wishlist) {
            //Intent intent = new Intent(this, WishlistDetails.class);
            // startActivity(intent);

        } else if (id == R.id.nav_logout) {

            //this.getSharedPreferences("YOUR_PREFS", 0).edit().clear().apply();
            // MyApp.getContext().getSharedPreferences("YOUR_PREFS", 0).edit().clear().apply();

            SharePreferenceUtils.getInstance().deletePref();
            Intent intent = new Intent(this, SigninActivity.class);
            startActivity(intent);


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mainmenu!=null){
            AppUtilits.UpdateCartCount(HomeActivity.this, mainmenu);
            Log.e(TAG , "  on resume mehtod " );
        }
    }

    public void getUserCartDetails(){

        if (!NetworkUtility.isNetworkConnected(HomeActivity.this)){
            AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.network_not_connected));

        }else {
            Log.e(TAG, "  get user cart count "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<getCartDetails> call = service.getCartDetailsCall( "1234" , SharePreferenceUtils.getInstance().getString(Constant.QUOTE_ID),
                    SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));

            call.enqueue(new Callback<getCartDetails>() {
                @Override
                public void onResponse(Call<getCartDetails> call, Response<getCartDetails> response) {
                    Log.e(TAG, "response is "+ response.body() + "  ---- "+ new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {
                            //      Log.e(TAG, "  ss sixe 3 ");
                            Log.e(TAG, " size is  "+ String.valueOf(response.body().getInformation().getProdDetails().size()));
                            SharePreferenceUtils.getInstance().saveInt( Constant.CART_ITEM_COUNT, response.body().getInformation().getProdDetails().size()  );
                            AppUtilits.UpdateCartCount(HomeActivity.this, mainmenu);

                        }
                    }

                }

                @Override
                public void onFailure(Call<getCartDetails> call, Throwable t) {
                    Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    //  AppUtilits.displayMessage(CartDetails.this, getString(R.string.fail_toGetcart));

                }
            });
        }






    }

}
