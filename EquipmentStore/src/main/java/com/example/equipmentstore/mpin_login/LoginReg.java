package com.example.equipmentstore.mpin_login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.equipmentstore.R;
import com.google.android.material.tabs.TabLayout;

import static com.example.equipmentstore.extra.constants.MyPREFERENCES;
import static com.example.equipmentstore.extra.constants.sPref_USERNAME;

public class LoginReg extends AppCompatActivity {

    SharedPreferences preferences;
    TextView textView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reg);
        textView = findViewById(R.id.textView);
        tabLayout = findViewById(R.id.tabLayout);
        preferences = getSharedPreferences(MyPREFERENCES, getApplicationContext().MODE_PRIVATE);//shared preference initialization

        textView.setText("Welcome  " + preferences.getString(sPref_USERNAME, "NA"));
        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Password"));
        tabLayout.addTab(tabLayout.newTab().setText("MPIN"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = findViewById(R.id.pager);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        selectPage(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    void selectPage(int pageIndex) {
        tabLayout.getTabAt(pageIndex).select();
        viewPager.setCurrentItem(pageIndex);
    }

}