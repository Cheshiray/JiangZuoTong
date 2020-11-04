package com.example.jiangzuotong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    private RadioGroup rg;
    private RadioButton rb_home;
    private RadioButton rb_settings;
    private ViewPager vpager;

    private MyFragmentPagerAdapter mAdapter;

    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
        rb_home.setChecked(true);
    }

    private void bindViews() {
        rg = (RadioGroup) findViewById(R.id.rg);
        rb_home = (RadioButton) findViewById(R.id.rb_home);
        rb_settings = (RadioButton) findViewById(R.id.rb_settings);

        rg.setOnCheckedChangeListener(this);

        vpager = (ViewPager) findViewById(R.id.view_pager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_home:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_settings:
                vpager.setCurrentItem(PAGE_TWO);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    rb_home.setChecked(true);
                    break;
                case PAGE_TWO:
                    rb_settings.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 2) {
            vpager.setCurrentItem(1);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}