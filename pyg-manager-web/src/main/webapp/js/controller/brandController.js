app.controller("brandController", function ($scope, $http, $controller, brandService) {
    //继承baseController
    $controller("baseController", {$scope:$scope});

    //查询所有品牌
    $scope.findAll = function () {
        brandService.findAll().success(function (data) {
            $scope.list = data;
        });
    };

    //分页查询品牌信息
    $scope.findPage = function (page, rows) {
        brandService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };
    //添加界面
    $scope.add = function () {
        var object = null;
        if ($scope.entity.id != null) {
            object = brandService.updateOne($scope.entity);
        } else {
            object = brandService.addOne($scope.entity);
        }
        //发送请求
        object.success(function (data) {
            if (data.success) {
                $scope.reloadList();
            } else {
                alert(data.message);
            }
        });
    };
    //修改
    $scope.findOne = function (id) {
        brandService.findOne(id).success(function (data) {
            $scope.entity = data;
        })
    };
    //对象初始化
    $scope.searchEntity = {};
    //条件查询
    $scope.search = function (page, rows) {
        brandService.search(page, rows, $scope.searchEntity).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;//更新总记录数
        })
    };

    $scope.dele = function () {
        brandService.dele($scope.selectIds).success(function (data) {
            if (data.success) {
                $scope.reloadList();
            } else {
                alert(data.message);
            }
        })
    }
})