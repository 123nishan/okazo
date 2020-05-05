package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Point;
import android.os.Bundle;
import android.widget.Toast;

import com.example.okazo.Fragment.AddModeratorFragment;
import com.example.okazo.Fragment.AllModeratorFragment;
import com.example.okazo.Fragment.FeedFragment;
import com.example.okazo.Fragment.ProfileFragment;
import com.ferfalk.simplesearchview.SimpleSearchView;
import com.ferfalk.simplesearchview.utils.DimensUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


import java.util.ArrayList;

public class ModeratorActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] titles = new String[]{"Add Moderator", "Moderator"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderator);
        tabLayout=findViewById(R.id.moderator_activity_tab_layout);
        viewPager=findViewById(R.id.moderator_activity_view_pager);




        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new AddModeratorFragment(),"Add Moderator");
        viewPagerAdapter.addFragment(new AllModeratorFragment(),"Moderators");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        //viewPager.getAdapter().notifyDataSetChanged();
viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
       
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
});
        //simpleSearchView.showSearch();
        // Adding padding to the animation because of the hidden menu item

    }

    class ViewPagerAdapter extends FragmentPagerAdapter

    {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;
        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}
