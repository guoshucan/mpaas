'use strict';
app.controller('12cb4552-7203-4a0d-bff5-220ec9f4dcc3', ['$scope', '$http', '$state', function($scope, $http, $state) {
	$scope.user = {};
	$scope.toaster = null;
	$scope.to = {
		type: 'success',
		title: '',
		text: ''
	};
	$scope.authError = null;
	$scope.isInitDatabase = function() {
		$scope.authError = null;
		//      $http.post('api/login', {email: $scope.user.email, password: $scope.user.password})
		//      .then(function(response) {
		//       if ( !response.data.user ) {
		//         $scope.authError = '邮箱或密码错误，请重试';
		//       }else{
		//         $state.go('app.dashboard-v1');
		//       }
		//      }, function(x) {
		//       $scope.authError = '服务器错误';
		//      });
		app.get($scope, "a8370638-ae7f-49e5-81f5-1197f1fd5323", function(r) {
			$scope.toaster.popInfo({
				type: 'info',
				title: $scope.i18n.edit.name,
				text: $scope.i18n.edit.name + ' {' + row.groupName + '} ' + $scope.i18n.statusComplete
			});
			$scope.select();
		});
		$state.go('app.dashboard-v1');
	};
}]);