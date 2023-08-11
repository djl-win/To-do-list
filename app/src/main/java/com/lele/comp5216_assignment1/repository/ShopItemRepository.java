package com.lele.comp5216_assignment1.repository;

import android.app.Application;

import com.lele.comp5216_assignment1.dao.ShopItemDao;
import com.lele.comp5216_assignment1.entity.ShopItem;
import com.lele.comp5216_assignment1.shopItemDatabase.ShopItemDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

public class ShopItemRepository {
    private ShopItemDao shopItemDao;
    List<ShopItem> shopItems;
    List<String> allItemsDate; // 所有待办事项的日期

    List<CalendarDay> calendarDays;
    int[] itemsNumber;
    public ShopItemRepository(Application application) {
        ShopItemDatabase db = ShopItemDatabase.getInstance(application);  // 通过RoomDatabase的getInstance()方法获取数据库实例
        shopItemDao = db.shopItemDao();
        itemsNumber = new int[]{0, 0, 0};
        calendarDays = new ArrayList<>();
    }

    public void insertShopItem(ShopItem shopItem){
        //处理时间的转换
        String replace = shopItem.getShopItemDate().replace(".", "");
        shopItem.setShopItemDate(replace);
        //待做，添加描述

        shopItemDao.insert(shopItem);
    }
    public List<ShopItem> getAllShopItems(){
        shopItems = shopItemDao.getAllShopItems();
        return shopItems;
    }

    public List<ShopItem> getUndoneItems(){
        shopItems = shopItemDao.getUndoneItem();
        return shopItems;
    }

    public List<ShopItem> getDoneItems(){
        shopItems = shopItemDao.getDoneItem();
        return shopItems;
    }

    public void markItemAsDone(int shopItemId){
        shopItemDao.markItemAsDone(shopItemId);
    }

    public void markItemAsUndone(int shopItemId) {
        shopItemDao.markItemAsUndone(shopItemId);
    }

    public int[] selectItemNumbersByStatus() {

        itemsNumber[0] = shopItemDao.selectNumbersUndone();
        itemsNumber[1] = shopItemDao.selectNumbersDone();
        itemsNumber[2] = shopItemDao.selectNumbersAll();
        return itemsNumber;
    }

    public List<CalendarDay> selectAllDate() {

        allItemsDate = shopItemDao.selectAllDate();

        for (String date : allItemsDate) {
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(4, 6));
            int day = Integer.parseInt(date.substring(6, 8));
            CalendarDay calendarDay = CalendarDay.from(year, month, day); // 注意: month是基于0的，所以要-1
            calendarDays.add(calendarDay);
        }
        return calendarDays;
    }

    public void  deleteAll(){
        shopItemDao.deleteAll();
    }
}
