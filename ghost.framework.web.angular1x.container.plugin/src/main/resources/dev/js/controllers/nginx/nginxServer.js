angular.module('app').controller('f3b62bf1', ['$scope', '$http', '$state', '$rootScope', '$compile',
	function($scope, $http, $state, $rootScope, $compile) {
		//初始信息
		$scope.data = {
			id: null,
			name: null,
			status: false,
			groupName: null,
			groupId: null,
			regionId: null,
			regionName: null,
			userName: null,
			ipAddress: null,
			hostName: null,
			timeout: 300000,
			remoteDirectory: "/",
			version: null,
			password: null,
			port: 22,
			description: null
		};
		$scope.tableContent = true;
		$scope.addContent = false;
		$scope.editContent = false;
		$scope.deleteAllDisabled = true;
		//组声明
		$scope.selectGroup;
		$scope.openSelectGroup;
		//区域声明
		$scope.selectRegion;
		$scope.openSelectRegion;
		//SSH服务器请求
		$scope.selectRequest = {
			key: null,
			startTime: null,
			endTime: null,
			status: "-1",
			start: 1,
			length: "10",
			groupId: null,
			groupName: null,
			regionId: null,
			regionName: null
		};
		$scope.selectAll = false; //选择全部
		//SSH服务器分页控件
		$scope.page = {
			data: [],
			count: 0,
			maxSize: 5,
			pages: 0
		};
		//区域对象
		$scope.region = {};
		//SSH服务器区域分页控件
		$scope.region.page = {
			data: [],
			count: 0,
			maxSize: 5,
			pages: 0
		};
		//SSH服务器区域请求
		$scope.region.selectRequest = {
			key: null,
			startTime: null,
			endTime: null,
			status: "-1",
			start: 1,
			length: 10
		};
		//SSH服务器区域搜索
		$scope.region.search = function(clean) {
			if(clean) {
				//清理搜索条件
				$scope.region.selectRequest.key = "";
				$scope.region.selectRequest.startTime = null;
				$scope.region.selectRequest.endTime = null;
				$scope.region.selectRequest.status = "-1";
				$scope.region.selectRequest.start = 1;
			}
			$scope.region.select();
		};
		//SSH服务器区域获取列表
		$scope.region.select = function() {
			//获取数据
			var o = Object.assign({}, $scope.region.selectRequest);
			o.start = o.start - 1;
			app.select($scope, "a59a7dbc-0d5a-4f31-b7e0-588451284681", o, function(r) {
				$scope.region.page.data = r.data.data;
				$scope.region.page.pages = r.data.pages;
				$scope.region.page.count = r.data.count;
				//初始化复选状态
				if($scope.region.page.data != null) {
					$scope.region.page.data.forEach(row => {
						row.statusCheck = (row.status == 1);
					});
				}
			});
		};
		//分页位置更改
		$scope.region.pageChange = function() {
			console.log('pageChange:' + $scope.region.selectRequest.start);
			$scope.region.select();
		};
		//组对象
		$scope.group = {};
		//SSH服务器组分页控件
		$scope.group.page = {
			data: [],
			count: 0,
			maxSize: 5,
			pages: 0
		};
		//SSH服务器组请求
		$scope.group.selectRequest = {
			key: null,
			startTime: null,
			endTime: null,
			status: "-1",
			start: 1,
			length: 10
		};
		//SSH服务器组搜索
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
		//SSH服务器组获取列表
		$scope.group.select = function() {
			//获取数据
			var o = Object.assign({}, $scope.group.selectRequest);
			o.start = o.start - 1;
			app.select($scope, "5d110f4d-c366-4804-b675-f61fe45cb356", o, function(r) {
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
			title: '这是标题',
			text: '这是消息内容'
		};
		//初始化
		$scope.init = function() {
			$scope.group.selectRequest.start = 1;
			$scope.region.selectRequest.start = 1;
			var sr = localStorage.getItem("d158aba5-8520-4849-b41d-ac5c2fe33e77");
			if(sr != undefined && sr != null) {
				$scope.selectRequest = JSON.parse(sr);
				if($scope.selectRequest.groupId != null) {
					if($scope.selectGroup == undefined) {
						$scope.selectGroup = {};
					}
					$scope.selectGroup.id = $scope.selectRequest.groupId;
					$scope.selectGroup.name = $scope.selectRequest.groupName;
				}
				if($scope.selectRequest.regionId != null) {
					if($scope.selectRegion == undefined) {
						$scope.selectRegion = {};
					}
					$scope.selectRegion.id = $scope.selectRequest.regionId;
					$scope.selectRegion.name = $scope.selectRequest.regionName;
				}
			}
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
				localStorage.removeItem("d158aba5-8520-4849-b41d-ac5c2fe33e77");
				$scope.selectRequest.key = "";
				$scope.selectRequest.startTime = null;
				$scope.selectRequest.endTime = null;
				$scope.selectRequest.status = "-1";
				$scope.selectGroup = null;
				$scope.selectRegion = null;
				$scope.selectRequest.start = 1;
				$scope.selectRequest.groupId = null;
				$scope.selectRequest.regionId = null;
			}
			var o = Object.assign({}, $scope.selectRequest);
			if($scope.selectGroup != null) {
				o.groupId = $scope.selectGroup.groupId;
				o.groupName = $scope.selectGroup.groupName;
			}
			if($scope.selectRegion != null) {
				o.regionId = $scope.selectRegion.regionId;
				o.regionName = $scope.selectRegion.regionName;
			}
			localStorage.setItem("d158aba5-8520-4849-b41d-ac5c2fe33e77", JSON.stringify(o));
			$scope.select();
		};
		//重置密码
		$scope.resetPassword = function(row, fun) {};
		//更新密码
		$scope.editPassword = function(row, password) {};
		//更新状态
		$scope.statusUpdate = function(row) {
			app.get($scope, "fdc3811b-4504-46b1-8a61-b6df31b53b37?id=" + row.id + "&status=" + (row.status == 1 ? 0 : 1), function(r) {
				$scope.toaster.popInfo({
					type: 'info',
					title: '修改',
					text: '修改 {' + row.name + '} 状态完成。'
				});
				$scope.select();
			});
		};
		//删除全部选中
		$scope.batchDelete = function() {
			app.batchDelete($scope, "aed4d3d1-a0a3-4286-99cf-debf6a77f449", app.getPageIds($scope), function(r) {
				$scope.toaster.popInfo({
					type: 'warning',
					title: '删除',
					text: '删除完成。'
				});
				$scope.select();
			});
		};
		//删除
		$scope.delete = function(row) {
			app.delete($scope, "b2062b6e-d4bb-4ac7-9834-a427e0a30aa9?id=" + row.id, function(r) {
				$scope.toaster.popInfo({
					type: 'warning',
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
			//获取数据
			var o = Object.assign({}, $scope.selectRequest);
			o.start = o.start - 1;
			if($scope.selectGroup != null) {
				o.groupId = $scope.selectGroup.id;
			}
			if($scope.selectRegion != null) {
				o.regionId = $scope.selectRegion.id;
			}
			app.select($scope, "e3fd026c-685b-475f-9ddd-ec58a20441cc", o, function(r) {
				$scope.page.data = r.data.data;
				$scope.page.pages = r.data.pages;
				$scope.page.count = r.data.count;
				//初始化复选状态
				if($scope.isSelect) {
					$scope.isSelect = false;
					if($scope.page.data == null || $scope.page.data.length == 0) {
						var sr = localStorage.getItem("d158aba5-8520-4849-b41d-ac5c2fe33e77");
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
		//修改
		$scope.edit = function(row) {
			if(row == null) {
				var o = Object.assign({}, $scope.data);
				o.status = o.status ? 1 : 0;
				o.groupId = $scope.openSelectGroup.id;
				o.groupName = $scope.openSelectGroup.name;
				o.regionId = $scope.openSelectRegion.id;
				o.regionName = $scope.openSelectRegion.name;
				app.edit($scope, "02c7feab-99a8-4b35-84d0-f7b7c64bc488", o,
					function(r) {
						$scope.toaster.popInfo({
							type: 'info',
							title: '修改',
							text: '修改 {' + $scope.data.name + '} 完成。'
						});
						$scope.openSelectGroup = null; //重置选择SSH服务器组
						$scope.openSelectRegion = null; //重置选择SSH服务器区域
						$scope.select();
					},
					function(r) {
						if(r.data && r.data.code == 1) {
							$scope.toaster.popInfo({
								type: 'warning',
								title: '修改',
								text: '修改 {' + $scope.data.name + '} 重复！'
							});
						} else {
							$scope.toaster.popInfo({
								type: 'error',
								title: '修改',
								text: '修改 {' + $scope.data.name + '} 错误！'
							});
						}
					});
			} else {
				//初始化修改参数
				$scope.tableContent = false;
				$scope.editContent = true;
				app.get($scope, "34d19d4a-bb85-4340-bf39-b9065c6ed934?id=" + row.id, function(r) {
					//组
					$scope.openSelectGroup = {
						id: row.groupId,
						name: row.groupName
					};
					//区域
					$scope.openSelectRegion = {
						id: row.regionId,
						name: row.regionName
					};
					$scope.data = r.data.data;
					$scope.data.status = data.status == 1;
				});
			}
		};
		//添加
		$scope.add = function() {
			if(!$scope.addContent) {
				//
				$scope.openSelectGroup = null;
				//
				$scope.data.id = null;
				$scope.data.name = null;
				$scope.data.status = false;
				$scope.data.groupName = null;
				$scope.data.groupId = null;
				$scope.data.regionName = null;
				$scope.data.regionId = null;
				$scope.data.userName = null;
				$scope.data.ipAddress = null;
				$scope.data.hostName = null;
				$scope.data.timeout = 300000;
				$scope.data.remoteDirectory = "/";
				$scope.data.version = null;
				$scope.data.confirmPassword = null;
				$scope.data.password = null;
				$scope.data.port = 22;
				$scope.data.description = null;
				//
				$scope.tableContent = false;
				$scope.addContent = true;
				return;
			}
			var o = Object.assign({}, $scope.data);
			o.status = o.status ? 1 : 0;
			o.groupId = $scope.openSelectGroup.id;
			o.regionId = $scope.openSelectRegion.id;
			app.add($scope, "6e8e98ea-1bac-4d7e-892e-a15cdfd3afb3", o, function(r) {
				$scope.toaster.popInfo({
					type: 'success',
					title: '添加',
					text: '添加 {' + o.name + '} 完成。'
				});
				$scope.openSelectGroup = null; //设置回选择组对象
				$scope.openSelectRegion = null; //设置回选择区域对象
				$scope.select();
			}, function(r) {
				if(r.data && r.data.code == 1) {
					$scope.toaster.popInfo({
						type: 'warning',
						title: '添加',
						text: '添加 {' + o.name + '} 重复！'
					});
				} else {
					$scope.toaster.popInfo({
						type: 'error',
						title: '添加',
						text: '添加 {' + o.name + '} 错误！'
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
		//测试连接
		$scope.testConnection = function(row) {
			console.log("测试连接:" + row.name);
			app.get($scope, "3fa73f23-2d27-426b-aabe-4901451225ba?id=" + row.id, function(r) {
				if(r.data.data) {
					$scope.toaster.popInfo({
						type: 'success',
						title: '测试连接',
						text: '正常连接 {' + row.name + '->' + row.hostName + ':' + row.port + '}。'
					});
				} else {
					$scope.toaster.popInfo({
						type: 'error',
						title: '测试连接',
						text: '连接失败 {' + row.name + '->' + row.hostName + ':' + row.port + '} ！'
					});
				}
			});
		};
	}
]);
//SSH服务器组对话框
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
//SSH服务器组控制器
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
	//选中SSH服务器组
	$scope.click = function(row) {
		console.log("click:" + row.name);
		$scope.modalInstance.close(row);
		//判断是否为修改SSH服务器选择SSH服务器组信息
		if($scope.p.addContent || $scope.p.editContent) {
			//搜索SSH服务器组选择
			$scope.p.openSelectGroup = row;
		} else {
			//添加或修改SSH服务器组选择
			$scope.p.selectGroup = row;
		}
	};
}]);
//SSH服务器区域对话框
app.controller('833a9302-56ee-4784-a159-c2dbc15bceb1', ['$scope', '$modalInstance', 'p', function($scope, $modalInstance, p) {
	$scope.p = p;
	//加载数据
	$scope.p.region.select(); //获取数据
	$scope.ok = function() {
		console.log("ok");
		$modalInstance.close('ok');
	};
	$scope.cancel = function() {
		console.log("cancel");
		$modalInstance.dismiss('cancel');
	};
}]);
//SSH服务器区域控制器
app.controller('701dd22f-9f03-425d-9170-213a7d298cdc', ['$scope', '$modal', '$log', function($scope, $modal, $log) {
	$scope.p;
	$scope.modalInstance;
	$scope.init = function() {
		$scope.p = $scope.$parent;
	};
	$scope.open = function(size) {
		$scope.modalInstance = $modal.open({
			templateUrl: '77dfbc63',
			controller: '833a9302-56ee-4784-a159-c2dbc15bceb1',
			size: size,
			resolve: {
				p: function() {
					return $scope;
				}
			}
		});
		$scope.p.region.selectRequest.status = '-1';
		$scope.p.region.selectRequest.start = 1;
	};
	//选中SSH服务器区域
	$scope.click = function(row) {
		console.log("click:" + row.name);
		$scope.modalInstance.close(row);
		//判断是否为修改SSH服务器选择SSH服务器区域信息
		if($scope.p.addContent || $scope.p.editContent) {
			//搜索SSH服务器区域选择
			$scope.p.openSelectRegion = row;
		} else {
			//添加或修改SSH服务器区域选择
			$scope.p.selectRegion = row;
		}
	};
}]);
//删除确认框
app.controller('ffcd05b6-9fcf-4420-939b-e3a3a2a64f1f', ['$scope', '$modalInstance', 'rows', function($scope, $modalInstance, rows) {
	$scope.rows = rows;
	$scope.ok = function() {
		console.log("ok");
		//为密码修改关闭
		$modalInstance.close($scope);
	};
	$scope.cancel = function() {
		console.log("cancel");
		$modalInstance.dismiss('cancel');
	};
}]);
//删除控制器
app.controller('ced851f6-aba4-4a36-90f3-c6b78087d774', ['$scope', '$modal', '$log', function($scope, $modal, $log) {
	$scope.open = function(row) {
		var rows = [];
		rows[0] = row;
		var modalInstance = $modal.open({
			templateUrl: 'aae9de1f',
			controller: 'ffcd05b6-9fcf-4420-939b-e3a3a2a64f1f',
			size: 'sm',
			resolve: {
				rows: function() {
					return rows;
				}
			}
		});
		modalInstance.result.then(function(scope) {
			$log.info('选择结果:', scope.rows);
			if(rows == 'cancel') {
				$log.info('放弃删除:', scope.rows);
				return;
			}
			$log.info('确认删除:', scope.rows);
			$scope.$parent.$parent.delete(scope.rows[0]);
		}, function() {
			$log.info('模式对话框关闭时间:', new Date())
		});
	};
}]);
//多个删除控制器
app.controller('f2c6bb9d-9faf-4ae7-9ead-989c047a7491', ['$scope', '$modal', '$log', function($scope, $modal, $log) {
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
			templateUrl: 'aae9de1f',
			controller: 'ffcd05b6-9fcf-4420-939b-e3a3a2a64f1f',
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
			$scope.$parent.batchDelete();
		}, function() {
			$log.info('模式对话框关闭时间:', new Date())
		});
	};
}]);
//SSH服务器详情弹框控制
app.controller('4a2d21ee', ['$scope', '$modal', '$log', function($scope, $modal, $log) {
	$scope.show = function(row) {
		app.get($scope.$parent, "34d19d4a-bb85-4340-bf39-b9065c6ed934?id=" + row.id, function(r) {
			var data = r.data.data;
			data.status = data.status == 1;
			var modalInstance = $modal.open({
				templateUrl: 'd933d3e7',
				controller: 'ffcd05b6-9fcf-4420-939b-e3a3a2a64f1f',
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
					}
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