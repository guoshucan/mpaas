app.controller('ToasterCtrl', ['$scope', 'toaster', function($scope, toaster) {
    $scope.toaster = {
        type: 'success',
        title: '这是标题',
        text: '这是消息内容'
    };
    $scope.pop = function(){
        toaster.pop($scope.toaster.type, $scope.toaster.title, $scope.toaster.text);
    };
    $scope.popInfo = function(to){
        toaster.pop(to.type, to.title, to.text);
    };
    $scope.init = function(){
    	$scope.$parent.toaster = $scope;
//  	toaster.pop($scope.toaster.type, $scope.toaster.title, $scope.toaster.text);
    };
}]);