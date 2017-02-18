package com.eaccid.hocreader.presentation.main.ins;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.settings.Preference;
import com.eaccid.hocreader.provider.semantic.ImageViewManager;

public class ActionBarDrawerToggleForReader extends ActionBarDrawerToggle {

    private NavigationView navigationView;

    public ActionBarDrawerToggleForReader(
            Activity activity,
            DrawerLayout drawerLayout,
            Toolbar toolbar,
            @StringRes int openDrawerContentDescRes,
            @StringRes int closeDrawerContentDescRes,
            NavigationView navigationView) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        this.navigationView = navigationView;
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        SharedPreferences sp = drawerView.getContext()
                .getSharedPreferences(Preference.SHP_NAME_AUTH, Context.MODE_PRIVATE);
        String FULL_NAME_LEO = sp.getString(Preference.FULL_NAME_LEO, drawerView.getContext().getString(R.string.app_name));
        String PICTURE_URL_LEO = sp.getString(Preference.PICTURE_URL_LEO, "");
        String EMAIL_LEO = sp.getString(Preference.EMAIL_LEO, drawerView.getContext().getString(R.string.helloreader_eaccid_com));
        View hView = navigationView.getHeaderView(0);
        ImageView imageView = (ImageView) hView.findViewById(R.id.navigation_drawer_user_account_picture_profile);
        new ImageViewManager().loadPictureFromUrl(
                imageView,
                PICTURE_URL_LEO,
                R.drawable.empty_circle_background_account,
                R.drawable.empty_circle_background_account,
                true
        );
        TextView name = (TextView) hView.findViewById(R.id.navigation_drawer_account_information_display_name);
        TextView email = (TextView) hView.findViewById(R.id.navigation_drawer_account_information_email);
        name.setText(FULL_NAME_LEO);
        email.setText(EMAIL_LEO);
    }
}
