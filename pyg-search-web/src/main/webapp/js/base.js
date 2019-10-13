//不带分页通用
var app = angular.module("pyg", []);
//定义过滤器
app.filter("trustHtml", ["$sce", function ($sce) {
    return function (data) {//传入参数是被过滤的内容
        return $sce.trustAsHtml(data);//返回过滤后的内容
    }
}])