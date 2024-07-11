/**
 * 角色管理的单例
 */
var Device = {
    id: "deviceTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Device.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '名称', field: 'name', align: 'center', valign: 'middle', sortable: false},
        {title: '部门', field: 'deptName', align: 'center', valign: 'middle', sortable: false},
        {title: '创建时间', field: 'createTime', align: 'center', valign: 'middle', sortable: true},
        {title: '维护次数', field: 'repair_count', align: 'center', valign: 'middle', sortable: true},
        {title: '上次维护时间', field: 'repairTime', align: 'center', valign: 'middle', sortable: true},
    ]
    return columns;
};


/**
 * 检查是否选中
 */
Device.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Device.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加污染类型
 */
Device.openAddDevice = function () {
    var index = layer.open({
        type: 2,
        title: '添加设备',
        area: ['800px', '450px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/device/getAdd'
    });
    this.layerIndex = index;
};

/**
 * 点击修改按钮时
 */
Device.openChangeDevice = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '修改设备',
            area: ['800px', '450px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/device/getEdit/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除角色
 */
Device.delDevice = function () {
    if (this.check()) {

        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/device/delete", function () {
                Feng.success("删除成功!");
                Device.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("deviceId", Device.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否删除设备 " + Device.seItem.name + "?",operation);
    }
};

$(function () {
    var defaultColunms = Device.initColumn();
    var table = new BSTable(Device.id, "/device/list", defaultColunms);
    table.setPaginationType("client");
    table.init();
    Device.table = table;
});
 