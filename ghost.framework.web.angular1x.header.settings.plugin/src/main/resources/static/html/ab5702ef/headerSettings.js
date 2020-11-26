angular.module('app').controller('72029f30', ['$scope', '$http', '$state', '$rootScope', '$compile', '$translate',
    function($s, $h, $st, $rs, $c, $t) {
        $s.i18n = {
            layoutSettings: "布局设置",
            selectThemeColor:"请选择颜色主题",
            container:"盒模型布局",
            asideDock:"顶部停靠侧边栏",
            asideFolded:"折叠侧边栏",
            asideFixed:"冻结侧边栏",
            headerFixed:"冻结顶部"
        };
        //初始化类型
        $s.initLocale = function() {
            app.getJsonObject("ab5702ef/i18n/" + $t.use() + ".json", function(r) {
                if(r) {
                    $s.i18n = r;
                }
            }, function(r) {
            });
        };
        $s.init = function() {
            $s.initLocale();
            //区域变动监听
            $rs.$on('$translateChangeSuccess', function() {
                console.log($t.use());
                $s.initLocale();
            })
            console.log('$s.init = function(72029f30)');
        };
    }]);