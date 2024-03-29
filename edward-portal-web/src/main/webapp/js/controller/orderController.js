//首页控制器
app.controller('orderController',function($scope,loginService,orderService){
	//显示登录用户
	$scope.showName=function(){
			loginService.showName().success(
					function(response){
						$scope.loginName=response.loginName;
					}
			);
	}

	$scope.status = ["待付款","待付款","待发货","待收货","已收货"];

    //用户所有订单列表
	$scope.getUserOrderList = function (status) {
		orderService.findOrderList(status).success(
			function (response) {
				$scope.orderList = response;

            }
		);
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

    $scope.updateEntity = {};
    //更新
    $scope.update = function(orderId,sta){
        $scope.updateEntity.status = sta;
        $scope.updateEntity.orderId = orderId;
        orderService.update($scope.updateEntity).success(
            function(response){
                alert("收货成功")
               location.reload();
            }
        );
    }

    //更新
    $scope.pay = function(orderId,sta){
        $scope.updateEntity.status = sta;
        $scope.updateEntity.orderId = orderId;
        orderService.update($scope.updateEntity).success(
            function(response){
                alert("付款成功");
                location.reload();
            }
        );
    }

    $scope.warm = function () {
	    alert("已提醒商家发货");
    }


});