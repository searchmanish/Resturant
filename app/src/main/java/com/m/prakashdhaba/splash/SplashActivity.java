package com.m.prakashdhaba.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.m.prakashdhaba.home.HomeActivity;
import com.m.prakashdhaba.R;
import com.m.prakashdhaba.user_login.SigninActivity;
import com.m.prakashdhaba.utility.Constant;
import com.m.prakashdhaba.utility.SharePreferenceUtils;

public class SplashActivity extends AppCompatActivity {

    private String TAG ="splashActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        Log.e(TAG, " splash start showing");
    }

    public void init(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /// if user registered user
                // then show home screen
                // else  show login screen
                // key  register_user
                Log.e(TAG, "  counter running ");
                String name = SharePreferenceUtils.getInstance().getString(Constant.USER_name);
              // Toast.makeText(SplashActivity.this,Constant.USER_DATA,Toast.LENGTH_LONG).show();
                if (SharePreferenceUtils.getInstance().getString(Constant.USER_DATA).equalsIgnoreCase("")){
                    // not registted user  so show login screen
                    Intent intent = new Intent(SplashActivity.this, SigninActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    // home sscreen
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        }, 3000 );

    }
}