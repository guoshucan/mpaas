angular.module('app').controller('e097c6f3', ['$scope', '$http', '$state', '$rootScope', '$compile', '$translate', '$rootScope',
	function($scope, $http, $state, $rootScope, $compile, $translate, $rootScope) {
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
			script: "脚本",
			name: "名称",
			type: "类型",
			status: "状态",
			date: "日期",
			open: "操作",
			submit: "提交",
			back: "返回",
			repeat: "重复！",
			statusComplete: "状态完成。",
			show: "查看",
			form: {
				name: {
					name: "名称：",
					placeholder: "请填写名称..."
				},
				description: {
					name: "描述：",
					placeholder: "请填写描述..."
				},
				status: "是否启用：",
				content: "脚本内容：",
				type: "脚本类型：",
				python:"Python版本：",
				golnag:"GoLnag版本：",
				java: "Java版本："
			},
			add: {
				name: "新增",
				script: "添加脚本",
			},
			edit: {
				name: "修改",
				script: "修改脚本"
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
				details: "脚本详情",
				scriptManagement: "脚本管理"
			},
			search: {
				pleaseSelectStatus: "请选择状态",
				pleaseSelectScriptType: "请选择脚本类型",
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
			id: null,
			name: null,
			status: false,
			description: null,
			pythonVersionId: null,
			pythonVersion: null,
			golangVersionId: null,
			golangVersion: null,
			javaVersionId: null,
			javaVersion: null,
			content: null,
			type: 0
		};
		$scope.pythonList = [];
		$scope.golangList = [];
		$scope.javaList = [];
		$scope.addEditor;
		$scope.editor;
		$scope.tableContent = true;
		$scope.addContent = false;
		$scope.editContent = false;
		$scope.deleteAllDisabled = true;
		$scope.selectRequest = {
			key: null,
			startTime: null,
			endTime: null,
			status: "-1",
			type: "-1",
			start: 1,
			length: "10",
			javaVersionId: null,
			golangVersionId: null,
			pythonVersionId: null
		};
		$scope.clearSelectRequestVersion = function() {
			var r = $scope.selectRequest;
			r.javaVersionId = null;
			r.javaVersion = null;
			r.golangVersionId = null;
			r.golangVersion = null;
			r.pythonVersionId = null;
			r.pythonVersion = null;
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
			app.getJsonObject("5540b2eb/i18n/scriptManage/" + $translate.use() + ".json", function(r) {
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
			var sr = localStorage.getItem("8525e043-0f57-49dc-b70f-9606059ba9c5");
			if(sr != undefined && sr != null) {
				$scope.selectRequest = JSON.parse(sr);
			}
			$scope.initLocale();
			//区域变动监听
			$rootScope.$on('$translateChangeSuccess', function() {
				console.log($translate.use());
				$scope.initLocale();
			});
			$scope.$watch('data.type', function(newVal, oldVal) {
				var g = '// Prime Sieve in Go.\n';
				g += '// Taken from the Go specification.\n';
				g += '// Copyright © The Go Authors.\n';
				g += 'package main\n';
				g += 'import "fmt"';
				var j = "import com.demo\n";
				j += 'public class Java{\n';
				j += '    public Java(){\n';
				j += '    }\n';
				j += '}';
				var p = "#!/usr/bin/env python";
				var s = "#!/bin/sh";
				var a = $scope.addEditor;
				var e = $scope.editor;
				if(newVal == 0) {
					if(e) {
						e.options.mode = "shell";
						e.options.lineNumbers = true;
						e.options.matchBrackets = true;
						if(e.getValue() == "" || e.getValue() == p || e.getValue() == g || e.getValue() == j) {
							e.setValue(s);
						}
					}
					if(a) {
						a.options.mode = "shell";
						a.options.lineNumbers = true;
						a.options.matchBrackets = true;
						if(a.getValue() == "" || a.getValue() == p || a.getValue() == g || a.getValue() == j) {
							a.setValue(s);
						}
					}
					return;
				}
				if(newVal == 1) {
					if($scope.pythonList.length > 0) {
						$scope.data.pythonVersion = $scope.pythonList[0].name;
						$scope.data.pythonVersionId = $scope.pythonList[0].id;
					}
					if(e) {
						e.options.mode = "python";
						e.options.version = $scope.data.pythonVersion;
						e.options.singleLineStringErrors = false;
						e.options.lineNumbers = true;
						e.options.indentUnit = 4;
						e.options.matchBrackets = true;
						if(e.getValue() == "" || e.getValue() == s || e.getValue() == j || e.getValue() == g) {
							e.setValue(p);
						}
					}
					if(a) {
						a.options.mode = "python";
						a.options.version = $scope.data.pythonVersion;
						a.options.singleLineStringErrors = false;
						a.options.lineNumbers = true;
						a.options.indentUnit = 4;
						a.options.matchBrackets = true;
						if(a.getValue() == "" || a.getValue() == s || a.getValue() == j || a.getValue() == g) {
							a.setValue(p);
						}
					}
					return;
				}
				if(newVal == 2) {
					if($scope.golangList.length > 0) {
						$scope.data.golangVersion = $scope.golangList[0].name;
						$scope.data.golangVersionId = $scope.golangList[0].id;
					}
					if(e) {
						e.options.mode = "text/x-go";
						e.options.matchBrackets = true;
						e.options.indentUnit = 8;
						e.options.tabSize = 8;
						e.options.indentWithTabs = true;
						if(e.getValue() == "" || e.getValue() == s || e.getValue() == p || e.getValue() == j) {
							e.setValue(g);
						}
					}
					if(a) {
						a.options.mode = "text/x-go";
						a.options.matchBrackets = true;
						a.options.indentUnit = 8;
						a.options.tabSize = 8;
						a.options.indentWithTabs = true;
						if(a.getValue() == "" || a.getValue() == s || a.getValue() == p || a.getValue() == j) {
							a.setValue(g);
						}
					}
					return;
				}
				if(newVal == 3) {
					if($scope.javaList.length > 0) {
						$scope.data.javaVersion = $scope.javaList[0].name;
						$scope.data.javaVersionId = $scope.javaList[0].id;
					}
					if(e) {
						e.options.mode = "text/x-java";
						e.options.lineNumbers = true;
						e.options.matchBrackets = true;
						if(e.getValue() == "" || e.getValue() == s || e.getValue() == p || e.getValue() == g) {
							e.setValue(j);
						}
					}
					if(a) {
						a.options.mode = "text/x-java";
						a.options.lineNumbers = true;
						a.options.matchBrackets = true;
						if(a.getValue() == "" || a.getValue() == s || a.getValue() == p || a.getValue() == g) {
							a.setValue(j);
						}
					}
					return;
				}
			}, true);
			$scope.$watch('data.pythonVersionId', function(n, o) {
				if($scope.addEditor) {
					angular.forEach($scope.pythonList, function(item) {
						if(item.id == n) {
							$scope.addEditor.options.version = item.name;
							return;
						}
					});
				}
			}, true);
			$scope.$watch('data.golangVersionId', function(n, o) {
				if($scope.addEditor) {
					angular.forEach($scope.golangList, function(item) {
						if(item.id == n) {
							$scope.addEditor.options.version = item.name;
							return;
						}
					});
				}
			}, true);
			$scope.$watch('data.javaVersionId', function(n, o) {
				if($scope.addEditor) {
					angular.forEach($scope.javaList, function(item) {
						if(item.id == n) {
							$scope.addEditor.options.version = item.name;
							return;
						}
					});
				}
			}, true);
			$scope.$watch('selectRequest.type', function(n, o) {
				$scope.clearSelectRequestVersion();
				if(n == -1 || n == 0) {
					return;
				}
				if(n == 1) {
					if($scope.pythonList.length > 0) {
						$scope.selectRequest.pythonVersion = $scope.pythonList[0].name;
						$scope.selectRequest.pythonVersionId = $scope.pythonList[0].id;
					}
				}
				if(n == 2) {
					if($scope.golangList.length > 0) {
						$scope.selectRequest.golangVersion = $scope.golangList[0].name;
						$scope.selectRequest.golangVersionId = $scope.golangList[0].id;
					}
				}
				if(n == 3) {
					if($scope.javaList.length > 0) {
						$scope.selectRequest.javaVersion = $scope.javaList[0].name;
						$scope.selectRequest.javaVersionId = $scope.javaList[0].id;
					}
				}
			});
			app.get($scope, "01bfbccb-7983-436c-880e-b8ec386236a6", function(r) {
				//回调数据
				if(r.data.data) {
					$scope.pythonList = r.data.data;
					if($scope.pythonList.length > 0) {
						$scope.data.pythonVersionId = r.data.data[0].id;
						$scope.data.pythonVersion = r.data.data[0].name;
					}
				}
			});
			app.get($scope, "4f5a1819-e115-4c44-9eed-db3fb6ce84d2", function(r) {
				//回调数据
				if(r.data.data) {
					$scope.golangList = r.data.data;
					if($scope.golangList.length > 0) {
						$scope.data.golangVersionId = r.data.data[0].id;
						$scope.data.golangVersion = r.data.data[0].name;
					}
				}
			});
			app.get($scope, "95da696c-2804-4217-953d-d067b92138f5", function(r) {
				//回调数据
				if(r.data.data) {
					$scope.javaList = r.data.data;
					if($scope.javaList.length > 0) {
						$scope.data.javaVersionId = r.data.data[0].id;
						$scope.data.javaVersion = r.data.data[0].name;
					}
				}
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
				localStorage.removeItem("8525e043-0f57-49dc-b70f-9606059ba9c5");
				var r = $scope.selectRequest;
				r.key = null;
				r.startTime = null;
				r.endTime = null;
				r.status = "-1";
				r.type = "-1";
				r.start = 1;
				r.golangVersionId = null;
				r.golangVersion = null;
				r.javaVersionId = null;
				r.javaVersion = null;
				r.pythonVersionId = null;
				r.pythonVersion = null;
			}
			localStorage.setItem("8525e043-0f57-49dc-b70f-9606059ba9c5", JSON.stringify(r));
			$scope.select();
		};
		//更新状态
		$scope.statusUpdate = function(row) {
			app.get($scope, "1c78f9bc-bbf8-46d9-a9df-1e9320ed5572?id=" + row.id + "&status=" + (row.status == 1 ? 0 : 1), function(r) {
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
			app.batchDelete($scope, "fb9c9e77-6107-44ba-ac0a-01ef1ff72ed9", app.getPageIds($scope), function(r) {
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
			app.get($scope, "f3c5ee68-3c0c-43b6-9b0a-10489b04b260?id=" + row.id, function(r) {
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
			var o = Object.assign({}, $scope.selectRequest);
			o.start = o.start - 1;
			app.select($scope, "1c25c1c8-e315-4dea-892e-73f9aedf984f", o, function(r) {
				$scope.page.data = r.data.data;
				$scope.page.pages = r.data.pages;
				$scope.page.count = r.data.count;
				//初始化复选状态
				if($scope.isSelect) {
					$scope.isSelect = false;
					if($scope.page.data == null || $scope.page.data.length == 0) {
						var sr = localStorage.getItem("8525e043-0f57-49dc-b70f-9606059ba9c5");
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
		$scope.initEditor = function(row) {
			if(!$scope.editor) {
				$scope.editor = CodeMirror.fromTextArea($("#script_edit_code")[0], {
					lineNumbers: true, //是否显示行号
					mode: "shell",
					　 //默认脚本编码
					lineWrapping: true, //是否强制换行
				});
				$scope.editor.setOption("theme", "erlang-dark");
				$scope.editor.setSize('auto', 'auto');
			}
			$scope.editor.setValue(row.content);
			$scope.refresh($scope.editor);
		};
		/**
		 * 刷新显示内容
		 * @param {Object} e 编辑器
		 */
		$scope.refresh = function(e) {
			setTimeout(function() {
				e.refresh();
			}, 1);
		};
		//修改
		$scope.edit = function(row) {
			if(row == null) {
				var o = new Object();
				o.status = $scope.data.status ? 1 : 0;
				o.id = $scope.data.id;
				o.pythonVersionId = $scope.data.pythonVersionId;
				o.golangVersionId = $scope.data.golangVersionId;
				o.javaVersionId = $scope.data.javaVersionId;
				o.type = $scope.data.type;
				o.name = $scope.data.name;
				o.description = $scope.data.description;
				o.content = $scope.editor.getValue();
				app.edit($scope, "99b6ab8f-715b-4dc4-a0dc-8741135c5fc4", o, function(r) {
					$scope.toaster.popInfo({
						type: 'info',
						title: '修改',
						text: '修改 {' + o.name + '} 完成。'
					});
					$scope.select();
					$scope.editor.setValue("");
					window.scrollTo(0, 0);
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
				$scope.get(row, function(d) {
					$scope.tableContent = false;
					$scope.editContent = true;
					$scope.data = d;
					$scope.data.status = d.status == 1;
					$scope.initEditor(d);
				});
			}
		};
		$scope.initAddEditor = function() {
			if(!$scope.addEditor) {
				$scope.addEditor = CodeMirror.fromTextArea($("#script_add_code")[0], { //script_once_code为你的textarea的ID号
					lineNumbers: true, //是否显示行号
					mode: "shell",
					　 //默认脚本编码
					lineWrapping: true, //是否强制换行
				});
				$scope.addEditor.setOption("theme", "erlang-dark"); //editor.setOption()为codeMirror提供的设置风格的方法
				$scope.addEditor.setSize('auto', 'auto');
			}
			$scope.addEditor.setValue("#!/bin/sh");
			$scope.refresh($scope.addEditor);
		};
		//添加
		$scope.add = function() {
			if(!$scope.addContent) {
				$scope.data.id = null;
				$scope.data.name = null;
				$scope.data.status = false;
				$scope.data.content = null;
				//				$scope.data.pythonVersionId = null;
				//				$scope.data.pythonVersion = null;
				//				$scope.data.golangVersionId = null;
				//				$scope.data.golangVersion = null;
				//				$scope.data.javaVersionId = null;
				//				$scope.data.javaVersion = null;
				$scope.data.type = 0;
				$scope.data.description = null;
				$scope.tableContent = false;
				$scope.addContent = true;
				$scope.initAddEditor();
				return;
			}
			app.add($scope, "50f247b3-1530-4bb7-a660-7c171006d2b7", {
				name: $scope.data.name,
				description: $scope.data.description,
				status: $scope.data.status ? 1 : 0,
				type: $scope.data.type,
				pythonVersionId: $scope.data.pythonVersionId,
				golangVersionId: $scope.data.golangVersionId,
				javaVersionId: $scope.data.javaVersionId,
				content: $scope.addEditor.getValue()
			}, function(r) {
				$scope.toaster.popInfo({
					type: 'success',
					title: '添加',
					text: '添加 {' + $scope.data.name + '} 完成。'
				});
				$scope.select();
				$scope.addEditor.setValue("");
				window.scrollTo(0, 0);
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
			app.get($scope, "41610841-35c3-4ee0-afe8-682eb9e5856c?id=" + row.id, function(r) {
				//回调数据
				fun(r.data.data);
			});
		};
		//返回
		$scope.back = function() {
			if($scope.addEditor) {
				$scope.addEditor.setValue("");
			}
			if($scope.editor) {
				$scope.editor.setValue("");
			}
			window.scrollTo(0, 0);
			$scope.addContent = false;
			$scope.editContent = false;
			$scope.tableContent = true;
		};
	}
]);