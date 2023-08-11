package com.lele.comp5216_assignment1.shopItemDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lele.comp5216_assignment1.dao.ShopItemDao;
import com.lele.comp5216_assignment1.entity.ShopItem;

@Database(entities = {ShopItem.class}, version = 1, exportSchema = false)
public abstract class ShopItemDatabase extends RoomDatabase {
    private static ShopItemDatabase instance;

    public abstract ShopItemDao shopItemDao();

    public static synchronized ShopItemDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ShopItemDatabase.class, "shop_item_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()  // 这行代码允许在主线程上进行查询
                    .build();
        }
        return instance;
    }
}
