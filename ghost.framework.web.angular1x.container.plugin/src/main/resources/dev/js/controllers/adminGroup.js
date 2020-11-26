angular.module('app').controller('416940dc', ['$scope', '$http', '$state', '$rootScope', '$translate',
	function($scope, $http, $state, $rootScope, $translate) {
		//初始信息
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
		$scope.addData = {
			groupName: "",
			status: false
		};
		$scope.tableContent = true;
		$scope.addContent = false;
		$scope.editContent = false;
		$scope.detailContentShow = false;
		$scope.deleteAllDisabled = true;
		$scope.editRow;
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
			title: '',
			text: ''
		};
		//初始化区域
		$scope.initLocale = function() {
			app.getJsonObject("a87b955f/i18n/adminGroup/" + $translate.use() + ".json", function(r) {
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
			var sr = localStorage.getItem("59ffca32-5bcf-4b33-aaef-1b6e046cfafe");
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
				if(row.groupId == id) {
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
				localStorage.removeItem("59ffca32-5bcf-4b33-aaef-1b6e046cfafe");
				$scope.selectRequest.key = "";
				$scope.selectRequest.startTime = null;
				$scope.selectRequest.endTime = null;
				$scope.selectRequest.status = "-1";
				$scope.selectRequest.start = 1;
			}
			localStorage.setItem("59ffca32-5bcf-4b33-aaef-1b6e046cfafe", JSON.stringify($scope.selectRequest));
			$scope.select();
		};
		//更新状态
		$scope.statusUpdate = function(row) {
			app.get($scope, "6703b7ae-3ed3-4802-9fc2-2b5969bceb0f?groupId=" + row.groupId + "&status=" + (row.status == 1 ? 0 : 1), function(r) {
				$scope.toaster.popInfo({
					type: 'info',
					title: $scope.i18n.edit.name,
					text: $scope.i18n.edit.name + ' {' + row.groupName + '} ' + $scope.i18n.statusComplete
				});
				$scope.select();
			});
		};
		//删除全部选中
		$scope.batchDelete = function() {
			var all = new Object();
			all.ids = [];
			//遍历是否有选中
			var row;
			for(var i = 0; i < $scope.page.data.length; i++) {
				row = $scope.page.data[i];
				if(row.check) {
					all.ids[all.ids.length] = row.groupId;
				}
			}
			if(all.ids.length == 0) {
				return;
			}
			app.batchDelete($scope, "33dc8b62-e702-48b7-9ed5-61c65f55943a", all, function(r) {
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
			app.delete($scope, "d595c6ea-9350-46e5-a0ac-f3372589acb7?groupId=" + row.groupId, function(r) {
				$scope.toaster.popInfo({
					type: 'success',
					title: $scope.i18n.delete.name,
					text: $scope.i18n.delete.name + ' {' + row.groupName + '} ' + $scope.i18n.complete
				});
				$scope.select();
			}, function(r) {
				$scope.toaster.popInfo({
					type: 'error',
					title: $scope.i18n.delete.error,
					text: $scope.i18n.delete.name + ' {' + row.groupName + '} ' + $scope.i18n.error
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
			app.select($scope, "0d10fb4e-d6a8-4914-a8a4-ccc13f7d0ec0", obj, function(r) {
				$scope.page.data = r.data.data;
				$scope.page.pages = r.data.pages;
				$scope.page.count = r.data.count;
				//初始化复选状态
				if($scope.isSelect) {
					$scope.isSelect = false;
					if($scope.page.data == null || $scope.page.data.length == 0) {
						var sr = localStorage.getItem("59ffca32-5bcf-4b33-aaef-1b6e046cfafe");
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
				o.status = $scope.editRow.status ? 1 : 0;
				o.groupId = $scope.editRow.groupId;
				o.groupName = $scope.editRow.groupName;
				app.edit($scope, "1534cf96-90a4-4572-8a5b-d7fd873d3c92", o, function(r) {
					$scope.toaster.popInfo({
						type: 'info',
						title: $scope.i18n.edit.name,
						text: $scope.i18n.edit.name + ' {' + o.groupName + '} ' + $scope.i18n.complete
					});
					$scope.select();
				}, function(r) {
					if(r.data && r.data.code == 1) {
						$scope.toaster.popInfo({
							type: 'warning',
							title: $scope.i18n.edit.name,
							text: $scope.i18n.edit.name + ' {' + o.groupName + '} ' + $scope.i18n.repeat
						});
					} else {
						$scope.toaster.popInfo({
							type: 'error',
							title: $scope.i18n.edit.name,
							text: $scope.i18n.edit.name + ' {' + o.groupName + '} ' + $scope.i18n.error
						});
					}
				});
			} else {
				//初始化修改参数
				$scope.tableContent = false;
				$scope.editContent = true;
				$scope.editRow = row;
				$scope.editRow.status = row.status == 1;
			}
		};
		//添加
		$scope.add = function() {
			if(!$scope.addContent) {
				$scope.addData.groupName = "";
				$scope.addData.status = false;
				$scope.tableContent = false;
				$scope.addContent = true;
				return;
			}
			app.add($scope, "6e6c11d4-6171-4f81-b485-5d8614b106b5", {
				groupName: $scope.addData.groupName,
				status: ($scope.addData.status ? 1 : 0),
			}, function(r) {
				$scope.toaster.popInfo({
					type: 'success',
					title: $scope.i18n.add.name,
					text: $scope.i18n.add.name + ' {' + $scope.addData.groupName + '} ' + $scope.i18n.complete
				});
				$scope.select();
			}, function(r) {
				if(r.data && r.data.code == 1) {
					$scope.toaster.popInfo({
						type: 'warning',
						title: $scope.i18n.add.name,
						text: $scope.i18n.add.name + ' {' + $scope.addData.groupName + '} ' + $scope.i18n.repeat
					});
				} else {
					$scope.toaster.popInfo({
						type: 'error',
						title: $scope.i18n.add.name,
						text: $scope.i18n.add.name + ' {' + $scope.addData.groupName + '} ' + $scope.i18n.error
					});
				}
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