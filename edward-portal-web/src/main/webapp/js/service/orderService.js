//订单服务层
app.service('orderService',function($http){
	//用户所有订单列表
	this.findOrderList=function(status){
		return $http.get('order/findAll.do?status='+status);
	}
	//删除订单Long orderId,Long orderItemId
	this.delete=function(orderId,orderItemId){
		return $http.get('order/delOrderItem.do?orderId='+orderId+'&orderItemId='+orderItemId);
	}
    //修改
    this.update=function(entity){
        return  $http.post('order/update.do',entity );
    }




});