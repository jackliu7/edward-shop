package com.edward.service;
import java.util.List;

import com.edward.pojo.TbOrder;
import com.edward.pojo.group.OrderVo;
import com.edward.pojo.group.ShopOrder;
import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface OrderService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<OrderVo> findUserOrder(String username,String status);

	/**
	 * 返回店铺订单
	 * @return
	 */
	public List<ShopOrder> findShopOrder(String sellerId, String status);

	public List<ShopOrder> search(String sellerId,TbOrder order);
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbOrder order);
	
	
	/**
	 * 修改
	 */
	public void update(TbOrder order);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbOrder findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 删除订单明细
	 * @param orderId
	 * @param orderItemId
	 */
	public void delOrderItem(Long orderId,Long orderItemId);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbOrder order, int pageNum, int pageSize);


	
}
