package com.m.prakashdhaba.web_service;

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

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceInterface {

    @Multipart
    @POST("dhaba/new_user_registration.php")
    Call<NewUserRegistration> NewUserRegistrationCall(
            @Part("fullname") RequestBody fullname,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password
    );
    ///  user signin request
    @Multipart
    @POST("dhaba/user_signin.php")
    Call<userSignin> UserSigninCall(
            @Part("phone") RequestBody phone,
            @Part("password") RequestBody password
    );
    ///  user forgot password request
    @Multipart
    @POST("dhaba/user_forgot_password.php")
    Call<ForgotPassword> UserForgotPassword(
            @Part("phone") RequestBody phone
    );
    ///  create new password request
    @Multipart
    @POST("dhaba/new_password.php")
    Call<NewPassword> UserNewPassword(
            @Part("userid") RequestBody userid,
            @Part("password") RequestBody password
    );

    // get banner image
    @Multipart
    @POST("dhaba/getbanner.php")
    Call<GetbannerModel> getbannerimagecall(
            @Part("securecode") RequestBody securecode
    );

    // get best selling products
    @Multipart
    @POST("dhaba/getbestsellingprod.php")
    Call<NewProdResponse> getBestSelling(
            @Part("securecode") RequestBody securecode
    );

    // get trending products
    @Multipart
    @POST("dhaba/gettrendingprod.php")
    Call<NewProdResponse> getTrendingProd(
            @Part("securecode") RequestBody securecode
    );


    // get product details
    @Multipart
    @POST("dhaba/getproductdetails.php")
    Call<ProductDetail_Res> getProductDetials(
            @Part("securecode") RequestBody securecode,
            @Part("prod_id") RequestBody prod_id
    );

    // get user cart
    @Multipart
    @POST("dhaba/getusercartdetails.php")
    Call<com.m.prakashdhaba.cart.getCartDetails> getusercartcall(
            @Part("securecode") RequestBody securecode,
            @Part("qoute_id") RequestBody qoute_id,
            @Part("user_id") RequestBody user_id
    );
    // add into wishlist
    @Multipart
    @POST("dhaba/add_prod_into_wishlist.php")
    Call<AddtoCart> addtowishlist(
            @Part("securecode") RequestBody securecode,
            @Part("prod_id") RequestBody prod_id,
            @Part("user_id") RequestBody user_id
    );
    // get order summery
    @Multipart
    @POST("dhaba/getordersummery.php")
    Call<OrderSummery> getOrderSummerycall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("qoute_id") RequestBody qoute_id
    );

    // get user address
    @Multipart
    @POST("dhaba/getUserAddress.php")
    Call<GetAddress> getUserAddress(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id
    );

    // add new address
    @Multipart
    @POST("dhaba/add_address.php")
    Call<AddNewAddress> addnewAddresscall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("fullname") RequestBody fullname,
            @Part("address1") RequestBody address1,
            @Part("address2") RequestBody address2,
            @Part("city") RequestBody city,
            @Part("state") RequestBody state,
            @Part("pincode") RequestBody pincode,
            @Part("phone") RequestBody phone


    );


    // add to cart
    @Multipart
    @POST("dhaba/add_prod_into_cart.php")
    Call<AddtoCart> addtocartcall(
            @Part("securecode") RequestBody securecode,
            @Part("prod_id") RequestBody prod_id,
            @Part("user_id") RequestBody user_id,
            @Part("prod_price") RequestBody prod_price
    );



    // delete cart item
    @Multipart
    @POST("dhaba/deletecartitem.php")
    Call<AddtoCart> deleteCartProd(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("prod_id") RequestBody prod_id
    );

    // edit cart qty
    @Multipart
    @POST("dhaba/editcartitem.php")
    Call<EditCartItem> editCartQty(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("prod_id") RequestBody prod_id,
            @Part("prod_qty") RequestBody prod_qty
    );
}
