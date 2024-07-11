/**
 * 角色管理的单例
 */
var Pollution = {
    id: "PollutionTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Pollution.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '污染类型', field: 'pollTypeName', align: 'center', valign: 'middle', sortable: false},
        {title: '污染值', field: 'value', align: 'center', valign: 'middle', sortable: true},
        {title: '单位', field: 'pollTypeUnit', align: 'center', valign: 'middle', sortable: false},
        {title: '创建时间', field: 'updatetime', align: 'center', valign: 'middle', sortable: true},
        {title: '水源', field: 'waterSourceName', align: 'center', valign: 'middle', sortable: false},
        {title: '水源类型', field: 'waterTypeName', align: 'center', valign: 'middle', sortable: false},
    ]
    return columns;
};


/**
 * 检查是否选中
 */
Pollution.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Pollution.seItem = selected[0];
        return true;
    }
};

Pollution.openSearchByType = function () {
    var index = layer.open({
        type: 2,
        title: '查询',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/pollution/get_search_by_type'
    });
    this.layerIndex = index;
}

Pollution.openSearchBySource = function () {
    var index = layer.open({
        type: 2,
        title: '查询',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/pollution/get_search_by_source'
    });
    this.layerIndex = index;
}
Pollution.submitType = function () {
    var queryData = {};
    queryData['pollutionTypeId'] = $("#pollutionTypeId").val();
    queryData['days'] = $("#days").val();
    queryData['waterSourceId'] = $("#waterSourceId").val();
    var newUrl = "/pollution/selectPollutionsInDays";
    Pollution.table.refresh({query: queryData, url: newUrl});
}



$(function () {
    console.log("开始初始化");
    var defaultColunms = Pollution.initColumn();
    var table = new BSTable(Pollution.id, "/pollution/selectPollutionsInDays?", defaultColunms);
    table.setPaginationType("client");
    table.init();
    Pollution.table = table;
});
