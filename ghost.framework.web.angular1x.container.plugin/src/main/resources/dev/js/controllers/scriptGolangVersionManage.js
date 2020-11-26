angular.module('app').controller('32b34b22', ['$scope', '$http', '$state', '$rootScope', '$compile', '$translate','$rootScope',
	function($scope, $http, $state, $rootScope, $compile, $translate,$rootScope) {
		$scope.i18n = {
			operationMaintenance: "运维",
			error: "错误！",
			version: "版本",
			open: "操作",
			submit: "提交",
			back: "返回",
			repeat: "重复！",
			script: "脚本",
			form: {
				version: {
					name: "版本：",
					placeholder: "请填写版本..."
				}
			},
			add: {
				name: "新增",
				version: "添加版本"
			},
			edit: {
				name: "修改",
				version: "修改版本"
			},
			content: {
				versionManagement: "GoLang版本管理"
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
			id: "",
			version: ""
		};
		$scope.tableContent = true;
		$scope.addContent = false;
		$scope.editContent = false;
		$scope.deleteAllDisabled = true;
		$scope.selectRequest = {
			key: null,
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
			app.getJsonObject("5540b2eb/i18n/scriptGolangVersionManage/" + $translate.use() + ".json", function(r) {
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
			var sr = localStorage.getItem("8f67166f-5176-495d-8354-34cfe70836d4");
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
				localStorage.removeItem("8f67166f-5176-495d-8354-34cfe70836d4");
				$scope.selectRequest.key = "";
				$scope.selectRequest.start = 1;
			}
			localStorage.setItem("8f67166f-5176-495d-8354-34cfe70836d4", JSON.stringify($scope.selectRequest));
			$scope.select();
		};
		//删除全部选中
		$scope.batchDelete = function() {
			app.batchDelete($scope, "c3b59231-7324-44d5-99c4-a4b45627618f", app.getPageIds($scope), function(r) {
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
			app.get($scope, "bf55016f-5311-4458-96d7-6efcc9592af7?id=" + row.id, function(r) {
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
			app.select($scope, "526359f5-c112-437d-a041-62a141399ba4", obj, function(r) {
				$scope.page.data = r.data.data;
				$scope.page.pages = r.data.pages;
				$scope.page.count = r.data.count;
				//初始化复选状态
				if($scope.isSelect) {
					$scope.isSelect = false;
					if($scope.page.data == null || $scope.page.data.length == 0) {
						var sr = localStorage.getItem("8f67166f-5176-495d-8354-34cfe70836d4");
						if(sr != undefined && sr != null) {
							$scope.search(true);
							return;
						}
					}
				}
			});
		};
		//修改
		$scope.edit = function(row) {
			if(row == null) {
				var o = new Object();
				o.id = $scope.data.id;
				o.name = $scope.data.name;
				app.edit($scope, "bc0e3d9b-9e34-4a9a-9768-4b388d0ac0c6", o, function(r) {
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
			}
		};
		//添加
		$scope.add = function() {
			if(!$scope.addContent) {
				$scope.data.name = "";
				$scope.tableContent = false;
				$scope.addContent = true;
				return;
			}
			app.add($scope, "657a572e-58ff-4e25-a8d8-9ebc79019717", {
				name: $scope.data.name
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
			app.get($scope, "d487b33c-4f15-4f61-bbac-4ba6bf994508?id=" + row.id, function(r) {
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