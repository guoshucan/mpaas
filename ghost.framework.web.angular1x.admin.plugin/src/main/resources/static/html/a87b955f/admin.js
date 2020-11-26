angular.module('app').controller('a87b955f', ['$scope', '$http', '$state', '$rootScope', '$translate',
	function($scope, $http, $state, $rootScope, $translate) {
		//初始信息
		$scope.i18n = {
			address: "地址",
			weixin: "微信",
			email: "邮箱",
			mobilePhone: "手机",
			passwordsAreInconsistent: "两次密码输入不一致!",
			reset: "重置",
			accountPassword: "账号密码",
			account: "账号",
			newPassword: "新密码",
			resetPassword: "重置密码",
			editPassword: "修改密码",
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
			show: "查看",
			date: "日期",
			open: "操作",
			submit: "提交",
			back: "返回",
			repeat: "重复！",
			statusComplete: "状态完成。",
			selectGroup: "选择管理组",
			cancel: "取消",
			addAdmin: "添加管理员",
			adminDetails: "管理员信息",
			groupName: "管理组名称",
			adminName: "管理员名称",
			groupPlaceholder: "请填写管理组名称...",
			adminPlaceholder: "请填写管理员名称...",
			adminAccount: "管理员账号",
			adminAccountPlaceholder: "请填写管理员账号...",
			adminPassword: "管理员密码",
			adminPasswordPlaceholder: "请填写管理员密码...",
			mobilePhonePlaceholder: "请填写手机...",
			emailPlaceholder: "请填写邮箱...",
			qqPlaceholder: "请填写QQ...",
			weixinPlaceholder: "请填写微信...",
			addressPlaceholder: "请填写地址...",
			editAdmin: "修改管理组",
			add: "新增",
			edit: "修改",
			group: "修改管理组",
			passwordComplete: "密码完成。",
			chooseIs: "选择的是",
			confirm: "确认",
			delete: {
				confirmDeletion: "确认删除！",
				selected: "删除所选",
				name: "删除",
				complete: "删除完成",
				error: "删除错误！",
				batchComplete: "批量删除完成。"
			},
			systemManagement: "系统管理",
			adminManagement: "管理员管理",
			managementGroup: "管理组",
			search: {
				pleaseSelectStatus: "请选择状态",
				key: "请输入关键字...",
				name: "搜索",
				placeholder: "请选择管理组..."
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
		$scope.addData = {
			adminName: null,
			status: false,
			groupName: null,
			groupId: null,
			email: null,
			mobilePhone: null,
			address: null,
			qq: null,
			weixin: null,
			password: null,
			confirmPassword: null,
			adminUser: null
		};
		$scope.tableContent = true;
		$scope.addContent = false;
		$scope.editContent = false;
		$scope.editRightsShow = false;
		$scope.detailContentShow = false;
		$scope.deleteAllDisabled = true;
		$scope.editRow;
		$scope.selectGroup;
		$scope.openSelectGroup;
		//管理员请求
		$scope.selectRequest = {
			key: null,
			startTime: null,
			endTime: null,
			status: "-1",
			start: 1,
			length: "10",
			groupId: null,
			groupName: null
		};
		$scope.selectAll = false; //选择全部
		//管理员分页控件
		$scope.page = {
			data: [],
			count: 0,
			maxSize: 5,
			pages: 0
		};
		//组对象
		$scope.group = {};
		//管理组分页控件
		$scope.group.page = {
			data: [],
			count: 0,
			maxSize: 5,
			pages: 0
		};
		//管理组请求
		$scope.group.selectRequest = {
			key: null,
			startTime: null,
			endTime: null,
			status: "-1",
			start: 1,
			length: 10
		};
		//管理组搜索
		$scope.group.search = function(clean) {
			if(clean) {
				//清理搜索条件
				$scope.group.selectRequest.key = "";
				$scope.group.selectRequest.startTime = null;
				$scope.group.selectRequest.endTime = null;
				$scope.group.selectRequest.status = "-1";
				$scope.group.selectRequest.start = 1;
			}
			$scope.group.select();
		};
		//管理组获取列表
		$scope.group.select = function() {
			//获取数据
			var obj = Object.assign({}, $scope.group.selectRequest);
			obj.start = obj.start - 1;
			app.select($scope, "0d10fb4e-d6a8-4914-a8a4-ccc13f7d0ec0", obj, function(r) {
				$scope.group.page.data = r.data.data;
				$scope.group.page.pages = r.data.pages;
				$scope.group.page.count = r.data.count;
				//初始化复选状态
				if($scope.group.page.data != null) {
					$scope.group.page.data.forEach(row => {
						row.statusCheck = (row.status == 1);
					});
				}
			});
		};
		//分页位置更改
		$scope.group.pageChange = function() {
			console.log('pageChange:' + $scope.group.selectRequest.start);
			$scope.group.select();
		};
		$scope.toaster = null;
		$scope.to = {
			type: 'success',
			title: '',
			text: ''
		};
		//初始化区域
		$scope.initLocale = function() {
			app.getJsonObject("a87b955f/i18n/admin/" + $translate.use() + ".json", function(r) {
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
			$scope.group.selectRequest.start = 1;
			var sr = localStorage.getItem("a87b955f");
			if(sr != undefined && sr != null) {
				$scope.selectRequest = JSON.parse(sr);
				if($scope.selectRequest.groupId != null) {
					if($scope.selectGroup == undefined) {
						$scope.selectGroup = {};
					}
					$scope.selectGroup.groupId = $scope.selectRequest.groupId;
					$scope.selectGroup.groupName = $scope.selectRequest.groupName;
				}
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
				localStorage.removeItem("a87b955f");
				$scope.selectRequest.key = "";
				$scope.selectRequest.startTime = null;
				$scope.selectRequest.endTime = null;
				$scope.selectRequest.status = "-1";
				$scope.selectGroup = null;
				$scope.selectRequest.start = 1;
				$scope.selectRequest.groupId = null;
			}
			var o = Object.assign({}, $scope.selectRequest);
			if($scope.selectGroup != null) {
				o.groupId = $scope.selectGroup.groupId;
				o.groupName = $scope.selectGroup.groupName;
			}
			localStorage.setItem("a87b955f", JSON.stringify(o));
			$scope.select();
		};
		//重置密码
		$scope.resetPassword = function(row, fun) {
			app.get($scope, "8f928d25-101d-431a-b1d0-47b2059c63e1?adminId=" + row.adminId, function(r) {
				row.password = r.data.data;
				fun(row);
			});
		};
		//更新密码
		$scope.editPassword = function(row, password) {
			app.edit($scope, "6e5fee72-3800-4c23-b95a-c8797c9f6e6c", {
				id: row.adminId,
				password: password
			}, function(r) {
				$scope.toaster.popInfo({
					type: 'info',
					title: $scope.i18n.edit,
					text: $scope.i18n.edit + ' {' + row.adminUser + '} ' + $scope.i18n.passwordComplete
				});
			});
		};
		//更新状态
		$scope.statusUpdate = function(row) {
			app.get($scope, "8cfd1bb7-22de-46b3-b48b-729118ea34bc?adminId=" + row.adminId + "&status=" + (row.status == 1 ? 0 : 1), function(r) {
				$scope.toaster.popInfo({
					type: 'info',
					title: $scope.i18n.edit,
					text: $scope.i18n.edit + ' {' + row.adminUser + '} ' + $scope.i18n.statusComplete
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
					all.ids[all.ids.length] = row.adminId;
				}
			}
			if(all.ids.length == 0) {
				return;
			}
			app.batchDelete($scope, "fa793855-c8ad-465f-baba-646c2c225eb7", all, function(r) {
				$scope.toaster.popInfo({
					type: 'warning',
					title: $scope.i18n.delete.name,
					text: $scope.i18n.delete.batchComplete
				});
				$scope.selectAll = false;
				$scope.select();
			});
		};
		//删除
		$scope.delete = function(row) {
			app.delete($scope, "3d5d6e6d-4d59-426e-ab25-955825387c0a?adminId=" + row.adminId, function(r) {
				$scope.toaster.popInfo({
					type: 'warning',
					title: $scope.i18n.delete.name,
					text: $scope.i18n.delete.name + ' {' + row.adminUser + '} ' + $scope.i18n.delete.complete
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
			//获取数据
			var obj = Object.assign({}, $scope.selectRequest);
			obj.start = obj.start - 1;
			if($scope.selectGroup != null) {
				obj.groupId = $scope.selectGroup.groupId;
			}
			app.select($scope, "527ac642-3df8-4844-85db-bae9ae6c271a", obj, function(r) {
				$scope.page.data = r.data.data;
				$scope.page.pages = r.data.pages;
				$scope.page.count = r.data.count;
				//初始化复选状态
				if($scope.isSelect) {
					$scope.isSelect = false;
					if($scope.page.data == null || $scope.page.data.length == 0) {
						var sr = localStorage.getItem("a87b955f");
						if(sr != undefined && sr != null) {
							$scope.search(true);
							return;
						}
					}
				}
				if($scope.page.data != null) {
					$scope.page.data.forEach(row => {
						row.check = false;
						row.statusCheck = (row.status == 1);
					});
				}
			});
		};
		//获取数据
		$scope.get = function(row, fun) {
			app.get($scope, "121bf996-cd8c-432f-a4d6-787c0069d170?adminId=" + row.adminId, function(r) {
				//回调数据
				fun(r.data.data);
			});
		};
		//修改
		$scope.edit = function(row) {
			if(row == null) {
				var o = Object.assign({}, $scope.editRow);
				o.status = o.status ? 1 : 0;
				o.groupId = o.groupId;
				o.groupName = o.groupName;
				app.edit($scope, "21bdb51a-24dc-44a1-9fc4-e05cb8ec4971", o, function(r) {
					$scope.toaster.popInfo({
						type: 'info',
						title: $scope.i18n.edit,
						text: $scope.i18n.edit + ' {' + $scope.editRow.adminUser + '} ' + $scope.i18n.complete
					});
					$scope.openSelectGroup = null; //重置选择管理组
					$scope.select();
				}, function(r) {
					if(r.data && r.data.code == 1) {
						$scope.toaster.popInfo({
							type: 'warning',
							title: $scope.i18n.edit,
							text: $scope.i18n.edit + ' {' + $scope.editRow.adminUser + '} ' + $scope.i18n.repeat
						});
					} else {
						$scope.toaster.popInfo({
							type: 'error',
							title: $scope.i18n.edit,
							text: $scope.i18n.edit + ' {' + $scope.editRow.adminUser + '} ' + $scope.i18n.error
						});
					}
				});
			} else {
				//初始化修改参数
				$scope.tableContent = false;
				$scope.editContent = true;
				$scope.get(row, function(data) {
					$scope.openSelectGroup = {
						groupId: row.groupId,
						groupName: row.groupName
					};
					$scope.editRow = data;
					$scope.editRow.status = data.status == 1;
				});
			}
		};
		//添加
		$scope.add = function() {
			if(!$scope.addContent) {
				//
				$scope.openSelectGroup = null;
				//
				$scope.addData.groupName = null;
				$scope.addData.groupId = null;
				$scope.addData.adminName = null;
				$scope.addData.adminUser = null;
				$scope.addData.password = null;
				$scope.addData.mobilePhone = null;
				$scope.addData.email = null;
				$scope.addData.qq = null;
				$scope.addData.weixin = null;
				$scope.addData.address = null;
				$scope.addData.status = false;
				//
				$scope.tableContent = false;
				$scope.addContent = true;
				return;
			}
			var data = Object.assign({}, $scope.addData);
			data.status = data.status ? 1 : 0;
			data.groupId = $scope.openSelectGroup.groupId;
			app.add($scope, "bc9815c5-0d88-470c-8bb3-5386004966ba", data, function(r) {
				$scope.toaster.popInfo({
					type: 'success',
					title: $scope.i18n.add,
					text: $scope.i18n.add + ' {' + data.adminUser + '} ' + $scope.i18n.complete
				});
				$scope.openSelectGroup = null; //设置回选择组对象
				$scope.select();
			}, function(r) {
				if(r.data && r.data.code == 1) {
					$scope.toaster.popInfo({
						type: 'warning',
						title: $scope.i18n.add,
						text: $scope.i18n.add + ' {' + data.adminUser + '} ' + $scope.i18n.repeat
					});
				} else {
					$scope.toaster.popInfo({
						type: 'error',
						title: $scope.i18n.add,
						text: $scope.i18n.add + ' {' + data.adminUser + '} ' + $scope.i18n.error
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
//管理组对话框
app.controller('52e46d58-39fb-4406-96b7-169adec64ec3', ['$scope', '$modalInstance', 'p', function($scope, $modalInstance, p) {
	$scope.p = p;
	//加载数据
	$scope.p.group.select(); //获取数据
	$scope.ok = function() {
		console.log("ok");
		$modalInstance.close('ok');
	};
	$scope.cancel = function() {
		console.log("cancel");
		$modalInstance.dismiss('cancel');
	};
}]);
//管理组控制器
app.controller('f341fa72', ['$scope', '$modal', '$log', function($scope, $modal, $log) {
	$scope.p;
	$scope.modalInstance;
	$scope.init = function() {
		$scope.p = $scope.$parent;
	};
	$scope.open = function(size) {
		$scope.modalInstance = $modal.open({
			templateUrl: '299137d7',
			controller: '52e46d58-39fb-4406-96b7-169adec64ec3',
			size: size,
			resolve: {
				p: function() {
					return $scope;
				}
			}
		});
		$scope.p.group.selectRequest.status = '-1';
		$scope.p.group.selectRequest.start = 1;
	};
	//选中管理组
	$scope.click = function(row) {
		console.log("click:" + row.groupName);
		$scope.modalInstance.close(row);
		//判断是否为修改管理员选择管理组信息
		if($scope.p.addContent || $scope.p.editContent) {
			//搜索管理组选择
			$scope.p.openSelectGroup = row;
		} else {
			//添加或修改管理组选择
			$scope.p.selectGroup = row;
		}
	};
}]);
//删除确认框
app.controller('ffcd05b6-9fcf-4420-939b-e3a3a2a64f1f', ['$scope', '$modalInstance', 'rows', 'i18n', function($scope, $modalInstance, rows, i18n) {
	$scope.i18n = i18n;
	$scope.rows = rows;
	$scope.confirm_password = null;
	$scope.password = null;
	$scope.ok = function() {
		console.log("ok");
		if($scope.confirm_password == null) {
			$modalInstance.close($scope.rows);
			return;
		}
		//为密码修改关闭
		$modalInstance.close($scope);
	};
	$scope.cancel = function() {
		console.log("cancel");
		$modalInstance.dismiss('cancel');
	};
}]);
//修改密码弹框控制器
app.controller('c38fa735', ['$scope', '$modal', '$log', function($scope, $modal, $log) {
	$scope.editPassword = function(row) {
		var modalInstance = $modal.open({
			templateUrl: 'b8bba39c',
			controller: 'ffcd05b6-9fcf-4420-939b-e3a3a2a64f1f',
			size: '{width:680px;max-width:1024px;}',
			resolve: {
				rows: function() {
					return row;
				},
				i18n: function() {
					return $scope.$parent.i18n;
				}
			}
		});
		modalInstance.result.then(function(data) {
			$log.info('确认修改密码:', data);
			$scope.$parent.$parent.editPassword(data.rows, data.password);
		}, function() {
			$log.info('模式对话框关闭时间:', new Date())
		});
	};
}]);
//重置密码控制器
app.controller('6fb41bc1', ['$scope', '$modal', '$log', function($scope, $modal, $log) {
	$scope.resetPassword = function(row) {
		$scope.$parent.$parent.resetPassword(row, function(data) {
			var modalInstance = $modal.open({
				templateUrl: '27f61412',
				controller: 'ffcd05b6-9fcf-4420-939b-e3a3a2a64f1f',
				size: 'sm',
				resolve: {
					rows: function() {
						return row;
					},
					i18n: function() {
						return $scope.$parent.i18n;
					}
				},
				i18n: function() {
					return $scope.$parent.i18n;
				}
			});
			modalInstance.result.then(function(data) {
				$log.info('重置密码:', data);
			}, function() {
				$log.info('模式对话框关闭时间:', new Date())
			});
		});
	};
}]);