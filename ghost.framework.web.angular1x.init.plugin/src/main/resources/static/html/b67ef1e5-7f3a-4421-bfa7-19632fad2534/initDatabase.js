'use strict';
app.controller('12cb4552-7203-4a0d-bff5-220ec9f4dcc3', ['$scope', '$http', '$state', function ($scope, $http, $state) {
    $scope.user = {};
    $scope.authError = null;
    $scope.login = function () {
        $scope.authError = null;
        $http.post('api/login', {email: $scope.user.email, password: $scope.user.password})
        .then(function(response) {
         if ( !response.data.user ) {
           $scope.authError = '邮箱或密码错误，请重试';
         }else{
           $state.go('app.dashboard-v1');
         }
        }, function(x) {
         $scope.authError = '服务器错误';
        });
        $state.go('app.dashboard-v1');
    };
}]);