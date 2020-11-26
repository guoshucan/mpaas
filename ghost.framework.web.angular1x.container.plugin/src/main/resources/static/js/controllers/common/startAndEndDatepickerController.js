//开始与结束datepicker控制器
angular.module('app').controller('02fdff6a', ['$scope', '$filter', function($s, $f) {
	// 禁止选择周末
	$s.disabled = function(date, mode) {
		return false; //(mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
	};
	$s.open = function($event, id) {
		$event.preventDefault();
		$event.stopPropagation();
		if(id == $s.s) {
			$('#' + $s.e).next().css('display', 'none');
			$s.$parent.$broadcast('$f67f8e68-7132-477f-8251-c1ae5db9f068', $s.e);
		} else {
			$('#' + $s.s).next().css('display', 'none');
			$s.$parent.$broadcast('$f67f8e68-7132-477f-8251-c1ae5db9f068', $s.s);
		}
		if($s.opened) {
			$s.opened = false;
			console.log("close");
			return;
		}
		$s.opened = true;
		console.log("open");
	};
	$s.dateOptions = {
		formatYear: 'yy',
		startingDay: 1,
		class: 'datepicker'
	};
	$s.opened = false;
	$s.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
	$s.format = $s.formats[1];
	//监听开始或结束日期控件相互显示处理
	$s.$on('$f67f8e68-7132-477f-8251-c1ae5db9f068', function(e, data) {
		console.log(data);
		if(data == $s.name) {
			$s.opened = false;
		}
	});
	$s.s = 'startTime';
	$s.e = 'endTime';
	/**
	 * 初始化
	 * @param {String} s 开始日期名称
	 * @param {String} e 结束日期名称
	 */
	$s.init = function(s, e) {
		$s.s = s;
		$s.e = e;
	};
}]);