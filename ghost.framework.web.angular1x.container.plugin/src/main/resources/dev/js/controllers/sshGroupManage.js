angular.module('app').controller('3c0c6519', ['$scope', '$http', '$state', '$rootScope', '$compile', '$translate',
	function($scope, $http, $state, $rootScope, $compile, $translate) {
		$scope.i18n = {
			operationMaintenance: "运维",
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
			show: "查看",
			form: {
				group: {
					name: "组：",
					placeholder: "请填写组..."
				},
				description: {
					name: "描述：",
					placeholder: "请填写描述..."
				},
				status: "是否启用："
			},
			add: {
				name: "新增",
				group: "添加组"
			},
			edit: {
				name: "修改",
				group: "修改组"
			},
			content: {
				details: "组详情",
				groupManagement: "组管理"
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
			app.getJsonObject("3e1e51ed/i18n/sshGroupManage/" + $translate.use() + ".json", function(r) {
				if(r) {
					$scope.i18n = r;
				}
				if($scope.tableContent) {
					$scope.isSelect = true;
					$scope.select();
				}
			}, function(r) {
				if($scope.tableContent) {
					$scope.isSelect = true;
					$scope.select();
				}
			});
		};
		//初始化
		$scope.init = function() {
			var sr = localStorage.getItem("7f432848-9585-4d9f-a93d-c2c8b85f56b4");
			if(sr != undefined && sr != null) {
				$scope.selectRequest = JSON.parse(sr);
			}
			$scope.initLocale();
			//区域变动监听
			$rootScope.$on('$translateChangeSuccess', function() {
				console.log($translate.use());
				$scope.initLocale();
			});
			//监听跳转添加
			if($rootScope.params && $rootScope.params.add) {
				delete $rootScope.params.add;
				$scope.add();
				return;
			}
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
				localStorage.removeItem("7f432848-9585-4d9f-a93d-c2c8b85f56b4");
				$scope.selectRequest.key = "";
				$scope.selectRequest.startTime = null;
				$scope.selectRequest.endTime = null;
				$scope.selectRequest.status = "-1";
				$scope.selectRequest.start = 1;
			}
			localStorage.setItem("7f432848-9585-4d9f-a93d-c2c8b85f56b4", JSON.stringify($scope.selectRequest));
			$scope.select();
		};
		//更新状态
		$scope.statusUpdate = function(row) {
			app.get($scope, "72089e92-54f1-470d-a9bd-a86cd463974d?id=" + row.id + "&status=" + (row.status == 1 ? 0 : 1), function(r) {
				$scope.toaster.popInfo({
					type: 'info',
					title: $scope.i18n.edit.name,
					text: $scope.i18n.edit.name + ' {' + row.name + '} ' + $scope.i18n.statusComplete
				});
				$scope.select();
			});
		};
		//删除全部选中
		$scope.batchDelete = function() {
			app.batchDelete($scope, "e39cd079-1c65-4bb3-b0d9-fce15de6742b", app.getPageIds($scope), function(r) {
				$scope.toaster.popInfo({
					type: 'success',
					title: $scope.i18n.delete.name,
					text: $scope.i18n.delete.complete
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
					title: $scope.i18n.delete.name,
					text: $scope.i18n.delete.name + ' {' + row.name + '} ' + $scope.i18n.complete
				});
				$scope.select();
			}, function(r) {
				$scope.toaster.popInfo({
					type: 'error',
					title: $scope.i18n.delete.error,
					text: $scope.i18n.delete.name + ' {' + row.name + '} ' + $scope.i18n.error
				});
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
						var sr = localStorage.getItem("7f432848-9585-4d9f-a93d-c2c8b85f56b4");
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
						title: $scope.i18n.edit.name,
						text: $scope.i18n.edit.name + ' {' + o.name + '} ' + $scope.i18n.complete
					});
					$scope.select();
				}, function(r) {
					if(r.data && r.data.code == 1) {
						$scope.toaster.popInfo({
							type: 'warning',
							title: $scope.i18n.edit.name,
							text: $scope.i18n.edit.name + ' {' + o.name + '} ' + $scope.i18n.repeat
						});
					} else {
						$scope.toaster.popInfo({
							type: 'error',
							title: $scope.i18n.edit.name,
							text: $scope.i18n.edit.name + ' {' + o.name + '} ' + $scope.i18n.error
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
					title: $scope.i18n.add.name,
					text: $scope.i18n.add.name + ' {' + $scope.data.name + '} ' + $scope.i18n.complete
				});
				$scope.select();
			}, function(r) {
				if(r.data && r.data.code == 1) {
					$scope.toaster.popInfo({
						type: 'warning',
						title: $scope.i18n.add.name,
						text: $scope.i18n.add.name + ' {' + $scope.data.name + '} ' + $scope.i18n.repeat
					});
				} else {
					$scope.toaster.popInfo({
						type: 'error',
						title: $scope.i18n.add.name,
						text: $scope.i18n.add.name + ' {' + $scope.data.name + '} ' + $scope.i18n.error
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