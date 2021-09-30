google.charts.load('current', { 'packages': ['bar'] });
google.charts.setOnLoadCallback(drawStuff);

google.charts.load('current', { packages: ['corechart', 'bar'] });
google.charts.setOnLoadCallback(drawAxisTickColors);

function drawStuff() {

	var jsonData = $.ajax({
		type: "GET",
		url: "/api/report/best-sold-product",
		dataType: "json",
		async: false
	}).responseText;
	var result = [];
	var productObj = JSON.parse(jsonData);

	for (var i in productObj) {
		result.push(productObj[i]);
	}

	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Name');
	data.addColumn('number', 'Total');

	for (var i = 0; i < result.length; i++) {
		data.addRow([result[i].name, result[i].total]);
	}

	var options = {
		width: 1630,
		legend: { position: 'none' },
		chart: {
			title: 'Best sold product',
			subtitle: ''
		},
		axes: {
			x: {
				0: { side: 'top', label: '' } // Top x-axis.
			}
		},
		bar: { groupWidth: "90%" }
	};

	var chart = new google.charts.Bar(document.getElementById('top_x_div'));
	chart.draw(data, google.charts.Bar.convertOptions(options));
};

function drawAxisTickColors() {
	
	var jsonData = $.ajax({
		type: "GET",
		url: "/api/report/total-money",
		dataType: "json",
		async: false
	}).responseText;
	var result = [];
	var orderObj = JSON.parse(jsonData);
	
	for (var i in orderObj) {
		result.push(orderObj[i]);
	}

	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Date');
	data.addColumn('number', 'Total');
	
	for (var i = 0; i < result.length; i++) {
		data.addRow([result[i].strDate, result[i].total]);
	}

	var options = {
		title: 'Total Money By Date',
		chartArea: { width: '70%' },
		hAxis: {
			title: 'Total Money',
			minValue: 0,
			textStyle: {
				bold: true,
				fontSize: 12,
				color: '#4d4d4d'
			},
			titleTextStyle: {
				bold: true,
				fontSize: 18,
				color: '#4d4d4d'
			}
		},
		vAxis: {
			title: 'Date',
			textStyle: {
				fontSize: 14,
				bold: true,
				color: '#848484'
			},
			titleTextStyle: {
				fontSize: 14,
				bold: true,
				color: '#848484'
			}
		}
	};
	var chart = new google.visualization.BarChart(document.getElementById('chart_div'));
	chart.draw(data, options);
};