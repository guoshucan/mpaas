angular.module('app').controller('320c8b1c', ['$scope', '$http', '$state', '$rootScope', '$compile', '$translate',
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
			name: "账户",
			root: "超级账户",
			status: "状态",
			date: "日期",
			open: "操作",
			submit: "提交",
			back: "返回",
			repeat: "重复！",
			statusComplete: "状态完成。",
			rootComplete: "超管完成。",
			show: "查看",
			form: {
				account: {
					name: "账户：",
					placeholder: "请填写账户..."
				},
				description: {
					name: "描述：",
					placeholder: "请填写描述..."
				},
				status: "是否启用：",
				root: "超级账户：",
				password: {
					name: "密码：",
					placeholder: "请填写密码..."
				}
			},
			add: {
				name: "新增",
				account: "添加账户"
			},
			edit: {
				name: "修改",
				account: "修改账户"
			},
			content: {
				details: "账户详情",
				accountManagement: "账户管理"
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
			root: false,
			description: "",
			password: ""
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
			app.getJsonObject("3e1e51ed/i18n/sshAccountManage/" + $translate.use() + ".json", function(r) {
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
			var sr = localStorage.getItem("8d64fd21-0f1b-4f38-a216-fbef7a5ea0f0");
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
				localStorage.removeItem("8d64fd21-0f1b-4f38-a216-fbef7a5ea0f0");
				$scope.selectRequest.key = "";
				$scope.selectRequest.startTime = null;
				$scope.selectRequest.endTime = null;
				$scope.selectRequest.status = "-1";
				$scope.selectRequest.start = 1;
			}
			localStorage.setItem("8d64fd21-0f1b-4f38-a216-fbef7a5ea0f0", JSON.stringify($scope.selectRequest));
			$scope.select();
		};
		//更新状态
		$scope.statusUpdate = function(row) {
			app.get($scope, "1740c9cb-c5ff-4108-bcf3-50ff037eb93c?id=" + row.id + "&status=" + (row.status == 1 ? 0 : 1), function(r) {
				$scope.toaster.popInfo({
					type: 'info',
					title: $scope.i18n.edit.name,
					text: $scope.i18n.edit.name + ' {' + row.name + '} ' + $scope.i18n.statusComplete
				});
				$scope.select();
			});
		};
		$scope.rootUpdate = function(row) {
			app.get($scope, "bd5db608-9521-4d28-bd9e-e84aa5307496?id=" + row.id + "&root=" + row.root, function(r) {
				$scope.toaster.popInfo({
					type: 'info',
					title: $scope.i18n.edit.name,
					text: $scope.i18n.edit.name + ' {' + row.name + '} ' + $scope.i18n.rootComplete
				});
				$scope.select();
			});
		};
		//删除全部选中
		$scope.batchDelete = function() {
			app.batchDelete($scope, "aa0d7b48-0032-4602-9aa1-799c5d0deb1a", app.getPageIds($scope), function(r) {
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
			app.get($scope, "c6afe6a9-6175-4374-86f8-6d745aefa56a?id=" + row.id, function(r) {
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
			app.select($scope, "aa1d3d07-0382-4288-ad27-055a24b2f5cf", obj, function(r) {
				$scope.page.data = r.data.data;
				$scope.page.pages = r.data.pages;
				$scope.page.count = r.data.count;
				//初始化复选状态
				if($scope.isSelect) {
					$scope.isSelect = false;
					if($scope.page.data == null || $scope.page.data.length == 0) {
						var sr = localStorage.getItem("8d64fd21-0f1b-4f38-a216-fbef7a5ea0f0");
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
				o.root = $scope.data.root;
				o.id = $scope.data.id;
				o.name = $scope.data.name;
				o.password = $scope.data.password;
				o.description = $scope.data.description;
				app.edit($scope, "d49237c3-70d4-46c1-899e-bb883197d40e", o, function(r) {
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
				$scope.data.root = false;
				$scope.data.description = "";
				$scope.data.password = "";
				$scope.tableContent = false;
				$scope.addContent = true;
				return;
			}
			app.add($scope, "23ca2b7b-80ec-4153-88b6-9b0a4c3b784f", {
				name: $scope.data.name,
				description: $scope.data.description,
				status: ($scope.data.status ? 1 : 0),
				root: $scope.data.root,
				password: $scope.data.password
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
			app.get($scope, "fd0d4fb1-0653-473e-ba62-096404cb01d9?id=" + row.id, function(r) {
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