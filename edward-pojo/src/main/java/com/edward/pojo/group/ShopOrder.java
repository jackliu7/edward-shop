package com.edward.pojo.group;

import com.edward.pojo.TbOrder;
import com.edward.pojo.TbOrderItem;

import java.io.Serializable;

public class ShopOrder implements Serializable {
    private TbOrder order;
    private TbOrderItem orderItem;

    public TbOrder getOrder() {
        return order;
    }

    public void setOrder(TbOrder order) {
        this.order = order;
    }

    public TbOrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(TbOrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
