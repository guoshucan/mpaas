//模态确认控制器
angular.module('app').controller('f79e3a7b', ['$scope', '$modalInstance', 'rows', 'i18n', function($s, $mi, rows, i18n) {
	$s.i18n = i18n;
	$s.rows = rows;
	$s.ok = function() {
		console.log("ok");
		$mi.close($s.rows);
	};
	$s.cancel = function() {
		console.log("cancel");
		$mi.dismiss('cancel');
	};
}]);