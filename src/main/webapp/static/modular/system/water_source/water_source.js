/**
 * 角色管理的单例
 */
var WaterSource = {
    id: "waterSourceTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
WaterSource.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '名称', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '联系方式', field: 'phone', align: 'center', valign: 'middle', sortable: true},
        ]
    return columns;
};


/**
 * 检查是否选中
 */
WaterSource.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        WaterSource.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加管理员
 */
WaterSource.openAddWaterSource = function () {
    var index = layer.open({
        type: 2,
        title: '添加角色',
        area: ['800px', '450px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/waterSource/getAdd'
    });
    this.layerIndex = index;
};

/**
 * 点击修改按钮时
 */
WaterSource.openChangeWaterSource = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '修改角色',
            area: ['800px', '450px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/waterSource/getEdit/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除角色
 */
WaterSource.delWaterSource = function () {
    if (this.check()) {

        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/waterSource/delete", function () {
                Feng.success("删除成功!");
                WaterSource.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", WaterSource.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否删除水源 " + WaterSource.seItem.name + "?",operation);
    }
};

/**
 * 搜索角色
 */
WaterSource.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    WaterSource.table.refresh({query: queryData});
}

$(function () {
    var defaultColunms = WaterSource.initColumn();
    var table = new BSTable(WaterSource.id, "/waterSource/list", defaultColunms);
    table.setPaginationType("client");
    table.init();
    WaterSource.table = table;
});
