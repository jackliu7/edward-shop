package com.edward.service.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.edward.mapper.TbItemMapper;
import com.edward.mapper.TbOrderItemMapper;
import com.edward.mapper.TbOrderMapper;
import com.edward.pojo.*;
import com.edward.pojo.group.Cart;
import com.edward.pojo.group.OrderVo;
import com.edward.pojo.group.ShopOrder;
import com.edward.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import entity.PageResult;
import util.IdWorker;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;

	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<OrderVo> findUserOrder(String username,String status) {
		List<OrderVo> result = new ArrayList<OrderVo>();
		TbOrderExample example=new TbOrderExample();
		TbOrderExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(username);
		if (!"0".equals(status)){
			criteria.andStatusEqualTo(status);
		}
		List<TbOrder> orderList = orderMapper.selectByExample(example);//用户所有的订单
		//根据订单查询每个订单明细
		for (TbOrder order : orderList){
			OrderVo orderVo = new OrderVo();
			orderVo.setOrder(order);
			TbOrderItemExample tbOrderItemExample = new TbOrderItemExample();
			TbOrderItemExample.Criteria itemExampleCriteria = tbOrderItemExample.createCriteria();
			itemExampleCriteria.andOrderIdEqualTo(order.getOrderId());
			List<TbOrderItem> orderItemList = orderItemMapper.selectByExample(tbOrderItemExample);
			orderVo.setOrderItemList(orderItemList);
			result.add(orderVo);
		}
		return result;
	}

	/**
	 * 查询店铺订单
	 * @param sellerId
	 * @param status
	 * @return
	 */
	@Override
	public List<ShopOrder> findShopOrder(String sellerId, String status) {
		List<ShopOrder> result = new ArrayList<ShopOrder>();
		TbOrderExample example=new TbOrderExample();
		TbOrderExample.Criteria criteria = example.createCriteria();
		criteria.andSellerIdEqualTo(sellerId);
		if (!"0".equals(status)){
			criteria.andStatusEqualTo(status);
		}
		List<TbOrder> orderList = orderMapper.selectByExample(example);//店铺所有的订单
		//根据订单查询每个订单明细
		for (TbOrder order : orderList){
			TbOrderItemExample tbOrderItemExample = new TbOrderItemExample();
			TbOrderItemExample.Criteria itemExampleCriteria = tbOrderItemExample.createCriteria();
			itemExampleCriteria.andOrderIdEqualTo(order.getOrderId());
			List<TbOrderItem> orderItemList = orderItemMapper.selectByExample(tbOrderItemExample);
			for (TbOrderItem orderItem : orderItemList){
				TbOrder order1 = new TbOrder();
				order1.setOrderId(order.getOrderId());
				order1.setReceiver(order.getReceiver());
				order1.setReceiverAreaName(order.getReceiverAreaName());
				order1.setReceiverMobile(order.getReceiverMobile());
				order1.setStatus(order.getStatus());
				ShopOrder shopOrder = new ShopOrder();
				shopOrder.setOrder(order1);
				shopOrder.setOrderItem(orderItem);
				result.add(shopOrder);
			}
		}
		return result;
	}

	@Override
	public List<ShopOrder> search(String sellerId,TbOrder order) {
		List<ShopOrder> result = new ArrayList<ShopOrder>();
		TbOrderExample example=new TbOrderExample();
		TbOrderExample.Criteria criteria = example.createCriteria();
		criteria.andSellerIdEqualTo(sellerId);
		if (order != null && order.getOrderId()!=null){
			criteria.andOrderIdEqualTo(order.getOrderId());
		}
		List<TbOrder> orderList = orderMapper.selectByExample(example);//店铺所有的订单
		//根据订单查询每个订单明细
		for (TbOrder order1 : orderList){
			TbOrderItemExample tbOrderItemExample = new TbOrderItemExample();
			TbOrderItemExample.Criteria itemExampleCriteria = tbOrderItemExample.createCriteria();
			itemExampleCriteria.andOrderIdEqualTo(order1.getOrderId());
			List<TbOrderItem> orderItemList = orderItemMapper.selectByExample(tbOrderItemExample);
			for (TbOrderItem orderItem : orderItemList){
				ShopOrder shopOrder = new ShopOrder();
				shopOrder.setOrder(order1);
				shopOrder.setOrderItem(orderItem);
				result.add(shopOrder);
			}
		}
		return result;
	}


	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbOrder> page=   (Page<TbOrder>) orderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private IdWorker idWorker;


	

	
	/**
	 * 增加
	 */
	@Override
	public void add(TbOrder order) {
		
		//1.从redis中提取购物车列表
		List<Cart> cartList= (List<Cart>) redisTemplate.boundHashOps("cartList").get(order.getUserId());
				
		//2.循环购物车列表添加订单
		for(Cart  cart:cartList){
			TbOrder tbOrder=new TbOrder();
			long orderId = idWorker.nextId();	//获取ID		
			tbOrder.setOrderId(orderId);
			tbOrder.setPaymentType(order.getPaymentType());//支付类型
			tbOrder.setStatus("1");//未付款 
			tbOrder.setCreateTime(new Date());//下单时间
			tbOrder.setUpdateTime(new Date());//更新时间
			tbOrder.setUserId(order.getUserId());//当前用户
			tbOrder.setReceiverAreaName(order.getReceiverAreaName());//收货人地址
			tbOrder.setReceiverMobile(order.getReceiverMobile());//收货人电话
			tbOrder.setReceiver(order.getReceiver());//收货人
			tbOrder.setSourceType(order.getSourceType());//订单来源
			tbOrder.setSellerId(cart.getSellerId());//商家ID
			
			double money=0;//合计数
			//循环购物车中每条明细记录
			for(TbOrderItem orderItem:cart.getOrderItemList()  ){

				orderItem.setId(idWorker.nextId());//主键
				orderItem.setOrderId(orderId);//订单编号
				orderItem.setSellerId(cart.getSellerId());//商家ID
				orderItemMapper.insert(orderItem);
				money+=orderItem.getTotalFee().doubleValue();
			}
			
			tbOrder.setPayment(new BigDecimal(money));//合计
			
			orderMapper.insert(tbOrder);
		}
		
		//3.清除redis中的购物车
		redisTemplate.boundHashOps("cartList").delete(order.getUserId());
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbOrder order){
		TbOrder order1 = orderMapper.selectByPrimaryKey(order.getOrderId());
		order1.setStatus(order.getStatus());
		orderMapper.updateByPrimaryKey(order1);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbOrder findOne(Long id){
		return orderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			orderMapper.deleteByPrimaryKey(id);
		}		
	}

	/**
	 * 删除订单明细
	 * @param orderId
	 * @param orderItemId
	 */
	@Override
	public void delOrderItem(Long orderId,Long orderItemId){
		orderItemMapper.deleteByPrimaryKey(orderItemId);
		TbOrderItemExample example=new TbOrderItemExample();
		TbOrderItemExample.Criteria criteria = example.createCriteria();
		criteria.andOrderIdEqualTo(orderId);
		List<TbOrderItem> orderItems = orderItemMapper.selectByExample(example);
		if (orderItems == null || orderItems.isEmpty()){
			orderMapper.deleteByPrimaryKey(orderId);
		}

	}
	
	
	@Override
	public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbOrderExample example=new TbOrderExample();
		TbOrderExample.Criteria criteria = example.createCriteria();
		
		if(order!=null){			
						if(order.getPaymentType()!=null && order.getPaymentType().length()>0){
				criteria.andPaymentTypeLike("%"+order.getPaymentType()+"%");
			}
			if(order.getPostFee()!=null && order.getPostFee().length()>0){
				criteria.andPostFeeLike("%"+order.getPostFee()+"%");
			}
			if(order.getStatus()!=null && order.getStatus().length()>0){
				criteria.andStatusLike("%"+order.getStatus()+"%");
			}
			if(order.getShippingName()!=null && order.getShippingName().length()>0){
				criteria.andShippingNameLike("%"+order.getShippingName()+"%");
			}
			if(order.getShippingCode()!=null && order.getShippingCode().length()>0){
				criteria.andShippingCodeLike("%"+order.getShippingCode()+"%");
			}
			if(order.getUserId()!=null && order.getUserId().length()>0){
				criteria.andUserIdLike("%"+order.getUserId()+"%");
			}
			if(order.getBuyerMessage()!=null && order.getBuyerMessage().length()>0){
				criteria.andBuyerMessageLike("%"+order.getBuyerMessage()+"%");
			}
			if(order.getBuyerNick()!=null && order.getBuyerNick().length()>0){
				criteria.andBuyerNickLike("%"+order.getBuyerNick()+"%");
			}
			if(order.getBuyerRate()!=null && order.getBuyerRate().length()>0){
				criteria.andBuyerRateLike("%"+order.getBuyerRate()+"%");
			}
			if(order.getReceiverAreaName()!=null && order.getReceiverAreaName().length()>0){
				criteria.andReceiverAreaNameLike("%"+order.getReceiverAreaName()+"%");
			}
			if(order.getReceiverMobile()!=null && order.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+order.getReceiverMobile()+"%");
			}
			if(order.getReceiverZipCode()!=null && order.getReceiverZipCode().length()>0){
				criteria.andReceiverZipCodeLike("%"+order.getReceiverZipCode()+"%");
			}
			if(order.getReceiver()!=null && order.getReceiver().length()>0){
				criteria.andReceiverLike("%"+order.getReceiver()+"%");
			}
			if(order.getInvoiceType()!=null && order.getInvoiceType().length()>0){
				criteria.andInvoiceTypeLike("%"+order.getInvoiceType()+"%");
			}
			if(order.getSourceType()!=null && order.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+order.getSourceType()+"%");
			}
			if(order.getSellerId()!=null && order.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+order.getSellerId()+"%");
			}
	
		}
		
		Page<TbOrder> page= (Page<TbOrder>)orderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
