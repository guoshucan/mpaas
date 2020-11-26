angular.module('app').directive('convertString', function() {
	return {
		require: 'ngModel',
		link: function(scope, el, attr, ctrl) {
			ctrl.$parsers.push(function(value) {
				return value.toString();
			});

			ctrl.$formatters.push(function(value) {
				return value;
			});
		}
	}
});