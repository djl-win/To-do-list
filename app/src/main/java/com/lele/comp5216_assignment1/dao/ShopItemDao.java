package com.lele.comp5216_assignment1.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.lele.comp5216_assignment1.entity.ShopItem;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ShopItemDao {

    @Insert
    void insert(ShopItem shopItem);

    //查询全部的清单
    @Query("SELECT * FROM shop_item_table ORDER BY shopItemDate ASC")
    List<ShopItem> getAllShopItems();

    //查询未完成的清单
    @Query("SELECT * FROM shop_item_table WHERE shopItemStatus = 0 ORDER BY shopItemDate ASC")
    List<ShopItem> getUndoneItem();

    //查询已经完成的清单
    @Query("SELECT * FROM shop_item_table WHERE shopItemStatus = 1 ORDER BY shopItemDate ASC")
    List<ShopItem> getDoneItem();

    //将清单标记为完成
    @Query("UPDATE shop_item_table SET shopItemStatus = 1 WHERE shopItemId = :shopItemId")
    void markItemAsDone(int shopItemId);

    //将清单标记为未完成
    @Query("UPDATE shop_item_table SET shopItemStatus = 0 WHERE shopItemId = :shopItemId")
    void markItemAsUndone(int shopItemId);

    //查询未完成的number
    @Query("SELECT COUNT(*) FROM shop_item_table WHERE shopItemStatus = 0")
    int selectNumbersUndone();

    //查询完成的number
    @Query("SELECT COUNT(*) FROM shop_item_table WHERE shopItemStatus = 1")
    int selectNumbersDone();

    //查询所有的number
    @Query("SELECT COUNT(*) FROM shop_item_table")
    int selectNumbersAll();

    // 查询数据库中所有的日期
    @Query("SELECT DISTINCT shopItemDate FROM shop_item_table ORDER BY shopItemDate ASC")
    List<String> selectAllDate();

    //清空数据库数据
    @Query("DELETE FROM shop_item_table")
    int deleteAll();
}
