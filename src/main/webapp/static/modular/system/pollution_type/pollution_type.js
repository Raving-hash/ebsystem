/**
 * 角色管理的单例
 */
var PollutionType = {
    id: "PollutionTypeTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
PollutionType.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '名称', field: 'name', align: 'center', valign: 'middle', sortable: false},
        {title: '单位', field: 'unit', align: 'center', valign: 'middle', sortable: false},
        {title: '阈值', field: 'limitvalue', align: 'center', valign: 'middle', sortable: true}
    ]
    return columns;
};


/**
 * 检查是否选中
 */
PollutionType.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        PollutionType.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加污染类型
 */
PollutionType.openAddPollutionType = function () {
    var index = layer.open({
        type: 2,
        title: '添加污染类型',
        area: ['800px', '450px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/pollution/type/getAdd'
    });
    this.layerIndex = index;
};

/**
 * 点击修改按钮时
 */
PollutionType.openChangePollutionType = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '修改角色',
            area: ['800px', '450px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/pollution/type/getUpdate/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除角色
 */
PollutionType.delPollutionType = function () {
    if (this.check()) {

        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/pollution/type/delete", function () {
                Feng.success("删除成功!");
                PollutionType.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("pollutionTypeId", PollutionType.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否删除污染类型 " + PollutionType.seItem.name + "?",operation);
    }
};

$(function () {
    var defaultColunms = PollutionType.initColumn();
    var table = new BSTable(PollutionType.id, "/pollution/type/list", defaultColunms);
    table.setPaginationType("client");
    table.init();
    PollutionType.table = table;
});
