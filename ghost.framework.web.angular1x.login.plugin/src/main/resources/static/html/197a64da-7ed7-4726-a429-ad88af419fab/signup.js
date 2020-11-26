'use strict';
app.controller('712e1a2d-37c4-4cdb-ae80-3fd999674a4a', ['$scope', '$http', '$state', function ($scope, $http, $state) {
    $scope.user = {};
    $scope.authError = null;
    $scope.signup = function () {
        $scope.authError = null;
        // Try to create
        $http.post('api/signup', {name: $scope.user.name, email: $scope.user.email, password: $scope.user.password})
            .then(function (response) {
                if (!response.data.user) {
                    $scope.authError = response;
                } else {
                    // $state.go('app.dashboard-v1');
                }
            }, function (x) {
                $scope.authError = 'Server Error';
            });
    };
}]);