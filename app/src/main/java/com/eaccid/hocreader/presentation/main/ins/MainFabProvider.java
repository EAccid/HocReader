package com.eaccid.hocreader.presentation.main.ins;

import android.app.Activity;
import android.support.annotation.AnimRes;
import android.support.design.widget.FloatingActionButton;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

public class MainFabProvider {

    public boolean displayFab(Activity context, FloatingActionButton fab, double rightMargin, double bottomMargin, @AnimRes int anim) {
        Animation show_fab_1 = AnimationUtils.loadAnimation(context.getApplication(), anim);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab.getLayoutParams();
        layoutParams.rightMargin += (int) (fab.getWidth() * rightMargin);
        layoutParams.bottomMargin += (int) (fab.getHeight() * bottomMargin);
        fab.setLayoutParams(layoutParams);
        fab.startAnimation(show_fab_1);
        fab.setClickable(true);
        return true;
    }

    public boolean hideFab(Activity context, FloatingActionButton fab, double rightMargin, double bottomMargin, @AnimRes int anim) {
        Animation hide_fab_1 = AnimationUtils.loadAnimation(context.getApplication(), anim);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab.getLayoutParams();
        layoutParams.rightMargin -= (int) (fab.getWidth() * rightMargin);
        layoutParams.bottomMargin -= (int) (fab.getHeight() * bottomMargin);
        fab.setLayoutParams(layoutParams);
        fab.startAnimation(hide_fab_1);
        fab.setClickable(false);
        return false;
    }
}
