package com.m.prakashdhaba.productpreview;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.m.prakashdhaba.R;

import com.bumptech.glide.Glide;
import com.m.prakashdhaba.bean_response.AddtoCart;
import com.m.prakashdhaba.bean_response.ProductDetail_Res;
import com.m.prakashdhaba.cart.CartDetails;
import com.m.prakashdhaba.home.BestSelling_Adapter;
import com.m.prakashdhaba.home.BestSelling_Model;
import com.m.prakashdhaba.utility.AppUtilits;
import com.m.prakashdhaba.utility.Constant;
import com.m.prakashdhaba.utility.NetworkUtility;
import com.m.prakashdhaba.utility.SharePreferenceUtils;
import com.m.prakashdhaba.web_service.ServiceWrapper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductDetails extends AppCompatActivity {
    private String TAG ="productDetails";
    private String prod_id="";
    private TextView prod_name, prod_price, prod_oldprice, prod_rating_count, prod_stock, prod_qty;
    private AppCompatRatingBar prod_rating;
    private ImageView add_to_cart,add_to_wishlist;
    private ImageView productImage;
    // related product
    private RecyclerView recycler_relatedProd;
    private ArrayList<BestSelling_Model> relatedProdModelArrayList = new ArrayList<BestSelling_Model>();
    private BestSelling_Adapter relatedProdAdapter;
    // overview and review tab layout
    private TabLayout tablayout;
    private FrameLayout frag_container;
    public String prod_overview ="";
    public String prod_fulldetails ="";
    public String prod_review ="";


    private FragmentManager fragmentManager = getSupportFragmentManager();

    //private NavigationView navigationView;
   // private DrawerLayout drawer;
    private Menu mainmenu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_preview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

 /*
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
*/

        final Intent intent = getIntent();
        prod_id =  intent.getExtras().getString("prod_id");

        prod_name = (TextView) findViewById(R.id.prod_name);
        prod_price =(TextView) findViewById(R.id.prod_price);
        prod_oldprice = (TextView) findViewById(R.id.price_old);
        prod_rating_count = (TextView) findViewById(R.id.rating_count);
        prod_stock = (TextView) findViewById(R.id.stock_avail);
        prod_qty = (TextView) findViewById(R.id.prod_qty_value);
        prod_rating = (AppCompatRatingBar) findViewById(R.id.prod_rating);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        frag_container = (FrameLayout) findViewById(R.id.frag_container);
        add_to_cart = (ImageView) findViewById(R.id.add_to_cart);
        add_to_wishlist = (ImageView) findViewById(R.id.add_to_wishlist);
        productImage=findViewById(R.id.product_image);


        String prod_Img_url =  intent.getExtras().getString("prod_img_url");
        Glide.with(this).load(prod_Img_url).into(productImage);


        tablayout.addTab( tablayout.newTab().setText(getString(R.string.overview)));
        tablayout.addTab( tablayout.newTab().setText(getString(R.string.details)));
        tablayout.addTab( tablayout.newTab().setText(getString(R.string.review)));

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // "blueappsoftware.shopeasy.productpreview.tabfragment"
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment PrevFrag = fragmentManager.findFragmentByTag("com.beliefitsolution.buyonline.productpreview.tabfragment");
                if (PrevFrag !=null){
                    fragmentTransaction.remove(PrevFrag);
                }

                if (tab.getPosition()==0){
                    Overview overview = new Overview();
                    fragmentTransaction.add( R.id.frag_container, overview, "com.beliefitsolution.buyonline.productpreview.tabfragment");
                    fragmentTransaction.commit();

                }else if (tab.getPosition()==1){
                    Details details = new Details();
                    fragmentTransaction.add( R.id.frag_container, details, "com.beliefitsolution.buyonline.productpreview.tabfragment");
                    fragmentTransaction.commit();

                }else if (tab.getPosition()==2){
                    Review review = new Review();
                    fragmentTransaction.add( R.id.frag_container, review, "com.beliefitsolution.buyonline.productpreview.tabfragment");
                    fragmentTransaction.commit();
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);

        getProductDetails();




        recycler_relatedProd = (RecyclerView) findViewById(R.id.recycler_relatedprod);
        LinearLayoutManager mLayoutManger = new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false);
        recycler_relatedProd.setLayoutManager(mLayoutManger);
        recycler_relatedProd.setItemAnimator(new DefaultItemAnimator());

        relatedProdAdapter = new BestSelling_Adapter(this,  relatedProdModelArrayList, GetScreenWidth() );
        recycler_relatedProd.setAdapter(relatedProdAdapter);


       add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // store produ id with user id on server  and get quate id as response and store it on share preferernce
                 if ( !prod_price.getText().toString().equals("") && prod_price.getText().toString()!= null)
                   addtocartapi( );

            }
        });

        add_to_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtowishlist();
            }
        });

    }

    public int GetScreenWidth(){
        int  width =100;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager =  (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        return width;

    }


    public void getProductDetails(){

        if (!NetworkUtility.isNetworkConnected(ProductDetails.this)){
            AppUtilits.displayMessage(ProductDetails.this,  getString(R.string.network_not_connected));

        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<ProductDetail_Res> call = service.getProductDetails("1234", prod_id );
            call.enqueue(new Callback<ProductDetail_Res>() {
                @Override
                public void onResponse(Call<ProductDetail_Res> call, Response<ProductDetail_Res> response) {
                    Log.e(TAG, "reponse is " + response.body().getInformation());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            if (response.body().getInformation().getDetails().getName()!=null){
                                prod_name.setText(response.body().getInformation().getDetails().getName());

                                if (response.body().getInformation().getDetails().getStock() >0){
                                    prod_stock.setText( getString(R.string.instock));
                                }else {
                                    prod_stock.setText( getString(R.string.outofstock));
                                }


                                prod_oldprice.setText( response.body().getInformation().getDetails().getMrp());
                                prod_price.setText(response.body().getInformation().getDetails().getPrice());
                                prod_qty.setText("1");
                                // prod image
                               // Log.e(TAG, "rating count "+)
                                prod_rating_count.setText(response.body().getInformation().getDetails().getRatingCount());
                                prod_rating.setRating(response.body().getInformation().getDetails().getRating());

                                if (response.body().getInformation().getRelatedprod().size()>0){
                                    relatedProdModelArrayList.clear();
                                    for (int i = 0; i < response.body().getInformation().getRelatedprod().size(); i++) {

                                        relatedProdModelArrayList.add(new BestSelling_Model(response.body().getInformation().getRelatedprod().get(i).getId(),
                                                response.body().getInformation().getRelatedprod().get(i).getName(),
                                                response.body().getInformation().getRelatedprod().get(i).getImgUrl(),
                                                response.body().getInformation().getRelatedprod().get(i).getMrp(),
                                                response.body().getInformation().getRelatedprod().get(i).getPrice(),
                                               String.valueOf( response.body().getInformation().getRelatedprod().get(i).getRating())) );


                                    }
                                    relatedProdAdapter.notifyDataSetChanged();


                                }
                                 prod_overview =response.body().getInformation().getDetails().getDesc();
                                 prod_fulldetails = response.body().getInformation().getDetails().getFulldetails();
                                 prod_review = response.body().getInformation().getReview();


                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                Overview overview = new Overview();
                                fragmentTransaction.add( R.id.frag_container, overview, "com.beliefitsolution.buyonline.productpreview.tabfragment");
                                fragmentTransaction.commit();



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
                public void onFailure(Call<ProductDetail_Res> call, Throwable t) {
                    Log.e(TAG, " fail best sell "+ t.toString());
                }
            });

        }


    }



    public void addtocartapi(){

        if (!NetworkUtility.isNetworkConnected(ProductDetails.this)){
            AppUtilits.displayMessage(ProductDetails.this,  getString(R.string.network_not_connected));

        }else {

           //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AddtoCart> call = service.addtoCartCall("12345", prod_id, SharePreferenceUtils.getInstance().getString(Constant.USER_DATA) , prod_price.getText().toString() );
            call.enqueue(new Callback<AddtoCart>() {
                @Override
                public void onResponse(Call<AddtoCart> call, Response<AddtoCart> response) {
                    Log.e(TAG, "reponse is " + response.body().getInformation());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            AppUtilits.displayMessage(ProductDetails.this, getString(R.string.add_to_cart));
                            SharePreferenceUtils.getInstance().saveString(Constant.QUOTE_ID, response.body().getInformation().getQouteId());

                            SharePreferenceUtils.getInstance().saveInt( Constant.CART_ITEM_COUNT,   response.body().getInformation().getCartCount());
                            AppUtilits.UpdateCartCount(ProductDetails.this,mainmenu);

                        }else {
                            AppUtilits.displayMessage(ProductDetails.this, getString(R.string.fail_add_to_cart));
                        }
                    }else {
                        AppUtilits.displayMessage(ProductDetails.this, getString(R.string.network_error));
                    }


                }

                @Override
                public void onFailure(Call<AddtoCart> call, Throwable t) {
                    Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    AppUtilits.displayMessage(ProductDetails.this, getString(R.string.fail_add_to_cart));
                }
            });
        }
    }




    public void addtowishlist(){
        if (!NetworkUtility.isNetworkConnected(ProductDetails.this)){
            AppUtilits.displayMessage(ProductDetails.this,  getString(R.string.network_not_connected));

        }else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AddtoCart> call = service.addtowishlistcall("12345", prod_id,SharePreferenceUtils.getInstance().getString(Constant.USER_DATA) );
            call.enqueue(new Callback<AddtoCart>() {
                @Override
                public void onResponse(Call<AddtoCart> call, Response<AddtoCart> response) {

                    Log.e(TAG, "reponse is " + response.body().getInformation());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            AppUtilits.displayMessage(ProductDetails.this, getString(R.string.add_to_wishlist));
                        }else {
                            AppUtilits.displayMessage(ProductDetails.this, getString(R.string.fail_add_to_wishlist));
                        }
                    }else {
                        AppUtilits.displayMessage(ProductDetails.this, getString(R.string.network_error));
                    }
                }

                @Override
                public void onFailure(Call<AddtoCart> call, Throwable t) {
                    AppUtilits.displayMessage(ProductDetails.this, getString(R.string.fail_add_to_wishlist));
                }
            });


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.myaccount_toolbar_menu, menu);
        mainmenu = menu;
        if (mainmenu!=null)
            AppUtilits.UpdateCartCount(ProductDetails.this, mainmenu);
        return true;
    }

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

/*
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id =  item.getItemId();
        Log.e(TAG, "navi option "+ item.getTitle());

        if (id == R.id.nav_home){


        }else if (id == R.id.nav_new_prod){


        }else if (id == R.id.nav_myaccount){


        }else if (id == R.id.nav_wishlist){
            Intent intent = new Intent(this, WishlistDetails.class);
            startActivity(intent);

        }else if (id == R.id.nav_logout){


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


*/
    @Override
    protected void onResume() {
        super.onResume();
        if (mainmenu!=null)
            AppUtilits.UpdateCartCount(ProductDetails.this, mainmenu);
    }


}
