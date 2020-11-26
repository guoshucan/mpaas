app.controller('a5034bc0', ['$scope', '$state', '$rootScope', '$compile', '$modal',
	function($scope, $state, $rootScope, $compile, locals, $modal) {
		//初始信息
		$scope.data = {
			logoUrl: null,
			logo: null,
			id: null,
			copyright: null,
			locale: null,
			title: null,
			url: null
		};
		$scope.i18n = {
			logo: null,
			edit: {
				title: "标题：",
				url: "网址：",
				copyright: "版权："
			}
		};
		$scope.broadcast = false;
		$scope.toaster = null;
		$scope.to = {
			type: 'success',
			title: '这是标题',
			text: '这是消息内容'
		};
		$scope.get = function() {
			if($scope.data.locale == null) {
				$scope.data.locale = app.translate.use();
				if($scope.data.locale == undefined) {
					app.translate.use("zh_CN");
					$scope.data.locale = app.translate.use();
				}
			}
			app.get($scope, "1c833c07-bc54-41cf-af0e-cf34c8262570?locale=" + $scope.data.locale, function(r) {
				$scope.data = r.data.data;
			});
		};
		//修改
		$scope.edit = function() {
			var d = new Object();
			d.copyright = $scope.data.copyright;
			d.locale = app.translate.use();
			d.title = $scope.data.title;
			d.url = $scope.data.url;
			app.edit($scope, "e1e1e89a-a070-4878-8a81-afa631f48fe9", d, function(r) {
				$scope.toaster.popInfo({
					type: 'info',
					title: '修改',
					text: '修改完成。'
				});
				//发送全局消息
				$scope.broadcast = true;
				app.rootScope.$broadcast('$translateChangeSuccess', d.locale)
				$scope.broadcast = false;
				$scope.get();
			});
		};
		$scope.uploadLogo = function() {
			console.log("uploadLogo");
		};
		$scope.initLocale = function(){
			app.getJsonObject("b0388959/i18n/" + app.translate.use() + ".json", function(r) {
				$scope.i18n = r;
				$scope.get();
			}, function(r){
				$scope.get();
			});
		};
		$scope.init = function() {
			$scope.initLocale();
			//区域变动监听
			app.rootScope.$on('$translateChangeSuccess', function() {
				if($scope.broadcast) {
					return;
				}
				console.log(app.translate.use());
				$scope.data.locale = app.translate.use();
				$scope.initLocale();
			})
		};
	}
]);