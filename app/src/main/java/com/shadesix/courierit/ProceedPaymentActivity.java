package com.shadesix.courierit;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shadesix.courierit.fragments.MyOrderFragment;
import com.shadesix.courierit.fragments.OverviewFragment;
import com.shadesix.courierit.fragments.PaymentFragment;
import com.shadesix.courierit.fragments.ProfileFragment;
import com.shadesix.courierit.fragments.ReceiveOrderFragment;
import com.shadesix.courierit.fragments.ShippingFragment;

import java.util.ArrayList;
import java.util.List;

public class ProceedPaymentActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_payment);

        viewPager = (ViewPager) findViewById(R.id.viewpager1);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs1);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ProceedPaymentActivity.ViewPagerAdapter adapter = new ProceedPaymentActivity.ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new OverviewFragment(), "Overview");
        adapter.addFragment(new ShippingFragment(), "Shipping");
        adapter.addFragment(new PaymentFragment(), "Payment");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
