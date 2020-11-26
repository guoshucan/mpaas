app.controller('fb70bdfb-6212-4b0d-8a18-67beb3d39b3f', ['$scope', '$http', '$state', '$rootScope', '$compile', '$modal',
    function($scope, $http, $state, $rootScope, $compile, locals, $modal) {
        //初始信息
        $scope.addData = {
            groupName: "",
            status: false,
            icon:"",
            url:"",
            createTime
        };
        $scope.tableContent = true;
        $scope.addContent = false;
        $scope.editContent = false;
        $scope.editRightsShow = false;
        $scope.detailContentShow = false;
        $scope.deleteAllDisabled = true;
        $scope.editRow;
        $scope.selectRequest = {
            key: null,
            startTime: null,
            endTime: null,
            status: "-1",
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
            var sr = localStorage.getItem("2ab76004-4918-447b-99c3-4e2fa00eb650");
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
                localStorage.removeItem("2ab76004-4918-447b-99c3-4e2fa00eb650");
                $scope.selectRequest.key = "";
                $scope.selectRequest.startTime = null;
                $scope.selectRequest.endTime = null;
                $scope.selectRequest.status = "-1";
                $scope.selectRequest.start = 1;
            }
            localStorage.setItem("2ab76004-4918-447b-99c3-4e2fa00eb650", JSON.stringify($scope.selectRequest));
            $scope.select();
        };
        //更新状态
        $scope.statusUpdate = function(row) {
            //获取数据
            $http.get(app.apiAddress + "admin/6703b7ae-3ed3-4802-9fc2-2b5969bceb0f?groupId=" + row.groupId + "&status=" + (row.status == 1 ? 0 : 1))
                .then(function(response) {
                    if(response.data.code == 0 && response.data.message == "success") {
                        $scope.toaster.popInfo({
                            type: 'info',
                            title: '修改',
                            text: '修改 {' + row.groupName + '} 状态完成。'
                        });
                        $scope.select();
                    } else {
                        $scope.toaster.popInfo({
                            type: 'error',
                            title: '修改',
                            text: '修改 {' + row.groupName + '} 状态错误！'
                        });
                    }
                }, function(x) {
                    $scope.toaster.popInfo({
                        type: 'error',
                        title: '修改',
                        text: '修改 {' + row.groupName + '} 状态错误！'
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
                    all.ids[all.ids.length] = row.groupId;
                }
            }
            if(all.ids.length == 0) {
                return;
            }
            $http.post(app.apiAddress + "admin/33dc8b62-e702-48b7-9ed5-61c65f55943a", all)
                .then(function(response) {
                    if(response.data.code == 0 && response.data.message == "success") {
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
        //删除
        $scope.delete = function(row) {
            $http.get(app.apiAddress + "admin/d595c6ea-9350-46e5-a0ac-f3372589acb7?groupId=" + row.groupId)
                .then(function(response) {
                    if(response.data.code == 0 && response.data.message == "success") {
                        $scope.toaster.popInfo({
                            type: 'success',
                            title: '删除',
                            text: '删除 {' + row.groupName + '} 完成。'
                        });
                        $scope.select();
                    } else {
                        $scope.toaster.popInfo({
                            type: 'error',
                            title: '删除错误',
                            text: '删除 {' + row.groupName + '} 错误！'
                        });
                    }
                }, function(x) {
                    $scope.toaster.popInfo({
                        type: 'error',
                        title: '删除错误',
                        text: '删除 {' + row.groupName + '} 错误！'
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
            //获取数据
            $http.post(app.apiAddress + "admin/0d10fb4e-d6a8-4914-a8a4-ccc13f7d0ec0", obj)
                .then(function(response) {
                    if(response.data.code == 0 && response.data.message == "success") {
                        $scope.page.data = response.data.data;
                        $scope.page.pages = response.data.pages;
                        $scope.page.count = response.data.count;
                        //初始化复选状态
                        if($scope.isSelect) {
                            $scope.isSelect = false;
                            if($scope.page.data == null || $scope.page.data.length == 0) {
                                var sr = localStorage.getItem("2ab76004-4918-447b-99c3-4e2fa00eb650");
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
        //修改
        $scope.edit = function(row) {
            if(row == null) {
                var editObg = new Object();
                editObg.status = $scope.editRow.status ? 1 : 0;
                editObg.groupId = $scope.editRow.groupId;
                editObg.groupName = $scope.editRow.groupName;
                //提交数据
                $http.post(app.apiAddress + "admin/1534cf96-90a4-4572-8a5b-d7fd873d3c92", editObg)
                    .then(function(response) {
                        if(response.data.code == 0 && response.data.message == "success") {
                            $scope.toaster.popInfo({
                                type: 'info',
                                title: '修改',
                                text: '修改 {' + editObg.groupName + '} 完成。'
                            });
                            $scope.select();
                        } else if(response.data.code == 1) {
                            $scope.toaster.popInfo({
                                type: 'warning',
                                title: '修改',
                                text: '修改 {' + editObg.groupName + '} 重复！'
                            });
                        } else {
                            $scope.toaster.popInfo({
                                type: 'error',
                                title: '修改',
                                text: '修改 {' + editObg.groupName + '} 错误！'
                            });
                        }
                    }, function(x) {
                        $scope.toaster.popInfo({
                            type: 'error',
                            title: '修改',
                            text: '修改 {' + editObg.groupName + '} 错误！'
                        });
                    });
            } else {
                //初始化修改参数
                $scope.tableContent = false;
                $scope.editContent = true;
                $scope.editRow = row;
                $scope.editRow.status = row.status == 1;
            }
        };
        //添加
        $scope.add = function() {
            if(!$scope.addContent) {
                $scope.addData.groupName = "";
                $scope.addData.status = false;
                $scope.tableContent = false;
                $scope.addContent = true;
                return;
            }
            $http.post(app.apiAddress + "admin/6e6c11d4-6171-4f81-b485-5d8614b106b5", {
                groupName: $scope.addData.groupName,
                status: ($scope.addData.status ? 1 : 0),
            })
                .then(function(response) {
                    if(response.data.code == 0 && response.data.message == "success") {
                        $scope.toaster.popInfo({
                            type: 'success',
                            title: '添加',
                            text: '添加 {' + $scope.addData.groupName + '} 完成。'
                        });
                        $scope.select();
                    } else if(response.data.code == 1) {
                        $scope.toaster.popInfo({
                            type: 'warning',
                            title: '添加',
                            text: '添加 {' + $scope.addData.groupName + '} 重复！'
                        });
                    } else {
                        $scope.toaster.popInfo({
                            type: 'error',
                            title: '添加',
                            text: '添加 {' + $scope.addData.groupName + '} 错误！'
                        });
                    }
                }, function(x) {
                    $scope.toaster.popInfo({
                        type: 'error',
                        title: '添加',
                        text: '添加 {' + $scope.addData.groupName + '} 错误！'
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