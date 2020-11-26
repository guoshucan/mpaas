app.controller('fb70bdfb-6212-4b0d-8a18-67beb3d39b3f', ['$scope', '$http', '$state', '$rootScope', '$compile', '$modal',
	function($scope, $http, $state, $rootScope, $compile, locals, $modal) {
		//初始信息
		$scope.data = {
			id: null,
			title: null,
			status: false,
			icon: null,
			url: null,
			createTime: null
		};
		$scope.tableContent = true;
		$scope.content = false;
		$scope.deleteAllDisabled = true;
		$scope.selectRequest = {
			key: null,
			startTime: null,
			endTime: null,
			status: "-1",
			start: 1,
			length: "10"
		};
		$scope.isInit = false;
		$scope.selectAll = false; //选择全部
		//分页控件
		$scope.page = {
			data: [],
			count: 0,
			maxSize: 5,
			pages: 0
		};
		$scope.toaster = null;
		$scope.to = {
			type: 'success',
			title: '这是标题',
			text: '这是消息内容'
		};
		$scope.pop = function() {
			$scope.toaster.popInfo($scope.to);
		};
		//初始化
		$scope.init = function() {
			if($scope.isInit) {
				return;
			}
			var sr = localStorage.getItem("fdb124d9-238d-40a4-ad42-2a783a019377|selectRequest");
			if(sr != undefined && sr != null) {
				$scope.selectRequest = JSON.parse(sr);
			}
			$scope.isInit = true;
			$scope.isSelect = true;
			$scope.select();
		};
		//分页位置更改
		$scope.pageChange = function() {
			console.log('pageChange:' + $scope.selectRequest.start);
			$scope.select();
		};
		//行数选择事件
		$scope.selectRowLengthChange = function() {
			$scope.select();
		};
		//全部选中事件
		$scope.checkboxAll = function() {
			if($scope.page.data.length == 0) {
				return;
			}
			if($scope.selectAll) {
				$scope.page.data.forEach(row => {
					row.check = true;
					$scope.deleteAllDisabled = false;
				});
			} else {
				$scope.page.data.forEach(row => {
					row.check = false;
				});
				$scope.deleteAllDisabled = true;
			}
		};
		//复选框选中事件
		$scope.checkboxSelected = function($event, id) {
			let checkbox = $event.target;
			//指定选择判断
			var row;
			for(var i = 0; i < $scope.page.data.length; i++) {
				row = $scope.page.data[i];
				if(row.id == id) {
					row.check = checkbox.checked;
					break;
				}
			}
			//遍历是否有选中
			for(var i = 0; i < $scope.page.data.length; i++) {
				row = $scope.page.data[i];
				if(row.check) {
					$scope.selectAll = true;
					$scope.deleteAllDisabled = false;
					return;
				}
			}
			//没有任何选中，设置全部未选状态
			$scope.deleteAllDisabled = true;
			$scope.selectAll = false;
		}
		//搜索
		$scope.search = function(clean) {
			if(clean) {
				//清理搜索条件
				localStorage.removeItem("fdb124d9-238d-40a4-ad42-2a783a019377|selectRequest");
				$scope.selectRequest.key = "";
				$scope.selectRequest.startTime = null;
				$scope.selectRequest.endTime = null;
				$scope.selectRequest.status = "-1";
				$scope.selectRequest.start = 1;
			}
			localStorage.setItem("fdb124d9-238d-40a4-ad42-2a783a019377|selectRequest", JSON.stringify($scope.selectRequest));
			$scope.select();
		};
		/**
		 * 查看
		 * @param {Object} row
		 */
		$scope.show = function(row) {
			app.get("82ebea43-72ce-4d52-b6e7-4a35be015ef1?id=" + row.id, function(r) {
				$scope.data = r.data.data;
				$scope.tableContent = false;
				$scope.content = true;
			});
		};
		//删除全部选中
		$scope.batchDelete = function() {
			app.batchDelete("33dc8b62-e702-48b7-9ed5-61c65f55943a", app.getPageIds($scope), function(r) {
				$scope.toaster.popInfo({
					type: 'success',
					title: '删除',
					text: '删除完成。'
				});
				$scope.selectAll = false;
				$scope.select();
			});
		};
		//删除
		$scope.delete = function(row) {
			app.delete("d595c6ea-9350-46e5-a0ac-f3372589acb7?id=" + row.id, function(r) {
				$scope.toaster.popInfo({
					type: 'success',
					title: '删除',
					text: '删除 {' + row.title + '} 完成。'
				});
				$scope.select();
			});
		};
		//获取列表
		$scope.isSelect = false;
		$scope.select = function() {
			$scope.content = false;
			$scope.tableContent = true;
			var obj = Object.assign({}, $scope.selectRequest);
			obj.start = obj.start - 1;
			app.select("0915139c-73b9-450f-a930-38f7fbb68de2", obj, function(r) {
				$scope.page.data = r.data.data;
				$scope.page.pages = r.data.pages;
				$scope.page.count = r.data.count;
				//初始化复选状态
				if($scope.isSelect) {
					$scope.isSelect = false;
					if($scope.page.data == null || $scope.page.data.length == 0) {
						var sr = localStorage.getItem("fdb124d9-238d-40a4-ad42-2a783a019377|selectRequest");
						if(sr != undefined && sr != null) {
							$scope.search(true);
							return;
						}
					}
				}
				if($scope.page.data != null) {
					$scope.page.data.forEach(row => {
						row.check = false;
						row.statusCheck = row.status == 1;
					});
				}
			});
		};
		//返回
		$scope.back = function() {
			$scope.content = false;
			$scope.tableContent = true;
		};
	}
]);
//删除确认框
app.controller('f79e3a7b', ['$scope', '$modalInstance', 'rows', function($scope, $modalInstance, rows) {
	$scope.rows = rows;
	$scope.ok = function() {
		console.log("ok");
		$modalInstance.close($scope.rows);
	};
	$scope.cancel = function() {
		console.log("cancel");
		$modalInstance.dismiss('cancel');
	};
}]);
//删除控制器
app.controller('0ebf2e8e-9269-4661-8b34-e8364c0e59d5', ['$scope', '$modal', '$log', function($scope, $modal, $log) {
	$scope.open = function(row) {
		var rows = [];
		rows[0] = row;
		var modalInstance = $modal.open({
			templateUrl: 'd305fb00-49d3-4b0c-9af0-268e9bf0e2be',
			controller: 'f79e3a7b',
			size: 'sm',
			resolve: {
				rows: function() {
					return rows;
				}
			}
		});
		modalInstance.result.then(function(rows) {
			$log.info('选择结果: ', rows);
			if(rows == 'cancel') {
				$log.info('放弃删除:', rows);
				return;
			}
			$log.info('确认删除:', rows);
			$scope.$parent.$parent.delete(rows[0]);
		}, function() {
			$log.info('模式对话框关闭时间:', new Date())
		});
	};
}]);
//多个删除控制器
app.controller('ff6f79f6', ['$scope', '$modal', '$log', function($scope, $modal, $log) {
	$scope.open = function() {
		var d = $scope.$parent.page.data;
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
		var modalInstance = $modal.open({
			templateUrl: 'd305fb00-49d3-4b0c-9af0-268e9bf0e2be',
			controller: 'f79e3a7b',
			size: 'sm',
			resolve: {
				rows: function() {
					return rows;
				}
			}
		});
		modalInstance.result.then(function(rows) {
			$log.info('选择结果: ', rows);
			if(rows == 'cancel') {
				$log.info('放弃删除:', rows);
				return;
			}
			$log.info('确认删除: ', rows);
			$scope.$parent.batchDelete();
		}, function() {
			$log.info('模式对话框关闭时间:', new Date())
		});
	};
}]);
app.controller('02fdff6a', ['$scope', '$filter', function($scope, $filter) {
	// 禁止选择周末
	$scope.disabled = function(date, mode) {
		return false; //(mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
	};
	$scope.open = function($event, id) {
		$event.preventDefault();
		$event.stopPropagation();
		if(id == 'startTime') {
			$('#endTime').next().css('display', 'none');
			$scope.$parent.$broadcast('$f67f8e68-7132-477f-8251-c1ae5db9f068', 'endTime');
		} else {
			$('#startTime').next().css('display', 'none');
			$scope.$parent.$broadcast('$f67f8e68-7132-477f-8251-c1ae5db9f068', 'startTime');
		}
		if($scope.opened) {
			$scope.opened = false;
			console.log("close");
			return;
		}
		$scope.opened = true;
		console.log("open");
	};
	$scope.dateOptions = {
		formatYear: 'yy',
		startingDay: 1,
		class: 'datepicker'
	};
	$scope.opened = false;
	$scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
	$scope.format = $scope.formats[1];
	//监听开始或结束日期控件相互显示处理
	$scope.$on('$f67f8e68-7132-477f-8251-c1ae5db9f068', function(e, data) {
		console.log(data);
		if(data == $scope.name) {
			$scope.opened = false;
		}
	});
	$scope.name;
	//初始化
	$scope.init = function(name) {
		$scope.name = name;
	};
}]);