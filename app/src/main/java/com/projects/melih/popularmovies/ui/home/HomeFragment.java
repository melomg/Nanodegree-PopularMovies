package com.projects.melih.popularmovies.ui.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.melih.popularmovies.R;
import com.projects.melih.popularmovies.databinding.FragmentHomeBinding;
import com.projects.melih.popularmovies.ui.base.BaseFragment;

/**
 * Created by Melih Gültekin on 1.03.2018
 */

public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding binding;
    private ViewPager.OnPageChangeListener listener;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        binding.viewPager.setAdapter(new BottomNavigationPagerAdapter(getChildFragmentManager()));
        listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //no-op
            }

            @Override
            public void onPageSelected(int position) {
                int selectedItemId;
                switch (position) {
                    case BottomNavigationPagerAdapter.BY_TOP_RATED:
                        selectedItemId = R.id.action_sort_highest_rated;
                        break;
                    case BottomNavigationPagerAdapter.BY_POPULAR:
                    default:
                        selectedItemId = R.id.action_sort_most_popular;
                        break;
                }
                binding.bottomNavigation.setSelectedItemId(selectedItemId);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //no-op
            }
        };
        binding.viewPager.addOnPageChangeListener(listener);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            //TODO await
            int viewPagerCurrentItem;
            switch (item.getItemId()) {
                case R.id.action_sort_highest_rated:
                    viewPagerCurrentItem = BottomNavigationPagerAdapter.BY_TOP_RATED;
                    break;
                case R.id.action_sort_most_popular:
                default:
                    viewPagerCurrentItem = BottomNavigationPagerAdapter.BY_POPULAR;
                    break;
            }
            binding.viewPager.setCurrentItem(viewPagerCurrentItem);
            return true;
        });

        return binding.getRoot();
    }
}