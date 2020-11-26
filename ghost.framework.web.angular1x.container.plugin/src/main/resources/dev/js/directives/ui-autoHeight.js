angular.module('app')
	.directive('autoHeight', function() {
		function autoHeight(elem) {
			elem.style.height = 'auto';
			elem.scrollTop = 0; //防抖动
			elem.style.height = elem.scrollHeight + 'px';
		}
		return {
			scope: {},
			link: function(scope, ele, attrs) {
				var oriEle = $(ele).get(0);
				$(oriEle).focus();
				$(oriEle).bind('keyup click', function(e) {
					autoHeight($(this).get(0));
				});
				var timer = setInterval(function() {
					if($(oriEle).val()) {
						autoHeight(oriEle);
						clearInterval(timer);
					}
				}, 100);
			}
		};
	});