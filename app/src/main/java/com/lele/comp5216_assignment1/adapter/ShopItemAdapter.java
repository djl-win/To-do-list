package com.lele.comp5216_assignment1.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.lele.comp5216_assignment1.R;
import com.lele.comp5216_assignment1.entity.ShopItem;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class ShopItemAdapter extends ArrayAdapter<ShopItem> {
    // 回调接口，获取单机事件的回调
    public interface ItemClickListener {
        void onItemClick(int shopItemId);
    }
    private ItemClickListener itemClickListener;
    private Context context;
    private List<ShopItem> items;
    private int status;
    Calendar calendar;
    private String todayDate;

    public ShopItemAdapter(Context context, int resource, List<ShopItem> items,int status, ItemClickListener listener) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
        this.status = status;
        this.todayDate = getCurrentDateString();
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, parent, false);
        }

        TextView itemName = convertView.findViewById(R.id.shop_item_name);
        TextView itemDate = convertView.findViewById(R.id.shop_item_date); // 这里假设您已在布局中添加了一个显示日期的TextView，并为其分配了id


        // 颜色修改时候，再改回原来的，不然可能会出现，之前修改的数据，现在还保留
        itemName.setTextColor(ContextCompat.getColor(context, R.color.black)); // 默认颜色, 请替换为适当的颜色资源
        itemName.setPaintFlags(itemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        itemDate.setTextColor(ContextCompat.getColor(context, R.color.dialog_background_color)); // 默认颜色, 请替换为适当的颜色资源
        itemDate.setPaintFlags(itemDate.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));


        ShopItem currentItem = items.get(position);
        itemName.setText(currentItem.getShopItemName());
        //日期转化
        String dataBeforeConvert = currentItem.getShopItemDate();
        String year = dataBeforeConvert.substring(0, 4);
        String month = dataBeforeConvert.substring(4, 6);
        String day = dataBeforeConvert.substring(6, 8);
        String dataAfterConvert = year + "." + month + "." + day;
        itemDate.setText(dataAfterConvert);

        //检查是否过期
        boolean isExpired = todayDate.compareTo(dataBeforeConvert) > 0;
        // 针对过期的情况
        if (isExpired) {
            if (status == 0 || (status == 2 && currentItem.getShopItemStatus() != 1)) {
                String formattedString = getContext().getResources().getString(R.string.expired_label, dataAfterConvert);
                itemDate.setText(formattedString);
                itemDate.setTextColor(ContextCompat.getColor(context, R.color.red)); // 将文本颜色更改为红色，显示已过期
            }
        }

        // 针对完成和未完成的事项
        if (status == 1 || (status == 2 && currentItem.getShopItemStatus() == 1)) {
            itemName.setTextColor(ContextCompat.getColor(context, R.color.dialog_background_color)); // 将文本颜色更改为灰色
            itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); // 添加横线
        }

        convertView.setOnClickListener(v -> {
            // 调用接口的回调方法，将点击的位置传递给 MainActivity
            if (itemClickListener != null) {
                itemClickListener.onItemClick(currentItem.getShopItemId());
            }
            // 进行动画等其他操作
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.on_click);
            v.startAnimation(animation);
        });
        return convertView;
    }

    public String getCurrentDateString() {
        calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(calendar.getTime());
    }
}
