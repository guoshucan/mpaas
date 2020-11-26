angular.module('app').directive('convertNumber', function() {
	return {
		require: 'ngModel',
		link: function(scope, el, attr, ctrl) {
			ctrl.$parsers.push(function(value) {
				return parseInt(value, 10);
			});

			ctrl.$formatters.push(function(value) {
				return value.toString();
			});
		}
	}
});