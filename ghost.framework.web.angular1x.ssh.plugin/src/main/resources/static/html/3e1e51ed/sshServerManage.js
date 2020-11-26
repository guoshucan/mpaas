angular.module('app').controller('8235d9fe', ['$scope', '$http', '$state', '$rootScope', '$compile', '$timeout', '$translate',
	function($scope, $http, $state, $rootScope, $compile, $timeout, $translate) {
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
			select: "选择",
			cancel: "取消",
			version: "版本",
			supertube: "超管",
			hostName: "地址",
			port: "端口",
			test: "测试",
			testConnection: "测试连接",
			testConnectionFailed: "测试连接失败",
			testConnectionComplete: "测试连接完成",
			console: "控制台",
			fileManagement: "文件管理",
			management: {
				hide: "隐藏管理列表",
				show: "显示管理列表"
			},
			form: {
				name: {
					name: "名称：",
					placeholder: "请填写名称..."
				},
				port: {
					name: "端口：",
					placeholder: "请填写端口..."
				},
				group: {
					name: "组：",
					placeholder: "请选择组..."
				},
				type: {
					name: "类型：",
					placeholder: "请选择类型..."
				},
				account: {
					name: "账号：",
					placeholder: "请选择账号..."
				},
				region: {
					name: "区域：",
					placeholder: "请选择区域..."
				},
				host: {
					name: "主机：",
					placeholder: "请填写主机..."
				},
				timeout: {
					name: "超时：",
					placeholder: "请填写超时..."
				},
				channelTimeout: {
					name: "通道超时：",
					placeholder: "请填写通道超时..."
				},
				remoteDirectory: {
					name: "远程路径：",
					placeholder: "请填写远程路径..."
				},
				createTime: "创建时间：",
				description: {
					name: "描述：",
					placeholder: "请填写描述..."
				},
				status: "是否启用：",
				id: "Id："
			},
			add: {
				name: "新增",
				server: "添加服务器"
			},
			edit: {
				name: "修改",
				server: "修改服务器"
			},
			content: {
				details: "服务器详情",
				serverManagement: "服务器管理"
			},
			group: {
				details: "组"
			},
			type: {
				details: "类型"
			},
			region: {
				details: "区域"
			},
			account: {
				details: "账号",
				select: {
					pleaseSelect: "请选择超管",
					manage: "管理"
				}
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
				pleaseSelect: "请选择...",
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
		$scope.data = {
			id: null,
			name: null,
			status: false,
			groupName: null,
			groupId: null,
			regionId: null,
			regionName: null,
			typeId: null,
			typeName: null,
			accountId: null,
			accountName: null,
			ipAddress: null,
			hostName: null,
			timeout: 30000,
			channelTimeout: 3000,
			remoteDirectory: "/",
			port: 22,
			description: null,
			version: null
		};
		$scope.tableContent = true;
		$scope.addContent = false;
		$scope.editContent = false;
		$scope.deleteAllDisabled = true;
		//服务器声明
		$scope.selectGroup;
		$scope.openSelectGroup;
		//区域声明
		$scope.selectRegion;
		$scope.openSelectRegion;
		//类型声明
		$scope.selectType;
		$scope.openSelectType;
		//账号声明
		$scope.selectAccount;
		$scope.openSelectAccount;
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
			regionName: null,
			typeId: null,
			typeName: null,
			accountId: null,
			accountName: null
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
		//服务器对象
		$scope.group = {};
		//SSH服务器服务器分页控件
		$scope.group.page = {
			data: [],
			count: 0,
			maxSize: 5,
			pages: 0
		};
		//SSH服务器服务器请求
		$scope.group.selectRequest = {
			key: null,
			startTime: null,
			endTime: null,
			status: "-1",
			start: 1,
			length: 10
		};
		//SSH服务器服务器搜索
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
		//SSH服务器服务器获取列表
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
		//类型对象
		$scope.type = {};
		//SSH服务器服务器分页控件
		$scope.type.page = {
			data: [],
			count: 0,
			maxSize: 5,
			pages: 0
		};
		//SSH服务器服务器请求
		$scope.type.selectRequest = {
			key: null,
			startTime: null,
			endTime: null,
			status: "-1",
			start: 1,
			length: 10
		};
		//SSH服务器服务器搜索
		$scope.type.search = function(clean) {
			if(clean) {
				//清理搜索条件
				$scope.type.selectRequest.key = "";
				$scope.type.selectRequest.startTime = null;
				$scope.type.selectRequest.endTime = null;
				$scope.type.selectRequest.status = "-1";
				$scope.type.selectRequest.start = 1;
			}
			$scope.type.select();
		};
		//SSH服务器服务器获取列表
		$scope.type.select = function() {
			//获取数据
			var o = Object.assign({}, $scope.type.selectRequest);
			o.start = o.start - 1;
			app.select($scope, "d4f4beb7-fae6-4368-8a02-ecd2831709d6", o, function(r) {
				$scope.type.page.data = r.data.data;
				$scope.type.page.pages = r.data.pages;
				$scope.type.page.count = r.data.count;
				//初始化复选状态
				if($scope.type.page.data != null) {
					$scope.type.page.data.forEach(row => {
						row.statusCheck = (row.status == 1);
					});
				}
			});
		};
		//分页位置更改
		$scope.type.pageChange = function() {
			console.log('pageChange:' + $scope.type.selectRequest.start);
			$scope.type.select();
		};
		//账号对象
		$scope.account = {};
		$scope.account.add = function() {};
		//SSH服务器服务器分页控件
		$scope.account.page = {
			data: [],
			count: 0,
			maxSize: 5,
			pages: 0
		};
		//SSH服务器服务器请求
		$scope.account.selectRequest = {
			key: null,
			startTime: null,
			endTime: null,
			status: "-1",
			start: 1,
			length: 10,
			root: "-1"
		};
		//SSH服务器服务器搜索
		$scope.account.search = function(clean) {
			if(clean) {
				//清理搜索条件
				$scope.account.selectRequest.key = "";
				$scope.account.selectRequest.startTime = null;
				$scope.account.selectRequest.endTime = null;
				$scope.account.selectRequest.status = "-1";
				$scope.account.selectRequest.start = 1;
				$scope.account.selectRequest.root = "-1"
			}
			$scope.account.select();
		};
		//SSH服务器服务器获取列表
		$scope.account.select = function() {
			//获取数据
			var o = Object.assign({}, $scope.account.selectRequest);
			o.start = o.start - 1;
			if(o.root == '-1') {
				o.root = null;
			} else {
				o.root = o.root == '1';
			}
			app.select($scope, "aa1d3d07-0382-4288-ad27-055a24b2f5cf", o, function(r) {
				$scope.account.page.data = r.data.data;
				$scope.account.page.pages = r.data.pages;
				$scope.account.page.count = r.data.count;
				//初始化复选状态
				if($scope.account.page.data != null) {
					$scope.account.page.data.forEach(row => {
						row.statusCheck = (row.status == 1);
					});
				}
			});
		};
		//分页位置更改
		$scope.account.pageChange = function() {
			console.log('pageChange:' + $scope.account.selectRequest.start);
			$scope.account.select();
		};
		$scope.console = {};
		$scope.console.containerStyle = "padding-top: 0px;";
		$scope.console.list = [];
		$scope.console.close = function(row) {
			$scope.page.data.forEach(r => {
				if(r.id == row.id) {
					r.console = false;
					row.ssh.client.close();
					return;
				}
			});
			console.log('console.close:' + row.id);
			var l = $scope.console.list;
			for(i = 0; i < l.length; i++) {
				if(l[i].id == row.id) {
					l.splice(i, 1);
					break;
				}
			}
			if(l.length == 0 && $scope.isCollapsed) {
				$scope.isCollapsed = false;
			}
		};
		$scope.console.init = function(row) {
			console.log('console.init:' + row.id);
		};
		$scope.console.show = function(row) {
			row.console = true;
			var l = $scope.console.list;
			for(i = 0; i < l.length; i++) {
				if(l[i].id == row.id) {
					return;
				}
			}
			console.log('console.show:' + row.id);
			l.push({
				id: row.id,
				groupName: row.groupName,
				regionName: row.regionName,
				typeName: row.typeName,
				accountName: row.accountName,
				hostName: row.hostName,
				port: row.port,
				show: true,
				full: false,
				init: false
			});
		};
		$scope.console.fileManagement = function(row) {
			console.log('console.fileManagement:' + row.id);
		};
		$scope.console.fullscreen = function(row) {
			console.log('console.fullscreen:' + row.id);
			var fullElem = $('#' + row.id)[0];
			var cs = $('#' + row.id).children();
			cs.eq(0).css("height", "100%");
			cs = cs.children();
			cs.eq(0).css("height", "100%");
			cs.eq(1).css("height", "100%");
			var s = {};
			if(document.webkitFullscreenElement) {
				document.webkitCancelFullScreen();
				s.ctrlBar = true;
				row.full = true;
			} else {
				fullElem.webkitRequestFullscreen();
				s.toggleVideo = function() {
					if(s.ctrlBar) {
						$timeout.cancel(s.ctrlBar);
					}
					s.ctrlBar = $timeout(
						function() {
							s.ctrlBar = false;
							row.full = false;
						},
						5000);
				}
			}
		};
		$scope.collapsedManagement = $scope.i18n.management.show;
		$scope.isCollapsed = false;
		$scope.toaster = null;
		$scope.to = {
			type: 'success',
			title: '这是标题',
			text: '这是消息内容'
		};
		//初始化区域
		$scope.initLocale = function() {
			app.getJsonObject("3e1e51ed/i18n/sshServerManage/" + $translate.use() + ".json", function(r) {
				if(r) {
					$scope.i18n = r;
					if($scope.isCollapsed) {
						$scope.collapsedManagement = $scope.i18n.management.show;
					} else {
						$scope.collapsedManagement = $scope.i18n.management.hide;
					}
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
		//初始化
		$scope.init = function() {
			//控制台列表渲染完成事件
			$scope.$on('repeatHistoryFinish', function() {
				var l = $scope.console.list;
				console.log('console.repeatHistoryFinish:' + l.length);
				for(i = 0; i < l.length; i++) {
					var n = l[i];
					if(!n.init) {
						n.init = true;
						var d = $('#' + n.id);
						console.log('console.repeatHistoryFinish:' + d.id);
						$timeout(function() {
							//ssh
							n.ssh = {};
							n.ssh.options = {
								id: n.id,
								operate: "connect"
							};
							n.ssh.client = new WSSHClient();
							n.ssh.term = new Terminal({
								cols: 67,
								rows: 17,
								cursorBlink: true, // 光标闪烁
								cursorStyle: "block", // 光标样式  null | 'block' | 'underline' | 'bar'
								scrollback: 800, //回滚
								tabStopWidth: 8, //制表宽度
								screenKeys: true
							});
							//							n.ssh.fit = new window.FitAddon.FitAddon();
							//							n.ssh.term.loadAddon(n.ssh.term.fitAddon);
							//							n.ssh.fit = new fit();
							//							n.ssh.term.loadAddon(n.ssh.fit);
							//							n.ssh.fit.fit();
							n.ssh.term.on('data', function(data) {
								//键盘输入时的回调函数
								console.log("send:" + data);
								n.ssh.client.sendClientData(data);
							});
							n.ssh.term.open(d[0]);
							//在页面上显示连接中...
							n.ssh.term.write('Connecting...');
							//执行连接操作
							n.ssh.client.connect({
								onError: function(error) {
									//连接失败回调
									n.ssh.term.write('Error: ' + error + '\r\n');
								},
								onConnect: function() {
									//连接成功回调
									n.ssh.client.sendInitData(n.ssh.options);
								},
								onClose: function() {
									//连接关闭回调
									n.ssh.term.write("\rconnection closed");
								},
								onData: function(data) {
									//收到数据时回调
									console.log("write:" + data);
									n.ssh.term.write(data);
								}
							});
						});
					}
				}
			});
			$scope.group.selectRequest.start = 1;
			$scope.region.selectRequest.start = 1;
			$scope.type.selectRequest.start = 1;
			$scope.account.selectRequest.start = 1;
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
				if($scope.selectRequest.typeId != null) {
					if($scope.selectType == undefined) {
						$scope.selectType = {};
					}
					$scope.selectType.id = $scope.selectRequest.typeId;
					$scope.selectType.name = $scope.selectRequest.typeName;
				}
				if($scope.selectRequest.accountId != null) {
					if($scope.selectAccount == undefined) {
						$scope.selectAccount = {};
					}
					$scope.selectAccount.id = $scope.selectRequest.accountId;
					$scope.selectAccount.name = $scope.selectRequest.accountName;
				}
			}
			$scope.initLocale();
			//区域变动监听
			$rootScope.$on('$translateChangeSuccess', function() {
				console.log($translate.use());
				$scope.initLocale();
			});
			$scope.$watch('isCollapsed', function(n, o) {
				console.log('isCollapsed:' + n.toString());
				if(n) {
					$scope.console.containerStyle = "";
					$scope.collapsedManagement = $scope.i18n.management.show;
				} else {
					$scope.console.containerStyle = "padding-top: 0px;";
					$scope.collapsedManagement = $scope.i18n.management.hide;
				}
			});
			//监听跳转添加
			if($rootScope.params && $rootScope.params.add) {
				delete $rootScope.params.add;
				$scope.add();
				return;
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
		};
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
				$scope.selectType = null;
				$scope.selectAccount = null;
				$scope.selectRequest.start = 1;
				$scope.selectRequest.groupId = null;
				$scope.selectRequest.regionId = null;
				$scope.selectRequest.typeId = null;
				$scope.selectRequest.accountId = null;
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
			if($scope.selectType != null) {
				o.typeId = $scope.selectType.typeId;
				o.typeName = $scope.selectType.typeName;
			}
			if($scope.selectAccount != null) {
				o.accountId = $scope.selectAccount.accountId;
				o.accountName = $scope.selectAccount.accountName;
			}
			localStorage.setItem("d158aba5-8520-4849-b41d-ac5c2fe33e77", JSON.stringify(o));
			$scope.select();
		};
		//更新状态
		$scope.statusUpdate = function(row) {
			app.get($scope, "fdc3811b-4504-46b1-8a61-b6df31b53b37?id=" + row.id + "&status=" + (row.status == 1 ? 0 : 1), function(r) {
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
			var l = app.getPageIds($scope);
			app.batchDelete($scope, "aed4d3d1-a0a3-4286-99cf-debf6a77f449", l, function(r) {
				angular.forEach(l, function(d) {
					$scope.console.close({
						id: d
					});
				});
				$scope.toaster.popInfo({
					type: 'warning',
					title: $scope.i18n.delete.name,
					text: $scope.i18n.delete.complete
				});
				$scope.select();
			});
		};
		//删除
		$scope.delete = function(row) {
			app.delete($scope, "b2062b6e-d4bb-4ac7-9834-a427e0a30aa9?id=" + row.id, function(r) {
				$scope.console.close(row);
				$scope.toaster.popInfo({
					type: 'warning',
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
			//获取数据
			var o = Object.assign({}, $scope.selectRequest);
			o.start = o.start - 1;
			if($scope.selectGroup != null) {
				o.groupId = $scope.selectGroup.id;
			}
			if($scope.selectRegion != null) {
				o.regionId = $scope.selectRegion.id;
			}
			if($scope.selectType != null) {
				o.typeId = $scope.selectType.id;
			}
			if($scope.selectAccount != null) {
				o.accountId = $scope.selectAccount.id;
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
						row.console = false;
						row.test = false;
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
				o.typeId = $scope.openSelectType.id;
				o.typeName = $scope.openSelectType.name;
				o.accountId = $scope.openSelectAccount.id;
				o.accountName = $scope.openSelectAccount.name;
				app.edit($scope, "02c7feab-99a8-4b35-84d0-f7b7c64bc488", o,
					function(r) {
						$scope.toaster.popInfo({
							type: 'info',
							title: $scope.i18n.edit.name,
							text: $scope.i18n.edit.name + ' {' + $scope.data.name + '} ' + $scope.i18n.complete
						});
						$scope.openSelectGroup = null; //重置选择SSH服务器
						$scope.openSelectRegion = null; //重置选择SSH区域
						$scope.openSelectType = null; //重置选择SSH类型
						$scope.openSelectAccount = null; //重置选择SSH账号
						//滚动条到顶部
						window.scrollTo(0, 0);
						$scope.select();
					},
					function(r) {
						if(r.data && r.data.code == 1) {
							$scope.toaster.popInfo({
								type: 'warning',
								title: $scope.i18n.edit.name,
								text: $scope.i18n.edit.name + ' {' + $scope.data.name + '} ' + $scope.i18n.repeat
							});
						} else {
							$scope.toaster.popInfo({
								type: 'error',
								title: $scope.i18n.edit.name,
								text: $scope.i18n.edit.name + ' {' + $scope.data.name + '} ' + $scope.i18n.error
							});
						}
					});
			} else {
				//初始化修改参数
				$scope.tableContent = false;
				$scope.editContent = true;
				app.get($scope, "34d19d4a-bb85-4340-bf39-b9065c6ed934?id=" + row.id, function(r) {
					//服务器
					$scope.openSelectGroup = {
						id: row.groupId,
						name: row.groupName
					};
					//区域
					$scope.openSelectRegion = {
						id: row.regionId,
						name: row.regionName
					};
					//类型
					$scope.openSelectType = {
						id: row.typeId,
						name: row.typeName
					};
					//账户
					$scope.openSelectAccount = {
						id: row.accountId,
						name: row.accountName
					};
					$scope.data = r.data.data;
					$scope.data.status = $scope.data.status == 1;
				});
			}
		};
		//添加
		$scope.add = function() {
			if(!$scope.addContent) {
				//
				$scope.openSelectGroup = null;
				$scope.openSelectType = null;
				$scope.openSelectRegion = null;
				$scope.openSelectAccount = null;
				//
				$scope.data.id = null;
				$scope.data.name = null;
				$scope.data.status = false;
				$scope.data.groupName = null;
				$scope.data.groupId = null;
				$scope.data.regionName = null;
				$scope.data.regionId = null;
				$scope.data.typeName = null;
				$scope.data.typeId = null;
				$scope.data.accountName = null;
				$scope.data.accountId = null;
				$scope.data.ipAddress = null;
				$scope.data.hostName = null;
				$scope.data.timeout = 30000;
				$scope.data.channelTimeout = 3000;
				$scope.data.remoteDirectory = "/";
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
			o.typeId = $scope.openSelectType.id;
			o.accountId = $scope.openSelectAccount.id;
			app.add($scope, "6e8e98ea-1bac-4d7e-892e-a15cdfd3afb3", o, function(r) {
				$scope.toaster.popInfo({
					type: 'success',
					title: '添加',
					text: '添加 {' + o.name + '} 完成。'
				});
				$scope.openSelectGroup = null;
				$scope.openSelectRegion = null;
				$scope.openSelectAccount = null;
				$scope.openSelectType = null;
				//滚动条到顶部
				window.scrollTo(0, 0);
				$scope.select();
			}, function(r) {
				if(r.data && r.data.code == 1) {
					$scope.toaster.popInfo({
						type: 'warning',
						title: $scope.i18n.add.name,
						text: $scope.i18n.add.name + ' {' + o.name + '} ' + $scope.i18n.repeat
					});
				} else {
					$scope.toaster.popInfo({
						type: 'error',
						title: $scope.i18n.add.name,
						text: $scope.i18n.add.name + ' {' + o.name + '} ' + $scope.i18n.error
					});
				}
			});
		};
		//获取数据
		$scope.get = function(row, fun) {
			app.get($scope, "34d19d4a-bb85-4340-bf39-b9065c6ed934?id=" + row.id, function(r) {
				//回调数据
				fun(r.data.data);
			});
		};
		//返回
		$scope.back = function() {
			$scope.addContent = false;
			$scope.editContent = false;
			$scope.tableContent = true;
			window.scrollTo(0, 0);
		};
		//测试连接
		$scope.testConnection = function(row) {
			console.log("测试连接:" + row.name);
			if(!row.test) {
				row.test = true;
			}
			app.get($scope, "3fa73f23-2d27-426b-aabe-4901451225ba?id=" + row.id, function(r) {
				if(r.data.data) {
					$scope.toaster.popInfo({
						type: 'success',
						title: $scope.i18n.testConnection,
						text: $scope.i18n.testConnectionComplete + ' {' + row.name + '->' + row.hostName + ':' + row.port + '}'
					});
					row.test = false;
				} else {
					$scope.toaster.popInfo({
						type: 'error',
						title: $scope.i18n.testConnection,
						text: $scope.i18n.testConnectionFailed + ' {' + row.name + '->' + row.hostName + ':' + row.port + '} ！'
					});
					row.test = false;
				}
			});
		};
	}
]);
//SSH服务器对话框
angular.module('app').controller('52e46d58', ['$scope', '$modalInstance', 'p', function($scope, $modalInstance, p) {
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
//SSH服务器控制器
angular.module('app').controller('f341fa72', ['$scope', '$modal', '$log', '$GLOBAL_MESSAGE', function($scope, $modal, $log, $GLOBAL_MESSAGE) {
	$scope.p;
	$scope.modalInstance;
	$scope.init = function() {
		$scope.p = $scope.$parent;
	};
	$scope.open = function(size) {
		$scope.modalInstance = $modal.open({
			templateUrl: '299137d7',
			controller: '52e46d58',
			size: size,
			resolve: {
				p: function() {
					return $scope;
				}
			}
		});
		$scope.p.group.selectRequest.status = '-1';
		$scope.p.group.selectRequest.start = 1;
		$scope.p.group.add = function() {
			console.log("add");
			$scope.modalInstance.close('ok');
			$scope.$emit($GLOBAL_MESSAGE, {
				go: 'app.45a0ebd7',
				params: {
					add: true
				}
			});
		};
	};
	//选中SSH服务器服务器
	$scope.click = function(row) {
		console.log("click:" + row.name);
		$scope.modalInstance.close(row);
		//判断是否为修改SSH服务器选择SSH服务器服务器信息
		if($scope.p.addContent || $scope.p.editContent) {
			//搜索SSH服务器服务器选择
			$scope.p.openSelectGroup = row;
		} else {
			//添加或修改SSH服务器服务器选择
			$scope.p.selectGroup = row;
		}
	};
}]);
//SSH区域对话框
angular.module('app').controller('833a9302', ['$scope', '$modalInstance', 'p', function($scope, $modalInstance, p) {
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
//SSH区域控制器
angular.module('app').controller('701dd22f', ['$scope', '$modal', '$log', '$GLOBAL_MESSAGE', function($scope, $modal, $log, $GLOBAL_MESSAGE) {
	$scope.p;
	$scope.modalInstance;
	$scope.init = function() {
		$scope.p = $scope.$parent;
	};
	$scope.open = function(size) {
		$scope.modalInstance = $modal.open({
			templateUrl: '77dfbc63',
			controller: '833a9302',
			size: size,
			resolve: {
				p: function() {
					return $scope;
				}
			}
		});
		$scope.p.region.selectRequest.status = '-1';
		$scope.p.region.selectRequest.start = 1;
		$scope.p.region.add = function() {
			console.log("add");
			$scope.modalInstance.close('ok');
			$scope.$emit($GLOBAL_MESSAGE, {
				go: 'app.198c76b0',
				params: {
					add: true
				}
			});
		};
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
//SSH类型对话框
angular.module('app').controller('9e22b51d', ['$scope', '$modalInstance', 'p', function($scope, $modalInstance, p) {
	$scope.p = p;
	//加载数据
	$scope.p.type.select(); //获取数据
	$scope.ok = function() {
		console.log("ok");
		$modalInstance.close('ok');
	};
	$scope.cancel = function() {
		console.log("cancel");
		$modalInstance.dismiss('cancel');
	};
}]);
//SSH类型控制器
angular.module('app').controller('d13205c9', ['$scope', '$modal', '$state', '$log', '$GLOBAL_MESSAGE', function($scope, $modal, $state, $log, $GLOBAL_MESSAGE) {
	$scope.p;
	$scope.modalInstance;
	$scope.init = function() {
		$scope.p = $scope.$parent;
	};
	$scope.open = function(size) {
		$scope.modalInstance = $modal.open({
			templateUrl: '1353ca3e',
			controller: '9e22b51d',
			size: size,
			resolve: {
				p: function() {
					return $scope;
				}
			}
		});
		$scope.p.type.selectRequest.status = '-1';
		$scope.p.type.selectRequest.start = 1;
		$scope.p.type.add = function() {
			console.log("add");
			$scope.modalInstance.close('ok');
			$scope.$emit($GLOBAL_MESSAGE, {
				go: 'app.21317a37',
				params: {
					add: true
				}
			});
		};
	};
	//选中SSH服务器区域
	$scope.click = function(row) {
		console.log("click:" + row.name);
		$scope.modalInstance.close(row);
		//判断是否为修改类型信息
		if($scope.p.addContent || $scope.p.editContent) {
			//搜索类型选择
			$scope.p.openSelectType = row;
		} else {
			//添加或修改类型选择
			$scope.p.selectType = row;
		}
	};
}]);
//SSH账号对话框
angular.module('app').controller('0ebd32a3', ['$scope', '$modalInstance', 'p', function($scope, $modalInstance, p) {
	$scope.p = p;
	//加载数据
	$scope.p.account.select(); //获取数据
	$scope.cancel = function() {
		console.log("cancel");
		$modalInstance.dismiss('cancel');
	};

}]);
//SSH账号控制器
angular.module('app').controller('7889e66b', ['$scope', '$modal', '$state', '$log', '$GLOBAL_MESSAGE', function($scope, $modal, $state, $log, $GLOBAL_MESSAGE) {
	$scope.p;
	$scope.modalInstance;
	$scope.init = function() {
		$scope.p = $scope.$parent;
	};
	$scope.open = function(size) {
		$scope.modalInstance = $modal.open({
			templateUrl: '8657fa2e',
			controller: '0ebd32a3',
			size: size,
			resolve: {
				p: function() {
					return $scope;
				}
			}
		});
		$scope.p.account.selectRequest.status = '-1';
		$scope.p.account.selectRequest.start = 1;
		$scope.p.account.add = function() {
			console.log("add");
			$scope.modalInstance.close('ok');
			$scope.$emit($GLOBAL_MESSAGE, {
				go: 'app.7d2a444c',
				params: {
					add: true
				}
			});
		};
	};
	//选中SSH服务器区域
	$scope.click = function(row) {
		console.log("click:" + row.name);
		$scope.modalInstance.close(row);
		//判断是否为修改账号信息
		if($scope.p.addContent || $scope.p.editContent) {
			//搜索账号选择
			$scope.p.openSelectAccount = row;
		} else {
			//添加或修改账号选择
			$scope.p.selectAccount = row;
		}
	};
}]);