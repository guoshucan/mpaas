$(function() {
	pvuv = echarts.init(document.getElementById('pvuv'));
	statusDiv = echarts.init(document.getElementById('statusDiv'));
	browser = echarts.init(document.getElementById('browser'));
	httpReferer = echarts.init(document.getElementById('httpReferer'));

})

function delAll() {
	if (confirm(logStr.clearAll)) {
		$.ajax({
			type: 'GET',
			url: ctx + '/adminPage/log/delAll',
			dataType: 'json',

			success: function(data) {
				if (data.success) {
					location.reload();
				} else {
					layer.msg(data.msg);
				}
			},
			error: function() {
				layer.closeAll();
				layer.alert(commonStr.errorInfo);
			}
		});
	}

}

function detail(id) {
	$.ajax({
		type: 'GET',
		url: ctx + '/adminPage/log/detail',
		dataType: 'json',
		data: {
			id: id
		},
		success: function(data) {
			if (data.success) {
				showContent(JSON.parse(data.obj.json))
			} else {
				layer.msg(data.msg);
			}
		},
		error: function() {
			layer.closeAll();
			layer.alert(commonStr.errorInfo);
		}
	});
}


function del(id) {
	if (confirm(commonStr.confirmDel)) {
		$.ajax({
			type: 'GET',
			url: ctx + '/adminPage/log/del',
			dataType: 'json',
			data: {
				id: id
			},
			success: function(data) {
				if (data.success) {
					location.reload();
				} else {
					layer.msg(data.msg);
				}
			},
			error: function() {
				layer.closeAll();
				layer.alert(commonStr.errorInfo);
			}
		});
	}
}

var pvuv, statusDiv, browser, httpReferer;
function showContent(dataGroup) {
	// 请求状态占比
	var option = {
		title: {
			text: logStr.status,
			left: 'center'
		},
		tooltip: {
			trigger: 'item',
			formatter(params) {
				const item = params.data;
				return item.name + commonStr.status + ": " + item.value;
			},
		},
		series: [{
			type: 'pie',
			radius: '55%',
			data: dataGroup.status,
			label: {
				formatter: '{b}' + commonStr.status + ' : {c} ({d}%)'
			}
		}]
	};

	statusDiv.setOption(option);
	// 系统占比
	option = {
		title: {
			text: logStr.browser,
			left: 'center'
		},
		tooltip: {
			trigger: 'item'
		},
		series: [{
			type: 'pie',
			radius: '55%',
			data: dataGroup.browser,
			label: {
				formatter: '{b} : {c} ({d}%)'
			},
		}]
	};

	browser.setOption(option);

	// pv uv统计
	var hour = [];
	var pv = [];
	var uv = [];
	for (var i = 0; i < 24; i++) {
		hour.push(i);

		var hasPv = false;
		for (var j = 0; j < dataGroup.pv.length; j++) {
			if (parseInt(dataGroup.pv[j].name) == i) {
				pv.push(dataGroup.pv[j].value);
				hasPv = true;
			} 
		}
		if(!hasPv){
			pv.push(0);
		}
		
		var hasUv = false;
		for (var j = 0; j < dataGroup.uv.length; j++) {
			if (parseInt(dataGroup.uv[j].name) == i) {
				uv.push(dataGroup.uv[j].value);
				hasUv = true;
			}
		}
		if(!hasUv){
			uv.push(0);
		}
	}

	option = {
		title: {
			text: logStr.pvuv,
			left: 'center'
		},
		tooltip: {
			trigger: 'axis',
			formatter(params) {
				return `
	            	${params[0].name}时<br>
	            	pv: ${params[0].value}<br>
	            	uv: ${params[1].value}
	            `;
			},
		},
		xAxis: {
			name: logStr.hour,
			type: 'category',
			data: hour
		},
		yAxis: {
			type: 'value'
		},
		series: [{
			name: 'pv',
			data: pv,
			type: 'line',
			showBackground: true,
			backgroundStyle: {
				color: 'rgba(108,80,243,0.3)'
			}
		}, {
			name: 'uv',
			data: uv,
			type: 'line',
			showBackground: true,
			backgroundStyle: {
				color: 'rgba(0,202,149,0.3)'
			}
		}

		]
	};

	pvuv.setOption(option);


	// 域名统计
	var names = [];
	var values = [];
	for (var i = 0; i < dataGroup.httpReferer.length; i++) {
		names.push(dataGroup.httpReferer[i].name);
		values.push(dataGroup.httpReferer[i].value);
	}

	option = {
		title: {
			text: logStr.httpReferer,
			left: 'center'
		},
		tooltip: {
			trigger: 'axis',
			axisPointer: {
				type: 'shadow'
			}
		},
		xAxis: {
			type: 'value'
		},
		yAxis: {
			type: 'category',
			data: names
		},
		grid: { // 控制图的大小，调整下面这些值就可以，
			x: 150// x的值可以空值y轴与label标签的距离，效果如下图：
		},
		series: [{
			data: values,
			type: 'bar',
			showBackground: true,
			backgroundStyle: {
				color: 'rgba(220, 220, 220, 0.8)'
			}
		}]
	};

	httpReferer.setOption(option);

	// 弹出框
	layer.open({
		type: 1,
		title: logStr.statistics,
		area: ['1150px', '700px'], // 宽高
		content: $('#windowDiv')
	});
}


function analysis() {
	if (confirm(logStr.analysisStart)) {
		layer.load();
		$.ajax({
			type: 'GET',
			url: ctx + '/adminPage/log/analysis',
			dataType: 'json',
			success: function(data) {
				if (data.success) {
					location.reload();
				} else {
					layer.msg(data.msg);
				}
			},
			error: function() {
				layer.closeAll();
				layer.alert(commonStr.errorInfo);
			}
		});
	}

}
