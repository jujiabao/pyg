 //控制层 
app.controller('goodsController' ,function($scope,$controller, $location, goodsService, uploadService, itemCatService, typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){

		var id = $location.search()["id"];
		if (id == null) {
			return;
		}
		goodsService.findOne(id).success(
			function(response){
				console.log(JSON.stringify(response))
				$scope.entity= response;
				editor.html($scope.entity.goodsDesc.introduction);//商品介绍
				$scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);//图片列表
				$scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems)//商品扩展属性
				$scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);
				//转换SKU对象
				for (var i=0; i<$scope.entity.itemList.length; i++) {
					$scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
				}
			}
		);
	}
	
	//保存 
	$scope.save=function(){
		$scope.entity.goodsDesc.introduction = editor.html();
		var serviceObject;//服务层对象  				
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加
		}				
		serviceObject.success(
			function(response){
				if(response.success){
		        	alert("保存成功");
					$scope.entity={goods:{},goodsDesc:{itemImages:[], specificationItems:[]}};//重新定义结构，不能{}
					editor.html("");
					location.href = "goods.html";
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){
		//获取选中的复选框
		console.log( $scope.selectIds)
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}
			}
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	$scope.uploadFile = function () {
		uploadService.uploadFile().success(function (response) {
			if (response.success) {
				$scope.image_entity.url = response.message;
			} else {
				alert(response.message)
			}
		})
	}

	//定义页面实体结构
	$scope.entity={goods:{},goodsDesc:{itemImages:[], specificationItems:[]}};
	//添加图片列表
	$scope.add_image_entity=function(){
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity);
	}

	//移除图片
	$scope.remove_image_entity = function (index) {
		$scope.entity.goodsDesc.itemImages.splice(index, 1);
	}

	//查询一级商品分类列表
	$scope.selectItemCat1List = function () {
		itemCatService.findByParentId(0).success(function (response) {
			$scope.itemCat1List = response;
		});
	}

	//查询二级商品分类列表
	$scope.$watch('entity.goods.category1Id', function (newValue, oldValue) {
		if (newValue != undefined) {
			itemCatService.findByParentId(newValue).success(function (response) {
				$scope.itemCat2List = response;
			});
		}
	});

	//查询三级商品分类列表
	$scope.$watch('entity.goods.category2Id', function (newValue, oldValue) {
		if (newValue != undefined) {
			itemCatService.findByParentId(newValue).success(function (response) {
				$scope.itemCat3List = response;
			});
		}
	});
	//显示模板ID
	$scope.$watch('entity.goods.category3Id', function (newValue, oldValue) {
		if (newValue != undefined) {
			itemCatService.findOne(newValue).success(function (response) {
				$scope.entity.goods.typeTemplateId = response.typeId;
			});
		}
	});

	//加载品牌列表
	$scope.$watch('entity.goods.typeTemplateId', function (newValue, oldValue) {
		if (newValue != undefined) {
			typeTemplateService.findOne(newValue).success(function (response) {
				$scope.typeTemplate = response;
				$scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);//品牌列表转换
				if ($location.search()["id"] == null) {
					$scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);//扩展属性
				}
			});
			//加载规格列表
			typeTemplateService.findSpecList(newValue).success(function (response) {
				$scope.specList = response;
			});
		}
	});

	//规格复选框
	$scope.updateSpecAttribute = function ($event, name, value) {
		var obj = $scope.searchObjectKey($scope.entity.goodsDesc.specificationItems, "attributeName", name);
		if (obj != null) {
			if ($event.target.checked) {
				//勾选
				obj.attributeValue.push(value);
			} else {
				//取消勾选
				var index = obj.attributeValue.indexOf(value);
				obj.attributeValue.splice(index, 1);
				if (obj.attributeValue.length == 0) {
					//全部取出
					var index = $scope.entity.goodsDesc.specificationItems.indexOf(obj);
					$scope.entity.goodsDesc.specificationItems.splice(index, 1);
				}
			}

		}  else {
			$scope.entity.goodsDesc.specificationItems.push({"attributeName":name, "attributeValue":[value]});
		}

	}
	
	//创建SKU列表
	$scope.createItemList = function () {
		$scope.entity.itemList = [{spec:{}, price:0, num:99999, status:"0", isDefault:"0"}];
		var items = $scope.entity.goodsDesc.specificationItems;
		for (var i=0; i<items.length; i++) {
			$scope.entity.itemList = addColumn($scope.entity.itemList, items[i].attributeName, items[i].attributeValue);
		} 
	}

	addColumn = function (list, columnName, columnValues) {
		var newList = [];
		for (var i=0; i<list.length; i++) {
			var oldRow = list[i];
			for (var j=0; j<columnValues.length; j++) {
				var newRow = JSON.parse(JSON.stringify(oldRow));
				newRow.spec[columnName] = columnValues[j];
				newList.push(newRow);
			}
		}
		return newList;
	}

	$scope.status = ["未审核", "已审核", "审核未通过", "已关闭"];

	//查询商品分类列表
	$scope.itemCatList = [];//初始化
	$scope.findItemCatService = function () {
		itemCatService.findAll().success(function (response) {
			for (var i=0; i<response.length; i++) {
				$scope.itemCatList[response[i].id] = response[i].name;
			}
		})
	}

	//检查是否勾选
	$scope.checkAttributeValue = function (specName, optionName) {
		var object = $scope.searchObjectKey($scope.entity.goodsDesc.specificationItems, "attributeName", specName);
		if (object != null) {
			if (object.attributeValue.indexOf(optionName) >= 0)  {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
});	
