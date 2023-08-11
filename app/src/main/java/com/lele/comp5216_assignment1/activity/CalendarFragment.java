package com.lele.comp5216_assignment1.activity;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.lele.comp5216_assignment1.R;
import com.lele.comp5216_assignment1.adapter.EventDecorator;
import com.lele.comp5216_assignment1.adapter.ShopItemAdapter;
import com.lele.comp5216_assignment1.entity.ShopItem;
import com.lele.comp5216_assignment1.repository.ShopItemRepository;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment implements ShopItemAdapter.ItemClickListener {
    private View rootView;  // 定义一个rootView成员变量
    Activity activity; // 定义activity
    ShopItemRepository shopItemRepository; // 获取ShopItemRepository
    MaterialCalendarView materialCalView; // 日历
    List<CalendarDay> eventDates; // 有事件的日期
    ListView listView; //日历页面列表框
    ShopItemAdapter shopItemAdapter; // 自定义list
    List<ShopItem> items; // 查询到的items
    CalendarDay todayDate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 膨胀fragment的视图
        rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        // 实例化 ShopItemRepository，定义定义activity
        activity = getActivity();

        if (activity != null) {
            Application application = activity.getApplication();
            shopItemRepository = new ShopItemRepository(application);
        }

        // 初始化视图
        initViews();
        // 设置监听器
        setListener();
        return rootView;
    }

    private void initViews() {
        materialCalView = rootView.findViewById(R.id.calendar_view); // 初始化日历组件
        eventDates = new ArrayList<>(); // 初始化有事件的日期
        // 默认日历显示当前日期
        materialCalView.setDateSelected(CalendarDay.today(), true);
        // 设置当前日期
        todayDate = CalendarDay.today();
        DecorateEventDate(); // 装饰有事件的日期

        // 初始化列表框
        listView = rootView.findViewById(R.id.calendar_list_view);
        // 从数据库查询特定日期的信息

        // 从数据库查询信息
        items = shopItemRepository.getItemsByDate(todayDate);
        // 加载适配器
        shopItemAdapter = new ShopItemAdapter(getContext(), R.layout.listview_item, items,2,this);
        listView.setAdapter(shopItemAdapter);

    }

    // 装饰有事件的日期
    private void DecorateEventDate() {
        // 查询数据库有时间的日期
        eventDates = shopItemRepository.selectAllDate();
        // 创建并应用装饰器
        int eventColor = ContextCompat.getColor(activity, R.color.select_date_button_color); // 圆点的颜色
        materialCalView.removeDecorators();
        materialCalView.addDecorator(new EventDecorator(eventColor, eventDates));
    }

    private void getCalendarItems(CalendarDay date) {
        // 从数据库查询信息
        items = shopItemRepository.getItemsByDate(date);
        // 加载列表数据
        shopItemAdapter.updateList(2,items);
    }

    private void setListener() {
        // 监听calendarView的改变，并获取calendarDay对象
        materialCalView.setOnDateChangedListener((widget, date, selected) -> {
            todayDate = date;
            // 从数据库查询特定日期的信息
            getCalendarItems(todayDate);
        });
    }

    // 间接在主activity中调用装饰的方法,当切换到日历页面时，会刷新这个页面，获取新的数据
    public void triggerDecorateEventDate() {
        DecorateEventDate();
        // 从数据库查询特定日期的信息
        getCalendarItems(todayDate);
    }

    @Override
    public void onItemClick(int shopItemId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Action");
        builder.setMessage("Is it marked as completed?");

        builder.setNegativeButton("yes", (dialog, which) -> {
            // 如果需要的话，在这里放置点击“确认”按钮时的操作
            shopItemRepository.markItemAsDone(shopItemId);
            // 从数据库查询特定日期的信息
            getCalendarItems(todayDate);
        });

        builder.setPositiveButton("no",  (dialog, which) -> {
            // 如果需要的话，在这里放置点击“确认”按钮时的操作
            shopItemRepository.markItemAsUndone(shopItemId);
            // 从数据库查询特定日期的信息
            getCalendarItems(todayDate);
        });
        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                // 从数据库查询特定日期的信息
                getCalendarItems(todayDate);
            }
        });
        dialog.show();
    }
}
