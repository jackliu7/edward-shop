app.controller("contentController",function($scope,contentService,loginService){
	$scope.contentList = [];
	// 根据分类ID查询广告的方法:
	$scope.findByCategoryId = function(categoryId){
		contentService.findByCategoryId(categoryId).success(function(response){
			$scope.contentList[categoryId] = response;
		});
	}
	
	//搜索  （传递参数）
	$scope.search=function(){
		location.href="http://localhost:9103/search.html#?keywords="+$scope.keywords;
	}

    $scope.loginName = "";

    $scope.showName=function(){
        loginService.showName().success(
            function(response){
                $scope.loginName=response.loginName;
            }
        );
    }
	
});