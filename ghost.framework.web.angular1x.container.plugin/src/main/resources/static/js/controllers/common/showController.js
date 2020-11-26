//显示控制器
angular.module('app').controller('93719063', ['$scope', '$modal', '$log', function($s, $m, $l) {
	$s.show = function(row) {
		$s.$parent.get(row, function(data) {
			if(data.status) {
				data.check = data.status == 1;
			}
			var mi = $m.open({
				templateUrl: $s.u,
				controller: 'f79e3a7b',
				size: $s.s,
				resolve: {
					i18n: function() {
						return $s.$parent.$parent.i18n;
					},
					rows: function() {
						return data;
					}
				}
			});
			mi.result.then(function(row) {}, function() {
				$l.info('模式对话框关闭时间:', new Date())
			});
		});
	};
	$s.s = '{width:680px;max-width:1024px;}';
	$s.u;
	$s.init = function(u, s) {
		$s.u = u;
		if(s) {
			$s.s = s;
		}
	};
}]);