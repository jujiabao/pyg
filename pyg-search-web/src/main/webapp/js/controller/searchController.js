app.controller("searchController", function ($scope, searchService, $location) {

    $scope.searchMap = {
        "keywords": "",
        "category": "",
        "brand": "",
        "spec": {},
        "price": "",
        "pageNo": 1,
        "redictPageNo": "",
        "pageSize": 40,
        "sort": "",
        "sortField": ""
    }

    $scope.search = function () {
        $scope.searchMap.pageNo = parseInt($scope.searchMap.pageNo);
        searchService.search($scope.searchMap).success(function (response) {
            $scope.resultMap = response;
            buildPagelabel();//构建分页栏
        })
    }

    buildPagelabel = function () {
        //构建分页栏
        $scope.pageLabel = [];

        var firstPage = 1;//开始页码
        var lastPage = $scope.resultMap.totalPages;//截止页码

        if ($scope.resultMap.totalPages > 5) {
            if ($scope.searchMap.pageNo <= 3) {//显示前5页
                lastPage = 5;
            } else if ($scope.searchMap.pageNo >= $scope.resultMap.totalPages - 2) {
                firstPage = $scope.resultMap.totalPages - 4;//显示后五页
            }  else {//显示中心前后五页
                firstPage = $scope.searchMap.pageNo - 2;
                lastPage = $scope.searchMap.pageNo + 2;
            }
        }

        for (var i = firstPage; i <= lastPage; i++) {
            $scope.pageLabel.push(i);
        }
    }

    $scope.addSearchItem = function (key, value) {
        if (key == "category" || key == "brand" || key == "price") {
            $scope.searchMap[key] = value;
        } else {
            $scope.searchMap.spec[key] = value;
        }
        $scope.search();//查询
    }

    $scope.removeSearchItem = function (key) {
        if (key == "category" || key == "brand" || key == "price") {
            $scope.searchMap[key] = "";
        } else {
            delete $scope.searchMap.spec[key];
        }
        $scope.search();//查询
    }
    
    $scope.queryByPage = function (pageNo) {
        if (pageNo < 1 || pageNo > $scope.resultMap.totalPages) {
            return;
        }
        $scope.searchMap.pageNo = pageNo;
        $scope.search();
    }
    
    $scope.sortSearch = function (sortValue, sortField) {
        $scope.searchMap.sort = sortValue;
        $scope.searchMap.sortField = sortField;
        $scope.search();
    }

    $scope.keywordsIsBrand = function () {
        for (var i = 0; i<$scope.resultMap.brandList.length; i++) {
            if ($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text)>=0) {
                return true;
            }
        }
        return false;
    }
    //加载关键字
    $scope.loadkeywords = function () {
        $scope.searchMap.keywords = $location.search()["keywords"];
        $scope.search();
    }
})