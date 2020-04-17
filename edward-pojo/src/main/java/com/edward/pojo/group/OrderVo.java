package com.edward.pojo.group;

import com.edward.pojo.TbOrder;
import com.edward.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;

public class OrderVo implements Serializable {
    private TbOrder order;
    private List<TbOrderItem> orderItemList;

    public TbOrder getOrder() {
        return order;
    }

    public void setOrder(TbOrder order) {
        this.order = order;
    }

    public List<TbOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<TbOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
