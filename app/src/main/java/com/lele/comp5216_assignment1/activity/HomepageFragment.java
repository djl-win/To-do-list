package com.lele.comp5216_assignment1.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayout;
import com.lele.comp5216_assignment1.R;
import com.lele.comp5216_assignment1.adapter.ShopItemAdapter;
import com.lele.comp5216_assignment1.entity.ShopItem;
import com.lele.comp5216_assignment1.repository.ShopItemRepository;

import java.util.List;

public class HomepageFragment extends Fragment implements AddItemDialog.OnDialogShowListener, AddItemDialog.OnDialogDismissListener, ShopItemAdapter.ItemClickListener {


    private View rootView;  // 定义一个rootView成员变量
    Activity activity; // 定义activity
    MainActivity mainActivity; // 获取主activity的activity
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    List<ShopItem> items;
    View dimBackground;
    LayoutInflater inflater;
    ShopItemRepository shopItemRepository;
    private ShopItemAdapter shopItemAdapter;
    TabLayout tabLayout;
    //初始化查询未完成项目
    int selectedStatus = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 膨胀fragment的视图
        rootView = inflater.inflate(R.layout.fragment_homepage, container, false);

        // 实例化 ShopItemRepository，定义定义activity
        activity = getActivity();
        // 获取主activity的activity
        mainActivity = (MainActivity) getActivity();
        if (activity != null) {
            Application application = activity.getApplication();
            shopItemRepository = new ShopItemRepository(application);
        } else {
            // Handle error or throw an exception
        }

        // 初始化视图
        initViews();

        return rootView;
    }


    // 获取xml中资源
    private void initViews() {
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
        listView = rootView.findViewById(R.id.list_view);
        dimBackground = mainActivity.findViewById(R.id.dim_background); // 获取主activity的view
        tabLayout = rootView.findViewById(R.id.tabLayout);

        //在主页面的列表下面添加footer
        inflater = getLayoutInflater();
        View footerView = inflater.inflate(R.layout.listview_footer, listView, false);
        listView.addFooterView(footerView);

        //获取当前页面的购物请求
        items = shopItemRepository.getUndoneItems();
        shopItemAdapter = new ShopItemAdapter(getContext(), R.layout.listview_item, items,selectedStatus,this);
        listView.setAdapter(shopItemAdapter);
        //显示所有的时间数量
        getItemsNumberClassifyByDoneUndoneAll();

        //上部导航栏监听器
        tabLayoutListener();

        //下滑监听器
        scrollDownListener();

    }

    // 获取数据库中相应的数据，展示在主页面
    private void getAllItems(int selectedStatus) {
        // 初始化待办事项列表
        // 应用item布局
        if(shopItemRepository == null) return;
        //未完成项目
        if(selectedStatus == 0) {
            items = shopItemRepository.getUndoneItems();
        }
        //已完成项目
        else if(selectedStatus == 1){
            items = shopItemRepository.getDoneItems();
        }
        //全部项目
        else if(selectedStatus == 2){
            items = shopItemRepository.getAllShopItems();
        }

        shopItemAdapter.updateList(selectedStatus,items);

        getItemsNumberClassifyByDoneUndoneAll();
    }

    private void getItemsNumberClassifyByDoneUndoneAll() {
        // 获取所有项目的个数,0为undone，1为done，2为所有
        int[] itemsNumber = shopItemRepository.selectItemNumbersByStatus();
        if (tabLayout != null) {
            TabLayout.Tab tab1 = tabLayout.getTabAt(0);
            if (tab1 != null) {
                String undoneText = getString(R.string.undone);  // 获取实际的字符串值
                tab1.setText(undoneText + " (" + itemsNumber[0] + ")");
            }

            TabLayout.Tab tab2 = tabLayout.getTabAt(1);
            if (tab2 != null) {
                String doneText = getString(R.string.done);  // 获取实际的字符串值
                tab2.setText(doneText + " (" + itemsNumber[1] + ")");
            }

            TabLayout.Tab tab3 = tabLayout.getTabAt(2);
            if (tab3 != null) {
                String allText = getString(R.string.all);  // 获取实际的字符串值
                tab3.setText(allText + " (" + itemsNumber[2] + ")");
            }
        }
    }

    // 上部tabLayout监听器
    private void tabLayoutListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedStatus = tab.getPosition();
                getAllItems(selectedStatus);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    // 下滑监听器
    private void scrollDownListener() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            //弹出dialog，里面选择信息
            showAddItemDialog();
            // 设置刷新为false，这将停止刷新动画
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    // 显示添加对话框
    private void showAddItemDialog() {
        AddItemDialog dialog = new AddItemDialog(); // 创建登录对话框实例
        dialog.setOnDialogDismissListener(this); // 绑定dialog，消失时候的监听器
        dialog.setOnDialogShowListener(this); // 绑定dialog，出现时候的监听器
        dialog.show(getParentFragmentManager(), "AddItemDialog"); // 显示登录对话框
    }

    // 显示阴影
    public void showDimBackground() {
        dimBackground.setVisibility(View.VISIBLE);
        activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.dialog_background_color));

    }

    // 关闭阴影
    public void hideDimBackground() {
        dimBackground.setVisibility(View.GONE);
        activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.white));
    }

    // 关闭阴影
    @Override
    public void onDialogDismissed() {
        hideDimBackground();
        getAllItems(selectedStatus);
    }

    // 打开阴影
    @Override
    public void onDialogShowed() {
        showDimBackground();
    }

    // 点击事件，弹出对话框
    @Override
    public void onItemClick(int shopItemId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //dialog打开时显示以阴影
        showDimBackground();
        //dialog底部设置为灰色
        activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity, R.color.dialog_background_color));
        builder.setTitle("操作");
        builder.setMessage("是否已完成？");

        builder.setNegativeButton("是", (dialog, which) -> {
            // 如果需要的话，在这里放置点击“确认”按钮时的操作
            shopItemRepository.markItemAsDone(shopItemId);
        });

        builder.setPositiveButton("否",  (dialog, which) -> {
            // 如果需要的话，在这里放置点击“确认”按钮时的操作
            shopItemRepository.markItemAsUndone(shopItemId);
        });
        AlertDialog dialog = builder.create();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                onDialogDismissed();
                // dialog底部设置为白色
                activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity, R.color.white));

            }
        });

        dialog.show();
    }

    public void triggerGetAllItems() {
        getAllItems(selectedStatus);
    }
}
