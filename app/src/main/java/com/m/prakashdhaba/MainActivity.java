package com.m.prakashdhaba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.m.prakashdhaba.user_login.SigninActivity;
import com.m.prakashdhaba.utility.SharePreferenceUtils;

public class MainActivity extends AppCompatActivity {
 Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logout = findViewById(R.id.logout);
          logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreferenceUtils.getInstance().deletePref();
                Intent intent =new Intent(MainActivity.this, SigninActivity.class);
                startActivity(intent);

                finish();

            }
        });

    }


}
