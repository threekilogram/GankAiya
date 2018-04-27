package com.example.wuxio.gankexamples.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.constraintlayout.Constraint;
import com.example.constraintlayout.adapter.BaseConstraintAdapter;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.utils.ViewSupplier;

/**
 * @author wuxio 2018-04-27:13:37
 */
public class NavigationViewAdapter extends BaseConstraintAdapter {

    private final int childCount = 1;

    private Context   mainActivity;
    private ViewGroup navigationView;


    public NavigationViewAdapter(Context mainActivity, ViewGroup navigationView) {

        this.mainActivity = mainActivity;
        this.navigationView = navigationView;
    }


    @Override
    public View generateViewTo(int i) {

        switch (i) {
            case 0:
                return ViewSupplier.get(mainActivity, navigationView, R.layout.main_navi_header_logo_iv);
            case 1:
                return ViewSupplier.get(mainActivity, navigationView, R.layout.main_navi_header_head_iv);
            case 2:
                break;
            default:
                break;
        }

        return null;
    }


    @Override
    public Constraint generateConstraintTo(int i, Constraint constraint) {

        switch (i) {
            case 0:
                constraint.leftToLeftOfParent(0)
                        .rightToRightOfParent(0)
                        .topToTopOfParent(0, 500);
                break;
            case 1:
                constraint.leftToLeftOfView(0, 0, 200)
                        .bottomToBottomOfView(0, 0, 200);
            case 2:
                break;
            default:
                break;
        }

        return constraint;
    }


    @Override
    public void beforeLayout(int position, View view) {

        switch (position) {
            case 0:
                Glide.with(mainActivity).load(R.drawable.homepage_header).into((ImageView) view);
                break;
            case 1:
                Glide.with(mainActivity).load(R.drawable.homepage_header).into((ImageView) view);
            case 2:
                break;
            default:
                break;
        }
    }


    @Override
    public int getChildCount() {

        return childCount;
    }
}
