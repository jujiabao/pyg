app.controller("itemController", function ($scope, $location, $http) {
    $scope.num = 1;

    $scope.specificationItems = {};//存储用户选择的规则

    $scope.addNum = function (x) {
        $scope.num += x;
        if ($scope.num < 1) {
            $scope.num = 1;
        }
    }

    $scope.selectSpecification = function (key, value) {
        $scope.specificationItems[key] = value;
        searchSku();
    }

    //判断某规格是否被选中
    $scope.isSelected = function (key, value) {
        if ($scope.specificationItems[key] == value) {
            return true;
        } else {
            return false;
        }
    }

    $scope.sku = {};//当前选择的sku

    //加载默认的SKU
    $scope.loadSku = function () {
        $scope.sku = skuList[0];
        var itemId = $location.search()["itemId"];
        for (var i = 0; i < skuList.length; i++) {
            if (skuList[i]["id"] == itemId) {
                $scope.sku = skuList[i];
                $scope.specificationItems = JSON.parse(JSON.stringify(JSON.parse($scope.sku.spec)));//深克隆
                return;
            }
        }
        $scope.specificationItems = JSON.parse(JSON.stringify(JSON.parse($scope.sku.spec)));//深克隆
    }

    //匹配两个对象是否相等
    matchObject = function (map1, map2) {
        for (var k in map1) {
            if (map1[k] != map2[k]) {
                return false;
            }
        }
        for (var k in map2) {
            if (map2[k] != map1[k]) {
                return false;
            }
        }
        return true;
    }

    //根据选择的查询sku
    searchSku = function () {
        for (var i = 0; i < skuList.length; i++) {
            if (matchObject(JSON.parse(skuList[i].spec), $scope.specificationItems)) {
                $scope.sku = skuList[i];
                return;
            }
        }
    }

    //加入购物车
    $scope.addToCat = function () {
        //跨域请求实现
        $http.get('http://localhost:9107/cart/addGoods2CartList.do?itemId='
            + $scope.sku.id + '&num=' + $scope.num, {'withCredentials': true}).success(
            function (response) {
                if (response.success) {
                    location.href = "http://localhost:9107/cart.html";
                } else {
                    alert("加入购物车失败");
                }
            }
        ).error(function () {
            alert("请求失败")
        });
    }

});