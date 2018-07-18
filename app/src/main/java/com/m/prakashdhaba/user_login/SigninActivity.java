package com.m.prakashdhaba.user_login;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.m.prakashdhaba.home.HomeActivity;
import com.m.prakashdhaba.MainActivity;
import com.m.prakashdhaba.R;
import com.m.prakashdhaba.bean_response.userSignin;
import com.m.prakashdhaba.utility.AppUtilits;
import com.m.prakashdhaba.utility.Constant;
import com.m.prakashdhaba.utility.DataValidation;
import com.m.prakashdhaba.utility.NetworkUtility;
import com.m.prakashdhaba.utility.SharePreferenceUtils;
import com.m.prakashdhaba.web_service.ServiceWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity {
    private String TAG = "SigninActivity";
    private TextView skip, forgot_password, login;
    private EditText phone_no, password;
    private LinearLayout signup_here;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Log.e(TAG,"  start siggnin Activity" );

        skip =(TextView) findViewById(R.id.btn_skip);
        login = (TextView) findViewById(R.id.login);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        signup_here = (LinearLayout) findViewById(R.id.layout_signup_here);

        phone_no = (EditText) findViewById(R.id.phone_number);
        password = (EditText) findViewById(R.id.password);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signup_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if ( DataValidation.isValidPhoneNumber(phone_no.getText().toString())){
                    AppUtilits.displayMessage(SigninActivity.this, getString(R.string.phone_no) + " "+ getString(R.string.is_invalid));

                }else if (DataValidation.isNotValidPassword(password.getText().toString())){
                    AppUtilits.displayMessage(SigninActivity.this, getString(R.string.password) + " "+ getString(R.string.is_invalid));

                }else {

                    sendUserLoginData();

                }

            }
        });

    }

    public void sendUserLoginData(){

        if (!NetworkUtility.isNetworkConnected(SigninActivity.this)){
            AppUtilits.displayMessage(SigninActivity.this,  getString(R.string.network_not_connected));

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<userSignin> userSigninCall = serviceWrapper.UserSigninCall(phone_no.getText().toString(), password.getText().toString());
            userSigninCall.enqueue(new Callback<userSignin>() {
                @Override
                public void onResponse(Call<userSignin> call, Response<userSignin> response) {

                    Log.d(TAG, "reponse : "+ response.toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1){
                            // store userdata to share prerference
                            Log.e(TAG, "  user data "+  response.body().getInformation());
                           //Toast.makeText(SigninActivity.this,"data"+response.body().getInformation().getFullname(),Toast.LENGTH_LONG).show();
                            SharePreferenceUtils.getInstance().saveString(Constant.USER_DATA, response.body().getInformation().getUserId());
                            SharePreferenceUtils.getInstance().saveString(Constant.USER_name, response.body().getInformation().getFullname());
                            SharePreferenceUtils.getInstance().saveString(Constant.USER_email, response.body().getInformation().getEmail());
                           SharePreferenceUtils.getInstance().saveString(Constant.USER_phone, response.body().getInformation().getPhone());


                            // start home activity
                            Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
                            //intent.putExtra("userid", "sdfsd");
                            startActivity(intent);
                            finish();

                        }else {
                            AppUtilits.displayMessage(SigninActivity.this,  response.body().getMsg());
                        }
                    }else {
                        AppUtilits.displayMessage(SigninActivity.this,  getString(R.string.failed_request));

                    }
                }

                @Override
                public void onFailure(Call<userSignin> call, Throwable t) {
                    Log.e(TAG, " failure "+ t.toString());
                    AppUtilits.displayMessage(SigninActivity.this,  getString(R.string.failed_request));

                }
            });




        }








    }


}
