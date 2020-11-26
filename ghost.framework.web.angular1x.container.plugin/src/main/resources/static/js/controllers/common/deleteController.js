//删除控制器
angular.module('app').controller('d50d4f45', ['$scope', '$modal', '$log', function($s, $m, $l) {
	$s.open = function(row) {
		var rows = [];
		rows[0] = row;
		var mi = $m.open({
			templateUrl: $s.u,
			controller: 'f79e3a7b',
			size: $s.s,
			resolve: {
				i18n: function() {
					return $s.$parent.$parent.i18n;
				},
				rows: function() {
					return rows;
				}
			}
		});
		mi.result.then(function(rows) {
			$l.info('选择结果: ', rows);
			if(rows == 'cancel') {
				$l.info('放弃删除:', rows);
				return;
			}
			$l.info('确认删除:', rows);
			$s.$parent.$parent.delete(rows[0]);
		}, function() {
			$l.info('模式对话框关闭时间:', new Date())
		});
	};
	$s.s = 'sm';
	$s.u = 'js/controllers/common/deleteTemplate.html';
	$s.init = function(u, s) {
		$s.u = u;
		if(s) {
			$s.s = s;
		}
	};
}]);