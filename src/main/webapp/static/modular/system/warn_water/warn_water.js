var WarnWater = {};

WarnWater.refreshWarnTable = function () {
    d3.select("#myChart").selectAll("*").remove();

    var ajax = new $ax(Feng.ctxPath + "/notice/getWarnCount",
        function (data) {
            //var data1 = $.parseJSON(data);
            var valueDay1 = data['day1']
            var valueDay7 = data['day7']
            var valueDay30 = data['day30']

            var dataInput = [valueDay1, valueDay7, valueDay30];

            // 2. 创建SVG元素
            var chart = d3.select("#myChart")
                .append("svg")
                .attr("width", 350)
                .attr("height", 280);

            chart.append("g")
                .attr("transform", "translate(0, 0)")
                .append("rect")
                .attr("x", 0)
                .attr("y", 0)
                .attr("width", 350)
                .attr("height", 280)
                .style("stroke", "steelblue")
                .style("fill", "none")
                .style("stroke-width", "2px");

            // 添加一个<g>元素用于居中图表内容
            var chartGroup = chart.append("g")
                .attr("transform", "translate(50, 50)");

            var x = d3.scaleBand()
                .domain(dataInput.map(function (d, i) {
                    return i;
                }))
                .range([0, 300])
                .padding(0.5);

            var y = d3.scaleLinear()
                .domain([0, d3.max(dataInput)])
                .range([200, 0]);

            var xAxis = d3.axisBottom(x)
                .tickValues(dataInput.map(function (d, i) {
                    return i;
                }))
                .tickFormat(function (i) {
                    if (i === 0) {
                        return "近一日";
                    } else if (i === 1) {
                        return "近一周";
                    } else {
                        return "近一月";
                    }
                });

            var yAxis = d3.axisLeft(y)
                .tickFormat(function (d) {
                    return d;
                })
                .ticks(5);

            chartGroup.selectAll(".bar")
                .data(dataInput)
                .enter()
                .append("rect")
                .attr("class", "bar") // add class attribute
                .attr("x", function (d, i) {
                    return x(i) - 20;
                })
                .attr("y", function (d) {
                    return y(d);
                })
                .attr("height", function (d) {
                    return 200 - y(d);
                })
                .attr("width", x.bandwidth())
                .style('fill', 'steelblue')
                .on("mouseover", function (d, i) {
                    d3.select(this).style("fill", "orange");
                    chart.append("text")
                        .attr("class", "value")
                        .attr("x", 50)
                        .attr("y", 40)
                        .attr("text-anchor", "middle")
                        .text(i + '次')
                        .style("fill", "steelblue")
                        .style("font-size", "13px");
                })
                .on("mouseout", function (d, i) {
                    d3.select(this).style("fill", "steelblue");
                    chart.select(".value").remove();
                });


            chartGroup.append("g")
                .attr("transform", "translate(0," + 200 + ")")
                .call(xAxis)
                .selectAll("text")
                .style("text-anchor", "end")
                .attr("dx", "-.8em")
                .attr("dy", ".15em");

            chartGroup.append("g")
                .call(yAxis)
                .selectAll("text")
                .style("text-anchor", "end")
                .attr("dx", "-.8em")
                .attr("dy", ".15em");

            chartGroup.append("g")
                .append("text")
                .attr("fill", "steelblue")
                .attr("transform", "translate(0," + 80 + ") rotate(-90)")
                .attr("y", 6)
                .attr("dy", "0.71em")
                .attr("text-anchor", "end")
                .text("数量")
                .style("font-size", "12px");

            chartGroup.append("text")
                .attr("x", 120)
                .attr("y", -30)
                .attr("text-anchor", "middle")
                .text("报警数量统计")
                .style("font-size", "18px")
                .style("font-weight", "bold")
                .style("fill", "black");

        }, function (data) {
            //请求失败时执行该函数
            layer.msg('服务器开了点小差，请稍后刷新页面!', {icon: 0});
        });

    ajax.start()
}

WarnWater.refreshRankChart = function () {
    d3.selectAll("#rankings").selectAll("*").remove();
    var ajax = new $ax(Feng.ctxPath + "/notice/getWarnWaterSourceRank" + "?day=7",
            function (data) {
                var dataInput = [];
                for (var i = 0; i < data.length; i++) {
                    var item = {
                        waterSourceName: data[i].waterSourceName,
                        count: data[i].count,
                        waterSourceId: data[i].water_source_id,
                    };
                    dataInput.push(item);
                }

                // 将数据按照count属性进行排序
                dataInput.sort(function (a, b) {
                    return b.count - a.count;
                });

// 创建一个包含所有水源名称的数组
                var waterSourceNames = dataInput.map(function (d) {
                    return d.waterSourceName;
                });

                var margin = {top: 40, right: 20, bottom: 20, left: 20},
                    width = 600 - margin.left - margin.right,
                    height = 300 - margin.top - margin.bottom;

// 创建svg元素
                var svg = d3.select("#rankings")
                    .append("svg")
                    .attr("width", width + margin.left + margin.right)
                    .attr("height", height + margin.top + margin.bottom);

// 创建一个矩形元素作为排行榜的背景
                var background = svg.append("rect")
                    .attr("x", margin.left)
                    .attr("y", margin.top)
                    .attr("width", width)
                    .attr("height", height + 20)
                    .style("fill", "#f3f3f4")
                    .style("stroke", "steelblue")
                    .style("stroke-width", "1px");

// 添加标题
                svg.append("text")
                    .attr("x", margin.left + width / 2)
                    .attr("y", margin.top / 2)
                    .attr("text-anchor", "middle")
                    .text("水源报警排行榜(7日)")
                    .style("font-size", "18px")
                    .style("font-weight", "bold");

// 定义y轴的线性比例尺
                var yScale = d3.scaleLinear()
                    .domain([1, dataInput.length]) // 输入的数据范围
                    .range([margin.top + 30, margin.top + height - 30]); // 输出的像素坐标范围

// 在左侧显示水源名称和排名
                svg.selectAll("text.label")
                    .data(dataInput)
                    .enter()
                    .append("text")
                    .attr("class", "label")
                    .text(function (d, i) {
                        return (i + 1) + ". " + d.waterSourceName;
                    })
                    .attr("x", margin.left + 10)
                    .attr("y", function (d, i) {
                        return yScale(i + 1) + 20; // 减小偏移量，使文本更紧凑
                    })
                    .style("font-size", "15px") // 减小字体大小
                    .style("font-weight", "bold")
                    .style("fill", "steelblue");

// 在右侧显示排名条和对应的数值
                // 获取dataInput的最大值
                var maxCount = d3.max(dataInput, function (d) {
                    return d.count;
                });
                var unit = 300 / maxCount;
                svg.selectAll("g.bar-group")
                    .data(dataInput)
                    .enter()
                    .append("g")
                    .attr("class", "bar-group")
                    .attr("transform", function(d, i) {
                        return "translate(" + (margin.left + 160) + "," + (yScale(i + 1) + 5) + ")";
                    })
                    .on("mouseover", function() {
                        d3.select(this).select(".value")
                            .style("display", "block");
                        d3.select(this).select(".bar")
                            .style("fill", "orange");
                    })
                    .on("mouseout", function() {
                        d3.select(this).select(".value")
                            .style("display", "none");
                        d3.select(this).select(".bar")
                            .style("fill", "#69b3a2");
                    })
                    .append("rect")
                    .attr("class", "bar")
                    .attr("width", function(d) {
                        return d.count * unit; // 一个计数值对应1像素宽度的条形
                    })
                    .attr("height", 20) // 减小排名条高度
                    .style("fill", "#69b3a2");

                svg.selectAll("g.bar-group")
                    .append("text")
                    .attr("class", "value")
                    .text(function(d) {
                        return d.count + "次";
                    })
                    .attr("x", function(d) {
                        return d.count * unit + 5; // 在条形的末尾显示数值
                    })
                    .attr("y", 15) // 将文本垂直居中
                    .style("font-size", "14px")
                    .style("display", "none") // 初始时不显示
                    .style("fill", "steelblue");
            },
            function (data) {
                //请求失败时执行该函数
                layer.msg('服务器开了点小差，请稍后刷新页面!', {icon: 0});
            }
        )
    ;

    ajax.start()

}
$(function () {
    // 请求/notice/getWarnCount , 获取数据初始化Echarts
    var intervalTime = 10 * 1000;
    WarnWater.refreshWarnTable()
    setInterval(WarnWater.refreshWarnTable, intervalTime)
    WarnWater.refreshRankChart()
    setInterval(WarnWater.refreshRankChart, intervalTime)
});


