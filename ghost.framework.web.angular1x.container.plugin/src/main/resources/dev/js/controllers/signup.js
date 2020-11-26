//'use strict';

// signup controller
app.controller('SignupFormController', ['$scope', '$http', '$state', '$rootScope', '$translate', function($scope, $http, $state, $rootScope, $translate) {
	$scope.i18n = {
		name: "注册",
		error: "错误！",
		complete: "完成",
		repeat: "重复！"
	};
	$scope.user = {};
	$scope.user.adminUser = '';
	$scope.user.adminName = '';
	$scope.user.mobilePhone = '';
	$scope.user.password = '';
	$scope.user.email = '';
	//初始化区域
	$scope.initLocale = function() {
		app.getJsonObject("a87b955f/i18n/signup/" + $translate.use() + ".json", function(r) {
			if(r) {
				$scope.i18n = r;
			}
		}, function(r) {});
	};
	$scope.authError = null;
	$scope.init = function() {
		$scope.initLocale();
		//区域变动监听
		$rootScope.$on('$translateChangeSuccess', function() {
			console.log($translate.use());
			$scope.initLocale();
		})
	};
	$scope.signup = function() {
		$scope.authError = null;
		// Try to create
		app.add($scope, "4949f374-74c0-4dc4-b0df-1eb10564d86d", $scope.user,
			function(r) {
				//完成跳转
				$state.go('app.dashboard-v1');
			},
			function(r) {
				//错误
				if(r.data && r.data.code == 1) {
					$scope.authError = r.data.message;
				} else {
					$scope.authError = 'Server Error';
				}
			});
	};
}]);