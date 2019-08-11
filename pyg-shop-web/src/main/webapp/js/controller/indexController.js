app.controller("indexController", function ($scope, loginService) {
    //显示用户名
    $scope.showLoginName = function () {
        loginService.loginName().success(function (response) {
            $scope.loginName = response.loginName;
        });
    }
});