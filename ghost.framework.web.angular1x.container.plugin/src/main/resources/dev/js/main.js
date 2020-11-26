'use strict';
app.controller('AppCtrl', ['$scope', '$translate', '$localStorage', '$window',
	'$rootScope', '$http', '$interpolate', 'tmhDynamicLocale', '$log', '$state',
	'$SERVER_API_ADDRESS', '$SERVER_ADDRESS', '$GLOBAL_MESSAGE',
	function($scope, $translate, $localStorage, $window,
		$rootScope, $http, $interpolate, tmhDynamicLocale, $log, $state,
		$SERVER_API_ADDRESS, $SERVER_ADDRESS, $GLOBAL_MESSAGE) {
		// add 'ie' classes to html
		var isIE = !!navigator.userAgent.match(/MSIE/i);
		if(isIE) {
			angular.element($window.document.body).addClass('ie');
		}
		if(isSmartDevice($window)) {
			angular.element($window.document.body).addClass('smart')
		};
		// config
		$scope.app = {
			name: 'Angulr UI',
			version: '2.2.0',
			copyright: "",
			// for chart colors
			color: {
				primary: '#7266ba',
				info: '#23b7e5',
				success: '#27c24c',
				warning: '#fad733',
				danger: '#f05050',
				light: '#e8eff0',
				dark: '#3a3f51',
				black: '#1c2b36'
			},
			settings: {
				themeID: 1,
				navbarHeaderColor: 'bg-black',
				navbarCollapseColor: 'bg-white-only',
				asideColor: 'bg-black',
				headerFixed: true,
				asideFixed: false,
				asideFolded: false,
				asideDock: false,
				container: false
			}
		}
		// save settings to local storage
		if(angular.isDefined($localStorage.settings)) {
			$scope.app.settings = $localStorage.settings;
		} else {
			$localStorage.settings = $scope.app.settings;
		}
		$scope.$watch('app.settings', function() {
			if($scope.app.settings.asideDock && $scope.app.settings.asideFixed) {
				// aside dock and fixed must set the header fixed.
				$scope.app.settings.headerFixed = true;
			}
			// for box layout, add background image
			$scope.app.settings.container ? angular.element('html').addClass('bg') : angular.element('html').removeClass('bg');
			// save to local storage
			$localStorage.settings = $scope.app.settings;
		}, true);
		app.rootScope = $rootScope;
		// angular translate
		app.translate = $translate;
		//区域变动监听
		$rootScope.$on('$translateChangeSuccess', function() {
			var l = app.translate.use().replace('_', '-').toLowerCase();
			console.log("angular-locale_" + l);
			tmhDynamicLocale.set(l);
		});
		$rootScope.$on('$localeChangeSuccess', function($locale, localeId) {
			console.log("angular-locale_" + localeId);
			console.log($locale);
			var t = app.translateProvider.translations(localeId);
			console.log(t);
		});
		//监听跳转
		$rootScope.$on($GLOBAL_MESSAGE, function(event, data) {
			$log.log(data);
			if(data.params) {
				$rootScope.params = data.params;
			} else {
				delete $rootScope.params;
			}
			$state.go(data.go);
		});
		//区域变动监听
		$rootScope.$on('$translateChangeSuccess', function() {
			console.log(app.translate.use());
			//获取配置信息
			app.get(null, "1c833c07-bc54-41cf-af0e-cf34c8262570?locale=" + $translate.use(), function(r) {
				console.log(r);
				document.title = r.data.data.title;
				$scope.app.name = r.data.data.title;
				$scope.app.copyright = r.data.data.copyright;
			}, function(r) {});
		});
		$scope.lang = {
			isopen: false
		};
		$scope.langs = {
			en: 'English',
			de_DE: 'German',
			it_IT: 'Italian',
			zh_CN: '简体中文',
			zh_TW: '繁體中文',
			ko: 'Korea'
		};
		$scope.selectLang = $scope.langs[$translate.proposedLanguage()] || "简体中文";
		$scope.setLang = function(langKey, $event) {
			// set the current lang
			$scope.selectLang = $scope.langs[langKey];
			// You can change the language during runtime
			$translate.use(langKey);
			$scope.lang.isopen = !$scope.lang.isopen;
		};

		function isSmartDevice($window) {
			// Adapted from http://www.detectmobilebrowsers.com
			var ua = $window['navigator']['userAgent'] || $window['navigator']['vendor'] || $window['opera'];
			// Checks for iOs, Android, Blackberry, Opera Mini, and Windows mobile devices
			return(/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test(ua);
		};
		/**
		 * 获取json内容
		 * @param {Object} api
		 * @param {fun} callback
		 * @param {fun} errorCallback
		 */
		app.getJsonObject = function(api, callback, errorCallback) {
			$http({
					method: 'get',
					timeout: 1000,
					url: ($SERVER_ADDRESS + api)
				})
				.then(function(r) {
						callback(r.data);
					},
					function(x) {
						if(errorCallback == undefined) {
							console.log(x);
						} else {
							errorCallback(x);
						}
					});
		};
		/**
		 * 获取数据
		 * @param {Object} scope $scope
		 * @param {String} api 
		 * @param {fun} callback
		 * @param {fun} errorCallback
		 */
		app.get = function(scope, api, callback, errorCallback) {
			$http.get($SERVER_API_ADDRESS + api)
				.then(function(r) {
						if(r.data.code == 0 && r.data.message == "success") {
							callback(r);
						} else {
							if(errorCallback != undefined) {
								errorCallback(r);
							} else {
								scope.toaster.popInfo({
									type: 'error',
									title: '获取',
									text: '获取错误！'
								});
							}
						}
					},
					function(x) {
						if(errorCallback == undefined) {
							scope.toaster.popInfo({
								type: 'error',
								title: '获取',
								text: '获取错误！'
							});;
						} else {
							errorCallback(x);
						}
					});
		};
		/**
		 * 批量删除
		 * @param {Object} scope $scope
		 * @param {String} api
		 * @param {Object} list
		 * @param {fun} callback
		 * @param {fun} errorCallback
		 */
		app.batchDelete = function(scope, api, list, callback, errorCallback) {
			$http.post($SERVER_API_ADDRESS + api, list)
				.then(function(r) {
					if(r.data.code == 0 && r.data.message == "success") {
						if(callback == undefined) {
							scope.toaster.popInfo({
								type: 'success',
								title: '删除',
								text: '删除完成。'
							});
						} else {
							callback(r);
						}
					} else {
						if(errorCallback == undefined) {
							scope.toaster.popInfo({
								type: 'error',
								title: '删除错误',
								text: '删除错误！'
							});
						} else {
							errorCallback(r);
						}
					}
				}, function(x) {
					if(errorCallback == undefined) {
						scope.toaster.popInfo({
							type: 'error',
							title: '删除错误',
							text: '删除错误！'
						});
					} else {
						errorCallback(x);
					}
				});
		};
		/**
		 * 删除
		 * @param {Object} scope $scope
		 * @param {String} api 
		 * @param {fun} callback
		 * @param {fun} errorCallback
		 */
		app.delete = function(scope, api, callback, errorCallback) {
			$http.get($SERVER_API_ADDRESS + api)
				.then(function(r) {
						if(r.data.code == 0 && r.data.message == "success") {
							if(callback == undefined) {
								scope.toaster.popInfo({
									type: 'success',
									title: '删除',
									text: '删除 完成。'
								});
							} else {
								callback(r);
							}
						} else {
							if(errorCallback == undefined) {
								scope.toaster.popInfo({
									type: 'error',
									title: '删除错误',
									text: '删除错误！'
								});
							} else {
								errorCallback(r);
							}
						}
					},
					function(x) {
						if(errorCallback == undefined) {
							scope.toaster.popInfo({
								type: 'error',
								title: '删除错误',
								text: '删除 错误！'
							});
						} else {
							errorCallback(x);
						}
					});
		};
		/**
		 * 获取列表
		 * @param {Object} scope $scope
		 * @param {String} api
		 * @param {Object} data
		 * @param {fun} callback
		 * @param {fun} errorCallback
		 */
		app.select = function(scope, api, data, callback, errorCallback) {
			$http.post($SERVER_API_ADDRESS + api, data)
				.then(function(r) {
					if(r.data.code == 0 && r.data.message == "success") {
						callback(r);
					} else {
						if(errorCallback == undefined) {
							scope.toaster.popInfo({
								type: 'error',
								title: '错误',
								text: '获取列表错误！'
							});
						} else {
							errorCallback(r);
						}
					}
				}, function(x) {
					if(errorCallback == undefined) {
						scope.toaster.popInfo({
							type: 'error',
							title: '错误',
							text: '获取列表错误！'
						});
					} else {
						errorCallback(x);
					}
				});
		};
		/**
		 * 修改
		 * @param {Object} scope $scope
		 * @param {String}api
		 * @param {Object} data
		 * @param {fun} callback
		 * @param {fun} errorCallback
		 */
		app.edit = function(scope, api, data, callback, errorCallback) {
			$http.post($SERVER_API_ADDRESS + api, data)
				.then(function(r) {
					if(r.data.code == 0 && r.data.message == "success") {
						if(callback == undefined) {
							scope.toaster.popInfo({
								type: 'info',
								title: '修改',
								text: '修改 完成。'
							});
						} else {
							callback(r);
						}
					} else {
						if(errorCallback == undefined) {
							scope.toaster.popInfo({
								type: 'warning',
								title: '修改',
								text: '修改 错误！'
							});
						} else {
							errorCallback(r);
						}
					}
				}, function(x) {
					if(errorCallback == undefined) {
						scope.toaster.popInfo({
							type: 'error',
							title: '修改',
							text: '修改错误！'
						});
					} else {
						errorCallback(x);
					}
				});
		};
		/**
		 * 添加
		 * @param {Object} scope $scope
		 * @param {String} api
		 * @param {Object} data
		 * @param {fun} callback
		 * @param {fun} errorCallback
		 */
		app.add = function(scope, api, data, callback, errorCallback) {
			$http.post($SERVER_API_ADDRESS + api, data)
				.then(function(r) {
					if(r.data.code == 0 && r.data.message == "success") {
						if(callback == undefined) {
							scope.toaster.popInfo({
								type: 'info',
								title: '添加',
								text: '添加完成。'
							});
						} else {
							callback(r);
						}
					} else {
						if(errorCallback == undefined) {
							scope.toaster.popInfo({
								type: 'warning',
								title: '添加',
								text: '添加 错误！'
							});
						} else {
							errorCallback(r);
						}
					}
				}, function(x) {
					if(errorCallback == undefined) {
						scope.toaster.popInfo({
							type: 'error',
							title: '添加',
							text: '添加错误！'
						});
					} else {
						errorCallback(x);
					}
				});
		};
		/**
		 * 获取分页选择的ids
		 * @param {Object} scope $scope
		 */
		app.getPageIds = function(scope) {
			var all = new Object();
			all.ids = [];
			//遍历是否有选中
			var row;
			for(var i = 0; i < scope.page.data.length; i++) {
				row = scope.page.data[i];
				if(row.check) {
					all.ids[all.ids.length] = row.id;
				}
			}
			return all;
		};
	}
]);