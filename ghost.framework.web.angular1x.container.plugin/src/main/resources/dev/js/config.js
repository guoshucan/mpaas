// config
var app = angular.module('app').config(
		['$controllerProvider', '$compileProvider', '$filterProvider', '$provide', '$translateProvider',
			function($controllerProvider, $compileProvider, $filterProvider, $provide, $translateProvider) {
				//
				// lazy controller, directive and service
				app.controller = $controllerProvider.register;
				app.directive = $compileProvider.directive;
				app.filter = $filterProvider.register;
				app.factory = $provide.factory;
				app.service = $provide.service;
				app.constant = $provide.constant;
				app.value = $provide.value;
				app.translateProvider = $translateProvider;
			}
		])
	.config(['$translateProvider', function($translateProvider) {
		// Register a loader for the static files
		// So, the module will search missing translation tables under the specified urls.
		// Those urls are [prefix][langKey][suffix].
		$translateProvider.useStaticFilesLoader({
			prefix: 'i18n/',
			suffix: '.json'
		});
		// Tell the module what language to use by default
		//$translateProvider.preferredLanguage('en');
		$translateProvider.preferredLanguage('zh_CN');
		// Tell the module to store the language in the local storage
		$translateProvider.useLocalStorage();
	}])
	//初始化拦截器
	.factory("httpInterceptor", ["$q", "$rootScope", '$translate', function($q, $rootScope, $translate) {
		return {
			request: function(config) {
				//判断是否带指定区域
				if($translate.use()) {
					config.headers["Accept-Language"] = $translate.use().replace('_', '-');
				}
				// config.headers["Accept-Language"] = $translate.use().replace('_','-');
				// do something on request success
				return config || $q.when(config);
			},
			requestError: function(rejection) {
				// do something on request error
				return $q.reject(rejection)
			},
			response: function(response) {
				// do something on response success
				return response || $q.when(response);
			},
			responseError: function(rejection) {
				// do something on response error
				return $q.reject(rejection);
			}
		};
	}])
	//注册拦截器
	.config(function($httpProvider) {
		$httpProvider.interceptors.push('httpInterceptor');
	});
app
    .constant('$GLOBAL_MESSAGE', 'e645035f-3858-4b91-8dd1-adc6205d4330')
	.constant('$SERVER_API_ADDRESS', 'http://127.0.0.1:8081/api/')
	.constant('$SERVER_ADDRESS', 'http://127.0.0.1:8081/');