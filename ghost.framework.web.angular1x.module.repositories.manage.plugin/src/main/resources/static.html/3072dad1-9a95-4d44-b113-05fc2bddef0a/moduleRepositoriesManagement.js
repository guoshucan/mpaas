app.controller('4b5602f0', ['$scope', '$http', '$state', '$rootScope', '$compile',
	function($scope, $http, $state, $rootScope, $compile, locals) {
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
		$scope.selectGroup; // = {groupId:null,groupName:null,status:false};
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
		$scope.isInit = false;
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
			$http.post(app.apiAddress + "admin/0d10fb4e-d6a8-4914-a8a4-ccc13f7d0ec0", obj)
				.then(function(response) {
					if(response.data.code == 0 && response.data.message == "success") {
						$scope.group.page.data = response.data.data;
						$scope.group.page.pages = response.data.pages;
						$scope.group.page.count = response.data.count;
						//初始化复选状态
						if($scope.group.page.data != null) {
							$scope.group.page.data.forEach(row => {
								row.statusCheck = (row.status == 1);
							});
						}
					} else {
						$scope.toaster.popInfo({
							type: 'error',
							title: '错误',
							text: '获取列表错误！'
						});
					}
				}, function(x) {
					$scope.toaster.popInfo({
						type: 'error',
						title: '错误',
						text: '获取列表错误！'
					});
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
		$scope.pop = function() {
			$scope.toaster.popInfo($scope.to);
		};
		//初始化
		$scope.init = function() {
			if($scope.isInit) {
				return;
			}
			$scope.group.selectRequest.start = 1;
			var sr = localStorage.getItem("a87b955f|selectRequest");
			if(sr != undefined && sr != null) {
				$scope.selectRequest = JSON.parse(sr);
				if($scope.selectRequest.groupId != null) {
					if($scope.selectGroup == undefined){
						$scope.selectGroup = {};
					}
					$scope.selectGroup.groupId = $scope.selectRequest.groupId;
					$scope.selectGroup.groupName = $scope.selectRequest.groupName;
				}
			}
			$scope.isInit = true;
			$scope.isSelect = true;
			$scope.select();
		};
		//观察name 当一个model值发生改变的时候 都会触发第二个函数
		$scope.$watch('selectRequest.key', function(newValue, oldValue) {
			console.log(newValue + "==" + oldValue);
		});
		$scope.$watch('selectRequest.startTime', function(newValue, oldValue) {
			console.log(newValue + "==" + oldValue);
		});
		$scope.$watch('selectRequest.endTime', function(newValue, oldValue) {
			console.log(newValue + "==" + oldValue);
		});
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
				localStorage.removeItem("a87b955f|selectRequest");
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
			localStorage.setItem("a87b955f|selectRequest", JSON.stringify(o));
			$scope.select();
		};
		//重置密码
		$scope.resetPassword = function(row, fun) {
			$http.get(app.apiAddress + "admin/8f928d25-101d-431a-b1d0-47b2059c63e1?adminId=" + row.adminId)
				.then(function(response) {
					if(response.data.code == 0 && response.data.message == "success") {
						$scope.toaster.popInfo({
							type: 'info',
							title: '修改',
							text: '修改 {' + row.adminUser + '} 状态完成。'
						});
						row.password = response.data.data;
						fun(row);
					} else {
						$scope.toaster.popInfo({
							type: 'error',
							title: '修改',
							text: '修改 {' + row.adminUser + '} 状态错误！'
						});
					}
				}, function(x) {
					$scope.toaster.popInfo({
						type: 'error',
						title: '修改',
						text: '修改 {' + row.adminUser + '} 状态错误！'
					});;
				});
		};
		//更新密码
		$scope.editPassword = function(row, password) {
			//修改密码
			$http.post(app.apiAddress + "admin/6e5fee72-3800-4c23-b95a-c8797c9f6e6c", {
					id: row.adminId,
					password: password
				})
				.then(function(response) {
					if(response.data.code == 0 && response.data.message == "success") {
						$scope.toaster.popInfo({
							type: 'info',
							title: '修改',
							text: '修改 {' + row.adminUser + '} 密码完成。'
						});
					} else {
						$scope.toaster.popInfo({
							type: 'error',
							title: '修改',
							text: '修改 {' + row.adminUser + '} 密码错误！'
						});
					}
				}, function(x) {
					$scope.toaster.popInfo({
						type: 'error',
						title: '修改',
						text: '修改 {' + row.adminUser + '} 密码错误！'
					});;
				});
		};
		//更新状态
		$scope.statusUpdate = function(row) {
			//修改状态
			$http.get(app.apiAddress + "admin/8cfd1bb7-22de-46b3-b48b-729118ea34bc?adminId=" + row.adminId + "&status=" + (row.status == 1 ? 0 : 1))
				.then(function(response) {
					if(response.data.code == 0 && response.data.message == "success") {
						$scope.toaster.popInfo({
							type: 'info',
							title: '修改',
							text: '修改 {' + row.adminUser + '} 状态完成。'
						});
						$scope.select();
					} else {
						$scope.toaster.popInfo({
							type: 'error',
							title: '修改',
							text: '修改 {' + row.adminUser + '} 状态错误！'
						});
					}
				}, function(x) {
					$scope.toaster.popInfo({
						type: 'error',
						title: '修改',
						text: '修改 {' + row.adminUser + '} 状态错误！'
					});;
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
			$http.post(app.apiAddress + "admin/fa793855-c8ad-465f-baba-646c2c225eb7", all)
				.then(function(response) {
					if(response.data.code == 0 && response.data.message == "success") {
						$scope.toaster.popInfo({
							type: 'warning',
							title: '删除',
							text: '删除完成。'
						});
						$scope.select();
					} else {
						$scope.toaster.popInfo({
							type: 'error',
							title: '删除错误',
							text: '删除错误！'
						});
					}
				}, function(x) {
					$scope.toaster.popInfo({
						type: 'error',
						title: '删除错误',
						text: '删除错误！'
					});
				});
		};
		//删除
		$scope.delete = function(row) {
			//获取数据
			$http.get(app.apiAddress + "admin/3d5d6e6d-4d59-426e-ab25-955825387c0a?adminId=" + row.adminId)
				.then(function(response) {
					if(response.data.code == 0 && response.data.message == "success") {
						$scope.toaster.popInfo({
							type: 'warning',
							title: '删除',
							text: '删除 {' + row.adminUser + '} 完成。'
						});
						$scope.select();
					} else {
						$scope.toaster.popInfo({
							type: 'error',
							title: '删除错误',
							text: '删除 {' + row.adminUser + '} 错误！'
						});
					}
				}, function(x) {
					$scope.toaster.popInfo({
						type: 'error',
						title: '删除错误',
						text: '删除 {' + row.adminUser + '} 错误！'
					});
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
			$http.post(app.apiAddress + "admin/527ac642-3df8-4844-85db-bae9ae6c271a", obj)
				.then(function(response) {
					if(response.data.code == 0 && response.data.message == "success") {
						$scope.page.data = response.data.data;
						$scope.page.pages = response.data.pages;
						$scope.page.count = response.data.count;
						//初始化复选状态
						if($scope.isSelect) {
							$scope.isSelect = false;
							if($scope.page.data == null || $scope.page.data.length == 0) {
								var sr = localStorage.getItem("a87b955f|selectRequest");
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
					} else {
						$scope.toaster.popInfo({
							type: 'error',
							title: '错误',
							text: '获取列表错误！'
						});
					}
				}, function(x) {
					$scope.toaster.popInfo({
						type: 'error',
						title: '错误',
						text: '获取列表错误！'
					});
				});
		};
		//获取数据
		$scope.get = function(row, fun) {
			$http.get(app.apiAddress + "admin/121bf996-cd8c-432f-a4d6-787c0069d170?adminId=" + row.adminId)
				.then(function(response) {
					if(response.data.code == 0 && response.data.message == "success") {
						//回调数据
						fun(response.data.data);
					} else {
						$scope.toaster.popInfo({
							type: 'error',
							title: '获取',
							text: '获取 {' + row.adminUser + '} 错误！'
						});
					}
				}, function(x) {
					$scope.toaster.popInfo({
						type: 'error',
						title: '获取',
						text: '获取 {' + row.adminUser + '} 错误！'
					});
				});
		};
		//修改
		$scope.edit = function(row) {
			if(row == null) {
				var editObj = Object.assign({}, $scope.editRow);
				editObj.status = editObj.status ? 1 : 0;
				editObj.groupId = editObj.groupId;
				editObj.groupName = editObj.groupName;
				//提交数据
				$http.post(app.apiAddress + "admin/21bdb51a-24dc-44a1-9fc4-e05cb8ec4971", editObj)
					.then(function(response) {
						if(response.data.code == 0 && response.data.message == "success") {
							$scope.toaster.popInfo({
								type: 'info',
								title: '修改',
								text: '修改 {' + $scope.editRow.adminUser + '} 完成。'
							});
							$scope.openSelectGroup = null; //重置选择管理组
							$scope.select();
						} else if(response.data.code == 1) {
							$scope.toaster.popInfo({
								type: 'warning',
								title: '修改',
								text: '修改 {' + $scope.editRow.adminUser + '} 重复！'
							});
						} else {
							$scope.toaster.popInfo({
								type: 'error',
								title: '修改',
								text: '修改 {' + $scope.editRow.adminUser + '} 错误！'
							});
						}
					}, function(x) {
						$scope.toaster.popInfo({
							type: 'error',
							title: '修改',
							text: '修改 {' + $scope.editRow.adminUser + '} 错误！'
						});
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
			$http.post(app.apiAddress + "admin/bc9815c5-0d88-470c-8bb3-5386004966ba", data)
				.then(function(response) {
					if(response.data.code == 0 && response.data.message == "success") {
						$scope.toaster.popInfo({
							type: 'success',
							title: '添加',
							text: '添加 {' + data.adminUser + '} 完成。'
						});
						$scope.openSelectGroup = null; //设置回选择组对象
						$scope.select();
					} else if(response.data.code == 1) {
						$scope.toaster.popInfo({
							type: 'warning',
							title: '添加',
							text: '添加 {' + data.adminUser + '} 重复！'
						});
					} else {
						$scope.toaster.popInfo({
							type: 'error',
							title: '添加',
							text: '添加 {' + data.adminUser + '} 错误！'
						});
					}
				}, function(x) {
					$scope.toaster.popInfo({
						type: 'error',
						title: '添加',
						text: '添加 {' + data.adminUser + '} 错误！'
					});
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
app.controller('ffcd05b6-9fcf-4420-939b-e3a3a2a64f1f', ['$scope', '$modalInstance', 'rows', function($scope, $modalInstance, rows) {
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
		modalInstance.result.then(function(rows) {
			$log.info('选择结果:', rows);
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
//管理员详情弹框控制
app.controller('4a2d21ee', ['$scope', '$modal', '$log', function($scope, $modal, $log) {
	$scope.show = function(row) {
		$scope.$parent.get(row, function(data) {
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