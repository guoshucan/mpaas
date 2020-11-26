'use strict';
var app = angular.module('myApp', ['ui.bootstrap','ngSanitize'])
.factory('alertService', function($rootScope) {
    var alertService = {};
 
    // 创建一个全局的 alert 数组
    $rootScope.alerts = [];
 
    alertService.add = function(type, msg) {
      $rootScope.alerts.push({'type': type, 'msg': msg, 'close': function(){ alertService.closeAlert(this); }});
    };
 
    alertService.closeAlert = function(alert) {
      alertService.closeAlertIdx($rootScope.alerts.indexOf(alert));
    };
 
    alertService.closeAlertIdx = function(index) {
      $rootScope.alerts.splice(index, 1);
    };
 
    return alertService;
});
app.controller('UserCtrl',['$scope','alertService',
    function($scope,alertService) {
        alertService.add('warning', '这是一个警告消息！');
        alertService.add('error', "这是一个出错消息！");
 
        $scope.editProfile = function() {
            //...do something
            alertService.add('success', '<h4>成功！</h4> 你的个人资料已经修改。');
        };
}]);