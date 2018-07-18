package com.m.prakashdhaba.web_service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.m.prakashdhaba.BuildConfig;
import com.m.prakashdhaba.bean_response.AddNewAddress;
import com.m.prakashdhaba.bean_response.AddtoCart;
import com.m.prakashdhaba.bean_response.EditCartItem;
import com.m.prakashdhaba.bean_response.ForgotPassword;
import com.m.prakashdhaba.bean_response.GetAddress;
import com.m.prakashdhaba.bean_response.GetbannerModel;
import com.m.prakashdhaba.bean_response.NewPassword;
import com.m.prakashdhaba.bean_response.NewProdResponse;
import com.m.prakashdhaba.bean_response.NewUserRegistration;
import com.m.prakashdhaba.bean_response.OrderSummery;
import com.m.prakashdhaba.bean_response.ProductDetail_Res;
import com.m.prakashdhaba.bean_response.userSignin;
import com.m.prakashdhaba.utility.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceWrapper {
    private ServiceInterface mServiceInterface;

    public ServiceWrapper(Interceptor mInterceptorheader) {
        mServiceInterface = getRetrofit(mInterceptorheader).create(ServiceInterface.class);
    }

    public Retrofit getRetrofit(Interceptor mInterceptorheader) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient mOkHttpClient = null;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(Constant.API_CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(Constant.API_READ_TIMEOUT, TimeUnit.SECONDS);

//        if (BuildConfig.DEBUG)
//            builder.addInterceptor(loggingInterceptor);

        if (BuildConfig.DEBUG) {
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }


        mOkHttpClient = builder.build();
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(mOkHttpClient)
                .build();
        return retrofit;
    }

    public Call<NewUserRegistration> newUserRegistrationCall(String fullname, String email, String phone, String username, String password) {
        return mServiceInterface.NewUserRegistrationCall
                (convertPlainString(fullname), convertPlainString(email), convertPlainString(phone), convertPlainString(username),
                convertPlainString(password));
    }


    ///  user signin
    public Call<userSignin> UserSigninCall(String phone, String password){
        return mServiceInterface.UserSigninCall(convertPlainString(phone),  convertPlainString(password));
    }

    ///  user signin
    public Call<ForgotPassword> UserForgotPassword(String phone){
        return mServiceInterface.UserForgotPassword(convertPlainString(phone));
    }
    ///  user new password
    public Call<NewPassword> UserNewPassword(String userid, String password){
        return mServiceInterface.UserNewPassword(convertPlainString(userid), convertPlainString(password));
    }



    // get banner image
    public Call<GetbannerModel> getbannerModelCall(String securcode){
        return mServiceInterface.getbannerimagecall(convertPlainString(securcode) );
    }

    ///  best selling product details
    public Call<NewProdResponse> getBestselling(String securcode){
        return mServiceInterface.getBestSelling(convertPlainString(securcode));
    }


    ///  get trending  product details
    public Call<NewProdResponse> getTrendingProd(String securcode){
        return mServiceInterface.getTrendingProd(convertPlainString(securcode));
    }



    // get product detials
    public Call<ProductDetail_Res> getProductDetails(String securcode, String prod_id){
        return mServiceInterface.getProductDetials(convertPlainString(securcode), convertPlainString(prod_id));
    }

    // convert aa param into plain text
    public RequestBody convertPlainString(String data){
        RequestBody plainString = RequestBody.create(MediaType.parse("text/plain"), data);
        return  plainString;
    }

    // get user cart
    public Call<com.m.prakashdhaba.cart.getCartDetails> getCartDetailsCall(String securcode, String qoute_id, String user_id){
        return mServiceInterface.getusercartcall(convertPlainString(securcode), convertPlainString(qoute_id),convertPlainString(user_id) );
    }
    // add to wishlist
    public Call<AddtoCart> addtowishlistcall(String securcode, String prod_id, String user_id){
        return mServiceInterface.addtowishlist(convertPlainString(securcode), convertPlainString(prod_id),convertPlainString(user_id) );
    }

    // get order summery
    public Call<OrderSummery> getOrderSummerycall(String securcode, String user_id, String qoute_id){
        return mServiceInterface.getOrderSummerycall(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(qoute_id) );
    }
    // get order summery
    public Call<GetAddress> getUserAddresscall(String securcode, String user_id){
        return mServiceInterface.getUserAddress(convertPlainString(securcode), convertPlainString(user_id) );

    }
    // add new address
    public Call<AddNewAddress> addNewAddressCall(String securcode, String user_id, String fullname, String address1, String adress2, String city, String state,
                                                 String pincode, String phone){
        return mServiceInterface.addnewAddresscall(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(fullname), convertPlainString(address1)
                , convertPlainString(adress2), convertPlainString(city), convertPlainString(state), convertPlainString(pincode), convertPlainString(phone));
    }

    // add to cart
    public Call<AddtoCart> addtoCartCall(String securcode, String prod_id, String user_id, String prod_price){
        return mServiceInterface.addtocartcall(convertPlainString(securcode), convertPlainString(prod_id),convertPlainString(user_id), convertPlainString(prod_price) );
    }

    // delete cart item
    public Call<AddtoCart> deletecartprod(String securcode, String user_id, String prod_id){
        return mServiceInterface.deleteCartProd(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(prod_id) );
    }
    // edit cart item
    public Call<EditCartItem> editcartcartprodqty(String securcode, String user_id, String prod_id, String prod_qty){
        return mServiceInterface.editCartQty(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(prod_id),  convertPlainString(prod_qty) );
    }


}
