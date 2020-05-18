//首页控制器
app.controller('orderController',function($scope,orderService){

	$scope.status = ["待付款","待付款","待发货","待收货","已收货"];

    $scope.searchEntity={};//定义搜索对象

    //搜索
    $scope.search=function(){
        orderService.search($scope.searchEntity).success(
            function(response){
                $scope.orderList = response;
            }
        );
    }

    $scope.updateEntity={};//定义搜索对象
    //更新
    $scope.update = function(orderId,sta){
        $scope.updateEntity.status = sta;
        $scope.updateEntity.orderId = orderId;
        orderService.update($scope.updateEntity).success(
            function(response){
                $scope.getShopOrderList($scope.stas);
            }
        );
    }
    //更新
    $scope.send = function(orderId,sta){
        $scope.updateEntity.status = sta;
        $scope.updateEntity.orderId = orderId;
        orderService.update($scope.updateEntity).success(
            function(response){
                alert("发货成功");
                $scope.getShopOrderList($scope.stas);
            }
        );
    }
    $scope.stas = "0";

    //用户所有订单列表
	$scope.getShopOrderList = function (status) {
        $scope.stas = status;
		orderService.findOrderList(status).success(
			function (response) {
				$scope.orderList = response;

            }
		);
    }

    $scope.warm = function(){
	    alert("已提醒用户支付");
    }

    $scope.delete = function (orderId,orderItemId,status) {
		orderService.delete(orderId,orderItemId).success(
			function (response) {
                if (response.success){
                    $scope.getUserOrderList(status);
                    alert(response.message);
                } else {
                    alert(response.message);
                }

            }
		)
    }


});