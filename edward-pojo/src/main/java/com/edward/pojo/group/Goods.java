package com.edward.pojo.group;

import com.edward.pojo.TbGoods;
import com.edward.pojo.TbGoodsDesc;
import com.edward.pojo.TbItem;

import java.io.Serializable;
import java.util.List;


/**
 * 商品的组合实体类
 * @author jt
 *
 */

public class Goods implements Serializable{
	
	private TbGoods goods; // 商品信息
	private TbGoodsDesc goodsDesc; // 商品扩展信息
	
	private List<TbItem> itemList; // SKU的列表信息
	public TbGoods getGoods() {
		return goods;
	}
	public void setGoods(TbGoods goods) {
		this.goods = goods;
	}
	public TbGoodsDesc getGoodsDesc() {
		return goodsDesc;
	}
	public void setGoodsDesc(TbGoodsDesc goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	public List<TbItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<TbItem> itemList) {
		this.itemList = itemList;
	}

	
}
