package com.example.equipmentstore;

import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;


import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.example.equipmentstore.extra.FunctionCall;
import com.example.equipmentstore.fragments.AttendanceInsert;
import com.example.equipmentstore.fragments.AttendanceReport;
import com.example.equipmentstore.fragments.HardwareComplaint;
import com.example.equipmentstore.fragments.MainFragment;
import com.example.equipmentstore.fragments.ManagementApproval;
import com.example.equipmentstore.fragments.PostOffice;
import com.example.equipmentstore.fragments.Received_Aproval;
import com.example.equipmentstore.fragments.ReceviedPost;
import com.example.equipmentstore.fragments.RequestFragment;
import com.example.equipmentstore.fragments.Staff_Outgoing;
import com.example.equipmentstore.fragments.ViewComplaint;
import com.example.equipmentstore.fragments.ViewFragment;
import com.example.equipmentstore.fragments.Visitors_Fragment;
import com.example.equipmentstore.models.LoginDetails;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.example.equipmentstore.extra.constants.MyPREFERENCES;
import static com.example.equipmentstore.extra.constants.sPref_Group;
import static com.example.equipmentstore.extra.constants.sPref_SUBDIVCODE;
import static com.example.equipmentstore.extra.constants.sPref_USERNAME;

public class

MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button equipment, management, view_complaint;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private boolean isFirstBackPressed = false;
    DrawerLayout drawer;
    private List<LoginDetails> loginDetailsList;
    static final float END_SCALE = 0.7f;
    private FunctionCall functionsCall;
    Toolbar toolbar;
    SharedPreferences settings;
    NavigationView navigationView;
    CoordinatorLayout coordinate;
    NavController navController;
    private AppBarConfiguration mAppBarConfiguration;

    public enum Steps {

        FORM0(RequestFragment.class),
        FORM1(Received_Aproval.class),
        FORM2(ViewFragment.class),
        //FORM3(ViewAllApprovalFragment.class),
        FORM4(ManagementApproval.class),
        FORM5(MainFragment.class),
        FORM6(PostOffice.class),
        FORM7(Visitors_Fragment.class),
        FORM8(Staff_Outgoing.class),
        FORM9(AttendanceReport.class),
        FORM10(AttendanceInsert.class),
        FORM11(HardwareComplaint.class),
        FORM12(ViewComplaint.class),
        FORM13(ReceviedPost.class);

        private Class clazz;

        Steps(Class clazz) {
            this.clazz = clazz;
        }

        public Class getFragClass() {
            return clazz;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinate = findViewById(R.id.coordinate);
        intialization();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView user_name = header.findViewById(R.id.nav_user_name);
        TextView subdiv_code = header.findViewById(R.id.nav_subdiv_code);
        subdiv_code.setText(settings.getString(sPref_SUBDIVCODE,"0"));

        ImageView tv_close = header.findViewById(R.id.myImageView);
        tv_close.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(MainActivity.this, SplashScreen.class));
            finish();
        });

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.main,R.id.equipment_insert,
                R.id.nav_recieved,R.id.view,R.id.management_nav,
                R.id.postoffice_nav,R.id.visitor_nav,R.id.staffoutgoing_nav).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        Menu nav_Menu = navigationView.getMenu();

        if (settings.getString(sPref_Group, "0").equals("Management")) {
            nav_Menu.findItem(R.id.management_nav).setVisible(true);
            nav_Menu.findItem(R.id.postoffice_nav).setVisible(true);
            nav_Menu.findItem(R.id.visitor_nav).setVisible(true);
            nav_Menu.findItem(R.id.staffoutgoing_nav).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.management_nav).setVisible(false);
            nav_Menu.findItem(R.id.postoffice_nav).setVisible(false);
            nav_Menu.findItem(R.id.visitor_nav).setVisible(false);
            nav_Menu.findItem(R.id.staffoutgoing_nav).setVisible(false);
        }
        nav_Menu.findItem(R.id.management_nav).setVisible(true);
        //*****************************set app version to drawer******************************************************************************
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String main_curr_version = null;
        if (pInfo != null) {
            main_curr_version = pInfo.versionName;
        }


        NavigationView logout_navigationView = findViewById(R.id.nav_drawer_bottom);
        logout_navigationView.setNavigationItemSelectedListener(this);
        Menu menu = logout_navigationView.getMenu();
        MenuItem nav_login = menu.findItem(R.id.nav_version);
        nav_login.setTitle("Version" + " : " + main_curr_version);
        nav_login.setOnMenuItemClickListener(item -> true);
        naviagtionDrawer();
        //switchContent(Steps.FORM5, "Store Management");

    }

    private void intialization() {
        Intent intent = getIntent();
        settings = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        fragmentManager = getSupportFragmentManager();
        if (intent != null) {
            loginDetailsList = (ArrayList<LoginDetails>) intent.getSerializableExtra("loginList");
        }
        functionsCall = new FunctionCall();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.equipment_insert) {
            navController.navigate(R.id.equipment_insert);
           // switchContent(Steps.FORM0, "Equipment Insert");
        } else if (id == R.id.nav_recieved) {
            navController.navigate(R.id.nav_recieved);
           // switchContent(Steps.FORM1, "ReceivedApproval");
        } else if (id == R.id.view) {
            navController.navigate(R.id.view);
          //  switchContent(Steps.FORM2, "View Approvals ");
        } else if (id == R.id.management_nav) {
            navController.navigate(R.id.management_nav);
          //  switchContent(Steps.FORM4, "Management Apporval");
        } else if (id == R.id.main) {
            navController.navigate(R.id.main);
          //  switchContent(Steps.FORM5, "Store Management");
        } else if (id == R.id.postoffice_nav) {
            navController.navigate(R.id.postoffice_nav)
            ;
          //  switchContent(Steps.FORM6, "PostOfficeLetter");
        } else if (id == R.id.visitor_nav) {
            navController.navigate(R.id.visitor_nav);
           // switchContent(Steps.FORM7, "Visitors Info");
        } else if (id == R.id.staffoutgoing_nav) {
            navController.navigate(R.id.staffoutgoing_nav);
          //  switchContent(Steps.FORM8, "Staff Out Going");
        }
        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchContent(Steps currentForm, String title) {
        try {
            fragment = (Fragment) currentForm.getFragClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        toolbar.setTitle(title);
        ft.replace(R.id.container_navigation, fragment, currentForm.name());
        ft.commit();
    }

    public void snackBar() {
        final DrawerLayout linearLayout = findViewById(R.id.drawer_layout);
        Snackbar snackbar = Snackbar.make(linearLayout, "Please turn on internet & proceed.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void naviagtionDrawer() {
        //Naviagtion Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.main);
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                coordinate.setScaleX(offsetScale);
//                coordinate.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = coordinate.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                coordinate.setTranslationX(xTranslation);
            }
        });

    }

    public void onBackPressed() {
        final AlertDialog alertDialog;
        AlertDialog.Builder manual_upload = new AlertDialog.Builder(MainActivity.this);
        @SuppressLint("InflateParams")
        LinearLayout manual_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);
        manual_upload.setView(manual_layout);
        manual_upload.setCancelable(false);
        Button yes = functionsCall.btnId(manual_layout, R.id.dialog_positive_btn);
        yes.setText(getResources().getString(R.string.select_ok));
        Button no = functionsCall.btnId(manual_layout, R.id.dialog_negative_btn);
        alertDialog = manual_upload.create();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
                alertDialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    public void visibleGone(boolean todo){
        /*View redLayout = findViewById(R.id.toolbar);
        ViewGroup parent = findViewById(R.id.drawer_layout);

        Transition transition = new Slide(Gravity.TOP);
        transition.setDuration(1000);
        transition.addTarget(R.id.toolbar);

        TransitionManager.beginDelayedTransition(parent, transition);*/
        toolbar.setVisibility(todo ? View.VISIBLE : View.GONE);
    }
}

