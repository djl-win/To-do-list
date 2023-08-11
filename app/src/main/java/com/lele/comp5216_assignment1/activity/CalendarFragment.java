package com.lele.comp5216_assignment1.activity;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.lele.comp5216_assignment1.R;
import com.lele.comp5216_assignment1.adapter.EventDecorator;
import com.lele.comp5216_assignment1.repository.ShopItemRepository;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment {
    private View rootView;  // 定义一个rootView成员变量
    Activity activity; // 定义activity
    ShopItemRepository shopItemRepository; // 获取ShopItemRepository
    MaterialCalendarView materialCalView; // 日历
    List<CalendarDay> eventDates; // 有事件的日期

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
        return rootView;
    }

    private void initViews() {
        materialCalView = rootView.findViewById(R.id.calendar_view); // 初始化日历组件
        eventDates = new ArrayList<>(); // 初始化有事件的日期
        // 默认日历显示当前日期
        materialCalView.setDateSelected(CalendarDay.today(), true);
        DecorateEventDate(); // 装饰有事件的日期
    }


    // 装饰有事件的日期
    private void DecorateEventDate() {
        // 查询数据库有时间的日期
        eventDates = shopItemRepository.selectAllDate();
        // 创建并应用装饰器
        int eventColor = ContextCompat.getColor(activity, R.color.select_date_button_color); // 圆点的颜色
        materialCalView.addDecorator(new EventDecorator(eventColor, eventDates));
    }

    // 间接在主activity中调用装饰的方法
    public void triggerDecorateEventDate() {
        DecorateEventDate();
    }
}
