//收获地址服务层
app.service('addressService',function($http){
    //查询实体
    this.findOne=function(id){
        return $http.get('../address/findOne.do?id='+id);
    }
    //增加
    this.add=function(entity){
        return  $http.post('../address/add.do?smscode=',entity );
    }
    //修改
    this.update=function(entity){
        return  $http.post('../address/update.do',entity );
    }
    //删除
    this.delete=function(ids){
        return $http.get('../address/delete.do?ids='+ids);
    }
	
	
});