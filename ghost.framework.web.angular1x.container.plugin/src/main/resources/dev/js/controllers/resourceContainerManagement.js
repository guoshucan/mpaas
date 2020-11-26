app.controller('63a9e086-2f82-44ca-896c-85305ecf552d', ['$scope', '$http', '$state', '$rootScope', '$compile', '$modal',
	function($scope, $http, $state, $rootScope, $compile, locals, $modal) {
		//初始信息
		$scope.data = {
			id: "",
			name: "",
			status: false,
			soTimeout: "0",
			connectTimeout: "0",
			endpoint: "",
			userName: "",
			accessKeyId: "",
			accessKeySecret: "",
			bucket: "",
			region: "",
			trafficLimit: 0
		};
		$scope.tableContent = true;
		//添加内容
		$scope.addFastDFSContent = false;
		$scope.addHadoopHDFSContent = false;
		$scope.addAliyunOssContent = false;
		$scope.addTencentCloudCosContent = false;
		$scope.addMinioContent = false;
		//修改内容
		$scope.editFastDFSContent = false;
		$scope.editHadoopHDFSContent = false;
		$scope.editAliyunOssContent = false;
		$scope.editTencentCloudCosContent = false;
		$scope.editMinioContent = false;
		$scope.deleteAllDisabled = true;
		$scope.selectRequest = {
			key: null,
			startTime: null,
			endTime: null,
			status: "-1",
			provider: "-1",
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
			var sr = localStorage.getItem("784903c8-ea08-441f-b965-7bc37a4b45c4");
			if(sr != undefined && sr != null) {
				$scope.selectRequest = JSON.parse(sr);
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
				localStorage.removeItem("784903c8-ea08-441f-b965-7bc37a4b45c4");
				$scope.selectRequest.key = "";
				$scope.selectRequest.startTime = null;
				$scope.selectRequest.endTime = null;
				$scope.selectRequest.status = "-1";
				$scope.selectRequest.provider = "-1";
				$scope.selectRequest.start = 1;
			}
			localStorage.setItem("784903c8-ea08-441f-b965-7bc37a4b45c4", JSON.stringify($scope.selectRequest));
			$scope.select();
		};
		//更新状态
		$scope.statusUpdate = function(row) {
			//获取数据
			$http.get(app.apiAddress + "8993afcf-ca2f-4475-bfe6-5b242bcb1a08?id=" + row.id + "&status=" + (row.status == 1 ? 0 : 1))
				.then(function(r) {
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
				}, function(x) {
					$scope.toaster.popInfo({
						type: 'error',
						title: '修改',
						text: '修改 {' + row.name + '} 状态错误！'
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
					all.ids[all.ids.length] = row.id;
				}
			}
			if(all.ids.length == 0) {
				return;
			}
			$http.post(app.apiAddress + "106c4c47-5589-42de-81af-e40322444664", all)
				.then(function(r) {
					if(r.data.code == 0 && r.data.message == "success") {
						$scope.toaster.popInfo({
							type: 'success',
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
		//添加Minio
		$scope.addMinio = function(add) {
			console.log('添加阿里云OSS');
			if(add) {
				//添加数据
				var o = new Object();
				o.name = $scope.data.name;
				o.status = $scope.data.status ? 1 : 0;
				o.endpoint = $scope.data.endpoint;
				o.bucket = $scope.data.bucket;
				o.accessKeyId = $scope.data.accessKeyId;
				o.accessKeySecret = $scope.data.accessKeySecret;
				$scope.addPost("3dc7755f-234d-4540-9008-7837a796c291", o);
				return;
			}
			//清理参数
			$scope.data.name = "";
			$scope.data.status = false;
			$scope.data.endpoint = "";
			$scope.data.bucket = "";
			$scope.data.accessKeyId = "";
			$scope.data.accessKeySecret = "";
			//添加显示
			$scope.hideContent();
			$scope.addMinioContent = true;
		};
		//添加阿里云OSS
		$scope.addAliyunOss = function(add) {
			console.log('添加阿里云OSS');
			if(add) {
				//添加数据
				var o = new Object();
				o.name = $scope.data.name;
				o.status = $scope.data.status ? 1 : 0;
				o.endpoint = $scope.data.endpoint;
				o.bucket = $scope.data.bucket;
				o.accessKeyId = $scope.data.accessKeyId;
				o.accessKeySecret = $scope.data.accessKeySecret;
				$scope.addPost("72234965-4192-46e8-94c0-7e93c188e1f7", o);
				return;
			}
			//清理参数
			$scope.data.name = "";
			$scope.data.status = false;
			$scope.data.endpoint = "";
			$scope.data.bucket = "";
			$scope.data.accessKeyId = "";
			$scope.data.accessKeySecret = "";
			//添加显示
			$scope.hideContent();
			$scope.addAliyunOssContent = true;
		};
		//添加腾讯云COS
		$scope.addTencentCloudCos = function(add) {
			console.log('添加腾讯云COS');
			if(add) {
				//添加数据
				var o = new Object();
				o.name = $scope.data.name;
				o.status = $scope.data.status ? 1 : 0;
				o.endpoint = $scope.data.endpoint;
				o.bucket = $scope.data.bucket;
				o.accessKeyId = $scope.data.accessKeyId;
				o.accessKeySecret = $scope.data.accessKeySecret;
				o.region = $scope.data.region;
				o.trafficLimit = $scope.data.trafficLimit;
				$scope.addPost("0c9f4c61-36f5-4131-aa8b-94ec81432e68", o);
				return;
			}
			//清理参数
			$scope.data.name = "";
			$scope.data.status = false;
			$scope.data.endpoint = "";
			$scope.data.bucket = "";
			$scope.data.accessKeyId = "";
			$scope.data.accessKeySecret = "";
			$scope.data.region = "";
			$scope.data.trafficLimit = 0;
			//添加显示
			$scope.hideContent();
			$scope.addTencentCloudCosContent = true;
		};
		//添加FastDFS
		$scope.addFastDFS = function(add) {
			console.log('添加FastDFS');
			if(add) {
				//添加数据
				var o = new Object();
				o.name = $scope.data.name;
				o.status = $scope.data.status ? 1 : 0;
				o.endpoint = $scope.data.endpoint;
				o.soTimeout = $scope.data.soTimeout;
				o.connectTimeout = $scope.data.connectTimeout;
				$scope.addPost("b37ba8ae-bf4b-4dc9-a00e-8ae5fbd64a3a", o);
				return;
			}
			//清理参数
			$scope.data.name = "";
			$scope.data.status = false;
			$scope.data.endpoint = "";
			$scope.data.soTimeout = "0";
			$scope.data.connectTimeout = "0";
			//添加显示
			$scope.hideContent();
			$scope.addFastDFSContent = true;
		};
		//添加Hadoop HDFS
		$scope.addHdfs = function(add) {
			console.log('添加Hadoop HDFS');
			if(add) {
				//添加数据
				var o = new Object();
				o.name = $scope.data.name;
				o.status = $scope.data.status ? 1 : 0;
				o.endpoint = $scope.data.endpoint;
				o.userName = $scope.data.userName;
				$scope.addPost("c3654c1d-b9f5-454f-b3f1-ac667ebe2e52", o);
				return;
			}
			//清理参数
			$scope.data.name = "";
			$scope.data.status = false;
			$scope.data.endpoint = "";
			$scope.data.userName = "";
			//添加显示
			$scope.hideContent();
			$scope.addHadoopHDFSContent = true;
		};
		//删除
		$scope.delete = function(row) {
			$http.get(app.apiAddress + "2305ad8c-094c-46b9-ac95-8bbbe5f4313f?id=" + row.id)
				.then(function(r) {
					if(r.data.code == 0 && r.data.message == "success") {
						$scope.toaster.popInfo({
							type: 'success',
							title: '删除',
							text: '删除 {' + row.name + '} 完成。'
						});
						$scope.select();
					} else {
						$scope.toaster.popInfo({
							type: 'error',
							title: '删除错误',
							text: '删除 {' + row.name + '} 错误！'
						});
					}
				}, function(x) {
					$scope.toaster.popInfo({
						type: 'error',
						title: '删除错误',
						text: '删除 {' + row.name + '} 错误！'
					});
				});
		};
		//获取列表
		$scope.isSelect = false;
		$scope.select = function() {
			$scope.hideContent();
			$scope.tableContent = true;
			var obj = Object.assign({}, $scope.selectRequest);
			obj.start = obj.start - 1;
			//获取数据
			$http.post(app.apiAddress + "f6be73c4-7e08-48c6-95d8-44f78b972d80", obj)
				.then(function(r) {
					if(r.data.code == 0 && r.data.message == "success") {
						$scope.page.data = r.data.data;
						$scope.page.pages = r.data.pages;
						$scope.page.count = r.data.count;
						//初始化复选状态
						if($scope.isSelect) {
							$scope.isSelect = false;
							if($scope.page.data == null || $scope.page.data.length == 0) {
								var sr = localStorage.getItem("784903c8-ea08-441f-b965-7bc37a4b45c4");
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
								if(row.provider == 0) {
									row.providerName = '阿里云OSS';
								}
								if(row.provider == 1) {
									row.providerName = '腾讯云COS';
								}
								if(row.provider == 2) {
									row.providerName = 'FastDFS';
								}
								if(row.provider == 3) {
									row.providerName = 'Hadoop HDFS';
								}
								if(row.provider == 4) {
									row.providerName = 'Minio';
								}
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
		//测试连接
		$scope.testConnection = function(row) {
			console.log("测试连接:" + row.name);

		};
		//修改
		$scope.edit = function(row) {
			console.log("修改:" + row.name);
			//初始化修改参数
			$scope.hideContent();
			//行数据基础参数
			$scope.data.id = row.id;
			$scope.data.name = row.name;
			$scope.data.endpoint = row.endpoint;
			$scope.data.status = row.status == 1;
			//判断服务商
			if(row.provider == 0) {
				//获取子表参数
				$scope.getCallback("9ede3264-7b6d-4b5b-b0b9-03c8959ca822?id=" + row.id + "&childTable=" + true, function(r) {
					//获取子表参数数据
					$scope.data.accessKeyId = r.data.data.accessKeyId;
					$scope.data.accessKeySecret = r.data.data.accessKeySecret;
					$scope.data.bucket = r.data.data.bucket;
					$scope.editAliyunOssContent = true;
				}, row);
				return;
			}
			if(row.provider == 1) {
				//获取子表参数
				$scope.getCallback("ea5890c9-57f3-461c-bc45-18e6fb4ac2f9?id=" + row.id + "&childTable=" + true, function(r) {
					//获取子表参数数据
					$scope.data.accessKeyId = r.data.data.accessKeyId;
					$scope.data.accessKeySecret = r.data.data.accessKeySecret;
					$scope.data.bucket = r.data.data.bucket;
					$scope.data.region = r.data.data.region;
					$scope.data.trafficLimit = r.data.data.trafficLimit;
					$scope.editTencentCloudCosContent = true;
				}, row);
				return;
			}
			if(row.provider == 2) {
				//获取子表参数
				$scope.getCallback("ec549b5a-3fef-4525-9e55-1b41ca7781e6?id=" + row.id + "&childTable=" + true, function(r) {
					//获取子表参数数据
					$scope.data.soTimeout = r.data.data.soTimeout;
					$scope.data.connectTimeout = r.data.data.connectTimeout;
					$scope.editFastDFSContent = true;
				}, row);
				return;
			}
			if(row.provider == 3) {
				//获取子表参数
				$scope.getCallback("1a088d23-2b45-4444-aed6-c045b0a6f112?id=" + row.id + "&childTable=" + true, function(r) {
					//获取子表参数数据
					$scope.data.userName = r.data.data.userName;
					$scope.editHadoopHDFSContent = true;
				}, row);
				return;
			}
			if(row.provider == 4) {
				//获取子表参数
				$scope.getCallback("db2b7064-caa3-4143-a25b-f58221cea346?id=" + row.id + "&childTable=" + true, function(r) {
					//获取子表参数数据
					$scope.editMinioContent = true;
				}, row);
				return;
			}
		};
		/**
		 * 获取数据回调
		 * @param {String} url 获取api地址
		 * @param {Object} complete 完成回调函数
		 * @param {Object} row 行数据
		 */
		$scope.getCallback = function(url, complete, row) {
			//获取子表参数
			$http.get(app.apiAddress + url)
				.then(function(r) {
					if(r.data.code == 0 && r.data.message == "success") {
						complete(r);
					} else {
						$scope.toaster.popInfo({
							type: 'error',
							title: '读取错误',
							text: '读取 {' + row.name + '} 错误！'
						});
					}
				}, function(x) {
					$scope.toaster.popInfo({
						type: 'error',
						title: '读取错误',
						text: '读取 {' + row.name + '} 错误！'
					});
				});
		};
		//修改Minio
		$scope.editMinio = function() {
			var o = new Object();
			o.id = $scope.data.id;
			o.name = $scope.data.name;
			o.status = $scope.data.status ? 1 : 0;
			o.endpoint = $scope.data.endpoint;
			o.accessKeyId = $scope.data.accessKeyId;
			o.accessKeySecret = $scope.data.accessKeySecret;
			o.bucket = $scope.data.bucket;
			//提交数据
			$scope.editPost("dc20bea9-64c4-47cb-adda-c741f3c251d8", o);
		};
		//修改阿里云oss
		$scope.editAliyunOss = function() {
			var o = new Object();
			o.id = $scope.data.id;
			o.name = $scope.data.name;
			o.status = $scope.data.status ? 1 : 0;
			o.endpoint = $scope.data.endpoint;
			o.accessKeyId = $scope.data.accessKeyId;
			o.accessKeySecret = $scope.data.accessKeySecret;
			o.bucket = $scope.data.bucket;
			//提交数据
			$scope.editPost("348a1d8f-0568-4d01-89d0-503369747a07", o);
		};
		//修改腾讯云cos
		$scope.editTencentCloudCos = function() {
			var o = new Object();
			o.id = $scope.data.id;
			o.name = $scope.data.name;
			o.status = $scope.data.status ? 1 : 0;
			o.endpoint = $scope.data.endpoint;
			o.region = $scope.data.region;
			o.trafficLimit = $scope.data.trafficLimit;
			o.accessKeyId = $scope.data.accessKeyId;
			o.accessKeySecret = $scope.data.accessKeySecret;
			o.bucket = $scope.data.bucket;
			//提交数据
			$scope.editPost("c9707bfe-3c6f-4852-901b-dd6971efe17d", o);
		};
		//修改Hdfs
		$scope.editHdfs = function() {
			var o = new Object();
			o.id = $scope.data.id;
			o.name = $scope.data.name;
			o.status = $scope.data.status ? 1 : 0;
			o.endpoint = $scope.data.endpoint;
			o.userName = $scope.data.userName;
			//提交数据
			$scope.editPost("a4dbdb2b-3008-4a9b-9c7d-becf5624cfb7", o);
		};
		/**
		 * 修改post
		 * @param {String} url 修改api地址
		 * @param {Object} o 修改对象
		 */
		$scope.editPost = function(url, o) {
			$http.post(app.apiAddress + url, o)
				.then(function(r) {
					if(r.data.code == 0 && r.data.message == "success") {
						$scope.toaster.popInfo({
							type: 'info',
							title: '修改',
							text: '修改 {' + o.name + '} 完成。'
						});
						$scope.select();
					} else if(r.data.code == 1) {
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
				}, function(x) {
					$scope.toaster.popInfo({
						type: 'error',
						title: '修改',
						text: '修改 {' + o.name + '} 错误！'
					});
				});
		};
		/**
		 * 添加post
		 * @param {Object} url 添加api地址
		 * @param {Object} o 添加对象
		 */
		$scope.addPost = function(url, o) {
			$http.post(app.apiAddress + url, o)
				.then(function(r) {
					if(r.data.code == 0 && r.data.message == "success") {
						$scope.toaster.popInfo({
							type: 'info',
							title: '添加',
							text: '添加 {' + o.name + '} 完成。'
						});
						$scope.select();
					} else if(r.data.code == 1) {
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
				}, function(x) {
					$scope.toaster.popInfo({
						type: 'error',
						title: '添加',
						text: '添加 {' + o.name + '} 错误！'
					});
				});
		};
		//修改FastDFS
		$scope.editFastDFS = function() {
			var o = new Object();
			o.id = $scope.data.id;
			o.name = $scope.data.name;
			o.status = $scope.data.status ? 1 : 0;
			o.endpoint = $scope.data.endpoint;
			o.soTimeout = $scope.data.soTimeout;
			o.connectTimeout = $scope.data.connectTimeout;
			//提交数据
			$scope.editPost("27ad259a-e7c3-4090-888d-290ad9443eb6", o);
		};
		/**
		 * 隐藏内容
		 */
		$scope.hideContent = function() {
			$scope.tableContent = false;
			//隐藏添加
			$scope.addFastDFSContent = false;
			$scope.addHadoopHDFSContent = false;
			$scope.addAliyunOssContent = false;
			$scope.addTencentCloudCosContent = false;
			$scope.addMinioContent = false;
			//隐藏修改
			$scope.editFastDFSContent = false;
			$scope.editHadoopHDFSContent = false;
			$scope.editAliyunOssContent = false;
			$scope.editTencentCloudCosContent = false;
			$scope.editMinioContent = false;
		};
		//返回
		$scope.back = function() {
			$scope.hideContent();
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
//资源服务器详情弹框控制
app.controller('52703cfb-2d25-4811-9f01-99429dfdf698', ['$scope', '$modal', '$log', function($scope, $modal, $log) {
	$scope.show = function(row) {
		$log.info(row);
		//判断服务商
		if(row.provider == 0) {
			//获取子表参数
			$scope.getCallback("9ede3264-7b6d-4b5b-b0b9-03c8959ca822?id=" + row.id + "&childTable=" + false, function(r) {
				$log.info(r);
				var data = r.data.data;
				data.title = "阿里云";
				$scope.showContent(data);
			}, row);
			return;
		}
		if(row.provider == 1) {
			//获取子表参数
			$scope.getCallback("ea5890c9-57f3-461c-bc45-18e6fb4ac2f9?id=" + row.id + "&childTable=" + false, function(r) {
				$log.info(r);
				var data = r.data.data;
				data.title = "腾讯云";
				$scope.showContent(data);
			}, row);
			return;
		}
		if(row.provider == 2) {
			//获取子表参数
			$scope.getCallback("ec549b5a-3fef-4525-9e55-1b41ca7781e6?id=" + row.id + "&childTable=" + false, function(r) {
				$log.info(r);
				var data = r.data.data;
				data.title = "FastDFS";
				$scope.showContent(data);
			}, row);
			return;
		}
		if(row.provider == 3) {
			//获取子表参数
			$scope.getCallback("1a088d23-2b45-4444-aed6-c045b0a6f112?id=" + row.id + "&childTable=" + false, function(r) {
				$log.info(r);
				var data = r.data.data;
				data.title = "Hadoop HDFS";
				$scope.showContent(data);
			}, row);
			return;
		}
		if(row.provider == 4) {
			//获取子表参数
			$scope.getCallback("db2b7064-caa3-4143-a25b-f58221cea346?id=" + row.id + "&childTable=" + false, function(r) {
				$log.info(r);
				var data = r.data.data;
				data.title = "Minio";
				$scope.showContent(data);
			}, row);
			return;
		}
	};
	$scope.showContent = function(data) {
		data.status = data.status == 1;
		if(data.provider == 0) {
			data.providerName = '阿里云OSS';
		}
		if(data.provider == 1) {
			data.providerName = '腾讯云COS';
		}
		if(data.provider == 2) {
			data.providerName = 'FastDFS';
		}
		if(data.provider == 3) {
			data.providerName = 'Hadoop HDFS';
		}
		if(data.provider == 4) {
			data.providerName = 'Minio';
		}
		var modalInstance = $modal.open({
			templateUrl: '79ab6d65-76b3-4311-9c54-9c0c304271b8',
			controller: 'f79e3a7b',
			size: '{width:680px;max-width:1024px;}',
			resolve: {
				rows: function() {
					return data;
				}
			}
		});
		modalInstance.result.then(function(data) {}, function() {
			$log.info('模式对话框关闭时间:', new Date())
		});
	};
}]);