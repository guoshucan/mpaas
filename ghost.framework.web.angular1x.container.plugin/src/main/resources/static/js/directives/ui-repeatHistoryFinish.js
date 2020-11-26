angular.module('app')
	.directive('repeatHistoryFinish', ['$timeout', function($timeout) {
		return {
			restrict: 'ECMA',
			link: function(scope, element, attrs) {
				if(scope.$last == true) {
					$timeout(function() {
						scope.$emit('repeatHistoryFinish');
					});
				}
			}
		}
	}]);