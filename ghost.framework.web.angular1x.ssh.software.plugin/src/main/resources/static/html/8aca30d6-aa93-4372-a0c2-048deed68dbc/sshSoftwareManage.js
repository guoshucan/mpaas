app.controller('0fccd9a1-7669-44b6-b661-8b89900d2124', ['$scope', '$http', '$state', '$rootScope', '$compile', '$translate',
	function($scope, $http, $state, $rootScope, $compile, $translate) {
		$scope.i18n = {
			error: "错误！",
			complete: "完成",
			disable: "禁用",
			enable: "启用",
			startTime: "开始日期",
			endTime: "结束日期",
			close: "关闭",
			clear: "清除",
			currentDate: "今天",
			name: "名称",
			status: "状态",
			date: "日期",
			open: "操作",
			submit: "提交",
			back: "返回",
			repeat: "重复！",
			statusComplete: "状态完成。",
			group: {
				name: "管理组名称：",
				placeholder: "请填写管理组名称..."
			},
			add: {
				name: "新增",
				group: "添加管理组",
				status: "是否启用："
			},
			edit: {
				name: "修改",
				group: "修改管理组"
			},
			delete: {
				confirmDeletion: "确认删除！",
				chooseIs: "选择的是:",
				cancel: "取消",
				confirm: "确认",
				selected: "删除所选",
				name: "删除",
				complete: "删除完成",
				error: "删除错误！"
			},
			content: {
				systemManagement: "系统管理",
				groupManagement: "管理组管理",
				managementGroup: "管理组"
			},
			search: {
				pleaseSelectStatus: "请选择状态",
				key: "请输入关键字...",
				name: "搜索"
			},
			page: {
				perPage: "每页",
				row: "行",
				show: "显示",
				article: "条",
				total: "总",
				previous: "上一页",
				next: "下一页",
				first: "第一页",
				last: "尾页"
			}
		};
		//初始信息
		$scope.data = {
			name: "",
			status: false,
			description: ""
		};
		$scope.tableContent = true;
		$scope.addContent = false;
		$scope.editContent = false;
		$scope.deleteAllDisabled = true;
		$scope.selectRequest = {
			key: null,
			startTime: null,
			endTime: null,
			status: "-1",
			start: 1,
			length: "10"
		};
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
		//初始化区域
		$scope.initLocale = function() {
			app.getJsonObject("a87b955f/i18n/sshGroupManage/" + $translate.use() + ".json", function(r) {
				if(r) {
					$scope.i18n = r;
				}
				$scope.isSelect = true;
				$scope.select();
			}, function(r) {
				$scope.isSelect = true;
				$scope.select();
			});
		};
		//初始化
		$scope.init = function() {
			var sr = localStorage.getItem("df8bbd2f-47fc-4753-a32d-96a969a0d2a3");
			if(sr != undefined && sr != null) {
				$scope.selectRequest = JSON.parse(sr);
			}
			$scope.initLocale();
			//区域变动监听
			$rootScope.$on('$translateChangeSuccess', function() {
				console.log($translate.use());
				$scope.initLocale();
			})
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
				localStorage.removeItem("df8bbd2f-47fc-4753-a32d-96a969a0d2a3");
				$scope.selectRequest.key = "";
				$scope.selectRequest.startTime = null;
				$scope.selectRequest.endTime = null;
				$scope.selectRequest.status = "-1";
				$scope.selectRequest.start = 1;
			}
			localStorage.setItem("df8bbd2f-47fc-4753-a32d-96a969a0d2a3", JSON.stringify($scope.selectRequest));
			$scope.select();
		};
		//更新状态
		$scope.statusUpdate = function(row) {
			app.get($scope, "72089e92-54f1-470d-a9bd-a86cd463974d?id=" + row.id + "&status=" + (row.status == 1 ? 0 : 1), function(r) {
				if(r.data.code == 0 && r.data.message == "success") {
					$scope.toaster.popInfo({
						type: 'info',
						title: '修改',
						text: '修改 {' + row.name + '} 状态完成。'
					});
					$scope.select();
				} else {
					$scope.toaster.popInfo({
						type: 'error',
						title: '修改',
						text: '修改 {' + row.name + '} 状态错误！'
					});
				}
			});
		};
		//删除全部选中
		$scope.batchDelete = function() {
			app.batchDelete($scope, "e39cd079-1c65-4bb3-b0d9-fce15de6742b", app.getPageIds($scope), function(r) {
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
			app.get($scope, "fa0633c2-f525-4945-b4f0-2608c78169ee?id=" + row.id, function(r) {
				$scope.toaster.popInfo({
					type: 'success',
					title: '删除',
					text: '删除 {' + row.name + '} 完成。'
				});
				$scope.select();
			});
		};
		//获取列表
		$scope.isSelect = false;
		$scope.select = function() {
			$scope.editContent = false;
			$scope.addContent = false;
			$scope.tableContent = true;
			var obj = Object.assign({}, $scope.selectRequest);
			obj.start = obj.start - 1;
			app.select($scope, "5d110f4d-c366-4804-b675-f61fe45cb356", obj, function(r) {
				$scope.page.data = r.data.data;
				$scope.page.pages = r.data.pages;
				$scope.page.count = r.data.count;
				//初始化复选状态
				if($scope.isSelect) {
					$scope.isSelect = false;
					if($scope.page.data == null || $scope.page.data.length == 0) {
						var sr = localStorage.getItem("df8bbd2f-47fc-4753-a32d-96a969a0d2a3");
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
		//修改
		$scope.edit = function(row) {
			if(row == null) {
				var o = new Object();
				o.status = $scope.data.status ? 1 : 0;
				o.id = $scope.data.id;
				o.name = $scope.data.name;
				o.description = $scope.data.description;
				app.edit($scope, "cf97af68-9feb-4585-9631-7f83a60bbaf8", o, function(r) {
					$scope.toaster.popInfo({
						type: 'info',
						title: '修改',
						text: '修改 {' + o.name + '} 完成。'
					});
					$scope.select();
				}, function(r) {
					if(r.data && r.data.code == 1) {
						$scope.toaster.popInfo({
							type: 'warning',
							title: '修改',
							text: '修改 {' + o.name + '} 重复！'
						});
					} else {
						$scope.toaster.popInfo({
							type: 'error',
							title: '修改',
							text: '修改 {' + o.name + '} 错误！'
						});
					}
				});
			} else {
				//初始化修改参数
				$scope.tableContent = false;
				$scope.editContent = true;
				$scope.data = row;
				$scope.data.status = row.status == 1;
			}
		};
		//添加
		$scope.add = function() {
			if(!$scope.addContent) {
				$scope.data.name = "";
				$scope.data.status = false;
				$scope.data.description = "";
				$scope.tableContent = false;
				$scope.addContent = true;
				return;
			}
			app.add($scope, "77a9f1ac-e836-449a-a8b6-4a565b52f4d4", {
				name: $scope.data.name,
				description: $scope.data.description,
				status: ($scope.data.status ? 1 : 0),
			}, function(r) {
				$scope.toaster.popInfo({
					type: 'success',
					title: '添加',
					text: '添加 {' + $scope.data.name + '} 完成。'
				});
				$scope.select();
			}, function(r) {
				if(r.data && r.data.code == 1) {
					$scope.toaster.popInfo({
						type: 'warning',
						title: '添加',
						text: '添加 {' + $scope.data.name + '} 重复！'
					});
				} else {
					$scope.toaster.popInfo({
						type: 'error',
						title: '添加',
						text: '添加 {' + $scope.data.name + '} 错误！'
					});
				}
			});
		};
		//获取数据
		$scope.get = function(row, fun) {
			app.get($scope, "b7dab1c4-3e86-455b-ab11-a5c48cec4971?id=" + row.id, function(r) {
				//回调数据
				fun(r.data.data);
			});
		};
		//返回
		$scope.back = function() {
			$scope.addContent = false;
			$scope.editContent = false;
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
//仓库详情弹框控制
app.controller('4a2d21ee', ['$scope', '$modal', '$log', function($scope, $modal, $log) {
	$scope.show = function(row) {
		$scope.$parent.get(row, function(data) {
			data.check = data.status == 1;
			var modalInstance = $modal.open({
				templateUrl: '9234f1ec-d3a5-4412-843a-90b893465b35',
				controller: 'f79e3a7b',
				size: '{width:680px;max-width:1024px;}',
				resolve: {
					rows: function() {
						return data;
					}
				}
			});
			modalInstance.result.then(function(row) {}, function() {
				$log.info('模式对话框关闭时间:', new Date())
			});
		});
	};
}]);