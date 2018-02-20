package com.projects.melih.popularmovies.ui.base;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by Melih Gültekin on 17.02.2018
 */

public class BaseFragment extends Fragment {

    protected Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}