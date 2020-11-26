//批量删除控制器
angular.module('app').controller('944fee3a', ['$scope', '$modal', '$log', function($s, $m, $l) {
	$s.open = function() {
		var d = $s.$parent.page.data;
		var rows = [];
		//遍历是否有选中
		var row;
		for(var i = 0; i < d.length; i++) {
			row = d[i];
			if(row.check) {
				rows[rows.length] = row;
			}
		}
		if(rows.length == 0) {
			return;
		}
		var mi = $m.open({
			templateUrl: $s.u,
			controller: 'f79e3a7b',
			size: $s.s,
			resolve: {
				rows: function() {
					return rows;
				},
				i18n: function() {
					return $s.$parent.i18n;
				}
			}
		});
		mi.result.then(function(rows) {
			$l.info('选择结果: ', rows);
			if(rows == 'cancel') {
				$l.info('放弃删除:', rows);
				return;
			}
			$l.info('确认删除: ', rows);
			$s.$parent.batchDelete();
		}, function() {
			$l.info('模式对话框关闭时间:', new Date())
		});
	};
	$s.s = 'sm';
	$s.u= 'js/controllers/common/deleteTemplate.html';
	$s.init = function(u, s) {
		$s.u= u;
		if(s) {
			$s.s = s;
		}
	};
}]);