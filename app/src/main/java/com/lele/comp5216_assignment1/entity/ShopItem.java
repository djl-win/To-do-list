package com.lele.comp5216_assignment1.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shop_item_table")
public class ShopItem {

    @PrimaryKey(autoGenerate = true)
    private int shopItemId;
    private String shopItemName;
    private String shopItemDescription;
    private String shopItemDate;  // Format: YYYYMMDD
    private int shopItemStatus = 0;


    public int getShopItemId() {
        return shopItemId;
    }

    public void setShopItemId(int shopItemId) {
        this.shopItemId = shopItemId;
    }

    public String getShopItemName() {
        return shopItemName;
    }

    public void setShopItemName(String shopItemName) {
        this.shopItemName = shopItemName;
    }

    public String getShopItemDescription() {
        return shopItemDescription;
    }

    public void setShopItemDescription(String shopItemDescription) {
        this.shopItemDescription = shopItemDescription;
    }

    public String getShopItemDate() {
        return shopItemDate;
    }

    public void setShopItemDate(String shopItemDate) {
        this.shopItemDate = shopItemDate;
    }

    public int getShopItemStatus() {
        return shopItemStatus;
    }

    public void setShopItemStatus(int shopItemStatus) {
        this.shopItemStatus = shopItemStatus;
    }
}

