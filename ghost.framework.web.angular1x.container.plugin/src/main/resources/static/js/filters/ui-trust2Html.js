'use strict';
angular.module('app').filter('trust2Html', ['$sce',function($sce) {
	return function(val) {
		return $sce.trustAsHtml(val.replace(/\n/g,"<br/>")); 
	};
}])