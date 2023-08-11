package com.lele.comp5216_assignment1.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lele.comp5216_assignment1.R;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;
    HomepageFragment homeFragment;
    CalendarFragment calendarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 创建两个Fragment的实例
        homeFragment = new HomepageFragment();
        calendarFragment = new CalendarFragment();

        // 首先将两个Fragment添加到FragmentManager，并隐藏calendarFragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homeFragment)
                .add(R.id.fragment_container, calendarFragment)
                .hide(calendarFragment)
                .commit();

        initView();
        setListener();

        // 如果保存的实例状态为空，则加载默认的Fragment
        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.page_1);  // 设置默认选中项
        }
    }

    private void initView() {
        navView = findViewById(R.id.bottom_navigation);
    }

    private void setListener() {
        navView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.page_1) {
                getSupportFragmentManager().beginTransaction()
                        .hide(calendarFragment)
                        .show(homeFragment)
                        .commit();
                // 调用获取所有日期的方法
                triggerGetAllItems();
            } else if (item.getItemId() == R.id.page_2) {
                getSupportFragmentManager().beginTransaction()
                        .hide(homeFragment)
                        .show(calendarFragment)
                        .commit();
                // 调用刷新新添加的日期
                triggerDecorateEventDate();
            }
            return true;  // 返回true表示已处理选择事件
        });
    }

    public void triggerDecorateEventDate() {
        if (calendarFragment != null) {
            calendarFragment.triggerDecorateEventDate();
        }
    }

    public void triggerGetAllItems() {
        if (homeFragment != null) {
            homeFragment.triggerGetAllItems();
        }
    }

}
